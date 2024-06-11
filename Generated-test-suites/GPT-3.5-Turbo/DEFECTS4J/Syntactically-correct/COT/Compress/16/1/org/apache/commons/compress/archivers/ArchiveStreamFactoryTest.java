
import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamAR() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
            assertNotNull(ais);
            assertEquals(ArArchiveInputStream.class, ais.getClass());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamCPIO() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
            assertNotNull(ais);
            assertEquals(CpioArchiveInputStream.class, ais.getClass());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamDUMP() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
            assertNotNull(ais);
            assertEquals(DumpArchiveInputStream.class, ais.getClass());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamJAR() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
            assertNotNull(ais);
            assertEquals(JarArchiveInputStream.class, ais.getClass());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamTAR() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
            assertNotNull(ais);
            assertEquals(TarArchiveInputStream.class, ais.getClass());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamZIP() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNotNull(ais);
            assertEquals(ZipArchiveInputStream.class, ais.getClass());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiver() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream("unknown", in);
    }

    @Test
    public void testCreateArchiveOutputStreamAR() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
            assertNotNull(aos);
            assertEquals(ArArchiveOutputStream.class, aos.getClass());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamCPIO() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
            assertNotNull(aos);
            assertEquals(CpioArchiveOutputStream.class, aos.getClass());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamJAR() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
            assertNotNull(aos);
            assertEquals(JarArchiveOutputStream.class, aos.getClass());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamTAR() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
            assertNotNull(aos);
            assertEquals(TarArchiveOutputStream.class, aos.getClass());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamZIP() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertNotNull(aos);
            assertEquals(ZipArchiveOutputStream.class, aos.getClass());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiver() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("unknown", out);
    }

    @Test
    public void testCreateArchiveInputStreamWithInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
            assertTrue(ais instanceof ZipArchiveInputStream || ais instanceof JarArchiveInputStream
                    || ais instanceof ArArchiveInputStream || ais instanceof CpioArchiveInputStream
                    || ais instanceof DumpArchiveInputStream || ais instanceof TarArchiveInputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream((InputStream) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithUnsupportedInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownSignature() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(in);
    }
}

