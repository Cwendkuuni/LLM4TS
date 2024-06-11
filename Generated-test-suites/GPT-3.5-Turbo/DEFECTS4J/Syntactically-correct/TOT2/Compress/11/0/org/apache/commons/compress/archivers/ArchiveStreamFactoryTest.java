import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream_ValidArchiverName() throws ArchiveException {
        ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
        assertNotNull(inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() throws ArchiveException {
        factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws ArchiveException {
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArchiverName() throws ArchiveException {
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
        assertNotNull(outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() throws ArchiveException {
        factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws ArchiveException {
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetection() throws ArchiveException {
        ArchiveInputStream inputStream = factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        assertNotNull(inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NoMarkSupported() throws ArchiveException {
        factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownSignature() throws ArchiveException {
        factory.createArchiveInputStream(new ByteArrayInputStream(new byte[1]));
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidInputStream() throws ArchiveException {
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }

}