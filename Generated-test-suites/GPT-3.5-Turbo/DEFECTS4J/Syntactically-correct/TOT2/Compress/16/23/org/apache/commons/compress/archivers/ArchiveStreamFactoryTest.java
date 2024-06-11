import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStreamWithValidArchiverNameAndInputStream() {
        try {
            ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream("test".getBytes()));
            assertNotNull(inputStream);
        } catch (ArchiveException | IOException e) {
            fail("Exception thrown when creating ArchiveInputStream with valid archiver name and input stream");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        try {
            factory.createArchiveInputStream(null, new ByteArrayInputStream("test".getBytes()));
            fail("No exception thrown when creating ArchiveInputStream with null archiver name");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (ArchiveException | IOException e) {
            fail("Wrong exception thrown when creating ArchiveInputStream with null archiver name");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidArchiverNameAndOutputStream() {
        try {
            ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
            assertNotNull(outputStream);
        } catch (ArchiveException | IOException e) {
            fail("Exception thrown when creating ArchiveOutputStream with valid archiver name and output stream");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("No exception thrown when creating ArchiveOutputStream with null output stream");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (ArchiveException | IOException e) {
            fail("Wrong exception thrown when creating ArchiveOutputStream with null output stream");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithAutoDetection() {
        try {
            ArchiveInputStream inputStream = factory.createArchiveInputStream(new ByteArrayInputStream("testdata/test1.xml".getBytes()));
            assertNotNull(inputStream);
        } catch (ArchiveException | IOException e) {
            fail("Exception thrown when creating ArchiveInputStream with auto detection");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidSignature() {
        try {
            ByteArrayInputStream input = new ByteArrayInputStream("invalid signature".getBytes());
            factory.createArchiveInputStream(input);
            fail("No exception thrown when creating ArchiveInputStream with invalid signature");
        } catch (ArchiveException e) {
            assertTrue(true);
        } catch (IOException e) {
            fail("Wrong exception thrown when creating ArchiveInputStream with invalid signature");
        }
    }

}