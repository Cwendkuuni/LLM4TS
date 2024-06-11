import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;

    @Before
    public void setup() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream_withValidArchiverNameAndInputStream_returnsArchiveInputStream() {
        // Arrange
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Act
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(archiverName, in);

        // Assert
        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullArchiverName_throwsIllegalArgumentException() {
        // Arrange
        String archiverName = null;
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Act
        archiveStreamFactory.createArchiveInputStream(archiverName, in);

        // Assert: Expects IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullInputStream_throwsIllegalArgumentException() {
        // Arrange
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = null;

        // Act
        archiveStreamFactory.createArchiveInputStream(archiverName, in);

        // Assert: Expects IllegalArgumentException
    }

    @Test
    public void testCreateArchiveOutputStream_withValidArchiverNameAndOutputStream_returnsArchiveOutputStream() {
        // Arrange
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = new ByteArrayOutputStream();

        // Act
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(archiverName, out);

        // Assert
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullArchiverName_throwsIllegalArgumentException() {
        // Arrange
        String archiverName = null;
        OutputStream out = new ByteArrayOutputStream();

        // Act
        archiveStreamFactory.createArchiveOutputStream(archiverName, out);

        // Assert: Expects IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullOutputStream_throwsIllegalArgumentException() {
        // Arrange
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = null;

        // Act
        archiveStreamFactory.createArchiveOutputStream(archiverName, out);

        // Assert: Expects IllegalArgumentException
    }

    @Test
    public void testCreateArchiveInputStream_withInputStream_returnsArchiveInputStream() {
        // Arrange
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Act
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);

        // Assert
        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullInputStream_throwsIllegalArgumentException() {
        // Arrange
        InputStream in = null;

        // Act
        archiveStreamFactory.createArchiveInputStream(in);

        // Assert: Expects IllegalArgumentException
    }
}