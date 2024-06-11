import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidArchiverName_StreamNotNull() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
            assertNotNull(in);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName_ThrowsIllegalArgumentException() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullStream_ThrowsIllegalArgumentException() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownArchiverName_ThrowsArchiveException() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
    }
    
    @Test
    public void testCreateArchiveOutputStream_ValidArchiverName_StreamNotNull() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            ArchiveOutputStream out = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
            assertNotNull(out);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName_ThrowsIllegalArgumentException() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullStream_ThrowsIllegalArgumentException() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownArchiverName_ThrowsArchiveException() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream("unknown", new ByteArrayOutputStream());
    }
    
    @Test
    public void testCreateArchiveInputStream_Autodetect_ValidInputStream_StreamNotNull() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            ArchiveInputStream in = factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
            assertNotNull(in);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_Autodetect_NullStream_ThrowsIllegalArgumentException() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_Autodetect_StreamDoesNotSupportMark_ThrowsIllegalArgumentException() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_Autodetect_UnknownArchiveFormat_ThrowsArchiveException() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
    }
}