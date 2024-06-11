import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamValidAr() {
        String archiverName = "ar";
        byte[] data = new byte[]{1, 2, 3, 4};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream ais = factory.createArchiveInputStream(archiverName, in);
            assertNotNull(ais);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamInvalidName() {
        String archiverName = "invalid";
        byte[] data = new byte[]{1, 2, 3, 4};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream(archiverName, in);
            fail("Expected ArchiveException was not thrown");
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamNullInputStream() {
        String archiverName = "ar";
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream(archiverName, null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Add more test cases for other methods here

}