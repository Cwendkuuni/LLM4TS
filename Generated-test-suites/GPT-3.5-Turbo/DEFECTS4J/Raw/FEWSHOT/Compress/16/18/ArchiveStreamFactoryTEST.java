import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    public void testCreateArchiveInputStream_withValidArArchiverName_shouldReturnArArchiveInputStream() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertNotNull(ais);
        assertTrue(ais instanceof ArArchiveInputStream);;
    }

    @Test
    public void testCreateArchiveInputStream_withValidZipArchiverName_shouldReturnZipArchiveInputStream() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(ais);
        assertTrue(ais instanceof ZipArchiveInputStream);;
    }

    @Test
    public void testCreateArchiveInputStream_withValidTarArchiverName_shouldReturnTarArchiveInputStream() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertNotNull(ais);
        assertTrue(ais instanceof TarArchiveInputStream);;
    }

    @Test
    public void testCreateArchiveInputStream_withValidJarArchiverName_shouldReturnJarArchiveInputStream() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        assertNotNull(ais);
        assertTrue(ais instanceof JarArchiveInputStream);;
    }

    @Test
    public void testCreateArchiveInputStream_withValidCpioArchiverName_shouldReturnCpioArchiveInputStream() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        assertNotNull(ais);
        assertTrue(ais instanceof CpioArchiveInputStream);;
    }

    @Test
    public void testCreateArchiveInputStream_withValidDumpArchiverName_shouldReturnDumpArchiveInputStream() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
        assertNotNull(ais);
        assertTrue(ais instanceof DumpArchiveInputStream);;
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_withInvalidArchiverName_shouldThrowArchiveException() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream("invalid", in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullArchiverName_shouldThrowIllegalArgumentException() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullInputStream_shouldThrowIllegalArgumentException() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test
    public void testCreateArchiveOutputStream_withValidArArchiverName_shouldReturnArArchiveOutputStream() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertNotNull(aos);
        assertTrue(aos instanceof ArArchiveOutputStream);;
    }

    @Test
    public void testCreateArchiveOutputStream_withValidZipArchiverName_shouldReturnZipArchiveOutputStream() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertNotNull(aos);
        assertTrue(aos instanceof ZipArchiveOutputStream);;
    }

    @Test
    public void testCreateArchiveOutputStream_withValidTarArchiverName_shouldReturnTarArchiveOutputStream() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertNotNull(aos);
        assertTrue(aos instanceof TarArchiveOutputStream);;
    }

    @Test
    public void testCreateArchiveOutputStream_withValidJarArchiverName_shouldReturnJarArchiveOutputStream() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        assertNotNull(aos);
        assertTrue(aos instanceof JarArchiveOutputStream);;
    }

    @Test
    public void testCreateArchiveOutputStream_withValidCpioArchiverName_shouldReturnCpioArchiveOutputStream() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        assertNotNull(aos);
        assertTrue(aos instanceof CpioArchiveOutputStream);;
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_withInvalidArchiverName_shouldThrowArchiveException() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("invalid", out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullArchiverName_shouldThrowIllegalArgumentException() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullOutputStream_shouldThrowIllegalArgumentException() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test
    public void testCreateArchiveInputStream_withValidInputStream_shouldReturnArchiveInputStream() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullInputStream_shouldThrowIllegalArgumentException() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveInputStream(null);
    }
}