package org.apache.commons.compress.archivers;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {
    
    private ArchiveStreamFactory archiveStreamFactory;

    @Before
    public void setUp() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test AR
        InputStream in1 = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais1 = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, in1);
        assertNotNull(ais1);
        
        // Test ZIP
        InputStream in2 = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais2 = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in2);
        assertNotNull(ais2);
        
        // Test TAR
        InputStream in3 = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais3 = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, in3);
        assertNotNull(ais3);
        
        // Test JAR
        InputStream in4 = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais4 = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, in4);
        assertNotNull(ais4);
        
        // Test CPIO
        InputStream in5 = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais5 = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in5);
        assertNotNull(ais5);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownArchiverName() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        archiveStreamFactory.createArchiveInputStream("unknownArchiver", in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        archiveStreamFactory.createArchiveInputStream(null, in);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }
    
    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test AR
        OutputStream out1 = new ByteArrayOutputStream();
        ArchiveOutputStream aos1 = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, out1);
        assertNotNull(aos1);
        
        // Test ZIP
        OutputStream out2 = new ByteArrayOutputStream();
        ArchiveOutputStream aos2 = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out2);
        assertNotNull(aos2);
        
        // Test TAR
        OutputStream out3 = new ByteArrayOutputStream();
        ArchiveOutputStream aos3 = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out3);
        assertNotNull(aos3);
        
        // Test JAR
        OutputStream out4 = new ByteArrayOutputStream();
        ArchiveOutputStream aos4 = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out4);
        assertNotNull(aos4);
        
        // Test CPIO
        OutputStream out5 = new ByteArrayOutputStream();
        ArchiveOutputStream aos5 = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out5);
        assertNotNull(aos5);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownArchiverName() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        archiveStreamFactory.createArchiveOutputStream("unknownArchiver", out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        archiveStreamFactory.createArchiveOutputStream(null, out);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }
    
    @Test
    public void testCreateArchiveInputStream_AutoDetect() throws Exception {
        // Test ZIP
        byte[] zipSignature = {0x50, 0x4b, 0x03, 0x04};
        InputStream in1 = new ByteArrayInputStream(zipSignature);
        ArchiveInputStream ais1 = archiveStreamFactory.createArchiveInputStream(in1);
        assertNotNull(ais1);
        
        // Test JAR
        byte[] jarSignature = {0x50, 0x4b, 0x03, 0x04};
        InputStream in2 = new ByteArrayInputStream(jarSignature);
        ArchiveInputStream ais2 = archiveStreamFactory.createArchiveInputStream(in2);
        assertNotNull(ais2);
        
        // Test AR
        byte[] arSignature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0xa};
        InputStream in3 = new ByteArrayInputStream(arSignature);
        ArchiveInputStream ais3 = archiveStreamFactory.createArchiveInputStream(in3);
        assertNotNull(ais3);
        
        // Test CPIO
        byte[] cpioSignature = {0x30, 0x37, 0x30, 0x37, 0x30, 0x30};
        InputStream in4 = new ByteArrayInputStream(cpioSignature);
        ArchiveInputStream ais4 = archiveStreamFactory.createArchiveInputStream(in4);
        assertNotNull(ais4);
        
        // Test Dump
        byte[] dumpSignature = {0x0, 0x0, 0x3e, 0x3e, 0x0, 0x0, 0x0, 0x0};
        InputStream in5 = new ByteArrayInputStream(dumpSignature);
        ArchiveInputStream ais5 = archiveStreamFactory.createArchiveInputStream(in5);
        assertNotNull(ais5);
        
        // Test TAR
        byte[] tarSignature = {0x75, 0x73, 0x74, 0x61, 0x72, 0x20, 0x20, 0x0};
        InputStream in6 = new ByteArrayInputStream(tarSignature);
        ArchiveInputStream ais6 = archiveStreamFactory.createArchiveInputStream(in6);
        assertNotNull(ais6);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AutoDetect_UnknownSignature() throws ArchiveException {
        byte[] unknownSignature = {0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};
        InputStream in = new ByteArrayInputStream(unknownSignature);
        archiveStreamFactory.createArchiveInputStream(in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_AutoDetect_NullInputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(null);
    }

}