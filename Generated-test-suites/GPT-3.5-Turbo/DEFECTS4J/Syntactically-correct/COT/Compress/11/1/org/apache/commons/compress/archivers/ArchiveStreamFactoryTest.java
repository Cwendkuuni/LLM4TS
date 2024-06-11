
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Test basic functionality with valid archiver names and a valid input stream
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new FileInputStream("test.txt");
            
            // Test AR archiver
            ArchiveInputStream arInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
            assertNotNull(arInputStream);
            
            // Test ZIP archiver
            ArchiveInputStream zipInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNotNull(zipInputStream);
            
            // Test TAR archiver
            ArchiveInputStream tarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
            assertNotNull(tarInputStream);
            
            // Test JAR archiver
            ArchiveInputStream jarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
            assertNotNull(jarInputStream);
            
            // Test CPIO archiver
            ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
            assertNotNull(cpioInputStream);
            
            // Test DUMP archiver
            ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
            assertNotNull(dumpInputStream);
        } catch (ArchiveException | IllegalArgumentException | IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test invalid archiver name
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new FileInputStream("test.txt");
            
            ArchiveInputStream invalidInputStream = factory.createArchiveInputStream("invalid", in);
            fail("Exception not thrown for invalid archiver name");
        } catch (ArchiveException e) {
            assertEquals("Archiver: invalid not found.", e.getMessage());
        } catch (IllegalArgumentException | IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        
        // Test null archiver name
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new FileInputStream("test.txt");
            
            ArchiveInputStream nullInputStream = factory.createArchiveInputStream(null, in);
            fail("Exception not thrown for null archiver name");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch (ArchiveException | IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        
        // Test null input stream
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            
            ArchiveInputStream nullInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("Exception not thrown for null input stream");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        } catch (ArchiveException | IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Test basic functionality with valid archiver names and a valid output stream
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new FileOutputStream("test.txt");
            
            // Test AR archiver
            ArchiveOutputStream arOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
            assertNotNull(arOutputStream);
            
            // Test ZIP archiver
            ArchiveOutputStream zipOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertNotNull(zipOutputStream);
            
            // Test TAR archiver
            ArchiveOutputStream tarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
            assertNotNull(tarOutputStream);
            
            // Test JAR archiver
            ArchiveOutputStream jarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
            assertNotNull(jarOutputStream);
            
            // Test CPIO archiver
            ArchiveOutputStream cpioOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
            assertNotNull(cpioOutputStream);
        } catch (ArchiveException | IllegalArgumentException | IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test invalid archiver name
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new FileOutputStream("test.txt");
            
            ArchiveOutputStream invalidOutputStream = factory.createArchiveOutputStream("invalid", out);
            fail("Exception not thrown for invalid archiver name");
        } catch (ArchiveException e) {
            assertEquals("Archiver: invalid not found.", e.getMessage());
        } catch (IllegalArgumentException | IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        
        // Test null archiver name
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new FileOutputStream("test.txt");
            
            ArchiveOutputStream nullOutputStream = factory.createArchiveOutputStream(null, out);
            fail("Exception not thrown for null archiver name");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch (ArchiveException | IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        
        // Test null output stream
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            
            ArchiveOutputStream nullOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            fail("Exception not thrown for null output stream");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        } catch (ArchiveException | IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithAutodetection() {
        // Test basic functionality with a valid input stream that supports mark
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new FileInputStream("test.txt");
            
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(in);
            assertNotNull(archiveInputStream);
        } catch (ArchiveException | IllegalArgumentException | IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test null input stream
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            
            ArchiveInputStream nullInputStream = factory.createArchiveInputStream(null);
            fail("Exception not thrown for null input stream");
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        } catch (ArchiveException | IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        
        // Test input stream that does not support mark
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new FileInputStream("test.txt");
            
            ArchiveInputStream unsupportedInputStream = factory.createArchiveInputStream(in);
            fail("Exception not thrown for input stream that does not support mark");
        } catch (IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        } catch (ArchiveException | IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        
        // Test ARCHIVE type that is not detected
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new FileInputStream("test.txt");
            
            ArchiveInputStream unrecognisedInputStream = factory.createArchiveInputStream(in);
            fail("Exception not thrown for unrecognised input stream");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        } catch (IllegalArgumentException | IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }
}
