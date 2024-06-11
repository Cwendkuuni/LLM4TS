import org.junit.Test;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {
    
    @Test
    public void testWithDeserializer() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonDeserializer<?> deserializer = null;
        NullValueProvider provider = null;
        SetterlessProperty result = property.withValueDeserializer(deserializer);
        assertEquals(deserializer, result.getValueDeserializer());
        assertEquals(provider, result.getNullValueProvider());
    }
    
    @Test
    public void testWithNullProvider() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        NullValueProvider provider = null;
        SetterlessProperty result = property.withNullProvider(provider);
        assertEquals(provider, result.getNullValueProvider());
    }
    
    @Test
    public void testFixAccess() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        DeserializationConfig config = null;
        property.fixAccess(config);
    }
    
    @Test
    public void testGetAnnotation() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Class<A> acls = null;
        Annotation result = property.getAnnotation(acls);
        assertNull(result);
    }
    
    @Test
    public void testGetMember() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        assertNull(property.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        try {
            property.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        try {
            Object result = property.deserializeSetAndReturn(p, ctxt, instance);
            assertEquals(instance, result);
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = null;
        Object value = null;
        property.set(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = null;
        Object value = null;
        try {
            Object result = property.setAndReturn(instance, value);
            assertEquals(instance, result);
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }
}