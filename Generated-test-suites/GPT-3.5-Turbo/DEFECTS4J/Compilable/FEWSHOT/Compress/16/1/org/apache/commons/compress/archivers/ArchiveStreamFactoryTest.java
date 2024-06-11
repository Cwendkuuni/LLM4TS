package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.io.ByteArrayInputStream;

public class ArchiveStreamFactoryTest {
    @Test
    public void testCreateArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0])));
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0])));
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0])));
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0])));
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0])));
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0])));
        
        assertNull(factory.createArchiveInputStream("invalid", new ByteArrayInputStream(new byte[0])));
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.AR, System.out));
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, System.out));
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, System.out));
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, System.out));
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, System.out));
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, System.out));
        
        assertNull(factory.createArchiveOutputStream("invalid", System.out));
    }

    @Test
    public void testCreateArchiveInputStreamWithAutodetect() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        assertNotNull(factory.createArchiveInputStream(new ByteArrayInputStream(new byte[512])));
        
        assertNull(factory.createArchiveInputStream(null));
        assertNull(factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0])));
    }
}