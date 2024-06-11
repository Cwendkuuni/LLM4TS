import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test valid archives
        try (InputStream in = new ByteArrayInputStream("valid-ar-signature".getBytes());) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
            assertNotNull(archives);
        }

        try (InputStream in = new ByteArrayInputStream("valid-zip-signature".getBytes())) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNotNull(archives);
        }

        try (InputStream in = new ByteArrayInputStream("valid-tar-signature".getBytes())) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, in);
            assertNotNull(archives);
        }

        try (InputStream in = new ByteArrayInputStream("valid-jar-signature".getBytes())) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, in);
            assertNotNull(archives);
        }

        try (InputStream in = new ByteArrayInputStream("valid-cpio-signature".getBytes())) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
            assertNotNull(archives);
        }

        try (InputStream in = new ByteArrayInputStream("valid-dump-signature".getBytes())) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
            assertNotNull(archives);
        }

        // Test unknown archive
        try (InputStream in = new ByteArrayInputStream("unknown-signature".getBytes())) {
            assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in));
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test valid archives
        try (OutputStream out = new ByteArrayOutputStream()) {
            ArchiveOutputStream archives = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, out);
            assertNotNull(archives);
        }

        try (OutputStream out = new ByteArrayOutputStream()) {
            ArchiveOutputStream archives = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertNotNull(archives);
        }

        try (OutputStream out = new ByteArrayOutputStream()) {
            ArchiveOutputStream archives = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
            assertNotNull(archives);
        }

        try (OutputStream out = new ByteArrayOutputStream()) {
            ArchiveOutputStream archives = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
            assertNotNull(archives);
        }

        try (OutputStream out = new ByteArrayOutputStream()) {
            ArchiveOutputStream archives = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
            assertNotNull(archives);
        }

        try (OutputStream out = new ByteArrayOutputStream()) {
            ArchiveOutputStream archives = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, out);
            assertNotNull(archives);
        }

        // Test unknown archive
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.UNKNOWN, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() throws Exception {
        // Test valid archives
        final byte[] validArSignature = "valid-ar-signature".getBytes();
        try (InputStream in = new ByteArrayInputStream(validArSignature)) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
            assertNotNull(archives);
        }

        final byte[] validZipSignature = "valid-zip-signature".getBytes();
        try (InputStream in = new ByteArrayInputStream(validZipSignature)) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNotNull(archives);
        }

        final byte[] validTarSignature = "valid-tar-signature".getBytes();
        try (InputStream in = new ByteArrayInputStream(validTarSignature)) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, in);
            assertNotNull(archives);
        }

        final byte[] validJarSignature = "valid-jar-signature".getBytes();
        try (InputStream in = new ByteArrayInputStream(validJarSignature)) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, in);
            assertNotNull(archives);
        }

        final byte[] validCpioSignature = "valid-cpio-signature".getBytes();
        try (InputStream in = new ByteArrayInputStream(validCpioSignature)) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
            assertNotNull(archives);
        }

        final byte[] validDumpSignature = "valid-dump-signature".getBytes();
        try (InputStream in = new ByteArrayInputStream(validDumpSignature)) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
            assertNotNull(archives);
        }

        // Test unknown archive
        final byte[] unknownSignature = "unknown-signature".getBytes();
        try (InputStream in = new ByteArrayInputStream(unknownSignature)) {
            assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in));
        }
    }

    @Test
    public void testCreateArchiveOutputStreamFromBytes() throws Exception {
        // Test valid archives
        final byte[] validArSignature = "valid-ar-signature".getBytes();
        try (OutputStream out = new ByteArrayOutputStream()) {
            ArchiveOutputStream archives = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, out);
            assertNotNull(archives);
            archives.close();
            assertEquals(validArSignature, out.toByteArray());
        }

        final byte[] validZipSignature = "valid-zip-signature".getBytes();
        try (OutputStream out = new ByteArrayOutputStream()) {
            ArchiveOutputStream archives = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertNotNull(archives);
            archives.close();
            assertEquals(validZipSignature, out.toByteArray());
        }

        final byte[] validTarSignature = "valid-tar-signature".getBytes();
        try (OutputStream out = new ByteArrayOutputStream()) {
            ArchiveOutputStream archives = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
            assertNotNull(archives);
            archives.close();
            assertEquals(validTarSignature, out.toByteArray());
        }

        final byte[] validJarSignature = "valid-jar-signature".getBytes();
        try (OutputStream out = new ByteArrayOutputStream()) {
            ArchiveOutputStream archives = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
            assertNotNull(archives);
            archives.close();
            assertEquals(validJarSignature, out.toByteArray());
        }

        final byte[] validCpioSignature = "valid-cpio-signature".getBytes();
        try (OutputStream out = new ByteArrayOutputStream()) {
            ArchiveOutputStream archives = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
            assertNotNull(archives);
            archives.close();
            assertEquals(validCpioSignature, out.toByteArray());
        }

        final byte[] validDumpSignature = "valid-dump-signature".getBytes();
        try (OutputStream out = new ByteArrayOutputStream()) {
            ArchiveOutputStream archives = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, out);
            assertNotNull(archives);
            archives.close();
            assertEquals(validDumpSignature, out.toByteArray());
        }

        // Test unknown archive
        final byte[] unknownSignature = "unknown-signature".getBytes();
        try (OutputStream out = new ByteArrayOutputStream()) {
            assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, out));
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        // Test valid archives
        try (InputStream in = new ByteArrayInputStream("valid-ar-signature".getBytes());) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
            assertNotNull(archives);
        }

        try (InputStream in = new ByteArrayInputStream("valid-zip-signature".getBytes())) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNotNull(archives);
        }

        try (InputStream in = new ByteArrayInputStream("valid-tar-signature".getBytes())) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, in);
            assertNotNull(archives);
        }

        try (InputStream in = new ByteArrayInputStream("valid-jar-signature".getBytes())) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, in);
            assertNotNull(archives);
        }

        try (InputStream in = new ByteArrayInputStream("valid-cpio-signature".getBytes())) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
            assertNotNull(archives);
        }

        try (InputStream in = new ByteArrayInputStream("valid-dump-signature".getBytes())) {
            ArchiveInputStream archives = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
            assertNotNull(archives);
        }

        // Test unknown archive
        try (InputStream in = new ByteArrayInputStream("unknown-signature".getBytes())) {
            assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in));
        }
    }
}