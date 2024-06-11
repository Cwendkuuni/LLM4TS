import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import com.fasterxml.jackson.core.io.IOContext;

public class IOContextTest {

    private IOContext ioContext;
    private BufferRecycler bufferRecycler;

    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        ioContext = new IOContext(bufferRecycler, new Object(), false);
    }

    @After
    public void tearDown() {
        ioContext = null;
        bufferRecycler = null;
    }

    @Test
    public void testSetEncoding() {
        assertNull(ioContext.getEncoding());
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        assertNull(ioContext.getEncoding());
        IOContext updatedContext = ioContext.withEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, updatedContext.getEncoding());
    }
    
    @Test
    public void testGetSourceReference() {
        Object sourceRef = new Object();
        ioContext = new IOContext(bufferRecycler, sourceRef, false);
        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        assertFalse(ioContext.isResourceManaged());
    }
    
    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        // Validate that a TextBuffer was created
        assertTrue(textBuffer instanceof TextBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        // Validate that a byte buffer was created
        assertTrue(buffer instanceof byte[]);
        // Validate that the allocated buffer is the same as the one stored in the IOContext
        assertEquals(buffer, ioContext._readIOBuffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 1024;
        byte[] buffer = ioContext.allocReadIOBuffer(minSize);
        // Validate that a byte buffer with minimum size was created
        assertTrue(buffer instanceof byte[]);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        // Validate that a byte buffer was created
        assertTrue(buffer instanceof byte[]);
        // Validate that the allocated buffer is the same as the one stored in the IOContext
        assertEquals(buffer, ioContext._writeEncodingBuffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 1024;
        byte[] buffer = ioContext.allocWriteEncodingBuffer(minSize);
        // Validate that a byte buffer with minimum size was created
        assertTrue(buffer instanceof byte[]);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        // Validate that a byte buffer was created
        assertTrue(buffer instanceof byte[]);
        // Validate that the allocated buffer is the same as the one stored in the IOContext
        assertEquals(buffer, ioContext._base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        // Validate that a char buffer was created
        assertTrue(buffer instanceof char[]);
        // Validate that the allocated buffer is the same as the one stored in the IOContext
        assertEquals(buffer, ioContext._tokenCBuffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 1024;
        char[] buffer = ioContext.allocTokenBuffer(minSize);
        // Validate that a char buffer with minimum size was created
        assertTrue(buffer instanceof char[]);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        // Validate that a char buffer was created
        assertTrue(buffer instanceof char[]);
        // Validate that the allocated buffer is the same as the one stored in the IOContext
        assertEquals(buffer, ioContext._concatCBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 1024;
        char[] buffer = ioContext.allocNameCopyBuffer(minSize);
        // Validate that a char buffer was created
        assertTrue(buffer instanceof char[]);
        // Validate that the allocated buffer is the same as the one stored in the IOContext
        assertEquals(buffer, ioContext._nameCopyBuffer);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        // Allocate a read IO buffer
        byte[] buffer = ioContext.allocReadIOBuffer();

        // Release the buffer
        ioContext.releaseReadIOBuffer(buffer);

        // Validate that the buffer stored in IOContext is null after releasing
        assertNull(ioContext._readIOBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferWithDifferentBuffer() {
        // Allocate a read IO buffer
        byte[] buffer = ioContext.allocReadIOBuffer();

        // Attempt to release a different buffer
        ioContext.releaseReadIOBuffer(new byte[1024]);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        // Allocate a write encoding buffer
        byte[] buffer = ioContext.allocWriteEncodingBuffer();

        // Release the buffer
        ioContext.releaseWriteEncodingBuffer(buffer);

        // Validate that the buffer stored in IOContext is null after releasing
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferWithDifferentBuffer() {
        // Allocate a write encoding buffer
        byte[] buffer = ioContext.allocWriteEncodingBuffer();

        // Attempt to release a different buffer
        ioContext.releaseWriteEncodingBuffer(new byte[1024]);
    }

    @Test
    public void testReleaseBase64Buffer() {
        // Allocate a base64 buffer
        byte[] buffer = ioContext.allocBase64Buffer();

        // Release the buffer
        ioContext.releaseBase64Buffer(buffer);

        // Validate that the buffer stored in IOContext is null after releasing
        assertNull(ioContext._base64Buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferWithDifferentBuffer() {
        // Allocate a base64 buffer
        byte[] buffer = ioContext.allocBase64Buffer();

        // Attempt to release a different buffer
        ioContext.releaseBase64Buffer(new byte[1024]);
    }

    @Test
    public void testReleaseTokenBuffer() {
        // Allocate a token buffer
        char[] buffer = ioContext.allocTokenBuffer();

        // Release the buffer
        ioContext.releaseTokenBuffer(buffer);

        // Validate that the buffer stored in IOContext is null after releasing
        assertNull(ioContext._tokenCBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferWithDifferentBuffer() {
        // Allocate a token buffer
        char[] buffer = ioContext.allocTokenBuffer();

        // Attempt to release a different buffer
        ioContext.releaseTokenBuffer(new char[1024]);
    }

    @Test
    public void testReleaseConcatBuffer() {
        // Allocate a concat buffer
        char[] buffer = ioContext.allocConcatBuffer();

        // Release the buffer
        ioContext.releaseConcatBuffer(buffer);

        // Validate that the buffer stored in IOContext is null after releasing
        assertNull(ioContext._concatCBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseConcatBufferWithDifferentBuffer() {
        // Allocate a concat buffer
        char[] buffer = ioContext.allocConcatBuffer();

        // Attempt to release a different buffer
        ioContext.releaseTokenBuffer(new char[1024]);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        // Allocate a name copy buffer
        char[] buffer = ioContext.allocNameCopyBuffer(1024);

        // Release the buffer
        ioContext.releaseNameCopyBuffer(buffer);

        // Validate that the buffer stored in IOContext is null after releasing
        assertNull(ioContext._nameCopyBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNameCopyBufferWithDifferentBuffer() {
        // Allocate a name copy buffer
        char[] buffer = ioContext.allocNameCopyBuffer(1024);

        // Attempt to release a different buffer
        ioContext.releaseTokenBuffer(new char[1024]);
    }
}