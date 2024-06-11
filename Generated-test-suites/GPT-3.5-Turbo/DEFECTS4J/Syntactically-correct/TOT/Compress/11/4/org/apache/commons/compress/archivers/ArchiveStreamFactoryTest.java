import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;

    @Before
    public void setup() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream_ValidArchiverName_ValidInputStream() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = new ByteArrayInputStream(new byte[]{});
        
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(archiverName, in);
        
        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName_ValidInputStream() throws ArchiveException {
        String archiverName = null;
        InputStream in = new ByteArrayInputStream(new byte[]{});
        
        archiveStreamFactory.createArchiveInputStream(archiverName, in);
        
        fail("Exception should have been thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_ValidArchiverName_NullInputStream() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = null;
        
        archiveStreamFactory.createArchiveInputStream(archiverName, in);
        
        fail("Exception should have been thrown");
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidArchiverName_ValidInputStream() throws ArchiveException {
        String archiverName = "invalid";
        InputStream in = new ByteArrayInputStream(new byte[]{});
        
        archiveStreamFactory.createArchiveInputStream(archiverName, in);
        
        fail("Exception should have been thrown");
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArchiverName_ValidOutputStream() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = new ByteArrayOutputStream();
        
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(archiverName, out);
        
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName_ValidOutputStream() throws ArchiveException {
        String archiverName = null;
        OutputStream out = new ByteArrayOutputStream();
        
        archiveStreamFactory.createArchiveOutputStream(archiverName, out);
        
        fail("Exception should have been thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_ValidArchiverName_NullOutputStream() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = null;
        
        archiveStreamFactory.createArchiveOutputStream(archiverName, out);
        
        fail("Exception should have been thrown");
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_InvalidArchiverName_ValidOutputStream() throws ArchiveException {
        String archiverName = "invalid";
        OutputStream out = new ByteArrayOutputStream();
        
        archiveStreamFactory.createArchiveOutputStream(archiverName, out);
        
        fail("Exception should have been thrown");
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_ValidInputStream() throws ArchiveException, IOException {
        byte[] signature = new byte[]{80, 75};
        InputStream in = new ByteArrayInputStream(signature);
        
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
        
        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_AutoDetect_NullInputStream() throws ArchiveException, IOException {
        InputStream in = null;
        
        archiveStreamFactory.createArchiveInputStream(in);
        
        fail("Exception should have been thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_AutoDetect_InputStreamWithoutMark() throws ArchiveException, IOException {
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        
        archiveStreamFactory.createArchiveInputStream(in);
        
        fail("Exception should have been thrown");
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AutoDetect_UnknownSignature() throws ArchiveException, IOException {
        byte[] signature = new byte[]{0, 0};
        InputStream in = new ByteArrayInputStream(signature);
        
        archiveStreamFactory.createArchiveInputStream(in);
        
        fail("Exception should have been thrown");
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_ZipSignature() throws ArchiveException, IOException {
        byte[] signature = new byte[]{80, 75, 3, 4};
        InputStream in = new ByteArrayInputStream(signature);
        
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
        
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_JarSignature() throws ArchiveException, IOException {
        byte[] signature = new byte[]{80, 75, 5, 6};
        InputStream in = new ByteArrayInputStream(signature);
        
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
        
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_ArSignature() throws ArchiveException, IOException {
        byte[] signature = new byte[]{33, 60, 0, 97};
        InputStream in = new ByteArrayInputStream(signature);
        
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
        
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_CpioSignature() throws ArchiveException, IOException {
        byte[] signature = new byte[]{71, 60, 0, 97};
        InputStream in = new ByteArrayInputStream(signature);
        
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
        
        assertNotNull(archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AutoDetect_UnknownDumpSignature() throws ArchiveException, IOException {
        byte[] signature = new byte[]{0, 0, 0, 0};
        InputStream in = new ByteArrayInputStream(signature);
        
        archiveStreamFactory.createArchiveInputStream(in);
        
        fail("Exception should have been thrown");
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_DumpSignature() throws ArchiveException, IOException {
        byte[] signature = new byte[]{29, 3, 0, 0};
        InputStream in = new ByteArrayInputStream(signature);
        
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
        
        assertNotNull(archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AutoDetect_UnknownTarSignature() throws ArchiveException, IOException {
        byte[] signature = new byte[]{0, 0, 0, 0};
        InputStream in = new ByteArrayInputStream(signature);
        
        archiveStreamFactory.createArchiveInputStream(in);
        
        fail("Exception should have been thrown");
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_TarSignature() throws ArchiveException, IOException {
        byte[] signature = new byte[]{31, -117, 8, 0};
        InputStream in = new ByteArrayInputStream(signature);
        
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
        
        assertNotNull(archiveInputStream);
    }
}