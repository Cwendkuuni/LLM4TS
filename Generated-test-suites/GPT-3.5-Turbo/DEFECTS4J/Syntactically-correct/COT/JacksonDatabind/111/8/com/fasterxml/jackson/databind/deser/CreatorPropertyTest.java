
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("test");
        JavaType type = new JavaType();
        PropertyName wrapperName = new PropertyName("testWrapper");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();

        CreatorProperty property = new CreatorProperty(name, type, wrapperName,
                typeDeser, contextAnnotations, param, index, injectableValueId, metadata);

        assertEquals(name, property.getName());
        assertEquals(type, property.getType());
        assertEquals(wrapperName, property.getWrapperName());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(param, property.getMember());
        assertEquals(index, property.getCreatorIndex());
        assertEquals(injectableValueId, property.getInjectableValueId());
        assertEquals(metadata, property.getMetadata());
    }

    @Test
    public void testCopyConstructor() {
        PropertyName name = new PropertyName("test");
        JavaType type = new JavaType();
        PropertyName wrapperName = new PropertyName("testWrapper");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();

        CreatorProperty srcProperty = new CreatorProperty(name, type, wrapperName,
                typeDeser, contextAnnotations, param, index, injectableValueId, metadata);

        PropertyName newName = new PropertyName("testNew");
        CreatorProperty copiedProperty = new CreatorProperty(srcProperty, newName);

        assertEquals(newName, copiedProperty.getName());
        assertEquals(type, copiedProperty.getType());
        assertEquals(wrapperName, copiedProperty.getWrapperName());
        assertEquals(typeDeser, copiedProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, copiedProperty.getContextAnnotations());
        assertEquals(param, copiedProperty.getMember());
        assertEquals(index, copiedProperty.getCreatorIndex());
        assertEquals(injectableValueId, copiedProperty.getInjectableValueId());
        assertEquals(metadata, copiedProperty.getMetadata());
    }

    @Test
    public void testDeserializationAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        CreatorProperty property = new CreatorProperty();

        try {
            property.deserializeAndSet(p, ctxt, instance);
            fail("Expected InvalidDefinitionException");
        } catch (InvalidDefinitionException e) {
            assertEquals("No fallback setter/field defined for creator property 'null'", e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        CreatorProperty property = new CreatorProperty();

        try {
            Object result = property.deserializeSetAndReturn(p, ctxt, instance);
            assertNull(result);
            fail("Expected InvalidDefinitionException");
        } catch (InvalidDefinitionException e) {
            assertEquals("No fallback setter/field defined for creator property 'null'", e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();

        CreatorProperty property = new CreatorProperty();

        try {
            property.set(instance, value);
            fail("Expected InvalidDefinitionException");
        } catch (InvalidDefinitionException e) {
            assertEquals("No fallback setter/field defined for creator property 'null'", e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();

        CreatorProperty property = new CreatorProperty();

        try {
            Object result = property.setAndReturn(instance, value);
            assertNull(result);
            fail("Expected InvalidDefinitionException");
        } catch (InvalidDefinitionException e) {
            assertEquals("No fallback setter/field defined for creator property 'null'", e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testFindInjectableValue() {
        DeserializationContext ctxt = new DeserializationContext();
        Object beanInstance = new Object();

        CreatorProperty property = new CreatorProperty();

        try {
            property.findInjectableValue(ctxt, beanInstance);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Property 'null' (type null) has no injectable value id configured", e.getMessage());
        }
    }

    @Test
    public void testInject() {
        DeserializationContext ctxt = new DeserializationContext();
        Object beanInstance = new Object();

        CreatorProperty property = new CreatorProperty();

        try {
            property.inject(ctxt, beanInstance);
            fail("Expected JsonMappingException");
        } catch (IOException e) {
            fail("Unexpected IOException");
        } catch (JsonMappingException e) {
            assertEquals("No fallback setter/field defined for creator property 'null'", e.getMessage());
        }
    }

    @Test
    public void testConstructor_noArgs() {
        CreatorProperty property = new CreatorProperty();

        assertNull(property.getName());
        assertNull(property.getType());
        assertNull(property.getWrapperName());
        assertNull(property.getTypeDeserializer());
        assertNull(property.getContextAnnotations());
        assertNull(property.getMember());
        assertEquals(0, property.getCreatorIndex());
        assertNull(property.getInjectableValueId());
        assertNull(property.getMetadata());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new NullValueProvider();
        CreatorProperty property = new CreatorProperty();

        CreatorProperty resultProperty = property.withNullProvider(nva);

        assertSame(nva, resultProperty.getNullValueProvider());
        assertNull(property.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        CreatorProperty property = new CreatorProperty();
        SettableBeanProperty fallbackSetter = new SettableBeanProperty();

        property.setFallbackSetter(fallbackSetter);

        property.fixAccess(config);

        assertNotNull(fallbackSetter.getMember());
    }

    @Test
    public void testMarkAsIgnorableAndIsIgnorable() {
        CreatorProperty property = new CreatorProperty();
        assertFalse(property.isIgnorable());

        property.markAsIgnorable();

        assertTrue(property.isIgnorable());
    }

    @Test
    public void testSetterMethods() {
        CreatorProperty property = new CreatorProperty();

        property.withName(new PropertyName("test"));
        property.withValueDeserializer(null);

        assertNull(property.getNullValueProvider());
    }

    @Test
    public void testGetAnnotation() {
        CreatorProperty property = new CreatorProperty();

        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testToString() {
        PropertyName name = new PropertyName("test");
        JavaType type = new JavaType();
        PropertyName wrapperName = new PropertyName("testWrapper");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();

        CreatorProperty property = new CreatorProperty(name, type, wrapperName,
                typeDeser, contextAnnotations, param, index, injectableValueId, metadata);

        assertEquals("[creator property, name 'test'; inject id 'java.lang.Object']", property.toString());
    }

    @Test
    public void testGetInjectableValueId() {
        CreatorProperty property = new CreatorProperty();

        assertNull(property.getInjectableValueId());
    }

    @Test
    public void testVerifySetter() {
        CreatorProperty property = new CreatorProperty();

        try {
            property.verifySetter(null, null);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("No fallback setter/field defined for creator property 'null'", e.getMessage());
        }
    }

    @Test
    public void testReportMissingSetter() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        try {
            CreatorProperty.reportMissingSetter(p, ctxt, null);
            fail("Expected InvalidDefinitionException");
        } catch (InvalidDefinitionException e) {
            assertEquals("No fallback setter/field defined for creator property 'null'", e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
}

