import org.junit.Test;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {
    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertNotNull(property);
        assertEquals(propDef, property.getFullName());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getValueTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(method, property.getMember());

        assertFalse(property.hasValueDeserializer());
        assertNull(property.getValueDeserializer());
        assertFalse(property.hasNullProvider());
        assertNull(property.getNullProvider());
    }

    @Test
    public void testWithMethods() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        SetterlessProperty propertyWithNewName = (SetterlessProperty) property.withName(new PropertyName("newName"));
        assertEquals("newName", propertyWithNewName.getFullName().getSimpleName());

        JsonDeserializer<?> deser = new JsonDeserializer<>();
        SetterlessProperty propertyWithValueDeserializer = (SetterlessProperty) property.withValueDeserializer(deser);
        assertTrue(propertyWithValueDeserializer.hasValueDeserializer());
        assertEquals(deser, propertyWithValueDeserializer.getValueDeserializer());

        NullValueProvider nva = new NullValueProvider();
        SetterlessProperty propertyWithNullProvider = (SetterlessProperty) property.withNullProvider(nva);
        assertTrue(propertyWithNullProvider.hasNullProvider());
        assertEquals(nva, propertyWithNullProvider.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        SetterlessProperty property = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        property.fixAccess(config);
    }
    
    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        SetterlessProperty property = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        try {
            property.deserializeAndSet(p, ctxt, instance);
            assertEquals(instance, property.deserializeSetAndReturn(p, ctxt, instance));
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        SetterlessProperty property = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        property.set(null, null);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        SetterlessProperty property = new SetterlessProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
        assertEquals("instance", property.setAndReturn("instance", null));
    }
}