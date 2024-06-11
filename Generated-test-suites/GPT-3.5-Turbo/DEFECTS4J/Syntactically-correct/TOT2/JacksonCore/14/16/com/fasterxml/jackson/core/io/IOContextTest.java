import static org.junit.Assert.*;
import org.junit.Test;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
        context.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, context.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
        IOContext newContext = context.withEncoding(JsonEncoding.UTF16_BE);
        assertEquals(JsonEncoding.UTF16_BE, newContext.getEncoding());
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
        TextBuffer textBuffer = context.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] buffer = context.allocReadIOBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] buffer = context.allocWriteEncodingBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] buffer = context.allocBase64Buffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
        char[] buffer = context.allocTokenBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
        char[] buffer = context.allocConcatBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
        char[] buffer = context.allocNameCopyBuffer(10);
        assertNotNull(buffer);
    }

    // Add test cases for release methods as well

}