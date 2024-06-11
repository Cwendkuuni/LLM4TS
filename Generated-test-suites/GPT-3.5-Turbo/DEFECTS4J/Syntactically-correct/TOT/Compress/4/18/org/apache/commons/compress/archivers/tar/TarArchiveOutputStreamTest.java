import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
    
    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;
    private final String ENTRY_NAME = "testEntry.txt";
    private final String CONTENTS = "This is a test file.";
    
    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }
    
    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        outputStream.close();
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(ENTRY_NAME);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(CONTENTS.getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        
        byte[] tarBytes = outputStream.toByteArray();
        assertTrue(tarBytes.length > 0);
    }
    
    @Test(expected = IOException.class)
    public void testPutArchiveEntry_WithInvalidEntryName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("This_is_a_very_long_file_name_that_exceeds_the_limit_of_character_allowed_in_TarConstants.NAMELEN");
        tarArchiveOutputStream.putArchiveEntry(entry);
    }
    
    @Test
    public void testPutArchiveEntry_WithTruncatedEntryName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("This_is_a_very_long_file_name_that_exceeds_the_limit_of_character_allowed_in_TarConstants.NAMELEN");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
    }
    
    @Test
    public void testPutArchiveEntry_WithGNULongFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("This_is_a_very_long_file_name_that_exceeds_the_limit_of_character_allowed_in_TarConstants.NAMELEN");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_WithoutCallToPutArchiveEntry() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
    }
    
    @Test
    public void testCloseArchiveEntry_WithIncompleteDataWritten() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(ENTRY_NAME);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(CONTENTS.getBytes(), 0, CONTENTS.length()-1);
        
        try {
            tarArchiveOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("before the '" + CONTENTS.getBytes().length + "' bytes specified"));
            return;
        }
        fail("IOException expected");
    }
    
    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        
        byte[] tarBytes = outputStream.toByteArray();
        assertEquals(1024, tarBytes.length);
        assertEquals(0, tarBytes[tarBytes.length-1]);
    }
    
    @Test(expected = IOException.class)
    public void testFinish_WithUnclosedEntries() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(ENTRY_NAME);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(CONTENTS.getBytes());
        tarArchiveOutputStream.finish();
    }
    
    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        assertTrue(outputStream.toString().isEmpty());
    }
    
    @Test(expected = IOException.class)
    public void testClose_AfterClosingTarArchiveOutputStream() throws IOException {
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.close();
    }
    
    @Test
    public void testGetRecordSize() {
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("testFile.txt");
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(inputFile, ENTRY_NAME);
        assertNotNull(entry);
        assertEquals(ENTRY_NAME, entry.getName());
        assertTrue(entry instanceof TarArchiveEntry);
    }
    
}