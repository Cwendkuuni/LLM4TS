import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        byteArrayOutputStream.close();
    }

    @Test
    public void testConstructor() {
        assertEquals(byteArrayOutputStream, tarArchiveOutputStream.out);
        assertFalse(tarArchiveOutputStream.closed);
        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
        assertEquals(0, tarArchiveOutputStream.assemLen);
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.assemBuf.length);
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.recordBuf.length);
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, tarArchiveOutputStream.buffer.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.longFileMode);
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();

        assertEquals(2, byteArrayOutputStream.toByteArray().length);
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntries() throws IOException {
        tarArchiveOutputStream.haveUnclosedEntry = true;
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();

        assertTrue(tarArchiveOutputStream.closed);
        assertTrue(byteArrayOutputStream.closed);
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFile");
        tarArchiveEntry.setSize(1000);

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
        
        tarArchiveOutputStream.closeArchiveEntry();
        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameErrorMode() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFileLongerThanNameLength");
        
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameTruncateMode() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFileLongerThanNameLength");
        
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
        
        tarArchiveOutputStream.closeArchiveEntry();
        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameGnuMode() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFileLongerThanNameLength");
        
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
        
        tarArchiveOutputStream.closeArchiveEntry();
        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        byte[] testData = "Test data".getBytes();
        int testDataSize = testData.length;

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFile");
        tarArchiveEntry.setSize(testDataSize);

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(testData);
        tarArchiveOutputStream.closeArchiveEntry();

        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);

        byte[] byteArrayOutputStreamData = byteArrayOutputStream.toByteArray();
        assertEquals(testDataSize, byteArrayOutputStreamData.length);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithIncompleteData() throws IOException {
        byte[] testData = "Test data".getBytes();
        int testDataSize = testData.length;

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFile");
        tarArchiveEntry.setSize(testDataSize);

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        byte[] testData = "Test data".getBytes();
        int testDataSize = testData.length;

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFile");
        tarArchiveEntry.setSize(testDataSize);

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(testData);
        tarArchiveOutputStream.closeArchiveEntry();

        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);

        byte[] byteArrayOutputStreamData = byteArrayOutputStream.toByteArray();
        assertEquals(testDataSize, byteArrayOutputStreamData.length);
    }

    @Test(expected = IOException.class)
    public void testWriteWithExceededDataSize() throws IOException {
        int testDataSize = TarBuffer.DEFAULT_RCDSIZE + 1;

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFile");
        tarArchiveEntry.setSize(testDataSize);

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(new byte[testDataSize]);
    }

    @Test
    public void testWriteWithMultipleRecordSizes() throws IOException {
        int testDataSize = TarBuffer.DEFAULT_RCDSIZE * 2;

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFile");
        tarArchiveEntry.setSize(testDataSize);

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(new byte[testDataSize]);
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        tarArchiveOutputStream.writeEOFRecord();

        assertEquals(512, byteArrayOutputStream.toByteArray().length);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = File.createTempFile("test", "file");
        String entryName = "testFile";
        
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);

        assertTrue(archiveEntry instanceof TarArchiveEntry);
        assertEquals(inputFile, ((TarArchiveEntry) archiveEntry).getFile());
        assertEquals(entryName, ((TarArchiveEntry) archiveEntry).getName());
    }
}