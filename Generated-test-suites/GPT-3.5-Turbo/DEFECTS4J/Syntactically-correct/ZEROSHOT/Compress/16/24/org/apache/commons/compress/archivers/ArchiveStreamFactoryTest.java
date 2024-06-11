import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;
    
    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }
    
    @Test
    public void testCreateArchiveInputStream_ar() throws Exception {
        ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
        assertNull(inputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_cpio() throws Exception {
        ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, null);
        assertNull(inputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_dump() throws Exception {
        ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, null);
        assertNull(inputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_jar() throws Exception {
        ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, null);
        assertNull(inputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_tar() throws Exception {
        ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null);
        assertNull(inputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_zip() throws Exception {
        ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
        assertNull(inputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_archiverNameNull() throws Exception {
        factory.createArchiveInputStream(null, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_inputStreamNull() throws Exception {
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }
    
    @Test
    public void testCreateArchiveOutputStream_ar() throws Exception {
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
        assertNull(outputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_cpio() throws Exception {
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, null);
        assertNull(outputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_dump() throws Exception {
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, null);
        assertNull(outputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_jar() throws Exception {
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
        assertNull(outputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_tar() throws Exception {
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
        assertNull(outputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_zip() throws Exception {
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
        assertNull(outputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_archiverNameNull() throws Exception {
        factory.createArchiveOutputStream(null, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_outputStreamNull() throws Exception {
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }
    
    @Test
    public void testCreateArchiveInputStream() throws Exception {
        ArchiveInputStream inputStream = factory.createArchiveInputStream(null);
        assertNull(inputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_inputStreamNull() throws Exception {
        factory.createArchiveInputStream((InputStream) null);
    }
    
}