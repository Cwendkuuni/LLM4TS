import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorPropertyTest {

    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("test");
        JavaType type = new JavaType(String.class);
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();

        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser,
                contextAnnotations, param, index, injectableValueId, metadata);

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
    public void testFindInjectableValue() {
        DeserializationContext context = new DeserializationContext();
        CreatorProperty creatorProperty = new CreatorProperty();
        Object beanInstance = new Object();

        try {
            creatorProperty.findInjectableValue(context, beanInstance);
            fail("Expected JsonMappingException was not thrown.");
        } catch (JsonMappingException e) {
            assertEquals("Property 'null' (type null) has no injectable value id configured", e.getMessage());
        }
    }

    @Test
    public void testInject() {
        DeserializationContext context = new DeserializationContext();
        CreatorProperty creatorProperty = new CreatorProperty();
        Object beanInstance = new Object();

        try {
            creatorProperty.inject(context, beanInstance);
            fail("Expected JsonMappingException was not thrown.");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        CreatorProperty creatorProperty = new CreatorProperty();
        creatorProperty.setFallbackSetter(new SettableBeanProperty());

        try {
            creatorProperty.deserializeAndSet(p, ctxt, instance);
            fail("Expected IOException was not thrown.");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        CreatorProperty creatorProperty = new CreatorProperty();
        creatorProperty.setFallbackSetter(new SettableBeanProperty());

        try {
            creatorProperty.deserializeSetAndReturn(p, ctxt, instance);
            fail("Expected IOException was not thrown.");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testSet() {
        Object instance = new Object();

        CreatorProperty creatorProperty = new CreatorProperty();
        creatorProperty.setFallbackSetter(new SettableBeanProperty());

        try {
            creatorProperty.set(instance, "test");
            fail("Expected IOException was not thrown.");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();

        CreatorProperty creatorProperty = new CreatorProperty();
        creatorProperty.setFallbackSetter(new SettableBeanProperty());

        try {
            creatorProperty.setAndReturn(instance, "test");
            fail("Expected IOException was not thrown.");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testGetterAndSetter() {
        CreatorProperty creatorProperty = new CreatorProperty();
        PropertyName newName = new PropertyName("newName");
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();

        creatorProperty.setFallbackSetter(new SettableBeanProperty());

        creatorProperty.setFallbackSetter(new SettableBeanProperty());
        creatorProperty.setFallbackSetter(null);

        creatorProperty.setFallbackSetter(new SettableBeanProperty());
        creatorProperty.withNullProvider(nva);
        creatorProperty.withNullProvider(null);

        creatorProperty.setFallbackSetter(new SettableBeanProperty());
        creatorProperty.withValueDeserializer(deser);
        creatorProperty.withValueDeserializer(null);

        creatorProperty.setFallbackSetter(new SettableBeanProperty());
        creatorProperty.withName(newName);
        creatorProperty.withName(null);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        CreatorProperty creatorProperty = new CreatorProperty();

        creatorProperty.fixAccess(config);
    }

    @Test
    public void testMarkAsIgnorable() {
        CreatorProperty creatorProperty = new CreatorProperty();
        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());
    }

    @Test
    public void testGetAnnotation() {
        CreatorProperty creatorProperty = new CreatorProperty();
        Annotation annotation = creatorProperty.getAnnotation(Annotation.class);
        assertNull(annotation);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = new AnnotatedMember();
        CreatorProperty creatorProperty = new CreatorProperty();
        creatorProperty._annotated = member;

        assertEquals(member, creatorProperty.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        CreatorProperty creatorProperty = new CreatorProperty();
        creatorProperty._creatorIndex = 1;
        assertEquals(1, creatorProperty.getCreatorIndex());
    }

    @Test
    public void testToString() {
        PropertyName name = new PropertyName("test");
        Object injectableValueId = new Object();

        CreatorProperty creatorProperty = new CreatorProperty();
        creatorProperty.setName(name);
        creatorProperty.setInjectableValueId(injectableValueId);

        String expected = "[creator property, name 'test'; inject id '" + injectableValueId + "']";
        assertEquals(expected, creatorProperty.toString());
    }

    @Test
    public void testReportMissingSetter() {
        CreatorProperty creatorProperty = new CreatorProperty();
        try {
            creatorProperty._reportMissingSetter(null, null);
            fail("Expected InvalidDefinitionException was not thrown.");
        } catch (InvalidDefinitionException e) {
            assertEquals("No fallback setter/field defined for creator property 'null'", e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
    }
}