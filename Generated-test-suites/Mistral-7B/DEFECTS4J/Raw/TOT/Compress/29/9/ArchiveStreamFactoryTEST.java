package org.apache.commons.compress.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test class for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    private static final String TEST_DATA = "testData";
    private static final String TEST_DATA_INPUT = "testDataInput.bin";

    @org.junit.Test
    public void testCreateArchiveInputStream() throws Exception {
        String inputFile = TEST_DATA_INPUT;
        InputStream testStream = new ByteArrayInputStream(createTestArchiveBytes(ZipArchiveOutputStream.DEFAULT_COMPRESSION_LEVEL));

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, testStream);

        assertArchiveStream(archiveInputStream, ZipArchiveInputStream.class, inputFile, "testData.txt");

        archiveInputStream.close();
    }

    @org.junit.Test
    public void testCreateArchiveInputStream_invalidArchiveType() throws Exception {
        String inputFile = TEST_DATA_INPUT;
        InputStream testStream = new ByteArrayInputStream(createTestArchiveBytes(ZipArchiveOutputStream.DEFAULT_COMPRESSION_LEVEL));

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream("invalidArchiveType", testStream);

        assertNull(archiveInputStream);
    }

    @org.junit.Test
    public void testCreateArchiveInputStream_invalidInputStream() throws Exception {
        InputStream invalidStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };

        ArchiveInputStream archiveInputStream = assertThrows(IllegalArgumentException.class,
                () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, invalidStream));

        assertNull(archiveInputStream);
    }

    @org.junit.Test
    public void testCreateArchiveOutputStream() throws Exception {
        String outputFile = TEST_DATA + "/testData.tar.gz";
        OutputStream outputStream = new ByteArrayOutputStream();

        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR_GZ, outputStream);

        archiveOutputStream.putNextEntry(new TarArchiveEntry("testData.txt"));
        archiveOutputStream.write("testData".getBytes());
        archiveOutputStream.finish();

        byte[] expectedArchive = createTestArchiveBytes(TarArchiveOutputStream.DEFAULT_COMPRESSION_LEVEL);
        byte[] archiveBytes = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertEquals(expectedArchive, archiveBytes);

        archiveOutputStream.close();
    }

    @org.junit.Test
    public void testCreateArchiveOutputStream_invalidArchiveType() throws Exception {
        String outputFile = TEST_DATA + "/testData.arj";
        OutputStream outputStream = new ByteArrayOutputStream();

        ArchiveOutputStream archiveOutputStream = assertThrows(ArchiveException.class,
                () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ARJ, outputStream));

        assertNull(archiveOutputStream);
    }

    @org.junit.Test
    public void testCreateArchiveOutputStream_invalidOutputStream() throws Exception {
        OutputStream invalidStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        };

        ArchiveOutputStream archiveOutputStream = assertThrows(IllegalArgumentException.class,
                () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, invalidStream));

        assertNull(archiveOutputStream);
    }

    @org.junit.Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        String inputFile = TEST_DATA_INPUT;
        InputStream testStream = new ByteArrayInputStream(createTestArchiveBytes(ZipArchiveOutputStream.DEFAULT_COMPRESSION_LEVEL));

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(testStream);

        assertArchiveStream(archiveInputStream, ZipArchiveInputStream.class, inputFile, "testData.txt");

        archiveInputStream.close();
    }

    private void assertArchiveStream(ArchiveInputStream archiveInputStream, Class<? extends ArchiveInputStream> expectedClass, String inputFile, String expectedEntryName) throws Exception {
        assertEquals(expectedClass, archiveInputStream.getClass());
        assertEquals(inputFile, archiveInputStream.getInputStream().toString());

        ArchiveEntry entry = archiveInputStream.getNextEntry();
        assertEquals(expectedEntryName, entry.getName());
        assertEquals("testData", IOUtils.toString(archiveInputStream));
        archiveInputStream.closeEntry();

        assertNull(archiveInputStream.getNextEntry());
        archiveInputStream.close();
    }

    private byte[] createTestArchiveBytes(int compressionLevel) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        archiveOutputStream.setLevel(compressionLevel);
        archiveOutputStream.putArchiveEntry(new JarArchiveEntry("testData.txt"));
        archiveOutputStream.write("testData".getBytes());
        archiveOutputStream.finish();
        archiveOutputStream.close();

        return ((ByteArrayOutputStream) outputStream).toByteArray();
    }
}