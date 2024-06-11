package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;

public class TarArchiveOutputStreamTest {

    @Test
    public void testCloseWithNoEntries() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOos = new TarArchiveOutputStream(os);
        tarOos.finish();
        tarOos.close();
        byte[] bytes = os.toByteArray();
        assertArrayEquals(TarConstants.MAGIC_LONG, Arrays.copyOfRange(bytes, 0, 8), "Invalid magic header.");
        assertEquals(0, bytes.length - 8, "Invalid archive size.");
    }

    @Test
    public void testFinishWithoutClose() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOos = new TarArchiveOutputStream(os);
        tarOos.finish();
        assertThrows(IOException.class, () -> tarOos.close());
    }

    @Test
    public void testFinishWithUnclosedEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOos = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOos.putArchiveEntry(entry);
        tarOos.finish();
        assertThrows(IOException.class, () -> tarOos.close());
    }

    @Test
    public void testLongFileName() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/aaa/bbb/ccc/ddd/eee/fff/ggg/hhh";
        TarArchiveOutputStream tarOos = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOos.putArchiveEntry(entry);
        tarOos.finish();
        tarOos.close();
        byte[] bytes = os.toByteArray();
        assertArrayEquals(TarConstants.MAGIC_LONG, Arrays.copyOfRange(bytes, 0, 8), "Invalid magic header.");
        int longFileNameLength = longFileName.getBytes().length + 1; // +1 for NUL terminator
        assertEquals(longFileNameLength + 1, bytes.length - 8, "Invalid archive size.");
        assertArrayEquals(longFileName.getBytes(), Arrays.copyOfRange(bytes, 8, 8 + longFileNameLength), "Invalid long file name.");
        assertEquals(0, bytes[8 + longFileNameLength], "Invalid file mode.");
        assertEquals(0, bytes[8 + longFileNameLength + 1], "Invalid number of links.");
        assertEquals(0, bytes[8 + longFileNameLength + 2], "Invalid user id.");
        assertEquals(0, bytes[8 + longFileNameLength + 3], "Invalid group id.");
        assertEquals(0, bytes[8 + longFileNameLength + 4], "Invalid file size.");
        assertEquals(0, bytes[8 + longFileNameLength + 5], "Invalid modification time.");
        assertEquals(0, bytes[8 + longFileNameLength + 6], "Invalid checksum.");
        assertEquals(0, bytes[8 + longFileNameLength + 7], "Invalid number of file data blocks.");
        assertEquals(0, bytes[8 + longFileNameLength + 8], "Invalid number of link record blocks.");
        assertEquals(0, bytes[8 + longFileNameLength + 9], "Invalid file data block size.");
        assertEquals(0, bytes[8 + longFileNameLength + 10], "Invalid link record block size.");
    }

    @Test
    public void testWriteToArchive() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOos = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] data = "Hello, World!".getBytes();
        tarOos.putArchiveEntry(entry);
        tarOos.write(data, 0, data.length);
        tarOos.closeArchiveEntry();
        tarOos.finish();
        tarOos.close();
        byte[] bytes = os.toByteArray();
        assertArrayEquals(TarConstants.MAGIC_LONG, Arrays.copyOfRange(bytes, 0, 8), "Invalid magic header.");
        int longFileNameLength = "test.txt".getBytes().length + 1; // +1 for NUL terminator
        assertEquals(longFileNameLength + 1 + data.length, bytes.length - 8, "Invalid archive size.");
        assertArrayEquals(longFileNameLength + 1, Arrays.copyOfRange(bytes, 8, 8 + longFileNameLength + 1), "Invalid file name.");
        assertEquals(0644, bytes[8 + longFileNameLength + 1], "Invalid file permissions.");
        assertEquals(0, bytes[8 + longFileNameLength + 2], "Invalid number of links.");
        assertEquals(1, bytes[8 + longFileNameLength + 3], "Invalid user id.");
        assertEquals(1, bytes[8 + longFileNameLength + 4], "Invalid group id.");
        long fileSize = 11; // 10 bytes of data + 1 byte for NUL terminator
        assertEquals((int) fileSize, bytes[8 + longFileNameLength + 4], "Invalid file size.");
        long modificationTime = 1234567890123L;
        assertEquals((int) (modificationTime >> 32), bytes[8 + longFileNameLength + 5], "Invalid modification time.");
        assertEquals((int) (modificationTime & 0xFFFFFFFFL), bytes[8 + longFileNameLength + 4], "Invalid modification time.");
        assertEquals(0, bytes[8 + longFileNameLength + 6], "Invalid checksum.");
        assertEquals(0, bytes[8 + longFileNameLength + 7], "Invalid number of file data blocks.");
        assertEquals(1, bytes[8 + longFileNameLength + 8], "Invalid number of link record blocks.");
        assertEquals(0, bytes[8 + longFileNameLength + 9], "Invalid file data block size.");
        assertEquals(0, bytes[8 + longFileNameLength + 10], "Invalid link record block size.");
        assertEquals(data.length, bytes.length - (8 + longFileNameLength + 1), "Invalid data size.");
        assertArrayEquals(data, Arrays.copyOfRange(bytes, 8 + longFileNameLength + 1, bytes.length), "Invalid data.");
    }

    @Test
    public void testWriteMultipleBlocks() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOos = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] data1 = "Hello, World!".getBytes();
        byte[] data2 = "This is a test.".getBytes();
        tarOos.putArchiveEntry(entry);
        tarOos.write(data1, 0, data1.length);
        tarOos.write(data2, 0, data2.length);
        tarOos.closeArchiveEntry();
        tarOos.finish();
        tarOos.close();
        byte[] bytes = os.toByteArray();
        assertArrayEquals(TarConstants.MAGIC_LONG, Arrays.copyOfRange(bytes, 0, 8), "Invalid magic header.");
        int longFileNameLength = "test.txt".getBytes().length + 1; // +1 for NUL terminator
        assertEquals(longFileNameLength + 1 + data1.length + data2.length, bytes.length - 8, "Invalid archive size.");
        assertArrayEquals(longFileNameLength + 1, Arrays.copyOfRange(bytes, 8, 8 + longFileNameLength + 1), "Invalid file name.");
        assertEquals(0644, bytes[8 + longFileNameLength + 1], "Invalid file permissions.");
        assertEquals(0, bytes[8 + longFileNameLength + 2], "Invalid number of links.");
        assertEquals(1, bytes[8 + longFileNameLength + 3], "Invalid user id.");
        assertEquals(1, bytes[8 + longFileNameLength + 4], "Invalid group id.");
        long fileSize = 11 + data2.length; // 10 bytes of data1 + 1 byte for NUL terminator + 11 bytes of data2
        assertEquals((int) fileSize, bytes[8 + longFileNameLength + 4], "Invalid file size.");
        long modificationTime = 1234567890123L;
        assertEquals((int) (modificationTime >> 32), bytes[8 + longFileNameLength + 5], "Invalid modification time.");
        assertEquals((int) (modificationTime & 0xFFFFFFFFL), bytes[8 + longFileNameLength + 4], "Invalid modification time.");
        assertEquals(0, bytes[8 + longFileNameLength + 6], "Invalid checksum.");
        assertEquals(2, bytes[8 + longFileNameLength + 7], "Invalid number of file data blocks.");
        assertEquals(0, bytes[8 + longFileNameLength + 8], "Invalid number of link record blocks.");
        assertEquals(0, bytes[8 + longFileNameLength + 9], "Invalid file data block size.");
        assertEquals(0, bytes[8 + longFileNameLength + 10], "Invalid link record block size.");
        assertEquals(data1.length, bytes.length - (8 + longFileNameLength + 1 + 512), "Invalid data size.");
        assertArrayEquals(data1, Arrays.copyOfRange(bytes, 8 + longFileNameLength + 1 + 512, bytes.length - 512), "Invalid data1.");
        assertEquals(data2.length, bytes.length - (8 + longFileNameLength + 1 + data1.length + 512), "Invalid data2.");
        assertArrayEquals(data2, Arrays.copyOfRange(bytes, bytes.length - data2.length, bytes.length), "Invalid data2.");
    }

    @Test
    public void testWriteLongFileNameWithMultipleBlocks() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/aaa/bbb/ccc/ddd/eee/fff/ggg/hhh";
        TarArchiveOutputStream tarOos = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        byte[] data = "Hello, World!".getBytes();
        tarOos.putArchiveEntry(entry);
        tarOos.write(data, 0, data.length);
        tarOos.finish();
        tarOos.close();
        byte[] bytes = os.toByteArray();
        int longFileNameLength = longFileName.getBytes().length + 1; // +1 for NUL terminator
        int longFileNameLengthWithBlockSize = longFileNameLength + 512;
        assertEquals(longFileNameLengthWithBlockSize * 2 + data.length, bytes.length - 8, "Invalid archive size.");
        assertArrayEquals(TarConstants.MAGIC_LONG, Arrays.copyOfRange(bytes, 0, 8), "Invalid magic header.");
        assertArrayEquals(longFileName.getBytes(), Arrays.copyOfRange(bytes, 8, 8 + longFileNameLength), "Invalid long file name.");
        assertEquals(0644, bytes[8 + longFileNameLength + 1], "Invalid file permissions.");
        assertEquals(0, bytes[8 + longFileNameLength + 2], "Invalid number of links.");
        assertEquals(1, bytes[8 + longFileNameLength + 3], "Invalid user id.");
        assertEquals(1, bytes[8 + longFileNameLength + 4], "Invalid group id.");
        long fileSize = 11 + data.length; // 10 bytes of data + 1 byte for NUL terminator
        assertEquals((int) fileSize, bytes[8 + longFileNameLength + 4], "Invalid file size.");
        long modificationTime = 1234567890123L;
        assertEquals((int) (modificationTime >> 32), bytes[8 + longFileNameLength + 5], "Invalid modification time.");
        assertEquals((int) (modificationTime & 0xFFFFFFFFL), bytes[8 + longFileNameLength + 4], "Invalid modification time.");
        assertEquals(0, bytes[8 + longFileNameLength + 6], "Invalid checksum.");
        assertEquals(1, bytes[8 + longFileNameLength + 7], "Invalid number of file data blocks.");
        assertEquals(0, bytes[8 + longFileNameLength + 8], "Invalid number of link record blocks.");
        assertEquals(0, bytes[8 + longFileNameLength + 9], "Invalid file data block size.");
        assertEquals(0, bytes[8 + longFileNameLength + 10], "Invalid link record block size.");
        assertEquals(data.length, bytes.length - (8 + longFileNameLengthWithBlockSize), "Invalid data size.");
        assertArrayEquals(data, Arrays.copyOfRange(bytes, bytes.length - data.length, bytes.length), "Invalid data.");
        byte[] firstBlock = new byte[512];
        System.arraycopy(Arrays.copyOfRange(bytes, 8, 8 + longFileNameLength), 0, firstBlock, 0, longFileNameLength);
        byte[] secondBlock = new byte[512];
        System.arraycopy(Arrays.copyOfRange(bytes, bytes.length - 512, bytes.length), 0, secondBlock, 0, 512);
        assertArrayEquals(firstBlock, Arrays.copyOfRange(bytes, 8, bytes.length - 512), "Invalid first block.");
        assertArrayEquals(secondBlock, data, "Invalid second block.");
    }

}