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

import java.io.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test valid inputs
        for (String archiverName : new String[]{"AR", "ZIP", "TAR", "JAR", "CPIO", "DUMP"}) {
            InputStream input = new ByteArrayInputStream(createSignature(archiverName).getBytes());
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, input);
            Assert.assertNotNull(archiveInputStream);
            archiveInputStream.close();
        }

        // Test null input
        for (String archiverName : new String[]{"AR", "ZIP", "TAR", "JAR", "CPIO", "DUMP"}) {
            InputStream input = null;
            try {
                new ArchiveStreamFactory().createArchiveInputStream(archiverName, input);
                Assert.fail();
            } catch (ArchiveException e) {
                Assert.assertEquals("Archivername must not be null.", e.getMessage());
            }
        }

        // Test unknown archiverName
        InputStream input = new ByteArrayInputStream(createSignature("Unknown").getBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream("Unknown", input);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: Unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test valid inputs
        for (String archiverName : new String[]{"AR", "ZIP", "TAR", "JAR", "CPIO", "DUMP"}) {
            OutputStream output = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, output);
            Assert.assertNotNull(archiveOutputStream);
            archiveOutputStream.finish();
            archiveOutputStream.close();
        }

        // Test null output
        for (String archiverName : new String[]{"AR", "ZIP", "TAR", "JAR", "CPIO", "DUMP"}) {
            OutputStream output = null;
            try {
                new ArchiveStreamFactory().createArchiveOutputStream(archiverName, output);
                Assert.fail();
            } catch (ArchiveException e) {
                Assert.assertEquals("OutputStream must not be null.", e.getMessage());
            }
        }

        // Test unknown archiverName
        OutputStream output = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("Unknown", output);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: Unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidSignature() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[10]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("AR", input);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }

    private byte[] createSignature(String archiverName) {
        switch (archiverName) {
            case "AR":
                return AR_SIGNATURE;
            case "ZIP":
                return ZIP_SIGNATURE;
            case "TAR":
                return TAR_SIGNATURE;
            case "JAR":
                return JAR_SIGNATURE;
            case "CPIO":
                return CPIO_SIGNATURE;
            case "DUMP":
                return DUMP_SIGNATURE;
            default:
                throw new IllegalArgumentException("Unknown archiverName: " + archiverName);
        }
    }

    private static final byte[] AR_SIGNATURE = new byte[]{0x78, 0x41, 0x4d, 0x4c, 0x45, 0x4e, 0x43, 0x41, 0x50, 0x50, 0x50};
    private static final byte[] ZIP_SIGNATURE = new byte[]{0x50, 0x4b, 0x03, 0x04, 0x14, 0x00, 0x02, 0x01};
    private static final byte[] TAR_SIGNATURE = new byte[]{0x75, 0x74, 0x61, 0x72, 0x00, 0x01, 0x00, 0x00};
    private static final byte[] JAR_SIGNATURE = new byte[]{0x00, 0x02, 0x01, 0x01, 0x01, 0x00};
    private static final byte[] CPIO_SIGNATURE = new byte[]{0x73, 0x75, 0x70, 0x69, 0x63, 0x61, 0x72, 0x74, 0x65, 0x64};
    private static final byte[] DUMP_SIGNATURE = new byte[]{0x64, 0x75, 0x73, 0x74, 0x72, 0x65, 0x61, 0x6D, 0x65};
}