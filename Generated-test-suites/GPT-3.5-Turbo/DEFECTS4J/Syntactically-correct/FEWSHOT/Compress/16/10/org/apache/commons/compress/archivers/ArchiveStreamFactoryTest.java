package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test AR archive format
        InputStream inAr = new ByteArrayInputStream("Test AR Archive".getBytes());
        ArchiveInputStream ar = factory.createArchiveInputStream(ArchiveStreamFactory.AR, inAr);
        assertNotNull(ar);
        inAr.close();

        // Test ZIP archive format
        InputStream inZip = new ByteArrayInputStream("Test ZIP Archive".getBytes());
        ArchiveInputStream zip = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inZip);
        assertNotNull(zip);
        inZip.close();

        // Test TAR archive format
        InputStream inTar = new ByteArrayInputStream("Test TAR Archive".getBytes());
        ArchiveInputStream tar = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, inTar);
        assertNotNull(tar);
        inTar.close();

        // Test JAR archive format
        InputStream inJar = new ByteArrayInputStream("Test JAR Archive".getBytes());
        ArchiveInputStream jar = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, inJar);
        assertNotNull(jar);
        inJar.close();

        // Test CPIO archive format
        InputStream inCpio = new ByteArrayInputStream("Test CPIO Archive".getBytes());
        ArchiveInputStream cpio = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inCpio);
        assertNotNull(cpio);
        inCpio.close();

        // Test DUMP archive format
        InputStream inDump = new ByteArrayInputStream("Test DUMP Archive".getBytes());
        ArchiveInputStream dump = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, inDump);
        assertNotNull(dump);
        inDump.close();

        // Test unknown archive format
        InputStream inUnknown = new ByteArrayInputStream("Test Unknown Archive".getBytes());
        try {
            factory.createArchiveInputStream("unknown", inUnknown);
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
        inUnknown.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test AR archive format
        OutputStream outAr = new ByteArrayOutputStream();
        ArchiveOutputStream ar = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, outAr);
        assertNotNull(ar);
        outAr.close();

        // Test ZIP archive format
        OutputStream outZip = new ByteArrayOutputStream();
        ArchiveOutputStream zip = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outZip);
        assertNotNull(zip);
        outZip.close();

        // Test TAR archive format
        OutputStream outTar = new ByteArrayOutputStream();
        ArchiveOutputStream tar = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outTar);
        assertNotNull(tar);
        outTar.close();

        // Test JAR archive format
        OutputStream outJar = new ByteArrayOutputStream();
        ArchiveOutputStream jar = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outJar);
        assertNotNull(jar);
        outJar.close();

        // Test CPIO archive format
        OutputStream outCpio = new ByteArrayOutputStream();
        ArchiveOutputStream cpio = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, outCpio);
        assertNotNull(cpio);
        outCpio.close();

        // Test unknown archive format
        OutputStream outUnknown = new ByteArrayOutputStream();
        try {
            factory.createArchiveOutputStream("unknown", outUnknown);
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
        outUnknown.close();
    }

    @Test
    public void testCreateArchiveInputStreamWithAutodetection() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test ZIP archive format
        InputStream inZip = new ByteArrayInputStream("Test ZIP Archive".getBytes());
        ArchiveInputStream zip = factory.createArchiveInputStream(inZip);
        assertNotNull(zip);
        inZip.close();

        // Test JAR archive format
        InputStream inJar = new ByteArrayInputStream("Test JAR Archive".getBytes());
        ArchiveInputStream jar = factory.createArchiveInputStream(inJar);
        assertNotNull(jar);
        inJar.close();

        // Test AR archive format
        InputStream inAr = new ByteArrayInputStream("Test AR Archive".getBytes());
        ArchiveInputStream ar = factory.createArchiveInputStream(inAr);
        assertNotNull(ar);
        inAr.close();

        // Test CPIO archive format
        InputStream inCpio = new ByteArrayInputStream("Test CPIO Archive".getBytes());
        ArchiveInputStream cpio = factory.createArchiveInputStream(inCpio);
        assertNotNull(cpio);
        inCpio.close();

        // Test DUMP archive format
        InputStream inDump = new ByteArrayInputStream("Test DUMP Archive".getBytes());
        ArchiveInputStream dump = factory.createArchiveInputStream(inDump);
        assertNotNull(dump);
        inDump.close();

        // Test TAR archive format
        InputStream inTar = new ByteArrayInputStream("Test TAR Archive".getBytes());
        ArchiveInputStream tar = factory.createArchiveInputStream(inTar);
        assertNotNull(tar);
        inTar.close();

        // Test unknown archive format
        InputStream inUnknown = new ByteArrayInputStream("Test Unknown Archive".getBytes());
        try {
            factory.createArchiveInputStream(inUnknown);
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
        inUnknown.close();
    }
}