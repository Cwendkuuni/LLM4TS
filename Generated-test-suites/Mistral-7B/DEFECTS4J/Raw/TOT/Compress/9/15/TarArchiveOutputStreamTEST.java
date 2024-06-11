import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    private File outputFile;

    @Before
    public void setUp() throws Exception {
        outputFile = new File("test.tar");
    }

    @After
    public void tearDown() throws Exception {
        if (outputFile.exists()) {
            IOUtils.deleteQuietly(outputFile);
        }
    }

    @Test
    public void testBasicUsage() throws IOException {
        try (TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(outputFile))) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            entry.setSize(10);
            entry.setName("test.txt");
            tarOutputStream.putNextEntry(entry);
            tarOutputStream.write("Hello, World!".getBytes(StandardCharsets.UTF_8));
            tarOutputStream.closeArchiveEntry();
            tarOutputStream.finish();
        }

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry = new TarArchiveEntry(outputFile, entry.getName());
        assertEquals("test.txt\000*\000test\000\0\0\0\0\0\0\0\0\0\0\0\0\000" +
                "\002*\012test.txt\000\0\0\0\0\0\0\0\0\0\0\0\000" +
                "\002*\012Hello, World!\000\012\0\0\0\0\0\0\0\0\0\0\0\0\0" +
                "\000", IOUtils.toByteArray(outputFile.toURI().toURL().openConnection().getInputStream()));

        assertEquals(10, entry.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullEntryName() throws IOException {
        try (TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(outputFile))) {
            TarArchiveEntry entry = new TarArchiveEntry(null);
            tarOutputStream.putArchiveEntry(entry);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws IOException {
        try (TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(outputFile))) {
            tarOutputStream.putArchiveEntry(null);
        }
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        try (TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(outputFile))) {
            String longFileName = "longFileNameWithMoreThan100Chars.txt";
            TarArchiveEntry entry = new TarArchiveEntry(longFileName);
            tarOutputStream.putArchiveEntry(entry);
            // Setting the long file mode to LONGFILE_TRUNCATE
            tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
            tarOutputStream.write("Hello, World!".getBytes(StandardCharsets.UTF_8));
            tarOutputStream.closeArchiveEntry();
            tarOutputStream.finish();

            TarArchiveEntry longFileNameEntry = new TarArchiveEntry(outputFile, longFileName);
            assertFalse(longFileNameEntry.isLongLink());

            // Setting the long file mode to LONGFILE_GNU
            tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(outputFile));
            entry = new TarArchiveEntry(longFileName);
            tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            tarOutputStream.putArchiveEntry(entry);
            tarOutputStream.write(longFileName.getBytes(StandardCharsets.UTF_8));
            tarOutputStream.write(new byte[]{(byte) 0x00});
            tarOutputStream.closeArchiveEntry();
            tarOutputStream.finish();

            longFileNameEntry = new TarArchiveEntry(outputFile, longFileName);
            assertTrue(longFileNameEntry.isLongLink());
        }
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNameLongerThanNameLength() throws IOException {
        try (TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(outputFile))) {
            String longFileName = "longFileNameWithMoreThan100Chars.txt";
            TarArchiveEntry entry = new TarArchiveEntry(longFileName);
            tarOutputStream.putArchiveEntry(entry);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        try (TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(outputFile))) {
            // Writing an entry
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            entry.setSize(10);
            entry.setName("test.txt");
            tarOutputStream.putNextEntry(entry);
            tarOutputStream.write("Hello, World!".getBytes(StandardCharsets.UTF_8));
            tarOutputStream.closeArchiveEntry();

            // Finishing the stream
            tarOutputStream.finish();

            // Attempting to write an entry after finishing the stream
            entry = new TarArchiveEntry("test2.txt");
            tarOutputStream.putArchiveEntry(entry);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteAfterFinish() throws IOException {
        try (TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(outputFile))) {
            // Writing an entry
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            entry.setSize(10);
            entry.setName("test.txt");
            tarOutputStream.putNextEntry(entry);
            tarOutputStream.write("Hello, World!".getBytes(StandardCharsets.UTF_8));
            tarOutputStream.closeArchiveEntry();

            // Finishing the stream
            tarOutputStream.finish();

            // Attempting to write bytes after finishing the stream
            tarOutputStream.write("Another message".getBytes(StandardCharsets.UTF_8));
        }
    }
}