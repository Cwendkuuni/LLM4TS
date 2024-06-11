import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutArchiveEntry() {
        try {
            ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.write("Test data".getBytes());
            tarArchiveOutputStream.closeArchiveEntry();
            tarArchiveOutputStream.finish();
            assertTrue(outputStream.toByteArray().length > 0);
        } catch (IOException e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        try {
            ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.closeArchiveEntry();
            assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
        } catch (IOException e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    public void testWrite() {
        try {
            ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.write("Test data".getBytes());
            tarArchiveOutputStream.closeArchiveEntry();
            tarArchiveOutputStream.finish();
            assertTrue(outputStream.toByteArray().length > 0);
        } catch (IOException e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    public void testFinish() {
        try {
            ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.finish();
            assertTrue(tarArchiveOutputStream.finished);
        } catch (IOException e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        try {
            ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.finish();
            tarArchiveOutputStream.close();
            assertTrue(tarArchiveOutputStream.closed);
        } catch (IOException e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        try {
            File file = new File("test.txt");
            ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");
            assertEquals(file.getName(), entry.getName());
        } catch (IOException e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

}