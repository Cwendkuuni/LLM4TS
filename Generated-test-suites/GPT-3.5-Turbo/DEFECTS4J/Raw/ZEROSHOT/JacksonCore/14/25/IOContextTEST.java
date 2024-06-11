import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testGetSourceReference() {
        IOContext context = new IOContext(null, new Object(), false);
        assertNotNull(context.getSourceReference());
    }

    @Test
    public void testSetEncoding() {
        IOContext context = new IOContext(null, new Object(), false);
        context.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, context.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext context = new IOContext(null, new Object(), false);
        context.withEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, context.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        IOContext context = new IOContext(null, new Object(), true);
        assertTrue(context.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext context = new IOContext(null, new Object(), true);
        assertNotNull(context.constructTextBuffer());
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext context = new IOContext(null, new Object(), true);
        assertNotNull(context.allocReadIOBuffer());
    }

    @Test
    public void testAllocReadIOBufferWithSize() {
        IOContext context = new IOContext(null, new Object(), true);
        assertNotNull(context.allocReadIOBuffer(32));
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext context = new IOContext(null, new Object(), true);
        assertNotNull(context.allocWriteEncodingBuffer());
    }

    @Test
    public void testAllocWriteEncodingBufferWithSize() {
        IOContext context = new IOContext(null, new Object(), true);
        assertNotNull(context.allocWriteEncodingBuffer(32));
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext context = new IOContext(null, new Object(), true);
        assertNotNull(context.allocBase64Buffer());
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext context = new IOContext(null, new Object(), true);
        assertNotNull(context.allocTokenBuffer());
    }

    @Test
    public void testAllocTokenBufferWithSize() {
        IOContext context = new IOContext(null, new Object(), true);
        assertNotNull(context.allocTokenBuffer(64));
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext context = new IOContext(null, new Object(), true);
        assertNotNull(context.allocConcatBuffer());
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext context = new IOContext(null, new Object(), true);
        assertNotNull(context.allocNameCopyBuffer(64));
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext context = new IOContext(null, new Object(), true);
        byte[] buffer = context.allocReadIOBuffer();
        context.releaseReadIOBuffer(buffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext context = new IOContext(null, new Object(), true);
        byte[] buffer = context.allocWriteEncodingBuffer();
        context.releaseWriteEncodingBuffer(buffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext context = new IOContext(null, new Object(), true);
        byte[] buffer = context.allocBase64Buffer();
        context.releaseBase64Buffer(buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext context = new IOContext(null, new Object(), true);
        char[] buffer = context.allocTokenBuffer();
        context.releaseTokenBuffer(buffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext context = new IOContext(null, new Object(), true);
        char[] buffer = context.allocConcatBuffer();
        context.releaseConcatBuffer(buffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext context = new IOContext(null, new Object(), true);
        char[] buffer = context.allocNameCopyBuffer(64);
        context.releaseNameCopyBuffer(buffer);
    }
}