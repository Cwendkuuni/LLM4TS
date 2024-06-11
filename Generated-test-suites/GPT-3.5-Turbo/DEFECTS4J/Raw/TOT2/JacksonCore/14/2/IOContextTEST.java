import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testIOContextConstructor() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);

        assertNotNull(ioContext);
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
        IOContext updatedContext = ioContext.withEncoding(encoding);

        assertEquals(encoding, updatedContext.getEncoding());
    }

    // Additional test cases for the buffer management methods can be added here
}