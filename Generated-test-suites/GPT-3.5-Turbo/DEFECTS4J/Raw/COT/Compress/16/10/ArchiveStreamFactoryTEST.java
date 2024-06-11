
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
        assertNotNull(inputStream);
    }

    @Test
    public void testCreateArchiveInputStreamNullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamNullInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamUnknownArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
        assertNotNull(outputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamNullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamNullOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamUnknownArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream("unknown", new ByteArrayOutputStream());
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream inputStream = factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        assertNotNull(inputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStreamNullInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream((InputStream) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStreamUnsupportedMark() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(System.in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}

