import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveException;

public class ArchiveStreamFactoryTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testCreateArchiveInputStream_ValidInput_ReturnsArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        
        ArchiveInputStream archiveIn = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        
        assertNotNull(archiveIn);
    }

    @Test
    public void testCreateArchiveInputStream_NullArchiverName_ThrowsIllegalArgumentException() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        
        exception.expect(IllegalArgumentException.class);
        factory.createArchiveInputStream(null, in);
    }

    @Test
    public void testCreateArchiveInputStream_NullInputStream_ThrowsIllegalArgumentException() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        exception.expect(IllegalArgumentException.class);
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test
    public void testCreateArchiveOutputStream_ValidInput_ReturnsArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        
        ArchiveOutputStream archiveOut = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        
        assertNotNull(archiveOut);
    }

    @Test
    public void testCreateArchiveOutputStream_NullArchiverName_ThrowsIllegalArgumentException() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        
        exception.expect(IllegalArgumentException.class);
        factory.createArchiveOutputStream(null, out);
    }

    @Test
    public void testCreateArchiveOutputStream_NullOutputStream_ThrowsIllegalArgumentException() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        exception.expect(IllegalArgumentException.class);
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_ValidInput_ReturnsArchiveInputStream() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] zipSignature = {80, 75, 3, 4}; // ZIP file signature
        InputStream in = new ByteArrayInputStream(zipSignature);
        
        ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
        
        assertNotNull(archiveIn);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_InvalidInput_ThrowsArchiveException() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] invalidSignature = {1, 2, 3, 4};
        InputStream in = new ByteArrayInputStream(invalidSignature);
        
        exception.expect(ArchiveException.class);
        factory.createArchiveInputStream(in);
    }
}