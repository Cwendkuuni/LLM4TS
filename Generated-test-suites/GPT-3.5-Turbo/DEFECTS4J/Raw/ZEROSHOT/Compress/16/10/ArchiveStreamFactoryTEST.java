import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ArchiveStreamFactoryTest {
    
    @Mock
    InputStream mockedInputStream;
    
    @Mock
    OutputStream mockedOutputStream;
    
    ArchiveStreamFactory archiveStreamFactory;
    
    @Before
    public void setUp() {
        mockedInputStream = Mockito.mock(InputStream.class);
        mockedOutputStream = Mockito.mock(OutputStream.class);
        archiveStreamFactory = new ArchiveStreamFactory();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(null, mockedInputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream("zip", null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream(null, mockedOutputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream("zip", null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStreamAndNullArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(null, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStreamAndNullArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream(null, null);
    }
    
    @Test
    public void testCreateArchiveInputStreamAR() throws ArchiveException {
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream("ar", mockedInputStream);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamZIP() throws ArchiveException {
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream("zip", mockedInputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamTAR() throws ArchiveException {
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream("tar", mockedInputStream);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamJAR() throws ArchiveException {
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream("jar", mockedInputStream);
        assertTrue(archiveInputStream instanceof JarArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamCPIO() throws ArchiveException {
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream("cpio", mockedInputStream);
        assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamDUMP() throws ArchiveException {
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream("dump", mockedInputStream);
        assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamInvalidArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream("invalid", mockedInputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamAR() throws ArchiveException {
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("ar", mockedOutputStream);
        assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamZIP() throws ArchiveException {
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("zip", mockedOutputStream);
        assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamTAR() throws ArchiveException {
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("tar", mockedOutputStream);
        assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamJAR() throws ArchiveException {
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("jar", mockedOutputStream);
        assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamCPIO() throws ArchiveException {
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("cpio", mockedOutputStream);
        assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamInvalidArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream("invalid", mockedOutputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullStream() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithStreamNotSupportingMark() throws ArchiveException {
        InputStream nonMarkSupportedInputStream = new ByteArrayInputStream(new byte[0]);
        archiveStreamFactory.createArchiveInputStream(nonMarkSupportedInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamZIPFromStream() throws ArchiveException {
        byte[] zipSignature = ZipArchiveInputStream.LFH_SIG.getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zipSignature);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(byteArrayInputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamJARFromStream() throws ArchiveException {
        byte[] jarSignature = JarArchiveInputStream.LFH_SIG.getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jarSignature);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(byteArrayInputStream);
        assertTrue(archiveInputStream instanceof JarArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamARFromStream() throws ArchiveException {
        byte[] arSignature = ArArchiveInputStream.AR_ARCHIVE_MAGIC.getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arSignature);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(byteArrayInputStream);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamCPIOFromStream() throws ArchiveException {
        byte[] cpioSignature = CpioArchiveInputStream.CPIO_MAGIC.getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cpioSignature);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(byteArrayInputStream);
        assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamDUMPFromStream() throws ArchiveException {
        byte[] dumpSignature = DumpArchiveInputStream.SUN_MAGIC.getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dumpSignature);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(byteArrayInputStream);
        assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamTARFromStream() throws ArchiveException {
        byte[] tarSignature = new byte[512];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(tarSignature);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(byteArrayInputStream);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamInvalidSignatureFromStream() throws ArchiveException {
        byte[] invalidSignature = "invalid".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(invalidSignature);
        archiveStreamFactory.createArchiveInputStream(byteArrayInputStream);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamInvalidDumpSignatureFromStream() throws ArchiveException {
        byte[] invalidDumpSignature = new byte[32];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(invalidDumpSignature);
        archiveStreamFactory.createArchiveInputStream(byteArrayInputStream);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamInvalidTarSignatureFromStream() throws ArchiveException {
        byte[] invalidDumpSignature = new byte[512];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(invalidDumpSignature);
        archiveStreamFactory.createArchiveInputStream(byteArrayInputStream);
    }
}