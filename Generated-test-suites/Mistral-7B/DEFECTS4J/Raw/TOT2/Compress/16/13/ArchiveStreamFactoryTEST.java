package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ARArchiveEntry;
import org.apache.commons.compress.archivers.ar.ARArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.test.ArchiveTestHelper;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    /**
     * Test for createArchiveInputStream(String, InputStream) method.
     *
     * @throws IOException when an I/O error occurs
     */
    @Test
    public void testCreateArchiveInputStream() throws IOException {
        String[] archiverNames = new String[] { ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO,
                ArchiveStreamFactory.DUMP, ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR,
                ArchiveStreamFactory.ZIP };
        InputStream inputStream = new ByteArrayInputStream(ArchiveTestHelper.getTestFileData("test.tar.gz"));

        for (String archiverName : archiverNames) {
            ArchiveInputStream archiveInputStream = null;
            try {
                archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
                ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
                assertNotNull(archiveEntry);
            } finally {
                if (archiveInputStream != null) {
                    archiveInputStream.close();
                }
            }
        }
    }

    /**
     * Test for createArchiveInputStream(InputStream) method.
     *
     * @throws IOException when an I/O error occurs
     * @throws ArchiveException when an archive-specific error occurs
     */
    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws IOException, ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(ArchiveTestHelper.getTestFileData("test.tar.gz"));
        ArchiveInputStream archiveInputStream = null;

        try {
            archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(inputStream);
            ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
            assertNotNull(archiveEntry);
            if (archiveEntry instanceof TarArchiveEntry) {
                assertThrows(ArchiveException.class, () -> {
                    ArchiveInputStream archiveInputStream2 = ArchiveStreamFactory.newInstance()
                            .createArchiveInputStream("invalidFormat", inputStream);
                });
            }
        } finally {
            if (archiveInputStream != null) {
                archiveInputStream.close();
            }
        }
    }

    /**
     * Test for createArchiveOutputStream(String, OutputStream) method.
     *
     * @throws IOException when an I/O error occurs
     */
    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        String[] archiverNames = new String[] { ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO,
                ArchiveStreamFactory.DUMP, ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR,
                ArchiveStreamFactory.ZIP };
        OutputStream outputStream = new ByteArrayOutputStream();

        for (String archiverName : archiverNames) {
            ArchiveOutputStream archiveOutputStream = null;
            try {
                archiveOutputStream = ArchiveStreamFactory.newInstance()
                        .createArchiveOutputStream(archiverName, outputStream);
                ArchiveEntry archiveEntry = new JarArchiveEntry("test.txt");
                archiveOutputStream.putNextEntry(archiveEntry);
                outputStream.write(ArchiveTestHelper.getTestFileData("test.txt"));
                archiveOutputStream.closeArchiveEntry();
                archiveOutputStream.finish();
                assertNotNull(archiveOutputStream.getStream());
            } finally {
                if (archiveOutputStream != null) {
                    archiveOutputStream.close();
                }
            }
        }
    }

    /**
     * Test for createArchiveOutputStream(String, OutputStream) method with
     * different archive formats having a different number of bytes per header.
     *
     * @throws IOException when an I/O error occurs
     */
    @Test
    public void testCreateArchiveOutputStreamAutoDetect() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        // Test with a tar archive, which has a bigger header than other formats
        ArchiveOutputStream archiveOutputStreamTar = null;
        try {
            archiveOutputStreamTar = ArchiveStreamFactory.newInstance()
                    .createArchiveOutputStream("tar", outputStream);
            assertThrows(ArchiveException.class, () -> {
                ArchiveOutputStream archiveOutputStreamZip = ArchiveStreamFactory.newInstance()
                        .createArchiveOutputStream("zip", outputStream);
            });
        } finally {
            if (archiveOutputStreamTar != null) {
                archiveOutputStreamTar.close();
            }
        }

        // Test with a zip archive, which has a smaller header than other formats
        OutputStream outputStream2 = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStreamZip = null;
        try {
            archiveOutputStreamZip = ArchiveStreamFactory.newInstance()
                    .createArchiveOutputStream("zip", outputStream2);
            assertThrows(ArchiveException.class, () -> {
                ArchiveOutputStream archiveOutputStreamTar = ArchiveStreamFactory.newInstance()
                        .createArchiveOutputStream("tar", outputStream2);
            });
        } finally {
            if (archiveOutputStreamZip != null) {
                archiveOutputStreamZip.close();
            }
        }
    }
}