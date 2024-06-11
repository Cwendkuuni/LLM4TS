import static org.junit.Assert.*;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        // Test constructor with default block size and record size
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        assertNotNull(tarOut);

        // Test constructor with custom block size
        TarArchiveOutputStream tarOutCustomBlockSize = new TarArchiveOutputStream(null, 512);
        assertNotNull(tarOutCustomBlockSize);

        // Test constructor with custom block size and record size
        TarArchiveOutputStream tarOutCustomSize = new TarArchiveOutputStream(null, 512, 256);
        assertNotNull(tarOutCustomSize);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        
        assertEquals(tarOut.LONGFILE_TRUNCATE, tarOut.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() {
        // Test putting a directory entry
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("testDir/");
        tarOut.putArchiveEntry(entry);

        // Test putting a file entry
        TarArchiveEntry fileEntry = new TarArchiveEntry("testFile.txt");
        tarOut.putArchiveEntry(fileEntry);

        // Test putting an entry with long file name
        TarArchiveEntry longEntry = new TarArchiveEntry("thisIsAVeryLongFileNameToTestLongFileMode.txt");
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOut.putArchiveEntry(longEntry);
    }

    @Test
    public void testWrite() {
        // Test writing bytes to the current archive entry
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOut.putArchiveEntry(entry);
        
        byte[] data = "Hello, World!".getBytes();
        tarOut.write(data, 0, data.length);
    }

    @Test
    public void testCloseArchiveEntry() {
        // Test closing an archive entry
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.closeArchiveEntry();
    }

    @Test
    public void testFinish() {
        // Test finishing the tar archive
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        try {
            tarOut.finish();
        } catch (Exception e) {
            fail("Exception when finishing the archive");
        }
    }

}