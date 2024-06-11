import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
        JsonEncoding enc = JsonEncoding.UTF16_BE;
        ioContext.setEncoding(enc);
        assertSame(enc, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        JsonEncoding enc = JsonEncoding.UTF8;
        IOContext newContext = ioContext.withEncoding(enc);
        assertEquals(enc, newContext.getEncoding());
        assertSame(ioContext.getSourceReference(), newContext.getSourceReference());
        assertEquals(ioContext.isResourceManaged(), newContext.isResourceManaged());
    }

    @Test
    public void testGetSourceReference() {
        assertSame(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        assertNull(ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertEquals(textBuffer.getBufferRecycler(), bufferRecycler);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertEquals(buffer.length, BufferRecycler.BYTE_READ_IO_BUFFER);
        ioContext.releaseReadIOBuffer(buffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 100;
        byte[] buffer = ioContext.allocReadIOBuffer(minSize);
        assertEquals(buffer.length, minSize);
        ioContext.releaseReadIOBuffer(buffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertEquals(buffer.length, BufferRecycler.BYTE_WRITE_ENCODING_BUFFER);
        ioContext.releaseWriteEncodingBuffer(buffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 200;
        byte[] buffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertEquals(buffer.length, minSize);
        ioContext.releaseWriteEncodingBuffer(buffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        assertEquals(buffer.length, BufferRecycler.BYTE_BASE64_CODEC_BUFFER);
        ioContext.releaseBase64Buffer(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        assertEquals(buffer.length, BufferRecycler.CHAR_TOKEN_BUFFER);
        ioContext.releaseTokenBuffer(buffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 300;
        char[] buffer = ioContext.allocTokenBuffer(minSize);
        assertEquals(buffer.length, minSize);
        ioContext.releaseTokenBuffer(buffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        assertEquals(buffer.length, BufferRecycler.CHAR_CONCAT_BUFFER);
        ioContext.releaseConcatBuffer(buffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 400;
        char[] buffer = ioContext.allocNameCopyBuffer(minSize);
        assertEquals(buffer.length, minSize);
        ioContext.releaseNameCopyBuffer(buffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testVerifyAlloc() {
        byte[] buffer1 = ioContext.allocReadIOBuffer();
        ioContext._verifyAlloc(buffer1);
        byte[] buffer2 = ioContext.allocWriteEncodingBuffer();
        ioContext._verifyAlloc(buffer2);
    }

    @Test
    public void testVerifyReleaseByte() {
        byte[] buffer = new byte[10];
        ioContext._verifyRelease(buffer, buffer);
    }

    @Test
    public void testVerifyReleaseChar() {
        char[] buffer = new char[10];
        ioContext._verifyRelease(buffer, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyReleaseByteWithWrongBuf() {
        byte[] buffer = new byte[10];
        ioContext._verifyRelease(buffer, new byte[20]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyReleaseCharWithWrongBuf() {
        char[] buffer = new char[10];
        ioContext._verifyRelease(buffer, new char[20]);
    }
}