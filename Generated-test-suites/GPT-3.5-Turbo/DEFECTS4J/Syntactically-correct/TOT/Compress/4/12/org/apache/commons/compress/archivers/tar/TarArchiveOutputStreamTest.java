import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;

public class TarArchiveOutputStreamTest {

    @Test
    public void testSetLongFileMode_LongFileErrorMode() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tar.getLongFileMode());
    }

    @Test
    public void testSetLongFileMode_LongFileTruncateMode() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tar.getLongFileMode());
    }

    @Test
    public void testSetLongFileMode_LongFileGnuMode() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tar.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry_WithValidEntry() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file);
        tar.putArchiveEntry(entry);
        assertTrue(tar.haveUnclosedEntry);
    }

    @Test
    public void testPutArchiveEntry_WithInvalidEntry() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
        File file = new File("test test test test test test test test test test test.txt"); // long file name
        TarArchiveEntry entry = new TarArchiveEntry(file);
        try {
          tar.putArchiveEntry(entry);
          fail("Expected RuntimeException");
        } catch (RuntimeException e) {
          assertEquals("file name 'test test test test test test test test test test test.txt' is too long ( > 100 bytes)", e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry_WithOpenEntry() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file);
        try {
          tar.putArchiveEntry(entry);
          tar.closeArchiveEntry();
          assertFalse(tar.haveUnclosedEntry);
        } catch (Exception e) {
          fail(e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry_WithoutOpenEntry() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file);
        try {
          tar.closeArchiveEntry();
          fail("Expected IOException");
        } catch (IOException e) {
          assertEquals("This archives contains unclosed entries.", e.getMessage());
        }
    }

    @Test
    public void testWrite_WithValidData() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file);
        try {
          tar.putArchiveEntry(entry);
          byte[] data = "This is a test.".getBytes();
          tar.write(data);
          tar.closeArchiveEntry();
          assertFalse(tar.haveUnclosedEntry);
        } catch (Exception e) {
          fail(e.getMessage());
        }
    }

    @Test
    public void testWrite_WithExceedingData() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file);
        try {
          tar.putArchiveEntry(entry);
          byte[] data = new byte[1100]; // exceed the size specified in the header (1024 bytes)
          tar.write(data);
          fail("Expected IOException");
        } catch (IOException e) {
          assertEquals("request to write '1100' bytes exceeds size in header of '1024' bytes for entry 'test.txt'", e.getMessage());
        }
    }

    @Test
    public void testFinish_WithoutOpenEntries() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
        try {
          tar.finish();
        } catch (Exception e) {
          fail(e.getMessage());
        }
    }

    @Test
    public void testFinish_WithOpenEntries() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file);
        try {
          tar.putArchiveEntry(entry);
          tar.finish();
          fail("Expected IOException");
        } catch (IOException e) {
          assertEquals("This archives contains unclosed entries.", e.getMessage());
        }
    }

    @Test
    public void testClose() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
        try {
          tar.close();
          assertTrue(tar.closed);
        } catch (IOException e) {
          fail(e.getMessage());
        }
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tar.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry_WithValidFile() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
        File file = new File("test.txt");
        ArchiveEntry entry = null;
        try {
          entry = tar.createArchiveEntry(file, "test.txt");
        } catch (IOException e) {
          fail(e.getMessage());
        }
        assertTrue(entry instanceof TarArchiveEntry);
        assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testCreateArchiveEntry_WithInvalidFile() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
        File file = null;
        ArchiveEntry entry = null;
        try {
          entry = tar.createArchiveEntry(file, "test.txt");
        } catch (IOException e) {
          fail(e.getMessage());
        }
        assertNull(entry);
    }
}