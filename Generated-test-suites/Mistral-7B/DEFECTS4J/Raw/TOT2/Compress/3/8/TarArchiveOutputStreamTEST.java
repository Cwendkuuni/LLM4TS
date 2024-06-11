import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry testEntry = new TarArchiveEntry("testEntry.txt");

        tarArchiveOutputStream.putArchiveEntry(testEntry);

        // Test writing contents to the entry
        String testContent = "This is a test entry content.";
        byte[] testContentBytes = testContent.getBytes();
        tarArchiveOutputStream.write(testContentBytes, 0, testContentBytes.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
        byte[] outputBytes = outputStream.toByteArray();

        // Test reading the entry from the output stream
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new ByteArrayInputStream(outputBytes));
        TarArchiveEntry readEntry = tarArchiveInputStream.getNextEntry();
        Assert.assertEquals("testEntry.txt", readEntry.getName());

        // Read the content of the entry and compare it with the written content
        byte[] readContentBytes = new byte[testContentBytes.length];
        int read = tarArchiveInputStream.read(readContentBytes, 0, readContentBytes.length);
        Assert.assertEquals(testContentBytes.length, read);
        Assert.assertArrayEquals(testContentBytes, readContentBytes);
        tarArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry testEntry = new TarArchiveEntry("testEntryWithLongFileName.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        String testContent = "This is a test entry content.";
        byte[] testContentBytes = testContent.getBytes();
        tarArchiveOutputStream.write(testContentBytes, 0, testContentBytes.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
        byte[] outputBytes = outputStream.toByteArray();

        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new ByteArrayInputStream(outputBytes));
        TarArchiveEntry readEntry = tarArchiveInputStream.getNextEntry();

        // The test should fail here because the long file name is not supported without the LONGFILE_GNU mode
        Assert.fail("Test should have failed with an IOException due to the long file name.");
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithTooLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry testEntry = new TarArchiveEntry("testEntryWithTooLongFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(testEntry);

        tarArchiveOutputStream.finish();
        byte[] outputBytes = outputStream.toByteArray();

        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new ByteArrayInputStream(outputBytes));

        // The test should fail here because the long file name is truncated due to the LONGFILE_TRUNCATE mode
        TarArchiveEntry readEntry = tarArchiveInputStream.getNextEntry();
        Assert.fail("Test should have failed with an IOException due to the truncated long file name.");
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithInvalidEntryType() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        Object invalidEntry = new Object();
        tarArchiveOutputStream.putArchiveEntry(invalidEntry);
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        // No need to call finish() or closeArchiveEntry() since we're testing the close() method directly
        tarArchiveOutputStream.close();

        Assert.assertTrue("Output stream should be closed.", outputStream.close());
    }
}