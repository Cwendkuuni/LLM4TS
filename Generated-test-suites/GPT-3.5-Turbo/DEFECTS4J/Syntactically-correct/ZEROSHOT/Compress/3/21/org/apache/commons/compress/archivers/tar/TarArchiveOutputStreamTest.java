import static org.junit.Assert.*;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor1() {
        try {
            TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStream());
            assertNotNull(output);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testConstructor2() {
        try {
            TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStream(), 512);
            assertNotNull(output);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testConstructor3() {
        try {
            TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStream(), 512, 1024);
            assertNotNull(output);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStream());
        output.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, output.getLongFileMode());
    }

    @Test
    public void testFinish() {
        try {
            TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStream());
            output.finish();
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testClose() {
        try {
            TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStream());
            output.close();
            assertTrue(output.isClosed());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStream());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, output.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() {
        try {
            TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStream());
            TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
            output.putArchiveEntry(entry);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        try {
            TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStream());
            TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
            output.putArchiveEntry(entry);
            output.closeArchiveEntry();
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testWrite() {
        try {
            TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStream());
            byte[] buf = new byte[1024];
            output.write(buf, 0, buf.length);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStream());
        File inputFile = new File("testFile.txt");
        String entryName = "entryName";
        ArchiveEntry entry = output.createArchiveEntry(inputFile, entryName);
        assertNotNull(entry);
    }
}