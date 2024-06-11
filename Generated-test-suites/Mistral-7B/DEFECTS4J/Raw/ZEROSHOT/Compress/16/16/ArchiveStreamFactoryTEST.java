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
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.ArchiveStreamFactory.AR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.CPIO;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.DUMP;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.JAR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.TAR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.ZIP;

/**
 * Unit tests for {@link ArchiveStreamFactory} class.
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        testCreateArchiveInputStream(ZIP, new ByteArrayInputStream(ZipArchiveInputStream.getSignature()), new ZipArchiveInputStream(new ByteArrayInputStream(ZipArchiveInputStream.getSignature())));
        testCreateArchiveInputStream(JAR, new ByteArrayInputStream(JarArchiveInputStream.getSignature()), new JarArchiveInputStream(new ByteArrayInputStream(JarArchiveInputStream.getSignature())));
        testCreateArchiveInputStream(AR, new ByteArrayInputStream(ArArchiveInputStream.getSignature()), new ArArchiveInputStream(new ByteArrayInputStream(ArArchiveInputStream.getSignature())));
        testCreateArchiveInputStream(CPIO, new ByteArrayInputStream(CpioArchiveInputStream.getSignature()), new CpioArchiveInputStream(new ByteArrayInputStream(CpioArchiveInputStream.getSignature())));
        testCreateArchiveInputStream(DUMP, new ByteArrayInputStream(DumpArchiveInputStream.getSignature()), new DumpArchiveInputStream(new ByteArrayInputStream(DumpArchiveInputStream.getSignature())));
        testCreateArchiveInputStream(TAR, new ByteArrayInputStream(TarArchiveInputStream.getSignature()), new TarArchiveInputStream(new ByteArrayInputStream(TarArchiveInputStream.getSignature())));

        // Test unknown archive types
        InputStream unknownStream = new ByteArrayInputStream(new byte[10]);
        try {
            ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(null, unknownStream);
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
        try {
            ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream("unknown", unknownStream);
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
        try {
            ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(new ByteArrayInputStream(new byte[10]), unknownStream);
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }

        // Test null parameters
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null, null);
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
        try {
            new ArchiveStreamFactory().createArchiveInputStream(new String[0], null);
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null, new OutputStream() {});
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ZIP, null);
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ZIP, new OutputStream() {});
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }

        try {
            new ArchiveStreamFactory().createArchiveInputStream(ZIP, new ByteArrayInputStream(new byte[12]));
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }

        try {
            new ArchiveStreamFactory().createArchiveInputStream("ZIP", new ByteArrayInputStream(new byte[10]));
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        testCreateArchiveOutputStream(ZIP, new ByteArrayOutputStream());
        testCreateArchiveOutputStream(JAR, new ByteArrayOutputStream());
        testCreateArchiveOutputStream(AR, new ByteArrayOutputStream());
        testCreateArchiveOutputStream(CPIO, new ByteArrayOutputStream());
        testCreateArchiveOutputStream(DUMP, new ByteArrayOutputStream());
        testCreateArchiveOutputStream(TAR, new ByteArrayOutputStream());

        // Test unknown archive types
        OutputStream unknownStream = new ByteArrayOutputStream();
        try {
            ArchiveOutputStream outputStream = new ArchiveStreamFactory().createArchiveOutputStream(null, unknownStream);
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
        try {
            ArchiveOutputStream outputStream = new ArchiveStreamFactory().createArchiveOutputStream("unknown", unknownStream);
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
        try {
            ArchiveOutputStream outputStream = new ArchiveStreamFactory().createArchiveOutputStream(new OutputStream() {}, unknownStream);
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }

        // Test null parameters
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(null, null);
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(new String[0], null);
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(null, new InputStream() {});
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(ZIP, null);
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(ZIP, new InputStream() {});
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }

        try {
            new ArchiveStreamFactory().createArchiveOutputStream(ZIP, new ByteArrayOutputStream(10));
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }

        try {
            new ArchiveStreamFactory().createArchiveOutputStream("ZIP", new ByteArrayOutputStream(10));
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }

    }

    private void testCreateArchiveInputStream(String archiverName, InputStream inputStream, ArchiveInputStream expected) throws Exception {
        ArchiveInputStream actual = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        Assert.assertEquals(expected.getClass(), actual.getClass());
    }

    private void testCreateArchiveOutputStream(String archiverName, OutputStream outputStream) throws Exception {
        ArchiveOutputStream actual = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        Assert.assertEquals(ArchiveOutputStream.class, actual.getClass());
    }
}