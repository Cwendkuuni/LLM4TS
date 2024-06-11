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
import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamStringInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = null;
        try {
            archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
            assertNotNull(archiveInputStream);
        } finally {
            if (archiveInputStream != null) {
                try {
                    archiveInputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamNullInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = null;
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownArchiver() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream("unknownArchiver", inputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamStringOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = null;
        try {
            archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
            assertNotNull(archiveOutputStream);
        } finally {
            if (archiveOutputStream != null) {
                try {
                    archiveOutputStream.finish();
                    archiveOutputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamNullOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = null;
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknownArchiver() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("unknownArchiver", outputStream);
    }

    @Test
    public void testCreateArchiveInputStreamInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = null;
        try {
            archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
            assertNotNull(archiveInputStream);
        } finally {
            if (archiveInputStream != null) {
                try {
                    archiveInputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamNegativeLengthInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(-1, inputStream);
    }

    @Test
    public void testCreateArchiveInputStreamMarkNotSupportedInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]) {
            @Override
            public synchronized void mark(int readlimit) {
                throw new UnsupportedOperationException();
            }
        };
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
            fail();
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamInvalidSignature() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        try {
            factory.createArchiveInputStream("invalidArchive", inputStream);
            fail();
        } catch (ArchiveException e) {
            //Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamZipSignature() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[22]);
        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
            assertNotNull(archiveInputStream);
        } finally {
            if (archiveInputStream != null) {
                try {
                    archiveInputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Test
    public void testCreateArchiveInputStreamJarSignature() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[22]);
        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
            assertNotNull(archiveInputStream);
        } finally {
            if (archiveInputStream != null) {
                try {
                    archiveInputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Test
    public void testCreateArchiveInputStreamArSignature() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[18]);
        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
            assertNotNull(archiveInputStream);
        } finally {
            if (archiveInputStream != null) {
                try {
                    archiveInputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Test
    public void testCreateArchiveInputStreamTarSignature() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[512]);
        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
            assertNotNull(archiveInputStream);
        } finally {
            if (archiveInputStream != null) {
                try {
                    archiveInputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Test
    public void testCreateArchiveInputStreamCpioSignature() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[18]);
        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
            assertNotNull(archiveInputStream);
        } finally {
            if (archiveInputStream != null) {
                try {
                    archiveInputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Test
    public void testCreateArchiveInputStreamDumpSignature() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[32]);
        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
            assertNotNull(archiveInputStream);
        } finally {
            if (archiveInputStream != null) {
                try {
                    archiveInputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }
}