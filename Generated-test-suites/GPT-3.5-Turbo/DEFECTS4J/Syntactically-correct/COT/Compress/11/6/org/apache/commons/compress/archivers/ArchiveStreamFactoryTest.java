import org.apache.commons.compress.archivers.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;

    @Before
    public void setup() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream() {
        try {
            String archiverName = ArchiveStreamFactory.ZIP;
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(archiverName, in);

            assertNotNull(archiveInputStream);
            // additional assertions if needed
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        try {
            String archiverName = ArchiveStreamFactory.ZIP;
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(archiverName, out);

            assertNotNull(archiveOutputStream);
            // additional assertions if needed
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutodetect() {
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);

            assertNotNull(archiveInputStream);
            // additional assertions if needed
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
}