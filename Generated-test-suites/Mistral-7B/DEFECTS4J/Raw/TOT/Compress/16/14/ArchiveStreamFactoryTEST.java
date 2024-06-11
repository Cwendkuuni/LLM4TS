import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.FormatFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test for {@link ArchiveStreamFactory} class.
 */
public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        InputStream testInput = new ByteArrayInputStream("test".getBytes());

        // Test valid archives
        assertEquals(ArchiveFormat.AR, factory.getFormat(new ArArchiveInputStream(testInput)));
        assertEquals(ArchiveFormat.CPIO, factory.getFormat(new CpioArchiveInputStream(testInput)));
        assertEquals(ArchiveFormat.DUMP, factory.getFormat(new DumpArchiveInputStream(testInput)));
        assertEquals(ArchiveFormat.JAR, factory.getFormat(new JarArchiveInputStream(testInput)));
        assertEquals(ArchiveFormat.TAR, factory.getFormat(new TarArchiveInputStream(testInput)));
        assertEquals(ArchiveFormat.ZIP, factory.getFormat(new ZipArchiveInputStream(testInput)));

        // Test invalid archives
        assertNull(factory.getFormat(new ByteArrayInputStream(new byte[0])));
        assertNull(factory.getFormat(null));
        assertNull(factory.getFormat((InputStream) null));
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        OutputStream testOutput = new ByteArrayOutputStream();

        // Test valid archives
        assertEquals(ArchiveFormat.AR, factory.createArchiveOutputStream(ArchiveStreamFactory.AR, testOutput).getFormat());
        assertEquals(ArchiveFormat.CPIO, factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, testOutput).getFormat());
        assertEquals(ArchiveFormat.DUMP, factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, testOutput).getFormat());
        assertEquals(ArchiveFormat.JAR, factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, testOutput).getFormat());
        assertEquals(ArchiveFormat.TAR, factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, testOutput).getFormat());
        assertEquals(ArchiveFormat.ZIP, factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, testOutput).getFormat());

        // Test invalid archives
        assertNull(factory.createArchiveOutputStream(null, testOutput));
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null));
        assertNull(factory.createArchiveOutputStream((ArchiveOutputStream) null, testOutput));
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws IOException, ArchiveException {
        InputStream testInput = new ByteArrayInputStream("test".getBytes());

        ArchiveInputStream archiveInput = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, testInput);
        assertEquals(ArchiveFormat.ZIP, archiveInput.getFormat());
        IOUtils.closeQuietly(archiveInput);

        archiveInput = factory.createArchiveInputStream(null);
        assertNull(archiveInput);

        archiveInput = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
        assertNull(archiveInput);

        archiveInput = factory.createArchiveInputStream((InputStream) null);
        assertNull(archiveInput);
    }

    @Test
    public void testGetFormat() throws IOException, ArchiveException {
        byte[] testBytes = "test".getBytes();
        InputStream testInput = new ByteArrayInputStream(testBytes);

        assertEquals(ArchiveFormat.ZIP, factory.getFormat(new ZipArchiveInputStream(testInput)));
        assertEquals(ArchiveFormat.JAR, factory.getFormat(new JarArchiveInputStream(testInput)));
        assertEquals(ArchiveFormat.AR, factory.getFormat(new ArArchiveInputStream(testInput)));
        assertEquals(ArchiveFormat.CPIO, factory.getFormat(new CpioArchiveInputStream(testInput)));
        assertEquals(ArchiveFormat.DUMP, factory.getFormat(new DumpArchiveInputStream(testInput)));
        assertEquals(ArchiveFormat.TAR, factory.getFormat(new TarArchiveInputStream(testInput)));

        assertNull(factory.getFormat(new ByteArrayInputStream(new byte[0])));
        assertNull(factory.getFormat(null));
        assertNull(factory.getFormat((InputStream) null));
    }
}