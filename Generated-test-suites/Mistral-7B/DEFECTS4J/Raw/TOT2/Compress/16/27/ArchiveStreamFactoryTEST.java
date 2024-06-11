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

import java.io.*;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        // Test valid archiver names
        InputStream inputStream = new ByteArrayInputStream("valid zip signature".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream("valid tar signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream("valid jar signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        assertTrue(archiveInputStream instanceof JarArchiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream("valid ar signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream("valid cpio signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream("valid dump signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
        assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
        archiveInputStream.close();

        // Test invalid archiver names
        inputStream = new ByteArrayInputStream("invalid signature".getBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream("invalid", inputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
        inputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        // Test valid archiver names
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
        assertTrue(archiveOutputStream instanceof DumpArchiveOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        // Test invalid archiver names
        OutputStream invalidOutputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("invalid", invalidOutputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
        invalidOutputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws IOException, ArchiveException {
        // Test valid archiver names
        InputStream inputStream = new ByteArrayInputStream("valid zip signature".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream("valid tar signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream("valid jar signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        assertTrue(archiveInputStream instanceof JarArchiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream("valid ar signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream("valid cpio signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream("valid dump signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
        assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
        archiveInputStream.close();

        inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("", inputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        inputStream.close();

        inputStream = new ByteArrayInputStream("valid signature".getBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream("unknown", inputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
        inputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamFromOutputStream() throws IOException, ArchiveException {
        // Test valid archiver names
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
        assertTrue(archiveOutputStream instanceof DumpArchiveOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        outputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("", outputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        outputStream.close();

        outputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("unknown", outputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
        outputStream.close();
    }
}