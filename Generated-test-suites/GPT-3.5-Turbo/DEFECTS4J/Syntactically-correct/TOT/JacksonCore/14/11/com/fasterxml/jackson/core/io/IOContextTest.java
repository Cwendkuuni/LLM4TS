import org.junit.Test;

import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        // Test case: set encoding to UTF-8
        IOContext context = new IOContext(null, null, true);
        context.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, context.getEncoding());

        // Test case: set encoding to UTF-16
        context.setEncoding(JsonEncoding.UTF16_BE);
        assertEquals(JsonEncoding.UTF16_BE, context.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        // Test case: create a new IOContext with UTF-8 encoding
        IOContext context1 = new IOContext(null, null, true);
        IOContext context2 = context1.withEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, context2.getEncoding());

        // Test case: create a new IOContext with UTF-16 encoding
        IOContext context3 = context1.withEncoding(JsonEncoding.UTF16_LE);
        assertEquals(JsonEncoding.UTF16_LE, context3.getEncoding());
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext context = new IOContext(null, null, true);
        TextBuffer textBuffer = context.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext context = new IOContext(null, null, true);

        // Test case: default buffer allocation
        byte[] buffer1 = context.allocReadIOBuffer();
        assertNotNull(buffer1);

        // Test case: buffer allocation with minimum size
        byte[] buffer2 = context.allocReadIOBuffer(1024);
        assertNotNull(buffer2);
        assertEquals(1024, buffer2.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext context = new IOContext(null, null, true);

        // Test case: default buffer allocation
        byte[] buffer1 = context.allocWriteEncodingBuffer();
        assertNotNull(buffer1);

        // Test case: buffer allocation with minimum size
        byte[] buffer2 = context.allocWriteEncodingBuffer(1024);
        assertNotNull(buffer2);
        assertEquals(1024, buffer2.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocBase64Buffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext context = new IOContext(null, null, true);

        // Test case: default buffer allocation
        char[] buffer1 = context.allocTokenBuffer();
        assertNotNull(buffer1);

        // Test case: buffer allocation with minimum size
        char[] buffer2 = context.allocTokenBuffer(1024);
        assertNotNull(buffer2);
        assertEquals(1024, buffer2.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext context = new IOContext(null, null, true);
        char[] buffer = context.allocConcatBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext context = new IOContext(null, null, true);

        // Test case: buffer allocation with minimum size
        char[] buffer = context.allocNameCopyBuffer(1024);
        assertNotNull(buffer);
        assertEquals(1024, buffer.length);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocReadIOBuffer();
        context.releaseReadIOBuffer(buffer);
        assertNull(context._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocWriteEncodingBuffer();
        context.releaseWriteEncodingBuffer(buffer);
        assertNull(context._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocBase64Buffer();
        context.releaseBase64Buffer(buffer);
        assertNull(context._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext context = new IOContext(null, null, true);
        char[] buffer = context.allocTokenBuffer();
        context.releaseTokenBuffer(buffer);
        assertNull(context._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext context = new IOContext(null, null, true);
        char[] buffer = context.allocConcatBuffer();
        context.releaseConcatBuffer(buffer);
        assertNull(context._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext context = new IOContext(null, null, true);
        char[] buffer = context.allocNameCopyBuffer(1024);
        context.releaseNameCopyBuffer(buffer);
        assertNull(context._nameCopyBuffer);
    }

}