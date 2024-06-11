
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IOContextTest {
    private IOContext ioContext;

    @Before
    public void setUp() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
    }

    @Test
    public void testSetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);

        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.withEncoding(encoding);

        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        Object sourceRef = ioContext.getSourceReference();

        assertNotNull(sourceRef);
    }

    @Test
    public void testIsResourceManaged() {
        boolean managedResource = ioContext.isResourceManaged();

        assertTrue(managedResource);
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();

        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();

        assertNotNull(readIOBuffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 128;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);

        assertNotNull(readIOBuffer);
        assertTrue(readIOBuffer.length >= minSize);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();

        assertNotNull(writeEncodingBuffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 128;
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);

        assertNotNull(writeEncodingBuffer);
        assertTrue(writeEncodingBuffer.length >= minSize);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();

        assertNotNull(base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();

        assertNotNull(tokenBuffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 128;
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);

        assertNotNull(tokenBuffer);
        assertTrue(tokenBuffer.length >= minSize);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();

        assertNotNull(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 128;
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);

        assertNotNull(nameCopyBuffer);
        assertTrue(nameCopyBuffer.length >= minSize);
    }
}
