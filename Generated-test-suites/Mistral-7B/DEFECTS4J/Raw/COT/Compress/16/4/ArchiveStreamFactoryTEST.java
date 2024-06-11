package org.apache.commons.compress.archivers.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.junit.Test;

/**
 * Test the ArchiveStreamFactory class.
 */
public class ArchiveStreamFactoryTest {

    

    @Test
    public void testCreateArchiveInputStream_ValidArchiveFormat_ReturnsInstance() {
        for (String archiveFormat : new String[]{"AR", "ZIP", "TAR", "JAR", "CPIO", "DUMP"}) {
            try {
                InputStream testInputStream = new ByteArrayInputStream(new byte[1024]);
                ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance()
                        .createArchiveInputStream(archiveFormat, testInputStream);
                assertNotNull(archiveInputStream);
            } catch (ArchiveException e) {
                throw new AssertionError("Unexpected error creating ArchiveInputStream for format: " + archiveFormat, e);
            }
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArchiveFormat_ReturnsInstance() {
        for (String archiveFormat : new String[]{"AR", "ZIP", "TAR", "JAR", "CPIO"}) {
            try {
                OutputStream testOutputStream = new ByteArrayOutputStream();
                ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance()
                        .createArchiveOutputStream(archiveFormat, testOutputStream);
                assertNotNull(archiveOutputStream);
            } catch (ArchiveException e) {
                throw new AssertionError("Unexpected error creating ArchiveOutputStream for format: " + archiveFormat, e);
            }
        }
    }

    @Test
    public void testCreateArchiveInputStream_InvalidArchiveFormat_ThrowsArchiveException() {
        for (String invalidFormat : new String[]{"abc", "def"}) {
            try {
                InputStream testInputStream = new ByteArrayInputStream(new byte[1024]);
                ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();
                factory.createArchiveInputStream(invalidFormat, testInputStream);
                assert false; // Should not reach this point
            } catch (ArchiveException e) {
                // Expected
            }
        }
    }

    @Test
    public void testCreateArchiveOutputStream_NullOutputStream_ThrowsIllegalArgumentException() {
        ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();
        assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream(null, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveOutputStream_NullArchiveFormat_ThrowsIllegalArgumentException() {
        ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();
        assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream(null, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveOutputStream_InvalidInputStream_ThrowsArchiveException() {
        InputStream testInputStream = new ByteArrayInputStream(new byte[1024]);
        testInputStream.mark(1024);
        try {
            OutputStream testOutputStream = new ByteArrayOutputStream();
            ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();
            factory.createArchiveOutputStream(null, testInputStream);
            assert false; // Should not reach this point
        } catch (ArchiveException e) {
            // Expected
        } finally {
            try {
                testInputStream.reset();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    @Test
    public void testCreateArchiveInputStream_NoMatcherFound_ThrowsArchiveException() {
        InputStream testInputStream = new ByteArrayInputStream(new byte[1024]);
        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.newInstance().createArchiveInputStream(null, testInputStream));
    }

    
}