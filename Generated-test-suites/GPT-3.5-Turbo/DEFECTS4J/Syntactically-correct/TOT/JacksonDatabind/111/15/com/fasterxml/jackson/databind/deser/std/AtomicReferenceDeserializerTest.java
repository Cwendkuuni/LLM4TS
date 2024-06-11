import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.ValueInstantiator.Gettable;
import com.fasterxml.jackson.databind.deser.ValueInstantiator.Creator;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import com.fasterxml.jackson.databind.deser.std.ReferenceTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest {
    private AtomicReferenceDeserializer atomicReferenceDeserializer;
    
    @Mock
    private JavaType fullType;
    
    @Mock
    private ValueInstantiator inst;
    
    @Mock
    private TypeDeserializer typeDeser;
    
    @Mock
    private JsonDeserializer<?> deser;
    
    @Mock
    private DeserializationContext ctxt;
    
    @Before
    public void setUp() {
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
    }
    
    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer newDeserializer = atomicReferenceDeserializer.withResolved(typeDeser, deser);
        assertEquals(fullType, newDeserializer._fullType);
        assertEquals(inst, newDeserializer._valueInstantiator);
        assertEquals(typeDeser, newDeserializer._valueTypeDeserializer);
        assertEquals(deser, newDeserializer._valueDeserializer);
    }
    
    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReference<Object> nullValue = atomicReferenceDeserializer.getNullValue(ctxt);
        assertEquals(new AtomicReference<Object>(), nullValue);
    }
    
    @Test
    public void testGetEmptyValue() {
        Object emptyValue = atomicReferenceDeserializer.getEmptyValue(ctxt);
        assertEquals(new AtomicReference<Object>(), emptyValue);
    }
    
    @Test
    public void testReferenceValue() {
        Object contents = "test";
        AtomicReference<Object> referenceValue = atomicReferenceDeserializer.referenceValue(contents);
        assertEquals(new AtomicReference<Object>(contents), referenceValue);
    }
    
    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<Object>("test");
        Object referenced = atomicReferenceDeserializer.getReferenced(reference);
        assertEquals("test", referenced);
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<Object>();
        Object contents = "test";
        
        AtomicReference<Object> updatedReference = atomicReferenceDeserializer.updateReference(reference, contents);
        assertEquals(contents, updatedReference.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        Boolean supportsUpdate = atomicReferenceDeserializer.supportsUpdate(config);
        assertEquals(Boolean.TRUE, supportsUpdate);
    }
}