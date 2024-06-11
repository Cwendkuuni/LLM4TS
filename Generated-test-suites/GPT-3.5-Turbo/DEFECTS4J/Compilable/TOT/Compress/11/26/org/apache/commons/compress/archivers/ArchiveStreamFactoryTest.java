package org.apache.commons.compress.archivers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
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
import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveFactory;

    @Before
    public void setup() {
        archiveFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream_Ar() {
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = archiveFactory.createArchiveInputStream("ar", in);
            
            assertNotNull(ais);
            assertTrue(ais instanceof ArArchiveInputStream);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_Zip() {
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = archiveFactory.createArchiveInputStream("zip", in);
            
            assertNotNull(ais);
            assertTrue(ais instanceof ZipArchiveInputStream);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_Tar() {
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = archiveFactory.createArchiveInputStream("tar", in);
            
            assertNotNull(ais);
            assertTrue(ais instanceof TarArchiveInputStream);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_Jar() {
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = archiveFactory.createArchiveInputStream("jar", in);
            
            assertNotNull(ais);
            assertTrue(ais instanceof JarArchiveInputStream);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_Cpio() {
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = archiveFactory.createArchiveInputStream("cpio", in);
            
            assertNotNull(ais);
            assertTrue(ais instanceof CpioArchiveInputStream);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_Dump() {
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = archiveFactory.createArchiveInputStream("dump", in);
            
            assertNotNull(ais);
            assertTrue(ais instanceof DumpArchiveInputStream);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidArchiverName() throws ArchiveException {
        archiveFactory.createArchiveInputStream("invalid", new ByteArrayInputStream(new byte[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() throws ArchiveException {
        archiveFactory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws ArchiveException {
        archiveFactory.createArchiveInputStream("ar", null);
    }

    @Test
    public void testCreateArchiveOutputStream_Ar() {
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = archiveFactory.createArchiveOutputStream("ar", out);
            
            assertNotNull(aos);
            assertTrue(aos instanceof ArArchiveOutputStream);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_Zip() {
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = archiveFactory.createArchiveOutputStream("zip", out);
            
            assertNotNull(aos);
            assertTrue(aos instanceof ZipArchiveOutputStream);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_Tar() {
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = archiveFactory.createArchiveOutputStream("tar", out);
            
            assertNotNull(aos);
            assertTrue(aos instanceof TarArchiveOutputStream);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_Jar() {
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = archiveFactory.createArchiveOutputStream("jar", out);
            
            assertNotNull(aos);
            assertTrue(aos instanceof JarArchiveOutputStream);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_Cpio() {
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = archiveFactory.createArchiveOutputStream("cpio", out);
            
            assertNotNull(aos);
            assertTrue(aos instanceof CpioArchiveOutputStream);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_InvalidArchiverName() throws ArchiveException {
        archiveFactory.createArchiveOutputStream("invalid", new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() throws ArchiveException {
        archiveFactory.createArchiveOutputStream(null, new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws ArchiveException {
        archiveFactory.createArchiveOutputStream("ar", null);
    }

    @Test
    public void testCreateArchiveInputStream_StreamAutodetection() {
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = archiveFactory.createArchiveInputStream(in);
            
            assertNotNull(ais);
            assertTrue(ais instanceof ZipArchiveInputStream);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream_Autodetection() throws ArchiveException {
        archiveFactory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_InputMarkNotSupported_Autodetection() throws ArchiveException {
        archiveFactory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
    }

    // Additional test cases for autodetection can be added here

}