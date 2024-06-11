package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Test cases for ArchiveStreamFactory class.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test creation of input streams.
     */
    @Test
    public void testCreateInput() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        // Test creating input streams for known formats
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0])));
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, new ByteArrayInputStream(new byte[0])));
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0])));
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0])));
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0])));
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0])));
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0])));
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, new ByteArrayInputStream(new byte[0])));

        // Test creating input stream with null input stream
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, null));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, null));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, null));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, null));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.JAR, null));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, null));

        // Test creating input stream for unsupported format
        ArchiveException e = null;
        try {
            factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
        } catch (ArchiveException ex) {
            e = ex;
        }
        assertNotNull(e);
    }

    /**
     * Test creation of output streams.
     */
    @Test
    public void testCreateOutput() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        // Test creating output streams for known formats
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiverOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertNotNull(archiverOutputStream);

        archiverOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, out);
        assertNotNull(archiverOutputStream);

        archiverOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        assertNotNull(archiverOutputStream);

        archiverOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, out);
        assertNotNull(archiverOutputStream);

        archiverOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        assertNotNull(archiverOutputStream);

        archiverOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertNotNull(archiverOutputStream);

        archiverOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertNotNull(archiverOutputStream);

        archiverOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, out);
        assertNotNull(archiverOutputStream);

        // Test creating output stream with null output stream
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null));
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, null));
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, null));
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, null));
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, null));
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null));
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null));
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, null));

        // Test creating output stream for unsupported format
        ArchiveException e = null;
        try {
            factory.createArchiveOutputStream("unknown", new ByteArrayOutputStream());
        } catch (ArchiveException ex) {
            e = ex;
        }
        assertNotNull(e);

        // Test creating archive entries
        if (ArchiveStreamFactory.TAR.equalsIgnoreCase("tar")) {
            TarArchiveOutputStream tarOutputStream = (TarArchiveOutputStream) factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
            TarEntry tarEntry = new TarEntry("test.txt");
            tarEntry.setSize(1024L);
            tarOutputStream.putNextEntry(tarEntry);
            tarOutputStream.closeArchiveEntry();
            tarOutputStream.close();
        }
    }

    /**
     * Test autodetection of archive format.
     */
    @Test
    public void testCreateInputFromSignature() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        input.mark(512);
        ArchiveInputStream archiveInput = factory.createArchiveInputStream(input);
        assertNotNull(archiveInput);
        if (archiveInput instanceof ArArchiveInputStream) {
            ArArchiveInputStream arInput = (ArArchiveInputStream) archiveInput;
            assertNull(arInput.getNextEntry());
        } else if (archiveInput instanceof JarArchiveInputStream) {
            JarArchiveInputStream jarInput = (JarArchiveInputStream) archiveInput;
            assertNotNull(jarInput.getNextJarEntry());
        } else if (archiveInput instanceof TarArchiveInputStream) {
            TarArchiveInputStream tarInput = (TarArchiveInputStream) archiveInput;
            TarEntry tarEntry = tarInput.getNextEntry();
            assertNotNull(tarEntry);
            assertNotNull(tarEntry.getName());
            archiveInput.close();
        } else {
            assertFalse(true);
        }
        input.close();
    }
}