import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class TarArchiveOutputStreamTest {
    
    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;
    
    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(tarOutputStream);
    }
    
    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.longFileMode);
        
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.longFileMode);
        
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.longFileMode);
    }
    
    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(50);
        
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[50]);
        tarOutputStream.closeArchiveEntry();
        
        tarOutputStream.finish();
        
        byte[] expectedEOFRecord = new byte[512];
        byte[] actualEOFRecord = outputStream.toByteArray();
        
        assertArrayEquals(expectedEOFRecord, actualEOFRecord);
    }
    
    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntries() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.flush();
        tarOutputStream.finish();
    }
    
    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
        assertTrue(tarOutputStream.closed);
    }
    
    @Test(expected = IOException.class)
    public void testCloseBeforeFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();
    }
    
    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(50);
        
        tarOutputStream.putArchiveEntry(entry);
        
        assertEquals(entry.getName(), tarOutputStream.currName);
        assertEquals(entry.getSize(), tarOutputStream.currSize);
        assertTrue(tarOutputStream.haveUnclosedEntry);
    }
    
    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithInvalidType() throws IOException {
        ArchiveEntry entry = new ArchiveEntry() {
            public String getName() { return null; }
        };
        
        tarOutputStream.putArchiveEntry(entry);
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(50);
        
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[50]);
        tarOutputStream.closeArchiveEntry();
        
        assertFalse(tarOutputStream.haveUnclosedEntry);
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithIncompleteBytes() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(50);
        
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[49]);
        tarOutputStream.closeArchiveEntry();
    }
    
    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(50);
        
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[50]);
        tarOutputStream.closeArchiveEntry();
        
        byte[] expectedRecord = new byte[512];
        byte[] actualRecord = outputStream.toByteArray();
        
        assertTrue(actualRecord.length > expectedRecord.length);
    }
    
    @Test(expected = IOException.class)
    public void testWriteWithExceedingSize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(50);
        
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[51]);
        tarOutputStream.closeArchiveEntry();
    }
    
    @Test
    public void testWriteEOFRecord() throws IOException {
        tarOutputStream.writeEOFRecord();
        
        byte[] expectedEOFRecord = new byte[512];
        byte[] actualEOFRecord = outputStream.toByteArray();
        
        assertArrayEquals(expectedEOFRecord, actualEOFRecord);
    }
    
    @Test
    public void testFlush() throws IOException {
        tarOutputStream.flush();
        assertTrue(outputStream.size() > 0);
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("file.txt");
        String entryName = "file.txt";
        
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, entryName);
        assertNotNull(entry);
        assertEquals(entryName, entry.getName());
        assertTrue(entry instanceof TarArchiveEntry);
    }
}