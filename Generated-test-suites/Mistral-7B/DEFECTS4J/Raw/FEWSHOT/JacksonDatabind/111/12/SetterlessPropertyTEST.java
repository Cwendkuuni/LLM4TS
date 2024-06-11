package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Test;

public class SetterlessPropertyTest extends TestBase {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        SettableBeanProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMethod(SetterlessPropertyTest.class.getDeclaredMethod("getList")), javaType),
                javaType, null, Annotations.empty(), null);

        String json = "\"[\"foo\", \"bar\"]\"";
        JsonParser parser = mapper.getFactory().createParser(json);

        property.deserializeAndSet(parser, new DeserializationContext(), new Object());

        List<String> list = (List<String>) getList();
        assertEquals(2, list.size());
        assertEquals("foo", list.get(0));
        assertEquals("bar", list.get(1));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        SettableBeanProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMethod(SetterlessPropertyTest.class.getDeclaredMethod("getList")), javaType),
                javaType, null, Annotations.empty(), null);

        String json = "\"[\"foo\", \"bar\"]\"";
        JsonParser parser = mapper.getFactory().createParser(json);

        Object result = property.deserializeSetAndReturn(parser, new DeserializationContext(), new Object());

        List<String> list = (List<String>) getList();
        assertEquals(2, list.size());
        assertEquals("foo", list.get(0));
        assertEquals("bar", list.get(1));
        assertSame(result, getList());
    }

    @Test
    public void testGetAnnotation() {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        SettableBeanProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMethod(SetterlessPropertyTest.class.getDeclaredMethod("getList")), javaType),
                javaType, null, Annotations.empty(), null);

        JsonProperty jsonProperty = new JsonProperty();
        assertEquals(jsonProperty.getClass(), property.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testFixAccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        SettableBeanProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMethod(SetterlessPropertyTest.class.getDeclaredMethod("getList")), javaType),
                javaType, null, Annotations.empty(), null);

        AccessModifier accessModifier = AccessModifier.PUBLIC;
        property.fixAccess(mapper.getConfig());
        assertEquals(accessModifier.getModifiers(), _annotated.getModifiers());
    }

    @JsonProperty("list")
    private List<String> getList() {
        return new ArrayList<>();
    }

    private static class AccessModifier {
        public int getModifiers() {
            return 0;
        }
    }
}