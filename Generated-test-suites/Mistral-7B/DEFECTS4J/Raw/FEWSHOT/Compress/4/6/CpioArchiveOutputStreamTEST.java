package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.CpioConstants.CpioFormat;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link CpioArchiveOutputStream}
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream out;
    private CpioArchiveOutputStream cpioOut;

    @Before
    public void setUp() throws IOException {
        out = new ByteArrayOutputStream();
        cpioOut = new CpioArchiveOutputStream(out, CpioFormat.FORMAT_NEW);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("entryName", "testData".getBytes());
        cpioOut.putArchiveEntry(entry);

        StringBuilder expected = new StringBuilder();
        expected.append(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW));
        expected.append(writeNewEntryHeader(entry));
        expected.append("testData".getBytes());
        expected.append('\0');

        byte[] actual = out.toByteArray();
        assertArrayEquals(expected.toString().getBytes(), actual);
    }

    @Test
    public void testPutArchiveEntryWithTrailingZeroes() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("entryName", "testData\u0000".getBytes());
        cpioOut.putArchiveEntry(entry);

        StringBuilder expected = new StringBuilder();
        expected.append(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW));
        expected.append(writeNewEntryHeader(entry));
        expected.append("testData\u0000".getBytes());
        expected.append('\0');

        byte[] actual = out.toByteArray();
        assertArrayEquals(expected.toString().getBytes(), actual);
    }

    @Test
    public void testPutArchiveEntryWithNegativeSize() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("entryName", "testData".getBytes(), -1);
        try {
            cpioOut.putArchiveEntry(entry);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        try {
            cpioOut.putArchiveEntry(null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry("entryName", "testData".getBytes());
        cpioOut.putArchiveEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry("entryName", "testData".getBytes());
        try {
            cpioOut.putArchiveEntry(entry2);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("entryName", "testData".getBytes());
        entry.setFormat(CpioFormat.FORMAT_OLD_BINARY.getValue() - 1);
        try {
            cpioOut.putArchiveEntry(entry);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testPutArchiveEntryWithFormatCRC() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("entryName", "testData".getBytes());
        entry.setFormat(CpioFormat.FORMAT_NEW_CRC.getValue());
        cpioOut.putArchiveEntry(entry);

        byte[] actual = out.toByteArray();
        byte[] expected = getExpectedCrcData("testData");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testPutArchiveEntryWithMultipleEntries() throws IOException {
        byte[] testData1 = "testData1".getBytes();
        byte[] testData2 = "testData2".getBytes();

        CpioArchiveEntry entry1 = new CpioArchiveEntry("entryName1", testData1);
        CpioArchiveEntry entry2 = new CpioArchiveEntry("entryName2", testData2);

        cpioOut.putArchiveEntry(entry1);
        cpioOut.putArchiveEntry(entry2);

        StringBuilder expected = new StringBuilder();
        expected.append(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW));
        expected.append(writeNewEntryHeader(entry1));
        expected.append(testData1);
        expected.append('\0');
        expected.append(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW));
        expected.append(writeNewEntryHeader(entry2));
        expected.append(testData2);
        expected.append('\0');

        byte[] actual = out.toByteArray();
        assertArrayEquals(expected.toString().getBytes(), actual);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("entryName", "testData".getBytes());
        cpioOut.putArchiveEntry(entry);
        cpioOut.write(0);
        cpioOut.closeArchiveEntry();

        byte[] actual = out.toByteArray();
        StringBuilder expected = new StringBuilder();
        expected.append(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW));
        expected.append(writeNewEntryHeader(entry));
        expected.append("testData".getBytes());
        expected.append('\0');
        expected.append(ArchiveUtils.toAsciiBytes(CpioConstants.CPIO_TRAILER));
        expected.append("\u0000".getBytes());
        expected.append('\0');

        assertArrayEquals(expected.toString().getBytes(), actual);
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("entryName", "testData".getBytes());
        cpioOut.putArchiveEntry(entry);

        byte[] testData = "testData".getBytes();
        cpioOut.write(testData, 0, testData.length);

        byte[] actual = out.toByteArray();
        StringBuilder expected = new StringBuilder();
        expected.append(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW));
        expected.append(writeNewEntryHeader(entry));
        expected.append(testData);
        expected.append('\0');

        assertArrayEquals(expected.toString().getBytes(), actual);
    }

    @Test
    public void testWriteWithClosedEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("entryName", "testData".getBytes());
        cpioOut.putArchiveEntry(entry);
        cpioOut.closeArchiveEntry();

        byte[] testData = "testData".getBytes();
        try {
            cpioOut.write(testData, 0, testData.length);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("entryName", "testData".getBytes());
        cpioOut.putArchiveEntry(entry);
        cpioOut.write("testData".getBytes());
        cpioOut.finish();

        byte[] actual = out.toByteArray();
        StringBuilder expected = new StringBuilder();
        expected.append(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW));
        expected.append(writeNewEntryHeader(entry));
        expected.append("testData".getBytes());
        expected.append('\0');
        expected.append(ArchiveUtils.toAsciiBytes(CpioConstants.CPIO_TRAILER));
        expected.append("\u0000".getBytes());
        expected.append('\0');

        assertArrayEquals(expected.toString().getBytes(), actual);
    }

    @Test
    public void testFinishWithEntries() throws IOException {
        byte[] testData1 = "testData1".getBytes();
        byte[] testData2 = "testData2".getBytes();

        CpioArchiveEntry entry1 = new CpioArchiveEntry("entryName1", testData1);
        CpioArchiveEntry entry2 = new CpioArchiveEntry("entryName2", testData2);

        cpioOut.putArchiveEntry(entry1);
        cpioOut.write(testData1);
        cpioOut.putArchiveEntry(entry2);
        cpioOut.write(testData2);
        cpioOut.finish();

        byte[] actual = out.toByteArray();
        StringBuilder expected = new StringBuilder();
        expected.append(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW));
        expected.append(writeNewEntryHeader(entry1));
        expected.append(testData1);
        expected.append('\0');
        expected.append(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW));
        expected.append(writeNewEntryHeader(entry2));
        expected.append(testData2);
        expected.append('\0');
        expected.append(ArchiveUtils.toAsciiBytes(CpioConstants.CPIO_TRAILER));
        expected.append("\u0000".getBytes());
        expected.append('\0');

        assertArrayEquals(expected.toString().getBytes(), actual);
    }

    @Test
    public void testClose() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("entryName", "testData".getBytes());
        cpioOut.putArchiveEntry(entry);
        cpioOut.write(0);
        cpioOut.close();

        byte[] actual = out.toByteArray();
        StringBuilder expected = new StringBuilder();
        expected.append(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW));
        expected.append(writeNewEntryHeader(entry));
        expected.append("testData".getBytes());
        expected.append('\0');
        expected.append(ArchiveUtils.toAsciiBytes(CpioConstants.CPIO_TRAILER));
        expected.append("\u0000".getBytes());
        expected.append('\0');

        assertArrayEquals(expected.toString().getBytes(), actual);
    }

    @Test
    public void testCloseWithEntries() throws IOException {
        byte[] testData1 = "testData1".getBytes();
        byte[] testData2 = "testData2".getBytes();

        CpioArchiveEntry entry1 = new CpioArchiveEntry("entryName1", testData1);
        CpioArchiveEntry entry2 = new CpioArchiveEntry("entryName2", testData2);

        cpioOut.putArchiveEntry(entry1);
        cpioOut.write(testData1);
        cpioOut.putArchiveEntry(entry2);
        cpioOut.write(testData2);
        cpioOut.close();

        byte[] actual = out.toByteArray();
        StringBuilder expected = new StringBuilder();
        expected.append(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW));
        expected.append(writeNewEntryHeader(entry1));
        expected.append(testData1);
        expected.append('\0');
        expected.append(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW));
        expected.append(writeNewEntryHeader(entry2));
        expected.append(testData2);
        expected.append('\0');
        expected.append(ArchiveUtils.toAsciiBytes(CpioConstants.CPIO_TRAILER));
        expected.append("\u0000".getBytes());
        expected.append('\0');

        assertArrayEquals(expected.toString().getBytes(), actual);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("testFile");
        CpioArchiveEntry entry = cpioOut.createArchiveEntry(file, "testFile");

        assertSame(file.getName(), entry.getName());
    }

    @Test
    public void testCreateArchiveEntryWithNullName() throws IOException {
        File file = new File("testFile");
        try {
            CpioArchiveEntry entry = cpioOut.createArchiveEntry(file, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    private String writeNewEntryHeader(CpioArchiveEntry entry) {
        StringBuilder sb = new StringBuilder();

        // name
        int nameLength = entry.getName().length() + 1;
        sb.append(Integer.toUnsignedString(nameLength, 8));
        char[] nameChars = entry.getName().toCharArray();
        for (char c : nameChars) {
            if (Character.isISOControl(c)) {
                throw new IllegalArgumentException("Character is not printable: " + c);
            }
            sb.append(Character.isAlphanumeric(c) ? Character.toUpperCase(c) : "_" + Integer.toHexString((int) c).substring(1));
        }

        // format
        sb.append(Short.toUnsignedString((short) entry.getFormat().getValue(), 16));

        // mode
        sb.append(Integer.toUnsignedString(entry.getMode().intValue(), 8));

        // uid
        sb.append(Integer.toUnsignedString(entry.getUid(), 8));

        // gid
        sb.append(Integer.toUnsignedString(entry.getGid(), 8));

        // number of links
        sb.append(Integer.toUnsignedString(entry.getNumberOfLinks(), 8));

        // time
        long time = entry.getTime();
        int timeLength = (time > 0) ? Long.numberOfLeadingZeros(Long.bitLength(time)) : 12;
        sb.append(Integer.toUnsignedString((int) (time >> 32), 8));
        sb.append(Integer.toUnsignedString((int) time, 8));

        // size
        sb.append(Integer.toUnsignedString(entry.getSize(), 11));

        // device maj
        sb.append(Integer.toUnsignedString(entry.getDeviceMaj(), 8));

        // device min
        sb.append(Integer.toUnsignedString(entry.getDeviceMin(), 8));

        // remote device maj
        sb.append(Integer.toUnsignedString(entry.getRemoteDeviceMaj(), 8));

        // remote device min
        sb.append(Integer.toUnsignedString(entry.getRemoteDeviceMin(), 8));

        // checksum
        sb.append(Short.toUnsignedString((short) 0, 16)); // TODO: implement checksum calculation

        return sb.toString();
    }

    private byte[] getExpectedCrcData(String data) throws IOException {
        byte[] buffer = new byte[data.length()];
        cpioOut.putArchiveEntry(new CpioArchiveEntry("test", data.getBytes()));
        cpioOut.write(data.getBytes());
        cpioOut.finish();
        cpioOut.close();
        return out.toByteArray();
    }
}