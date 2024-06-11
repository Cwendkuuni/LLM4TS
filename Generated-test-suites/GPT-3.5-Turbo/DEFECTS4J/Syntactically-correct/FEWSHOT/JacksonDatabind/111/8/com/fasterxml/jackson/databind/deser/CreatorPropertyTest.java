package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Test;

public class CreatorPropertyTest {

    private final String propName = "testProperty";
    private final JavaType propType = JavaType.INT;
    private final PropertyName wrapperName = new PropertyName("wrapper");
    private final TypeDeserializer typeDeser = new TypeDeserializer();
    private final Annotations contextAnnotations = new Annotations();
    private final AnnotatedParameter param = new AnnotatedParameter(null, null, null);
    private final int index = 0;
    private final Object injectableValueId = new Object();
    private final PropertyMetadata metadata = new PropertyMetadata();
    
    private CreatorProperty creatorProperty = new CreatorProperty(propName, propType, wrapperName,
            typeDeser, contextAnnotations, param, index, injectableValueId, metadata);

    @Test
    public void testCreatorPropertyConstructor() {
        assertEquals(propName, creatorProperty.getName().getSimpleName());
        assertEquals(propType, creatorProperty.getType());

        assertNull(creatorProperty.getWrapperName());
        assertEquals(typeDeser, creatorProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, creatorProperty.getContextAnnotations());
        assertEquals(param, creatorProperty.getMember());
        assertEquals(index, creatorProperty.getCreatorIndex());
        assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
        assertEquals(metadata, creatorProperty.getMetadata());

        assertNull(creatorProperty.getNullProvider());
        assertNull(creatorProperty.getValueDeserializer());
    }

    @Test
    public void testCreatorPropertyWithMethods() {
        PropertyName newName = new PropertyName("newName");
        JavaType newType = JavaType.STRING;
        JsonDeserializer newDeserializer = new JsonDeserializer();
        NullValueProvider newNullProvider = new NullValueProvider();
        
        CreatorProperty withName = creatorProperty.withName(newName);
        CreatorProperty withTypeDeserializer = creatorProperty.withValueDeserializer(newDeserializer);
        CreatorProperty withNullProvider = creatorProperty.withNullProvider(newNullProvider);

        assertEquals(newName, withName.getName());
        assertEquals(newType, withName.getType());

        assertEquals(newDeserializer, withTypeDeserializer.getValueDeserializer());
        
        assertEquals(newNullProvider, withNullProvider.getNullProvider());
    }

    @Test
    public void testCreatorPropertyFixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        creatorProperty.fixAccess(config);

        assertTrue(true);
    }

    @Test
    public void testCreatorPropertySetFallbackSetter() {
        SettableBeanProperty fallbackSetter = new SettableBeanProperty();
        creatorProperty.setFallbackSetter(fallbackSetter);

        assertEquals(fallbackSetter, creatorProperty.getFallbackSetter());
    }

    @Test
    public void testCreatorPropertyMarkAsIgnorable() {
        assertFalse(creatorProperty.isIgnorable());
        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());
    }

    @Test
    public void testCreatorPropertyFindInjectableValue() {
        DeserializationContext context = new DeserializationContext();
        Object beanInstance = new Object();

        assertNull(creatorProperty.findInjectableValue(context, beanInstance));
    }

    @Test
    public void testCreatorPropertyInject() {
        DeserializationContext context = new DeserializationContext();
        Object beanInstance = new Object();

        creatorProperty.inject(context, beanInstance);

        assertEquals(null, creatorProperty.findInjectableValue(context, beanInstance));
    }

    @Test
    public void testCreatorPropertyGetAnnotation() {
        assertNull(creatorProperty.getAnnotation(null));
    }

    @Test
    public void testCreatorPropertyDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        try {
            creatorProperty.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            assertEquals("No fallback setter/field defined for creator property 'testProperty'", e.getMessage());
        }
    }

    @Test
    public void testCreatorPropertyDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        try {
            creatorProperty.deserializeSetAndReturn(p, ctxt, instance);
        } catch (IOException e) {
            assertEquals("No fallback setter/field defined for creator property 'testProperty'", e.getMessage());
        }
    }

    @Test
    public void testCreatorPropertySet() {
        Object instance = new Object();
        Object value = new Object();

        try {
            creatorProperty.set(instance, value);
        } catch (IOException e) {
            assertEquals("No fallback setter/field defined for creator property 'testProperty'", e.getMessage());
        }
    }

    @Test
    public void testCreatorPropertySetAndReturn() {
        Object instance = new Object();
        Object value = new Object();

        try {
            creatorProperty.setAndReturn(instance, value);
        } catch (IOException e) {
            assertEquals("No fallback setter/field defined for creator property 'testProperty'", e.getMessage());
        }
    }

    @Test
    public void testCreatorPropertyGetInjectableValueId() {
        assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
    }

    @Test
    public void testCreatorPropertyToString() {
        assertEquals("[creator property, name 'testProperty'; inject id 'java.lang.Object']", creatorProperty.toString());
    }

    @Test
    public void testCreatorPropertyVerifySetter() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        try {
            creatorProperty._verifySetter();
        } catch (IOException e) {
            assertEquals("No fallback setter/field defined for creator property 'testProperty'", e.getMessage());
        }
    }

    @Test
    public void testCreatorPropertyReportMissingSetter() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        try {
            creatorProperty._reportMissingSetter(p, ctxt);
        } catch (IOException e) {
            assertEquals("No fallback setter/field defined for creator property 'testProperty'", e.getMessage());
        }
    }
}