import static org.junit.Assert.*;
import org.junit.Test;

public class InnerClassPropertyTest {
    
    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<Object>();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        
        assertNotNull(property);
        assertEquals(delegate, property._delegate);
        assertEquals(ctor, property._creator);
    }
    
    @Test
    public void testDeserializesAndSet() {
        InnerClassProperty property = new InnerClassProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        
        // Test with JsonToken.VALUE_NULL
        JsonToken t = JsonToken.VALUE_NULL;
        jp.setCurrentToken(t);
        Object value = property._valueDeserializer.getNullValue(ctxt);
        property.deserializeAndSet(jp, ctxt, bean);
        
        // Test with _valueTypeDeserializer not null
        property._valueTypeDeserializer = new JsonDeserializer<>();
        value = property._valueDeserializer.deserializeWithType(jp, ctxt, property._valueTypeDeserializer);
        property.deserializeAndSet(jp, ctxt, bean);
        
        // Test with _creator.newInstance(bean) throws exception
        property._creator = new Constructor<Object>() {
            public Object newInstance(Object bean) {
                throw new RuntimeException("Test exception");
            }
        };
        try {
            property.deserializeAndSet(jp, ctxt, bean);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to instantiate class "+property._creator.getDeclaringClass().getName()+", problem: "+e.getMessage(), e.getMessage());
        }
        
        // Test with default case
        t = JsonToken.VALUE_STRING;
        jp.setCurrentToken(t);
        value = property._creator.newInstance(bean);
        property._valueDeserializer.deserialize(jp, ctxt, value);
        property.deserializeAndSet(jp, ctxt, bean);
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        InnerClassProperty property = new InnerClassProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        property._delegate = new SettableBeanProperty();
        Object value = property.deserialize(jp, ctxt);
        property.setAndReturn(instance, value);
    }
    
    @Test
    public void testSet() {
        InnerClassProperty property = new InnerClassProperty();
        Object instance = new Object();
        Object value = new Object();
        
        property._delegate = new SettableBeanProperty();
        property.set(instance, value);
    }
    
    @Test
    public void testReadResolve() {
        InnerClassProperty property = new InnerClassProperty();
        property._annotated = new AnnotatedConstructor();
        
        Object result = property.readResolve();
        assertNotNull(result);
        assertTrue(result instanceof InnerClassProperty);
    }
    
    @Test
    public void testWriteReplace() {
        InnerClassProperty property = new InnerClassProperty();
        property._creator = new Constructor<Object>();
        
        Object result = property.writeReplace();
        assertNotNull(result);
        if (property._annotated != null) {
            assertTrue(result instanceof InnerClassProperty);
        } else {
            assertTrue(result instanceof InnerClassProperty);
        }
    }
    
}