package org.apache.commons.compress.archivers.tests;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
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
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Test the ArchiveStreamFactory class.
 */
public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStreamFromName() {
        for (String archiverName : ArchiveStreamFactory.values()) {
            try {
                ArchiveInputStream inputStream = factory.createArchiveInputStream(archiverName, new ByteArrayInputStream(new byte[0]));
                assertNotNull(inputStream);
                inputStream.close();
            } catch (ArchiveException e) {
                // This test is not checking for the correct archiver name,
                // just for the absence of an ArchiveException
            }
        }
    }

    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() {
        for (String archiverName : ArchiveStreamFactory.values()) {
            try {
                OutputStream outputStream = new ByteArrayOutputStream();
                ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiverName, outputStream);
                assertNotNull(archiveOutputStream);
                archiveOutputStream.finish();
                assertNotNull(archiveOutputStream.getArchiveEntry());
                archiveOutputStream.close();
            } catch (ArchiveException e) {
                // This test is not checking for the correct archiver name,
                // just for the absence of an ArchiveException
            }
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromUnknownName() {
        factory.createArchiveInputStream("XYZ", new ByteArrayInputStream(new byte[0]));
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamFromUnknownName() {
        factory.createArchiveOutputStream("XYZ", new ByteArrayOutputStream());
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveOutputStreamFromSevenZName() {
        factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, new ByteArrayOutputStream());
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() {
        // Test ZIP
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        archiveOutputStream.putArchiveEntry(new JarArchiveEntry("test.txt"));
        archiveOutputStream.write(new byte[]{'T', 'e', 's', 't'}, 0, 4);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        InputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(outputStream.toByteArray()));
        JarArchiveEntry jarEntry = (JarArchiveEntry) inputStream.getNextJarEntry();
        assertNotNull(jarEntry);
        assertEquals("test.txt", jarEntry.getName());
        assertNotNull(inputStream.readAllBytes());
        inputStream.close();

        // Test TAR
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        archiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        archiveOutputStream.write(new byte[]{'T', 'e', 's', 't'}, 0, 4);
        archiveOutputStream.finishEntry();
        archiveOutputStream.close();

        inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(outputStream.toByteArray()));
        TarArchiveEntry tarEntry = (TarArchiveEntry) inputStream.getNextEntry();
        assertNotNull(tarEntry);
        assertEquals("test.txt", tarEntry.getName());
        assertNotNull(inputStream.readAllBytes());
        inputStream.close();

        // Test AR
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        archiveOutputStream.putNextEntry(new ArArchiveEntry("test.txt"));
        archiveOutputStream.write(new byte[]{'T', 'e', 's', 't'}, 0, 4);
        archiveOutputStream.finishEntry();
        archiveOutputStream.close();

        inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(outputStream.toByteArray()));
        ArArchiveEntry arEntry = (ArArchiveEntry) inputStream.getNextEntry();
        assertNotNull(arEntry);
        assertEquals("test.txt", arEntry.getName());
        assertNotNull(inputStream.readAllBytes());
        inputStream.close();

        // Test JAR
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        JarArchiveEntry jarEntry = new JarArchiveEntry("test.txt");
        archiveOutputStream.putNextEntry(jarEntry);
        archiveOutputStream.write(new byte[]{'T', 'e', 's', 't'}, 0, 4);
        archiveOutputStream.finishEntry();
        archiveOutputStream.close();

        inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(outputStream.toByteArray()));
        assertNotNull(inputStream.getNextJarEntry());
        inputStream.close();

        // Test CPIO
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        archiveOutputStream.putNextEntry(new CpioArchiveEntry("test.txt"));
        archiveOutputStream.write(new byte[]{'T', 'e', 's', 't'}, 0, 4);
        archiveOutputStream.finishEntry();
        archiveOutputStream.close();

        inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(outputStream.toByteArray()));
        CpioArchiveEntry cpioEntry = (CpioArchiveEntry) inputStream.getNextEntry();
        assertNotNull(cpioEntry);
        assertEquals("test.txt", cpioEntry.getName());
        assertNotNull(inputStream.readAllBytes());
        inputStream.close();

        // Test ARJ
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, outputStream);
        archiveOutputStream.close(); // No need to write any data for ARJ

        inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, new ByteArrayInputStream(outputStream.toByteArray()));
        assertNull(inputStream.getNextEntry());
        inputStream.close();

        // Test DUMP
        outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream dumpInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
        archiveOutputStream.putNextEntry(new DumpArchiveEntry("test.txt"));
        archiveOutputStream.writeTo(dumpInputStream);
        archiveOutputStream.finishEntry();
        archiveOutputStream.close();

        inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(outputStream.toByteArray()));
        DumpArchiveEntry dumpEntry = (DumpArchiveEntry) inputStream.getNextEntry();
        assertNotNull(dumpEntry);
        assertEquals("test.txt", dumpEntry.getName());
        assertNotNull(inputStream.readAllBytes());
        inputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamWithEntryEncoding() {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream, "UTF-8");
        archiveOutputStream.putArchiveEntry(new JarArchiveEntry("test.txt"));
        archiveOutputStream.write(new byte[]{'T', 'e', 's', 't'}, 0, 4);
        archiveOutputStream.finishEntry();
        archiveOutputStream.finish();
        archiveOutputStream.close();

        InputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(outputStream.toByteArray()));
        JarArchiveEntry jarEntry = (JarArchiveEntry) inputStream.getNextJarEntry();
        byte[] actualBytes = IOUtils.toByteArray(inputStream);
        inputStream.close();
        String expectedString = new String(new byte[]{'T', 'e', 's', 't'}, 0, 4, "UTF-8");
        assertEquals(expectedString, new String(actualBytes, "UTF-8"));
    }
}