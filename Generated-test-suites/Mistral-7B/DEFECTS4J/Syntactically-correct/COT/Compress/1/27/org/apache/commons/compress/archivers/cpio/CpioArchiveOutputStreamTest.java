import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    @Test
    public void testCpioArchiveOutputStreamConstruction() {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        Assert.assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getFormat());
    }

    @Test
    public void testPutNextEntryWithValidEntry() throws IOException {
        OutputStream out = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(10);
        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] testData = "test data".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        FileInputStream fileInputStream = new FileInputStream("test.cpio");
        byte[] buffer = new byte[10];
        int bytesRead;

        while ((bytesRead = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
            Assert.assertArrayEquals(buffer, inputStream.readNBytes(bytesRead));
        }

        inputStream.close();
        fileInputStream.close();
        new File("test.cpio").delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryWithNullOutputStream() {
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(null, CpioConstants.FORMAT_NEW);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryWithInvalidFormat() {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out);
        cpioArchiveOutputStream.setFormat((short) 0xFF);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile");
        entry1.setSize(10);
        cpioArchiveOutputStream.putNextEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile");
        cpioArchiveOutputStream.putNextEntry(entry2);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithNullEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putNextEntry(null);
    }

    @Test
    public void testWriteWithValidData() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(10);
        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] testData = "test data".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedData = new byte[10 + testData.length];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);

        byte[] actualData = cpioArchiveOutputStream.getOutputStream().toByteArray();
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.close();
        cpioArchiveOutputStream.write(new byte[10], 0, 10);
    }

    @Test
    public void testFinishWithValidData() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(10);
        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] testData = "test data".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);

        cpioArchiveOutputStream.finish();

        byte[] expectedData = new byte[testData.length + 1]; // for the trailing null byte
        System.arraycopy(testData, 0, expectedData, 0, testData.length);

        byte[] actualData = cpioArchiveOutputStream.getOutputStream().toByteArray();
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testCloseArchiveEntryWithDataLeftToWrite() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(10);
        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] testData = "test data".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length - 1); // write all but the last byte

        cpioArchiveOutputStream.closeArchiveEntry(); // should not throw an exception

        Assert.assertEquals(1, cpioArchiveOutputStream.getWritten()); // check that the remaining byte was not written

        byte[] expectedData = new byte[testData.length];
        System.arraycopy(testData, 0, expectedData, 0, testData.length - 1);

        byte[] actualData = cpioArchiveOutputStream.getOutputStream().toByteArray();
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testCloseArchiveEntryWithDataWritten() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(10);
        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] testData = "test data".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);

        cpioArchiveOutputStream.closeArchiveEntry(); // should not throw an exception

        Assert.assertEquals(testData.length, cpioArchiveOutputStream.getWritten()); // check that all bytes were written

        byte[] expectedData = new byte[testData.length + 1]; // for the trailing null byte
        System.arraycopy(testData, 0, expectedData, 0, testData.length);

        byte[] actualData = cpioArchiveOutputStream.getOutputStream().toByteArray();
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testCloseWithDataLeftToWrite() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(10);
        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] testData = "test data".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length - 1); // write all but the last byte

        cpioArchiveOutputStream.close();

        Assert.assertEquals(1, cpioArchiveOutputStream.getWritten()); // check that the remaining byte was not written

        byte[] expectedData = new byte[testData.length];
        System.arraycopy(testData, 0, expectedData, 0, testData.length - 1);

        byte[] actualData = cpioArchiveOutputStream.getOutputStream().toByteArray();
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testCloseWithDataWritten() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(10);
        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] testData = "test data".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);

        cpioArchiveOutputStream.close();

        Assert.assertEquals(testData.length, cpioArchiveOutputStream.getWritten()); // check that all bytes were written

        byte[] expectedData = new byte[testData.length + 1]; // for the trailing null byte
        System.arraycopy(testData, 0, expectedData, 0, testData.length);

        byte[] actualData = cpioArchiveOutputStream.getOutputStream().toByteArray();
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testPadFunctionality() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(1);
        cpioArchiveOutputStream.putNextEntry(entry);

        byte testByte = 0x01;
        cpioArchiveOutputStream.write(testByte);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();

        byte[] expectedData = new byte[] { 0x01, 0x00, 0x00, 0x00 };
        byte[] actualData = cpioArchiveOutputStream.getOutputStream().toByteArray();
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testWriteBinaryLongFunctionality() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_OLD_BINARY);

        cpioArchiveOutputStream.writeBinaryLong(0x12345678L, 4, false);

        byte[] expectedData = new byte[] { (byte) 0x78, 0x56, 0x34, 0x12 };
        byte[] actualData = cpioArchiveOutputStream.getOutputStream().toByteArray();
        Assert.assertArrayEquals(expectedData, actualData);

        cpioArchiveOutputStream.writeBinaryLong(0x12345678L, 4, true);

        expectedData = new byte[] { (byte) 0x12, 0x78, 0x56, 0x34 };
        actualData = cpioArchiveOutputStream.getOutputStream().toByteArray();
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testWriteAsciiLongFunctionality() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_OLD_ASCII);

        cpioArchiveOutputStream.writeAsciiLong(0x12345678L, 8, 8);

        byte[] expectedData = new byte[] { '0', 'x', '1', '2', '3', '4', '5', '6', '7', '8' };
        byte[] actualData = cpioArchiveOutputStream.getOutputStream().toByteArray();
        Assert.assertArrayEquals(expectedData, actualData);

        cpioArchiveOutputStream.writeAsciiLong(0x12345678L, 8, 16);

        expectedData = new byte[] { '0', 'x', '1', '2', '3', '4', '5', '6', '7', '8', '0', 'x' };
        actualData = cpioArchiveOutputStream.getOutputStream().toByteArray();
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testWriteCStringFunctionality() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_OLD_ASCII);

        cpioArchiveOutputStream.writeCString("testfile");

        byte[] expectedData = new byte[] { 't', 'e', 's', 't', 'f', 'i', 'l', 'e' };
        byte[] actualData = cpioArchiveOutputStream.getOutputStream().toByteArray();
        Assert.assertArrayEquals(expectedData, actualData);

        byte[] testData = "test data".getBytes();
        cpioArchiveOutputStream.writeCString("test data");

        expectedData = new byte[testData.length + 1];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);
        actualData = cpioArchiveOutputStream.getOutputStream().toByteArray();
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testPutArchiveEntryFunctionality() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(10);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write("test data".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();

        Map<String, CpioArchiveEntry> expectedEntries = new HashMap<>();
        CpioArchiveEntry expectedEntry = new CpioArchiveEntry();
        expectedEntry.setName("testfile");
        expectedEntry.setSize(10);
        expectedEntries.put("testfile", expectedEntry);

        Map<String, CpioArchiveEntry> actualEntries = cpioArchiveOutputStream.getEntries();
        Assert.assertEquals(expectedEntries, actualEntries);
    }
}