package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.*;

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
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream; // assuming ARJ is supported
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test class for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    private static final String AR_ARCHIVE = "test-files/test.ar";
    private static final String ARJ_ARCHIVE = "test-files/test.arj"; // assuming ARJ is supported
    private static final String CPIO_ARCHIVE = "test-files/test.cpio";
    private static final String DUMP_ARCHIVE = "test-files/test.dmp";
    private static final String JAR_ARCHIVE = "test-files/test.jar";
    private static final String TAR_ARCHIVE = "test-files/test.tar";
    private static final String ZIP_ARCHIVE = "test-files/test.zip";

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        for (String archiverType : new String[] { ArchiveStreamFactory.AR, ArchiveStreamFactory.JAR,
                ArchiveStreamFactory.TAR, ArchiveStreamFactory.ZIP }) {
            try (InputStream inputStream = new FileInputStream(getArchiveFile(archiverType))) {
                ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverType, inputStream);
                assertNotNull(archiveInputStream);
                TarArchiveEntry tarEntry = (TarArchiveEntry) archiveInputStream.getNextEntry();
                assertNotNull(tarEntry);
                assertEquals(tarEntry.getName(), archiveInputStream.getCurrentEntry().getName());
                archiveInputStream.close();
            }
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        for (String archiverType : new String[] { ArchiveStreamFactory.AR, ArchiveStreamFactory.JAR,
                ArchiveStreamFactory.TAR, ArchiveStreamFactory.ZIP }) {
            try (OutputStream outputStream = new ByteArrayOutputStream()) {
                ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverType, outputStream);
                assertNotNull(archiveOutputStream);
                JarArchiveEntry jarEntry = new JarArchiveEntry("test.txt");
                archiveOutputStream.putNextEntry(jarEntry);
                IOUtils.write("Test content", archiveOutputStream.getOutputStream());
                archiveOutputStream.closeArchiveEntry();
                archiveOutputStream.finish();
                byte[] bytes = outputStream.toByteArray();
                InputStream testInputStream = new ByteArrayInputStream(bytes);
                ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverType, testInputStream);
                TarArchiveEntry tarEntry = (TarArchiveEntry) archiveInputStream.getNextEntry();
                assertNotNull(tarEntry);
                assertEquals(jarEntry.getName(), tarEntry.getName());
                String content = IOUtils.toString(archiveInputStream.getInputStream());
                assertEquals("Test content", content);
                archiveInputStream.close();
            }
        }
    }

    @Test(expected = ArchiveException.class)
    public void testUnknownArchiverType() throws Exception {
        String unknownType = "unknown";
        try (InputStream inputStream = new ByteArrayInputStream(new byte[0])) {
            ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
            archiveStreamFactory.createArchiveInputStream(unknownType, inputStream);
            fail("Unexpected success");
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws Exception {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        try {
            archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
            fail("Unexpected success");
        } catch (ArchiveException e) {
            assertEquals("Output stream cannot be null.", e.getMessage());
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws Exception {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        try {
            archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, null);
            fail("Unexpected success");
        } catch (ArchiveException e) {
            assertEquals("Input stream cannot be null.", e.getMessage());
        }
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateSevenzArchiveInputStream() throws Exception {
        String sevenzType = ArchiveStreamFactory.SEVEN_Z;
        try (InputStream inputStream = new ByteArrayInputStream(new byte[0])) {
            ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
            archiveStreamFactory.createArchiveInputStream(sevenzType, inputStream);
            fail("Unexpected success");
        }
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateSevenzArchiveOutputStream() throws Exception {
        String sevenzType = ArchiveStreamFactory.SEVEN_Z;
        try (OutputStream outputStream = new ByteArrayOutputStream()) {
            ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
            archiveStreamFactory.createArchiveOutputStream(sevenzType, outputStream);
            fail("Unexpected success");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithArj() throws Exception {
        // Assuming ARJ is supported
        try (InputStream inputStream = new FileInputStream(ARJ_ARCHIVE)) {
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ARJ, inputStream);
            assertNotNull(archiveInputStream);
            ArjArchiveEntry arjEntry = (ArjArchiveEntry) archiveInputStream.getNextEntry();
            assertNotNull(arjEntry);
            assertEquals(arjEntry.getName(), archiveInputStream.getCurrentEntry().getName());
            archiveInputStream.close();
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithArj() throws Exception {
        // Assuming ARJ is supported
        try (OutputStream outputStream = new ByteArrayOutputStream()) {
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.ARJ, outputStream);
            assertNotNull(archiveOutputStream);
            ArjArchiveEntry arjEntry = new ArjArchiveEntry("test.txt");
            archiveOutputStream.putNextEntry(arjEntry);
            IOUtils.write("Test content", archiveOutputStream.getOutputStream());
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
            byte[] bytes = outputStream.toByteArray();
            InputStream testInputStream = new ByteArrayInputStream(bytes);
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ARJ, testInputStream);
            ArjArchiveEntry arjTestEntry = (ArjArchiveEntry) archiveInputStream.getNextEntry();
            assertNotNull(arjTestEntry);
            assertEquals(arjEntry.getName(), arjTestEntry.getName());
            String content = IOUtils.toString(archiveInputStream.getInputStream());
            assertEquals("Test content", content);
            archiveInputStream.close();
        }
    }

    private File getArchiveFile(String archiverType) throws IOException {
        String fileName = "test-files/test-" + archiverType + ".arch";
        return new File(fileName);
    }

}