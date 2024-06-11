import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class TarArchiveOutputStreamTest {
    @Test
    public void testConstructor1() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        assertNotNull(output);
    }

    @Test
    public void testConstructor2() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null, 512);
        assertNotNull(output);
    }

    @Test
    public void testConstructor3() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null, 512, 1024);
        assertNotNull(output);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        output.setLongFileMode(1);
        assertEquals(1, output.getLongFileMode());
    }

    @Test
    public void testFinish() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        try {
            output.finish();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        try {
            output.close();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        assertEquals(512, output.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        try {
            output.putArchiveEntry(null);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        try {
            output.closeArchiveEntry();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testWrite() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        try {
            output.write(null, 0, 0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testWriteEOFRecord() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        try {
            output.writeEOFRecord();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        try {
            output.createArchiveEntry(null, "");
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}