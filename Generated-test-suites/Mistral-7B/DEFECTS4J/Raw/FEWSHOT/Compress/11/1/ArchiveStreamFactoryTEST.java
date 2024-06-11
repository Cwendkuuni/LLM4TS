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
    public void testCreateArchiveInputStream() throws Exception {
        for (String archiver : new String[]{"ar", "zip", "tar", "jar", "cpio", "dump"}) {
            InputStream input = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiver, input);
            assertNotNull(archiveInputStream);
            archiveInputStream.close();
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        for (String archiver : new String[]{"ar", "zip", "tar", "jar", "cpio"}) {
            OutputStream output = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiver, output);
            assertNotNull(archiveOutputStream);
            archiveOutputStream.close();
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() throws Exception {
        String testData = "test data";

        for (String archiver : new String[]{"ar", "zip", "tar", "jar", "cpio", "dump"}) {
            byte[] bytes = getTestDataBytes(archiver, testData);
            InputStream input = new ByteArrayInputStream(bytes);
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiver, input);
            assertNotNull(archiveInputStream);
            // Here you can add your assertions to test the contents of the archive
            archiveInputStream.close();
        }
    }

    @Test
    public void testCreateArchiveOutputStreamToNull() {
        assertNull(ArchiveStreamFactory.getInstance().createArchiveOutputStream("zip", null));
    }

    private byte[] getTestDataBytes(String archiver, String testData) {
        ArchiveOutputStream archiveOutputStream = null;
        try {
            OutputStream output = new ByteArrayOutputStream();
            archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiver, output);
            archiveOutputStream.putNextEntry(new ArchiveEntry("test.txt"));
            archiveOutputStream.write(testData.getBytes());
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
            return output.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (archiveOutputStream != null) {
                try {
                    archiveOutputStream.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}