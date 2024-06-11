import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveEntry;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for ArchiveStreamFactory
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException {
        // Test creating an input stream for known formats
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = null;
        try {
            ais = new ArchiveStreamFactory().createArchiveInputStream("zip", in);
            Assert.assertTrue(ais instanceof ZipArchiveInputStream);
            ais = new ArchiveStreamFactory().createArchiveInputStream("tar", in);
            Assert.assertTrue(ais instanceof TarArchiveInputStream);
            ais = new ArchiveStreamFactory().createArchiveInputStream("jar", in);
            Assert.assertTrue(ais instanceof JarArchiveInputStream);
            ais = new ArchiveStreamFactory().createArchiveInputStream("ar", in);
            Assert.assertTrue(ais instanceof ArArchiveInputStream);
            ais = new ArchiveStreamFactory().createArchiveInputStream("cpio", in);
            Assert.assertTrue(ais instanceof CpioArchiveInputStream);
            ais = new ArchiveStreamFactory().createArchiveInputStream("arj", in);
            Assert.assertTrue(ais instanceof ArjArchiveInputStream);
        } finally {
            IOUtils.closeQuietly(ais);
            IOUtils.closeQuietly(in);
        }

        // Test creating an input stream for unknown format (7z)
        in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais7z = null;
        try {
            ais7z = new ArchiveStreamFactory().createArchiveInputStream("7z", in);
            Assert.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        } finally {
            IOUtils.closeQuietly(ais7z);
            IOUtils.closeQuietly(in);
        }

        // Test creating an input stream with a known signature
        byte[] tarSignature = TarArchiveInputStream.getSignature();
        InputStream tarSignatureStream = new ByteArrayInputStream(tarSignature);
        ArchiveInputStream aisTar = null;
        try {
            aisTar = new ArchiveStreamFactory().createArchiveInputStream(tarSignatureStream);
            Assert.assertTrue(aisTar instanceof TarArchiveInputStream);
        } finally {
            IOUtils.closeQuietly(aisTar);
            IOUtils.closeQuietly(tarSignatureStream);
        }

        // Test creating an input stream with a unknown signature
        byte[] unknownSignature = new byte[10];
        InputStream unknownSignatureStream = new ByteArrayInputStream(unknownSignature);
        ArchiveInputStream aisUnknown = null;
        try {
            aisUnknown = new ArchiveStreamFactory().createArchiveInputStream(unknownSignatureStream);
            Assert.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        } finally {
            IOUtils.closeQuietly(aisUnknown);
            IOUtils.closeQuietly(unknownSignatureStream);
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        // Test creating an output stream for known formats
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = null;
        try {
            aos = new ArchiveStreamFactory().createArchiveOutputStream("zip", out);
            Assert.assertTrue(aos instanceof ZipArchiveOutputStream);
            aos = new ArchiveStreamFactory().createArchiveOutputStream("tar", out);
            Assert.assertTrue(aos instanceof TarArchiveOutputStream);
            aos = new ArchiveStreamFactory().createArchiveOutputStream("jar", out);
            Assert.assertTrue(aos instanceof JarArchiveOutputStream);
            aos = new ArchiveStreamFactory().createArchiveOutputStream("ar", out);
            Assert.assertTrue(aos instanceof ArArchiveOutputStream);
            aos = new ArchiveStreamFactory().createArchiveOutputStream("cpio", out);
            Assert.assertTrue(aos instanceof CpioArchiveOutputStream);
            aos = new ArchiveStreamFactory().createArchiveOutputStream("arj", out);
            Assert.assertTrue(aos instanceof ArjArchiveOutputStream);
        } finally {
            IOUtils.closeQuietly(aos);
            IOUtils.closeQuietly(out);
        }

        // Test creating an output stream for unknown format (7z)
        OutputStream out7z = new ByteArrayOutputStream();
        ArchiveOutputStream aos7z = null;
        try {
            aos7z = new ArchiveStreamFactory().createArchiveOutputStream("7z", out7z);
            Assert.fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // expected
        } finally {
            IOUtils.closeQuietly(aos7z);
            IOUtils.closeQuietly(out7z);
        }

        // Test creating an archive entry
        ArchiveOutputStream aosTar = null;
        OutputStream os = new ByteArrayOutputStream();
        try {
            aosTar = new ArchiveStreamFactory().createArchiveOutputStream("tar", os);
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            aosTar.putNextEntry(entry);
        } finally {
            IOUtils.closeQuietly(aosTar);
            IOUtils.closeQuietly(os);
        }

        // Test creating an archive entry for unknown format (7z)
        ArchiveOutputStream aos7zEntry = null;
        OutputStream os7z = new ByteArrayOutputStream();
        try {
            aos7zEntry = new ArchiveStreamFactory().createArchiveOutputStream("7z", os7z);
            SevenZFile.StreamingSevenZFileEntry entry7z = new SevenZFile.StreamingSevenZFileEntry("test.txt");
            aos7zEntry.putNextEntry(entry7z);
            Assert.fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // expected
        } finally {
            IOUtils.closeQuietly(aos7zEntry);
            IOUtils.closeQuietly(os7z);
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithSignature() throws IOException {
        // Test creating an input stream for known formats with signature
        byte[] tarSignature = TarArchiveInputStream.getSignature();
        InputStream tarSignatureStream = new ByteArrayInputStream(tarSignature);
        ArchiveInputStream aisTar = null;
        try {
            aisTar = new ArchiveStreamFactory().createArchiveInputStream(tarSignature, tarSignatureStream);
            Assert.assertTrue(aisTar instanceof TarArchiveInputStream);
        } finally {
            IOUtils.closeQuietly(aisTar);
            IOUtils.closeQuietly(tarSignatureStream);
        }

        // Test creating an input stream for unknown format (7z) with signature
        byte[] sevenzSignature = SevenZFile.getSignature();
        InputStream sevenzSignatureStream = new ByteArrayInputStream(sevenzSignature);
        ArchiveInputStream ais7z = null;
        try {
            ais7z = new ArchiveStreamFactory().createArchiveInputStream(sevenzSignature, sevenzSignatureStream);
            Assert.fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // expected
        } finally {
            IOUtils.closeQuietly(ais7z);
            IOUtils.closeQuietly(sevenzSignatureStream);
        }
    }

}