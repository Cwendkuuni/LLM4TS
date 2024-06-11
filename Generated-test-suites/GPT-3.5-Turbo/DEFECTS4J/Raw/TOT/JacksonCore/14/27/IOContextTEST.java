import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import org.junit.Assert;
import org.junit.Test;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        // Prepare
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        // Execute
        ioContext.setEncoding(JsonEncoding.UTF8);

        // Assert
        Assert.assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        // Prepare
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        // Execute
        IOContext newContext = ioContext.withEncoding(JsonEncoding.UTF8);

        // Assert
        Assert.assertEquals(JsonEncoding.UTF8, newContext.getEncoding());
    }

    @Test
    public void testConstructTextBuffer() {
        // Prepare
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        // Execute
        TextBuffer textBuffer = ioContext.constructTextBuffer();

        // Assert
        Assert.assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        // Prepare
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        // Execute
        byte[] buffer = ioContext.allocReadIOBuffer();

        // Assert
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        // Prepare
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        // Execute
        byte[] buffer = ioContext.allocWriteEncodingBuffer();

        // Assert
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        // Prepare
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        // Execute
        byte[] buffer = ioContext.allocBase64Buffer();

        // Assert
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        // Prepare
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        // Execute
        char[] buffer = ioContext.allocTokenBuffer();

        // Assert
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        // Prepare
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        // Execute
        char[] buffer = ioContext.allocConcatBuffer();

        // Assert
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        // Prepare
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        // Execute
        char[] buffer = ioContext.allocNameCopyBuffer(10);

        // Assert
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        // Prepare
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        byte[] buffer = ioContext.allocReadIOBuffer();

        // Execute
        ioContext.releaseReadIOBuffer(buffer);

        // Assert
        Assert.assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        // Prepare
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();

        // Execute
        ioContext.releaseWriteEncodingBuffer(buffer);

        // Assert
        Assert.assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        // Prepare
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        byte[] buffer = ioContext.allocBase64Buffer();

        // Execute
        ioContext.releaseBase64Buffer(buffer);

        // Assert
        Assert.assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        // Prepare
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        char[] buffer = ioContext.allocTokenBuffer();

        // Execute
        ioContext.releaseTokenBuffer(buffer);

        // Assert
        Assert.assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        // Prepare
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        char[] buffer = ioContext.allocConcatBuffer();

        // Execute
        ioContext.releaseConcatBuffer(buffer);

        // Assert
        Assert.assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        // Prepare
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        char[] buffer = ioContext.allocNameCopyBuffer(10);

        // Execute
        ioContext.releaseNameCopyBuffer(buffer);

        // Assert
        Assert.assertNull(ioContext._nameCopyBuffer);
    }
}