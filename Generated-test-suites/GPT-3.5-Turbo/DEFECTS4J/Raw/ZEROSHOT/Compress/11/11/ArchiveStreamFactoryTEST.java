import org.junit.Test;
import static org.junit.Assert.*;

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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
            assertEquals(ArArchiveInputStream.class, archiveInputStream.getClass());

            in = new ByteArrayInputStream(new byte[0]);
            archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertEquals(ZipArchiveInputStream.class, archiveInputStream.getClass());

            in = new ByteArrayInputStream(new byte[0]);
            archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
            assertEquals(TarArchiveInputStream.class, archiveInputStream.getClass());

            in = new ByteArrayInputStream(new byte[0]);
            archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
            assertEquals(JarArchiveInputStream.class, archiveInputStream.getClass());

            in = new ByteArrayInputStream(new byte[0]);
            archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
            assertEquals(CpioArchiveInputStream.class, archiveInputStream.getClass());

            in = new ByteArrayInputStream(new byte[0]);
            archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
            assertEquals(DumpArchiveInputStream.class, archiveInputStream.getClass());
        } catch (ArchiveException e) {
            fail("ArchiveException should not be thrown");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream("unknown", in);
    }

    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            OutputStream out = null;
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
            assertEquals(ArArchiveOutputStream.class, archiveOutputStream.getClass());

            out = null;
            archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertEquals(ZipArchiveOutputStream.class, archiveOutputStream.getClass());

            out = null;
            archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
            assertEquals(TarArchiveOutputStream.class, archiveOutputStream.getClass());

            out = null;
            archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
            assertEquals(JarArchiveOutputStream.class, archiveOutputStream.getClass());

            out = null;
            archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
            assertEquals(CpioArchiveOutputStream.class, archiveOutputStream.getClass());
        } catch (ArchiveException e) {
            fail("ArchiveException should not be thrown");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = null;
        factory.createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = null;
        factory.createArchiveOutputStream("unknown", out);
    }

    @Test
    public void testCreateArchiveInputStreamWithInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(in);
            assertEquals(ZipArchiveInputStream.class, archiveInputStream.getClass());

            in = new ByteArrayInputStream(new byte[10]);
            archiveInputStream = factory.createArchiveInputStream(in);
            assertEquals(ZipArchiveInputStream.class, archiveInputStream.getClass());

            in = new ByteArrayInputStream(new byte[12]);
            archiveInputStream = factory.createArchiveInputStream(in);
            assertEquals(ArArchiveInputStream.class, archiveInputStream.getClass());

            in = new ByteArrayInputStream(new byte[32]);
            archiveInputStream = factory.createArchiveInputStream(in);
            assertEquals(DumpArchiveInputStream.class, archiveInputStream.getClass());

            in = new ByteArrayInputStream(new byte[512]);
            archiveInputStream = factory.createArchiveInputStream(in);
            assertEquals(TarArchiveInputStream.class, archiveInputStream.getClass());
        } catch (ArchiveException e) {
            fail("ArchiveException should not be thrown");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStreamUsingMark() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream((InputStream) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStreamNotSupportingMark() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        factory.createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownSignature2() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[12]);
        factory.createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownSignature3() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[32]);
        factory.createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownSignature4() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[512]);
        factory.createArchiveInputStream(in);
    }
}