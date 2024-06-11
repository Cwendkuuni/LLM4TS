package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.*;
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
import org.apache.commons.compress.archivers.test.TestArchiveFile;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test cases for ArchiveStreamFactory class
 */
public class ArchiveStreamFactoryTest {

    // Test createArchiveInputStream() with known archivers

    @Test
    public void testCreateArchiveInputStreamAr() throws IOException {
        InputStream inputStream = new TestArchiveFile("test-ar.tar.gz", "test").getInputStream();
        ArchiveInputStream in = ArchiveStreamFactory.getInstance().createArchiveInputStream("ar", inputStream);
        assertInstanceOf(ArArchiveInputStream.class, in);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStreamJar() throws IOException {
        InputStream inputStream = new TestArchiveFile("test-jar.jar", "test").getInputStream();
        ArchiveInputStream in = ArchiveStreamFactory.getInstance().createArchiveInputStream("jar", inputStream);
        assertInstanceOf(JarArchiveInputStream.class, in);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStreamTar() throws IOException {
        InputStream inputStream = new TestArchiveFile("test-tar.gz", "test").getInputStream();
        ArchiveInputStream in = ArchiveStreamFactory.getInstance().createArchiveInputStream("tar", inputStream);
        assertInstanceOf(TarArchiveInputStream.class, in);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStreamZip() throws IOException {
        InputStream inputStream = new TestArchiveFile("test-zip.zip", "test").getInputStream();
        ArchiveInputStream in = ArchiveStreamFactory.getInstance().createArchiveInputStream("zip", inputStream);
        assertInstanceOf(ZipArchiveInputStream.class, in);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStreamCpio() throws IOException {
        InputStream inputStream = new TestArchiveFile("test-cpio.cpio.gz", "test").getInputStream();
        ArchiveInputStream in = ArchiveStreamFactory.getInstance().createArchiveInputStream("cpio", inputStream);
        assertInstanceOf(CpioArchiveInputStream.class, in);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStreamDump() throws IOException {
        InputStream inputStream = new TestArchiveFile("test-dump.dump", "test").getInputStream();
        ArchiveInputStream in = ArchiveStreamFactory.getInstance().createArchiveInputStream("dump", inputStream);
        assertInstanceOf(DumpArchiveInputStream.class, in);
        in.close();
    }

    // Test createArchiveInputStream() with unknown archivers

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknown() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", inputStream);
    }

    // Test createArchiveOutputStream() with known archivers

    @Test
    public void testCreateArchiveOutputStreamAr() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream out = ArchiveStreamFactory.getInstance().createArchiveOutputStream("ar", outputStream);
        out.close();
        assertInstanceOf(ArArchiveOutputStream.class, out);
    }

    @Test
    public void testCreateArchiveOutputStreamJar() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream out = ArchiveStreamFactory.getInstance().createArchiveOutputStream("jar", outputStream);
        out.close();
        assertInstanceOf(JarArchiveOutputStream.class, out);
    }

    @Test
    public void testCreateArchiveOutputStreamTar() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream out = ArchiveStreamFactory.getInstance().createArchiveOutputStream("tar", outputStream);
        out.close();
        assertInstanceOf(TarArchiveOutputStream.class, out);
    }

    @Test
    public void testCreateArchiveOutputStreamZip() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream out = ArchiveStreamFactory.getInstance().createArchiveOutputStream("zip", outputStream);
        out.close();
        assertInstanceOf(ZipArchiveOutputStream.class, out);
    }

    @Test
    public void testCreateArchiveOutputStreamCpio() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream out = ArchiveStreamFactory.getInstance().createArchiveOutputStream("cpio", outputStream);
        out.close();
        assertInstanceOf(CpioArchiveOutputStream.class, out);
    }

    @Test
    public void testCreateArchiveOutputStreamDump() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream out = ArchiveStreamFactory.getInstance().createArchiveOutputStream("dump", outputStream);
        out.close();
        assertInstanceOf(DumpArchiveOutputStream.class, out);
    }

    // Test createArchiveOutputStream() with unknown archivers

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknown() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory.getInstance().createArchiveOutputStream("unknown", outputStream);
    }

    // Test createArchiveInputStream() with auto-detection

    @Test
    public void testCreateArchiveInputStreamAutoDetectionAr() throws IOException {
        InputStream inputStream = new TestArchiveFile("test-ar.tar.gz", "test").getInputStream();
        ArchiveInputStream in = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
        assertInstanceOf(ArArchiveInputStream.class, in);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectionJar() throws IOException {
        InputStream inputStream = new TestArchiveFile("test-jar.jar", "test").getInputStream();
        ArchiveInputStream in = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
        assertInstanceOf(JarArchiveInputStream.class, in);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectionTar() throws IOException {
        InputStream inputStream = new TestArchiveFile("test-tar.gz", "test").getInputStream();
        ArchiveInputStream in = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
        assertInstanceOf(TarArchiveInputStream.class, in);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectionZip() throws IOException {
        InputStream inputStream = new TestArchiveFile("test-zip.zip", "test").getInputStream();
        ArchiveInputStream in = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
        assertInstanceOf(ZipArchiveInputStream.class, in);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectionCpio() throws IOException {
        InputStream inputStream = new TestArchiveFile("test-cpio.cpio.gz", "test").getInputStream();
        ArchiveInputStream in = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
        assertInstanceOf(CpioArchiveInputStream.class, in);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectionDump() throws IOException {
        InputStream inputStream = new TestArchiveFile("test-dump.dump", "test").getInputStream();
        ArchiveInputStream in = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
        assertInstanceOf(DumpArchiveInputStream.class, in);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectionUnknown() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream in = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
        assertNull(in);
    }
}