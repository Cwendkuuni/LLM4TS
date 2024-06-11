import static org.junit.Assert.*;
import org.junit.Test;

public class SetterlessPropertyTest {
    
    @Test
    public void testWithName() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty withName = property.withName(newName);
        
        assertEquals(newName, withName.getFullName());
    }
    
    @Test
    public void testWithValueDeserializer_SameDeserializer() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        SettableBeanProperty withValueDeserializer = property.withValueDeserializer(deser);
        
        assertEquals(property, withValueDeserializer);
    }
    
    @Test
    public void testWithValueDeserializer_DifferentDeserializer() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        SettableBeanProperty withValueDeserializer = property.withValueDeserializer(deser);
        
        assertNotEquals(property.getValueDeserializer(), withValueDeserializer.getValueDeserializer());
        assertEquals(deser, withValueDeserializer.getValueDeserializer());
    }
    
    @Test
    public void testWithNullProvider() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        NullValueProvider nvp = new NullValueProvider();
        SettableBeanProperty withNullProvider = property.withNullProvider(nvp);
        
        assertEquals(nvp, withNullProvider.getNullValueProvider());
    }
    
    @Test
    public void testFixAccess() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        DeserializationConfig config = new DeserializationConfig();
        property.fixAccess(config);
        
        assertEquals(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS), property.getMember().isPublic());
    }
    
    @Test
    public void testGetAnnotation() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        assertNull(property.getAnnotation(Test.class));
        
        property.getMember().addOrOverrideAnnotation(Test.class);
        assertNotNull(property.getAnnotation(Test.class));
    }
    
    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        
        property.deserializeAndSet(parser, context, instance);
        
        assertNull(property.deserializeAndSet(parser, context, instance));
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        
        assertEquals(instance, property.deserializeSetAndReturn(parser, context, instance));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        Object value = new Object();
        
        property.set(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        Object value = new Object();
        
        assertEquals(instance, property.setAndReturn(instance, value));
    }
}