package org.apache.commons.compress.archivers;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            InputStream in = new ByteArrayInputStream(new byte[10]);
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            Assert.assertNotNull(archiveInputStream);
        } catch (Exception e) {
            Assert.fail("Should not throw Exception");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        InputStream in = new ByteArrayInputStream(new byte[10]);
        factory.createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        InputStream in = new ByteArrayInputStream(new byte[10]);
        factory.createArchiveInputStream("unknown", in);
    }

    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            Assert.assertNotNull(archiveOutputStream);
        } catch (Exception e) {
            Assert.fail("Should not throw Exception");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("unknown", out);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            InputStream in = new ByteArrayInputStream("Test".getBytes());
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(in);
            Assert.assertNotNull(archiveInputStream);
        } catch (Exception e) {
            Assert.fail("Should not throw Exception");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_AutoDetect_NullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        factory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_AutoDetect_InputStreamDoesNotSupportMark() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        InputStream in = new ByteArrayInputStream(new byte[10]);
        factory.createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AutoDetect_NoArchiverFound() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        byte[] bytes = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B};
        InputStream in = new ByteArrayInputStream(bytes);
        factory.createArchiveInputStream(in);
    }
}