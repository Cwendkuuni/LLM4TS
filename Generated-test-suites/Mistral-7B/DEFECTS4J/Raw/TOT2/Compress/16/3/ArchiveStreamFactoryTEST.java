import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test for ArchiveStreamFactory class
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test AR archiver
        ArchiveInputStream arInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
        Assert.assertEquals(ArArchiveInputStream.class, arInputStream.getClass());
        arInputStream.close();

        // Test ZIP archiver
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipInputStreamWrapper = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        Assert.assertEquals(ZipArchiveInputStream.class, zipInputStreamWrapper.getClass());
        zipInputStreamWrapper.close();
        zipInputStream.close();

        // Test JAR archiver
        InputStream jarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream jarInputStreamWrapper = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        Assert.assertEquals(JarArchiveInputStream.class, jarInputStreamWrapper.getClass());
        JarArchiveEntry jarEntry = (JarArchiveEntry) jarInputStreamWrapper.getNextEntry();
        Assert.assertNotNull(jarEntry);
        jarInputStreamWrapper.close();
        jarInputStream.close();

        // Test failure for invalid archiver name
        try {
            ArchiveInputStream invalidInputStream = new ArchiveStreamFactory().createArchiveInputStream("invalid", new ByteArrayInputStream(new byte[0]));
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: invalid not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test AR archiver
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arOutputStreamWrapper = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        arOutputStreamWrapper.close();
        Assert.assertEquals(ArArchiveOutputStream.class, arOutputStreamWrapper.getClass());

        // Test ZIP archiver
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipOutputStreamWrapper = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        zipOutputStreamWrapper.close();
        Assert.assertEquals(ZipArchiveOutputStream.class, zipOutputStreamWrapper.getClass());

        // Test JAR archiver
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        JarArchiveEntry jarEntry = new JarArchiveEntry("test.txt");
        ArchiveOutputStream jarOutputStreamWrapper = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarEntry, jarOutputStream);
        jarOutputStreamWrapper.putNextEntry(jarEntry);
        jarOutputStreamWrapper.closeArchiveEntry();
        jarOutputStreamWrapper.close();
        Assert.assertEquals(JarArchiveOutputStream.class, jarOutputStreamWrapper.getClass());

        // Test failure for invalid archiver name
        try {
            ArchiveOutputStream invalidOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("invalid", new ByteArrayOutputStream());
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: invalid not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithBytes() throws Exception {
        byte[] tarBytes = new byte[512];
        System.arraycopy(TarArchiveInputStream.magicNumber, 0, tarBytes, 0, TarArchiveInputStream.magicNumber.length);

        // Test TAR archiver
        InputStream tarInputStream = new ByteArrayInputStream(tarBytes);
        ArchiveInputStream tarInputStreamWrapper = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        Assert.assertEquals(TarArchiveInputStream.class, tarInputStreamWrapper.getClass());
        tarInputStreamWrapper.close();
        tarInputStream.close();

        // Test failure for invalid signature
        byte[] invalidTarBytes = new byte[512];
        for (int i = 0; i < TarArchiveInputStream.magicNumber.length; i++) {
            invalidTarBytes[i] = (byte) (TarArchiveInputStream.magicNumber[i] ^ 0x1);
        }

        InputStream invalidTarInputStream = new ByteArrayInputStream(invalidTarBytes);
        try {
            ArchiveInputStream invalidTarInputStreamWrapper = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, invalidTarInputStream);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
        invalidTarInputStream.close();
    }
}