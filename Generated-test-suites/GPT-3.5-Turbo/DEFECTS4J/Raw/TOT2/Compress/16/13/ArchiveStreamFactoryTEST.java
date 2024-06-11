import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;
    private ByteArrayInputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        archiveStreamFactory = new ArchiveStreamFactory();
        byte[] data = "test data".getBytes();
        inputStream = new ByteArrayInputStream(data);
        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testCreateArchiveInputStream_WithNullArchiverName_ShouldThrowIllegalArgumentException() throws ArchiveException {
        exceptionRule.expect(IllegalArgumentException.class);
        archiveStreamFactory.createArchiveInputStream(null, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_WithNullInputStream_ShouldThrowIllegalArgumentException() throws ArchiveException {
        exceptionRule.expect(IllegalArgumentException.class);
        archiveStreamFactory.createArchiveInputStream("zip", null);
    }

    // Add more test cases for createArchiveInputStream method here to cover all archiver names

    @Test
    public void testCreateArchiveOutputStream_WithNullArchiverName_ShouldThrowIllegalArgumentException() throws ArchiveException {
        exceptionRule.expect(IllegalArgumentException.class);
        archiveStreamFactory.createArchiveOutputStream(null, outputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_WithNullOutputStream_ShouldThrowIllegalArgumentException() throws ArchiveException {
        exceptionRule.expect(IllegalArgumentException.class);
        archiveStreamFactory.createArchiveOutputStream("zip", null);
    }

    // Add more test cases for createArchiveOutputStream method here to cover all archiver names

    @Test
    public void testCreateArchiveInputStream_AutoDetect_WithNullStream_ShouldThrowIllegalArgumentException() throws ArchiveException {
        exceptionRule.expect(IllegalArgumentException.class);
        archiveStreamFactory.createArchiveInputStream(null);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_StreamNotSupportingMark_ShouldThrowIllegalArgumentException() throws ArchiveException {
        exceptionRule.expect(IllegalArgumentException.class);
        ByteArrayInputStream nonMarkSupportedStream = new ByteArrayInputStream("test".getBytes());
        archiveStreamFactory.createArchiveInputStream(nonMarkSupportedStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_NoArchiverFound_ShouldThrowArchiveException() throws ArchiveException {
        byte[] data = "not an archive file".getBytes();
        ByteArrayInputStream invalidInputStream = new ByteArrayInputStream(data);
        exceptionRule.expect(ArchiveException.class);
        archiveStreamFactory.createArchiveInputStream(invalidInputStream);
    }

    // Add more test cases for createArchiveInputStream auto detect method here to cover different archive files

}