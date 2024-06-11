import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;
    
    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }
    
    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        outputStream.close();
    }
    
    @Test
    public void testSetLongFileMode() {
        int longFileMode = TarArchiveOutputStream.LONGFILE_TRUNCATE;
        tarOutputStream.setLongFileMode(longFileMode);
        assertEquals(longFileMode, tarOutputStream.getLongFileMode());
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        String entryName = "test.txt";
        long size = 10;
        TarArchiveEntry entry = new TarArchiveEntry(entryName);
        entry.setSize(size);
        
        tarOutputStream.putArchiveEntry(entry);
        
        assertEquals(entryName, tarOutputStream.getCurrentEntry().getName());
        assertEquals(size, tarOutputStream.getCurrentEntry().getSize());
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        String entryName = "test.txt";
        long size = 10;
        TarArchiveEntry entry = new TarArchiveEntry(entryName);
        entry.setSize(size);
        
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        
        assertFalse(tarOutputStream.haveUnclosedEntry());
    }
    
    @Test(expected = IOException.class)
    public void testFinish_CalledTwice() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.finish();
    }
    
    @Test(expected = IOException.class)
    public void testClose_CalledTwice() throws IOException {
        tarOutputStream.close();
        tarOutputStream.close();
    }
    
    @Test(expected = IOException.class)
    public void testPutArchiveEntry_AfterFinish() throws IOException {
        tarOutputStream.finish();
        
        String entryName = "test.txt";
        long size = 10;
        TarArchiveEntry entry = new TarArchiveEntry(entryName);
        entry.setSize(size);
        
        tarOutputStream.putArchiveEntry(entry);
    }
    
    @Test(expected = IOException.class)
    public void testPutArchiveEntry_AfterClose() throws IOException {
        tarOutputStream.close();
        
        String entryName = "test.txt";
        long size = 10;
        TarArchiveEntry entry = new TarArchiveEntry(entryName);
        entry.setSize(size);
        
        tarOutputStream.putArchiveEntry(entry);
    }
    
    @Test
    public void testWrite() throws IOException {
        String entryName = "test.txt";
        long size = 10;
        TarArchiveEntry entry = new TarArchiveEntry(entryName);
        entry.setSize(size);
        
        tarOutputStream.putArchiveEntry(entry);
        
        byte[] data = "Hello World".getBytes();
        tarOutputStream.write(data);
        
        tarOutputStream.closeArchiveEntry();
        
        tarOutputStream.finish();
        
        byte[] output = outputStream.toByteArray();
        
        // Check if output contains header
        assertEquals(512, output.length);
        
        // Check if output contains data
        assertEquals(data.length, outputStream.size() - 512);
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, entryName);
        
        assertEquals(inputFile, ((TarArchiveEntry)entry).getFile());
        assertEquals(entryName, entry.getName());
    }
    
    @Test(expected = IOException.class)
    public void testCreateArchiveEntry_AfterFinish() throws IOException {
        tarOutputStream.finish();
        
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        
        tarOutputStream.createArchiveEntry(inputFile, entryName);
    }
    
    @Test(expected = IOException.class)
    public void testCreateArchiveEntry_AfterClose() throws IOException {
        tarOutputStream.close();
        
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        
        tarOutputStream.createArchiveEntry(inputFile, entryName);
    }
    
    @Test
    public void testFlush() throws IOException {
        tarOutputStream.flush();
        
        assertTrue(outputStream.size() > 0);
    }
}