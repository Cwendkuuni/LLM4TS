import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory = new ArchiveStreamFactory();

    @Test
    public void testCreateArchiveInputStream() throws IOException {
        // Test AR
        InputStream arInputStream = createTestArchiveInputStream("AR");
        ArchiveInputStream arResult = factory.createArchiveInputStream("AR", arInputStream);
        Assert.assertNotNull(arResult);
        Assert.assertEquals(ArArchiveInputStream.class, arResult.getClass());
        arResult.close();
        arInputStream.close();

        // Test ARJ
        InputStream arjInputStream = createTestArchiveInputStream("ARJ");
        ArchiveInputStream arjResult = factory.createArchiveInputStream("ARJ", arjInputStream);
        Assert.assertNotNull(arjResult);
        Assert.assertEquals(ArjArchiveInputStream.class, arjResult.getClass());
        arjResult.close();
        arjInputStream.close();

        // Test CPIO
        InputStream cpioInputStream = createTestArchiveInputStream("CPIO");
        ArchiveInputStream cpioResult = factory.createArchiveInputStream("CPIO", cpioInputStream);
        Assert.assertNotNull(cpioResult);
        Assert.assertEquals(CpioArchiveInputStream.class, cpioResult.getClass());
        cpioResult.close();
        cpioInputStream.close();

        // Test Dump
        InputStream dumpInputStream = createTestArchiveInputStream("DUMP");
        ArchiveInputStream dumpResult = factory.createArchiveInputStream("DUMP", dumpInputStream);
        Assert.assertNotNull(dumpResult);
        Assert.assertEquals(DumpArchiveInputStream.class, dumpResult.getClass());
        dumpResult.close();
        dumpInputStream.close();

        // Test JAR
        InputStream jarInputStream = createTestArchiveInputStream("JAR");
        ArchiveInputStream jarResult = factory.createArchiveInputStream("JAR", jarInputStream);
        Assert.assertNotNull(jarResult);
        Assert.assertEquals(JarArchiveInputStream.class, jarResult.getClass());
        jarResult.close();
        jarInputStream.close();

        // Test TAR
        InputStream tarInputStream = createTestArchiveInputStream("TAR");
        ArchiveInputStream tarResult = factory.createArchiveInputStream("TAR", tarInputStream);
        Assert.assertNotNull(tarResult);
        Assert.assertEquals(TarArchiveInputStream.class, tarResult.getClass());
        tarResult.close();
        tarInputStream.close();

        // Test ZIP
        InputStream zipInputStream = createTestArchiveInputStream("ZIP");
        ArchiveInputStream zipResult = factory.createArchiveInputStream("ZIP", zipInputStream);
        Assert.assertNotNull(zipResult);
        Assert.assertEquals(ZipArchiveInputStream.class, zipResult.getClass());
        zipResult.close();
        zipInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        // Test AR
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arOutput = factory.createArchiveOutputStream("AR", outputStream);
        arOutput.putArchiveEntry(new ArArchiveEntry("test.txt"));
        arOutput.write(new byte[]{"T".encodeBase64()});
        arOutput.finish();
        arOutput.close();
        ArchiveInputStream arInput = factory.createArchiveInputStream("AR", new ByteArrayInputStream(outputStream.toByteArray()));
        Assert.assertNotNull(arInput.getNextEntry());
        arInput.close();

        // Test ARJ
        outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arjOutput = factory.createArchiveOutputStream("ARJ", outputStream);
        arjOutput.putArchiveEntry(new ArjArchiveEntry("test.txt"));
        arjOutput.write(new byte[]{"T".encodeBase64()});
        arjOutput.finish();
        arjOutput.close();
        ArchiveInputStream arjInput = factory.createArchiveInputStream("ARJ", new ByteArrayInputStream(outputStream.toByteArray()));
        Assert.assertNotNull(arjInput.getNextEntry());
        arjInput.close();

        // Test CPIO
        outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioOutput = factory.createArchiveOutputStream("CPIO", outputStream);
        cpioOutput.putNextHeader(new CpioHeader("test.txt", null, null));
        cpioOutput.write(new byte[]{"T".encodeBase64()});
        cpioOutput.finish();
        cpioOutput.close();
        ArchiveInputStream cpioInput = factory.createArchiveInputStream("CPIO", new ByteArrayInputStream(outputStream.toByteArray()));
        Assert.assertNotNull(cpioInput.getNextEntry());
        cpioInput.close();

        // Test Dump
        outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream dumpOutput = factory.createArchiveOutputStream("DUMP", outputStream);
        dumpOutput.putNextHeader(new DumpHeader(null, null, null, null, null, null, null, null, null, null));
        dumpOutput.write(new byte[]{"T".encodeBase64()});
        dumpOutput.finish();
        dumpOutput.close();
        ArchiveInputStream dumpInput = factory.createArchiveInputStream("DUMP", new ByteArrayInputStream(outputStream.toByteArray()));
        Assert.assertNotNull(dumpInput.getNextEntry());
        dumpInput.close();

        // Test JAR
        outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarOutput = factory.createArchiveOutputStream("JAR", outputStream);
        JarArchiveEntry jarEntry = new JarArchiveEntry("test.txt");
        jarOutput.putNextEntry(jarEntry);
        jarOutput.write(new byte[]{"T".encodeBase64()});
        jarOutput.finish();
        jarOutput.close();
        ArchiveInputStream jarInput = factory.createArchiveInputStream("JAR", new ByteArrayInputStream(outputStream.toByteArray()));
        Assert.assertNotNull(jarInput.getNextEntry());
        Assert.assertEquals(jarEntry.getName(), jarInput.getNextEntry().getName());
        jarInput.close();

        // Test TAR
        outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarOutput = factory.createArchiveOutputStream("TAR", outputStream);
        TarArchiveEntry tarEntry = new TarArchiveEntry("test.txt");
        tarOutput.putNextEntry(tarEntry);
        tarOutput.write(new byte[]{"T".encodeBase64()});
        tarOutput.finish();
        tarOutput.close();
        ArchiveInputStream tarInput = factory.createArchiveInputStream("TAR", new ByteArrayInputStream(outputStream.toByteArray()));
        Assert.assertNotNull(tarInput.getNextEntry());
        Assert.assertEquals(tarEntry.getName(), tarInput.getNextEntry().getName());
        tarInput.close();

        // Test ZIP
        outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipOutput = factory.createArchiveOutputStream("ZIP", outputStream);
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        zipOutput.putNextEntry(zipEntry);
        zipOutput.write(new byte[]{"T".encodeBase64()});
        zipOutput.finish();
        zipOutput.close();
        ArchiveInputStream zipInput = factory.createArchiveInputStream("ZIP", new ByteArrayInputStream(outputStream.toByteArray()));
        Assert.assertNotNull(zipInput.getNextEntry());
        Assert.assertEquals(zipEntry.getName(), zipInput.getNextEntry().getName());
        zipInput.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws IOException {
        // Test AR
        InputStream arInputStream = createTestArchiveInputStream("AR");
        ArchiveInputStream arResult = factory.createArchiveInputStream("AR", arInputStream);
        Assert.assertNotNull(arResult);
        arResult.close();
        arInputStream.close();

        // Test ARJ
        InputStream arjInputStream = createTestArchiveInputStream("ARJ");
        ArchiveInputStream arjResult = factory.createArchiveInputStream("ARJ", arjInputStream);
        Assert.assertNotNull(arjResult);
        arjResult.close();
        arjInputStream.close();

        // Test CPIO
        InputStream cpioInputStream = createTestArchiveInputStream("CPIO");
        ArchiveInputStream cpioResult = factory.createArchiveInputStream("CPIO", cpioInputStream);
        Assert.assertNotNull(cpioResult);
        cpioResult.close();
        cpioInputStream.close();

        // Test Dump
        InputStream dumpInputStream = createTestArchiveInputStream("DUMP");
        ArchiveInputStream dumpResult = factory.createArchiveInputStream("DUMP", dumpInputStream);
        Assert.assertNotNull(dumpResult);
        dumpResult.close();
        dumpInputStream.close();

        // Test JAR
        InputStream jarInputStream = createTestArchiveInputStream("JAR");
        ArchiveInputStream jarResult = factory.createArchiveInputStream("JAR", jarInputStream);
        Assert.assertNotNull(jarResult);
        jarResult.close();
        jarInputStream.close();

        // Test TAR
        InputStream tarInputStream = createTestArchiveInputStream("TAR");
        ArchiveInputStream tarResult = factory.createArchiveInputStream("TAR", tarInputStream);
        Assert.assertNotNull(tarResult);
        tarResult.close();
        tarInputStream.close();

        // Test ZIP
        InputStream zipInputStream = createTestArchiveInputStream("ZIP");
        ArchiveInputStream zipResult = factory.createArchiveInputStream("ZIP", zipInputStream);
        Assert.assertNotNull(zipResult);
        zipResult.close();
        zipInputStream.close();
    }

    private InputStream createTestArchiveInputStream(String archiveType) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = null;
        switch (archiveType) {
            case "AR":
                archiveOutputStream = new ArArchiveOutputStream(outputStream);
                break;
            case "ARJ":
                archiveOutputStream = new ArjArchiveOutputStream(outputStream);
                break;
            case "CPIO":
                archiveOutputStream = new CpioArchiveOutputStream(outputStream);
                break;
            case "DUMP":
                archiveOutputStream = new DumpArchiveOutputStream(outputStream);
                break;
            case "JAR":
                archiveOutputStream = new JarArchiveOutputStream(outputStream);
                break;
            case "TAR":
                archiveOutputStream = new TarArchiveOutputStream(outputStream);
                break;
            case "ZIP":
                archiveOutputStream = new ZipArchiveOutputStream(outputStream);
                break;
            default:
                throw new IOException("Unknown archive type: " + archiveType);
        }
        archiveOutputStream.putNextEntry(new ArArchiveEntry("test.txt"));
        archiveOutputStream.write(new byte[]{"T".encodeBase64()});
        archiveOutputStream.finish();
        archiveOutputStream.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}