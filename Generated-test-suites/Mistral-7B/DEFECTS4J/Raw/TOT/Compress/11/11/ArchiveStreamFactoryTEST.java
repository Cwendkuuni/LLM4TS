import static org.apache.commons.compress.archivers.ArchiveFormat.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveFormat;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ARArchiveEntry;
import org.apache.commons.compress.archivers.ar.ARArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Test for {@link ArchiveStreamFactory} class
 */
public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setup() {
        factory = new ArchiveStreamFactory();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateArchiveInputStreamFromNameAndInputStream() throws IOException, ArchiveException {
        final String archiveName = AR;
        final InputStream inputStream = mock(InputStream.class);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiveName, inputStream);
        verify(inputStream).mark(anyInt());
        verify(inputStream).reset();
        assertSame(AR.equalsIgnoreCase(archiveName), AR.equals(archiveInputStream.getFormat()));
    }

    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() throws IOException, ArchiveException {
        final String archiveName = AR;
        final OutputStream outputStream = mock(OutputStream.class);
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiveName, outputStream);
        assertSame(AR.equalsIgnoreCase(archiveName), AR.equals(archiveOutputStream.getFormat()));
        verify(outputStream).mark(anyInt());
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws IOException, ArchiveException {
        final InputStream inputStream = mock(InputStream.class);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
        verify(inputStream).mark(anyInt());
        // Verify that no specific ArchiveFormat is instantiated
        assertNull(archiveInputStream.getFormat());
    }

    @Test
    public void testCreateArchiveInputStreamFromUnsupportedFormat() throws IOException, ArchiveException {
        final InputStream inputStream = mock(InputStream.class);
        doThrow(new UnsupportedOperationException("Unsupported format")).when(inputStream).toString();
        assertThrows(ArchiveException.class, () -> {
            factory.createArchiveInputStream(inputStream.toString(), inputStream);
        });
    }

    @Test
    public void testCreateArchiveOutputStreamFromUnsupportedFormat() throws IOException, ArchiveException {
        final OutputStream outputStream = mock(OutputStream.class);
        doThrow(new UnsupportedOperationException("Unsupported format")).when(outputStream).toString();
        assertThrows(ArchiveException.class, () -> {
            factory.createArchiveOutputStream(inputStream.toString(), outputStream);
        });
    }

    @Test
    public void testCreateZipArchiveEntryAndStream() throws IOException {
        final String archiveName = ZIP;
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiveName, outputStream);
        final ArchiveEntry testEntry = new JarArchiveEntry("testEntry.txt");
        archiveOutputStream.putArchiveEntry(testEntry);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.close();

        final InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiveName, inputStream);
        final ArchiveEntry entry = archiveInputStream.getNextEntry();
        assertSame(testEntry, entry);
        archiveInputStream.close();
    }

    @Test
    public void testCreateTarArchiveEntryAndStream() throws IOException {
        final String archiveName = TAR;
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiveName, outputStream);
        final TarArchiveEntry testEntry = new TarArchiveEntry("testEntry.txt");
        archiveOutputStream.putNextEntry(testEntry);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.close();

        final InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiveName, inputStream);
        final ArchiveEntry entry = archiveInputStream.getNextEntry();
        assertSame(testEntry, entry);
        archiveInputStream.close();
    }

    @Test
    public void testCreateInvalidArchiveEntryAndStream() throws IOException, ArchiveException {
        final String archiveName = AR;
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiveName, outputStream);
        final ARArchiveEntry testEntry = new ARArchiveEntry("testEntry.txt");
        testEntry.setSize(0);

        try {
            archiveOutputStream.putArchiveEntry(testEntry);
            assertFalse("Expected ArchiveException to be thrown when invalid AR format is used.", true);
        } catch (ArchiveException e) {
            // Expected
        } finally {
            archiveOutputStream.close();
        }
    }
}