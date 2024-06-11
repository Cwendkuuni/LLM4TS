package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TypeDeserializerBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link AsWrapperTypeDeserializer} class.
 */
public class AsWrapperTypeDeserializerTest {

    private DeserializationContext ctxt;
    private JsonParser jp;
    private TypeIdResolver idRes;
    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        idRes = mock(TypeIdResolver.class);
        mapper = new ObjectMapper();
        ctxt = mapper.getDeserializationContext();
        jp = mapper.getFactory().createParser("[{\"type\":\"MyType\",\"value\":42}]");
    }

    @Test
    public void testDeserializeTypedFromObjectWithValidTypeId() throws IOException {
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType.class), idRes, null, false, MyDefaultImpl.class);

        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
        assertInstanceOf(MyType.class, result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithInvalidTypeId() throws IOException {
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType.class), idRes, null, false, MyDefaultImpl.class);

        jp.nextToken(); // consume [
        jp.nextToken(); // consume {
        jp.nextToken(); // consume "type"
        String typeId = jp.getText();
        jp.nextToken(); // consume :
        jp.nextToken(); // consume "wrongType"
        jp.nextToken(); // consume ,
        jp.nextToken(); // consume "value"
        jp.nextToken(); // consume :
        jp.nextInt(); // consume 42
        jp.nextToken(); // consume ]

        try {
            deserializer.deserializeTypedFromObject(jp, ctxt);
            assert false; // should not reach here
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeTypedFromObjectWithNullTypeId() throws IOException {
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType.class), idRes, null, false, MyDefaultImpl.class);

        jp.nextToken(); // consume [
        jp.nextToken(); // consume {
        jp.nextToken(); // consume "type"
        jp.nextToken(); // consume null
        jp.nextToken(); // consume ,
        jp.nextToken(); // consume "value"
        jp.nextToken(); // consume :
        jp.nextInt(); // consume 42
        jp.nextToken(); // consume ]

        try {
            deserializer.deserializeTypedFromObject(jp, ctxt);
            assertInstanceOf(MyDefaultImpl.class, deserializer.getDefaultValue());
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeTypedFromObjectWithNoTypeId() throws IOException {
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType.class), idRes, null, false, MyDefaultImpl.class);

        jp.nextToken(); // consume [
        jp.nextToken(); // consume {
        jp.nextToken(); // consume "value"
        jp.nextToken(); // consume :
        jp.nextInt(); // consume 42
        jp.nextToken(); // consume ]

        try {
            deserializer.deserializeTypedFromObject(jp, ctxt);
            assertInstanceOf(MyDefaultImpl.class, deserializer.getDefaultValue());
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeTypedFromObjectWithEmptyTypeId() throws IOException {
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType.class), idRes, null, false, MyDefaultImpl.class);

        jp.nextToken(); // consume [
        jp.nextToken(); // consume {
        jp.nextToken(); // consume "type"
        jp.nextString("");
        jp.nextToken(); // consume :
        jp.nextInt(); // consume 42
        jp.nextToken(); // consume ]

        try {
            deserializer.deserializeTypedFromObject(jp, ctxt);
            assertInstanceOf(MyDefaultImpl.class, deserializer.getDefaultValue());
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeTypedFromArrayWithValidTypeId() throws IOException {
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType[].class), idRes, null, false, null);

        Object result = deserializer.deserializeTypedFromArray(jp, ctxt);
        assertInstanceOf(MyType[].class, result.getClass());
        assertEquals(1, ((MyType[]) result).length);
        assertInstanceOf(MyType.class, ((MyType[]) result)[0]);
    }

    @Test
    public void testDeserializeTypedFromArrayWithInvalidTypeId() throws IOException {
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType[].class), idRes, null, false, null);

        jp.nextToken(); // consume [
        jp.nextToken(); // consume {
        jp.nextToken(); // consume "type"
        jp.nextString("wrongType");
        jp.nextToken(); // consume "value"
        jp.nextToken(); // consume :
        jp.nextInt(); // consume 42
        jp.nextToken(); // consume ]

        try {
            deserializer.deserializeTypedFromArray(jp, ctxt);
            // should not reach here
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeTypedFromArrayWithNoTypeId() throws IOException {
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType[].class), idRes, null, false, null);

        jp.nextToken(); // consume [
        jp.nextToken(); // consume {
        jp.nextToken(); // consume "value"
        jp.nextToken(); // consume :
        jp.nextInt(); // consume 42
        jp.nextToken(); // consume ]

        MyType[] result = (MyType[]) deserializer.deserializeTypedFromArray(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromArrayWithEmptyTypeId() throws IOException {
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType[].class), idRes, null, false, null);

        jp.nextToken(); // consume [
        jp.nextToken(); // consume {
        jp.nextToken(); // consume "type"
        jp.nextString("");
        jp.nextToken(); // consume "value"
        jp.nextToken(); // consume :
        jp.nextInt(); // consume 42
        jp.nextToken(); // consume ]

        MyType[] result = (MyType[]) deserializer.deserializeTypedFromArray(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromScalarWithValidTypeId() throws IOException {
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType.class), idRes, null, false, MyDefaultImpl.class);

        jp.nextToken(); // consume 42

        Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);
        assertInstanceOf(MyType.class, result);
    }

    @Test
    public void testDeserializeTypedFromScalarWithInvalidTypeId() throws IOException {
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType.class), idRes, null, false, MyDefaultImpl.class);

        String typeId = "wrongType";
        when(idRes.getId(jp, ctxt, MyType.class)).thenReturn(typeId);

        jp.nextToken(); // consume 42

        try {
            deserializer.deserializeTypedFromScalar(jp, ctxt);
            assert false; // should not reach here
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeTypedFromScalarWithNoTypeId() throws IOException {
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType.class), idRes, null, false, MyDefaultImpl.class);

        jp.nextToken(); // consume 42

        Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);
        assertInstanceOf(MyDefaultImpl.class, result);
    }

    @Test
    public void testDeserializeTypedFromAnyWithValidTypeId() throws IOException {
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType.class), idRes, null, false, MyDefaultImpl.class);

        jp.nextValue(); // consume 42

        Object result = deserializer.deserializeTypedFromAny(jp, ctxt);
        assertInstanceOf(MyType.class, result);
    }

    @Test
    public void testDeserializeTypedFromAnyWithInvalidTypeId() throws IOException {
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType.class), idRes, null, false, MyDefaultImpl.class);

        jp.nextValue(); // consume 42

        String typeId = "wrongType";
        when(idRes.getId(jp, ctxt, MyType.class)).thenReturn(typeId);

        try {
            deserializer.deserializeTypedFromAny(jp, ctxt);
            assert false; // should not reach here
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeTypedFromAnyWithNoTypeId() throws IOException {
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType.class), idRes, null, false, MyDefaultImpl.class);

        jp.nextValue(); // consume 42

        Object result = deserializer.deserializeTypedFromAny(jp, ctxt);
        assertInstanceOf(MyDefaultImpl.class, result);
    }

    private static class MyType {
    }

    private static class MyDefaultImpl {
    }
}