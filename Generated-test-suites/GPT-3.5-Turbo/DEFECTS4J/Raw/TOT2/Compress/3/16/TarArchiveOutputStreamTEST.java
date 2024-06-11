import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {
    
    @Test
    public void testConstructor() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(baos);

        assertNotNull(taos);
        assertEquals(baos, taos.out);
    }

    @Test
    public void testSetLongFileMode() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(baos);
        
        taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, taos.longFileMode);
    }

    @Test
    public void testFinish() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(baos);

        try {
            taos.finish();
        } catch (IOException e) {
            fail("Exception caught");
        }
    }

    @Test
    public void testClose() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(baos);

        try {
            taos.close();
        } catch (IOException e) {
            fail("Exception caught");
        }

        assertTrue(taos.closed);
    }

    @Test
    public void testPutArchiveEntry() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(baos);

        try {
            taos.putArchiveEntry(new TarArchiveEntry("testFile.txt"));
        } catch (IOException e) {
            fail("Exception caught");
        }
    }

    @Test
    public void testWrite() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(baos);

        byte[] bytes = {65, 66, 67};

        try {
            taos.write(bytes, 0, bytes.length);
        } catch (IOException e) {
            fail("Exception caught");
        }

        assertEquals('A', baos.toByteArray()[0]);
        assertEquals('B', baos.toByteArray()[1]);
        assertEquals('C', baos.toByteArray()[2]);
    }

    @Test
    public void testCloseArchiveEntry() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(baos);
        
        try {
            taos.putArchiveEntry(new TarArchiveEntry("testFile.txt"));
            taos.write(new byte[50], 0, 50);
            taos.closeArchiveEntry();
        } catch (IOException e) {
            fail("Exception caught");
        }
    }

    @Test
    public void testWriteEOFRecord() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(baos);

        try {
            taos.writeEOFRecord();
        } catch (IOException e) {
            fail("Exception caught");
        }

        assertEquals(0, baos.toByteArray()[0]);
        assertEquals(0, baos.toByteArray()[1]);
        assertEquals(0, baos.toByteArray()[2]);
    }

    @Test
    public void testCreateArchiveEntry() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream taos = new TarArchiveOutputStream(baos);

        File file = new File("test.txt");

        ArchiveEntry entry = taos.createArchiveEntry(file, "testFile.txt");

        assertTrue(entry instanceof TarArchiveEntry);
    }
}