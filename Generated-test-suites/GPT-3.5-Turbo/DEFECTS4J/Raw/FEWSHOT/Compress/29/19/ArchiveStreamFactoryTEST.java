import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {
    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        InputStream in = new FileInputStream("testFile.zip");
        ArchiveInputStream ais = factory.createArchiveInputStream("zip", in);
        assertNotNull(ais);
        ais.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        OutputStream out = new FileOutputStream("testFile.zip");
        ArchiveOutputStream aos = factory.createArchiveOutputStream("zip", out);
        assertNotNull(aos);
        aos.close();
    }

    @Test
    public void testCreateArchiveInputStreamAutodetect() throws Exception {
        InputStream in = new FileInputStream("testFile.tar");
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        assertNotNull(ais);
        ais.close();
    }
}