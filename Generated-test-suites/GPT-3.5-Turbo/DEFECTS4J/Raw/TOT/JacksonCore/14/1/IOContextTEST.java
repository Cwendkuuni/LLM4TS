import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonEncoding;

public class IOContextTest {

    private IOContext context;

    @Before
    public void setUp() {
        context = new IOContext(new BufferRecycler(), new Object(), true);
    }

    @Test
    public void testSetEncoding() {
        // Typical case
        context.setEncoding(JsonEncoding.UTF16_BE);
        assertEquals(JsonEncoding.UTF16_BE, context.getEncoding());

        // Edge case
        context.setEncoding(JsonEncoding.UTF32_LE);
        assertEquals(JsonEncoding.UTF32_LE, context.getEncoding());

        // Error scenario
        context.setEncoding(JsonEncoding.UTF8);
        assertNotEquals(JsonEncoding.UTF16_BE, context.getEncoding());
   }

    @Test
    public void testWithEncoding() {
        // Typical case
        IOContext newContext = context.withEncoding(JsonEncoding.UTF8);
        assertNotSame(context, newContext);
        assertEquals(JsonEncoding.UTF8, newContext.getEncoding());

        // Edge case
        newContext = context.withEncoding(JsonEncoding.UTF16_LE);
        assertNotSame(context, newContext);
        assertEquals(JsonEncoding.UTF16_LE, newContext.getEncoding());

        // Error scenario
        newContext = context.withEncoding(JsonEncoding.UTF32_BE);
        assertNotEquals(JsonEncoding.UTF8, newContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        // Typical case
        Object sourceRef = new Object();
        context = new IOContext(new BufferRecycler(), sourceRef, true);
        assertEquals(sourceRef, context.getSourceReference());

        // Error scenario
        context = new IOContext(new BufferRecycler(), new Object(), true);
        assertNotEquals(sourceRef, context.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        // Typical case
        context = new IOContext(new BufferRecycler(), new Object(), true);
        assertTrue(context.isResourceManaged());

        // Edge case
        context = new IOContext(new BufferRecycler(), new Object(), false);
        assertFalse(context.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        // Typical case
        TextBuffer textBuffer = context.constructTextBuffer();
        assertNotNull(textBuffer);

        // Error scenario
        assertNull(context.constructTextBuffer());
    }

    @Test
    public void testAllocReadIOBuffer() {
        // Typical case
        byte[] buffer = context.allocReadIOBuffer();
        assertNotNull(buffer);

        // Error scenario
        assertNull(context.allocReadIOBuffer());
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        // Typical case
        byte[] buffer = context.allocWriteEncodingBuffer();
        assertNotNull(buffer);

        // Error scenario
        assertNull(context.allocWriteEncodingBuffer());
    }

    @Test
    public void testAllocBase64Buffer() {
        // Typical case
        byte[] buffer = context.allocBase64Buffer();
        assertNotNull(buffer);

        // Error scenario
        assertNull(context.allocBase64Buffer());
    }

    @Test
    public void testAllocTokenBuffer() {
        // Typical case
        char[] buffer = context.allocTokenBuffer();
        assertNotNull(buffer);

        // Error scenario
        assertNull(context.allocTokenBuffer());
    }

    @Test
    public void testAllocConcatBuffer() {
        // Typical case
        char[] buffer = context.allocConcatBuffer();
        assertNotNull(buffer);

        // Error scenario
        assertNull(context.allocConcatBuffer());
    }

    @Test
    public void testAllocNameCopyBuffer() {
        // Typical case
        char[] buffer = context.allocNameCopyBuffer(10);
        assertNotNull(buffer);

        // Error scenario
        assertNull(context.allocNameCopyBuffer());
    }

    @Test
    public void testReleaseReadIOBuffer() {
        // Typical case
        byte[] buffer = context.allocReadIOBuffer();
        assertNotNull(buffer);
        context.releaseReadIOBuffer(buffer);

        // Error scenario
        try {
            context.releaseReadIOBuffer(buffer);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        // Typical case
        byte[] buffer = context.allocWriteEncodingBuffer();
        assertNotNull(buffer);
        context.releaseWriteEncodingBuffer(buffer);

        // Error scenario
        try {
            context.releaseWriteEncodingBuffer(buffer);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testReleaseBase64Buffer() {
        // Typical case
        byte[] buffer = context.allocBase64Buffer();
        assertNotNull(buffer);
        context.releaseBase64Buffer(buffer);

        // Error scenario
        try {
            context.releaseBase64Buffer(buffer);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testReleaseTokenBuffer() {
        // Typical case
        char[] buffer = context.allocTokenBuffer();
        assertNotNull(buffer);
        context.releaseTokenBuffer(buffer);

        // Error scenario
        try {
            context.releaseTokenBuffer(buffer);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testReleaseConcatBuffer() {
        // Typical case
        char[] buffer = context.allocConcatBuffer();
        assertNotNull(buffer);
        context.releaseConcatBuffer(buffer);

        // Error scenario
        try {
            context.releaseConcatBuffer(buffer);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        // Typical case
        char[] buffer = context.allocNameCopyBuffer(10);
        assertNotNull(buffer);
        context.releaseNameCopyBuffer(buffer);

        // Error scenario
        try {
            context.releaseNameCopyBuffer(buffer);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}