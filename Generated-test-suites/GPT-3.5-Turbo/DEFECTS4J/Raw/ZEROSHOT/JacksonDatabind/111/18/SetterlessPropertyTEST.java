import org.junit.Before;
import org.junit.Test;

public class SetterlessPropertyTest {
    private SetterlessProperty setterlessProperty;
    
    @Before
    public void setUp() {
        setterlessProperty = new SetterlessProperty(null, null, null, null, null);
    }
    
    @Test
    public void testWithNullValueDeserializer() {
        SetterlessProperty newSetterlessProperty = setterlessProperty.withValueDeserializer(null);
        assertNull(newSetterlessProperty.getValueDeserializer());
    }
    
    @Test
    public void testWithNonNullValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        SetterlessProperty newSetterlessProperty = setterlessProperty.withValueDeserializer(deser);
        assertNotNull(newSetterlessProperty.getValueDeserializer());
    }
    
    @Test
    public void testWithNullNullValueProvider() {
        SetterlessProperty newSetterlessProperty = setterlessProperty.withNullProvider(null);
        assertNull(newSetterlessProperty.getNullProvider());
    }
    
    @Test
    public void testWithNonNullNullValueProvider() {
        NullValueProvider nvp = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                return null;
            }
        };
        SetterlessProperty newSetterlessProperty = setterlessProperty.withNullProvider(nvp);
        assertNotNull(newSetterlessProperty.getNullProvider());
    }
    
    @Test
    public void testFixAccessWithOverridePublicAccessModifiersEnabled() {
        DeserializationConfig config = new DeserializationConfig(null, null, null);
        config.enable(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
        setterlessProperty.fixAccess(config);
    }
    
    @Test
    public void testFixAccessWithOverridePublicAccessModifiersDisabled() {
        DeserializationConfig config = new DeserializationConfig(null, null, null);
        config.disable(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
        setterlessProperty.fixAccess(config);
    }
    
    @Test
    public void testGetAnnotationWithNullClass() {
        assertNull(setterlessProperty.getAnnotation(null));
    }
    
    @Test
    public void testGetAnnotationWithNonNullClass() {
        Class<?> acls = Class.class;
        assertNull(setterlessProperty.getAnnotation(acls));
    }
    
    @Test
    public void testGetMember() {
        assertNotNull(setterlessProperty.getMember());
    }
    
    @Test
    public void testDeserializeAndSetWithJsonValueNull() throws IOException {
        JsonParser p = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object instance = new Object();
        
        setterlessProperty.deserializeAndSet(p, ctxt, instance);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object instance = new Object();
        
        Object result = setterlessProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals(instance, result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        setterlessProperty.set(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        Object result = setterlessProperty.setAndReturn(instance, value);
        assertEquals(instance, result);
    }
}