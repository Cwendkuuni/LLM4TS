import static org.junit.Assert.*;

import org.junit.Test;

public class IOContextTest {

    @Test
    public void testConstructTextBuffer() {
        IOContext context = new IOContext(null, null, false);
        
        assertNotNull(context.constructTextBuffer());
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext context = new IOContext(null, null, false);
        
        assertNotNull(context.allocReadIOBuffer());
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext context = new IOContext(null, null, false);
        
        assertNotNull(context.allocReadIOBuffer(128));
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext context = new IOContext(null, null, false);
        
        assertNotNull(context.allocWriteEncodingBuffer());
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext context = new IOContext(null, null, false);
        
        assertNotNull(context.allocWriteEncodingBuffer(128));
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext context = new IOContext(null, null, false);
        
        assertNotNull(context.allocBase64Buffer());
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext context = new IOContext(null, null, false);
        
        assertNotNull(context.allocTokenBuffer());
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext context = new IOContext(null, null, false);
        
        assertNotNull(context.allocTokenBuffer(128));
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext context = new IOContext(null, null, false);
        
        assertNotNull(context.allocConcatBuffer());
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext context = new IOContext(null, null, false);
        
        assertNotNull(context.allocNameCopyBuffer(128));
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = context.allocReadIOBuffer();
        
        context.releaseReadIOBuffer(buffer);
        
        assertNull(context._readIOBuffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferWithWrongBuffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = new byte[1024];
        
        context.releaseReadIOBuffer(buffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = context.allocWriteEncodingBuffer();
        
        context.releaseWriteEncodingBuffer(buffer);
        
        assertNull(context._writeEncodingBuffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferWithWrongBuffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = new byte[1024];
        
        context.releaseWriteEncodingBuffer(buffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = context.allocBase64Buffer();
        
        context.releaseBase64Buffer(buffer);
        
        assertNull(context._base64Buffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferWithWrongBuffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = new byte[1024];
        
        context.releaseBase64Buffer(buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = context.allocTokenBuffer();
        
        context.releaseTokenBuffer(buffer);
        
        assertNull(context._tokenCBuffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferWithWrongBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = new char[1024];
        
        context.releaseTokenBuffer(buffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = context.allocConcatBuffer();
        
        context.releaseConcatBuffer(buffer);
        
        assertNull(context._concatCBuffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseConcatBufferWithWrongBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = new char[1024];
        
        context.releaseConcatBuffer(buffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = context.allocNameCopyBuffer(128);
        
        context.releaseNameCopyBuffer(buffer);
        
        assertNull(context._nameCopyBuffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNameCopyBufferWithWrongBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = new char[1024];
        
        context.releaseNameCopyBuffer(buffer);
    }

}