import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.*;
import java.io.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        try {
            ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream("zip", new ByteArrayInputStream(new byte[0]));
            assertNotNull(input);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        try {
            ArchiveOutputStream output = new ArchiveStreamFactory().createArchiveOutputStream("zip", new ByteArrayOutputStream());
            assertNotNull(output);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() {
        try {
            ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
            assertNotNull(input);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

}