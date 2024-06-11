package org.apache.commons.compress.archivers.test;

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

/**
 * Test class for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    @org.junit.Test
    public void testCreateArchiveInputStream() {
        for (String archiverName : new String[]{"AR", "CPIO", "DUMP", "JAR", "TAR", "ZIP"}) {
            InputStream in = new ByteArrayInputStream(ArchiveStreamFactory.class.getResource("/test-" + archiverName + ".bin").readAllBytes());
            ArchiveInputStream archiveInputStream = null;
            try {
                archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, in);
                assertNotNull(archiveInputStream);
            } finally {
                if (archiveInputStream != null) {
                    try {
                        archiveInputStream.close();
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }
        }

        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream("UNKNOWN", new ByteArrayInputStream(new byte[0])));
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream(null, new ByteArrayInputStream(new byte[0])));
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream("AR", null));
    }

    @org.junit.Test
    public void testCreateArchiveOutputStream() {
        for (String archiverName : new String[]{"AR", "CPIO", "DUMP", "JAR", "TAR", "ZIP"}) {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = null;
            try {
                archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, out);
                assertNotNull(archiveOutputStream);
            } finally {
                if (archiveOutputStream != null) {
                    try {
                        archiveOutputStream.finish();
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }

            byte[] output = out.toByteArray();
            assertNotNull(output);
            // check the size of the output, it should be the same for all tests
            int expectedSize = 1024; // replace with the correct size of the test files
            assert (output.length == expectedSize);
        }

        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.getInstance().createArchiveOutputStream("UNKNOWN", new ByteArrayOutputStream()));
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance().createArchiveOutputStream(null, new ByteArrayOutputStream()));
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance().createArchiveOutputStream("AR", null));
    }
}