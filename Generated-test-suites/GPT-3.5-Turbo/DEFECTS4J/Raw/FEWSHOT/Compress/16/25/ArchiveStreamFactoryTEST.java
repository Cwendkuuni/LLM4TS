import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException, IOException {
        // Test AR
        InputStream arInput = new ByteArrayInputStream(new byte[]{});
        ArchiveInputStream arInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, arInput);
        assertTrue(arInputStream instanceof ArArchiveInputStream);

        // Test ZIP
        InputStream zipInput = new ByteArrayInputStream(new byte[]{});
        ArchiveInputStream zipInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInput);
        assertTrue(zipInputStream instanceof ZipArchiveInputStream);

        // Test TAR
        InputStream tarInput = new ByteArrayInputStream(new byte[]{});
        ArchiveInputStream tarInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, tarInput);
        assertTrue(tarInputStream instanceof TarArchiveInputStream);

        // Test JAR
        InputStream jarInput = new ByteArrayInputStream(new byte[]{});
        ArchiveInputStream jarInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, jarInput);
        assertTrue(jarInputStream instanceof JarArchiveInputStream);

        // Test CPIO
        InputStream cpioInput = new ByteArrayInputStream(new byte[]{});
        ArchiveInputStream cpioInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInput);
        assertTrue(cpioInputStream instanceof CpioArchiveInputStream);

        // Test DUMP
        InputStream dumpInput = new ByteArrayInputStream(new byte[]{});
        ArchiveInputStream dumpInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInput);
        assertTrue(dumpInputStream instanceof DumpArchiveInputStream);

        // Test Unknown Archiver
        try {
            InputStream unknownInput = new ByteArrayInputStream(new byte[]{});
            archiveStreamFactory.createArchiveInputStream("unknown", unknownInput);
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException, IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{});
        archiveStreamFactory.createArchiveInputStream(null, input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException, IOException {
        archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
        // Test AR
        OutputStream arOutput = new ByteArrayOutputStream();
        ArchiveOutputStream arOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, arOutput);
        assertTrue(arOutputStream instanceof ArArchiveOutputStream);

        // Test ZIP
        OutputStream zipOutput = new ByteArrayOutputStream();
        ArchiveOutputStream zipOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutput);
        assertTrue(zipOutputStream instanceof ZipArchiveOutputStream);

        // Test TAR
        OutputStream tarOutput = new ByteArrayOutputStream();
        ArchiveOutputStream tarOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutput);
        assertTrue(tarOutputStream instanceof TarArchiveOutputStream);

        // Test JAR
        OutputStream jarOutput = new ByteArrayOutputStream();
        ArchiveOutputStream jarOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutput);
        assertTrue(jarOutputStream instanceof JarArchiveOutputStream);

        // Test CPIO
        OutputStream cpioOutput = new ByteArrayOutputStream();
        ArchiveOutputStream cpioOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutput);
        assertTrue(cpioOutputStream instanceof CpioArchiveOutputStream);

        // Test Unknown Archiver
        try {
            OutputStream unknownOutput = new ByteArrayOutputStream();
            archiveStreamFactory.createArchiveOutputStream("unknown", unknownOutput);
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException, IOException {
        OutputStream output = new ByteArrayOutputStream();
        archiveStreamFactory.createArchiveOutputStream(null, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException, IOException {
        archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws ArchiveException, IOException {
        // Test ZIP
        InputStream zipInput = new ByteArrayInputStream(new byte[]{80, 75, 03, 04});
        ArchiveInputStream zipInputStream = archiveStreamFactory.createArchiveInputStream(zipInput);
        assertTrue(zipInputStream instanceof ZipArchiveInputStream);

        // Test JAR
        InputStream jarInput = new ByteArrayInputStream(new byte[]{80});
        ArchiveInputStream jarInputStream = archiveStreamFactory.createArchiveInputStream(jarInput);
        assertTrue(jarInputStream instanceof JarArchiveInputStream);

        // Test AR
        InputStream arInput = new ByteArrayInputStream(new byte[]{33, 60, 123});
        ArchiveInputStream arInputStream = archiveStreamFactory.createArchiveInputStream(arInput);
        assertTrue(arInputStream instanceof ArArchiveInputStream);

        // Test CPIO
        InputStream cpioInput = new ByteArrayInputStream(new byte[]{37, 95, 101, 123});
        ArchiveInputStream cpioInputStream = archiveStreamFactory.createArchiveInputStream(cpioInput);
        assertTrue(cpioInputStream instanceof CpioArchiveInputStream);

        // Test DUMP
        InputStream dumpInput = new ByteArrayInputStream(new byte[]{49, 79, 33});
        ArchiveInputStream dumpInputStream = archiveStreamFactory.createArchiveInputStream(dumpInput);
        assertTrue(dumpInputStream instanceof DumpArchiveInputStream);

        // Test TAR
        InputStream tarInput = new ByteArrayInputStream(new byte[]{48, 32, 123, 123});
        ArchiveInputStream tarInputStream = archiveStreamFactory.createArchiveInputStream(tarInput);
        assertTrue(tarInputStream instanceof TarArchiveInputStream);

        // Test Unknown Archiver
        try {
            InputStream unknownInput = new ByteArrayInputStream(new byte[]{});
            archiveStreamFactory.createArchiveInputStream(unknownInput);
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }

        // Test Null Stream
        try {
            archiveStreamFactory.createArchiveInputStream(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        }
    }
}