import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

    private CreatorProperty creatorProperty;

    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("testName");
        JavaType type = new JavaType(String.class);
        PropertyName wrapperName = new PropertyName("wrapperName");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();
        creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
        
        assertEquals(name, creatorProperty.getName());
        assertEquals(type, creatorProperty.getType());
        assertEquals(wrapperName, creatorProperty.getWrapperName());
        assertEquals(typeDeser, creatorProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, creatorProperty.getContextAnnotations());
        assertEquals(param, creatorProperty.getMember());
        assertEquals(index, creatorProperty.getCreatorIndex());
        assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
        assertEquals(metadata, creatorProperty.getMetadata());
    }
    
    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        try {
            creatorProperty.deserializeAndSet(p, ctxt, instance);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        try {
            creatorProperty.deserializeSetAndReturn(p, ctxt, instance);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected
        }
    }
    
    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        
        try {
            creatorProperty.set(instance, value);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected
        }
    }
    
    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        
        try {
            creatorProperty.setAndReturn(instance, value);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected
        }
    }
    
    @Test
    public void testFindInjectableValue() {
        DeserializationContext context = new DeserializationContext();
        Object beanInstance = new Object();
        
        try {
            creatorProperty.findInjectableValue(context, beanInstance);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // expected
        }
    }
    
    @Test
    public void testInject() {
        DeserializationContext context = new DeserializationContext();
        Object beanInstance = new Object();
        
        try {
            creatorProperty.inject(context, beanInstance);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected
        }
    }
}