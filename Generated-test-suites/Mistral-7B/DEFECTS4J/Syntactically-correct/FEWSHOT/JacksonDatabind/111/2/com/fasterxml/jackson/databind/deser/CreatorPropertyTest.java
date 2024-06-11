package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class CreatorPropertyTest {
    @Test
    public void testCreatorProperty() throws Exception {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = new AnnotatedParameter(null, "testParam", String.class, null);
        int index = 0;
        Object injectableValueId = "testId";
        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        assertEquals("testProperty", creatorProperty.getName().getShortName());
        assertEquals(String.class, creatorProperty.getType().rawClass());
        assertNull(creatorProperty.getWrapperName());
        assertNull(creatorProperty.getTypeDeserializer());
        assertNull(creatorProperty.getContextAnnotations());
        assertNull(creatorProperty.getMetadata());
        assertEquals(null, creatorProperty.getFallbackSetter());
        assertEquals(-1, creatorProperty.getCreatorIndex());
        assertEquals(false, creatorProperty.isIgnorable());
        assertEquals(param, creatorProperty.getAnnotated());
        assertEquals(injectableValueId, creatorProperty.getInjectableValueId());

        CreatorProperty cloned = creatorProperty.withName(PropertyName.construct("newName"));
        assertEquals("newName", cloned.getName().getShortName());
        assertEquals("testProperty", creatorProperty.getName().getShortName());

        JsonDeserializer<String> deserializer = new StandardDeserializer<String>(String.class);
        CreatorProperty deserialized = new CreatorProperty(creatorProperty, deserializer, null);
        assertEquals(deserializer, deserialized.getValueDeserializer());

        NullValueProvider nva = null;
        CreatorProperty nullProvider = new CreatorProperty(deserialized, null, nva);
        assertEquals(null, nullProvider.getNullProvider());

        CreatorProperty setter = new CreatorProperty(creatorProperty, null, null);
        setter.setFallbackSetter(creatorProperty);
        assertEquals(creatorProperty, setter.getFallbackSetter());

        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());

        Object injectableValue = new Object();
        DeserializationContext context = new TestDeserializationContext();
        assertNull(creatorProperty.findInjectableValue(context, null));
        creatorProperty.inject(context, null);
        assertEquals(injectableValue, creatorProperty.findInjectableValue(context, null));
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingSetter() throws Exception {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = new AnnotatedParameter(null, "testParam", String.class, null);
        int index = 0;
        Object injectableValueId = "testId";
        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        creatorProperty.deserializeAndSet(null, null, null);
    }
}

class TestDeserializationContext extends DeserializationContext {
    @Override
    public Object findInjectableValue(Object id, BeanProperty property, Object bean) throws JsonMappingException {
        return null;
    }
}