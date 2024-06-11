import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

public class ArchiveStreamFactoryTest {
    
    private ArchiveStreamFactory archiveStreamFactory;
    
    @Before
    public void setUp() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }
    
    @After
    public void tearDown() {
        archiveStreamFactory = null;
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream("zip", null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream(null, new ByteArrayOutputStream());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream("zip", null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullStream() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithUnsupportedStream() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        });
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithInvalidArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream("invalid", new ByteArrayInputStream(new byte[0]));
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithInvalidArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream("invalid", new ByteArrayOutputStream());
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownSignature() throws ArchiveException, IOException {
        byte[] data = new byte[10];
        InputStream in = new ByteArrayInputStream(data);
        
        archiveStreamFactory.createArchiveInputStream(in);
    }
    
    @Test
    public void testCreateArchiveInputStreamWithZipSignature() throws ArchiveException, IOException {
        byte[] data = new byte[4];
        System.arraycopy(new byte[] {0x50, 0x4B, 0x03, 0x04}, 0, data, 0, 4);
        InputStream in = new ByteArrayInputStream(data);
        
        Assert.assertNotNull(archiveStreamFactory.createArchiveInputStream(in));
    }
    
    @Test
    public void testCreateArchiveInputStreamWithJarSignature() throws ArchiveException, IOException {
        byte[] data = new byte[8];
        System.arraycopy(new byte[] {0x50, 0x4B, 0x03, 0x04, 0x0A, 0x0, 0x0, 0x0}, 0, data, 0, 8);
        InputStream in = new ByteArrayInputStream(data);
        
        Assert.assertNotNull(archiveStreamFactory.createArchiveInputStream(in));
    }
    
    @Test
    public void testCreateArchiveInputStreamWithArSignature() throws ArchiveException, IOException {
        byte[] data = new byte[8];
        System.arraycopy(new byte[] {0x21, (byte)0x3C, 0x61, 0x72, 0x63, 0x68, 0x3E, 0x0A}, 0, data, 0, 8);
        InputStream in = new ByteArrayInputStream(data);
        
        Assert.assertNotNull(archiveStreamFactory.createArchiveInputStream(in));
    }
    
    @Test
    public void testCreateArchiveInputStreamWithCpioSignature() throws ArchiveException, IOException {
        byte[] data = new byte[6];
        System.arraycopy(new byte[] {0x30, 0x37, 0x30, 0x37, 0x30, 0x31}, 0, data, 0, 6);
        InputStream in = new ByteArrayInputStream(data);
        
        Assert.assertNotNull(archiveStreamFactory.createArchiveInputStream(in));
    }
    
    @Test
    public void testCreateArchiveInputStreamWithDumpSignature() throws ArchiveException, IOException {
        byte[] data = new byte[4];
        System.arraycopy(new byte[] {0x30, 0x30, 0x30, 0x30}, 0, data, 0, 4);
        InputStream in = new ByteArrayInputStream(data);
        
        Assert.assertNotNull(archiveStreamFactory.createArchiveInputStream(in));
    }
    
    @Test
    public void testCreateArchiveInputStreamWithTarSignature() throws ArchiveException, IOException {
        byte[] data = new byte[512];
        InputStream in = new ByteArrayInputStream(data);
        
        Assert.assertNotNull(archiveStreamFactory.createArchiveInputStream(in));
    }
    
    @Test
    public void testCreateArchiveInputStreamWithBigTarSignature() throws ArchiveException, IOException {
        byte[] data = new byte[600];
        InputStream in = new ByteArrayInputStream(data);
        
        Assert.assertNotNull(archiveStreamFactory.createArchiveInputStream(in));
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithAr() throws ArchiveException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("ar", out);
        
        Assert.assertNotNull(archiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithZip() throws ArchiveException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("zip", out);
        
        Assert.assertNotNull(archiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithTar() throws ArchiveException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("tar", out);
        
        Assert.assertNotNull(archiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithJar() throws ArchiveException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("jar", out);
        
        Assert.assertNotNull(archiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithCpio() throws ArchiveException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("cpio", out);
        
        Assert.assertNotNull(archiveOutputStream);
    }
}