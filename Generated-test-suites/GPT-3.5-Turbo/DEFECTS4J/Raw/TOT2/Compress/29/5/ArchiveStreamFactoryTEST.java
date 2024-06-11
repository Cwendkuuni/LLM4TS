import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNotNull(ais);
        } catch (ArchiveException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertNotNull(aos);
        } catch (ArchiveException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStream_ArchiveExceptionThrown() {
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream ais = factory.createArchiveInputStream("invalidArchiver", in);
            fail("Exception should be thrown");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ArchiveExceptionThrown() {
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveOutputStream aos = factory.createArchiveOutputStream("invalidArchiver", out);
            fail("Exception should be thrown");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullArchiverName() {
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream(null, in);
            fail("Exception should be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (ArchiveException e) {
            fail("Wrong exception thrown");
        }
    }

    // Test cases for edge cases and exceptions can be added here

}