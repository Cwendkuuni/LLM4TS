import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tarArchiveOutputStream;
    
    @Before
    public void setup() {
        tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStreamStub());
    }
    
    @Test
    public void putArchiveEntry_validEntry_success() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
    }
    
    @Test
    public void putArchiveEntry_longFileNameError_success() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("thisIsALongFileNameThatExceedsTheAllowedLength.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        try {
            tarArchiveOutputStream.putArchiveEntry(entry);
            fail();
        } catch (RuntimeException e) {
            assertEquals("file name 'thisIsALongFileNameThatExceedsTheAllowedLength.txt' is too long ( > 100 bytes)", e.getMessage());
        }
    }
    
    @Test
    public void putArchiveEntry_longFileNameTruncate_success() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("thisIsALongFileNameThatExceedsTheAllowedLength.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.putArchiveEntry(entry);
    }
    
    @Test
    public void putArchiveEntry_longFileNameGNUSuccess_success() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("thisIsALongFileNameThatExceedsTheAllowedLength.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }
    
    @Test
    public void closeArchiveEntry_entryNotClosed_throwException() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        try {
            tarArchiveOutputStream.closeArchiveEntry();
            fail();
        } catch (IOException e) {
            assertEquals("entry 'file.txt' closed at '0' before the '0' bytes specified in the header were written", e.getMessage());
        }
    }
    
    @Test
    public void write_validBytes_success() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] bytes = "Hello World!".getBytes();
        tarArchiveOutputStream.write(bytes);
        tarArchiveOutputStream.closeArchiveEntry();
        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }
    
    @Test
    public void write_bytesExceedingEntrySize_throwException() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] bytes = "Hello World!".getBytes();
        try {
            tarArchiveOutputStream.write(bytes);
            fail();
        } catch (IOException e) {
            assertEquals("request to write '12' bytes exceeds size in header of '10' bytes for entry 'file.txt'", e.getMessage());
        }
    }
    
    @Test
    public void write_bytesInMultipleWrites_success() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] bytes1 = "Hello".getBytes();
        tarArchiveOutputStream.write(bytes1);
        byte[] bytes2 = " World!".getBytes();
        tarArchiveOutputStream.write(bytes2);
        tarArchiveOutputStream.closeArchiveEntry();
        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }
    
    @Test
    public void finish_haveUnclosedEntry_throwException() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        try {
            tarArchiveOutputStream.finish();
            fail();
        } catch (IOException e) {
            assertEquals("This archives contains unclosed entries.", e.getMessage());
        }
    }
    
    @Test
    public void close_validOutputStream_success() throws Exception {
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.closed);
    }
    
    @Test
    public void getRecordSize_validRecordSize_success() throws Exception {
        int recordSize = tarArchiveOutputStream.getRecordSize();
        assertEquals(512, recordSize);
    }
    
    @Test
    public void createArchiveEntry_validFileAndEntryName_success() throws Exception {
        File file = new File("file.txt");
        String entryName = "file.txt";
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(file, entryName);
        assertNotNull(archiveEntry);
        assertEquals("file.txt", archiveEntry.getName());
    }
    
    private class OutputStreamStub extends OutputStream {
        @Override
        public void write(int b) throws IOException {
            // do nothing
        }
    }
}