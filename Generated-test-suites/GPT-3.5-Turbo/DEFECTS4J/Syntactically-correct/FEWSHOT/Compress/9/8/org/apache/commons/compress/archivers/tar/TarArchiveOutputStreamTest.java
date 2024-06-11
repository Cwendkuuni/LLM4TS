import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
    
    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;
    
    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }
    
    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
    }
    
    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }
    
    @Test
    public void testPutArchiveEntry() throws Exception {
        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        assertTrue(outputStream.toByteArray().length > 0);
    }
    
    @Test(expected = IOException.class)
    public void testPutArchiveEntryFinished() throws Exception {
        tarOutputStream.finish();
        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOutputStream.putArchiveEntry(entry);
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws Exception {
        tarOutputStream.closeArchiveEntry();
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryFinished() throws Exception {
        tarOutputStream.finish();
        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
    }
    
    @Test
    public void testWrite() throws Exception {
        byte[] data = "test".getBytes();
        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(data);
        tarOutputStream.closeArchiveEntry();
        assertTrue(outputStream.toByteArray().length > 0);
    }
    
    @Test(expected = IOException.class)
    public void testWriteExceedSize() throws Exception {
        byte[] data = "test".getBytes();
        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(3);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(data);
        tarOutputStream.closeArchiveEntry();
    }
    
    @Test
    public void testFinish() throws Exception {
        tarOutputStream.finish();
        assertTrue(outputStream.toByteArray().length > 0);
    }
    
    @Test(expected = IOException.class)
    public void testFinishAlreadyFinished() throws Exception {
        tarOutputStream.finish();
        tarOutputStream.finish();
    }
    
    @Test
    public void testClose() throws Exception {
        tarOutputStream.close();
        assertTrue(outputStream.toByteArray().length > 0);
    }
    
    @Test
    public void testCreateArchiveEntry() throws Exception {
        File inputFile = new File("file.txt");
        String entryName = "file.txt";
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, entryName);
        assertTrue(entry instanceof TarArchiveEntry);
    }
}