import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ar.*;
import org.apache.commons.compress.archivers.arj.*;
import org.apache.commons.compress.archivers.cpio.*;
import org.apache.commons.compress.archivers.dump.*;
import org.apache.commons.compress.archivers.jar.*;
import org.apache.commons.compress.archivers.sevenz.*;
import org.apache.commons.compress.archivers.tar.*;
import org.apache.commons.compress.archivers.zip.*;
import org.apache.commons.compress.utils.IOUtils;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamAR() throws Exception {
        // Typical use case
        InputStream in = new FileInputStream("testfile.ar");
        ArchiveInputStream arStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertNotNull(arStream);
        
        // Edge case - empty input stream
        InputStream emptyIn = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream emptyStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, emptyIn);
        assertNotNull(emptyStream);
        
        // Error scenario - invalid archiver name
        try {
            new ArchiveStreamFactory().createArchiveInputStream("fake", in);
            fail("Should throw ArchiveException");
        } catch (ArchiveException e) {
            // Exception thrown as expected
        }
        
        // Error scenario - null input stream
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamARJ() throws Exception {
        // Typical use case
        InputStream in = new FileInputStream("testfile.arj");
        ArchiveInputStream arjStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ARJ, in);
        assertNotNull(arjStream);
        
        // Edge case - empty input stream
        InputStream emptyIn = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream emptyStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ARJ, emptyIn);
        assertNotNull(emptyStream);
        
        // Error scenario - invalid archiver name
        try {
            new ArchiveStreamFactory().createArchiveInputStream("fake", in);
            fail("Should throw ArchiveException");
        } catch (ArchiveException e) {
            // Exception thrown as expected
        }
        
        // Error scenario - null input stream
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ARJ, null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception thrown as expected
        }
    }

    // Similar test cases for other archiver formats ...

}