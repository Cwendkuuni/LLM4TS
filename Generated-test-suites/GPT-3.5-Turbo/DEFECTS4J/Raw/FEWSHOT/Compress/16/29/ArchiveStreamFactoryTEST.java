
package org.apache.commons.compress.archivers;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;

public class ArchiveStreamFactoryTest {
    
    @Test
    public void testCreateArchiveInputStream_Ar() throws Exception {
        InputStream in = new FileInputStream("test.ar");
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
        
        assertNotNull(ais);
        
        in.close();
        ais.close();
    }
    
    @Test
    public void testCreateArchiveInputStream_Zip() throws Exception {
        InputStream in = new FileInputStream("test.zip");
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        
        assertNotNull(ais);
        
        in.close();
        ais.close();
    }
    
    @Test
    public void testCreateArchiveInputStream_Tar() throws Exception {
        InputStream in = new FileInputStream("test.tar");
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        
        assertNotNull(ais);
        
        in.close();
        ais.close();
    }
    
    @Test
    public void testCreateArchiveInputStream_Jar() throws Exception {
        InputStream in = new FileInputStream("test.jar");
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        
        assertNotNull(ais);
        
        in.close();
        ais.close();
    }
    
    @Test
    public void testCreateArchiveInputStream_Cpio() throws Exception {
        InputStream in = new FileInputStream("test.cpio");
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        
        assertNotNull(ais);
        
        in.close();
        ais.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Ar() throws Exception {
        OutputStream out = new FileOutputStream("testOutput.ar");
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        
        assertNotNull(aos);
        
        out.close();
        aos.close();
    }
    
    @Test
    public void testCreateArchiveOutputStream_Zip() throws Exception {
        OutputStream out = new FileOutputStream("testOutput.zip");
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        
        assertNotNull(aos);
        
        out.close();
        aos.close();
    }
    
    @Test
    public void testCreateArchiveOutputStream_Tar() throws Exception {
        OutputStream out = new FileOutputStream("testOutput.tar");
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        
        assertNotNull(aos);
        
        out.close();
        aos.close();
    }
    
    @Test
    public void testCreateArchiveOutputStream_Jar() throws Exception {
        OutputStream out = new FileOutputStream("testOutput.jar");
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        
        assertNotNull(aos);
        
        out.close();
        aos.close();
    }
    
    @Test
    public void testCreateArchiveOutputStream_Cpio() throws Exception {
        OutputStream out = new FileOutputStream("testOutput.cpio");
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        
        assertNotNull(aos);
        
        out.close();
        aos.close();
    }
    
    @Test
    public void testCreateArchiveInputStream_AutoDetect() throws Exception {
        InputStream in = new FileInputStream("test.auto");
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        
        assertNotNull(ais);
        
        in.close();
        ais.close();
    }
}
