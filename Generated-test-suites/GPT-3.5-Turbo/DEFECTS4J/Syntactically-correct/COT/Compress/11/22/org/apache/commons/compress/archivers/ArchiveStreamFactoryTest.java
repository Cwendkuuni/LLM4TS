

import org.junit.Test;
import org.junit.Assert;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_AR() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new FileInputStream("testfile.ar");
            ArchiveInputStream ais = factory.createArchiveInputStream("ar", in);
            Assert.assertNotNull(ais);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new FileInputStream("testfile.zip");
            ArchiveInputStream ais = factory.createArchiveInputStream("zip", in);
            Assert.assertNotNull(ais);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_TAR() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new FileInputStream("testfile.tar");
            ArchiveInputStream ais = factory.createArchiveInputStream("tar", in);
            Assert.assertNotNull(ais);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_JAR() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new FileInputStream("testfile.jar");
            ArchiveInputStream ais = factory.createArchiveInputStream("jar", in);
            Assert.assertNotNull(ais);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new FileInputStream("testfile.cpio");
            ArchiveInputStream ais = factory.createArchiveInputStream("cpio", in);
            Assert.assertNotNull(ais);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_DUMP() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new FileInputStream("testfile.dump");
            ArchiveInputStream ais = factory.createArchiveInputStream("dump", in);
            Assert.assertNotNull(ais);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_AR() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            OutputStream out = new FileOutputStream("output.ar");
            ArchiveOutputStream aos = factory.createArchiveOutputStream("ar", out);
            Assert.assertNotNull(aos);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            OutputStream out = new FileOutputStream("output.zip");
            ArchiveOutputStream aos = factory.createArchiveOutputStream("zip", out);
            Assert.assertNotNull(aos);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_TAR() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            OutputStream out = new FileOutputStream("output.tar");
            ArchiveOutputStream aos = factory.createArchiveOutputStream("tar", out);
            Assert.assertNotNull(aos);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_JAR() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            OutputStream out = new FileOutputStream("output.jar");
            ArchiveOutputStream aos = factory.createArchiveOutputStream("jar", out);
            Assert.assertNotNull(aos);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            OutputStream out = new FileOutputStream("output.cpio");
            ArchiveOutputStream aos = factory.createArchiveOutputStream("cpio", out);
            Assert.assertNotNull(aos);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

}

