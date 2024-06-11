import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodPropertyTest {
  
    private MethodProperty methodProperty;

    @Before 
    public void setUp() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    }
    
    @After
    public void tearDown() {
        methodProperty = null;
    }
    
    @Test
    public void testGetAnnotation() {
        assertNull(methodProperty.getAnnotation(Class.class));
    }
    
    @Test
    public void testGetMember() {
        assertEquals(new AnnotatedMethod(), methodProperty.getMember());
    }
    
    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new NullValueProvider();
        assertEquals(new MethodProperty(methodProperty, null, nva), methodProperty.withNullProvider(nva));
    }
    
    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        config.enable(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
        methodProperty.fixAccess(config);
    }
    
    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            methodProperty.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        try {
            methodProperty.set(instance, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        try {
            methodProperty.setAndReturn(instance, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}