import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class TarArchiveOutputStreamTest {

    private File testFile;
    private TarArchiveOutputStream outputStream;
    private ArchiveEntry testEntry;

    @Before
    public void setUp() throws Exception {
        testFile = new File("test.tar");
        outputStream = new TarArchiveOutputStream(testFile);
        testEntry = new TarArchiveEntry("test.txt");
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        outputStream.putArchiveEntry(testEntry);
        outputStream.write("Hello, World!".getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();
        assertEquals("Hello, World!", readFile("test.txt"));
    }

    @Test
    public void testSetLongFileMode() throws Exception {
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        ArchiveEntry longLinkEntry = new TarArchiveEntry("longLink.txt", "Long Link Name");
        outputStream.putArchiveEntry(longLinkEntry);
        outputStream.write("Link Name".getBytes(StandardCharsets.UTF_8));
        outputStream.write(0); // NUL terminator
        outputStream.closeArchiveEntry();
        assertEquals("Link Name", readFile("longLink.txt"));
    }

    @Test(expected = IOException.class)
    public void testLongFileModeError() throws Exception {
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        ArchiveEntry longFileNameEntry = new TarArchiveEntry("longFileName.txt");
        outputStream.putArchiveEntry(longFileNameEntry);
        outputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testLongFileModeTruncate() throws Exception {
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        ArchiveEntry longFileNameEntry = new TarArchiveEntry("longFileName.txt");
        outputStream.putArchiveEntry(longFileNameEntry);
        outputStream.write("longFileName.txt".getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();
    }

    @Test
    public void testClose() throws Exception {
        outputStream.putArchiveEntry(testEntry);
        outputStream.write("Hello, World!".getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();
        outputStream.close();
        assertFalse(outputStream.isOpen());
    }

    @Test
    public void testFinish() throws Exception {
        outputStream.putArchiveEntry(testEntry);
        outputStream.write("Hello, World!".getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();
        outputStream.finish();
        assertNull(outputStream.getNextEntry());
        assertFalse(outputStream.isOpen());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryAfterFinish() throws Exception {
        outputStream.putArchiveEntry(testEntry);
        outputStream.write("Hello, World!".getBytes(StandardCharsets.UTF_8));
        outputStream.finish();
        outputStream.putArchiveEntry(testEntry);
        assertThrows(IOException.class, () -> outputStream.write("Test".getBytes()));
    }

    @Test
    public void testWrite() throws Exception {
        outputStream.putArchiveEntry(testEntry);
        outputStream.write("Hello, World!".getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();
        assertEquals("Hello, World!", readFile("test.txt"));
    }

    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws Exception {
        outputStream.putArchiveEntry(testEntry);
        outputStream.write("Hello, World!".getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();
        outputStream.close();
        assertThrows(IOException.class, () -> outputStream.write("Test".getBytes()));
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondSize() throws Exception {
        outputStream.putArchiveEntry(testEntry);
        outputStream.write("Hello, World!".getBytes(StandardCharsets.UTF_8));
        outputStream.write(new byte[testEntry.getSize() + 1]);
        outputStream.closeArchiveEntry();
    }

    private String readFile(String fileName) throws IOException {
        return new String(IOUtils.toByteArray(new File(testFile, fileName)),
                StandardCharsets.UTF_8);
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.deleteQuietly(testFile);
    }
}