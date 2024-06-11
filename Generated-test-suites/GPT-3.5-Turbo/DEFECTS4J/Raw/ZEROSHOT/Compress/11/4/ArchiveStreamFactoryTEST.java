import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test AR archive format
        InputStream arInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream arIn = factory.createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        Assert.assertTrue(arIn instanceof ArArchiveInputStream);
        arInputStream.close();

        // Test ZIP archive format
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipIn = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        Assert.assertTrue(zipIn instanceof ZipArchiveInputStream);
        zipInputStream.close();

        // Test TAR archive format
        InputStream tarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream tarIn = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        Assert.assertTrue(tarIn instanceof TarArchiveInputStream);
        tarInputStream.close();

        // Test JAR archive format
        InputStream jarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream jarIn = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        Assert.assertTrue(jarIn instanceof JarArchiveInputStream);
        jarInputStream.close();

        // Test CPIO archive format
        InputStream cpioInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream cpioIn = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        Assert.assertTrue(cpioIn instanceof CpioArchiveInputStream);
        cpioInputStream.close();

        // Test DUMP archive format
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream dumpIn = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        Assert.assertTrue(dumpIn instanceof DumpArchiveInputStream);
        dumpInputStream.close();

        // Test unknown archive format
        try {
            InputStream unknownInputStream = new ByteArrayInputStream(new byte[0]);
            factory.createArchiveInputStream("unknown", unknownInputStream);
            Assert.fail("ArchiveException expected");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test AR archive format
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arOut = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        Assert.assertTrue(arOut instanceof ArArchiveOutputStream);
        arOutputStream.close();

        // Test ZIP archive format
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipOut = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        Assert.assertTrue(zipOut instanceof ZipArchiveOutputStream);
        zipOutputStream.close();

        // Test TAR archive format
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarOut = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        Assert.assertTrue(tarOut instanceof TarArchiveOutputStream);
        tarOutputStream.close();

        // Test JAR archive format
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarOut = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        Assert.assertTrue(jarOut instanceof JarArchiveOutputStream);
        jarOutputStream.close();

        // Test CPIO archive format
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioOut = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
        Assert.assertTrue(cpioOut instanceof CpioArchiveOutputStream);
        cpioOutputStream.close();

        // Test unknown archive format
        try {
            OutputStream unknownOutputStream = new ByteArrayOutputStream();
            factory.createArchiveOutputStream("unknown", unknownOutputStream);
            Assert.fail("ArchiveException expected");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveInputStream_autoDetect() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test ZIP archive format
        byte[] zipFileBytes = new byte[] { 80, 75, 3, 4, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        InputStream zipInputStream = new ByteArrayInputStream(zipFileBytes);
        ArchiveInputStream zipIn = factory.createArchiveInputStream(zipInputStream);
        Assert.assertTrue(zipIn instanceof ZipArchiveInputStream);
        zipInputStream.close();

        // Test JAR archive format
        byte[] jarFileBytes = new byte[] { 80, 75, 3, 4, 10, 0, 20, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        InputStream jarInputStream = new ByteArrayInputStream(jarFileBytes);
        ArchiveInputStream jarIn = factory.createArchiveInputStream(jarInputStream);
        Assert.assertTrue(jarIn instanceof JarArchiveInputStream);
        jarInputStream.close();

        // Test AR archive format
        byte[] arFileBytes = new byte[] { 46, 33, 60 };
        InputStream arInputStream = new ByteArrayInputStream(arFileBytes);
        ArchiveInputStream arIn = factory.createArchiveInputStream(arInputStream);
        Assert.assertTrue(arIn instanceof ArArchiveInputStream);
        arInputStream.close();

        // Test CPIO archive format
        byte[] cpioFileBytes = new byte[] { 99, 112, 105, 111, 8, 0, 0, 0, 0, 0, 0, 0 };
        InputStream cpioInputStream = new ByteArrayInputStream(cpioFileBytes);
        ArchiveInputStream cpioIn = factory.createArchiveInputStream(cpioInputStream);
        Assert.assertTrue(cpioIn instanceof CpioArchiveInputStream);
        cpioInputStream.close();

        // Test DUMP archive format
        byte[] dumpFileBytes = new byte[] { 0x1F, (byte)0x9D, 0x90, 0x1A, 0x4D, 0, 0, 0 };
        InputStream dumpInputStream = new ByteArrayInputStream(dumpFileBytes);
        ArchiveInputStream dumpIn = factory.createArchiveInputStream(dumpInputStream);
        Assert.assertTrue(dumpIn instanceof DumpArchiveInputStream);
        dumpInputStream.close();

        // Test TAR archive format
        byte[] tarFileBytes = new byte[] { 0x1F, 0x9D };
        InputStream tarInputStream = new ByteArrayInputStream(tarFileBytes);
        ArchiveInputStream tarIn = factory.createArchiveInputStream(tarInputStream);
        Assert.assertTrue(tarIn instanceof TarArchiveInputStream);
        tarInputStream.close();

        // Test unknown archive format
        try {
            InputStream unknownInputStream = new ByteArrayInputStream(new byte[0]);
            factory.createArchiveInputStream(unknownInputStream);
            Assert.fail("ArchiveException expected");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }

}