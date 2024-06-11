import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    @Mock
    private JavaType mockJavaType;
    @Mock
    private ValueInstantiator mockValueInstantiator;
    @Mock
    private TypeDeserializer mockTypeDeserializer;
    @Mock
    private JsonDeserializer<?> mockValueDeserializer;
    @Mock
    private DeserializationContext mockDeserializationContext;
    @Mock
    private JsonMappingException mockJsonMappingException;

    private AtomicReferenceDeserializer atomicReferenceDeserializer;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(mockJavaType, mockValueInstantiator,
                mockTypeDeserializer, mockValueDeserializer);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(mockDeserializationContext);
        assertNull(result.get());
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReference<Object> result = atomicReferenceDeserializer.getEmptyValue(mockDeserializationContext);
        assertNull(result.get());
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        Object result = atomicReferenceDeserializer.getReferenced(reference);
        assertEquals(contents, result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, contents);
        assertEquals(contents, result.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig mockDeserializationConfig = mock(DeserializationConfig.class);
        Boolean result = atomicReferenceDeserializer.supportsUpdate(mockDeserializationConfig);
        assertTrue(result);
    }
}