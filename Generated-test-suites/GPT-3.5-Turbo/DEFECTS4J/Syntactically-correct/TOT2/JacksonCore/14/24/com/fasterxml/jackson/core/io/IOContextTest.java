import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.io.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IOContextTest {

    private IOContext ioContext;

    @Before
    public void setUp() {
        ioContext = new IOContext(new BufferRecycler(), "TestSource", true);
    }

    @Test
    public void testSetEncoding() {
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext context = ioContext.withEncoding(JsonEncoding.UTF16);
        assertEquals(JsonEncoding.UTF16, context.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        assertEquals("TestSource", ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocReadIOBufferWithSize() {
        byte[] buffer = ioContext.allocReadIOBuffer(1024);
        assertNotNull(buffer);
        assertEquals(1024, buffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithSize() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer(2048);
        assertNotNull(buffer);
        assertEquals(2048, buffer.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBufferWithSize() {
        char[] buffer = ioContext.allocTokenBuffer(512);
        assertNotNull(buffer);
        assertEquals(512, buffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBufferWithSize() {
        char[] buffer = ioContext.allocNameCopyBuffer(256);
        assertNotNull(buffer);
        assertEquals(256, buffer.length);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer(1024);
        ioContext.releaseReadIOBuffer(buffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer(2048);
        ioContext.releaseWriteEncodingBuffer(buffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer(512);
        ioContext.releaseTokenBuffer(buffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] buffer = ioContext.allocNameCopyBuffer(256);
        ioContext.releaseNameCopyBuffer(buffer);
    }
}