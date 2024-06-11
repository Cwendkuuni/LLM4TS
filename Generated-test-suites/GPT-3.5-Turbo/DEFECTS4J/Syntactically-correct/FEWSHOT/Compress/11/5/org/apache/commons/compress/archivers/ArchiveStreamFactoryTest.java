package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class ArchiveStreamFactoryTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_ArchiverNameNull() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, in);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_InputStreamNull() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_ArchiverNameNull() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, out);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_OutputStreamNull() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_StreamNull() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_StreamNoMarkSupport() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(in);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream("unknown", in);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("unknown", out);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_NoArchiverFound() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(in);
    }
    
    @Test
    public void testCreateArchiveInputStream_AR() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = generateArchiveData(ArchiveStreamFactory.AR);
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertNotNull(ais);
        assertTrue(ais instanceof ArArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_ZIP() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = generateArchiveData(ArchiveStreamFactory.ZIP);
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(ais);
        assertTrue(ais instanceof ZipArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_AR() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertNotNull(aos);
        assertTrue(aos instanceof ArArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_ZIP() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertNotNull(aos);
        assertTrue(aos instanceof ZipArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_AutoDetect_ZIP() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = generateArchiveData(ArchiveStreamFactory.ZIP);
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        assertNotNull(ais);
        assertTrue(ais instanceof ZipArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_AutoDetect_JAR() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = generateArchiveData(ArchiveStreamFactory.JAR);
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        assertNotNull(ais);
        assertTrue(ais instanceof JarArchiveInputStream);
    }
    
    private byte[] generateArchiveData(String archiverName) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = null;
        
        if (ArchiveStreamFactory.AR.equalsIgnoreCase(archiverName)) {
            aos = new ArArchiveOutputStream(out);
        } else if (ArchiveStreamFactory.ZIP.equalsIgnoreCase(archiverName)) {
            aos = new ZipArchiveOutputStream(out);
        }
        
        assertNotNull(aos);
        
        aos.finish();
        return out.toByteArray();
    }
}