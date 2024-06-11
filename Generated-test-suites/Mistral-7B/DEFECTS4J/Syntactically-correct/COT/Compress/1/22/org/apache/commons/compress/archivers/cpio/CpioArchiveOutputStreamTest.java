package org.apache.commons.compress.archivers.cpio;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.util.CpioUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;
    private Map<String, CpioArchiveEntry> entries;

    @Before
    public void setup() throws Exception {
        outputStream = new MemoryOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_BINARY);
        entries = new HashMap<>();
    }

    @Test
    public void testNewInstance() throws Exception {
        CpioArchiveOutputStream actual = new CpioArchiveOutputStream(new ByteArrayOutputStream(), CpioConstants.FORMAT_NEW);
        Assert.assertNotNull(actual);
    }

    @Test
    public void testPutNextEntryWithValidInput() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(10);

        cpioOutputStream.putNextEntry(entry);

        byte[] expectedHeader = getExpectedHeaderForTestPutNextEntryWithValidInput();
        byte[] actualHeader = getBytesFromOutputStream(cpioOutputStream.getOutputStream());

        Assert.assertArrayEquals(expectedHeader, actualHeader);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateEntryName() throws Exception {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile");
        cpioOutputStream.putNextEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile");

        cpioOutputStream.putNextEntry(entry2);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithNullEntry() throws Exception {
        cpioOutputStream.putNextEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithInvalidEntryFormat() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(CpioConstants.FORMAT_INVALID);

        cpioOutputStream.putNextEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithInvalidHeaderFormat() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        entry.setFormat(CpioConstants.FORMAT_NEW); // Change format after setting

        cpioOutputStream.putNextEntry(entry);
    }

    @Test
    public void testWrite() throws Exception {
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(testData.length);

        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write(testData);
        cpioOutputStream.closeArchiveEntry();

        byte[] expectedData = getExpectedDataForTestWrite();
        byte[] actualData = getBytesFromOutputStream(cpioOutputStream.getOutputStream());

        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws Exception {
        cpioOutputStream.close();
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);

        cpioOutputStream.write(testData);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullData() throws Exception {
        cpioOutputStream.write(null);
    }

    @Test(expected = IOException.class)
    public void testWriteWithDataBeyondEntrySize() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(5);

        cpioOutputStream.putNextEntry(entry);
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);

        cpioOutputStream.write(testData, 5, testData.length - 5);
    }

    @Test
    public void testFinish() throws Exception {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("entry1.txt");
        entry1.setSize(10);

        cpioOutputStream.putNextEntry(entry1);
        byte[] testData1 = "Entry 1 data".getBytes(StandardCharsets.UTF_8);
        cpioOutputStream.write(testData1);
        cpioOutputStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("entry2.txt");
        entry2.setSize(15);

        cpioOutputStream.putNextEntry(entry2);
        byte[] testData2 = "Entry 2 data".getBytes(StandardCharsets.UTF_8);
        cpioOutputStream.write(testData2);
        cpioOutputStream.closeArchiveEntry();

        cpioOutputStream.finish();

        byte[] expectedTrailer = getExpectedTrailerForTestFinish();
        byte[] actualTrailer = getBytesFromOutputStream(cpioOutputStream.getOutputStream());

        Assert.assertArrayEquals(expectedTrailer, actualTrailer);
    }

    @Test(expected = IOException.class)
    public void testFinishWithClosedStream() throws Exception {
        cpioOutputStream.close();
        cpioOutputStream.finish();
    }

    @Test
    public void testClose() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(10);

        cpioOutputStream.putNextEntry(entry);
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        cpioOutputStream.write(testData);
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.close();

        byte[] expectedEmptyStream = new byte[0];
        byte[] actualEmptyStream = getBytesFromOutputStream(cpioOutputStream.getOutputStream());

        Assert.assertArrayEquals(expectedEmptyStream, actualEmptyStream);
    }

    private byte[] getExpectedHeaderForTestPutNextEntryWithValidInput() throws Exception {
        byte[] format = CpioConstants.MAGIC_OLD_BINARY;
        byte[] magic = new byte[2];
        CpioUtil.writeShort(magic, 0, format.length);
        byte[] device = CpioUtil.long2byteArray(0, 2, false);
        byte[] inode = CpioUtil.long2byteArray(1, 2, false);
        byte[] mode = CpioUtil.long2byteArray(0777, 3, false);
        byte[] uid = CpioUtil.long2byteArray(1001, 3, false);
        byte[] gid = CpioUtil.long2byteArray(1001, 3, false);
        byte[] numberOfLinks = CpioUtil.long2byteArray(1, 2, false);
        byte[] time = CpioUtil.long2byteArray(System.currentTimeMillis(), 4, false);
        byte[] size = CpioUtil.long2byteArray(10, 4, false);
        byte[] deviceMaj = CpioUtil.long2byteArray(0, 2, false);
        byte[] deviceMin = CpioUtil.long2byteArray(0, 2, false);
        byte[] remoteDeviceMaj = CpioUtil.long2byteArray(0, 2, false);
        byte[] remoteDeviceMin = CpioUtil.long2byteArray(0, 2, false);
        byte[] nameLength = CpioUtil.long2byteArray(5, 2, false);
        byte[] chksum = CpioUtil.long2byteArray(0, 2, false);
        byte[] name = "testfile".getBytes(StandardCharsets.UTF_8);
        byte[] padding = new byte[2];

        byte[] expectedHeader = new byte[44 + name.length + 1];

        System.arraycopy(format, 0, expectedHeader, 0, format.length);
        System.arraycopy(device, 0, expectedHeader, 2, device.length);
        System.arraycopy(inode, 0, expectedHeader, 4, inode.length);
        System.arraycopy(mode, 0, expectedHeader, 6, mode.length);
        System.arraycopy(uid, 0, expectedHeader, 10, uid.length);
        System.arraycopy(gid, 0, expectedHeader, 14, gid.length);
        System.arraycopy(numberOfLinks, 0, expectedHeader, 18, numberOfLinks.length);
        System.arraycopy(time, 0, expectedHeader, 22, time.length);
        System.arraycopy(size, 0, expectedHeader, 26, size.length);
        System.arraycopy(deviceMaj, 0, expectedHeader, 30, deviceMaj.length);
        System.arraycopy(deviceMin, 0, expectedHeader, 32, deviceMin.length);
        System.arraycopy(remoteDeviceMaj, 0, expectedHeader, 34, remoteDeviceMaj.length);
        System.arraycopy(remoteDeviceMin, 0, expectedHeader, 36, remoteDeviceMin.length);
        System.arraycopy(nameLength, 0, expectedHeader, 38, nameLength.length);
        System.arraycopy(chksum, 0, expectedHeader, 40, chksum.length);
        System.arraycopy(name, 0, expectedHeader, 42, name.length);

        return expectedHeader;
    }

    private byte[] getExpectedDataForTestWrite() throws Exception {
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);

        return testData;
    }

    private byte[] getExpectedTrailerForTestFinish() throws Exception {
        byte[] trailerMagic = CpioConstants.MAGIC_TRAILER;
        byte[] nameSize = CpioUtil.long2byteArray(44, 2, false);
        byte[] nameLength = CpioUtil.long2byteArray(5, 2, false);

        byte[] expectedTrailer = new byte[2 + 4];

        System.arraycopy(trailerMagic, 0, expectedTrailer, 0, trailerMagic.length);
        System.arraycopy(nameSize, 0, expectedTrailer, 2, nameSize.length);
        System.arraycopy(nameLength, 0, expectedTrailer, 4, nameLength.length);

        return expectedTrailer;
    }

    private byte[] getBytesFromOutputStream(OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[outputStream.size()];
        outputStream.read(buffer);

        return buffer;
    }

    private class MemoryOutputStream extends FilterOutputStream {
        private final byte[] buffer = new byte[1024];
        private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        public MemoryOutputStream() {
            super(baos);
        }

        @Override
        public void write(int b) throws IOException {
            super.write(b);
            buffer[pos++] = (byte) b;
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            super.write(b, off, len);
            System.arraycopy(b, off, buffer, pos, len);
            pos += len;
        }

        public byte[] getBytes() {
            return baos.toByteArray();
        }

        private int pos = 0;
    }
}