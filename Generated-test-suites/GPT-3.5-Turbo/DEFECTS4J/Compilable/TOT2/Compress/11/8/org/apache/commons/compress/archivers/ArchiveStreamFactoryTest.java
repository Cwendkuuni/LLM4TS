package org.apache.commons.compress.archivers;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ar() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, in));
    }

    @Test
    public void testCreateArchiveInputStream_zip() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in));
    }

    @Test
    public void testCreateArchiveInputStream_tar() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in));
    }

    @Test
    public void testCreateArchiveInputStream_jar() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in));
    }

    @Test
    public void testCreateArchiveInputStream_cpio() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in));
    }

    @Test
    public void testCreateArchiveOutputStream_ar() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out));
    }

    @Test
    public void testCreateArchiveOutputStream_zip() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out));
    }

    @Test
    public void testCreateArchiveOutputStream_tar() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out));
    }

    @Test
    public void testCreateArchiveOutputStream_jar() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out));
    }

    @Test
    public void testCreateArchiveOutputStream_cpio() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out));
    }

    @Test
    public void testCreateArchiveInputStream_autoDetect() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        assertNotNull(factory.createArchiveInputStream(in));
    }
}