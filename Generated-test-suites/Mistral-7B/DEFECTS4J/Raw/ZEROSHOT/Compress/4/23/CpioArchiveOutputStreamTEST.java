import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testCpioArchiveOutputStream() throws IOException {
        final String testFileName = "test.cpio";
        ByteArrayOutputStream testByteArrayOutputStream = new ByteArrayOutputStream();
        OutputStream outputStream = new CpioArchiveOutputStream(testByteArrayOutputStream, CpioConstants.FORMAT_NEW);

        // Test putArchiveEntry method with valid parameters
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("test1.txt");
        entry1.setFormat(CpioConstants.FORMAT_NEW);
        entry1.setSize(10);
        entry1.setMode(CpioConstants.C_ISREG);
        outputStream.putArchiveEntry(entry1);

        byte[] testData1 = "Test data for entry1".getBytes();
        outputStream.write(testData1);
        outputStream.closeArchiveEntry();

        // Test putArchiveEntry method with duplicate entry name
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("test1.txt");
        entry2.setFormat(CpioConstants.FORMAT_NEW);
        entry2.setSize(20);
        entry2.setMode(CpioConstants.C_ISREG);
        try {
            outputStream.putArchiveEntry(entry2);
            Assert.fail("Expected IOException with message: duplicate entry: test1.txt");
        } catch (IOException e) {
            Assert.assertEquals("duplicate entry: test1.txt", e.getMessage());
        }

        // Test putArchiveEntry method with null entry
        CpioArchiveEntry entry3 = null;
        try {
            outputStream.putArchiveEntry(entry3);
            Assert.fail("Expected IOException with message: null");
        } catch (IOException e) {
            Assert.assertEquals("null", e.getMessage());
        }

        // Test finish method
        outputStream.finish();
        InputStream inputStream = new ByteArrayInputStream(testByteArrayOutputStream.toByteArray());
        File testFile = new File(testFileName);
        ArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(testFile.getAbsoluteFile());
        archiveOutputStream.setLevel(3);
        archiveOutputStream.putNextEntry(new JarEntry("test1.txt"));
        archiveOutputStream.write(testData1);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        archiveOutputStream.close();

        // Test read ArchiveEntry from the created cpio file
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(new File(testFileName));
        Assert.assertEquals("test1.txt", archiveInputStream.getNextEntry().getName());
        Assert.assertEquals(10, archiveInputStream.getNextEntry().getSize());
        String testDataRead = new String(ArchiveUtils.inputStreamToByteArray(archiveInputStream));
        Assert.assertEquals("Test data for entry1", testDataRead.substring(0, 10));
        archiveInputStream.close();

        // Test close method
        output.close();

        // Test that the file is deleted after test
        Assert.assertFalse(new File(testFileName).exists());
    }
}