import static org.junit.Assert.*;
import org.junit.Test;

public class IOContextTest {

    @Test
    public void testConstruction() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        
        assertNotNull(ioContext);
        assertSame(bufferRecycler, ioContext._bufferRecycler);
        assertSame(sourceRef, ioContext._sourceRef);
        assertTrue(ioContext._managedResource);
    }

    @Test
    public void testSetEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        JsonEncoding encoding = JsonEncoding.UTF8;
        
        ioContext.setEncoding(encoding);
        
        assertSame(encoding, ioContext._encoding);
    }

    @Test
    public void testWithEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        JsonEncoding encoding = JsonEncoding.UTF8;
        
        IOContext updatedContext = ioContext.withEncoding(encoding);
        
        assertSame(encoding, updatedContext._encoding);
    }

    // Add additional test cases to cover all methods in the IOContext class
    
}