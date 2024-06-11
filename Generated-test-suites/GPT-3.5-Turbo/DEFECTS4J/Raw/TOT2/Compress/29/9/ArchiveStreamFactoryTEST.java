import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        try {
            // Test creating AR archive input stream
            InputStream arInput = new ByteArrayInputStream(new byte[0]);
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, arInput));

            // Test creating ZIP archive input stream with encoding
            InputStream zipInput = new ByteArrayInputStream(new byte[0]);
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInput));
        } catch (IOException e) {
            fail("Exception thrown while testing createArchiveInputStream");
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        try {
            // Test creating TAR archive output stream
            OutputStream tarOutput = new ByteArrayOutputStream();
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutput));

            // Test creating JAR archive output stream
            OutputStream jarOutput = new ByteArrayOutputStream();
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutput));
        } catch (IOException e) {
            fail("Exception thrown while testing createArchiveOutputStream");
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        try {
            // Test creating archive input stream with auto-detection
            InputStream input = new ByteArrayInputStream(new byte[0]);
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            assertNotNull(factory.createArchiveInputStream(input));
        } catch (IOException e) {
            fail("Exception thrown while testing createArchiveInputStreamAutoDetect");
        }
    }

    @Test
    public void testGetEntryEncoding() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        assertNull(factory.getEntryEncoding());
    }

    @Test
    public void testSetEntryEncoding() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.setEntryEncoding("UTF-8");
        assertEquals("UTF-8", factory.getEntryEncoding());
    }

}