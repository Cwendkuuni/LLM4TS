package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveEntryTestUtils;
import org.apache.commons.compress.utils.BytesUtil;
import org.apache.commons.compress.utils.CountingInputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private static final int BLOCK_SIZE = 128;
    private static final int RECORD_SIZE = 512;
    private OutputStream output;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        output = new CountingOutputStream(bos);
        tarOutputStream = new TarArchiveOutputStream(output, BLOCK_SIZE, RECORD_SIZE);
    }

    @Test
    public void testConstructor() {
        assertEquals(BlockMode.BLOCK_SIZE, tarOutputStream.getBlockMode().getSize());
        assertEquals(RecordMode.RECORD_SIZE, tarOutputStream.getRecordMode().getSize());
        assertEquals(LONGFILE_ERROR, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        tarOutputStream.setLongFileMode(LONGFILE_TRUNCATE);
        assertEquals(LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        assertFalse(tarOutputStream.isFinished());
        tarOutputStream.finish();
        assertTrue(tarOutputStream.isFinished());
        assertEquals(0, output.getCount());
        byte[] buffer = new byte[RECORD_SIZE];
        tarOutputStream.buffer.readBlock(buffer);
        byte[] eofRecord = new byte[RECORD_SIZE * 2];
        Arrays.fill(eofRecord, (byte) 0);
        assertArrayEquals(eofRecord, buffer);

        tarOutputStream = new TarArchiveOutputStream(output, BLOCK_SIZE, RECORD_SIZE);
        tarOutputStream.finish();
        tarOutputStream.finish();
        assertEquals(0, output.getCount());
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(tarOutputStream.isClosed());
        tarOutputStream.close();
        assertTrue(tarOutputStream.isClosed());
        assertEquals(0, output.getCount());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(RECORD_SIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntryWithShortFileName() throws IOException {
        final String fileName = "test.txt";
        ArchiveEntry archiveEntry = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(archiveEntry);
        assertEquals(fileName, tarOutputStream.currName);
        assertEquals(TarEntryType.NORMAL, archiveEntry.getEntryType());
        assertEquals(false, archiveEntry.isDirectory());
        assertEquals(false, tarOutputStream.haveUnclosedEntry);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        final String longFileName = "test_long_name_" + new String(new char[TarConstants.NAMELEN * 2]).replace("\0", "a");
        ArchiveEntry archiveEntry = new TarArchiveEntry(longFileName);
        try {
            tarOutputStream.putArchiveEntry(archiveEntry);
            fail("Expected exception: RuntimeException");
        } catch (RuntimeException e) {
            // Expected
        }

        final int longFileMode = TarArchiveOutputStream.LONGFILE_GNU;
        tarOutputStream.setLongFileMode(longFileMode);
        archiveEntry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(archiveEntry);
        assertEquals(longFileName, tarOutputStream.currName);
        assertEquals(TarEntryType.NORMAL, archiveEntry.getEntryType());
        assertEquals(false, archiveEntry.isDirectory());
        assertEquals(true, tarOutputStream.haveUnclosedEntry);

        byte[] longLinkNameBytes = ArchiveUtils.toAsciiBytes(longFileName);
        byte[] longLinkRecord = new byte[longLinkNameBytes.length + 1];
        longLinkRecord[0] = TarConstants.GNU_LONGLINK;
        System.arraycopy(longLinkNameBytes, 0, longLinkRecord, 1, longLinkNameBytes.length);
        byte[] record = new byte[RECORD_SIZE];
        tarOutputStream.buffer.readBlock(record);
        assertArrayEquals(ArchiveEntryTestUtils.createRecordHeader(longFileName, TarEntryType.LONGNAME), Arrays.copyOfRange(record, 0, 512));
        assertArrayEquals(longLinkRecord, Arrays.copyOfRange(record, 512, 1024));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        final String fileName = "test.txt";
        ArchiveEntry archiveEntry = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] record = new byte[RECORD_SIZE];
        tarOutputStream.buffer.readBlock(record);
        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.haveUnclosedEntry);

        ByteArrayInputStream input = new ByteArrayInputStream(record);
        CountingInputStream countingInput = new CountingInputStream(input);
        ArchiveEntry entry = tarOutputStream.getNextEntry();
        assertEquals(fileName, entry.getName());
        assertEquals(0, countingInput.getByteCount());

        input = new ByteArrayInputStream(new byte[] {});
        countingInput = new CountingInputStream(input);
        entry = tarOutputStream.getNextEntry();
        assertNull(entry);
        assertEquals(0, countingInput.getByteCount());
    }

    @Test
    public void testWrite() throws IOException {
        final byte[] data = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        CountingInputStream countingInput = new CountingInputStream(input);
        ArchiveEntry entry = tarOutputStream.getNextEntry();
        assertEquals("test.txt", entry.getName());
        byte[] buffer = new byte[data.length];
        int bytesRead = countingInput.read(buffer, 0, data.length);
        assertEquals(data.length, bytesRead);
        assertArrayEquals(data, buffer);
        assertEquals(0, countingInput.getByteCount());

        tarOutputStream = new TarArchiveOutputStream(output, BLOCK_SIZE, RECORD_SIZE);
        archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] smallData = new byte[] { 'H' };
        tarOutputStream.write(smallData);
        byte[] assembleBuffer = new byte[RecordMode.RECORD_SIZE];
        tarOutputStream.write(smallData);
        tarOutputStream.write(smallData);
        bytesRead = countingInput.read(assembleBuffer, 0, RecordMode.RECORD_SIZE);
        assertEquals(3 * smallData.length, bytesRead);
        assertArrayEquals(BytesUtil.concat(smallData, smallData, smallData), Arrays.copyOfRange(assembleBuffer, 0, bytesRead));
        tarOutputStream.closeArchiveEntry();

        input = new ByteArrayInputStream(BytesUtil.concat(smallData, smallData, smallData));
        countingInput = new CountingInputStream(input);
        entry = tarOutputStream.getNextEntry();
        assertEquals("test.txt", entry.getName());
        buffer = new byte[3 * smallData.length];
        bytesRead = countingInput.read(buffer, 0, 3 * smallData.length);
        assertEquals(3 * smallData.length, bytesRead);
        assertArrayEquals(BytesUtil.concat(smallData, smallData, smallData), buffer);
        assertEquals(0, countingInput.getByteCount());
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        output.close();
    }
}