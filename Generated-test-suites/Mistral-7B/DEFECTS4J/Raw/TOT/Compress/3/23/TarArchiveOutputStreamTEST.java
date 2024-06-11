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
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class TarArchiveOutputStreamTest {
    private File tempFile;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        tempFile = File.createTempFile("junit-", null);
        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws Exception {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void testEmptyArchive() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry firstEntry = new TarArchiveEntry(new File(""));
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);
        assertFalse(tarInputStream.hasNextEntry());
        tarInputStream.close();

        assertEquals(0, outputStream.size());
    }

    @Test
    public void testValidArchive() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8), new Date(), new Date(), 0);
        tarOutputStream.putArchiveEntry(testEntry);
        testEntry.writeEntryHeader(outputStream.toByteArray());
        outputStream.write("test content".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);
        assertNotNull(tarInputStream.getNextEntry());
        assertEquals("test.txt", tarInputStream.getNextEntry().getName());
        assertEquals(29L, tarInputStream.getNextEntry().getSize());
        byte[] testData = new byte[10];
        inputStream.read(testData);
        assertEquals("test content".getBytes(StandardCharsets.UTF_8), testData);
        tarInputStream.close();
    }

    @Test
    public void testLongFileName() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        String longFileName = "a/long/path/to/file.txt";
        TarArchiveEntry testEntry = new TarArchiveEntry(longFileName, "test content".getBytes(StandardCharsets.UTF_8), new Date(), new Date(), 0);
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);
        TarArchiveEntry archiveEntry = tarInputStream.getNextEntry();
        assertEquals("a/long/path/to/file.txt", archiveEntry.getName());
        tarInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLongFileNameError() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry testEntry = new TarArchiveEntry("too/long/filename.txt", "test content".getBytes(StandardCharsets.UTF_8), new Date(), new Date(), 0);
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.finish();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry testEntry = null;
        tarOutputStream.putArchiveEntry(testEntry);
    }

    @Test(expected = IOException.class)
    public void testUnclosedArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8), new Date(), new Date(), 0);
        tarOutputStream.putArchiveEntry(testEntry);
        testEntry.writeEntryHeader(outputStream.toByteArray());
        outputStream.write("test content".getBytes(StandardCharsets.UTF_8));
        // do not close the archive entry

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);
        assertThrows(IOException.class, () -> tarInputStream.getNextEntry());
        tarInputStream.close();
    }
}