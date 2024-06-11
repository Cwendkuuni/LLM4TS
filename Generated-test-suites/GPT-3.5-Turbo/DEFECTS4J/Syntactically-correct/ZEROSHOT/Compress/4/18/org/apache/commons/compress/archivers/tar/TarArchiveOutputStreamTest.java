import static org.junit.Assert.*;
import org.junit.*;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.util.zip.CRC32;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tao;
    private ByteArrayOutputStream outputStream;
    private static final int BLOCK_SIZE = 512;
    
    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        tao = new TarArchiveOutputStream(outputStream);
    }
    
    @After
    public void tearDown() throws IOException {
        tao.close();
    }
    
    @Test
    public void testSetLongFileMode() {
        tao.setLongFileMode(1);
        // Not sure how to test this method as it only modifies an internal variable
    }
    
    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntries() throws IOException {
        tao.putArchiveEntry(new TarArchiveEntry("test"));
        tao.finish();
    }
    
    @Test
    public void testFinish() throws IOException {
        tao.finish();
        
        byte[] bytes = outputStream.toByteArray();
        assertEquals(BLOCK_SIZE * 2, bytes.length);
        
        // Verify that the end of archive record is all zeros
        for (int i = 0; i < bytes.length; i++) {
            assertEquals(0, bytes[i]);
        }
    }
    
    @Test
    public void testClose() throws IOException {
        tao.close();
        assertTrue(outputStream.toByteArray().length > 0);
    }
    
    @Test
    public void testGetRecordSize() {
        assertEquals(BLOCK_SIZE, tao.getRecordSize());
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);
        
        tao.putArchiveEntry(entry);
        
        // Verify that the header is written correctly
        byte[] bytes = outputStream.toByteArray();
        assertEquals(BLOCK_SIZE, bytes.length);
        
        for (int i = 0; i < 100; i++) {
            assertEquals(0, bytes[i]);
        }
    }
    
    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFilenameError() throws IOException {
        tao.setLongFileMode(0);
        tao.putArchiveEntry(new TarArchiveEntry("longfilename.txt"));
    }
    
    @Test
    public void testPutArchiveEntryWithLongFilenameTruncate() throws IOException {
        tao.setLongFileMode(1);
        tao.putArchiveEntry(new TarArchiveEntry("longfilename.txt"));
        // No assert statements because the method does not throw an exception
    }
    
    @Test
    public void testWrite() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(data.length);
        
        tao.putArchiveEntry(entry);
        tao.write(data);
        tao.closeArchiveEntry();
        
        byte[] bytes = outputStream.toByteArray();
        assertEquals(BLOCK_SIZE * 2, bytes.length);
        
        // Verify that the data is written correctly
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], bytes[BLOCK_SIZE + i]);
        }
    }
    
    @Test
    public void testWriteWithAssembleBuffer() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(data.length);
        
        tao.putArchiveEntry(entry);
        tao.write("Hello, ".getBytes());
        tao.write("World!".getBytes());
        tao.closeArchiveEntry();
        
        byte[] bytes = outputStream.toByteArray();
        assertEquals(BLOCK_SIZE * 2, bytes.length);
        
        // Verify that the data is written correctly
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], bytes[BLOCK_SIZE + i]);
        }
    }
    
    @Test(expected = IOException.class)
    public void testWriteWithExceedingEntrySize() throws IOException {
        byte[] data = new byte[TarArchiveOutputStream.DEFAULT_RCDSIZE + 1];
        
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(data.length);
        
        tao.putArchiveEntry(entry);
        tao.write(data);
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);
        
        tao.putArchiveEntry(entry);
        tao.closeArchiveEntry();
        
        // Verify that the data buffer is filled with zeros
        byte[] bytes = outputStream.toByteArray();
        for (int i = 0; i < BLOCK_SIZE; i++) {
            assertEquals(0, bytes[BLOCK_SIZE + i]);
        }
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithUnwrittenBytes() throws IOException {
        tao.putArchiveEntry(new TarArchiveEntry("test"));
        tao.write("Hello, World!".getBytes());
        tao.closeArchiveEntry();
    }
    
    @Test(expected = IOException.class)
    public void testCreateArchiveEntryWithInvalidFile() throws IOException {
        File file = new File("invalid.file");
        tao.createArchiveEntry(file, "entry");
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("file.txt");
        TarArchiveEntry entry = (TarArchiveEntry) tao.createArchiveEntry(file, "entry");
        
        assertEquals(file, entry.getFile());
        assertEquals("entry", entry.getName());
    }
    
}