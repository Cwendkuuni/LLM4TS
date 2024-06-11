import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.SevenZFile;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    private static final int BUFFER_SIZE = 1024;
    private OutputStream output;
    private InputStream input;

    @Before
    public void setUp() throws IOException {
        output = new ByteArrayOutputStream(BUFFER_SIZE);
        input = new ByteArrayInputStream(new byte[BUFFER_SIZE]);
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);
    }

    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        // Test AR archive
        ArchiveInputStream arInputStream = new ArchiveStreamFactory().createArchiveInputStream(AR, input);
        Assert.assertEquals(ArArchiveInputStream.class, arInputStream.getClass());
        IOUtils.closeQuietly(arInputStream);

        // Test ARJ archive
        ArjArchiveInputStream arjInputStream = new ArchiveStreamFactory().createArchiveInputStream(ARJ, input);
        Assert.assertEquals(ArjArchiveInputStream.class, arjInputStream.getClass());
        IOUtils.closeQuietly(arjInputStream);

        // Test CPIO archive
        CpioArchiveInputStream cpioInputStream = new ArchiveStreamFactory().createArchiveInputStream(CPIO, input);
        Assert.assertEquals(CpioArchiveInputStream.class, cpioInputStream.getClass());
        IOUtils.closeQuietly(cpioInputStream);

        // Test Dump archive
        DumpArchiveInputStream dumpInputStream = new ArchiveStreamFactory().createArchiveInputStream(DUMP, input);
        Assert.assertEquals(DumpArchiveInputStream.class, dumpInputStream.getClass());
        IOUtils.closeQuietly(dumpInputStream);

        // Test JAR archive
        JarArchiveInputStream jarInputStream = new ArchiveStreamFactory().createArchiveInputStream(JAR, input);
        Assert.assertEquals(JarArchiveInputStream.class, jarInputStream.getClass());
        IOUtils.closeQuietly(jarInputStream);

        // Test TAR archive
        TarArchiveInputStream tarInputStream = new ArchiveStreamFactory().createArchiveInputStream(TAR, input);
        Assert.assertEquals(TarArchiveInputStream.class, tarInputStream.getClass());
        IOUtils.closeQuietly(tarInputStream);

        // Test ZIP archive
        ZipArchiveInputStream zipInputStream = new ArchiveStreamFactory().createArchiveInputStream(ZIP, input);
        Assert.assertEquals(ZipArchiveInputStream.class, zipInputStream.getClass());
        IOUtils.closeQuietly(zipInputStream);

        // Test SEVEN_Z archive
        try {
            new ArchiveStreamFactory().createArchiveInputStream(SEVEN_Z, input);
            Assert.fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        // Test AR archive
        ArchiveOutputStream arOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(AR, output);
        Assert.assertEquals(ArArchiveOutputStream.class, arOutputStream.getClass());
        arOutputStream.close();

        // Test ARJ archive
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(ARJ, output);
            Assert.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }

        // Test CPIO archive
        CpioArchiveOutputStream cpioOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(CPIO, output);
        Assert.assertEquals(CpioArchiveOutputStream.class, cpioOutputStream.getClass());
        cpioOutputStream.close();

        // Test Dump archive
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(DUMP, output);
            Assert.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }

        // Test JAR archive
        JarArchiveOutputStream jarOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(JAR, output);
        Assert.assertEquals(JarArchiveOutputStream.class, jarOutputStream.getClass());
        jarOutputStream.finish();
        jarOutputStream.close();

        // Test TAR archive
        TarArchiveOutputStream tarOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(TAR, output);
        Assert.assertEquals(TarArchiveOutputStream.class, tarOutputStream.getClass());
        tarOutputStream.finish();
        tarOutputStream.close();

        // Test ZIP archive
        ZipArchiveOutputStream zipOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ZIP, output);
        Assert.assertEquals(ZipArchiveOutputStream.class, zipOutputStream.getClass());
        zipOutputStream.finish();
        zipOutputStream.close();

        // Test SEVEN_Z archive
        SevenZFile sevenZFile = new SevenZFile(output.toByteArray());
        // SevenZFile doesn't support writing, so we can't test creating an output stream from it
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws IOException, ArchiveException {
        // Test AR archive
        ArchiveInputStream arInputStream = new ArchiveStreamFactory().createArchiveInputStream(AR, input);
        Assert.assertEquals(ArArchiveInputStream.class, arInputStream.getClass());
        IOUtils.closeQuietly(arInputStream);

        // Test ARJ archive
        ArjArchiveInputStream arjInputStream = new ArchiveStreamFactory().createArchiveInputStream(ARJ, input);
        Assert.assertEquals(ArjArchiveInputStream.class, arjInputStream.getClass());
        IOUtils.closeQuietly(arjInputStream);

        // Test CPIO archive
        CpioArchiveInputStream cpioInputStream = new ArchiveStreamFactory().createArchiveInputStream(CPIO, input);
        Assert.assertEquals(CpioArchiveInputStream.class, cpioInputStream.getClass());
        IOUtils.closeQuietly(cpioInputStream);

        // Test Dump archive
        DumpArchiveInputStream dumpInputStream = new ArchiveStreamFactory().createArchiveInputStream(DUMP, input);
        Assert.assertEquals(DumpArchiveInputStream.class, dumpInputStream.getClass());
        IOUtils.closeQuietly(dumpInputStream);

        // Test JAR archive
        JarArchiveInputStream jarInputStream = new ArchiveStreamFactory().createArchiveInputStream(JAR, input);
        Assert.assertEquals(JarArchiveInputStream.class, jarInputStream.getClass());
        IOUtils.closeQuietly(jarInputStream);

        // Test TAR archive
        TarArchiveInputStream tarInputStream = new ArchiveStreamFactory().createArchiveInputStream(TAR, input);
        Assert.assertEquals(TarArchiveInputStream.class, tarInputStream.getClass());
        IOUtils.closeQuietly(tarInputStream);

        // Test ZIP archive
        ZipArchiveInputStream zipInputStream = new ArchiveStreamFactory().createArchiveInputStream(ZIP, input);
        Assert.assertEquals(ZipArchiveInputStream.class, zipInputStream.getClass());
        IOUtils.closeQuietly(zipInputStream);

        // Test SEVEN_Z archive
        try {
            new ArchiveStreamFactory().createArchiveInputStream(SEVEN_Z, input);
            Assert.fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // Expected
        }
    }
}