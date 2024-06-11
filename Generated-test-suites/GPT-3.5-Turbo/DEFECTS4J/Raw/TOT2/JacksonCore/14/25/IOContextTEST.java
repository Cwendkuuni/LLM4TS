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
        assertEquals(sourceRef, ioContext.getSourceReference());
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testSetEncoding() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;

        IOContext ioContext = new IOContext(br, sourceRef, managedResource);

        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);

        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;

        IOContext ioContext = new IOContext(br, sourceRef, managedResource);

        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext newContext = ioContext.withEncoding(encoding);

        assertEquals(encoding, newContext.getEncoding());
    }

    // Add tests for other public methods, edge cases, and exception scenarios

}