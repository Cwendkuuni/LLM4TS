import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory mockFactory;
    private InputStream mockInputStream;
    private OutputStream mockOutputStream;

    @Before
    public void setUp() {
        mockFactory = new ArchiveStreamFactory();
        mockInputStream = mock(InputStream.class);
        mockOutputStream = mock(OutputStream.class);
    }
    
    // Test createArchiveInputStream method with known archiverName and valid InputStream
    @Test
    public void testCreateArchiveInputStream_validArchiverNameAndInputStream() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.AR;
        
        ArchiveInputStream result = mockFactory.createArchiveInputStream(archiverName, mockInputStream);
        
        assertNotNull(result);
        assertTrue(result instanceof ArArchiveInputStream);
    }
    
    // Test createArchiveInputStream method with unknown archiverName
    @Test(expected=ArchiveException.class)
    public void testCreateArchiveInputStream_unknownArchiverName() throws ArchiveException {
        String archiverName = "unknown";
        
        mockFactory.createArchiveInputStream(archiverName, mockInputStream);
    }
    
    // Test createArchiveInputStream method with null archiverName
    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullArchiverName() throws ArchiveException {
        String archiverName = null;
        
        mockFactory.createArchiveInputStream(archiverName, mockInputStream);
    }
    
    // Test createArchiveInputStream method with valid archiverName and null InputStream
    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullInputStream() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = null;
        
        mockFactory.createArchiveInputStream(archiverName, inputStream);
    }
    
    // Test createArchiveInputStream method with valid archiverName and invalid InputStream that does not support mark
    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveInputStream_invalidInputStream() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = mock(InputStream.class);
        
        mockFactory.createArchiveInputStream(archiverName, inputStream);
    }
    
    // Test createArchiveOutputStream method with known archiverName and valid OutputStream
    @Test
    public void testCreateArchiveOutputStream_validArchiverNameAndOutputStream() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.AR;
        
        ArchiveOutputStream result = mockFactory.createArchiveOutputStream(archiverName, mockOutputStream);
        
        assertNotNull(result);
        assertTrue(result instanceof ArArchiveOutputStream);
    }
    
    // Test createArchiveOutputStream method with unknown archiverName
    @Test(expected=ArchiveException.class)
    public void testCreateArchiveOutputStream_unknownArchiverName() throws ArchiveException {
        String archiverName = "unknown";
        
        mockFactory.createArchiveOutputStream(archiverName, mockOutputStream);
    }
    
    // Test createArchiveOutputStream method with null archiverName
    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullArchiverName() throws ArchiveException {
        String archiverName = null;
        
        mockFactory.createArchiveOutputStream(archiverName, mockOutputStream);
    }
    
    // Test createArchiveOutputStream method with valid archiverName and null OutputStream
    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullOutputStream() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = null;
        
        mockFactory.createArchiveOutputStream(archiverName, outputStream);
    }
    
    // Test createArchiveInputStream method with null InputStream
    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullInputStream() throws ArchiveException {
        InputStream inputStream = null;
        
        mockFactory.createArchiveInputStream(inputStream);
    }
    
    // Test createArchiveInputStream method with invalid InputStream that does not support mark
    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveInputStream_invalidInputStream() throws ArchiveException {
        InputStream inputStream = mock(InputStream.class);
        
        mockFactory.createArchiveInputStream(inputStream);
    }
    
    // Test createArchiveInputStream method with known archiverName and valid InputStream
    @Test
    public void testCreateArchiveInputStream_validInputStream() throws ArchiveException, IOException {
        byte[] signature = new byte[4];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(signature);
        when(mockInputStream.markSupported()).thenReturn(true);
        when(mockInputStream.read(signature)).thenReturn(4);
        when(mockInputStream.read(signature)).thenReturn(4);
        when(mockInputStream.reset()).thenReturn(null);
        when(mockFactory.createArchiveInputStream(argThat(new InputStreamMatcher(byteArrayInputStream)))).thenReturn(new JarArchiveInputStream(byteArrayInputStream));
        
        ArchiveInputStream result = mockFactory.createArchiveInputStream(mockInputStream);
        
        assertNotNull(result);
        assertTrue(result instanceof JarArchiveInputStream);
    }
    
    // Test createArchiveInputStream method with valid InputStream, but unknown archive type
    @Test(expected=ArchiveException.class)
    public void testCreateArchiveInputStream_unknownArchiveType() throws ArchiveException, IOException {
        byte[] signature = new byte[4];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(signature);
        when(mockInputStream.markSupported()).thenReturn(true);
        when(mockInputStream.read(signature)).thenReturn(4);
        when(mockInputStream.read(signature)).thenReturn(4);
        when(mockInputStream.reset()).thenReturn(null);
        when(mockFactory.createArchiveInputStream(argThat(new InputStreamMatcher(byteArrayInputStream)))).thenThrow(new ArchiveException("Unknown archive type"));
        
        mockFactory.createArchiveInputStream(mockInputStream);
    }
    
    // Test createArchiveInputStream method with null InputStream
    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullInputStream2() throws ArchiveException {
        InputStream inputStream = null;
        
        mockFactory.createArchiveInputStream(inputStream);
    }
    
    // Test createArchiveInputStream method with invalid InputStream that does not support mark
    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveInputStream_invalidInputStream2() throws ArchiveException {
        InputStream inputStream = mock(InputStream.class);
        
        mockFactory.createArchiveInputStream(inputStream);
    }
    
    // Custom ArgumentMatcher to compare InputStreams
    public class InputStreamMatcher extends ArgumentMatcher<InputStream> {
        
        private InputStream expected;
        
        public InputStreamMatcher(InputStream expected) {
            this.expected = expected;
        }
        
        @Override
        public boolean matches(Object argument) {
            if (argument instanceof InputStream) {
                try {
                    byte[] expectedBytes = new byte[expected.available()];
                    expected.read(expectedBytes);
                    byte[] actualBytes = new byte[((InputStream) argument).available()];
                    ((InputStream) argument).read(actualBytes);
                    return expectedBytes.equals(actualBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        }
    }
}