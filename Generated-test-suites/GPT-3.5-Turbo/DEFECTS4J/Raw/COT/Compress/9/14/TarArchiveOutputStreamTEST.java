import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test
    public void constructorTest() {
        try {
            FileOutputStream fos = new FileOutputStream("test.tar");
            TarArchiveOutputStream tarOut = new TarArchiveOutputStream(fos);
            assertNotNull(tarOut);
            tarOut.close();
            fos.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void setLongFileModeTest() {
        try {
            FileOutputStream fos = new FileOutputStream("test.tar");
            TarArchiveOutputStream tarOut = new TarArchiveOutputStream(fos);
            tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOut.getLongFileMode());
            tarOut.close();
            fos.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void putArchiveEntryTest() {
        try {
            FileOutputStream fos = new FileOutputStream("test.tar");
            TarArchiveOutputStream tarOut = new TarArchiveOutputStream(fos);
            File file = new File("test.txt");
            TarArchiveEntry entry = new TarArchiveEntry(file);
            tarOut.putArchiveEntry(entry);
            tarOut.closeArchiveEntry();
            tarOut.close();
            fos.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void closeArchiveEntryTest() {
        try {
            FileOutputStream fos = new FileOutputStream("test.tar");
            TarArchiveOutputStream tarOut = new TarArchiveOutputStream(fos);
            File file = new File("test.txt");
            TarArchiveEntry entry = new TarArchiveEntry(file);
            tarOut.putArchiveEntry(entry);
            tarOut.closeArchiveEntry();
            assertFalse(tarOut.isHaveUnclosedEntry());
            tarOut.close();
            fos.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void writeTest() {
        try {
            FileOutputStream fos = new FileOutputStream("test.tar");
            TarArchiveOutputStream tarOut = new TarArchiveOutputStream(fos);
            File file = new File("test.txt");
            TarArchiveEntry entry = new TarArchiveEntry(file);
            tarOut.putArchiveEntry(entry);
            String content = "This is a test file.";
            byte[] contentBytes = content.getBytes();
            tarOut.write(contentBytes);
            tarOut.closeArchiveEntry();
            tarOut.close();
            fos.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void finishTest() {
        try {
            FileOutputStream fos = new FileOutputStream("test.tar");
            TarArchiveOutputStream tarOut = new TarArchiveOutputStream(fos);
            tarOut.finish();
            assertTrue(tarOut.isFinished());
            tarOut.close();
            fos.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void closeTest() {
        try {
            FileOutputStream fos = new FileOutputStream("test.tar");
            TarArchiveOutputStream tarOut = new TarArchiveOutputStream(fos);
            tarOut.close();
            assertTrue(tarOut.isClosed());
            fos.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void getRecordSizeTest() {
        try {
            FileOutputStream fos = new FileOutputStream("test.tar");
            TarArchiveOutputStream tarOut = new TarArchiveOutputStream(fos);
            assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOut.getRecordSize());
            tarOut.close();
            fos.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void createArchiveEntryTest() {
        try {
            FileOutputStream fos = new FileOutputStream("test.tar");
            TarArchiveOutputStream tarOut = new TarArchiveOutputStream(fos);
            File file = new File("test.txt");
            TarArchiveEntry entry = (TarArchiveEntry) tarOut.createArchiveEntry(file, "test.txt");
            assertEquals(file, entry.getFile());
            assertEquals("test.txt", entry.getName());
            tarOut.close();
            fos.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
}