package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntryWithValidArguments() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String entryName = "testEntry.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setSize(10);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        assertNull(cpioArchiveOutputStream.cpioEntry);
        assertEquals(10, cpioArchiveOutputStream.written);
    }

    @Test
    public void testPutNextEntryWithValidArgumentsAndCustomFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_BINARY);

        String entryName = "testEntry.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setSize(10);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        assertNull(cpioArchiveOutputStream.cpioEntry);
        assertEquals(10, cpioArchiveOutputStream.written);
    }

    @Test
    public void testPutNextEntryWithDuplicateEntryName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String entryName = "testEntry.txt";
        CpioArchiveEntry cpioArchiveEntry1 = new CpioArchiveEntry();
        cpioArchiveEntry1.setName(entryName);
        cpioArchiveEntry1.setSize(10);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry1);
        cpioArchiveEntry1 = new CpioArchiveEntry();
        cpioArchiveEntry1.setName(entryName);

        assertThrows(IOException.class, () -> cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry1));
    }

    @Test
    public void testWriteToOutputStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String entryName = "testEntry.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setSize(10);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        byte[] testBytes = "Test Data".getBytes();
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedOutput = getExpectedOutput(entryName, testBytes);
        byte[] actualOutput = outputStream.toByteArray();

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWriteToOutputStreamWithCustomFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_BINARY);

        String entryName = "testEntry.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setSize(10);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        byte[] testBytes = "Test Data".getBytes();
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedOutput = getExpectedOutputForOldFormat(entryName, testBytes);
        byte[] actualOutput = outputStream.toByteArray();

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCloseArchiveEntryWithValidArguments() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String entryName = "testEntry.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setSize(10);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        byte[] testBytes = "Test Data".getBytes();
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);

        cpioArchiveOutputStream.closeArchiveEntry();

        assertNull(cpioArchiveOutputStream.cpioEntry);
        assertEquals(10, cpioArchiveOutputStream.written);
    }

    @Test
    public void testCloseArchiveEntryWithInvalidArguments() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String entryName = "testEntry.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setSize(10);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        byte[] testBytes = "Test Data".getBytes();
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);

        cpioArchiveEntry = null;
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.closeArchiveEntry());
    }

    @Test
    public void testFinishMethodWithValidArguments() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String entryName = "testEntry.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setSize(10);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        byte[] testBytes = "Test Data".getBytes();
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);
        cpioArchiveOutputStream.finish();

        byte[] expectedOutput = getExpectedOutput(entryName, testBytes);
        byte[] actualOutput = outputStream.toByteArray();

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFinishMethodWithInvalidArguments() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        assertThrows(IOException.class, () -> cpioArchiveOutputStream.finish());
    }

    @Test
    public void testCloseMethodWithValidArguments() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        assertThrows(IOException.class, () -> cpioArchiveOutputStream.close());
    }

    @Test
    public void testCloseMethodWithInvalidArguments() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();

        assertNull(cpioArchiveOutputStream.cpioEntry);
        assertEquals(0, cpioArchiveOutputStream.written);
    }

    private byte[] getExpectedOutput(String entryName, byte[] testBytes) {
        byte[] header = CpioUtil.long2byteArray(CpioConstants.MAGIC_NEW[0], 1);
        byte[] inode = CpioUtil.long2byteArray(1, 7);
        byte[] mode = CpioUtil.long2byteArray(0755, 3);
        byte[] uid = CpioUtil.long2byteArray(1001, 4);
        byte[] gid = CpioUtil.long2byteArray(1001, 4);
        byte[] links = CpioUtil.long2byteArray(1, 1);
        byte[] mtime = CpioUtil.long2byteArray(System.currentTimeMillis() / 1000, 8);
        byte[] size = CpioUtil.long2byteArray(testBytes.length, 8);
        byte[] checksum = new byte[8];

        for (int i = 0; i < testBytes.length; i++) {
            checksum[i % 2] = (byte) (testBytes[i] ^ (i % 2 + 1));
        }

        byte[] nameLength = CpioUtil.long2byteArray(entryName.length() + 1, 8);

        byte[] nameBytes = entryName.getBytes();

        byte[] trailer = CpioUtil.long2byteArray(0, 1);

        return CpioUtil.concatenate(header, inode, mode, uid, gid, links, mtime,
                size, checksum, nameLength, nameBytes, trailer);
    }

    private byte[] getExpectedOutputForOldFormat(String entryName, byte[] testBytes) {
        byte[] header = CpioUtil.long2byteArray(CpioConstants.MAGIC_OLD_BINARY[0], 2);
        byte[] device = CpioUtil.long2byteArray(0, 2);
        byte[] inode = CpioUtil.long2byteArray(1, 3);
        byte[] mode = CpioUtil.long2byteArray(0755, 3);
        byte[] uid = CpioUtil.long2byteArray(1001, 4);
        byte[] gid = CpioUtil.long2byteArray(1001, 4);
        byte[] links = CpioUtil.long2byteArray(1, 3);
        byte[] mtime = CpioUtil.long2byteArray(System.currentTimeMillis() / 1000, 4);
        byte[] nameLength = CpioUtil.long2byteArray(entryName.length() + 1, 2);
        byte[] size = CpioUtil.long2byteArray(testBytes.length, 4);
        byte[] nameBytes = entryName.getBytes();
        byte[] trailer = new byte[2];

        for (int i = 0; i < 2; i++) {
            trailer[i] = (byte) (i == 0 ? 0xFF : 0x00);
        }

        return CpioUtil.concatenate(header, device, inode, mode, uid, gid, links,
                mtime, nameLength, size, nameBytes, trailer);
    }
}