import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream("zip", null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream("zip", null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithoutMarkSupport() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream("invalid", new ByteArrayInputStream(new byte[0]));
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream("invalid", new ByteArrayOutputStream());
    }
    
    @Test
    public void testCreateArchiveInputStreamWithArArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream inputStream = factory.createArchiveInputStream("ar", new ByteArrayInputStream(new byte[0]));
        assertTrue(inputStream instanceof ArArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamWithZipArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream inputStream = factory.createArchiveInputStream("zip", new ByteArrayInputStream(new byte[0]));
        assertTrue(inputStream instanceof ZipArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamWithTarArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream inputStream = factory.createArchiveInputStream("tar", new ByteArrayInputStream(new byte[0]));
        assertTrue(inputStream instanceof TarArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamWithJarArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream inputStream = factory.createArchiveInputStream("jar", new ByteArrayInputStream(new byte[0]));
        assertTrue(inputStream instanceof JarArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamWithCpioArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream inputStream = factory.createArchiveInputStream("cpio", new ByteArrayInputStream(new byte[0]));
        assertTrue(inputStream instanceof CpioArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithArArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream("ar", new ByteArrayOutputStream());
        assertTrue(outputStream instanceof ArArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithZipArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream("zip", new ByteArrayOutputStream());
        assertTrue(outputStream instanceof ZipArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithTarArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream("tar", new ByteArrayOutputStream());
        assertTrue(outputStream instanceof TarArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithJarArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream("jar", new ByteArrayOutputStream());
        assertTrue(outputStream instanceof JarArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithCpioArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream("cpio", new ByteArrayOutputStream());
        assertTrue(outputStream instanceof CpioArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamWithAutodetectingArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream inputStream = factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        assertNotNull(inputStream);
    }
    
}