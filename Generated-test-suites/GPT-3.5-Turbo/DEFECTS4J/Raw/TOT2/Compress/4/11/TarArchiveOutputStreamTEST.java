import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutArchiveEntry() {
        try {
            ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");
            tarArchiveOutputStream.putArchiveEntry(archiveEntry);
            tarArchiveOutputStream.closeArchiveEntry();
            assertTrue(tarArchiveOutputStream.getRecordSize() > 0);
        } catch (IOException e) {
            fail("putArchiveEntry test failed: " + e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        try {
            ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");
            tarArchiveOutputStream.putArchiveEntry(archiveEntry);
            tarArchiveOutputStream.write("Test Data".getBytes());
            tarArchiveOutputStream.closeArchiveEntry();
            assertEquals(9, tarArchiveOutputStream.getRecordSize());
        } catch (IOException e) {
            fail("closeArchiveEntry test failed: " + e.getMessage());
        }
    }

    @Test
    public void testWrite() {
        try {
            ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");
            tarArchiveOutputStream.putArchiveEntry(archiveEntry);
            tarArchiveOutputStream.write("Test Data".getBytes());
            tarArchiveOutputStream.closeArchiveEntry();
            assertEquals("Test Data", outputStream.toString());
        } catch (IOException e) {
            fail("write test failed: " + e.getMessage());
        }
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testFinish() {
        try {
            tarArchiveOutputStream.finish();
            assertEquals(1024, outputStream.size());
        } catch (IOException e) {
            fail("finish test failed: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        try {
            tarArchiveOutputStream.finish();
            tarArchiveOutputStream.close();
            assertTrue(outputStream.size() > 0);
        } catch (IOException e) {
            fail("close test failed: " + e.getMessage());
        }
    }

}