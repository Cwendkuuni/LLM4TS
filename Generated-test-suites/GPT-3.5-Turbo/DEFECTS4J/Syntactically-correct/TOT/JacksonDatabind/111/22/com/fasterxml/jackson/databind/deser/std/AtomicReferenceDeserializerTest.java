import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AtomicReferenceDeserializerTest {

    @Mock
    private DeserializationContext mockDeserializationContext;

    private AtomicReferenceDeserializer atomicReferenceDeserializer;

    @Test
    public void testGetNullValue() throws JsonMappingException {
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(mockDeserializationContext);
        
        assertNotNull(result);
    }

    @Test
    public void testGetEmptyValue() {
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object result = atomicReferenceDeserializer.getEmptyValue(mockDeserializationContext);
        
        assertNotNull(result);
        assertTrue(result instanceof AtomicReference);
    }

    @Test
    public void testReferenceValue() {
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object contents = new Object();
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
        
        assertNotNull(result);
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        Object result = atomicReferenceDeserializer.getReferenced(reference);
        
        assertEquals(contents, result);
    }

    @Test
    public void testUpdateReference() {
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>();
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, contents);
        
        assertNotNull(result);
        assertEquals(contents, result.get());
    }

    @Test
    public void testSupportsUpdate() {
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        DeserializationConfig config = mock(DeserializationConfig.class);
        when(config.getName()).thenReturn("TestConfig");
        
        Boolean result = atomicReferenceDeserializer.supportsUpdate(config);
        
        assertNotNull(result);
        assertTrue(result);
    }
}