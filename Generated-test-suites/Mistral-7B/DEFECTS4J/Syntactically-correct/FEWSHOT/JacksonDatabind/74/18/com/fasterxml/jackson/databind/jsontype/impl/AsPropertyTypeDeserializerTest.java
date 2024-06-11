package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * Unit tests for {@link AsPropertyTypeDeserializer}.
 */
public class AsPropertyTypeDeserializerTest {

    private static final String JSON_WITH_TYPE_ID = "{\"type\":\"MyType\",\"data\":\"some data\"}";
    private static final String JSON_WITH_TYPE_PROPERTY = "{\"myTypeProperty\":\"MyType\",\"data\":\"some data\"}";

    @Test
    public void testDeserializeTypedFromObjectWithTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(MyType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, null, "myTypeProperty", true, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser(JSON_WITH_TYPE_ID);
        Object result = deserializer.deserializeTypedFromObject(parser, new TestDeserializationContext());
        assertInstanceOf(MyType.class, result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithoutTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(MyType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, null, "myTypeProperty", true, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser(JSON_WITH_TYPE_PROPERTY);
        Object result = deserializer.deserializeTypedFromObject(parser, new TestDeserializationContext());
        assertInstanceOf(MyType.class, result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithNullTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(MyType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, null, "myTypeProperty", true, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser("{ \"data\":\"some data\" }");
        Object result = deserializer.deserializeTypedFromObject(parser, new TestDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithVisibleTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(MyType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, null, "myTypeProperty", true, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser(JSON_WITH_TYPE_ID);
        Object result = deserializer.deserializeTypedFromObject(parser, new TestDeserializationContext());
        assertInstanceOf(MyType.class, result);

        // The type id should be merged back into the JSON input
        JsonParser parsedParser = parser;
        parser = mapper.getFactory().createParser(new ByteArrayInputStream(parsedParser.getValueAsBytes()));
        assertInstanceOf(MyType.class, deserializer.deserializeTypedFromObject(parser, new TestDeserializationContext()).getClass());
    }

    @Test
    public void testDeserializeTypedFromObjectWithHiddenTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(MyType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, null, "myTypeProperty", false, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser(JSON_WITH_TYPE_ID);
        Object result = deserializer.deserializeTypedFromObject(parser, new TestDeserializationContext());
        assertInstanceOf(MyType.class, result);

        // The type id should not be merged back into the JSON input
        JsonParser parsedParser = parser;
        parser = mapper.getFactory().createParser(new ByteArrayInputStream(parsedParser.getValueAsBytes()));
        assertNull(deserializer.deserializeTypedFromObject(parser, new TestDeserializationContext()));
    }

    @Test
    public void testDeserializeTypedFromObjectWithNativeTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(MyNativeType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, null, "myTypeProperty", true, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser(JSON_WITH_TYPE_ID.replace("MyType", "MyNativeType"));
        Object result = deserializer.deserializeTypedFromObject(parser, new TestDeserializationContext());
        assertInstanceOf(MyNativeType.class, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromObjectWithWrongPropertyName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(MyType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, null, "myTypeProperty", true, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser(JSON_WITH_TYPE_PROPERTY.replace("myTypeProperty", "wrongPropertyName"));
        deserializer.deserializeTypedFromObject(parser, new TestDeserializationContext());
    }
}

class MyType {

}

class MyNativeType {

}

class TestDeserializationContext extends DeserializationContext {

    /**
     * Constructor.
     */
    public TestDeserializationContext() {
        super(new ObjectMapper(), null, null);
    }

}