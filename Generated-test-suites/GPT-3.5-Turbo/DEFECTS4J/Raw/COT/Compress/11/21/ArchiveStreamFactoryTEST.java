
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamAR() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try (InputStream in = new FileInputStream("test.ar")) {
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
            assertNotNull(ais);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        } catch (ArchiveException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStreamAR_Exception() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("Exception should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        } catch (ArchiveException e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStreamZIP() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try (InputStream in = new FileInputStream("test.zip")) {
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNotNull(ais);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        } catch (ArchiveException e) {
            fail("Exception should not be thrown");
        }
    }

    // Add more test cases for the other createArchiveInputStream methods

    @Test
    public void testCreateArchiveOutputStreamAR() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try (OutputStream out = new FileOutputStream("test.ar")) {
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
            assertNotNull(aos);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        } catch (ArchiveException e) {
            fail("Exception should not be thrown");
        }
    }

    // Add more test cases for the other createArchiveOutputStream methods

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try (InputStream in = new FileInputStream("test.tar.gz")) {
            ArchiveInputStream ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        } catch (ArchiveException e) {
            fail("Exception should not be thrown");
        }
    }

    // Add more test cases for the auto-detect createArchiveInputStream method

}

