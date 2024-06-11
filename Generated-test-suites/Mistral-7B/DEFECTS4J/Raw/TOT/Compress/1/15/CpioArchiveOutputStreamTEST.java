import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CpioArchiveOutputStreamTest {

    private OutputStream os;
    private CpioArchiveOutputStream out;

    @Before
    public void setUp() throws IOException {
        os = new ByteArrayOutputStream();
        out = new CpioArchiveOutputStream(os, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (out != null) {
            out.close();
        }
    }

    @Test
    public void testPutNextEntry() throws IOException {
        // Test valid entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        out.putNextEntry(entry);
        byte[] expectedHeader = new byte[entry.getHeaderSize() + entry.getName().length() + 1];
        writeExpectedHeader(expectedHeader, entry);
        byte[] testData = "Hello, World!".getBytes();
        out.write(testData);
        out.closeArchiveEntry();
        byte[] actualData = os.toByteArray();
        assertEquals(expectedHeader.length + testData.length, actualData.length);
        for (int i = 0; i < expectedHeader.length; i++) {
            assertEquals(expectedHeader[i], actualData[i]);
        }
        for (int i = 0; i < testData.length; i++) {
            assertEquals(testData[i], actualData[expectedHeader.length + i]);
        }

        // Test duplicate entry with same name
        entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        Map<String, CpioArchiveEntry> nameMap = new HashMap<>();
        nameMap.put(entry.getName(), entry);
        out.putNextEntry(entry);
        try {
            out.putNextEntry(entry);
            fail("Expected IOException for duplicate entry");
        } catch (IOException e) {
            // Expected
        }

        // Test null entry
        entry = null;
        try {
            out.putNextEntry(entry);
            fail("Expected IOException for null entry");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testWriteEmptyEntry() throws IOException {
        // Test empty entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("empty.txt");
        entry.setSize(0);
        out.putNextEntry(entry);
        byte[] expectedHeader = new byte[entry.getHeaderSize() + entry.getName().length() + 1];
        writeExpectedHeader(expectedHeader, entry);
        out.closeArchiveEntry();
        byte[] actualData = os.toByteArray();
        assertEquals(expectedHeader.length, actualData.length);
        for (int i = 0; i < expectedHeader.length; i++) {
            assertEquals(expectedHeader[i], actualData[i]);
        }
    }

    @Test
    public void testWriteDataAfterHeader() throws IOException {
        // Test writing data after header
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        out.putNextEntry(entry);
        byte[] testData = "Hello, World!".getBytes();
        out.write(testData);
        byte[] expectedHeader = new byte[entry.getHeaderSize() + entry.getName().length() + 1];
        writeExpectedHeader(expectedHeader, entry);
        byte[] actualData = os.toByteArray();
        assertEquals(expectedHeader.length + testData.length, actualData.length);
        for (int i = 0; i < expectedHeader.length; i++) {
            assertEquals(expectedHeader[i], actualData[i]);
        }
        for (int i = 0; i < testData.length; i++) {
            assertEquals(testData[i], actualData[expectedHeader.length + i]);
        }
    }

    @Test
    public void testWriteDataAfterHeaderWithCRC() throws IOException {
        // Test writing data after header with CRC
        short crc = 0;
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        out.setFormat(CpioConstants.FORMAT_NEW_CRC);
        out.putNextEntry(entry);
        byte[] testData = "Hello, World!".getBytes();
        out.write(testData);
        byte[] expectedHeader = new byte[entry.getHeaderSize() + entry.getName().length() + 1];
        writeExpectedHeader(expectedHeader, entry);
        byte[] actualData = os.toByteArray();
        assertEquals(expectedHeader.length + testData.length, actualData.length);
        for (int i = 0; i < expectedHeader.length; i++) {
            assertEquals(expectedHeader[i], actualData[i]);
        }
        for (int i = 0; i < testData.length; i++) {
            assertEquals(testData[i], actualData[expectedHeader.length + i]);
        }
        for (int i = 0; i < testData.length; i++) {
            crc = (short) (crc ^ testData[i] & 0xFF);
        }
        assertEquals(CpioUtil.crc16(expectedHeader, 0, expectedHeader.length), crc);
    }

    @Test
    public void testWriteDataAfterHeaderWithSwappedBytes() throws IOException {
        // Test writing data after header with swapped bytes
        short swapHalfWord = 1;
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        out.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        out.putNextEntry(entry);
        byte[] testData = "Hello, World!".getBytes();
        out.write(testData);
        byte[] expectedHeader = new byte[entry.getHeaderSize() + entry.getName().length() + 1];
        writeExpectedHeader(expectedHeader, entry, swapHalfWord);
        byte[] actualData = os.toByteArray();
        assertEquals(expectedHeader.length + testData.length, actualData.length);
        for (int i = 0; i < expectedHeader.length; i++) {
            if (i < expectedHeader.length / 2) {
                assertEquals(expectedHeader[i], actualData[i]);
            } else {
                assertEquals(CpioUtil.swapBytes(expectedHeader[i]), actualData[i]);
            }
        }
        for (int i = 0; i < testData.length; i++) {
            assertEquals(testData[i], actualData[expectedHeader.length + i]);
        }
    }

    @Test
    public void testWriteDataAfterHeaderWithSwappedBytesAndCRC() throws IOException {
        // Test writing data after header with swapped bytes and CRC
        short swapHalfWord = 1;
        short crc = 0;
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        out.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        out.putNextEntry(entry);
        byte[] testData = "Hello, World!".getBytes();
        out.write(testData);
        byte[] expectedHeader = new byte[entry.getHeaderSize() + entry.getName().length() + 1];
        writeExpectedHeader(expectedHeader, entry, swapHalfWord);
        byte[] actualData = os.toByteArray();
        assertEquals(expectedHeader.length + testData.length, actualData.length);
        for (int i = 0; i < expectedHeader.length; i++) {
            if (i < expectedHeader.length / 2) {
                assertEquals(expectedHeader[i], actualData[i]);
            } else {
                assertEquals(CpioUtil.swapBytes(expectedHeader[i]), actualData[i]);
            }
        }
        for (int i = 0; i < testData.length; i++) {
            crc = (short) (crc ^ testData[i] & 0xFF);
            byte tmp = actualData[expectedHeader.length + i];
            byte swapped = CpioUtil.swapBytes(tmp);
            assertEquals(swapped, actualData[expectedHeader.length + i]);
        }
        assertEquals(CpioUtil.crc16(expectedHeader, 0, expectedHeader.length), crc);
    }

    @Test
    public void testFinish() throws IOException {
        // Test finish method
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        out.putNextEntry(entry);
        byte[] testData = "Hello, World!".getBytes();
        out.write(testData);
        out.finish();
        byte[] expectedHeader = new byte[entry.getHeaderSize() + entry.getName().length() + 1];
        writeExpectedHeader(expectedHeader, entry);
        byte[] actualHeader = new byte[os.size()];
        System.arraycopy(os.toByteArray(), 0, actualHeader, 0, actualHeader.length);
        byte[] actualData = new byte[testData.length];
        System.arraycopy(os.toByteArray(), entry.getHeaderSize() + entry.getName().length() + 1, actualData, 0, actualData.length);
        assertEquals(expectedHeader.length + testData.length, actualHeader.length);
        for (int i = 0; i < expectedHeader.length; i++) {
            assertEquals(expectedHeader[i], actualHeader[i]);
        }
        for (int i = 0; i < testData.length; i++) {
            assertEquals(testData[i], actualData[i]);
        }
    }

    private void writeExpectedHeader(byte[] header, CpioArchiveEntry entry) {
        int headerSize = entry.getHeaderSize();
        writeAsciiLong(header, headerSize - 8, 8, entry.getDevice());
        writeAsciiLong(header, headerSize - 4, 8, entry.getInode());
        writeAsciiLong(header, headerSize - 4, 8, entry.getMode());
        writeAsciiLong(header, headerSize - 8, 8, entry.getUID());
        writeAsciiLong(header, headerSize - 8, 8, entry.getGID());
        writeAsciiLong(header, headerSize - 4, 8, entry.getNumberOfLinks());
        writeAsciiLong(header, headerSize - 8, 8, entry.getTime());
        writeAsciiLong(header, headerSize - 8, 8, entry.getSize());
        writeAsciiLong(header, headerSize - 8, 8, entry.getDeviceMaj());
        writeAsciiLong(header, headerSize - 8, 8, entry.getDeviceMin());
        writeAsciiLong(header, headerSize - 8, 8, entry.getRemoteDeviceMaj());
        writeAsciiLong(header, headerSize - 8, 8, entry.getRemoteDeviceMin());
        writeAsciiLong(header, 0, 8, entry.getName().length() + 1);
        writeAsciiLong(header, headerSize - 4, 16, entry.getChksum());
        writeCString(header, entry.getName());
        pad(header, 4);
    }

    private void writeExpectedHeader(byte[] header, CpioArchiveEntry entry, boolean swapHalfWord) {
        int headerSize = entry.getHeaderSize();
        writeBinaryLong(header, headerSize - 4, swapHalfWord, entry.getDevice());
        writeBinaryLong(header, headerSize - 4, swapHalfWord, entry.getInode());
        writeBinaryLong(header, headerSize - 4, swapHalfWord, entry.getMode());
        writeBinaryLong(header, headerSize - 4, swapHalfWord, entry.getUID());
        writeBinaryLong(header, headerSize - 4, swapHalfWord, entry.getGID());
        writeBinaryLong(header, headerSize - 4, swapHalfWord, entry.getNumberOfLinks());
        writeBinaryLong(header, headerSize - 4, swapHalfWord, entry.getDeviceMaj());
        writeBinaryLong(header, headerSize - 4, swapHalfWord, entry.getDeviceMin());
        writeBinaryLong(header, headerSize - 4, swapHalfWord, entry.getRemoteDeviceMaj());
        writeBinaryLong(header, headerSize - 4, swapHalfWord, entry.getRemoteDeviceMin());
        writeBinaryLong(header, headerSize - 4, swapHalfWord, entry.getTime());
        writeBinaryLong(header, headerSize - 4, swapHalfWord, entry.getSize());
        writeCString(header, entry.getName());
        pad(header, 2);
    }

    private void writeAsciiLong(byte[] header, int position, int length, long number) {
        String numberStr = Long.toString(number, 16);
        int numberLength = numberStr.length();
        int paddingLength = length - numberLength;
        for (int i = 0; i < paddingLength; i++) {
            header[position + i] = (byte) '0';
        }
        for (int i = 0; i < numberLength; i++) {
            header[position + paddingLength + i] = (byte) numberStr.charAt(i);
        }
    }

    private void writeBinaryLong(byte[] header, int position, boolean swapHalfWord, long number) {
        byte[] longBytes = CpioUtil.long2byteArray(number, 4, swapHalfWord);
        System.arraycopy(longBytes, 0, header, position, 4);
    }

    private void writeCString(byte[] header, String str) {
        byte[] strBytes = str.getBytes();
        System.arraycopy(strBytes, 0, header, header.length - strBytes.length - 1, strBytes.length + 1);
    }

    private void pad(byte[] header, int border) {
        int paddingLength = header.length % border;
        if (paddingLength > 0) {
            for (int i = 0; i < paddingLength; i++) {
                header[header.length - 1 - i] = (byte) 0;
            }
        }
    }
}