package org.apache.commons.compress.archivers;

import static org.apache.commons.compress.utils.IOUtils.closeQuietly;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ar.ARArchiveEntry;
import org.apache.commons.compress.archivers.ar.ARArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for ArchiveStreamFactory class
 */
public class ArchiveStreamFactoryTest {

    private InputStream arInputStream;
    private OutputStream arOutputStream;

    @Before
    public void setUp() throws Exception {
        arInputStream = new ByteArrayInputStream(new byte[0]);
        arOutputStream = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws Exception {
        closeQuietly(arInputStream);
        closeQuietly(arOutputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithKnownFormat() throws Exception {
        // Ar Archive
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        assertSame(ArArchiveInputStream.class, archiveInputStream.getClass());

        // Cpio Archive
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, arInputStream);
        assertSame(CpioArchiveInputStream.class, archiveInputStream.getClass());

        // Jar Archive
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, arInputStream);
        assertSame(JarArchiveInputStream.class, archiveInputStream.getClass());

        // Tar Archive
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, arInputStream);
        assertSame(TarArchiveInputStream.class, archiveInputStream.getClass());

        // Zip Archive
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, arInputStream);
        assertSame(ZipArchiveInputStream.class, archiveInputStream.getClass());

        // SevenZ Archive
        assertThrows(StreamingNotSupportedException.class,
                () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, arInputStream));
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidFormat() throws Exception {
        // Invalid format
        assertThrows(ArchiveException.class,
                () -> new ArchiveStreamFactory().createArchiveInputStream("UnknownFormat", arInputStream));
    }

    @Test
    public void testCreateArchiveInputStreamWithDetectedFormat() throws Exception {
        // Create a mock AR archive stream
        ARArchiveOutputStream arOutputStreamMock = mock(ARArchiveOutputStream.class);
        when(arOutputStreamMock.getFormat()).thenReturn(ArchiveStreamFactory.AR);
        when(arOutputStreamMock.toByteArray()).thenReturn(new byte[0]);

        // Create input stream using ArchiveStreamFactory
        InputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream -> {
            if (arOutputStreamMock instanceof OutputStream) {
                return arOutputStreamMock;
            }
            throw new IOException();
        });

        // Check if the returned input stream is an ArArchiveInputStream
        assertSame(ArArchiveInputStream.class, inputStream.getClass());
    }

    @Test
    public void testCreateArchiveOutputStreamWithKnownFormat() throws Exception {
        // Ar Archive
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        assertSame(ARArchiveOutputStream.class, archiveOutputStream.getClass());

        // Cpio Archive
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, arOutputStream);
        assertSame(CpioArchiveOutputStream.class, archiveOutputStream.getClass());

        // Jar Archive
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, arOutputStream);
        assertSame(JarArchiveOutputStream.class, archiveOutputStream.getClass());

        // Tar Archive
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, arOutputStream);
        assertSame(TarArchiveOutputStream.class, archiveOutputStream.getClass());

        // Zip Archive
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, arOutputStream);
        assertSame(ZipArchiveOutputStream.class, archiveOutputStream.getClass());

        // SevenZ Archive
        assertThrows(UnsupportedOperationException.class,
                () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, arOutputStream));
    }

    @Test
    public void testCreateArchiveOutputStreamWithInvalidFormat() throws Exception {
        // Invalid format
        assertThrows(UnsupportedOperationException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream("UnknownFormat", arOutputStream));
    }

    @Test
    public void testCreateArchiveOutputStreamWithEntryEncoding() throws Exception {
        // Ar Archive with entry encoding
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory("UTF-8").createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        assertSame(ARArchiveOutputStream.class, archiveOutputStream.getClass());

        // Tar Archive with entry encoding
        archiveOutputStream = new ArchiveStreamFactory("UTF-8").createArchiveOutputStream(ArchiveStreamFactory.TAR, arOutputStream);
        assertSame(TarArchiveOutputStream.class, archiveOutputStream.getClass());

        // Jar Archive with entry encoding
        archiveOutputStream = new ArchiveStreamFactory("UTF-8").createArchiveOutputStream(ArchiveStreamFactory.JAR, arOutputStream);
        assertSame(JarArchiveOutputStream.class, archiveOutputStream.getClass());

        // Cpio Archive with entry encoding
        archiveOutputStream = new ArchiveStreamFactory("UTF-8").createArchiveOutputStream(ArchiveStreamFactory.CPIO, arOutputStream);
        assertSame(CpioArchiveOutputStream.class, archiveOutputStream.getClass());

        // Zip Archive with entry encoding
        archiveOutputStream = new ArchiveStreamFactory("UTF-8").createArchiveOutputStream(ArchiveStreamFactory.ZIP, arOutputStream);
        assertSame(ZipArchiveOutputStream.class, archiveOutputStream.getClass());

        // Set an entry
        JarArchiveOutputStream jarArchiveOutputStream = (JarArchiveOutputStream) archiveOutputStream;
        JarArchiveEntry jarArchiveEntry = new JarArchiveEntry("test.txt");
        jarArchiveOutputStream.putNextEntry(jarArchiveEntry);
        IOUtils.write("Test data.", jarArchiveOutputStream);
        jarArchiveOutputStream.closeArchiveEntry();

        // Read the entry content
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(jarArchiveOutputStream.toByteArray()));
        assertSame(JarArchiveEntry.class, archiveInputStream.getNextEntry());
        assertEquals("Test data.", IOUtils.toString(archiveInputStream));
        assertNull(archiveInputStream.getNextEntry());
        closeQuietly(archiveInputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithMixedFormats() throws Exception {
        // Create a mock AR archive entry
        ARArchiveEntry arArchiveEntry = mock(ARArchiveEntry.class);
        when(arArchiveEntry.getName()).thenReturn("test.txt");
        when(arArchiveEntry.getSize()).thenReturn(10);

        // Create a mock Cpio archive entry
        CpioArchiveEntry cpioArchiveEntry = mock(CpioArchiveEntry.class);
        when(cpioArchiveEntry.getName()).thenReturn("test2.txt");
        when(cpioArchiveEntry.getSize()).thenReturn(20);

        // Create ArchiveOutputStream using ArchiveStreamFactory
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(inputStream -> {
            if (arArchiveEntry instanceof ArchiveEntry) {
                return new ARArchiveOutputStream(arOutputStream);
            } else if (cpioArchiveEntry instanceof ArchiveEntry) {
                return new CpioArchiveOutputStream(arOutputStream);
            }
            throw new IOException();
        });

        // Set an entry
        ARArchiveEntry arArchiveEntry2 = mock(ARArchiveEntry.class);
        when(arArchiveEntry2.getName()).thenReturn("test3.txt");
        when(arArchiveEntry2.getSize()).thenReturn(30);

        JarArchiveOutputStream jarArchiveOutputStream = (JarArchiveOutputStream) archiveOutputStream;
        jarArchiveOutputStream.putNextEntry(new JarArchiveEntry(arArchiveEntry.getName()));
        IOUtils.write("Test data for AR entry.", jarArchiveOutputStream);
        jarArchiveOutputStream.closeArchiveEntry();

        jarArchiveOutputStream.putNextEntry(new JarArchiveEntry(arArchiveEntry2.getName()));
        IOUtils.write("Test data for AR entry 2.", jarArchiveOutputStream);
        jarArchiveOutputStream.closeArchiveEntry();

        // Set an entry for Cpio Archive
        CpioArchiveEntry cpioArchiveEntry2 = mock(CpioArchiveEntry.class);
        when(cpioArchiveEntry2.getName()).thenReturn("test4.txt");
        when(cpioArchiveEntry2.getSize()).thenReturn(40);

        jarArchiveOutputStream.putNextEntry(new JarArchiveEntry(cpioArchiveEntry.getName()));
        IOUtils.write("Test data for Cpio entry.", jarArchiveOutputStream);
        jarArchiveOutputStream.closeArchiveEntry();

        jarArchiveOutputStream.putNextEntry(new JarArchiveEntry(cpioArchiveEntry2.getName()));
        IOUtils.write("Test data for Cpio entry 2.", jarArchiveOutputStream);
        jarArchiveOutputStream.closeArchiveEntry();

        // Create input stream using ArchiveStreamFactory
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(jarArchiveOutputStream.toByteArray()));

        // Read AR entry
        ARArchiveEntry arArchiveEntry3 = (ARArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test.txt", arArchiveEntry3.getName());
        assertEquals(10, arArchiveEntry3.getSize());
        assertEquals("Test data.", IOUtils.toString(archiveInputStream));
        assertNull(archiveInputStream.getNextEntry());

        // Read AR entry 2
        ARArchiveEntry arArchiveEntry4 = (ARArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test3.txt", arArchiveEntry4.getName());
        assertEquals(30, arArchiveEntry4.getSize());
        assertEquals("Test data for AR entry 2.", IOUtils.toString(archiveInputStream));
        assertNull(archiveInputStream.getNextEntry());

        // Read Cpio entry
        CpioArchiveEntry cpioArchiveEntry3 = (CpioArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test2.txt", cpioArchiveEntry3.getName());
        assertEquals(20, cpioArchiveEntry3.getSize());
        assertEquals("Test data for Cpio entry.", IOUtils.toString(archiveInputStream));
        assertNull(archiveInputStream.getNextEntry());

        // Read Cpio entry 2
        CpioArchiveEntry cpioArchiveEntry4 = (CpioArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test4.txt", cpioArchiveEntry4.getName());
        assertEquals(40, cpioArchiveEntry4.getSize());
        assertEquals("Test data for Cpio entry 2.", IOUtils.toString(archiveInputStream));
        assertNull(archiveInputStream.getNextEntry());

        closeQuietly(archiveInputStream);
    }
}