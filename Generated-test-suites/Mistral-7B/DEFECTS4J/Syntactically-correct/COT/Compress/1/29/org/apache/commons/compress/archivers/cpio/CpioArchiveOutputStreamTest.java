import org.apache.commons.compress.archivers.cpio.*;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testCreateCpioArchiveOutputStream() {
        OutputStream out = new ByteArrayOutputStream(); // for testing
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(out);
        assertEquals(FORMAT_NEW, cpioOutputStream.getFormat());
    }

    @Test
    public void testCreateCpioArchiveOutputStreamWithFormat() {
        OutputStream out = new ByteArrayOutputStream(); // for testing
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(out, FORMAT_OLD_BINARY);
        assertEquals(FORMAT_OLD_BINARY, cpioOutputStream.getFormat());
    }

    @Test
    public void testPutNextEntryWithValidCpioArchiveEntry() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);

        String fileName = "testfile.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(fileName);
        cpioArchiveEntry.setSize(10);

        cpioOutputStream.putNextEntry(cpioArchiveEntry);

        assertEquals(1, cpioOutputStream.getNextEntryCount());
        assertEquals(fileName, cpioOutputStream.getName());
        assertEquals(10, cpioOutputStream.getNextEntry().getSize());
    }

    @Test
    public void testPutNextEntryWithSameFileNameTwice() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);

        String fileName = "testfile.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(fileName);
        cpioArchiveEntry.setSize(10);

        cpioOutputStream.putNextEntry(cpioArchiveEntry);

        assertThrows(IllegalArgumentException.class, () -> {
            cpioOutputStream.putNextEntry(cpioArchiveEntry);
        });

        assertEquals(1, cpioOutputStream.getNextEntryCount());
    }

    @Test
    public void testPutNextEntryWithNullCpioArchiveEntry() {
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream());

        assertThrows(NullPointerException.class, () -> {
            cpioOutputStream.putNextEntry(null);
        });
    }

    @Test
    public void testWriteDataToCpioArchiveEntry() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);

        String fileName = "testfile.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(fileName);
        cpioArchiveEntry.setSize(10);

        cpioOutputStream.putNextEntry(cpioArchiveEntry);

        String testData = "Test Data";
        byte[] testDataBytes = testData.getBytes();

        cpioOutputStream.write(testDataBytes, 0, testDataBytes.length);

        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(testDataBytes.length, outputBytes.length);
    }

    @Test
    public void testFinish() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);

        String fileName = "testfile.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(fileName);
        cpioArchiveEntry.setSize(10);

        cpioOutputStream.putNextEntry(cpioArchiveEntry);
        cpioOutputStream.write(new byte[10]);
        cpioOutputStream.finish();

        byte[] outputBytes = outputStream.toByteArray();
        byte[] expectedHeader = new byte[16]; // header size
        byte[] expectedData = new byte[10]; // data size
        System.arraycopy(CpioConstants.MAGIC_NEW, 0, expectedHeader, 0, 8);
        System.arraycopy(CpioConstants.MAGIC_NEW, 0, expectedHeader, 8, 8);
        System.arraycopy(CpioUtil.long2byteArray(0L, 4, false), 0, expectedHeader, 12, 4);
        System.arraycopy(CpioUtil.long2byteArray(8L, 4, false), 16, expectedHeader, 16, 4);
        System.arraycopy(fileName.getBytes(), 0, expectedHeader, 20, fileName.length());
        System.arraycopy(CpioUtil.long2byteArray(10L, 4, false), 21 + fileName.length(), expectedHeader, 21 + fileName.length(), 4);
        System.arraycopy(CpioUtil.long2byteArray(CpioUtil.crc32(expectedData), 4, false), 25 + fileName.length(), expectedHeader, 25 + fileName.length(), 4);
        byte[] expectedTrailer = new byte[8]; // trailer size
        System.arraycopy(CpioConstants.TRAILER_MAGIC.getBytes(), 0, expectedTrailer, 0, 8);

        assertEquals(expectedHeader.length + expectedTrailer.length, outputBytes.length);
        assertTrue(outputBytes.length > 0 && outputBytes[0] == expectedHeader[0]);
        assertTrue(outputBytes.length > 0 && outputBytes[outputBytes.length - 1] == expectedTrailer[0]);
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);

        String fileName = "testfile.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(fileName);
        cpioArchiveEntry.setSize(10);

        cpioOutputStream.putNextEntry(cpioArchiveEntry);
        cpioOutputStream.write(new byte[10]);
        cpioOutputStream.closeArchiveEntry();

        byte[] outputBytes = outputStream.toByteArray();
        byte[] expectedHeader = new byte[16]; // header size
        byte[] expectedData = new byte[10]; // data size
        System.arraycopy(CpioConstants.MAGIC_NEW, 0, expectedHeader, 0, 8);
        System.arraycopy(CpioConstants.MAGIC_NEW, 0, expectedHeader, 8, 8);
        System.arraycopy(CpioUtil.long2byteArray(0L, 4, false), 12, expectedHeader, 12, 4);
        System.arraycopy(CpioUtil.long2byteArray(8L, 4, false), 16, expectedHeader, 16, 4);
        System.arraycopy(fileName.getBytes(), 0, expectedHeader, 20, fileName.length());
        System.arraycopy(CpioUtil.long2byteArray(10L, 4, false), 21 + fileName.length(), expectedHeader, 21 + fileName.length(), 4);
        System.arraycopy(CpioUtil.long2byteArray(CpioUtil.crc32(expectedData), 4, false), 25 + fileName.length(), expectedHeader, 25 + fileName.length(), 4);
        byte[] expectedTrailer = new byte[8]; // trailer size
        System.arraycopy(CpioConstants.TRAILER_MAGIC.getBytes(), 0, expectedTrailer, 0, 8);

        assertEquals(expectedHeader.length + expectedTrailer.length, outputBytes.length);
        assertTrue(outputBytes.length > 0 && outputBytes[0] == expectedHeader[0]);
        assertTrue(outputBytes.length > 0 && outputBytes[outputBytes.length - 1] == expectedTrailer[0]);
    }

    @Test
    public void testClose() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);

        String fileName = "testfile.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(fileName);
        cpioArchiveEntry.setSize(10);

        cpioOutputStream.putNextEntry(cpioArchiveEntry);
        cpioOutputStream.write(new byte[10]);
        cpioOutputStream.close();

        assertNull(cpioOutputStream.getNextEntry());
    }

    @Test
    public void testCloseMultipleTimes() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);

        String fileName = "testfile.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(fileName);
        cpioArchiveEntry.setSize(10);

        cpioOutputStream.putNextEntry(cpioArchiveEntry);
        cpioOutputStream.write(new byte[10]);
        cpioOutputStream.close();
        cpioOutputStream.close();

        assertNull(cpioOutputStream.getNextEntry());
    }

    @Test
    public void testCloseWithoutWritingData() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);

        String fileName = "testfile.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(fileName);
        cpioArchiveEntry.setSize(10);

        cpioOutputStream.putNextEntry(cpioArchiveEntry);
        cpioOutputStream.close();

        assertNull(cpioOutputStream.getNextEntry());
    }

    @Test
    public void testWriteDataWithClosedCpioArchiveOutputStream() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        cpioOutputStream.finish();
        cpioOutputStream.close();

        assertFalse(cpioOutputStream.isOpen());

        String fileName = "testfile.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(fileName);
        cpioArchiveEntry.setSize(10);

        cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        cpioOutputStream.putNextEntry(cpioArchiveEntry);

        assertThrows(IOException.class, () -> {
            cpioOutputStream.write(new byte[10]);
        });

        cpioOutputStream.close();
    }

    @Test
    public void testWriteDataWithDifferentEntryFormat() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, FORMAT_OLD_BINARY);

        String fileName = "testfile.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(fileName);
        cpioArchiveEntry.setSize(10);
        cpioArchiveEntry.setFormat(FORMAT_NEW);

        cpioOutputStream.putNextEntry(cpioArchiveEntry);

        assertThrows(IllegalArgumentException.class, () -> {
            cpioOutputStream.write(new byte[10]);
        });
    }
}