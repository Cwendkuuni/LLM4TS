import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ARArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ARArchiveOutputStream;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Test for {@link ArchiveStreamFactory} class.
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        // Test with valid archiver name
        InputStream testInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, testInputStream);
        assertSame(ZipArchiveInputStream.class, archiveInputStream.getClass());

        // Test with unknown archiver name
        archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream("unknown", testInputStream);
        assertNull(archiveInputStream);

        // Test with null input stream
        assertNull(new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, null));
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        // Test with valid archiver name
        OutputStream testOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, testOutputStream);
        assertSame(ZipArchiveOutputStream.class, archiveOutputStream.getClass());

        // Test with unknown archiver name
        archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream("unknown", testOutputStream);
        assertNull(archiveOutputStream);

        // Test with null output stream
        assertNull(new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null));
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws ArchiveException, IOException {
        // Test with valid archiver name
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        archiveOutputStream.putArchiveEntry(new JarArchiveEntry("test.jar"));
        archiveOutputStream.write(new byte[1], 0, 1);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        archiveOutputStream.close();

        InputStream testInputStream = new ByteArrayInputStream(testOutputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, testInputStream);
        JarArchiveEntry jarArchiveEntry = (JarArchiveEntry) archiveInputStream.getNextEntry();
        assertSame(jarArchiveEntry.getName(), "test.jar");
        archiveInputStream.close();

        // Test with unknown archiver name
        archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream("unknown", testInputStream);
        assertNull(archiveInputStream);

        // Test with null input stream
        assertNull(new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, null));

        // Test with empty input stream
        testInputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, testInputStream);
        assertFalse(archiveInputStream.hasNextEntry());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamFromStream() throws ArchiveException, IOException {
        // Test with valid archiver name
        InputStream testInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, testInputStream);
        JarArchiveEntry jarArchiveEntry = new JarArchiveEntry("test.jar");
        JarArchiveOutputStream jarArchiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream());
        jarArchiveOutputStream.putNextEntry(jarArchiveEntry);

        OutputStream outputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.JAR, jarArchiveOutputStream);
        byte[] data = new byte[1];
        outputStream.write(data, 0, 1);
        outputStream.closeArchiveEntry();

        archiveInputStream.close();
        jarArchiveOutputStream.finish();
        jarArchiveOutputStream.close();

        byte[] output = ((ByteArrayOutputStream) jarArchiveOutputStream).toByteArray();
        InputStream testOutput = new ByteArrayInputStream(output);
        JarArchiveInputStream jarArchiveInputStream = (JarArchiveInputStream) new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.JAR, testOutput);
        JarArchiveEntry testArchiveEntry = (JarArchiveEntry) jarArchiveInputStream.getNextEntry();
        assertSame(testArchiveEntry.getName(), "test.jar");
        assertSame(testArchiveEntry.getSize(), 1);
        testArchiveInputStream.close();

        // Test with unknown archiver name
        archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream("unknown", testInputStream);
        assertNull(archiveInputStream);

        // Test with null input stream
        assertNull(new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.JAR, null));
    }

    @Test
    public void testCreateArchiveInputStreamFromFirstBytes() throws ArchiveException, IOException {
        // Test with valid archiver name
        byte[] tarSignature = new byte[]{'0', '0', '07', '0', '21', '10', '0'};
        InputStream testInputStream = new ByteArrayInputStream(tarSignature);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.TAR, testInputStream);
        assertSame(TarArchiveInputStream.class, archiveInputStream.getClass());

        // Test with unknown archiver name
        archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream("unknown", testInputStream);
        assertNull(archiveInputStream);

        // Test with null input stream
        assertNull(new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.TAR, null));
    }

    @Test
    public void testCreateArchiveOutputStreamFromFirstBytes() throws ArchiveException, IOException {
        // Test with valid archiver name
        byte[] tarHeader = new byte[512];
        OutputStream testOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, testOutputStream);
        archiveOutputStream.putNextEntry(new TarArchiveEntry("test.tar"));
        byte[] data = new byte[1];
        archiveOutputStream.write(data, 0, 1);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        archiveOutputStream.close();

        byte[] tarSignature = new byte[]{'0', '0', '07', '0', '21', '10', '0'};
        InputStream testInputStream = new ByteArrayInputStream(tarSignature);
        ArchiveInputStream archiveInput = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.TAR, testInputStream);
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveInput.getNextEntry();
        assertSame(tarArchiveEntry.getName(), "test.tar");
        testInputStream.close();

        // Test with unknown archiver name
        archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream("unknown", testInputStream);
        assertNull(archiveInputStream);

        // Test with null input stream
        assertNull(new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, null));
    }
}