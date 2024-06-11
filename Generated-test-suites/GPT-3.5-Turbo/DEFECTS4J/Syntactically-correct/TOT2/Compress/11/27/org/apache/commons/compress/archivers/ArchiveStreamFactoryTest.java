import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream_ValidArchiveName() throws ArchiveException {
        ArchiveInputStream stream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[]{}));
        assertTrue(stream instanceof ArArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() throws ArchiveException {
        factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[]{}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws ArchiveException {
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArchiveName() throws ArchiveException {
        ArchiveOutputStream stream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
        assertTrue(stream instanceof ZipArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() throws ArchiveException {
        factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws ArchiveException {
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_Zip() throws ArchiveException, IOException {
        ArchiveInputStream stream = factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{'P','K',3,4}));
        assertTrue(stream instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_Jar() throws ArchiveException, IOException {
        ArchiveInputStream stream = factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{'P','K',3,4}));
        assertTrue(stream instanceof ZipArchiveInputStream);
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveInputStream_AutoDetect_Unknown() throws ArchiveException, IOException {
        factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{'N','O','T','A','R','Z','I','P'}));
    }

}