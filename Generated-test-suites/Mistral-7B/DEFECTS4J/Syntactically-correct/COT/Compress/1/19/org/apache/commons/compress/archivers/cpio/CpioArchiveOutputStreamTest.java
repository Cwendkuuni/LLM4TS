import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * JUnit 4 test cases for the CpioArchiveOutputStream class.
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOutputStream.putNextEntry(entry);
        Assert.assertNull(cpioOutputStream.cpioEntry);
        Assert.assertEquals("testfile", cpioOutputStream.names.get("testfile"));
    }

    @Test(expected = IOException.class)
    public void testDuplicateEntry() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile");
        cpioOutputStream.putNextEntry(entry1);
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile");
        cpioOutputStream.putNextEntry(entry2);
    }

    @Test
    public void testWriteBytes() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOutputStream.putNextEntry(entry);
        byte[] testData = "test".getBytes();
        cpioOutputStream.write(testData, 0, testData.length);
        Assert.assertEquals(testData.length, cpioOutputStream.written);
    }

    @Test(expected = IOException.class)
    public void testWritePastEndOfEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOutputStream.putNextEntry(entry);
        byte[] testData = "test".getBytes();
        cpioOutputStream.write(testData, 0, testData.length + 1);
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile1");
        cpioOutputStream.putNextEntry(entry1);
        byte[] testData1 = "test1".getBytes();
        cpioOutputStream.write(testData1, 0, testData1.length);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile2");
        cpioOutputStream.putNextEntry(entry2);
        byte[] testData2 = "test2".getBytes();
        cpioOutputStream.write(testData2, 0, testData2.length);

        cpioOutputStream.finish();

        Assert.assertEquals(testData1.length + testData2.length + 2,
                outputStream.size());
    }

    @Test
    public void testClose() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile1");
        cpioOutputStream.putNextEntry(entry1);
        byte[] testData1 = "test1".getBytes();
        cpioOutputStream.write(testData1, 0, testData1.length);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile2");
        cpioOutputStream.putNextEntry(entry2);
        byte[] testData2 = "test2".getBytes();
        cpioOutputStream.write(testData2, 0, testData2.length);

        cpioOutputStream.close();

        Assert.assertEquals(testData1.length + testData2.length + 2,
                outputStream.size());
    }
}