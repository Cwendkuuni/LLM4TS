package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
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
import org.junit.Test;

/**
 * Unit tests for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        for (String archiverName : new String[]{"AR", "CPIO", "DUMP", "JAR", "TAR", "ZIP"}) {
            InputStream testStream = new ByteArrayInputStream(new byte[1024]);
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, testStream);
            assertNotNull(archiveInputStream);
            archiveInputStream.close();
        }
    }

    @Test
    public void testCreateArchiveInputStream_AR() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream testStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, testStream);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() throws Exception {
        String archiverName = ArchiveStreamFactory.CPIO;
        InputStream testStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, testStream);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_DUMP() throws Exception {
        String archiverName = ArchiveStreamFactory.DUMP;
        InputStream testStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, testStream);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_JAR() throws Exception {
        String archiverName = ArchiveStreamFactory.JAR;
        InputStream testStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, testStream);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof JarArchiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_TAR() throws Exception {
        String archiverName = ArchiveStreamFactory.TAR;
        InputStream testStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, testStream);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream testStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, testStream);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_Unknown() throws Exception {
        String archiverName = "unkown";
        InputStream testStream = new ByteArrayInputStream(new byte[1024]);
        new ArchiveStreamFactory().createArchiveInputStream(archiverName, testStream);
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        for (String archiverName : new String[]{"AR", "CPIO", "DUMP", "JAR", "TAR", "ZIP"}) {
            OutputStream testStream = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, testStream);
            assertNotNull(archiveOutputStream);
            archiveOutputStream.close();
        }
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream testStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, testStream);
        assertNotNull(archiveOutputStream);
        assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO() throws Exception {
        String archiverName = ArchiveStreamFactory.CPIO;
        OutputStream testStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, testStream);
        assertNotNull(archiveOutputStream);
        assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_DUMP() throws Exception {
        String archiverName = ArchiveStreamFactory.DUMP;
        OutputStream testStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, testStream);
        assertNotNull(archiveOutputStream);
        assertTrue(archiveOutputStream instanceof DumpArchiveOutputStream);
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_JAR() throws Exception {
        String archiverName = ArchiveStreamFactory.JAR;
        OutputStream testStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, testStream);
        assertNotNull(archiveOutputStream);
        assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_TAR() throws Exception {
        String archiverName = ArchiveStreamFactory.TAR;
        OutputStream testStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, testStream);
        assertNotNull(archiveOutputStream);
        assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream testStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, testStream);
        assertNotNull(archiveOutputStream);
        assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_Unknown() throws Exception {
        String archiverName = "unknown";
        OutputStream testStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, testStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect() throws Exception {
        String archiverName = null;
        InputStream testStream = new ByteArrayInputStream(new byte[]{"P PK\003\021"});
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, testStream);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Autodetect_Invalid() throws Exception {
        String archiverName = null;
        InputStream testStream = new ByteArrayInputStream(new byte[1024]);
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(archiverName, testStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }
    }
}