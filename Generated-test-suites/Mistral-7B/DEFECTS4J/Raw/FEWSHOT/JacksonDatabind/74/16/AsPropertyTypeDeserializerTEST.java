package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.util.SettableBeanProperty.BeanPropertyType.ARRAY;
import static com.fasterxml.jackson.core.util.SettableBeanProperty.BeanPropertyType.FIELD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.TestBase;

public class AsPropertyTypeDeserializerTest extends TestBase {

    @SuppressWarnings("unchecked")
    private static final JavaType TYPE = new TypeReference<AsPropertyTypeDeserializer>() {}.getType();

    @SuppressWarnings("resource")
    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForType(TYPE);

        String jsonWithObject = "{\"typePropertyName\":\"StringType\",\"data\":\"some data\"}";
        Object actual = reader.readValue(new JsonFactory().createParser(jsonWithObject));
        assertEquals(String.class, actual.getClass());
        assertEquals("some data", actual);

        String jsonWithArray = "[{\"typePropertyName\":\"ListType\",\"typeId\":\"int\"},1,2,3]";
        actual = reader.readValue(new JsonFactory().createParser(jsonWithArray));
        assertNotNull(actual);
        assertTrue(actual instanceof List);
        assertEquals(Integer.class, ((List) actual).get(0).getClass());

        String jsonWithNullTypeProperty = "{\"data\":\"some data\"}";
        try {
            actual = reader.readValue(new JsonFactory().createParser(jsonWithNullTypeProperty));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }

        String jsonWithInvalidTypePropertyName = "{\"invalidTypePropertyName\":\"StringType\",\"data\":\"some data\"}";
        try {
            actual = reader.readValue(new JsonFactory().createParser(jsonWithInvalidTypePropertyName));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }

        String jsonWithEmptyObject = "{}";
        try {
            actual = reader.readValue(new JsonFactory().createParser(jsonWithEmptyObject));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }

        String jsonWithMultipleProperties = "{\"typePropertyName1\":\"StringType1\",\"data1\":\"some data1\"}"
                + "{\"typePropertyName2\":\"StringType2\",\"data2\":\"some data2\"}";
        try {
            actual = reader.readValue(new JsonFactory().createParser(jsonWithMultipleProperties));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @SuppressWarnings("resource")
    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForType(TYPE);

        String jsonWithDefaultImpl = "{\"defaultImpl\":[1,2,3]}";
        Object actual = reader.readValue(new JsonFactory().createParser(jsonWithDefaultImpl));
        assertNotNull(actual);
        assertTrue(actual instanceof Object[]);
        assertEquals(3, ((Object[]) actual).length);
        assertEquals(Integer.class, ((Object[]) actual)[0].getClass());
        assertEquals(Integer.class, ((Object[]) actual)[1].getClass());
        assertEquals(Integer.class, ((Object[]) actual)[2].getClass());
    }

    @SuppressWarnings("resource")
    @Test
    public void testDeserializeTypedUsingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForType(TYPE);

        String jsonWithObjectWithTypeId = "{\"typePropertyName\":\"StringType\",\"typeId\":\"StringTypeId\",\"data\":\"some data\"}";
        Object actual = reader.readValue(new JsonFactory().createParser(jsonWithObjectWithTypeId));
        assertEquals("some data", actual);

        String jsonWithArrayWithTypeId = "[{\"typePropertyName\":\"ListType\",\"typeId\":\"ListIntId\",\"data\":[1,2,3]}]";
        Object actualArray = reader.readValue(new JsonFactory().createParser(jsonWithArrayWithTypeId));
        assertNotNull(actualArray);
        assertTrue(actualArray instanceof List);
        assertEquals(3, ((List) actualArray).size());
        assertEquals(Integer.class, ((List) actualArray).get(0).getClass());
        assertEquals(Integer.class, ((List) actualArray).get(1).getClass());
        assertEquals(Integer.class, ((List) actualArray).get(2).getClass());
    }

    @SuppressWarnings("resource")
    @Test
    public void testDeserializeTypedUsingNativeTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForType(TYPE);

        String jsonWithNativeTypeId = "{\"typePropertyName\":\"StringType\",\"@type\":\"StringTypeClass\"}";
        Object actual = reader.readValue(new JsonFactory().createParser(jsonWithNativeTypeId));
        assertEquals("StringTypeClass", actual.getClass().getName());

        String jsonWithArrayAndNativeTypeId = "[{\"typePropertyName\":\"ListType\",\"@type\":\"ListIntClass\",\"data\":[1,2,3]}]";
        Object actualArray = reader.readValue(new JsonFactory().createParser(jsonWithArrayAndNativeTypeId));
        assertNotNull(actualArray);
        assertTrue(actualArray instanceof List);
        assertEquals(3, ((List) actualArray).size());
        assertEquals(Integer.class, ((List) actualArray).get(0).getClass());
        assertEquals(Integer.class, ((List) actualArray).get(1).getClass());
        assertEquals(Integer.class, ((List) actualArray).get(2).getClass());
    }

    @SuppressWarnings("resource")
    @Test
    public void testDeserializeTypedUsingFieldProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForType(TYPE);

        String jsonWithFieldProperty = "{\"typePropertyName\":{\"name\":\"StringType\",\"beanProperty\":{ \"type\":FIELD, \"name\":\"typeFieldName\", \"getter\":null, \"setter\":null}}},"
                + "{\"data\":\"some data\"}}";
        Object actual = reader.readValue(new JsonFactory().createParser(jsonWithFieldProperty));
        assertEquals("some data", actual);
    }

    @SuppressWarnings("resource")
    @Test
    public void testDeserializeTypedUsingArrayProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForType(TYPE);

        String jsonWithArrayProperty = "{\"typePropertyName\":[{\"name\":\"ListType\",\"beanProperty\":{ \"type\":ARRAY, \"name\":\"arrayFieldName\", \"elementClass\":Integer.class, \"elementTypePropertyName\":null, \"elementTypeId\":null}}]},"
                + "{\"data\":[1,2,3]}";
        Object actual = reader.readValue(new JsonFactory().createParser(jsonWithArrayProperty));
        assertNotNull(actual);
        assertTrue(actual instanceof List);
        assertEquals(3, ((List) actual).size());
        assertEquals(Integer.class, ((List) actual).get(0).getClass());
        assertEquals(Integer.class, ((List) actual).get(1).getClass());
        assertEquals(Integer.class, ((List) actual).get(2).getClass());
    }
}