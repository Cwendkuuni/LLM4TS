import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outStream;
    
    @Before
    public void setUp() {
        outStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outStream);
    }
    
    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        outStream.close();
    }
    
    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(2);
        Assert.assertEquals(2, tarOutputStream.getLongFileMode());
    }
    
    @Test
    public void testGetRecordSize() {
        Assert.assertEquals(512, tarOutputStream.getRecordSize());
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(null, "test.txt");
        tarOutputStream.putArchiveEntry(entry);
        
        Assert.assertTrue(tarOutputStream.haveUnclosedEntry);
    }
    
    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(null, "thisisaverylongfilenameanditshouldnotbetruncated.txt");
        tarOutputStream.setLongFileMode(1);
        tarOutputStream.putArchiveEntry(entry);
    }
    
    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithInvalidEntryType() throws IOException {
        tarOutputStream.putArchiveEntry(null);
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(null, "test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        
        Assert.assertFalse(tarOutputStream.haveUnclosedEntry);
    }
    
    @Test
    public void testWrite() throws IOException {
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(null, "test.txt");
        tarOutputStream.putArchiveEntry(entry);
        
        byte[] data = "test data".getBytes();
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
        
        byte[] outputData = outStream.toByteArray();
        Assert.assertArrayEquals(data, outputData);
    }
    
    @Test
    public void testFinish() throws IOException {
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(null, "test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
        tarOutputStream.close();
        
        byte[] outputData = outStream.toByteArray();
        Assert.assertEquals(2 * 512, outputData.length);
    }
    
    @Test
    public void testClose() throws IOException {
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(null, "test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();
        
        Assert.assertTrue(tarOutputStream.closed);
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, entryName);
        
        Assert.assertEquals(inputFile, entry.getFile());
        Assert.assertEquals(entryName, entry.getName());
    }
}