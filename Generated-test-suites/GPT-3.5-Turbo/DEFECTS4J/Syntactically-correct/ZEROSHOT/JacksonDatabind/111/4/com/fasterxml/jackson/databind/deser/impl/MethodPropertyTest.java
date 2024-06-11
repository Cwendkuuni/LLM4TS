import org.junit.Test;

public class MethodPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        MethodProperty property = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        // Assertions
    }

    @Test
    public void testGetterAndSetter() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        MethodProperty property = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        // Test getter
        // Assertions

        // Test setter
        // Assertions
    }

    @Test
    public void testWithNullProvider() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        MethodProperty property = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        NullValueProvider nva = new NullValueProvider();
        MethodProperty newProperty = (MethodProperty) property.withNullProvider(nva);
        // Assertions
    }

    // Other test methods for remaining methods in the class

}