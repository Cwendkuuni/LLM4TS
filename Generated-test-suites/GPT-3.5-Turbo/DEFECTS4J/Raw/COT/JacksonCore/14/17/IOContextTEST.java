import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import org.junit.Test;

import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void getSourceReference() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        assertEquals("source", ioContext.getSourceReference());
    }

    @Test
    public void getEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        assertNull(ioContext.getEncoding());

        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void isResourceManaged() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        assertTrue(ioContext.isResourceManaged());

        ioContext = new IOContext(new BufferRecycler(), "source", false);
        assertFalse(ioContext.isResourceManaged());
    }

    @Test
    public void constructTextBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        TextBuffer textBuffer = ioContext.constructTextBuffer();

        assertNotNull(textBuffer);
    }

    @Test
    public void allocReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocReadIOBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void allocReadIOBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocReadIOBuffer(10);

        assertNotNull(buffer);
        assertTrue(buffer.length >= 10);
    }

    @Test
    public void allocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void allocWriteEncodingBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer(10);

        assertNotNull(buffer);
        assertTrue(buffer.length >= 10);
    }

    @Test
    public void allocBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocBase64Buffer();

        assertNotNull(buffer);
    }

    @Test
    public void allocTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocTokenBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void allocTokenBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocTokenBuffer(10);

        assertNotNull(buffer);
        assertTrue(buffer.length >= 10);
    }

    @Test
    public void allocConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocConcatBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void allocNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocNameCopyBuffer(10);

        assertNotNull(buffer);
        assertTrue(buffer.length >= 10);
    }

    @Test
    public void releaseReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocReadIOBuffer();

        ioContext.releaseReadIOBuffer(buffer);
    }

    @Test
    public void releaseWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();

        ioContext.releaseWriteEncodingBuffer(buffer);
    }

    @Test
    public void releaseBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocBase64Buffer();

        ioContext.releaseBase64Buffer(buffer);
    }

    @Test
    public void releaseTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocTokenBuffer();

        ioContext.releaseTokenBuffer(buffer);
    }

    @Test
    public void releaseConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocConcatBuffer();

        ioContext.releaseConcatBuffer(buffer);
    }

    @Test
    public void releaseNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocNameCopyBuffer(10);

        ioContext.releaseNameCopyBuffer(buffer);
    }

}