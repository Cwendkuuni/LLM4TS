package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
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

/**
 * Testing ArchiveStreamFactory class
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamNullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream(null, input);
        });
    }

    @Test
    public void testCreateArchiveInputStreamNullInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream("tar", null);
        });
    }

    @Test
    public void testCreateArchiveInputStreamUnknownArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> {
            factory.createArchiveInputStream("unknown", input);
        });
    }

    @Test
    public void testCreateArchiveInputStreamAr() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream result = factory.createArchiveInputStream("ar", input);
        assertNull(result);
    }

    @Test
    public void testCreateArchiveInputStreamZip() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream result = factory.createArchiveInputStream("zip", input);
        assertNull(result);
    }

    @Test
    public void testCreateArchiveInputStreamTar() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream result = factory.createArchiveInputStream("tar", input);
        assertNull(result);
    }

    @Test
    public void testCreateArchiveInputStreamJar() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream result = factory.createArchiveInputStream("jar", input);
        assertNull(result);
    }

    @Test
    public void testCreateArchiveInputStreamCpio() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream result = factory.createArchiveInputStream("cpio", input);
        assertNull(result);
    }

    @Test
    public void testCreateArchiveInputStreamDump() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream result = factory.createArchiveInputStream("dump", input);
        assertNull(result);
    }

    @Test
    public void testCreateArchiveOutputStreamNullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream(0);
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveOutputStream(null, output);
        });
    }

    @Test
    public void testCreateArchiveOutputStreamNullOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveOutputStream("tar", null);
        });
    }

    @Test
    public void testCreateArchiveOutputStreamUnknownArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream(0);
        assertThrows(ArchiveException.class, () -> {
            factory.createArchiveOutputStream("unknown", output);
        });
    }

    @Test
    public void testCreateArchiveOutputStreamAr() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream(512);
        ArchiveOutputStream result = factory.createArchiveOutputStream("ar", output);
        assertNull(result);
    }

    @Test
    public void testCreateArchiveOutputStreamZip() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream(512);
        ArchiveOutputStream result = factory.createArchiveOutputStream("zip", output);
        assertNull(result);
    }

    @Test
    public void testCreateArchiveOutputStreamTar() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream(512);
        ArchiveOutputStream result = factory.createArchiveOutputStream("tar", output);
        assertNull(result);
    }

    @Test
    public void testCreateArchiveOutputStreamJar() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream(512);
        ArchiveOutputStream result = factory.createArchiveOutputStream("jar", output);
        assertNull(result);
    }

    @Test
    public void testCreateArchiveOutputStreamCpio() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream(512);
        ArchiveOutputStream result = factory.createArchiveOutputStream("cpio", output);
        assertNull(result);
    }

    @Test
    public void testCreateArchiveOutputStreamDump() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream(512);
        ArchiveOutputStream result = factory.createArchiveOutputStream("dump", output);
        assertNull(result);
    }

    @Test
    public void testCreateArchiveInputStreamSignatureMatchZip() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(ZipArchiveInputStream.MAGIC.getValue());
        ArchiveInputStream result = factory.createArchiveInputStream("zip", input);
        assertNotNull(result);
        assertInstanceOf(ZipArchiveInputStream.class, result);
    }

    @Test
    public void testCreateArchiveInputStreamSignatureMatchJar() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(JarArchiveInputStream.MAGIC.getValue());
        ArchiveInputStream result = factory.createArchiveInputStream("jar", input);
        assertNotNull(result);
        assertInstanceOf(JarArchiveInputStream.class, result);
    }

    @Test
    public void testCreateArchiveInputStreamSignatureMatchAr() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(ArArchiveInputStream.MAGIC.getValue());
        ArchiveInputStream result = factory.createArchiveInputStream("ar", input);
        assertNotNull(result);
        assertInstanceOf(ArArchiveInputStream.class, result);
    }

    @Test
    public void testCreateArchiveInputStreamSignatureMatchCpio() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(CpioArchiveInputStream.MAGIC.getValue());
        ArchiveInputStream result = factory.createArchiveInputStream("cpio", input);
        assertNotNull(result);
        assertInstanceOf(CpioArchiveInputStream.class, result);
    }

    @Test
    public void testCreateArchiveInputStreamSignatureMatchTar() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(TarArchiveInputStream.MAGIC.getValue());
        ArchiveInputStream result = factory.createArchiveInputStream("tar", input);
        assertNotNull(result);
        assertInstanceOf(TarArchiveInputStream.class, result);
    }

    @Test
    public void testCreateArchiveInputStreamSignatureMatchDump() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(DumpArchiveInputStream.MAGIC.getValue());
        ArchiveInputStream result = factory.createArchiveInputStream("dump", input);
        assertNotNull(result);
        assertInstanceOf(DumpArchiveInputStream.class, result);
    }

    @Test
    public void testCreateArchiveOutputStreamSignatureMatchAr() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream(1024);
        ArchiveOutputStream result = factory.createArchiveOutputStream("ar", output);
        assertNotNull(result);
        assertInstanceOf(ArArchiveOutputStream.class, result);
    }

    @Test
    public void testCreateArchiveOutputStreamSignatureMatchZip() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream(1024);
        ArchiveOutputStream result = factory.createArchiveOutputStream("zip", output);
        assertNotNull(result);
        assertInstanceOf(ZipArchiveOutputStream.class, result);
    }

    @Test
    public void testCreateArchiveOutputStreamSignatureMatchTar() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream(1024);
        ArchiveOutputStream result = factory.createArchiveOutputStream("tar", output);
        assertNotNull(result);
        assertInstanceOf(TarArchiveOutputStream.class, result);
    }

    @Test
    public void testCreateArchiveOutputStreamSignatureMatchJar() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream(1024);
        ArchiveOutputStream result = factory.createArchiveOutputStream("jar", output);
        assertNotNull(result);
        assertInstanceOf(JarArchiveOutputStream.class, result);
    }

    @Test
    public void testCreateArchiveOutputStreamSignatureMatchCpio() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream(1024);
        ArchiveOutputStream result = factory.createArchiveOutputStream("cpio", output);
        assertNotNull(result);
        assertInstanceOf(CpioArchiveOutputStream.class, result);
    }

    @Test
    public void testCreateArchiveOutputStreamSignatureMatchDump() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream(1024);
        ArchiveOutputStream result = factory.createArchiveOutputStream("dump", output);
        assertNotNull(result);
        assertInstanceOf(DumpArchiveOutputStream.class, result);
    }
}