
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR archive format
        InputStream in1 = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais1 = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in1);
        assertEquals(ArArchiveInputStream.class, ais1.getClass());
        
        // Test ZIP archive format
        InputStream in2 = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais2 = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in2);
        assertEquals(ZipArchiveInputStream.class, ais2.getClass());
        
        // Test TAR archive format
        InputStream in3 = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais3 = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in3);
        assertEquals(TarArchiveInputStream.class, ais3.getClass());
        
        // Test JAR archive format
        InputStream in4 = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais4 = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in4);
        assertEquals(JarArchiveInputStream.class, ais4.getClass());
        
        // Test CPIO archive format
        InputStream in5 = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais5 = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in5);
        assertEquals(CpioArchiveInputStream.class, ais5.getClass());
        
        // Test DUMP archive format
        InputStream in6 = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais6 = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in6);
        assertEquals(DumpArchiveInputStream.class, ais6.getClass());
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR archive format
        OutputStream out1 = new ByteArrayOutputStream();
        ArchiveOutputStream aos1 = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out1);
        assertEquals(ArArchiveOutputStream.class, aos1.getClass());
        
        // Test ZIP archive format
        OutputStream out2 = new ByteArrayOutputStream();
        ArchiveOutputStream aos2 = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out2);
        assertEquals(ZipArchiveOutputStream.class, aos2.getClass());
        
        // Test TAR archive format
        OutputStream out3 = new ByteArrayOutputStream();
        ArchiveOutputStream aos3 = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out3);
        assertEquals(TarArchiveOutputStream.class, aos3.getClass());
        
        // Test JAR archive format
        OutputStream out4 = new ByteArrayOutputStream();
        ArchiveOutputStream aos4 = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out4);
        assertEquals(JarArchiveOutputStream.class, aos4.getClass());
        
        // Test CPIO archive format
        OutputStream out5 = new ByteArrayOutputStream();
        ArchiveOutputStream aos5 = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out5);
        assertEquals(CpioArchiveOutputStream.class, aos5.getClass());
    }

    @Test
    public void testCreateArchiveInputStream_autoDetect() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test ZIP archive format
        byte[] zipData = new byte[] { 80, 75, 3, 4 };
        InputStream in1 = new ByteArrayInputStream(zipData);
        ArchiveInputStream ais1 = factory.createArchiveInputStream(in1);
        assertEquals(ZipArchiveInputStream.class, ais1.getClass());
        
        // Test JAR archive format
        byte[] jarData = new byte[] { 80, 75, 3, 4 };
        InputStream in2 = new ByteArrayInputStream(jarData);
        ArchiveInputStream ais2 = factory.createArchiveInputStream(in2);
        assertEquals(JarArchiveInputStream.class, ais2.getClass());
        
        // Test AR archive format
        byte[] arData = new byte[8];
        InputStream in3 = new ByteArrayInputStream(arData);
        ArchiveInputStream ais3 = factory.createArchiveInputStream(in3);
        assertEquals(ArArchiveInputStream.class, ais3.getClass());
        
        // Test CPIO archive format
        byte[] cpioData = new byte[8];
        InputStream in4 = new ByteArrayInputStream(cpioData);
        ArchiveInputStream ais4 = factory.createArchiveInputStream(in4);
        assertEquals(CpioArchiveInputStream.class, ais4.getClass());
        
        // Test DUMP archive format
        byte[] dumpData = new byte[32];
        InputStream in5 = new ByteArrayInputStream(dumpData);
        ArchiveInputStream ais5 = factory.createArchiveInputStream(in5);
        assertEquals(DumpArchiveInputStream.class, ais5.getClass());
        
        // Test TAR archive format
        byte[] tarData = new byte[512];
        InputStream in6 = new ByteArrayInputStream(tarData);
        ArchiveInputStream ais6 = factory.createArchiveInputStream(in6);
        assertEquals(TarArchiveInputStream.class, ais6.getClass());
    }
}
