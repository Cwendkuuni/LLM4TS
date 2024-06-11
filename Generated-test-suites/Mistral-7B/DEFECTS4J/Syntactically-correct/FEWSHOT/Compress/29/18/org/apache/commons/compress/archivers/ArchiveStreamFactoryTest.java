package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateInputStream_AR() throws IOException {
        final ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(ArArchiveInputStream.class, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateInputStream_AR_InvalidSignature() throws IOException {
        final byte[] bytes = new byte[12];
        final InputStream inputStream = new ByteArrayInputStream(bytes);
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateInputStream_AR_InvalidFormat() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[12]);
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ARJ, inputStream);
    }

    @Test
    public void testCreateInputStream_ARJ() throws IOException {
        final ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ARJ, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(ArjArchiveInputStream.class, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateInputStream_ARJ_UnsupportedFormat() throws IOException {
        final ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
        assertNull(inputStream);
    }

    @Test
    public void testCreateInputStream_Cpio() throws IOException {
        final ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(CpioArchiveInputStream.class, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateInputStream_Cpio_UnsupportedFormat() throws IOException {
        final ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
        assertNull(inputStream);
    }

    @Test
    public void testCreateInputStream_Dump() throws IOException {
        final ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(DumpArchiveInputStream.class, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateInputStream_Dump_UnsupportedFormat() throws IOException {
        final ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
        assertNull(inputStream);
    }

    @Test
    public void testCreateInputStream_Jar() throws IOException {
        final ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(JarArchiveInputStream.class, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateInputStream_Jar_UnsupportedFormat() throws IOException {
        final ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
        assertNull(inputStream);
    }

    @Test
    public void testCreateInputStream_Tar() throws IOException {
        final ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(TarArchiveInputStream.class, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateInputStream_Tar_InvalidSignature() throws IOException {
        final byte[] bytes = new byte[512];
        final InputStream inputStream = new ByteArrayInputStream(bytes);
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateInputStream_Tar_UnsupportedFormat() throws IOException {
        final ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
        assertNull(inputStream);
    }

    @Test
    public void testCreateInputStream_Zip() throws IOException {
        final ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(ZipArchiveInputStream.class, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateInputStream_Zip_UnsupportedFormat() throws IOException {
        final ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
        assertNull(inputStream);
    }

    @Test
    public void testCreateOutputStream_AR() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertSame(outputStream, archiveOutputStream.getOutputStream());
        archiveOutputStream.finish();
    }

    @Test
    public void testCreateOutputStream_ARJ() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ARJ, outputStream);
        assertSame(outputStream, archiveOutputStream.getOutputStream());
        archiveOutputStream.finish();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateOutputStream_ARJ_UnsupportedFormat() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
    }

    @Test
    public void testCreateOutputStream_Cpio() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        assertSame(outputStream, archiveOutputStream.getOutputStream());
        archiveOutputStream.finish();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateOutputStream_Cpio_UnsupportedFormat() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
    }

    @Test
    public void testCreateOutputStream_Dump() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
        assertSame(outputStream, archiveOutputStream.getOutputStream());
        archiveOutputStream.finish();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateOutputStream_Dump_UnsupportedFormat() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
    }

    @Test
    public void testCreateOutputStream_Jar() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        assertSame(outputStream, archiveOutputStream.getOutputStream());
        archiveOutputStream.finish();
    }

    @Test
    public void testCreateOutputStream_Jar_SetEncoding() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveStreamFactory factory = new ArchiveStreamFactory("UTF-8");
        final ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        archiveOutputStream.setEncoding("UTF-16");
        assertFalse(archiveOutputStream.getEncoding().equals("UTF-8"));
        archiveOutputStream.finish();
    }

    @Test
    public void testCreateOutputStream_Jar_UnsupportedFormat() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
    }

    @Test
    public void testCreateOutputStream_SevenZ() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, outputStream);
        assertSame(outputStream, archiveOutputStream.getOutputStream());
        archiveOutputStream.finish();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateOutputStream_SevenZ_UnsupportedFormat() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
    }

    @Test
    public void testCreateOutputStream_Tar() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        assertSame(outputStream, archiveOutputStream.getOutputStream());
        archiveOutputStream.finish();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateOutputStream_Tar_UnsupportedFormat() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
    }

    @Test
    public void testCreateOutputStream_Zip() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertSame(outputStream, archiveOutputStream.getOutputStream());
        archiveOutputStream.finish();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateOutputStream_Zip_UnsupportedFormat() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
    }
}