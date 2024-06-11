import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
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
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test for ArchiveStreamFactory
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test createArchiveInputStream() with known archive types
     */
    @Test
    public void testCreateArchiveInputStreamWithKnownArchives() throws Exception {
        // Test AR
        InputStream arInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        Assert.assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        archiveInputStream.close();

        // Test ZIP
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        Assert.assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        archiveInputStream.close();

        // Test TAR
        InputStream tarInputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        Assert.assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        archiveInputStream.close();

        // Test JAR
        InputStream jarInputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        Assert.assertTrue(archiveInputStream instanceof JarArchiveInputStream);
        archiveInputStream.close();

        // Test CPIO
        InputStream cpioInputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        Assert.assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
        archiveInputStream.close();

        // Test DUMP
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        Assert.assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
        archiveInputStream.close();
    }

    /**
     * Test createArchiveInputStream() with invalid archive type
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithInvalidArchiveType() throws Exception {
        InputStream invalidArchiveInputStream = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream("invalidArchiveType", invalidArchiveInputStream);
    }

    /**
     * Test createArchiveOutputStream() with known archive types
     */
    @Test
    public void testCreateArchiveOutputStreamWithKnownArchives() throws Exception {
        // Test AR
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        Assert.assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);

        // Test ZIP
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        Assert.assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);

        // Test TAR
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        Assert.assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);

        // Test JAR
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        Assert.assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);

        // Test CPIO
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        Assert.assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);

        // Test DUMP
        OutputStream dumpOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        Assert.assertTrue(archiveOutputStream instanceof DumpArchiveOutputStream);
    }

    /**
     * Test createArchiveOutputStream() with invalid archive type
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithInvalidArchiveType() throws Exception {
        OutputStream invalidArchiveOutputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("invalidArchiveType", invalidArchiveOutputStream);
    }

    /**
     * Test createArchiveInputStream() with unknown archive format
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiveFormat() throws Exception {
        InputStream unknownArchiveInputStream = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream("unknownArchiveFormat", unknownArchiveInputStream);
    }

    /**
     * Test createArchiveOutputStream() with null stream
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    /**
     * Test createArchiveInputStream() with null archiverName
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream(null, inputStream);
    }

    /**
     * Test createArchiveInputStream() with empty archiverName
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithEmptyArchiverName() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream("", inputStream);
    }

    /**
     * Test createArchiveOutputStream() with empty output stream
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithEmptyOutputStream() {
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream());
    }

    /**
     * Test createArchiveOutputStream() with null archiverName
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(null, outputStream);
    }
}