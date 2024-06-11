import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.*;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;
    private InputStream input;
    private OutputStream output;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
        input = new ByteArrayInputStream("test".getBytes());
        output = new ByteArrayOutputStream();
    }

    @Test
    public void testCreateArchiveInputStream_validInput() throws ArchiveException {
        ArchiveInputStream archiveInput = factory.createArchiveInputStream("zip", input);
        assertNotNull(archiveInput);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullArchiverName() throws ArchiveException {
        factory.createArchiveInputStream(null, input);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullInputStream() throws ArchiveException {
        factory.createArchiveInputStream("zip", null);
    }

    @Test(expected=ArchiveException.class)
    public void testCreateArchiveInputStream_unknownArchiverName() throws ArchiveException {
        factory.createArchiveInputStream("rar", input);
    }

    @Test
    public void testCreateArchiveInputStream_autoDetectZipFormat() throws ArchiveException {
        ArchiveInputStream archiveInput = factory.createArchiveInputStream(input);
        assertNotNull(archiveInput);
    }

    @Test
    public void testCreateArchiveInputStream_autoDetectJarFormat() throws ArchiveException {
        String jarSignature = "PK";
        InputStream jarInput = new ByteArrayInputStream(jarSignature.getBytes());
        ArchiveInputStream archiveInput = factory.createArchiveInputStream(jarInput);
        assertNotNull(archiveInput);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveInputStream_invalidInputNullStream() throws ArchiveException {
        factory.createArchiveInputStream(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveInputStream_invalidInputMarkNotSupported() throws ArchiveException {
        InputStream invalidInput = new FileInputStream("path/to/file");
        factory.createArchiveInputStream(invalidInput);
    }

    @Test
    public void testCreateArchiveOutputStream_validInput() throws ArchiveException {
        ArchiveOutputStream archiveOutput = factory.createArchiveOutputStream("zip", output);
        assertNotNull(archiveOutput);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullArchiverName() throws ArchiveException {
        factory.createArchiveOutputStream(null, output);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullOutputStream() throws ArchiveException {
        factory.createArchiveOutputStream("zip", null);
    }

    @Test(expected=ArchiveException.class)
    public void testCreateArchiveOutputStream_unknownArchiverName() throws ArchiveException {
        factory.createArchiveOutputStream("rar", output);
    }

    @Test
    public void testCreateArchiveInputStream_autoDetectZipFormat() throws ArchiveException {
        ArchiveOutputStream archiveOutput = factory.createArchiveOutputStream(output);
        assertNotNull(archiveOutput);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveInputStream_invalidOutputNullStream() throws ArchiveException {
        factory.createArchiveOutputStream(null);
    }

    @Test
    public void testCreateArchiveInputStream_invalidOutputMarkNotSupported() throws ArchiveException {
        OutputStream invalidOutput = new FileOutputStream("path/to/file");
        ArchiveOutputStream archiveOutput = factory.createArchiveOutputStream(invalidOutput);
        assertNotNull(archiveOutput);
    }

}