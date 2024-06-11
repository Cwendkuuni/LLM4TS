import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import org.junit.Before;
import org.junit.Test;

public class IOContextTest {
    private IOContext ioContext;
    private BufferRecycler bufferRecycler;
    private Object sourceRef;
    private boolean managedResource;

    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        sourceRef = new Object();
        managedResource = true;
        ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
    }

    @Test
    public void testSetEncoding() {
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext result = ioContext.withEncoding(JsonEncoding.UTF16_BE);
        assertEquals(JsonEncoding.UTF16_BE, result.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        assertEquals(managedResource, ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertEquals(TextBuffer.class, textBuffer.getClass());
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] result = ioContext.allocReadIOBuffer();
        assertEquals(0, result.length);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 10;
        byte[] result = ioContext.allocReadIOBuffer(minSize);
        assertEquals(minSize, result.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] result = ioContext.allocWriteEncodingBuffer();
        assertEquals(0, result.length);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 10;
        byte[] result = ioContext.allocWriteEncodingBuffer(minSize);
        assertEquals(minSize, result.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] result = ioContext.allocBase64Buffer();
        assertEquals(0, result.length);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] result = ioContext.allocTokenBuffer();
        assertEquals(0, result.length);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 10;
        char[] result = ioContext.allocTokenBuffer(minSize);
        assertEquals(minSize, result.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] result = ioContext.allocConcatBuffer();
        assertEquals(0, result.length);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 10;
        char[] result = ioContext.allocNameCopyBuffer(minSize);
        assertEquals(minSize, result.length);
    }

    @Test(expected = IllegalStateException.class)
    public void testDoubleAllocReadIOBuffer() {
        ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test(expected = IllegalStateException.class)
    public void testDoubleAllocWriteEncodingBuffer() {
        ioContext.allocWriteEncodingBuffer();
        ioContext.allocWriteEncodingBuffer();
    }

    @Test(expected = IllegalStateException.class)
    public void testDoubleAllocBase64Buffer() {
        ioContext.allocBase64Buffer();
        ioContext.allocBase64Buffer();
    }

    @Test(expected = IllegalStateException.class)
    public void testDoubleAllocTokenBuffer() {
        ioContext.allocTokenBuffer();
        ioContext.allocTokenBuffer();
    }

    @Test(expected = IllegalStateException.class)
    public void testDoubleAllocConcatBuffer() {
        ioContext.allocConcatBuffer();
        ioContext.allocConcatBuffer();
    }

    @Test(expected = IllegalStateException.class)
    public void testDoubleAllocNameCopyBuffer() {
        ioContext.allocNameCopyBuffer(10);
        ioContext.allocNameCopyBuffer(10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferWithDifferentBuffer() {
        byte[] buffer = new byte[10];
        ioContext.releaseReadIOBuffer(buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferWithDifferentBuffer() {
        byte[] buffer = new byte[10];
        ioContext.releaseWriteEncodingBuffer(buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferWithDifferentBuffer() {
        byte[] buffer = new byte[10];
        ioContext.releaseBase64Buffer(buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferWithDifferentBuffer() {
        char[] buffer = new char[10];
        ioContext.releaseTokenBuffer(buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseConcatBufferWithDifferentBuffer() {
        char[] buffer = new char[10];
        ioContext.releaseConcatBuffer(buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNameCopyBufferWithDifferentBuffer() {
        char[] buffer = new char[10];
        ioContext.releaseNameCopyBuffer(buffer);
    }
}