package org.apache.commons.compress.archivers.test;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.apache.commons.compress.archivers.ArchiveStreamFactory.AR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.ARJ;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.CPIO;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.DUMP;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.JAR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.SEVEN_Z;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.TAR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.ZIP;

public class ArchiveStreamFactoryTest {

    private static ArchiveStreamFactory factory;

    @BeforeClass
    public static void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream_AR() throws IOException, ArchiveException {
        InputStream is = new FileInputStream("src/test/resources/test-data/archive/ar/test.tar.gz");
        ArchiveInputStream in = factory.createArchiveInputStream(AR, is);
        Assert.assertNotNull(in);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStream_AR_InvalidSignature() throws IOException, ArchiveException {
        InputStream is = new ByteArrayInputStream(new byte[10]);
        try {
            factory.createArchiveInputStream(AR, is);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("Could not find ArchiveStreamFactory for the given signature.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_ARJ() throws IOException, ArchiveException {
        InputStream is = new FileInputStream("src/test/resources/test-data/archive/arj/test.arj");
        ArchiveInputStream in = factory.createArchiveInputStream(ARJ, is);
        Assert.assertNotNull(in);
        in.close();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateArchiveOutputStream_ARJ() throws IOException {
        OutputStream out = new FileOutputStream("src/test/resources/test-output/test.arj");
        ArchiveOutputStream os = factory.createArchiveOutputStream(ARJ, out);
        os.putNextEntry(new ArjArchiveEntry("test.txt"));
        os.close();
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() throws IOException, ArchiveException {
        InputStream is = new FileInputStream("src/test/resources/test-data/archive/cpio/test.cpio.gz");
        ArchiveInputStream in = factory.createArchiveInputStream(CPIO, is);
        Assert.assertNotNull(in);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStream_Dump() throws IOException, ArchiveException {
        InputStream is = new FileInputStream("src/test/resources/test-data/archive/dump/test.dump");
        ArchiveInputStream in = factory.createArchiveInputStream(DUMP, is);
        Assert.assertNotNull(in);
        in.close();
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveInputStream_SevenZ() throws IOException {
        InputStream is = new FileInputStream("src/test/resources/test-data/archive/7z/test.7z");
        factory.createArchiveInputStream(SEVEN_Z, is);
    }

    @Test
    public void testCreateArchiveInputStream_Tar() throws IOException, ArchiveException {
        InputStream is = new FileInputStream("src/test/resources/test-data/archive/tar/test.tar.gz");
        ArchiveInputStream in = factory.createArchiveInputStream(TAR, is);
        Assert.assertNotNull(in);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStream_TarWithBigBuffer() throws IOException, ArchiveException {
        InputStream is = new FileInputStream("src/test/resources/test-data/archive/tar-bigbuffer/test.tar.gz");
        ArchiveInputStream in = factory.createArchiveInputStream(TAR, is);
        Assert.assertNotNull(in);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStream_Zip() throws IOException, ArchiveException {
        InputStream is = new FileInputStream("src/test/resources/test-data/archive/zip/test.zip");
        ArchiveInputStream in = factory.createArchiveInputStream(ZIP, is);
        Assert.assertNotNull(in);
        in.close();
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws IOException, ArchiveException {
        OutputStream out = new FileOutputStream("src/test/resources/test-output/test.tar");
        ArchiveOutputStream os = factory.createArchiveOutputStream(AR, out);
        os.putNextEntry(new ArArchiveEntry("test.txt"));
        os.write(new byte[10]);
        os.closeArchiveEntry();
        os.close();
        ArchiveInputStream in = factory.createArchiveInputStream(AR, new FileInputStream("src/test/resources/test-output/test.tar"));
        ArchiveEntry entry = in.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(10L, entry.getSize());
        in.close();
    }

    @Test
    public void testCreateArchiveOutputStream_AR_WithCompressor() throws IOException, ArchiveException {
        OutputStream out = new FileOutputStream("src/test/resources/test-output/test.tar.gz");
        ArchiveOutputStream os = factory.createArchiveOutputStream(AR, out, new GzipCompressorOutputStream(out));
        os.putNextEntry(new ArArchiveEntry("test.txt"));
        os.write(new byte[10]);
        os.closeArchiveEntry();
        os.finish();
        os.close();
        InputStream is = new FileInputStream("src/test/resources/test-output/test.tar.gz");
        ArchiveInputStream in = factory.createArchiveInputStream(AR, is);
        ArchiveEntry entry = in.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(10L, entry.getSize());
        in.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Jar() throws IOException, ArchiveException {
        OutputStream out = new FileOutputStream("src/test/resources/test-output/test.jar");
        ArchiveOutputStream os = factory.createArchiveOutputStream(JAR, out);
        os.putNextEntry(new JarArchiveEntry("test.txt"));
        os.write(new byte[10]);
        os.closeArchiveEntry();
        os.finish();
        os.close();
        InputStream is = new FileInputStream("src/test/resources/test-output/test.jar");
        JarArchiveInputStream in = (JarArchiveInputStream) factory.createArchiveInputStream(JAR, is);
        JarEntry entry = in.getNextJarEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(10L, entry.getSize());
        in.close();
    }

}