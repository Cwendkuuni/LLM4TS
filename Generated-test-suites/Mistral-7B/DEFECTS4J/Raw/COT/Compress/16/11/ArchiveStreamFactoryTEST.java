import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.archiveentry.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.apache.commons.compress.archivers.ArchiveFormat.AR;
import static org.apache.commons.compress.archivers.ArchiveFormat.CPIO;
import static org.apache.commons.compress.archivers.ArchiveFormat.DUMP;
import static org.apache.commons.compress.archivers.ArchiveFormat.JAR;
import static org.apache.commons.compress.archivers.ArchiveFormat.TAR;
import static org.apache.commons.compress.archivers.ArchiveFormat.ZIP;

public class ArchiveStreamFactoryTest {

    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setup() throws Exception {
        inputStream = new ByteArrayInputStream(new byte[0]);
        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws Exception {
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }

    @Test
    public void testCreateArchiveInputStream_AR() throws Exception {
        InputStream in = new ArchiveStreamFactory().createArchiveInputStream(AR, inputStream);
        ArchiveInputStream expected = new ArArchiveInputStream(inputStream);
        Assert.assertEquals(expected.getClass(), in.getClass());
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() throws Exception {
        InputStream in = new ArchiveStreamFactory().createArchiveInputStream(CPIO, inputStream);
        ArchiveInputStream expected = new CpioArchiveInputStream(inputStream);
        Assert.assertEquals(expected.getClass(), in.getClass());
    }

    @Test
    public void testCreateArchiveInputStream_DUMP() throws Exception {
        InputStream in = new ArchiveStreamFactory().createArchiveInputStream(DUMP, inputStream);
        ArchiveInputStream expected = new DumpArchiveInputStream(inputStream);
        Assert.assertEquals(expected.getClass(), in.getClass());
    }

    @Test
    public void testCreateArchiveInputStream_JAR() throws Exception {
        InputStream in = new ArchiveStreamFactory().createArchiveInputStream(JAR, inputStream);
        ArchiveInputStream expected = new JarArchiveInputStream(inputStream);
        Assert.assertEquals(expected.getClass(), in.getClass());
    }

    @Test
    public void testCreateArchiveInputStream_TAR() throws Exception {
        InputStream in = new ArchiveStreamFactory().createArchiveInputStream(TAR, inputStream);
        ArchiveInputStream expected = new TarArchiveInputStream(inputStream);
        Assert.assertEquals(expected.getClass(), in.getClass());
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() throws Exception {
        InputStream in = new ArchiveStreamFactory().createArchiveInputStream(ZIP, inputStream);
        ArchiveInputStream expected = new ZipArchiveInputStream(inputStream);
        Assert.assertEquals(expected.getClass(), in.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidArchiverName() throws Exception {
        new ArchiveStreamFactory().createArchiveInputStream("invalid", inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws Exception {
        ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(AR, outputStream);
        ArchiveOutputStream expected = new ArArchiveOutputStream(outputStream);
        Assert.assertEquals(expected.getClass(), out.getClass());
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO() throws Exception {
        ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(CPIO, outputStream);
        ArchiveOutputStream expected = new CpioArchiveOutputStream(outputStream);
        Assert.assertEquals(expected.getClass(), out.getClass());
    }

    @Test
    public void testCreateArchiveOutputStream_DUMP() throws Exception {
        ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(DUMP, outputStream);
        ArchiveOutputStream expected = new DumpArchiveOutputStream(outputStream);
        Assert.assertEquals(expected.getClass(), out.getClass());
    }

    @Test
    public void testCreateArchiveOutputStream_JAR() throws Exception {
        ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(JAR, outputStream);
        ArchiveOutputStream expected = new JarArchiveOutputStream(outputStream);
        Assert.assertEquals(expected.getClass(), out.getClass());
    }

    @Test
    public void testCreateArchiveOutputStream_TAR() throws Exception {
        ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(TAR, outputStream);
        ArchiveOutputStream expected = new TarArchiveOutputStream(outputStream);
        Assert.assertEquals(expected.getClass(), out.getClass());
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP() throws Exception {
        ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ZIP, outputStream);
        ArchiveOutputStream expected = new ZipArchiveOutputStream(outputStream);
        Assert.assertEquals(expected.getClass(), out.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_InvalidArchiverName() throws Exception {
        new ArchiveStreamFactory().createArchiveOutputStream("invalid", outputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetection_Zip() throws Exception {
        byte[] zipSignature = new byte[4];
        inputStream.read(zipSignature, 0, zipSignature.length);
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        Assert.assertTrue(ZipArchiveInputStream.matches(zipSignature, zipSignature.length));
        Assert.assertEquals(ZipArchiveInputStream.class, in.getClass());
    }

    @Test
    public void testCreateArchiveInputStream_Autodetection_Tar() throws Exception {
        byte[] tarHeader = new byte[512];
        inputStream.read(tarHeader, 0, tarHeader.length);
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        TarArchiveInputStream tarIn = new TarArchiveInputStream(new ByteArrayInputStream(tarHeader));
        TarEntry tarEntry = tarIn.getNextEntry();
        Assert.assertNotNull(tarEntry);
        Assert.assertEquals(tarEntry.getClass(), in.getNextEntry().getClass());
        Assert.assertEquals(TarArchiveInputStream.class, in.getClass());
    }

    @Test
    public void testCreateArchiveInputStream_Autodetection_Invalid() throws Exception {
        inputStream.read(new byte[1]); // read a byte to consume the stream
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        Assert.assertNull(in.getNextEntry());
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        OutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(TAR, outputStream);
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(outputStream);
        ArchiveOutputStream expected = tarOut;
        Assert.assertEquals(expected.getClass(), out.getClass());
    }
}