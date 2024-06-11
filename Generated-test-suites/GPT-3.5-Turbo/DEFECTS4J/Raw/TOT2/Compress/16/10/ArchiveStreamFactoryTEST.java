import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidInput() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream("zip", in);
        assertNotNull(ais);
    }

    @Test
    public void testCreateArchiveOutputStream_ValidInput() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream("zip", out);
        assertNotNull(aos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        new ArchiveStreamFactory().createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveInputStream("zip", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveOutputStream("zip", null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownArchiver() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        new ArchiveStreamFactory().createArchiveInputStream("unknown", in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownArchiver() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("unknown", out);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NoMarkSupport() throws ArchiveException {
        InputStream in = mock(InputStream.class);
        when(in.markSupported()).thenReturn(false);
        new ArchiveStreamFactory().createArchiveInputStream(in);
    }
}