package org.apache.commons.compress.archivers;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import static org.junit.Assert.*;
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

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamShouldReturnArArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream input = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);

        assertTrue(input instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamShouldReturnZipArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream input = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);

        assertTrue(input instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamShouldReturnTarArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream input = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);

        assertTrue(input instanceof TarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamShouldReturnJarArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream input = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);

        assertTrue(input instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamShouldReturnCpioArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream input = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);

        assertTrue(input instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamShouldReturnDumpArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream input = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);

        assertTrue(input instanceof DumpArchiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamShouldThrowArchiveExceptionWhenArchiverNameIsUnknown() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);

        factory.createArchiveInputStream("unknown", in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamShouldThrowIllegalArgumentExceptionWhenArchiverNameIsNull() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);

        factory.createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamShouldThrowIllegalArgumentExceptionWhenInputStreamIsNull() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveOutputStreamShouldReturnArArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();

        ArchiveOutputStream output = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);

        assertTrue(output instanceof ArArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamShouldReturnZipArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();

        ArchiveOutputStream output = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);

        assertTrue(output instanceof ZipArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamShouldReturnTarArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();

        ArchiveOutputStream output = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);

        assertTrue(output instanceof TarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamShouldReturnJarArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();

        ArchiveOutputStream output = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);

        assertTrue(output instanceof JarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamShouldReturnCpioArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();

        ArchiveOutputStream output = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);

        assertTrue(output instanceof CpioArchiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamShouldThrowArchiveExceptionWhenArchiverNameIsUnknown() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();

        factory.createArchiveOutputStream("unknown", out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamShouldThrowIllegalArgumentExceptionWhenArchiverNameIsNull() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();

        factory.createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamShouldThrowIllegalArgumentExceptionWhenOutputStreamIsNull() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveInputStreamShouldReturnZipArchiveInputStreamBasedOnSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(generateZipFileSignature());

        ArchiveInputStream input = factory.createArchiveInputStream(in);

        assertTrue(input instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamShouldReturnJarArchiveInputStreamBasedOnSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(generateJarFileSignature());

        ArchiveInputStream input = factory.createArchiveInputStream(in);

        assertTrue(input instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamShouldReturnArArchiveInputStreamBasedOnSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(generateArFileSignature());

        ArchiveInputStream input = factory.createArchiveInputStream(in);

        assertTrue(input instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamShouldReturnCpioArchiveInputStreamBasedOnSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(generateCpioFileSignature());

        ArchiveInputStream input = factory.createArchiveInputStream(in);

        assertTrue(input instanceof CpioArchiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamShouldThrowArchiveExceptionIfNoArchiverFoundForTheStreamSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);

        factory.createArchiveInputStream(in);
    }

    private byte[] generateZipFileSignature() {
        return new byte[]{80, 75, 3, 4};
    }

    private byte[] generateJarFileSignature() {
        return new byte[]{80, 75, 3, 4, 20, 0};
    }

    private byte[] generateArFileSignature() {
        return new byte[]{33, 60, 97, 114, 99, 104, 62};
    }

    private byte[] generateCpioFileSignature() {
        return new byte[]{59, 60, 48, 48};
    }
}
