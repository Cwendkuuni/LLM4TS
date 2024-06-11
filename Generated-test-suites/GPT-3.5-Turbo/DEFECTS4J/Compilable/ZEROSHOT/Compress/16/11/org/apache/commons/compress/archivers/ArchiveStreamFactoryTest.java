package org.apache.commons.compress.archivers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamForAr() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new ByteArrayInputStream(new byte[]{0x00});
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
            assertNotNull(ais);
        } catch (ArchiveException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStreamForCpio() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new ByteArrayInputStream(new byte[]{0x00});
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
            assertNotNull(ais);
        } catch (ArchiveException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStreamForDump() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new ByteArrayInputStream(new byte[]{0x00});
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
            assertNotNull(ais);
        } catch (ArchiveException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStreamForJar() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new ByteArrayInputStream(new byte[]{0x00});
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
            assertNotNull(ais);
        } catch (ArchiveException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStreamForTar() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new ByteArrayInputStream(new byte[]{0x00});
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
            assertNotNull(ais);
        } catch (ArchiveException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStreamForZip() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new ByteArrayInputStream(new byte[]{0x00});
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNotNull(ais);
        } catch (ArchiveException e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[]{0x00});
        factory.createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveOutputStreamForAr() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
            assertNotNull(aos);
        } catch (ArchiveException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamForCpio() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
            assertNotNull(aos);
        } catch (ArchiveException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamForJar() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
            assertNotNull(aos);
        } catch (ArchiveException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamForTar() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
            assertNotNull(aos);
        } catch (ArchiveException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamForZip() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertNotNull(aos);
        } catch (ArchiveException e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveInputStreamWithNullStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(null);
            fail("Exception not thrown");
        } catch (ArchiveException e) {
            fail("Exception thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithStreamNotSupportingMark() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[]{0x00});
        factory.createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnsupportedArchiver() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[]{0x00});
        factory.createArchiveInputStream("unsupported", in);
    }
}