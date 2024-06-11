package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertNotNull;

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
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test createArchiveInputStream method with valid arguments.
     * @throws Exception if test fails
     */
    @Test
    public void testCreateArchiveInputStreamValidArguments() throws Exception {
        for (String archiverName : ArchiveStreamFactory.values()) {
            InputStream inputStream = new ByteArrayInputStream(getTestFileBytes(archiverName));
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.INSTANCE.createArchiveInputStream(archiverName, inputStream);
            assertNotNull(archiveInputStream);
            archiveInputStream.close();
        }
    }

    /**
     * Test createArchiveInputStream method with null arguments.
     * @throws Exception if test fails
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullArguments() throws Exception {
        for (String archiverName : ArchiveStreamFactory.values()) {
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.INSTANCE.createArchiveInputStream(archiverName, null);
        }
    }

    /**
     * Test createArchiveInputStream method with unknown archiver name.
     * @throws Exception if test fails
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownArchiverName() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory.INSTANCE.createArchiveInputStream("unknownArchiver", inputStream);
    }

    /**
     * Test createArchiveOutputStream method with valid arguments.
     * @throws Exception if test fails
     */
    @Test
    public void testCreateArchiveOutputStreamValidArguments() throws Exception {
        for (String archiverName : ArchiveStreamFactory.values()) {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.INSTANCE.createArchiveOutputStream(archiverName, outputStream);
            assertNotNull(archiveOutputStream);
            archiveOutputStream.close();
        }
    }

    /**
     * Test createArchiveOutputStream method with null arguments.
     * @throws Exception if test fails
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullArguments() throws Exception {
        for (String archiverName : ArchiveStreamFactory.values()) {
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.INSTANCE.createArchiveOutputStream(archiverName, null);
        }
    }

    /**
     * Test createArchiveOutputStream method with unknown archiver name.
     * @throws Exception if test fails
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknownArchiverName() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory.INSTANCE.createArchiveOutputStream("unknownArchiver", outputStream);
    }

    /**
     * Test createArchiveInputStream method with an InputStream that does not support mark.
     * @throws Exception if test fails
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNoMarkSupported() throws Exception {
        ArchiveStreamFactory.INSTANCE.createArchiveInputStream("tar", new ByteArrayInputStream(new byte[0]));
    }

    /**
     * Test createArchiveInputStream method with an empty InputStream.
     * @throws Exception if test fails
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamEmptyInputStream() throws Exception {
        ArchiveStreamFactory.INSTANCE.createArchiveInputStream("zip", new ByteArrayInputStream(new byte[0]));
    }

    /**
     * Test createArchiveOutputStream method with an empty OutputStream.
     * @throws Exception if test fails
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamEmptyOutputStream() throws Exception {
        ArchiveStreamFactory.INSTANCE.createArchiveOutputStream("tar", new ByteArrayOutputStream());
    }

    /**
     * Test createArchiveInputStream method with a null InputStream.
     * @throws Exception if test fails
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullInputStream() throws Exception {
        ArchiveStreamFactory.INSTANCE.createArchiveInputStream("tar", null);
    }

    /**
     * Test createArchiveOutputStream method with a null OutputStream.
     * @throws Exception if test fails
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullOutputStream() throws Exception {
        ArchiveStreamFactory.INSTANCE.createArchiveOutputStream("tar", null);
    }

    /**
     * Helper method to retrieve test file bytes for a given archiver name.
     * @param archiverName the name of the archiver
     * @return the test file bytes
     */
    private byte[] getTestFileBytes(String archiverName) {
        switch (archiverName) {
            case ArchiveStreamFactory.AR:
                return new ArTestData().getArchiveBytes();
            case ArchiveStreamFactory.CPIO:
                return new CpioTestData().getCpioBytes();
            case ArchiveStreamFactory.DUMP:
                return new DumpTestData().getDumpBytes();
            case ArchiveStreamFactory.JAR:
                return new JarTestData().getJarBytes();
            case ArchiveStreamFactory.TAR:
                return new TarTestData().getTarBytes();
            case ArchiveStreamFactory.ZIP:
                return new ZipTestData().getZipBytes();
            default:
                throw new IllegalArgumentException("Unknown archiver name: " + archiverName);
        }
    }
}