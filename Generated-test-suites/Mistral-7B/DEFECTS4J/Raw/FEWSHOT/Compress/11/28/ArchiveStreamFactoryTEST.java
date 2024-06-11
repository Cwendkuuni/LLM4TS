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
    public void testCreateArchiveInputStream() throws Exception {
        // Test with known archive types
        for (String archiveType : new String[]{"ar", "zip", "tar", "jar", "cpio", "dump"}) {
            InputStream inputStream = new ByteArrayInputStream(createArchiveBytes(archiveType));
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveType, inputStream);
            assertNotNull(archiveInputStream);
            archiveInputStream.close();
        }

        // Test with unknown archive type
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("unknown", inputStream);
            assert false : "Should throw ArchiveException";
        } catch (ArchiveException e) {
            // Expected
        }

        // Test with null inputStream
        try {
            new ArchiveStreamFactory().createArchiveInputStream("zip", null);
            assert false : "Should throw IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test with known archive types
        for (String archiveType : new String[]{"ar", "zip", "tar", "jar", "cpio", "dump"}) {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveType, outputStream);
            assertNotNull(archiveOutputStream);
            archiveOutputStream.finish();
            archiveOutputStream.close();
        }

        // Test with unknown archive type
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("unknown", outputStream);
            assert false : "Should throw ArchiveException";
        } catch (ArchiveException e) {
            // Expected
        }

        // Test with null outputStream
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("zip", null);
            assert false : "Should throw IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        // Test with known archive types
        for (String archiveType : new String[]{"zip", "jar", "ar", "cpio", "tar", "dump"}) {
            InputStream inputStream = new ByteArrayInputStream(createArchiveBytes(archiveType));
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
            assertNotNull(archiveInputStream);
            archiveInputStream.close();
        }

        // Test with null inputStream
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null);
            assert false : "Should throw IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with empty inputStream
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    private byte[] createArchiveBytes(String archiveType) throws IOException {
        InputStream inputStream;
        if (archiveType.equals("ar")) {
            inputStream = new FileInputStream("testdata/test.ar");
        } else if (archiveType.equals("zip")) {
            inputStream = new FileInputStream("testdata/test.zip");
        } else if (archiveType.equals("tar")) {
            inputStream = new FileInputStream("testdata/test.tar.gz");
        } else if (archiveType.equals("jar")) {
            inputStream = new FileInputStream("testdata/test.jar");
        } else if (archiveType.equals("cpio")) {
            inputStream = new FileInputStream("testdata/test.cpio.gz");
        } else if (archiveType.equals("dump")) {
            inputStream = new FileInputStream("testdata/test.dump");
        } else {
            throw new IOException("Unsupported archive type: " + archiveType);
        }

        byte[] buffer = new byte[1024];
        int len;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
        return outputStream.toByteArray();
    }
}