import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() throws Exception {
        outputFile = File.createTempFile("cpio-test", null);
        cpioOutputStream = new CpioArchiveOutputStream(new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        if (outputFile.exists()) {
            outputFile.delete();
        }
        if (cpioOutputStream != null) {
            cpioOutputStream.close();
        }
    }

    @Test
    public void testPutArchiveEntryWithValidParameter() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile.txt");
        cpioOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile.txt");
        cpioOutputStream.putArchiveEntry(entry);
        assertThrows(IOException.class, () -> cpioOutputStream.putArchiveEntry(entry));
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(CpioConstants.FORMAT_INVALID);
        cpioOutputStream.putArchiveEntry(entry);
        assertThrows(IllegalArgumentException.class, () -> cpioOutputStream.putArchiveEntry(entry));
    }

    @Test
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        cpioOutputStream.putArchiveEntry(null);
        assertNull(cpioOutputStream.getNextEntry());
        assertFalse(cpioOutputStream.hasNextEntry());
    }

    @Test
    public void testWriteStringData() throws IOException {
        String testData = "Hello World!";
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile.txt");
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(testData.getBytes());
        cpioOutputStream.closeArchiveEntry();

        ByteArrayOutputStream actualData = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(testData.getBytes());
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.finish();
        cpioOutputStream.close();

        byte[] expectedData = testData.getBytes();
        byte[] actualByteArray = actualData.toByteArray();
        assertArrayEquals(expectedData, actualByteArray);
    }

    @Test
    public void testWriteInvalidDataSize() throws IOException {
        String testData = "Hello World!";
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile.txt");
        cpioOutputStream.putArchiveEntry(entry);

        // Write less data than the entry size
        cpioOutputStream.write(testData.getBytes(), 0, testData.length() - 1);
        assertFalse(cpioOutputStream.finishCalled);

        // Write more data than the entry size
        cpioOutputStream.write(testData.getBytes(), 0, testData.length() + 1);
        assertFalse(cpioOutputStream.finishCalled);

        // Close the output stream
        cpioOutputStream.close();
        assertFalse(cpioOutputStream.finishCalled);

        // Read the file and check that the data is correct
        File outputFileRead = new File(outputFile.getAbsolutePath() + ".tmp");
        byte[] expectedData = testData.getBytes();
        byte[] actualByteArray = ArchiveUtils.loadFile(outputFileRead).getBytes();
        assertArrayEquals(expectedData, actualByteArray);
    }

    @Test
    public void testWriteDataWithCrcChecksum() throws IOException {
        String testData = "Hello World!";
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile.txt");
        cpioOutputStream.putArchiveEntry(entry);

        // Write data without calculating the crc
        cpioOutputStream.write(testData.getBytes());
        cpioOutputStream.closeArchiveEntry();

        // Write data with calculating the crc
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(testData.getBytes());
        cpioOutputStream.setDoCrc(true);
        cpioOutputStream.closeArchiveEntry();

        // Check that the crc is correct
        cpioOutputStream = new CpioArchiveOutputStream(new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW_CRC);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(testData.getBytes());
        cpioOutputStream.setDoCrc(true);
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.finish();
        cpioOutputStream.close();

        File outputFileRead = new File(outputFile.getAbsolutePath() + ".tmp");
        byte[] expectedData = testData.getBytes();
        byte[] actualByteArray = ArchiveUtils.loadFile(outputFileRead).getBytes();
        byte[] crcBytes = new byte[8];
        System.arraycopy(actualByteArray, actualByteArray.length - 8, crcBytes, 0, 8);
        long calculatedCrc = CpioArchiveOutputStream.crc32(expectedData);
        long actualCrc = byteArrayToLong(crcBytes);
        assertEquals(calculatedCrc, actualCrc);
    }
}