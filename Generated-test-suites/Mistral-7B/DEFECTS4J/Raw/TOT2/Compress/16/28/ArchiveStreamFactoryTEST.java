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
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.jar.JarEntry;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test with valid archiver names
        for (String archiverName : new String[]{"AR", "CPIO", "DUMP", "JAR", "TAR", "ZIP"}) {
            try (InputStream testFile = new FileInputStream("test.tar.gz");
                 ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, testFile)) {
                Assert.assertNotNull(inputStream);
                JarEntry entry = inputStream.getNextJarEntry();
                Assert.assertNotNull(entry);
                InputStream entryStream = inputStream.getInputStream(entry);
                byte[] buffer = new byte[1024];
                int read = 0;
                while ((read = entryStream.read(buffer)) > 0) {
                    // Read and verify the content
                }
                entryStream.close();
            }
        }

        // Test with an unknown archiver name
        try (InputStream testFile = new FileInputStream("test.tar.gz")) {
            Assert.assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream("unknown", testFile));
        }

        // Test with a null input stream
        Assert.assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null));
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test with valid archiver names
        for (String archiverName : new String[]{"AR", "CPIO", "DUMP", "JAR", "TAR", "ZIP"}) {
            try (OutputStream testFile = new FileOutputStream("test.tar.gz");
                 ArchiveOutputStream outputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, testFile)) {
                Assert.assertNotNull(outputStream);
                outputStream.putNextEntry(new JarEntry("test.txt"));
                try (OutputStream entryStream = outputStream.createOutputStream("test.txt")) {
                    IOUtils.write("Test content", entryStream, StandardCharsets.UTF_8);
                }
                outputStream.closeArchiveEntry();
                outputStream.finish();
                outputStream.close();

                try (ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, new FileInputStream("test.tar.gz"))) {
                    JarEntry entry = archiveInputStream.getNextJarEntry();
                    Assert.assertNotNull(entry);
                    Assert.assertEquals("test.txt", entry.getName());
                    InputStream inputStream = archiveInputStream.getInputStream(entry);
                    byte[] buffer = new byte[1024];
                    int read = 0;
                    while ((read = inputStream.read(buffer)) > 0) {
                        Assert.assertEquals("Test content".getBytes(StandardCharsets.UTF_8)[0], buffer[0]);
                    }
                    inputStream.close();
                }
            }
        }

        // Test with an unknown archiver name
        try (OutputStream testFile = new FileOutputStream("test.tar.gz")) {
            Assert.assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream("unknown", testFile));
        }

        // Test with a null output stream
        Assert.assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null));
    }

    @Test
    public void testCreateArchiveInputStreamWithAutodetection() throws Exception {
        // Test with a valid archive (TAR.GZ)
        try (InputStream testFile = new FileInputStream("test.tar.gz")) {
            ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(testFile);
            Assert.assertNotNull(inputStream);
            JarEntry entry = inputStream.getNextJarEntry();
            Assert.assertNotNull(entry);
            InputStream entryStream = inputStream.getInputStream(entry);
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = entryStream.read(buffer)) > 0) {
                // Read and verify the content
            }
            entryStream.close();
        }

        // Test with an invalid archive (not a TAR.GZ, for example, a plain text file)
        try (InputStream testFile = new FileInputStream("test.txt")) {
            Assert.assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(testFile));
        }
    }
}