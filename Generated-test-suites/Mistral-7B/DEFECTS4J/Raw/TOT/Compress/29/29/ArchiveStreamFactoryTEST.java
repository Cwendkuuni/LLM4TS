package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.junit.ArchiveTestUtils;
import org.apache.commons.compress.utils.IOUtils;

import org.junit.Test;

/**
 * Test for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test creation of input streams for known archive types.
     */
    @Test
    public void testCreateArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test creation from known archive names and byte arrays
        final byte[] arBytes = ArchiveTestUtils.getTestFile("test-ar.tar.gz")
                .readAllBytes();

        final InputStream arStream = new ByteArrayInputStream(arBytes);
        final ArchiveInputStream arArchive = factory.createArchiveInputStream(
                ArchiveStreamFactory.AR, arStream);

        ArArchiveEntry entry = (ArArchiveEntry) arArchive.getNextEntry();
        assertEquals("test-ar.tar/test-ar.tar/test-ar.gz", entry.getName());
        assertTrue(arArchive.getNextEntry() == null);
        arArchive.close();

        // Test creation from known archive names and file streams
        final InputStream arFile = ArchiveTestUtils.getTestFile("test-ar.tar.gz")
                .getInputStream();
        final ArchiveInputStream arArchive2 = factory.createArchiveInputStream(
                ArchiveStreamFactory.AR, arFile);
        assertEquals(arArchive.getNextEntry().getName(), arArchive2.getNextEntry()
                .getName());
        arArchive2.close();
        arFile.close();

        // Test creation from known archive names and memory streams
        final byte[] jarBytes = ArchiveTestUtils.getTestFile("test-jar.jar")
                .readAllBytes();
        final InputStream jarStream = new ByteArrayInputStream(jarBytes);
        final OutputStream jarOutputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream jarArchive = factory.createArchiveOutputStream(
                ArchiveStreamFactory.JAR, jarOutputStream);
        jarArchive.putNextEntry(new JarArchiveEntry("test-jar.jar!/test.txt"));
        jarArchive.write("Hello World!".getBytes(), 0, "Hello World!".length());
        jarArchive.closeArchiveEntry();
        jarArchive.finish();
        final InputStream jarStream2 = new ByteArrayInputStream(
                jarOutputStream.toByteArray());
        final ArchiveInputStream jarArchive2 = factory.createArchiveInputStream(
                ArchiveStreamFactory.JAR, jarStream2);
        JarArchiveEntry entry2 = (JarArchiveEntry) jarArchive2.getNextEntry();
        assertEquals("test-jar.jar!/test.txt", entry2.getName());
        assertEquals("Hello World!", IOUtils.toString(jarArchive2));
        jarArchive2.close();
        jarStream2.close();
        jarOutputStream.close();
    }

    /**
     * Test creation of output streams for known archive types.
     */
    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        final OutputStream output = new ByteArrayOutputStream();

        // Test creation to known archive types
        final ArchiveOutputStream arArchive = factory.createArchiveOutputStream(
                ArchiveStreamFactory.AR, output);
        arArchive.putNextEntry(new ArArchiveEntry("test.txt"));
        arArchive.write("Hello World!".getBytes(), 0, "Hello World!".length());
        arArchive.closeArchiveEntry();
        arArchive.finish();
        final byte[] arBytes = output.toByteArray();
        final InputStream arInputStream = new ByteArrayInputStream(arBytes);
        final ArchiveInputStream arArchive2 = factory.createArchiveInputStream(
                ArchiveStreamFactory.AR, arInputStream);
        ArArchiveEntry entry = (ArArchiveEntry) arArchive2.getNextEntry();
        assertEquals("test.txt", entry.getName());
        assertTrue(arArchive2.getNextEntry() == null);
        arArchive2.close();
        arInputStream.close();

        final OutputStream zipOutputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream zipArchive = factory.createArchiveOutputStream(
                ArchiveStreamFactory.ZIP, zipOutputStream);
        zipArchive.putNextEntry(new ZipArchiveEntry("test.txt"));
        zipArchive.write("Hello World!".getBytes(), 0, "Hello World!".length());
        zipArchive.closeArchiveEntry();
        zipArchive.finish();
        final byte[] zipBytes = zipOutputStream.toByteArray();
        final InputStream zipInputStream = new ByteArrayInputStream(zipBytes);
        final ArchiveInputStream zipArchive2 = factory.createArchiveInputStream(
                ArchiveStreamFactory.ZIP, zipInputStream);
        JarArchiveEntry entry2 = (JarArchiveEntry) zipArchive2.getNextEntry();
        assertEquals("test.txt", entry2.getName());
        assertTrue(zipArchive2.getNextEntry() == null);
        zipArchive2.close();
        zipInputStream.close();

        final OutputStream jarOutputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream jarArchive = factory.createArchiveOutputStream(
                ArchiveStreamFactory.JAR, jarOutputStream);
        jarArchive.putNextEntry(new JarArchiveEntry("test.txt"));
        jarArchive.write("Hello World!".getBytes(), 0, "Hello World!".length());
        jarArchive.closeArchiveEntry();
        jarArchive.finish();
        final byte[] jarBytes = jarOutputStream.toByteArray();
        final InputStream jarInputStream = new ByteArrayInputStream(jarBytes);
        final ArchiveInputStream jarArchive2 = factory.createArchiveInputStream(
                ArchiveStreamFactory.JAR, jarInputStream);
        JarArchiveEntry entry3 = (JarArchiveEntry) jarArchive2.getNextEntry();
        assertEquals("test.txt", entry3.getName());
        assertTrue(jarArchive2.getNextEntry() == null);
        jarArchive2.close();
        jarInputStream.close();
        jarOutputStream.close();
    }

    /**
     * Test creation of input streams from unknown archive types (detected from
     * file extension).
     */
    @Test
    public void testCreateArchiveInputStreamFromUnknownType() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test creation from unknown archive name and file stream
        final String testFile = "test-unknown.ext";
        final InputStream unknownStream = ArchiveTestUtils.getTestFile(testFile)
                .getInputStream();
        try {
            factory.createArchiveInputStream(testFile, unknownStream);
            fail("Expected ArchiveException for unknown archive type.");
        } catch (ArchiveException e) {
            // Expected
        } finally {
            unknownStream.close();
        }
    }

    /**
     * Test creation of input streams from unknown archive types (detected from
     * byte array).
     */
    @Test
    public void testCreateArchiveInputStreamFromUnknownTypeBytes() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test creation from unknown archive name and byte array
        final byte[] unknownBytes = new byte[1024];
        final InputStream unknownStream = new ByteArrayInputStream(unknownBytes);
        try {
            factory.createArchiveInputStream(null, unknownStream);
            fail("Expected ArchiveException for unknown archive type.");
        } catch (ArchiveException e) {
            // Expected
        } finally {
            unknownStream.close();
        }
    }

    /**
     * Test creation of output streams for unsupported archive types.
     */
    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveOutputStreamForUnsupportedType() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        final OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, out);
    }
}