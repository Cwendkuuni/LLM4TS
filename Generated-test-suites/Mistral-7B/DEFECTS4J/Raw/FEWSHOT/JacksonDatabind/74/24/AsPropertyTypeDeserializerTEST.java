package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("{\"type\":\"MyType\",\"data\":42}");
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType.class),
                mapper.getTypeIdResolver(),
                "type",
                true,
                mapper.getTypeFactory().constructType(MyOtherType.class)
        );
        assertEquals(MyType.class, deserializer.deserializeTypedFromObject(parser, null).getClass());

        parser.nextToken(); // consume "data"
        JsonDeserializer<MyOtherType> myOtherTypeDeserializer = mapper.findValueDeserializer(MyOtherType.class);
        assertEquals(MyOtherType.class, deserializer.deserializeTypedFromObject(parser, null).getClass(), myOtherTypeDeserializer);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("[[{\"type\":\"MyType\",\"data\":42}],[{\"type\":\"MyOtherType\",\"data\":\"foo\"}]]");
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType.class),
                mapper.getTypeIdResolver(),
                "type",
                true,
                mapper.getTypeFactory().constructType(MyOtherType.class)
        );

        assertEquals(MyType.class, deserializer.deserializeTypedFromArray(parser, null).get(0).getClass());
        assertEquals(MyOtherType.class, deserializer.deserializeTypedFromArray(parser, null).get(1).getClass());
    }

    @Test
    public void testDeserializeUsingDefaultImpl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("{\"data\":42}");
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                null,
                null,
                null,
                false,
                null
        );
        assertEquals(Integer.class, deserializer.deserializeTypedFromObject(parser, null).getClass());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("{\"type\":\"MyType\",\"data\":42}");
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType.class),
                mapper.getTypeIdResolver(),
                "type",
                true,
                mapper.getTypeFactory().constructType(MyOtherType.class)
        );
        JsonDeserializer<MyType> myTypeDeserializer = mapper.findValueDeserializer(MyType.class);
        assertEquals(myTypeDeserializer, deserializer.findDeserializer(null, "MyType"));
        assertNull(deserializer.findDeserializer(null, "UnknownType"));
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("{\"@class\":\"MyType\",\"data\":42}");
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType.class),
                mapper.getTypeIdResolver(),
                "type",
                true,
                mapper.getTypeFactory().constructType(MyOtherType.class)
        );
        assertEquals(MyType.class, deserializer.deserializeTypedFromObject(parser, null).getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("{\"data\":42}");
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyType.class),
                mapper.getTypeIdResolver(),
                "type",
                true,
                mapper.getTypeFactory().constructType(MyOtherType.class)
        );
        deserializer.deserializeTypedFromObject(parser, null);
    }
}

class MyType {}
class MyOtherType {}