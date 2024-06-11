package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertNotNull;

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
        for (String archiverName : new String[]{"AR", "CPIO", "DUMP", "JAR", "TAR", "ZIP"}) {
            try {
                InputStream inputStream = new ByteArrayInputStream(new byte[1]);
                ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
                assertNotNull(archiveInputStream);
            } catch (ArchiveException e) {
                // Expected for unknown format
            }
        }

        for (String archiverName : new String[]{ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO, ArchiveStreamFactory.DUMP, ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR, ArchiveStreamFactory.ZIP}) {
            try {
                InputStream inputStream = getClass().getResourceAsStream("/testData/" + archiverName + ".test");
                ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
                assertNotNull(archiveInputStream);
            } catch (ArchiveException | IOException e) {
                // handle exceptions
            }
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        for (String archiverName : new String[]{"AR", "CPIO", "DUMP", "JAR", "TAR", "ZIP"}) {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
            assertNotNull(archiveOutputStream);
        }
    }
}