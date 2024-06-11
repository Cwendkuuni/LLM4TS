import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutput;

    @Before
    public void setUp() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        tarOutput = new TarArchiveOutputStream(byteArrayOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutput.longFileMode);
    }

    @Test
    public void testFinish() {
        // Test finish method
        try {
            tarOutput.finish();
            tarOutput.close();
        } catch (Exception e) {
            fail("Exception occured: " + e.getMessage());
        }
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutput.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() {
        // Test putArchiveEntry method
        try {
            tarOutput.putArchiveEntry(tarOutput.createArchiveEntry(new File("/test/file"), "/test/file"));
            tarOutput.closeArchiveEntry();
        } catch (Exception e) {
            fail("Exception occured: " + e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        // Test closeArchiveEntry method
        try {
            tarOutput.putArchiveEntry(tarOutput.createArchiveEntry(new File("/test/file"), "/test/file"));
            tarOutput.closeArchiveEntry();
        } catch (Exception e) {
            fail("Exception occured: " + e.getMessage());
        }
    }

    @Test
    public void testWrite() {
        // Test write method
        try {
            tarOutput.putArchiveEntry(tarOutput.createArchiveEntry(new File("/test/file"), "/test/file"));
            tarOutput.write("test data".getBytes());
            tarOutput.closeArchiveEntry();
        } catch (Exception e) {
            fail("Exception occured: " + e.getMessage());
        }
    }

    @Test
    public void testWrite_Boundary() {
        // Test write method with boundary condition
        try {
            tarOutput.putArchiveEntry(tarOutput.createArchiveEntry(new File("/test/file"), "/test/file"));
            tarOutput.write(new byte[TarBuffer.DEFAULT_RCDSIZE], 0, TarBuffer.DEFAULT_RCDSIZE);
            tarOutput.closeArchiveEntry();
        } catch (Exception e) {
            fail("Exception occured: " + e.getMessage());
        }
    }

    @Test
    public void testWrite_EOF() {
        // Test write method with EOF
        try {
            tarOutput.putArchiveEntry(tarOutput.createArchiveEntry(new File("/test/file"), "/test/file"));
            tarOutput.writeEOFRecord();
            tarOutput.closeArchiveEntry();
            tarOutput.close();
        } catch (Exception e) {
            fail("Exception occured: " + e.getMessage());
        }
    }

    @Test
    public void testFinish_Closed() {
        // Test finish method when already closed
        try {
            tarOutput.finish();
        } catch (Exception e) {
            assertEquals("Stream closed", e.getMessage());
        }
    }

    @Test
    public void testClose() {
        // Test close method
        try {
            tarOutput.close();
        } catch (Exception e) {
            fail("Exception occured: " + e.getMessage());
        }
    }

}