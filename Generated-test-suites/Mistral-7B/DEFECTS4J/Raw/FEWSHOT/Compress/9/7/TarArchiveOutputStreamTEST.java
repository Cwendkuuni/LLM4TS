package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.CountingInputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.TarConstants;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testDefaultConstructor() {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, instance.getBlockSize());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, instance.getRecordSize());
    }

    @Test
    public void testBlockSizeConstructor() {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        assertEquals(512, instance.getBlockSize());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, instance.getRecordSize());
    }

    @Test
    public void testRecordSizeConstructor() {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        assertEquals(512, instance.getBlockSize());
        assertEquals(1024, instance.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        instance.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, instance.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidMode() {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        instance.setLongFileMode(5);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        instance.finish();
        assertTrue(instance.isFinished());
    }

    @Test(expected = IOException.class)
    public void testFinishAlreadyFinished() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        instance.finish();
        instance.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        instance.putArchiveEntry(entry);
        instance.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        instance.putArchiveEntry(entry);
        instance.close();
        assertTrue(instance.isClosed());
    }

    @Test(expected = IOException.class)
    public void testCloseAlreadyClosed() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        instance.close();
        instance.close();
    }

    @Test(expected = IOException.class)
    public void testCloseWithUnfinishedArchive() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        instance.putArchiveEntry(entry);
        instance.close();
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        assertEquals(1024, instance.getRecordSize());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryNullEntry() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        instance.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryNonTarArchiveEntry() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        instance.putArchiveEntry(new Object());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        instance.putArchiveEntry(entry);
        assertNull(instance.getCurrentName());
        assertArrayEquals(new byte[0], instance.getCurrentRecordBuffer());
        assertFalse(instance.isEntryOpen());
    }

    @Test
    public void testPutArchiveEntryWriteEntryHeader() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        instance.putArchiveEntry(entry);
        byte[] header = new byte[512];
        entry.writeEntryHeader(header);
        assertArrayEquals(header, instance.getCurrentRecordBuffer());
    }

    @Test
    public void testPutArchiveEntryWriteEntryHeaderWithLongFileName() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        instance.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry entry = new TarArchiveEntry("test" + new String(new char[TarConstants.NAMELEN]));
        instance.putArchiveEntry(entry);
        byte[] header = new byte[512];
        entry.writeEntryHeader(header);
        assertArrayEquals(header, instance.getCurrentRecordBuffer());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        instance.putArchiveEntry(entry);
        instance.closeArchiveEntry();
        assertNull(instance.getCurrentName());
        assertArrayEquals(new byte[0], instance.getCurrentRecordBuffer());
        assertTrue(instance.isEntryOpen());
    }

    @Test
    public void testCloseArchiveEntryWriteRecord() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        instance.putArchiveEntry(entry);
        byte[] data = "test data".getBytes();
        instance.write(data, 0, data.length);
        instance.closeArchiveEntry();
        byte[] expected = new byte[512];
        System.arraycopy(entry.getHeader(), 0, expected, 0, 512);
        assertArrayEquals(expected, instance.getCurrentRecordBuffer());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        instance.putArchiveEntry(entry);
        byte[] data = "test data".getBytes();
        instance.write(data, 0, data.length);
        byte[] expected = new byte[512];
        System.arraycopy(entry.getHeader(), 0, expected, 0, 512);
        assertArrayEquals(expected, instance.getCurrentRecordBuffer());
        assertArrayEquals(data, instance.getCountingOutputStream().getByteBuffer().array());
    }

    @Test
    public void testWriteWithLongFileName() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        instance.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry entry = new TarArchiveEntry("test" + new String(new char[TarConstants.NAMELEN]));
        instance.putArchiveEntry(entry);
        byte[] data = "test data".getBytes();
        instance.write(data, 0, data.length);
        byte[] expected = new byte[512];
        System.arraycopy(entry.getHeader(), 0, expected, 0, 512);
        assertArrayEquals(expected, instance.getCurrentRecordBuffer());
        assertArrayEquals(data, instance.getCountingOutputStream().getByteBuffer().array());
    }

    @Test
    public void testWriteWithLongFileNameTruncated() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        instance.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry entry = new TarArchiveEntry("test" + new String(new char[TarConstants.NAMELEN + 1]));
        instance.putArchiveEntry(entry);
        byte[] data = "test data".getBytes();
        instance.write(data, 0, data.length);
        byte[] expected = new byte[512];
        System.arraycopy(entry.getHeader(), 0, expected, 0, 512);
        assertArrayEquals(expected, instance.getCurrentRecordBuffer());
        assertArrayEquals(data, instance.getCountingOutputStream().getByteBuffer().array(), 0, data.length);
        assertEquals(expected.length - TarConstants.NAMELEN, instance.getCountingOutputStream().getCount());
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameCloseArchiveEntryWriteRecordWithLongFileName() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        instance.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry entry = new TarArchiveEntry("test" + new String(new char[TarConstants.NAMELEN]));
        instance.putArchiveEntry(entry);
        byte[] data = "test data".getBytes();
        instance.write(data, 0, data.length);
        instance.closeArchiveEntry();
        byte[] expected = new byte[512];
        System.arraycopy(entry.getHeader(), 0, expected, 0, 512);
        assertArrayEquals(expected, instance.getCurrentRecordBuffer());
        assertEquals(expected.length, instance.getCountingOutputStream().getCount());
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameCloseArchiveEntryWriteRecordTruncated() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        instance.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry entry = new TarArchiveEntry("test" + new String(new char[TarConstants.NAMELEN + 1]));
        instance.putArchiveEntry(entry);
        byte[] data = "test data".getBytes();
        instance.write(data, 0, data.length);
        instance.closeArchiveEntry();
        byte[] expected = new byte[512];
        System.arraycopy(entry.getHeader(), 0, expected, 0, 512);
        assertArrayEquals(expected, instance.getCurrentRecordBuffer());
        assertArrayEquals(data, instance.getCountingOutputStream().getByteBuffer().array(), 0, data.length);
        assertEquals(expected.length - TarConstants.NAMELEN, instance.getCountingOutputStream().getCount());
    }

    @Test(expected = IOException.class)
    public void testWriteAfterFinish() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        instance.finish();
        byte[] data = "test data".getBytes();
        instance.write(data, 0, data.length);
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        instance.close();
        byte[] data = "test data".getBytes();
        instance.write(data, 0, data.length);
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        instance.putArchiveEntry(entry);
        byte[] data = "test data".getBytes();
        instance.write(data, 0, data.length);
        instance.flush();
        byte[] expected = new byte[512];
        System.arraycopy(entry.getHeader(), 0, expected, 0, 512);
        assertArrayEquals(expected, instance.getCurrentRecordBuffer());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        File file = new File("test.txt");
        ArchiveEntry actual = instance.createArchiveEntry(file, "test.txt");
        assertSame(TarArchiveEntry.class, actual.getClass());
        assertEquals("test.txt", actual.getName());
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryNullFile() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        instance.createArchiveEntry(null, "test.txt");
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryEmptyFileName() throws IOException {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        instance.createArchiveEntry(new File("test.txt"), "");
    }

    @Test
    public void testToString() {
        TarArchiveOutputStream instance = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        assertEquals("TarArchiveOutputStream[out=ByteArrayOutputStream@593e4593]", instance.toString());
    }
}