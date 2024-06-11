import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

public class CollectionDeserializerTest {
    
    @Test
    public void testConstructor() {
        JavaType collectionType = mock(JavaType.class);
        JsonDeserializer<Object> valueDeser = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeser = mock(TypeDeserializer.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeser, valueTypeDeser, valueInstantiator);
        
        assertEquals(collectionType, deserializer._collectionType);
        assertEquals(valueDeser, deserializer._valueDeserializer);
        assertEquals(valueTypeDeser, deserializer._valueTypeDeserializer);
        assertEquals(valueInstantiator, deserializer._valueInstantiator);
        assertNull(deserializer._delegateDeserializer);
        assertNull(deserializer._unwrapSingle);
    }
    
    @Test
    public void testDeserialixe() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<Object> result = new ArrayList<Object>();
        
        // TODO implement test case
    }
    
    // TODO implement test cases for other methods
    
}