import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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
import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream_AR_Success() {
        try {
            // Arrange
            byte[] data = new byte[10];
            InputStream in = new ByteArrayInputStream(data);

            // Act
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);

            // Assert
            assertNotNull(ais);
            assertTrue(ais instanceof ArArchiveInputStream);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStream_AR_NullArchiverName_ExceptionThrown() {
        try {
            // Arrange
            InputStream in = new FileInputStream("input.txt");

            // Act
            factory.createArchiveInputStream(null, in);

            // Assert
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStream_AR_NullInputStream_ExceptionThrown() {
        try {
            // Arrange

            // Act
            factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);

            // Assert
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStream_UnknownArchiverName_ExceptionThrown() {
        try {
            // Arrange
            InputStream in = new FileInputStream("input.txt");

            // Act
            factory.createArchiveInputStream("unknown", in);

            // Assert
            fail("ArchiveException should be thrown");
        } catch (ArchiveException e) {
            // Pass
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP_Success() {
        try {
            // Arrange
            OutputStream out = new ByteArrayOutputStream();

            // Act
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);

            // Assert
            assertNotNull(aos);
            assertTrue(aos instanceof ZipArchiveOutputStream);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP_NullArchiverName_ExceptionThrown() {
        try {
            // Arrange
            OutputStream out = new FileOutputStream("output.zip");

            // Act
            factory.createArchiveOutputStream(null, out);

            // Assert
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP_NullOutputStream_ExceptionThrown() {
        try {
            // Arrange

            // Act
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);

            // Assert
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStream_UnknownArchiverName_ExceptionThrown() {
        try {
            // Arrange
            OutputStream out = new FileOutputStream("output.zip");

            // Act
            factory.createArchiveOutputStream("unknown", out);

            // Assert
            fail("ArchiveException should be thrown");
        } catch (ArchiveException e) {
            // Pass
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_ZIP_Success() {
        try {
            // Arrange
            byte[] data = new byte[10];
            InputStream in = new ByteArrayInputStream(data);

            // Act
            ArchiveInputStream ais = factory.createArchiveInputStream(in);

            // Assert
            assertNotNull(ais);
            assertTrue(ais instanceof ZipArchiveInputStream);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_TAR_Success() {
        try {
            // Arrange
            byte[] data = new byte[512];
            InputStream in = new ByteArrayInputStream(data);

            // Act
            ArchiveInputStream ais = factory.createArchiveInputStream(in);

            // Assert
            assertNotNull(ais);
            assertTrue(ais instanceof TarArchiveInputStream);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_UnknownSignature_ExceptionThrown() {
        try {
            // Arrange
            byte[] data = "unknown".getBytes();
            InputStream in = new ByteArrayInputStream(data);

            // Act
            factory.createArchiveInputStream(in);

            // Assert
            fail("ArchiveException should be thrown");
        } catch (ArchiveException e) {
            // Pass
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

}