import static org.junit.Assert.*;

import org.junit.Test;

public class IOContextTest {

    @Test
    public void testConstructor() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;

        IOContext ioContext = new IOContext(br, sourceRef, managedResource);

        assertNotNull(ioContext);
        assertEquals(br, ioContext._bufferRecycler);
        assertEquals(sourceRef, ioContext._sourceRef);
        assertTrue(ioContext._managedResource);
    }

    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        JsonEncoding encoding = JsonEncoding.UTF8;

        ioContext.setEncoding(encoding);

        assertEquals(encoding, ioContext._encoding);
    }

    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        JsonEncoding encoding = JsonEncoding.UTF16_BE;

        IOContext updatedContext = ioContext.withEncoding(encoding);

        assertEquals(encoding, updatedContext._encoding);
    }

    @Test
    public void testGetters() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;

        IOContext ioContext = new IOContext(br, sourceRef, managedResource);

        assertEquals(sourceRef, ioContext.getSourceReference());
        assertEquals(null, ioContext.getEncoding());
        assertTrue(ioContext.isResourceManaged());
    }

    // Add more test cases for other methods as needed...

}