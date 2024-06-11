import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[10]);

        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream(null, in);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream("unknown", null);
        });

        assertThrows(ArchiveException.class, () -> {
            factory.createArchiveInputStream("unsupported", in);
        });
        // Additional test cases can be added for supported archiver formats
    }

    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveOutputStream(null, out);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveOutputStream("unknown", null);
        });

        assertThrows(ArchiveException.class, () -> {
            factory.createArchiveOutputStream("unsupported", out);
        });
        // Additional test cases can be added for supported archiver formats
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[10]);

        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream(in);
        });
        // Additional test cases can be added for auto-detection of archive formats
    }
}