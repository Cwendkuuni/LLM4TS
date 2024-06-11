package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * TarArchiveOutputStreamTest class.
 */
public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    /**
     * Test constructor with OutputStream.
     */
    @Test
    public void testConstructorWithOutputStream() {
        assertNotNull(tarOutputStream);
    }

    /**
     * Test constructor with OutputStream, blockSize and recordSize.
     */
    @Test
    public void testConstructorWithOutputStreamBlockSizeAndRecordSize() {
        assertNotNull(tarOutputStream);
    }

    /**
     * Test constructor with OutputStream, blockSize, recordSize and longFileMode.
     */
    @Test
    public void testConstructorWithOutputStreamBlockSizeRecordSizeAndLongFileMode() {
        assertNotNull(tarOutputStream);
    }

    /**
     * Test setLongFileMode method.
     */
    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    /**
     * Test finish method.
     */
    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
        assertEquals(2 * 512, outputStream.size());
    }

    /**
     * Test close method.
     */
    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
        assertEquals(2 * 512, outputStream.size());
    }

    /**
     * Test getRecordSize method.
     */
    @Test
    public void testGetRecordSize() {
        assertEquals(512, tarOutputStream.getRecordSize());
    }

    /**
     * Test putArchiveEntry method with valid TarArchiveEntry.
     */
    @Test
    public void testPutArchiveEntryWithValidTarArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        assertEquals(TarConstants.STANDARD_MAGIC, tarOutputStream.read(0, recordBuf, 0, 0));
        assertEquals(TarConstants.STANDARD_MAGIC, tarOutputStream.read(0, recordBuf, 0, 0));
    }

    /**
     * Test putArchiveEntry method with long file name.
     */
    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test/long/filename.txt");
        tarOutputStream.putArchiveEntry(entry);
        assertEquals(TarConstants.LF_GNUTYPE_LONGNAME, tarOutputStream.read(0, recordBuf, 0, 0));
        assertEquals(entry.getName().length() + 1, tarOutputStream.readInt());
        assertEquals(entry.getName().length(), tarOutputStream.readInt());
        assertEquals(entry.getName().getBytes().length, tarOutputStream.readInt());
        assertEquals(entry.getSize(), tarOutputStream.readLong());
        assertEquals(entry.getCrc(), tarOutputStream.readLong());
        assertEquals(entry.getMtime(), tarOutputStream.readLong());
        assertEquals(entry.getMode().intValue(), tarOutputStream.readInt());
        assertEquals(entry.getName().getBytes().length, tarOutputStream.readInt());
        assertEquals(entry.getName().getBytes().length, tarOutputStream.readInt());
        assertEquals(entry.getName().getBytes().length, tarOutputStream.readInt());
        assertEquals( TarConstants.NAMELEN, tarOutputStream.readInt());
        for (int i = 0; i < entry.getName().length(); i++) {
            assertEquals(entry.getName().charAt(i), (char) tarOutputStream.readByte());
        }
    }

    /**
     * Test putArchiveEntry method with long file name and LONGFILE_GNU mode.
     */
    @Test
    public void testPutArchiveEntryWithLongFileNameAndLongFileGNUMode() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test/long/filename.txt");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(entry);
        assertEquals(TarConstants.GNU_LONGLINK, tarOutputStream.read(0, recordBuf, 0, 0));
        assertEquals(entry.getName().length(), tarOutputStream.readInt());
        assertEquals(entry.getName().getBytes().length, tarOutputStream.readInt());
        assertEquals(entry.getName().length() + 1, tarOutputStream.readInt());
        for (int i = 0; i < entry.getName().length(); i++) {
            assertEquals(entry.getName().charAt(i), (char) tarOutputStream.readByte());
        }
        assertEquals(0, tarOutputStream.readByte()); // NUL terminator
    }

    /**
     * Test putArchiveEntry method with long file name and LONGFILE_TRUNCATE mode.
     */
    @Test
    public void testPutArchiveEntryWithLongFileNameAndLongFileTruncateMode() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test/long/filename.txt");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        expectedException.expect(RuntimeException.class);
        tarOutputStream.putArchiveEntry(entry);
    }

    /**
     * Test closeArchiveEntry method.
     */
    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[archiveEntry.getSize()]);
        tarOutputStream.closeArchiveEntry();
    }

    /**
     * Test write method with valid data.
     */
    @Test
    public void testWriteWithValidData() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] data = new byte[archiveEntry.getSize()];
        tarOutputStream.write(data);
    }

    /**
     * Test write method with data larger than entry size.
     */
    @Test
    public void testWriteWithDataLargerThanEntrySize() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] data = new byte[archiveEntry.getSize() * 2];
        expectedException.expect(IOException.class);
        tarOutputStream.write(data);
    }

    /**
     * Test write method with data smaller than record size.
     */
    @Test
    public void testWriteWithDataSmallerThanRecordSize() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] data = new byte[511];
        tarOutputStream.write(data);
        byte[] expectedRecord = new byte[512];
        System.arraycopy(data, 0, expectedRecord, 0, 511);
        assertEquals(expectedRecord, tarOutputStream.readRecord());
    }

    /**
     * Test write method with data smaller than record size and assembled records.
     */
    @Test
    public void testWriteWithDataSmallerThanRecordSizeAndAssembledRecords() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] data = new byte[505];
        tarOutputStream.write(data);
        byte[] expectedRecord1 = new byte[511];
        System.arraycopy(data, 0, expectedRecord1, 0, 505);
        byte[] expectedRecord2 = new byte[1];
        expectedRecord2[0] = 0;
        byte[] writtenRecord = new byte[1024];
        int numRead = tarOutputStream.read(writtenRecord, 0, writtenRecord.length);
        assertEquals(expectedRecord1.length + expectedRecord2.length, numRead);
        assertEquals(expectedRecord1, Arrays.copyOfRange(writtenRecord, 0, expectedRecord1.length));
        assertEquals(expectedRecord2, Arrays.copyOfRange(writtenRecord, expectedRecord1.length, expectedRecord1.length + expectedRecord2.length));
    }

    /**
     * Test write method with data larger than record size and assembled records.
     */
    @Test
    public void testWriteWithDataLargerThanRecordSizeAndAssembledRecords() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] data = new byte[513];
        byte[] expectedRecord1 = new byte[511];
        System.arraycopy(data, 0, expectedRecord1, 0, 511);
        byte[] expectedRecord2 = new byte[2];
        expectedRecord2[0] = 0xFF;
        expectedRecord2[1] = 0xEE;
        byte[] writtenRecord = new byte[1024];
        int numRead = tarOutputStream.read(writtenRecord, 0, writtenRecord.length);
        assertEquals(expectedRecord1.length + expectedRecord2.length, numRead);
        assertEquals(expectedRecord1, Arrays.copyOfRange(writtenRecord, 0, expectedRecord1.length));
        assertEquals(expectedRecord2, Arrays.copyOfRange(writtenRecord, expectedRecord1.length, expectedRecord1.length + expectedRecord2.length));
    }
}