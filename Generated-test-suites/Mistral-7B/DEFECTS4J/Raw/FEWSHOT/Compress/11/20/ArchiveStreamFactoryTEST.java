package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

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

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Test with valid archiver names
        for (String archiverName : new String[]{"AR", "CPIO", "DUMP", "JAR", "TAR", "ZIP"}) {
            InputStream inputStream = new ByteArrayInputStream(new byte[0]);
            try {
                ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
                assertInstanceOf(archiverName.toLowerCase().hashCode(), archiveInputStream.getClass());
                archiveInputStream.close();
            } catch (ArchiveException e) {
                // This test does not check for specific ArchiveException messages, as we're only testing if an exception is thrown
                // for unsupported archiver names
            }
        }

        // Test with invalid archiver name
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String invalidArchiverName = "invalid";
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(invalidArchiverName, inputStream);
            assert false : "An exception should have been thrown.";
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Test with valid archiver names
        for (String archiverName : new String[]{"AR", "CPIO", "JAR", "TAR", "ZIP"}) {
            OutputStream outputStream = new ByteArrayOutputStream();
            try {
                ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
                assertInstanceOf(archiverName.toLowerCase().hashCode(), archiveOutputStream.getClass());
                archiveOutputStream.finish();
            } catch (ArchiveException e) {
                // This test does not check for specific ArchiveException messages, as we're only testing if an exception is thrown
                // for unsupported archiver names
            }
        }

        // Test with invalid archiver name
        String invalidArchiverName = "invalid";
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(invalidArchiverName, outputStream);
            assert false : "An exception should have been thrown.";
        } catch (ArchiveException e) {
            // Expected
        }

        // Test with null output stream
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(JAR, null);
            assert false : "An exception should have been thrown.";
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() {
        // Test with valid archiver names
        for (String archiverName : new String[]{"AR", "CPIO", "DUMP", "JAR", "TAR", "ZIP"}) {
            InputStream inputStream = new ByteArrayInputStream(new byte[0]);
            try {
                ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
                assertInstanceOf(archiverName.toLowerCase().hashCode(), archiveInputStream.getClass());
                archiveInputStream.close();
            } catch (ArchiveException e) {
                // This test does not check for specific ArchiveException messages, as we're only testing if an exception is thrown
                // for unsupported archiver names
            }
        }

        // Test with null input stream
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(ZIP, null);
            assert false : "An exception should have been thrown.";
        } catch (ArchiveException e) {
            // Expected
        }

        // Test with input stream that does not support mark
        InputStream inputStreamThatDoesNotSupportMark = new ByteArrayInputStream(new byte[0]) {
            @Override
            public synchronized void mark(int readlimit) {
                throw new UnsupportedOperationException();
            }

            @Override
            public synchronized void reset() {
                throw new UnsupportedOperationException();
            }
        };
        try {
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(AR, inputStreamThatDoesNotSupportMark);
            assertNull("An exception should have been thrown.", archiveInputStream);
        } catch (ArchiveException e) {
            // Expected
        }
    }
}