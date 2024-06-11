import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntryWithValidParameters() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("testfile");
        cpioArchiveEntry.setFileSize(5L);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        Assertions.assertEquals(MAGIC_NEW.length, outputStream.size());
        Assertions.assertEquals(MAGIC_NEW[0], outputStream.read());
        Assertions.assertEquals(cpioArchiveEntry.getName().length() + 1, outputStream.read() & 0xFFFF);
        Assertions.assertEquals(cpioArchiveEntry.getName().length() + 1, outputStream.read() & 0xFFFF);
        Assertions.assertEquals(CpioConstants.FORMAT_NEW, outputStream.read() & 0xFFFF);
        // Write the name
        for (int i = 0; i < cpioArchiveEntry.getName().length(); i++) {
            Assertions.assertEquals(cpioArchiveEntry.getName().charAt(i), (char) outputStream.read());
        }
        Assertions.assertEquals('\0', (char) outputStream.read());
        // Write header information
        writeHeaderInfo(outputStream, cpioArchiveEntry);

        cpioArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testPutNextEntryWithInvalidFormatParameter() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("testfile");
        cpioArchiveEntry.setFileSize(5L);

        Assertions.assertThrows(IllegalArgumentException.class, () -> cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry));
    }

    @Test
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry cpioArchiveEntry1 = new CpioArchiveEntry();
        cpioArchiveEntry1.setName("testfile");
        cpioArchiveEntry1.setFileSize(5L);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry1);

        CpioArchiveEntry cpioArchiveEntry2 = new CpioArchiveEntry();
        cpioArchiveEntry2.setName("testfile");
        cpioArchiveEntry2.setFileSize(5L);

        Assertions.assertThrows(IOException.class, () -> cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry2));
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry());

        byte[] testData = "test data".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);

        byte[] expectedData = new byte[testData.length + MAGIC_NEW.length + 1];
        System.arraycopy(MAGIC_NEW, 0, expectedData, 0, MAGIC_NEW.length);
        expectedData[MAGIC_NEW.length] = (byte) (cpioArchiveEntry.getName().length() & 0xFF);
        expectedData[MAGIC_NEW.length + 1] = (byte) (cpioArchiveEntry.getName().length() >> 8);
        System.arraycopy(cpioArchiveEntry.getName().getBytes(), 0, expectedData, MAGIC_NEW.length + 2, cpioArchiveEntry.getName().length());
        expectedData[MAGIC_NEW.length + cpioArchiveEntry.getName().length() + 2] = '\0';
        System.arraycopy(testData, 0, expectedData, MAGIC_NEW.length + cpioArchiveEntry.getName().length() + 3, testData.length);

        Assertions.assertArrayEquals(expectedData, outputStream.toByteArray());
    }

    @Test
    public void testPutNextEntryWithInvalidEntrySize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("testfile");
        cpioArchiveEntry.setFileSize(5L);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);

        Assertions.assertThrows(IOException.class, () -> cpioArchiveOutputStream.write(new byte[10], 0, 10));
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry());
        cpioArchiveOutputStream.write("test data".getBytes());

        cpioArchiveOutputStream.finish();

        byte[] expectedData = new byte[testData.length + MAGIC_NEW.length + 1 + 1 + 4];
        System.arraycopy(MAGIC_NEW, 0, expectedData, 0, MAGIC_NEW.length);
        expectedData[MAGIC_NEW.length] = (byte) (cpioArchiveEntry.getName().length() & 0xFF);
        expectedData[MAGIC_NEW.length + 1] = (byte) (cpioArchiveEntry.getName().length() >> 8);
        System.arraycopy(cpioArchiveEntry.getName().getBytes(), 0, expectedData, MAGIC_NEW.length + 2, cpioArchiveEntry.getName().length());
        expectedData[MAGIC_NEW.length + cpioArchiveEntry.getName().length() + 2] = '\0';
        System.arraycopy(testData, 0, expectedData, MAGIC_NEW.length + cpioArchiveEntry.getName().length() + 3, testData.length);
        expectedData[expectedData.length - 4] = (byte) 0x0C;
        expectedData[expectedData.length - 3] = (byte) 0x00;
        expectedData[expectedData.length - 2] = (byte) 0x00;
        expectedData[expectedData.length - 1] = (byte) 0x00;

        Assertions.assertArrayEquals(expectedData, outputStream.toByteArray());
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.close();

        Assertions.assertEquals(0, outputStream.size());
    }

    /**
     * Helper method to write header information to the output stream
     *
     * @param outputStream The output stream to write the header information to
     * @param cpioArchiveEntry The cpio archive entry
     * @throws IOException
     */
    private void writeHeaderInfo(ByteArrayOutputStream outputStream, CpioArchiveEntry cpioArchiveEntry) throws IOException {
        long inode = 1;
        long mode = 0777 & 0xFFFFFFFFL;
        long uid = 1001;
        long gid = 1001;
        long numberOfLinks = 1;
        long time = System.currentTimeMillis();
        long size = cpioArchiveEntry.getFileSize();
        long deviceMaj = 1;
        long deviceMin = 1;
        long remoteDeviceMaj = 1;
        long remoteDeviceMin = 1;

        writeAsciiLong(outputStream, inode, 8);
        writeAsciiLong(outputStream, mode, 8);
        writeAsciiLong(outputStream, uid, 8);
        writeAsciiLong(outputStream, gid, 8);
        writeAsciiLong(outputStream, numberOfLinks, 8);
        writeAsciiLong(outputStream, time, 8);
        writeAsciiLong(outputStream, size, 8);
        writeAsciiLong(outputStream, deviceMaj, 8);
        writeAsciiLong(outputStream, deviceMin, 8);
        writeAsciiLong(outputStream, remoteDeviceMaj, 8);
        writeAsciiLong(outputStream, remoteDeviceMin, 8);
        writeAsciiLong(outputStream, cpioArchiveEntry.getName().length() + 1, 8);
        writeAsciiLong(outputStream, 0, 8); // chksum

        String name = cpioArchiveEntry.getName();
        writeCString(outputStream, name);
        pad(outputStream, cpioArchiveEntry.getHeaderSize() + name.length() + 1);
    }

    /**
     * Helper method to write an ascii long value to the output stream
     *
     * @param outputStream The output stream to write the value to
     * @param value The value to write
     * @param radix The radix to use
     * @throws IOException
     */
    private void writeAsciiLong(ByteArrayOutputStream outputStream, long value, int radix) throws IOException {
        String numberString = Long.toString(value, radix);
        int length = numberString.length();
        writeAsciiLong(outputStream, length, 8);

        if (length <= 8) {
            if (length < 8) {
                int numberOfZeroes = 8 - length;
                for (int i = 0; i < numberOfZeroes; i++) {
                    outputStream.write('0');
                }
            }
            outputStream.write(numberString.getBytes());
        } else {
            int firstHalfLength = 8;
            int secondHalfLength = length - firstHalfLength;
            outputStream.write(numberString.substring(0, firstHalfLength).getBytes());
            writeAsciiLong(outputStream, secondHalfLength, 8);
            outputStream.write(numberString.substring(firstHalfLength).getBytes());
        }
    }

    /**
     * Helper method to write a C string to the output stream
     *
     * @param outputStream The output stream to write the C string to
     * @param string The C string to write
     * @throws IOException
     */
    private void writeCString(ByteArrayOutputStream outputStream, String string) throws IOException {
        byte[] stringBytes = string.getBytes();
        outputStream.write(stringBytes, 0, stringBytes.length);
        outputStream.write('\0');
    }

    /**
     * Helper method to pad the output stream with zeroes
     *
     * @param outputStream The output stream to pad
     * @param count The number of bytes to pad
     * @throws IOException
     */
    private void pad(ByteArrayOutputStream outputStream, long count) throws IOException {
        long remainingLength = count % 4;
        long paddingLength = 4 - remainingLength;

        for (int i = 0; i < paddingLength; i++) {
            outputStream.write(0);
        }
    }

    /**
     * Magic constants
     */
    private static final byte[] MAGIC_NEW = {0x19, 'C', 'P', 'I', 'O', 0};
    private static final byte[] MAGIC_NEW_CRC = {0x1A, 'C', 'P', 'I', 'O', 0};
    private static final byte[] MAGIC_OLD_ASCII = {'C', 'P', 'I', 'O', '\u0001', '\u0001'};
    private static final byte[] MAGIC_OLD_BINARY = {0x19, (byte) 0x21, 0x21, 0x1A, 0x1A, 0x1A, 0x1A, 0x1A, 0x1A, 0x1A};
}