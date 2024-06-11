import org.junit.Before;
import org.junit.Test;

public class MethodPropertyTest {
    
    private MethodProperty methodProperty;
    
    @Before
    public void setUp() {
        methodProperty = new MethodProperty(
            new BeanPropertyDefinition("property", null, null, null),
            new JavaType(null, null, null),
            null,
            null,
            new AnnotatedMethod(null, null)
        );
    }
    
    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new NullValueProvider();
        MethodProperty property = methodProperty.withNullProvider(nva);
        assert property._nullProvider == nva;
    }
    
    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        methodProperty.fixAccess(config);
    }
    
    @Test
    public void testGetAnnotation() {
        Annotation annotation = methodProperty.getAnnotation(Annotation.class);
    }
    
    @Test
    public void testGetMember() {
        AnnotatedMember member = methodProperty.getMember();
    }
    
    @Test
    public void testDeserializeAndSet() {
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        try {
            methodProperty.deserializeAndSet(parser, context, new Object());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        try {
            methodProperty.deserializeSetAndReturn(parser, context, new Object());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSet() {
        try {
            methodProperty.set(new Object(), new Object());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSetAndReturn() {
        try {
            methodProperty.setAndReturn(new Object(), new Object());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadResolve() {
        MethodProperty property = (MethodProperty) methodProperty.readResolve();
    }
    
}