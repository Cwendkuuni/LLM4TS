import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream in = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(archiverName, in);

        assertNotNull(result);
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing
            }
        };

        ArchiveOutputStream result = archiveStreamFactory.createArchiveOutputStream(archiverName, out);

        assertNotNull(result);
    }

    @Test
    public void testCreateArchiveInputStream_autodetect() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(in);

        assertNotNull(result);
    }
}