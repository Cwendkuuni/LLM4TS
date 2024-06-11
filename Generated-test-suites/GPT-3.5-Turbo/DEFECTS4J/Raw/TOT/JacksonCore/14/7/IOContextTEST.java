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
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        ioContext = new IOContext(br, sourceRef, managedResource);
    }

    @Test
    public void testSetEncoding() {
        JsonEncoding enc = JsonEncoding.UTF8;
        ioContext.setEncoding(enc);

        assertSame(enc, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        JsonEncoding enc = JsonEncoding.UTF8;
        IOContext withEncodingContext = ioContext.withEncoding(JsonEncoding.UTF8);

        assertSame(enc, withEncodingContext.getEncoding());
        assertNotSame(withEncodingContext, ioContext);
    }

    @Test
    public void testGetSourceReference() {
        Object sourceRef = new Object();
        ioContext = new IOContext(new BufferRecycler(), sourceRef, true);

        assertSame(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        boolean managedResource = true;
        ioContext = new IOContext(new BufferRecycler(), new Object(), managedResource);

        assertTrue(ioContext.isResourceManaged());
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
        int minSize = 10;
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
        int minSize = 10;
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
        int minSize = 10;
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);

        assertNotNull(tokenBuffer);
        assertTrue(tokenBuffer.length >= minSize);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocTokenBuffer();

        assertNotNull(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 10;
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);

        assertNotNull(nameCopyBuffer);
        assertTrue(nameCopyBuffer.length >= minSize);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);

        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);

        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);

        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);

        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);

        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);

        assertNull(ioContext._nameCopyBuffer);
    }
}