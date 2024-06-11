import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0])));
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0])));
        } catch (Exception e) {
            fail("Exception thrown for valid archiver name");
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
            fail("No exception thrown for null archiver name");
        } catch (IllegalArgumentException e) {
            // Exception expected
        } catch (Exception e) {
            fail("Wrong exception thrown for null archiver name");
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("No exception thrown for null input stream");
        } catch (IllegalArgumentException e) {
            // Exception expected
        } catch (Exception e) {
            fail("Wrong exception thrown for null input stream");
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new FileOutputStream("output.zip")));
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, new FileOutputStream("output.tar")));
        } catch (Exception e) {
            fail("Exception thrown for valid archiver name");
        }
    }

    @Test
    public void testCreateArchiveOutputStream_NullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream(null, new FileOutputStream("output.zip"));
            fail("No exception thrown for null archiver name");
        } catch (IllegalArgumentException e) {
            // Exception expected
        } catch (Exception e) {
            fail("Wrong exception thrown for null archiver name");
        }
    }

    @Test
    public void testCreateArchiveOutputStream_NullOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("No exception thrown for null output stream");
        } catch (IllegalArgumentException e) {
            // Exception expected
        } catch (Exception e) {
            fail("Wrong exception thrown for null output stream");
        }
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            assertNotNull(factory.createArchiveInputStream(new FileInputStream("input.zip")));
        } catch (Exception e) {
            fail("Exception thrown for auto detection");
        }
    }

    @Test
    public void testCreateArchiveInputStream_MarkNotSupported() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
            fail("No exception thrown for input stream not supporting mark");
        } catch (IllegalArgumentException e) {
            // Exception expected
        } catch (Exception e) {
            fail("Wrong exception thrown for input stream not supporting mark");
        }
    }
}