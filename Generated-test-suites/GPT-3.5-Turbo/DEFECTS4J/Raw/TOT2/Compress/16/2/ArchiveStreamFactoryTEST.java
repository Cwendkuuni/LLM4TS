import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStreamAr() throws ArchiveException {
        // Testing creating ArchiveInputStream for AR format
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0])));
    }

    @Test
    public void testCreateArchiveInputStreamZip() throws ArchiveException {
        // Testing creating ArchiveInputStream for Zip format
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0])));
    }

    @Test
    public void testCreateArchiveInputStreamTar() throws ArchiveException {
        // Testing creating ArchiveInputStream for Tar format
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0])));
    }

    @Test
    public void testCreateArchiveInputStreamJar() throws ArchiveException {
        // Testing creating ArchiveInputStream for Jar format
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0])));
    }

    @Test
    public void testCreateArchiveInputStreamCpio() throws ArchiveException {
        // Testing creating ArchiveInputStream for Cpio format
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0])));
    }

    @Test
    public void testCreateArchiveInputStreamDump() throws ArchiveException {
        // Testing creating ArchiveInputStream for Dump format
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0])));
    }

    @Test
    public void testCreateArchiveInputStreamInvalidName() {
        // Testing creating ArchiveInputStream with invalid name
        try {
            factory.createArchiveInputStream("invalid", new ByteArrayInputStream(new byte[0]));
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamAr() throws ArchiveException {
        // Testing creating ArchiveOutputStream for AR format
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveOutputStreamZip() throws ArchiveException {
        // Testing creating ArchiveOutputStream for Zip format
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveOutputStreamTar() throws ArchiveException {
        // Testing creating ArchiveOutputStream for Tar format
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveOutputStreamJar() throws ArchiveException {
        // Testing creating ArchiveOutputStream for Jar format
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveOutputStreamCpio() throws ArchiveException {
        // Testing creating ArchiveOutputStream for Cpio format
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveOutputStreamDump() throws ArchiveException {
        // Testing creating ArchiveOutputStream for Dump format
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveOutputStreamInvalidName() {
        // Testing creating ArchiveOutputStream with invalid name
        try {
            factory.createArchiveOutputStream("invalid", new ByteArrayOutputStream());
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectionZip() throws ArchiveException, IOException {
        // Testing auto-detecting and creating ArchiveInputStream for Zip format
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{80, 75, 3, 4});
        assertNotNull(factory.createArchiveInputStream(in));
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectionJar() throws ArchiveException, IOException {
        // Testing auto-detecting and creating ArchiveInputStream for Jar format
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{80, 75, 3, 30});
        assertNotNull(factory.createArchiveInputStream(in));
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectionAr() throws ArchiveException, IOException {
        // Testing auto-detecting and creating ArchiveInputStream for AR format
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{33, 60, 10, 20});
        assertNotNull(factory.createArchiveInputStream(in));
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectionCpio() throws ArchiveException, IOException {
        // Testing auto-detecting and creating ArchiveInputStream for Cpio format
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{74, 67, 70, 0});
        assertNotNull(factory.createArchiveInputStream(in));
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectionDump() throws ArchiveException, IOException {
        // Testing auto-detecting and creating ArchiveInputStream for Dump format
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{124, 89, 0, 0});
        assertNotNull(factory.createArchiveInputStream(in));
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectionInvalid() throws IOException {
        // Testing auto-detecting with invalid stream
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createArchiveInputStream(in);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // Expected
        }
    }
}