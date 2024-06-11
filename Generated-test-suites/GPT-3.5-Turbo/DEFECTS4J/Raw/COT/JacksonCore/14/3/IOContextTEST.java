import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        IOContext context = new IOContext(null, null, false);
        context.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, context.getEncoding());
    }
    
    @Test
    public void testWithEncoding() {
        IOContext context = new IOContext(null, null, false);
        IOContext newContext = context.withEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, newContext.getEncoding());
    }
    
    @Test
    public void testConstructTextBuffer() {
        IOContext context = new IOContext(null, null, false);
        TextBuffer textBuffer = context.constructTextBuffer();
        assertNotNull(textBuffer);
    }
    
    @Test
    public void testAllocReadIOBuffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] readIOBuffer = context.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
    }

    // Additional test cases for edge cases and exceptions
    
    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext context = new IOContext(null, null, false);
        byte[] readIOBuffer = context.allocReadIOBuffer(1024);
        assertNotNull(readIOBuffer);
        assertTrue(readIOBuffer.length >= 1024);
    }
    
    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
    }
    
    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext context = new IOContext(null, null, false);
        byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer(1024);
        assertNotNull(writeEncodingBuffer);
        assertTrue(writeEncodingBuffer.length >= 1024);
    }
    
    @Test
    public void testAllocBase64Buffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] base64Buffer = context.allocBase64Buffer();
        assertNotNull(base64Buffer);
    }
    
    @Test
    public void testAllocTokenBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] tokenBuffer = context.allocTokenBuffer();
        assertNotNull(tokenBuffer);
    }
    
    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext context = new IOContext(null, null, false);
        char[] tokenBuffer = context.allocTokenBuffer(1024);
        assertNotNull(tokenBuffer);
        assertTrue(tokenBuffer.length >= 1024);
    }
    
    @Test
    public void testAllocConcatBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] concatBuffer = context.allocConcatBuffer();
        assertNotNull(concatBuffer);
    }
    
    @Test
    public void testAllocNameCopyBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] nameCopyBuffer = context.allocNameCopyBuffer(1024);
        assertNotNull(nameCopyBuffer);
        assertTrue(nameCopyBuffer.length >= 1024);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDoubleAllocReadIOBuffer() {
        IOContext context = new IOContext(null, null, false);
        context.allocReadIOBuffer();
        context.allocReadIOBuffer();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDoubleAllocWriteEncodingBuffer() {
        IOContext context = new IOContext(null, null, false);
        context.allocWriteEncodingBuffer();
        context.allocWriteEncodingBuffer();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDoubleAllocBase64Buffer() {
        IOContext context = new IOContext(null, null, false);
        context.allocBase64Buffer();
        context.allocBase64Buffer();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDoubleAllocTokenBuffer() {
        IOContext context = new IOContext(null, null, false);
        context.allocTokenBuffer();
        context.allocTokenBuffer();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDoubleAllocConcatBuffer() {
        IOContext context = new IOContext(null, null, false);
        context.allocConcatBuffer();
        context.allocConcatBuffer();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferWrongBuffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = new byte[1024];
        context.releaseReadIOBuffer(buffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferWrongBuffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = new byte[1024];
        context.releaseWriteEncodingBuffer(buffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferWrongBuffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = new byte[1024];
        context.releaseBase64Buffer(buffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferWrongBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = new char[1024];
        context.releaseTokenBuffer(buffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseConcatBufferWrongBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = new char[1024];
        context.releaseConcatBuffer(buffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNameCopyBufferWrongBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = new char[1024];
        context.releaseNameCopyBuffer(buffer);
    }
}