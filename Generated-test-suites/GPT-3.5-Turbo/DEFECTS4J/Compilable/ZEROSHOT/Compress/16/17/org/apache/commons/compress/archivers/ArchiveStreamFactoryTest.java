package org.apache.commons.compress.archivers;
import static org.junit.Assert.*;
import org.junit.Test;
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

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamAr() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = new byte[]{0x41, 0x52};
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertNotNull(ais);
        assertFalse(ais.canReadEntryData(null));
        ais.close();
    }

    @Test
    public void testCreateArchiveInputStreamCpio() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = new byte[]{0x43, 0x50};
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        assertNotNull(ais);
        assertFalse(ais.canReadEntryData(null));
        ais.close();
    }

    @Test
    public void testCreateArchiveInputStreamDump() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = new byte[]{0x44, 0x55};
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
        assertNotNull(ais);
        assertFalse(ais.canReadEntryData(null));
        ais.close();
    }

    @Test
    public void testCreateArchiveInputStreamJar() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = new byte[]{0x4a, 0x41};
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        assertNotNull(ais);
        assertFalse(ais.canReadEntryData(null));
        ais.close();
    }

    @Test
    public void testCreateArchiveInputStreamTar() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = new byte[]{0x54, 0x41};
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertNotNull(ais);
        assertFalse(ais.canReadEntryData(null));
        ais.close();
    }

    @Test
    public void testCreateArchiveInputStreamZip() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = new byte[]{0x5a, 0x49};
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(ais);
        assertFalse(ais.canReadEntryData(null));
        ais.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullArchiverName() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullInputStream() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamInvalidArchiverName() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream("invalid", in);
    }

    @Test
    public void testCreateArchiveOutputStreamAr() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertNotNull(aos);
        aos.close();
    }

    @Test
    public void testCreateArchiveOutputStreamCpio() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        assertNotNull(aos);
        aos.close();
    }

    @Test
    public void testCreateArchiveOutputStreamJar() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        assertNotNull(aos);
        aos.close();
    }

    @Test
    public void testCreateArchiveOutputStreamTar() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertNotNull(aos);
        aos.close();
    }

    @Test
    public void testCreateArchiveOutputStreamZip() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertNotNull(aos);
        aos.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullArchiverName() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullOutputStream() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamInvalidArchiverName() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("invalid", out);
    }

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = new byte[]{0x5a, 0x49};
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        assertNotNull(ais);
        assertFalse(ais.canReadEntryData(null));
        ais.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullStream() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNotSupportedStream() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new InputStream() {
            public int read() throws IOException {
                return 0;
            }
            public int read(byte[] b) throws IOException {
                return 0;
            }
        };
        factory.createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamNoArchiverFound() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = new byte[]{0x41, 0x52};
        InputStream in = new ByteArrayInputStream(data);
        factory.createArchiveInputStream(in);
    }
}