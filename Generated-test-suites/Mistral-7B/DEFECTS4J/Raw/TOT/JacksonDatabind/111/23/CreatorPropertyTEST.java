package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

    @Test
    public void testCreatorPropertyWithDefaultValue() throws JsonProcessingException {
        JacksonAnnotationIntrospector introspector = new JacksonAnnotationIntrospector();

        AnnotatedClass ac = introspector.findProperty(MyClass.class, "creatorProperty");
        CreatorProperty creatorProperty = (CreatorProperty) ac.getValueDeserializer();

        ObjectMapper mapper = new ObjectMapper();
        MyClass obj = mapper.readValue(ObjectMapperTestUtil.getJsonForDeserializationTesting(mapper, "{\"creatorProperty\": null}"), MyClass.class);
        assertNull(obj.creatorProperty);

        ObjectNode node = ObjectMapperTestUtil.getJsonForDeserializationTesting(mapper, "{\"creatorProperty\": 42}");
        obj = mapper.readValue(node, MyClass.class);
        assertEquals(42, obj.creatorProperty.getValue());
    }

    @Test
    public void testCreatorPropertyWithInjectedValue() throws JsonProcessingException {
        JacksonAnnotationIntrospector introspector = new JacksonAnnotationIntrospector();

        AnnotatedClass ac = introspector.findProperty(MyClass.class, "creatorPropertyWithInjectedValue");
        CreatorProperty creatorProperty = (CreatorProperty) ac.getValueDeserializer();

        ObjectMapper mapper = new ObjectMapper();
        MyClass obj = mapper.readValue(ObjectMapperTestUtil.getJsonForDeserializationTesting(mapper, "{}"), MyClass.class);

        ObjectNode node = ObjectMapperTestUtil.getJsonForDeserializationTesting(mapper, "{\"creatorPropertyWithInjectedValue\": { \"foo\": \"bar\" }}");
        obj = mapper.readValue(node, MyClass.class);
        assertEquals("bar", obj.creatorPropertyWithInjectedValue.getValue());

        ObjectNode nodeWithInjectedValue = ObjectMapperTestUtil.getJsonForDeserializationTesting(mapper, "{\"@com.fasterxml.jackson.annotation.JsonCreator\": [], \"creatorPropertyWithInjectedValue\": { \"foo\": \"baz\" }}");
        obj = mapper.readValue(nodeWithInjectedValue, MyClass.class);
        assertEquals("baz", obj.creatorPropertyWithInjectedValue.getValue());
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testCreatorPropertyWithoutFallbackSetter() throws JsonProcessingException {
        JacksonAnnotationIntrospector introspector = new JacksonAnnotationIntrospector();

        AnnotatedClass ac = introspector.findProperty(MyClass.class, "creatorPropertyWithoutFallbackSetter");
        CreatorProperty creatorProperty = (CreatorProperty) ac.getValueDeserializer();

        ObjectMapper mapper = new ObjectMapper();
        MyClass obj = mapper.readValue(ObjectMapperTestUtil.getJsonForDeserializationTesting(mapper, "{}"), MyClass.class);

        ObjectNode node = ObjectMapperTestUtil.getJsonForDeserializationTesting(mapper, "{\"creatorPropertyWithoutFallbackSetter\": 42}");
        creatorProperty._verifySetter();
    }

    @Test
    public void testCreatorPropertyWithMarkerFlag() throws JsonProcessingException {
        JacksonAnnotationIntrospector introspector = new JacksonAnnotationIntrospector();

        AnnotatedClass ac = introspector.findProperty(MyClassWithIgnorableCreatorProperty.class, "ignorableCreatorProperty");
        CreatorProperty creatorProperty = (CreatorProperty) ac.getValueDeserializer();

        ObjectMapper mapper = new ObjectMapper();
        MyClassWithIgnorableCreatorProperty obj = mapper.readValue(ObjectMapperTestUtil.getJsonForDeserializationTesting(mapper, "{\"ignorableCreatorProperty\": null}"), MyClassWithIgnorableCreatorProperty.class);
        assertNull(obj.ignorableCreatorProperty);

        creatorProperty.markAsIgnorable();

        ObjectNode node = ObjectMapperTestUtil.getJsonForDeserializationTesting(mapper, "{\"ignorableCreatorProperty\": 42}");
        obj = mapper.readValue(node, MyClassWithIgnorableCreatorProperty.class);
        assertNull(obj.ignorableCreatorProperty);
    }

    static class MyClass {
        @JsonDeserialize
        private CreatorProperty creatorProperty;

        public CreatorProperty getCreatorProperty() {
            return creatorProperty;
        }
    }

    static class MyClassWithIgnorableCreatorProperty {
        @JsonDeserialize
        @JsonIgnore
        private CreatorProperty ignoreableCreatorProperty;

        public CreatorProperty getIgnorableCreatorProperty() {
            return ignoreableCreatorProperty;
        }
    }
}