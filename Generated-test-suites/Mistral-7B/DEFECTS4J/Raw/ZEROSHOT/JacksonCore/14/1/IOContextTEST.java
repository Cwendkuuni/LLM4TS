import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IOContextTest {

    @Mock
    private BufferRecycler bufferRecycler;

    @InjectMocks
    private IOContext ioContext;

    @Test
    public void testIOContextConstruction() {
        ioContext = new IOContext(bufferRecycler, null, false);
        assertNotNull(ioContext.getSourceReference());
        assertNotNull(ioContext.getEncoding());
        assertFalse(ioContext.isResourceManaged());
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
        ioContext = ioContext.withEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(10);
        assertNotNull(readIOBuffer);
        assertEquals(10, readIOBuffer.length);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext.getReadIOBuffer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferWithDifferentBuffer() {
        byte[] readIOBuffer = new byte[10];
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(10);
        assertNotNull(writeEncodingBuffer);
        assertEquals(10, writeEncodingBuffer.length);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(ioContext.getWriteEncodingBuffer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferWithDifferentBuffer() {
        byte[] writeEncodingBuffer = new byte[10];
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
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
        char[] tokenBuffer = ioContext.allocTokenBuffer(10);
        assertNotNull(tokenBuffer);
        assertEquals(10, tokenBuffer.length);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
        assertNull(ioContext.getTokenBuffer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferWithDifferentBuffer() {
        char[] tokenBuffer = new char[10];
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
        assertNull(ioContext.getConcatBuffer());
    }

    @Test
    public void testAllocNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        assertNotNull(nameCopyBuffer);
        assertEquals(10, nameCopyBuffer.length);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(ioContext.getNameCopyBuffer());
    }

    private JsonEncoding encoding;

    private void initMocks() {
        encoding = JsonEncoding.UTF8;
        when(bufferRecycler.allocByteBuffer(BufferRecycler.BYTE_READ_IO_BUFFER)).thenReturn(new byte[10]);
        when(bufferRecycler.allocByteBuffer(BufferRecycler.BYTE_READ_IO_BUFFER, 10)).thenReturn(new byte[10]);
        when(bufferRecycler.allocByteBuffer(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER)).thenReturn(new byte[10]);
        when(bufferRecycler.allocByteBuffer(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, 10)).thenReturn(new byte[10]);
        when(bufferRecycler.allocByteBuffer(BufferRecycler.BYTE_BASE64_CODEC_BUFFER)).thenReturn(new byte[10]);
        when(bufferRecycler.allocCharBuffer(BufferRecycler.CHAR_TOKEN_BUFFER)).thenReturn(new char[10]);
        when(bufferRecycler.allocCharBuffer(BufferRecycler.CHAR_TOKEN_BUFFER, 10)).thenReturn(new char[10]);
        when(bufferRecycler.allocCharBuffer(BufferRecycler.CHAR_CONCAT_BUFFER)).thenReturn(new char[10]);
        when(bufferRecycler.allocCharBuffer(BufferRecycler.CHAR_NAME_COPY_BUFFER)).thenReturn(new char[10]);
        when(bufferRecycler.allocCharBuffer(BufferRecycler.CHAR_NAME_COPY_BUFFER, 10)).thenReturn(new char[10]);
        when(bufferRecycler.releaseByteBuffer(BufferRecycler.BYTE_READ_IO_BUFFER, (byte[]) anyObject())).thenReturn(true);
        when(bufferRecycler.releaseByteBuffer(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, (byte[]) anyObject())).thenReturn(true);
        when(bufferRecycler.releaseByteBuffer(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, (byte[]) anyObject())).thenReturn(true);
        when(bufferRecycler.releaseCharBuffer(BufferRecycler.CHAR_TOKEN_BUFFER, (char[]) anyObject())).thenReturn(true);
        when(bufferRecycler.releaseCharBuffer(BufferRecycler.CHAR_CONCAT_BUFFER, (char[]) anyObject())).thenReturn(true);
        when(bufferRecycler.releaseCharBuffer(BufferRecycler.CHAR_NAME_COPY_BUFFER, (char[]) anyObject())).thenReturn(true);
    }

    @Test
    public void testAllBuffersUsed() {
        initMocks();
        ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        char[] concatBuffer = ioContext.allocConcatBuffer();
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);

        ioContext.releaseReadIOBuffer(readIOBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        ioContext.releaseBase64Buffer(base64Buffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);

        assertNull(ioContext.getReadIOBuffer());
        assertNull(ioContext.getWriteEncodingBuffer());
        assertNull(ioContext.getBase64Buffer());
        assertNull(ioContext.getTokenBuffer());
        assertNull(ioContext.getConcatBuffer());
        assertNull(ioContext.getNameCopyBuffer());
    }
}