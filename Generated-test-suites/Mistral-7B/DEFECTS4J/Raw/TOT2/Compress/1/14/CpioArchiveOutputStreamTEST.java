import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntry() throws IOException {
        final byte[] testData = "Hello World".getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.txt");
        entry.setSize(testData.length);
        cpioOutput.putNextEntry(entry);
        cpioOutput.write(testData);
        cpioOutput.closeArchiveEntry();
        cpioOutput.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Map<String, CpioArchiveEntry> entryMap = new HashMap<>();
        CpioArchiveOutputStream testCpioOutput = new CpioArchiveOutputStream(inputStream, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry testEntry = testCpioOutput.getNextEntry();
        assertEquals("testfile.txt", testEntry.getName());
        assertEquals(testData.length, testEntry.getSize());
        testCpioOutput.close();

        byte[] readData = new byte[testData.length];
        testCpioOutput.read(readData, 0, testData.length);
        assertEquals(Arrays.toString(testData), Arrays.toString(readData));
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile.txt");
        cpioOutput.putNextEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile.txt");
        cpioOutput.putNextEntry(entry2);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithoutName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        cpioOutput.putNextEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithoutSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.txt");
        cpioOutput.putNextEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.txt");
        entry.setSize(10);
        cpioOutput.putNextEntry(entry);

        byte[] testData = new byte[20];
        cpioOutput.write(testData);
    }

    @Test
    public void testWriteWithSwappedEndian() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_BINARY);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.txt");
        entry.setSize(10);
        cpioOutput.putNextEntry(entry);
        cpioOutput.write(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        cpioOutput.closeArchiveEntry();
        cpioOutput.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CpioArchiveOutputStream cpioInput = new CpioArchiveOutputStream(inputStream, CpioConstants.FORMAT_OLD_BINARY);
        byte[] readData = new byte[7];
        cpioInput.read(readData, 0, readData.length);
        assertEquals(CpioUtil.long2byteArray(0x08070605L, 2, true), Arrays.copyOfRange(readData, 0, readData.length));
        cpioInput.close();
    }

    @Test
    public void testWriteWithNormalEndian() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.txt");
        entry.setSize(10);
        cpioOutput.putNextEntry(entry);
        cpioOutput.write(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        cpioOutput.closeArchiveEntry();
        cpioOutput.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CpioArchiveOutputStream cpioInput = new CpioArchiveOutputStream(inputStream, CpioConstants.FORMAT_NEW);
        byte[] readData = new byte[8];
        cpioInput.read(readData, 0, readData.length);
        assertEquals(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08}, readData);
        cpioInput.close();
    }

    @Test
    public void testWriteEmptyFile() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.txt");
        cpioOutput.putNextEntry(entry);
        cpioOutput.finish();
        cpioOutput.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CpioArchiveOutputStream cpioInput = new CpioArchiveOutputStream(inputStream, CpioConstants.FORMAT_NEW);
        byte[] readData = new byte[1];
        int bytesRead = cpioInput.read(readData, 0, readData.length);
        assertEquals(0, bytesRead);
        cpioInput.close();
    }
}