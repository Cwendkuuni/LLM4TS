
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    
    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }
    
    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
    }
    
    @Test
    public void testFinish() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testEntry"));

        tarOutputStream.finish();

        tarOutputStream.close();

        assertEquals(1024, byteArrayOutputStream.toByteArray().length);
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntries() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testEntry"));
        
        tarOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();

        assertTrue(tarOutputStream.isClosed());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(512, tarOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testEntry"));

        assertTrue(tarOutputStream.hasUnclosedEntry());
    }
    
    @Test
    public void testCLoseArchiveEntry() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testEntry"));
        
        tarOutputStream.closeArchiveEntry();

        assertFalse(tarOutputStream.hasUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutOpenEntry() throws IOException {
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        byte[] testData = "testData".getBytes();

        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testEntry"));
        
        tarOutputStream.write(testData);

        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        assertEquals(1024, byteArrayOutputStream.toByteArray().length);
    }

    @Test(expected = IOException.class)
    public void testWriteExceedingEntrySize() throws IOException {
        byte[] testData = new byte[TarArchiveOutputStream.DEFAULT_RCDSIZE + 1];

        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testEntry"));
        
        tarOutputStream.write(testData);

        tarOutputStream.closeArchiveEntry();
    }
    
    @Test
    public void testFlush() throws IOException {
        tarOutputStream.flush();

        // No exception should be thrown
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("testFile");

        ArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, "entryName");

        assertEquals(inputFile, ((TarArchiveEntry) entry).getFile());
        assertEquals("entryName", ((TarArchiveEntry) entry).getName());
    }
}
