import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {
    
    private IOContext ioContext;
    
    @Before
    public void setUp() {
        ioContext = new IOContext(null, null, true);
    }
    
    @Test
    public void testSetEncoding() {
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }
    
    @Test
    public void testWithEncoding() {
        ioContext.withEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }
    
    @Test
    public void testGetSourceReference() {
        assertNull(ioContext.getSourceReference());
    }
    
    @Test
    public void testGetEncoding() {
        assertNull(ioContext.getEncoding());
    }
    
    @Test
    public void testIsResourceManaged() {
        assertTrue(ioContext.isResourceManaged());
    }
    
    @Test
    public void testConstructTextBuffer() {
        assertNotNull(ioContext.constructTextBuffer());
    }
    
    @Test
    public void testAllocReadIOBuffer() {
        assertNull(ioContext._readIOBuffer);
        assertNotNull(ioContext.allocReadIOBuffer());
        assertNotNull(ioContext._readIOBuffer);
    }
    
    @Test
    public void testAllocReadIOBufferWithMinSize() {
        assertNull(ioContext._readIOBuffer);
        assertNotNull(ioContext.allocReadIOBuffer(1024));
        assertNotNull(ioContext._readIOBuffer);
    }
    
    @Test
    public void testAllocWriteEncodingBuffer() {
        assertNull(ioContext._writeEncodingBuffer);
        assertNotNull(ioContext.allocWriteEncodingBuffer());
        assertNotNull(ioContext._writeEncodingBuffer);
    }
    
    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        assertNull(ioContext._writeEncodingBuffer);
        assertNotNull(ioContext.allocWriteEncodingBuffer(1024));
        assertNotNull(ioContext._writeEncodingBuffer);
    }
    
    @Test
    public void testAllocBase64Buffer() {
        assertNull(ioContext._base64Buffer);
        assertNotNull(ioContext.allocBase64Buffer());
        assertNotNull(ioContext._base64Buffer);
    }
    
    @Test
    public void testAllocTokenBuffer() {
        assertNull(ioContext._tokenCBuffer);
        assertNotNull(ioContext.allocTokenBuffer());
        assertNotNull(ioContext._tokenCBuffer);
    }
    
    @Test
    public void testAllocTokenBufferWithMinSize() {
        assertNull(ioContext._tokenCBuffer);
        assertNotNull(ioContext.allocTokenBuffer(1024));
        assertNotNull(ioContext._tokenCBuffer);
    }
    
    @Test
    public void testAllocConcatBuffer() {
        assertNull(ioContext._concatCBuffer);
        assertNotNull(ioContext.allocConcatBuffer());
        assertNotNull(ioContext._concatCBuffer);
    }
    
    @Test
    public void testAllocNameCopyBuffer() {
        assertNull(ioContext._nameCopyBuffer);
        assertNotNull(ioContext.allocNameCopyBuffer(1024));
        assertNotNull(ioContext._nameCopyBuffer);
    }
    
    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertNotNull(ioContext._readIOBuffer);
        ioContext.releaseReadIOBuffer(buffer);
        assertNull(ioContext._readIOBuffer);
    }
    
    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(ioContext._writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(buffer);
        assertNull(ioContext._writeEncodingBuffer);
    }
    
    @Test
    public void testReleaseBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        assertNotNull(ioContext._base64Buffer);
        ioContext.releaseBase64Buffer(buffer);
        assertNull(ioContext._base64Buffer);
    }
    
    @Test
    public void testReleaseTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        assertNotNull(ioContext._tokenCBuffer);
        ioContext.releaseTokenBuffer(buffer);
        assertNull(ioContext._tokenCBuffer);
    }
    
    @Test
    public void testReleaseConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        assertNotNull(ioContext._concatCBuffer);
        ioContext.releaseConcatBuffer(buffer);
        assertNull(ioContext._concatCBuffer);
    }
    
    @Test
    public void testReleaseNameCopyBuffer() {
        char[] buffer = ioContext.allocNameCopyBuffer(1024);
        assertNotNull(ioContext._nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(buffer);
        assertNull(ioContext._nameCopyBuffer);
    }
}