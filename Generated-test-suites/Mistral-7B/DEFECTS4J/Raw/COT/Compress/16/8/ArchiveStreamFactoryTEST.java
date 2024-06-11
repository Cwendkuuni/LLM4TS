import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_AR() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.INSTANCE.createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() throws Exception {
        String archiverName = ArchiveStreamFactory.CPIO;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.INSTANCE.createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_Dump() throws Exception {
        String archiverName = ArchiveStreamFactory.DUMP;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.INSTANCE.createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_JAR() throws Exception {
        String archiverName = ArchiveStreamFactory.JAR;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.INSTANCE.createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_TAR() throws Exception {
        String archiverName = ArchiveStreamFactory.TAR;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.INSTANCE.createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.INSTANCE.createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownFormat() throws Exception {
        String archiverName = "unknown";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory factory = ArchiveStreamFactory.INSTANCE;
        factory.createArchiveInputStream(archiverName, inputStream);
        fail("Should have thrown ArchiveException");
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.INSTANCE.createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO() throws Exception {
        String archiverName = ArchiveStreamFactory.CPIO;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.INSTANCE.createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Dump() throws Exception {
        String archiverName = ArchiveStreamFactory.DUMP;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.INSTANCE.createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(DumpArchiveOutputStream.class, archiveOutputStream);
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_JAR() throws Exception {
        String archiverName = ArchiveStreamFactory.JAR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.INSTANCE.createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_TAR() throws Exception {
        String archiverName = ArchiveStreamFactory.TAR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.INSTANCE.createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.INSTANCE.createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownFormat() throws Exception {
        String archiverName = "unknown";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = ArchiveStreamFactory.INSTANCE;
        factory.createArchiveOutputStream(archiverName, outputStream);
        fail("Should have thrown ArchiveException");
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.INSTANCE.createArchiveInputStream(inputStream);
        try {
            assertTrue(archiveInputStream instanceof ZipArchiveInputStream || archiveInputStream instanceof JarArchiveInputStream);
            archiveInputStream.close();
        } catch (ArchiveException e) {
            fail("Should have been able to create a ZIP or JAR archive input stream from the given input stream.");
        }
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveInputStream_InvalidStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        inputStream.close();
        inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory factory = ArchiveStreamFactory.INSTANCE;
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
        fail("Should have thrown IOException as the input stream does not support mark and reset.");
    }
}