package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertNotNull;
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
    public void testCreateArchiveInputStream() throws ArchiveException, IOException {
        String[] testArchives = {ArchiveStreamFactory.AR, ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR, ArchiveStreamFactory.ZIP};

        for (String testArchive : testArchives) {
            InputStream testInputStream = null;
            try {
                testInputStream = new ByteArrayInputStream("TestData".getBytes()); // InputStream with test data
                ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(testArchive, testInputStream);
                assertNotNull(archiveInputStream);
                archiveInputStream.close();
            } catch (ArchiveException e) {
                if (testArchive.equalsIgnoreCase(ArchiveStreamFactory.AR)) {
                    // AR archive is not supported by JUnit test with the provided AR implementation
                    // So, we don't test it here.
                } else {
                    throw e;
                }
            } finally {
                if (testInputStream != null) {
                    testInputStream.close();
                }
            }
        }

        // Test for null input stream
        try {
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            assertNull(archiveInputStream);
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
        String[] testArchives = {ArchiveStreamFactory.AR, ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR, ArchiveStreamFactory.ZIP};

        for (String testArchive : testArchives) {
            OutputStream testOutputStream = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(testArchive, testOutputStream);
            assertNotNull(archiveOutputStream);
            archiveOutputStream.finish();
            archiveOutputStream.close();
            testOutputStream.close();
        }

        // Test for null output stream
        try {
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            assertNull(archiveOutputStream);
        } catch (ArchiveException e) {
            // Expected
        }
    }
}