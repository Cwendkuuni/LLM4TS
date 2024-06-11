import static org.junit.Assert.*;
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

        assertEquals(propDef, property.getPropertyDefinition());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(method, property.getMember());
    }

    @Test
    public void testFixAccess() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();

        MethodProperty property = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        DeserializationConfig config = new DeserializationConfig();

        property.fixAccess(config);

        assertTrue(property.getMember().isPublic());
    }

    @Test
    public void testGetAnnotation() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();

        MethodProperty property = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        Class<Annotation> acls = Annotation.class;

        assertNull(property.getAnnotation(acls));

        // Test with a valid annotation
        Class<JsonCreator> jsonCreator = JsonCreator.class;
        AnnotatedMethod annotatedMethod = new AnnotatedMethod();
        annotatedMethod.addOrOverride(jsonCreator);
        property = new MethodProperty(propDef, type, typeDeser, contextAnnotations, annotatedMethod);

        assertEquals(jsonCreator, property.getAnnotation(jsonCreator).annotationType());
    }

    @Test
    public void testDeserializeAndSet() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        JsonParser parser = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        MethodProperty property = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        // Test with null value
        assertTrue(parser.hasToken(JsonToken.VALUE_NULL));
        assertNull(property.deserializeAndSet(parser, ctxt, instance));

        // Test with non-null value and deserializer
        JsonDeserializer<?> deserializer = new JsonDeserializer<>();
        property.setValueDeserializer(deserializer);
        Object value = new Object();
        parser.clearTokens();
        assertFalse(parser.hasToken(JsonToken.VALUE_NULL));
        assertNull(property.deserializeAndSet(parser, ctxt, instance));

        // Test with null value and deserializer
        assertNull(value);
        assertTrue(parser.hasToken(JsonToken.VALUE_NULL));
        assertNull(property.deserializeAndSet(parser, ctxt, instance));

        // Test with non-null value and type deserializer
        TypeDeserializer valueTypeDeser = new TypeDeserializer();
        property.setValueTypeDeserializer(valueTypeDeser);
        parser.clearTokens();
        assertFalse(parser.hasToken(JsonToken.VALUE_NULL));
        assertNull(property.deserializeAndSet(parser, ctxt, instance));

    }

    @Test
    public void testDeserializeSetAndReturn() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        JsonParser parser = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        MethodProperty property = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        // Test with null value
        assertTrue(parser.hasToken(JsonToken.VALUE_NULL));
        assertSame(instance, property.deserializeSetAndReturn(parser, ctxt, instance));

        // Test with non-null value and deserializer
        JsonDeserializer<?> deserializer = new JsonDeserializer<>();
        property.setValueDeserializer(deserializer);
        Object value = new Object();
        parser.clearTokens();
        assertFalse(parser.hasToken(JsonToken.VALUE_NULL));
        assertSame(instance, property.deserializeSetAndReturn(parser, ctxt, instance));

        // Test with null value and deserializer
        assertNull(value);
        assertTrue(parser.hasToken(JsonToken.VALUE_NULL));
        assertSame(instance, property.deserializeSetAndReturn(parser, ctxt, instance));

        // Test with non-null value and type deserializer
        TypeDeserializer valueTypeDeser = new TypeDeserializer();
        property.setValueTypeDeserializer(valueTypeDeser);
        parser.clearTokens();
        assertFalse(parser.hasToken(JsonToken.VALUE_NULL));
        assertSame(instance, property.deserializeSetAndReturn(parser, ctxt, instance));

    }

    @Test
    public void testSet() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        Object instance = new Object();

        MethodProperty property = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        Object value = new Object();

        try {
            property.set(instance, value);
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
    }

    @Test
    public void testSetAndReturn() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        Object instance = new Object();

        MethodProperty property = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        Object value = new Object();

        try {
            assertSame(instance, property.setAndReturn(instance, value));
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
    }

    @Test
    public void testReadResolve() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        Method methodRef = new Method();

        MethodProperty property = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        MethodProperty resolvedProperty = (MethodProperty) property.readResolve();

        assertNotSame(property, resolvedProperty);
        assertEquals(method.getAnnotated(), resolvedProperty.getMember());
    }

}