package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;

public class TarArchiveOutputStreamTest {

    @Test
    public void testNewInstance() {
        // Test empty constructor
        TarArchiveOutputStream stream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        assertNull(stream.getArchiveEntry());
        assertFalse(stream.isInOutputStream());

        // Test with blockSize and recordSize
        TarArchiveOutputStream stream2 = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), 512, 512);
        assertSame(512, stream2.getBlockSize());
        assertSame(512, stream2.getRecordSize());

        // Test with OutputStream, blockSize and recordSize
        TarArchiveOutputStream stream3 = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), 512, 512);
        assertSame(new ByteArrayOutputStream(1024), stream3.getOutputStream());
        assertSame(512, stream3.getBlockSize());
        assertSame(512, stream3.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream stream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), 512, 512);
        stream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, stream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream stream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), 512, 512);
        stream.finish();
        assertEquals(1024, stream.getBytesWritten());
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream stream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), 512, 512);
        stream.close();
        assertTrue(stream.isClosed());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream stream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), 512, 512);
        byte[] data = "Hello World".getBytes(StandardCharsets.UTF_8);
        stream.write(data);
        assertEquals(10, stream.getBytesWritten());
        assertArrayEquals(data, Arrays.copyOfRange(stream.getOutputStream().toByteArray(), 0, 10));
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream stream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), 512, 512);
        File testFile = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        stream.putArchiveEntry(entry);
        assertEquals(41, stream.getBytesWritten());
        assertArrayEquals(testFile.getName().getBytes(StandardCharsets.UTF_8),
                         Arrays.copyOfRange(stream.getOutputStream().toByteArray(), 0, TarConstants.NAMELEN));
        assertTrue(Arrays.equals(testFile.length(), stream.getBytesWritten() - TarConstants.NAMELEN));
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveOutputStream stream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), 512, 512);
        stream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        File testFile = new File("testLongFileName.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testLongFileName.txt");
        stream.putArchiveEntry(entry);
        assertEquals(49 + testFile.getName().length(), stream.getBytesWritten());
        // The GNU tar extension header is 49 bytes long
        assertArrayEquals(testFile.getName().getBytes(StandardCharsets.UTF_8),
                         Arrays.copyOfRange(stream.getOutputStream().toByteArray(), 0, 49 + testFile.getName().length()));
        assertTrue(Arrays.equals(testFile.length(), stream.getBytesWritten() - (49 + testFile.getName().length())));
    }

    @Test
    public void testPutArchiveEntryWithTruncatedLongFileName() throws IOException {
        TarArchiveOutputStream stream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), 512, 512);
        stream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        File testFile = new File("testLongFileName.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testLongFileName.txt");
        stream.putArchiveEntry(entry);
        assertEquals(TarConstants.NAMELEN, stream.getBytesWritten());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream stream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), 512, 512);
        File testFile = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        stream.putArchiveEntry(entry);
        stream.write("Hello World".getBytes(StandardCharsets.UTF_8));
        stream.closeArchiveEntry();
        assertEquals(41 + 10, stream.getBytesWritten());
        assertArrayEquals(testFile.getName().getBytes(StandardCharsets.UTF_8),
                         Arrays.copyOfRange(stream.getOutputStream().toByteArray(), 0, TarConstants.NAMELEN));
        assertTrue(Arrays.equals(testFile.length(), stream.getBytesWritten() - TarConstants.NAMELEN));
    }

    @Test
    public void testWriteWithLongFileNameEntry() throws IOException {
        TarArchiveOutputStream stream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), 512, 512);
        File testFile = new File("testLongFileName.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testLongFileName.txt");
        stream.putArchiveEntry(entry);
        byte[] data = "Hello World".getBytes(StandardCharsets.UTF_8);
        stream.write(data);
        assertEquals(49 + testFile.getName().length() + data.length, stream.getBytesWritten());
        // The GNU tar extension header is 49 bytes long
        assertArrayEquals(testFile.getName().getBytes(StandardCharsets.UTF_8),
                         Arrays.copyOfRange(stream.getOutputStream().toByteArray(), 0, 49 + testFile.getName().length()));
        assertArrayEquals(data, Arrays.copyOfRange(stream.getOutputStream().toByteArray(), 49 + testFile.getName().length(), 49 + testFile.getName().length() + data.length));
    }
}