import static org.junit.Assert.*;

import org.junit.Test;

public class JsonValueSerializerTest {

    @Test
    public void testConstructor() {
        AnnotatedMethod valueMethod = new AnnotatedMethod();
        JsonSerializer<?> serializer = new JsonSerializer<>();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(valueMethod, serializer);
        
        assertNotNull(jsonValueSerializer);
        assertEquals(valueMethod, jsonValueSerializer._accessorMethod);
        assertEquals(serializer, jsonValueSerializer._valueSerializer);
        assertNull(jsonValueSerializer._property);
        assertTrue(jsonValueSerializer._forceTypeInformation);
    }

    @Test
    public void testWithResolved() {
        AnnotatedMethod valueMethod = new AnnotatedMethod();
        JsonSerializer<?> serializer = new JsonSerializer<>();
        BeanProperty property = new BeanProperty();
        boolean forceTypeInfo = true;
        
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(valueMethod, serializer);
        JsonValueSerializer resolvedSerializer = jsonValueSerializer.withResolved(property, serializer, forceTypeInfo);
        
        assertNotNull(resolvedSerializer);
        assertEquals(property, resolvedSerializer._property);
        assertEquals(serializer, resolvedSerializer._valueSerializer);
        assertEquals(forceTypeInfo, resolvedSerializer._forceTypeInformation);
    }

    @Test
    public void testCreateContextual() {
        SerializerProvider provider = new SerializerProvider();
        BeanProperty property = new BeanProperty();
        
        AnnotatedMethod valueMethod = new AnnotatedMethod();
        JsonSerializer<?> serializer = new JsonSerializer<>();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(valueMethod, serializer);
        
        JsonSerializer<?> contextualSerializer = jsonValueSerializer.createContextual(provider, property);
        
        assertNotNull(contextualSerializer);
    }

    // Add more test cases for other methods

}