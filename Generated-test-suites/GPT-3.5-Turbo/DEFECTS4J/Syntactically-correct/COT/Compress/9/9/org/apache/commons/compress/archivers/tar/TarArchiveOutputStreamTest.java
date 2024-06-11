
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class TarArchiveOutputStreamTest {
    
    // Test creating a TarArchiveOutputStream with default block size and record size
    @Test
    public void testConstructorDefaultBlockSize() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);

        assertNotNull(tarOutput);
    }

    // Test creating a TarArchiveOutputStream with specified block size and record size
    @Test
    public void testConstructorSpecifiedBlockSize() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        int blockSize = 512; // specify block size of 512 bytes
        int recordSize = 128; // specify record size of 128 bytes
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os, blockSize, recordSize);

        assertNotNull(tarOutput);
    }

    // Test setting the long file mode to LONGFILE_ERROR
    @Test
    public void testSetLongFileModeError() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutput.getLongFileMode());
    }

    // Test setting the long file mode to LONGFILE_TRUNCATE
    @Test
    public void testSetLongFileModeTruncate() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutput.getLongFileMode());
    }

    // Test setting the long file mode to LONGFILE_GNU
    @Test
    public void testSetLongFileModeGNU() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);

        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutput.getLongFileMode());
    }

    // Test putting an ArchiveEntry on the output stream
    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        String entryName = "test.txt";
        TarArchiveEntry entry = new TarArchiveEntry(entryName);
        
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        
        assertFalse(tarOutput.haveUnclosedEntry());
    }

    // Test closing an entry
    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        String entryName = "test.txt";
        TarArchiveEntry entry = new TarArchiveEntry(entryName);
        
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        
        assertFalse(tarOutput.haveUnclosedEntry());
    }

    // Test writing bytes to the current archive entry
    @Test
    public void testWriteBytes() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        String entryName = "test.txt";
        TarArchiveEntry entry = new TarArchiveEntry(entryName);
        
        tarOutput.putArchiveEntry(entry);
        tarOutput.write("Hello, World!".getBytes());
        tarOutput.closeArchiveEntry();
        tarOutput.close();

        assertEquals("Hello, World!", os.toString());
    }

    // Test finishing the tar archive
    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        String entryName = "test.txt";
        TarArchiveEntry entry = new TarArchiveEntry(entryName);
        
        tarOutput.putArchiveEntry(entry);
        tarOutput.write("Hello, World!".getBytes());
        tarOutput.closeArchiveEntry();
        tarOutput.finish();
        tarOutput.close();

        assertEquals("Hello, World!\0\0\0\0\0\0\0\0\0\0\0\0", os.toString());
    }

    // Test closing the tar archive
    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        String entryName = "test.txt";
        TarArchiveEntry entry = new TarArchiveEntry(entryName);
        
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        tarOutput.close();

        assertTrue(tarOutput.isClosed());
    }

    // Test creating an ArchiveEntry
    @Test
    public void testCreateArchiveEntry() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        
        ArchiveEntry entry = tarOutput.createArchiveEntry(inputFile, entryName);
        
        assertNotNull(entry);
        assertTrue(entry instanceof TarArchiveEntry);
        assertEquals(entryName, entry.getName());
    }
}
