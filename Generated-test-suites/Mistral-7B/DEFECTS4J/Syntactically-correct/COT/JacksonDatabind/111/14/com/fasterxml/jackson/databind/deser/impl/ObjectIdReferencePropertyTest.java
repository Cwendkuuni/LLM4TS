package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.ser.DefaultSerializers;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.util.TestUtil;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest extends BaseMapTest {

    @SuppressWarnings("unchecked")
    private static <T> ObjectIdReferenceProperty createObjectIdReferenceProperty(String fieldName) throws InstantiationException, IllegalAccessException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(JsonId.class, "id");
        SettableBeanProperty forwardProperty = new SettableBeanProperty(null, Object.class, null);
        Field idField = TestUtil.findField(ObjectIdReferenceProperty.class, "_objectIdInfo");
        idField.setAccessible(true);
        idField.set(null, objectIdInfo);

        return new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);
    }

    @Test
    public void testConstructor_SettableBeanPropertyAndObjectIdInfo() throws Exception {
        ObjectIdReferenceProperty property = createObjectIdReferenceProperty("testProperty");
        assertEquals("testProperty", property.getName());
        assertNotNull(property._objectIdInfo);
        assertNull(property._forward);
    }

    @Test
    public void testConstructor_ObjectIdReferencePropertyAndJsonDeserializer() throws Exception {
        ObjectIdReferenceProperty parentProperty = createObjectIdReferenceProperty("testParentProperty");
        JsonDeserializer<String> deserializer = new StdDeserializer<String>() {};
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(parentProperty, deserializer, null);
        assertEquals("testParentProperty", property.getName());
        assertSame(parentProperty, property._forward);
        assertNotNull(property._objectIdInfo);
    }

    @Test
    public void testConstructor_ObjectIdReferencePropertyAndPropertyName() throws Exception {
        ObjectIdReferenceProperty parentProperty = createObjectIdReferenceProperty("testParentProperty");
        PropertyName newName = PropertyName.construct("testNewName");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(parentProperty, newName);
        assertEquals("testNewName", property.getName());
        assertSame(parentProperty, property._forward);
        assertNotNull(property._objectIdInfo);
    }

    @Test
    public void testWithName() throws Exception {
        ObjectIdReferenceProperty property = createObjectIdReferenceProperty("testProperty");
        PropertyName newName = PropertyName.construct("testNewName");
        ObjectIdReferenceProperty newProperty = property.withName(newName);
        assertEquals("testNewName", newProperty.getName());
        assertEquals("testProperty", property.getName());
        assertSame(property, newProperty._forward);
        assertSame(property._objectIdInfo, newProperty._objectIdInfo);
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        ObjectIdReferenceProperty property = createObjectIdReferenceProperty("testProperty");
        JsonDeserializer<String> deserializer = new StdDeserializer<String>() {};
        ObjectIdReferenceProperty newProperty = property.withValueDeserializer(deserializer);
        assertSame(deserializer, newProperty._valueDeserializer);
        assertSame(property, newProperty._forward);
        assertSame(property._objectIdInfo, newProperty._objectIdInfo);
    }

    @Test
    public void testWithNullProvider() throws Exception {
        ObjectIdReferenceProperty property = createObjectIdReferenceProperty("testProperty");
        NullValueProvider nva = new DefaultNullValueProvider.NullProvider();
        ObjectIdReferenceProperty newProperty = property.withNullProvider(nva);
        assertSame(nva, newProperty._nullProvider);
        assertSame(property, newProperty._forward);
        assertSame(property._objectIdInfo, newProperty._objectIdInfo);
    }

    @Test
    public void testFixAccess() throws Exception {
        ObjectIdReferenceProperty property = createObjectIdReferenceProperty("testProperty");
        ObjectIdReferenceProperty forwardProperty = new SettableBeanProperty(null, Object.class, null);
        property.fixAccess(null);
        forwardProperty.fixAccess(null);
        assertSame(forwardProperty, property._forward);
    }

    @Test
    public void testGetAnnotation() throws Exception {
        ObjectIdReferenceProperty property = createObjectIdReferenceProperty("testProperty");
        JsonId jsonIdAnnotation = new JsonId();
        AnnotationIntrospector introspector = new AnnotationIntrospector();
        AnnotatedMember member = introspect(property);
        JsonId testAnnotation = (JsonId) property.getAnnotation(JsonId.class);
        assertEquals(jsonIdAnnotation, testAnnotation);
    }

    @Test
    public void testGetMember() throws Exception {
        ObjectIdReferenceProperty property = createObjectIdReferenceProperty("testProperty");
        SettableBeanProperty forwardProperty = new SettableBeanProperty(null, Object.class, null);
        assertEquals(forwardProperty.getMember(), property.getMember());
    }

    @Test
    public void testGetCreatorIndex() throws Exception {
        ObjectIdReferenceProperty property = createObjectIdReferenceProperty("testProperty");
        JsonDeserializer<String> deserializer = new StdDeserializer<String>() {};
        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, deserializer, null);
        int creatorIndex = newProperty.getCreatorIndex();
        int parentCreatorIndex = property.getCreatorIndex();
        assertEquals(creatorIndex, parentCreatorIndex + 1);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReferenceProperty property = createObjectIdReferenceProperty("testProperty");
        Map<String, String> input = new SimpleBeanPropertyFilter.SerializeAll().filterMap(mapper.getValueAsTree(new ObjectWrapper()));
        String value = "testValue";
        input.put("testProperty", value);

        ObjectIdReferenceProperty.PropertyReferring referring = null;
        try {
            mapper.readValue(mapper.valueToTree(input), Object.class);
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference reference) {
            referring = (ObjectIdReferenceProperty.PropertyReferring) reference.getReferring();
            assertSame(property, referring._parent);
        }

        Map<String, Object> result = mapper.readValue(mapper.valueToTree(input), Map.class);
        assertEquals(Collections.singletonMap("testProperty", value), result);
    }

    @Test
    public void testDeserializeSetAndReturnNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReferenceProperty property = createObjectIdReferenceProperty("testProperty");
        Map<String, Object> input = new SimpleBeanPropertyFilter.SerializeAll().filterMap(mapper.getValueAsTree(new ObjectWrapper()));

        Object result = mapper.readValue(mapper.valueToTree(input), Object.class);
        assertNull(result);
        assertEquals(Collections.emptyMap(), mapper.readValue(mapper.valueToTree(input), Map.class));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReferenceProperty property = createObjectIdReferenceProperty("testProperty");
        JsonDeserializer<Integer> deserializer = new StdDeserializer<Integer>() {};
        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, deserializer, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(JsonId.class, "id");
        Field idField = TestUtil.findField(ObjectIdReferenceProperty.class, "_objectIdInfo");
        idField.setAccessible(true);
        idField.set(newProperty, objectIdInfo);
        Map<String, Object> input = new SimpleBeanPropertyFilter.SerializeAll().filterMap(mapper.getValueAsTree(new ObjectWrapper()));
        Integer value = 1;
        input.put("testProperty", value);

        Object result = newProperty.deserializeSetAndReturn(mapper.getFactory().createParser(mapper.getFactory().createParser("{\"testProperty\": " + value + "}").nextVal()), null, new Object());
        assertEquals(value, result);
        assertEquals(Collections.singletonMap("testProperty", value), mapper.readValue(mapper.valueToTree(input), Map.class));
    }

    @Test
    public void testSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReferenceProperty property = createObjectIdReferenceProperty("testProperty");
        Object testObject = new Object();
        String value = "testValue";
        mapper.writeValueAsString(mapper.writeValueAsTree(Collections.singletonMap("testProperty", value)), new ObjectWriter(mapper).withDefaultPrettyPrinter());
        mapper.readValue(mapper.valueToTree(mapper.readTree(mapper.writeValueAsString(testObject))), Map.class).put("testProperty", value);

        property.set(testObject, value);
        assertEquals(value, mapper.readValue(mapper.valueToTree(mapper.readTree(mapper.writeValueAsString(testObject))), String.class));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReferenceProperty property = createObjectIdReferenceProperty("testProperty");
        Object testObject = new Object();
        String value = "testValue";
        Map<String, Object> input = new SimpleBeanPropertyFilter.SerializeAll().filterMap(mapper.getValueAsTree(new ObjectWrapper()));

        Object result = property.setAndReturn(testObject, value);
        assertEquals(value, result);
        assertEquals(Collections.singletonMap("testProperty", value), mapper.readValue(mapper.valueToTree(input), Map.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testThrowUnsupportedOperationException_DeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null, null);
        Map<String, Object> input = new SimpleBeanPropertyFilter.SerializeAll().filterMap(mapper.getValueAsTree(new ObjectWrapper()));

        property.deserializeAndSet(mapper.getFactory().createParser(mapper.getFactory().createParser("{\"testProperty\": \"testValue\"}").nextVal()), null, new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testThrowUnsupportedOperationException_DeserializeSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null, null);

        Object result = property.deserializeSetAndReturn(mapper.getFactory().createParser(mapper.getFactory().createParser("{\"testProperty\": \"testValue\"}").nextVal()), null, new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testThrowUnsupportedOperationException_Set() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null, null);
        Object testObject = new Object();
        String value = "testValue";

        property.set(testObject, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testThrowUnsupportedOperationException_SetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null, null);
        Object testObject = new Object();
        String value = "testValue";

        Object result = property.setAndReturn(testObject, value);
    }
}