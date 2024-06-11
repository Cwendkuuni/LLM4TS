
import org.junit.Test;

public class ArchiveStreamFactoryTest {
    
    // Test createArchiveInputStream(archiverName, in) for valid archiverName and non-null input stream
    @Test
    public void testCreateArchiveInputStream_ValidArchiverNameAndInputStream() {
        // Provide valid archiverName and input stream
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[5]);
        
        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiverName, in);
            // Assert the created archiveInputStream is of the expected type (ZipArchiveInputStream)
            Assert.assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        } catch (ArchiveException e) {
            fail("ArchiveException was thrown: " + e.getMessage());
        }
    }
    
    // Test createArchiveInputStream(archiverName, in) for null archiverName 
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() throws ArchiveException {
        // Provide null archiverName
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = null;
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[5]);
        
        // Attempt to create archiveInputStream
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiverName, in);
    }
    
    // Test createArchiveInputStream(archiverName, in) for null input stream 
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws ArchiveException {
        // Provide null input stream
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        ByteArrayInputStream in = null;
        
        // Attempt to create archiveInputStream
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiverName, in);
    }
    
    // Test createArchiveOutputStream(archiverName, out) for valid archiverName and non-null output stream
    @Test
    public void testCreateArchiveOutputStream_ValidArchiverNameAndOutputStream() {
        // Provide valid archiverName and output stream
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try {
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiverName, out);
            // Assert the created archiveOutputStream is of the expected type (ZipArchiveOutputStream)
            Assert.assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
        } catch (ArchiveException e) {
            fail("ArchiveException was thrown: " + e.getMessage());
        }
    }
    
    // Test createArchiveOutputStream(archiverName, out) for null archiverName 
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() throws ArchiveException {
        // Provide null archiverName
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        // Attempt to create archiveOutputStream
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiverName, out);
    }
    
    // Test createArchiveOutputStream(archiverName, out) for null output stream 
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws ArchiveException {
        // Provide null output stream
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        ByteArrayOutputStream out = null;
        
        // Attempt to create archiveOutputStream
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiverName, out);
    }
    
    // Test createArchiveInputStream(in) for valid input stream
    @Test
    public void testCreateArchiveInputStream_ValidInputStream() {
        // Provide valid input stream
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[10]);
        
        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(in);
            // Assert that archiveInputStream is not null
            Assert.assertNotNull(archiveInputStream);
        } catch (ArchiveException e) {
            fail("ArchiveException was thrown: " + e.getMessage());
        }
    }
    
    // Test createArchiveInputStream(in) for null input stream
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws ArchiveException {
        // Provide null input stream
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayInputStream in = null;

        // Attempt to create archiveInputStream
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(in);
    }
}
