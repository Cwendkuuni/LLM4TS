import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructorWithDefaultBlockSizeAndRecordSize() {
        // Create TarArchiveOutputStream object with default block size and record size
        OutputStream os = mock(OutputStream.class);
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        
        assertNotNull(tarOutput);
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, tarOutput.getRecordSize());
    }

    @Test
    public void testPutArchiveEntryLongFileNameTruncate() {
        // Test putArchiveEntry with a long file name using LONGFILE_TRUNCATE mode
        OutputStream os = mock(OutputStream.class);
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        TarArchiveEntry entry = new TarArchiveEntry("ThisIsAVeryLongFileNameThatWouldBeTruncated.txt");
        
        assertDoesNotThrow(() -> {
            tarOutput.putArchiveEntry(entry);
        });
    }

    @Test
    public void testWriteBytesToEntry() {
        // Test writing bytes to a tar archive entry
        OutputStream os = mock(OutputStream.class);
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutput.putArchiveEntry(entry);

        byte[] bytes = "Hello, World!".getBytes();
        
        assertDoesNotThrow(() -> {
            tarOutput.write(bytes, 0, bytes.length);
            tarOutput.closeArchiveEntry();
        });
    }

    @Test
    public void testFinishArchive() {
        // Test finishing the tar archive
        OutputStream os = mock(OutputStream.class);
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutput.putArchiveEntry(entry);

        byte[] bytes = "Hello, World!".getBytes();
        tarOutput.write(bytes, 0, bytes.length);
        tarOutput.closeArchiveEntry();
        
        assertDoesNotThrow(() -> {
            tarOutput.finish();
        });
    }
}