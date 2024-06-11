import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "testFile.txt";
    private static final String TEST_FILE_CONTENT = "This is a test file.";

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream byteOutputStream;
    
    @Before
    public void setUp() {
        byteOutputStream = new ByteArrayOutputStream();
        outputStream = byteOutputStream;
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }
    
    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        File testFile = createTestFile();
        TarArchiveEntry entry = new TarArchiveEntry(testFile, TEST_FILE_NAME);
        tarArchiveOutputStream.putArchiveEntry(entry);
        
        // Check entry properties
        assertEquals(TEST_FILE_NAME, entry.getName());
        assertFalse(entry.isDirectory());
        assertEquals(TEST_FILE_CONTENT.length(), entry.getSize());
    }
    
    @Test(expected = IOException.class)
    public void testPutArchiveEntryFinished() throws IOException {
        tarArchiveOutputStream.finish();
        File testFile = createTestFile();
        TarArchiveEntry entry = new TarArchiveEntry(testFile, TEST_FILE_NAME);
        tarArchiveOutputStream.putArchiveEntry(entry);
    }
    
    @Test
    public void testWrite() throws IOException {
        byte[] contentBytes = TEST_FILE_CONTENT.getBytes();
        
        File testFile = createTestFile();
        TarArchiveEntry entry = new TarArchiveEntry(testFile, TEST_FILE_NAME);
        tarArchiveOutputStream.putArchiveEntry(entry);
        
        tarArchiveOutputStream.write(contentBytes);
        
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        
        // Check output
        byte[] outputBytes = byteOutputStream.toByteArray();
        assertEquals(contentBytes.length, outputBytes.length);
        assertEquals(TEST_FILE_CONTENT, new String(outputBytes));
    }
    
    @Test(expected = IOException.class)
    public void testWriteTooMuch() throws IOException {
        byte[] contentBytes = TEST_FILE_CONTENT.getBytes();
        
        File testFile = createTestFile();
        TarArchiveEntry entry = new TarArchiveEntry(testFile, TEST_FILE_NAME);
        tarArchiveOutputStream.putArchiveEntry(entry);
        
        // Write more bytes than the length of the file
        tarArchiveOutputStream.write(contentBytes, 0, contentBytes.length + 1);
    }
    
    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        byte[] outputBytes = byteOutputStream.toByteArray();
        
        // Check output
        assertEquals(1024, outputBytes.length);
        assertEquals(0, outputBytes[1015]);
        assertEquals(0, outputBytes[1016]);
        assertEquals(0, outputBytes[1017]);
    }
    
    @Test(expected = IOException.class)
    public void testFinishTwice() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.finish();
    }
    
    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        File testFile = createTestFile();
        TarArchiveEntry entry = new TarArchiveEntry(testFile, TEST_FILE_NAME);
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.finish();
    }
    
    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        
        // Check if the output stream is closed
        assertTrue(byteOutputStream.isClosed());
    }
    
    @Test
    public void testGetRecordSize() {
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        File testFile = createTestFile();
        
        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(testFile, TEST_FILE_NAME);
        
        assertEquals(TEST_FILE_NAME, entry.getName());
        assertEquals(testFile.length(), entry.getSize());
        assertFalse(entry.isDirectory());
    }
    
    private File createTestFile() throws IOException {
    	File testFile = new File(TEST_FILE_NAME);
    	ArchiveUtils.toAsciiFile(TEST_FILE_CONTENT, testFile);
    	return testFile;
    }
}