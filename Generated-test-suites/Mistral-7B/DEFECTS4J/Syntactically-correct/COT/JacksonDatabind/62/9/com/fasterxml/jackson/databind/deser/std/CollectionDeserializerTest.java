package com.example.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CollectionDeserializerTest {

    private ObjectMapper objectMapper;
    private JsonParser jsonParser;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        jsonParser = objectMapper.getFactory().createParser("[]");
    }

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        Collection<Object> expectedResult = new ArrayList<>();
        Collection<Object> result = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class),
                null, null, null, null, null).deserialize(jsonParser, ObjectMapper.getDefaultInstance().getDeserializationContext(), expectedResult);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testSingleElementCollectionDeserialization() throws IOException {
        JsonParser jsonParserWithSingleElement = objectMapper.getFactory().createParser("[1]");
        Collection<Object> expectedResult = new ArrayList<>();
        expectedResult.add(1);
        Collection<Object> result = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class),
                null, null, null, null, null).deserialize(jsonParserWithSingleElement, ObjectMapper.getDefaultInstance().getDeserializationContext(), expectedResult);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializationWithTypeDeserializer() throws IOException {
        JsonParser jsonParserWithStringArray = objectMapper.getFactory().createParser("[\"Hello\", \"World\"]");
        JavaType stringType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String[].class);
        TypeDeserializer typeDeserializer = ObjectMapper.getDefaultInstance().getTypeDeserializer().forProperty(null);
        Collection<Object> expectedResult = new ArrayList<>();
        expectedResult.add("Hello");
        expectedResult.add("World");
        Collection<Object> result = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class),
                null, typeDeserializer, null, null, null).deserialize(jsonParserWithStringArray, ObjectMapper.getDefaultInstance().getDeserializationContext(), expectedResult);
        Assert.assertEquals(expectedResult, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidJson() throws IOException {
        JsonParser jsonParserWithInvalidJson = objectMapper.getFactory().createParser("invalid_json");
        Collection<Object> result = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class),
                null, null, null, null, null).deserialize(jsonParserWithInvalidJson, ObjectMapper.getDefaultInstance().getDeserializationContext(), null);
    }

    @Test
    public void testDeserializationWithNullValue() throws IOException {
        JsonParser jsonParserWithNullValue = objectMapper.getFactory().createParser("null");
        Collection<Object> expectedResult = new ArrayList<>();
        Collection<Object> result = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class),
                null, null, null, null, null).deserialize(jsonParserWithNullValue, ObjectMapper.getDefaultInstance().getDeserializationContext(), expectedResult);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializationWithDelegateDeserializer() throws IOException {
        JsonParser jsonParserWithDelegateDeserializer = objectMapper.getFactory().createParser("{ \"name\": \"John\" }");
        JsonDeserializer<?> delegateDeserializer = new FromStringDeserializer();
        Collection<Object> expectedResult = new ArrayList<>();
        expectedResult.add(new FromStringDeserializer.Name("John"));
        Collection<Object> result = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class),
                null, null, null, delegateDeserializer, null).deserialize(jsonParserWithDelegateDeserializer, ObjectMapper.getDefaultInstance().getDeserializationContext(), expectedResult);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializationWithUnwrappedSingleValue() throws IOException {
        JsonParser jsonParserWithUnwrappedSingleValue = objectMapper.getFactory().createParser("1");
        Collection<Object> expectedResult = new ArrayList<>();
        Collection<Object> result = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class),
                null, null, null, null, Boolean.TRUE).deserialize(jsonParserWithUnwrappedSingleValue, ObjectMapper.getDefaultInstance().getDeserializationContext(), expectedResult);
        Assert.assertEquals(expectedResult, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithUnwrappedSingleValueAndInvalidJson() throws IOException {
        JsonParser jsonParserWithUnwrappedSingleValueAndInvalidJson = objectMapper.getFactory().createParser("invalid_json");
        Collection<Object> result = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class),
                null, null, null, null, Boolean.TRUE).deserialize(jsonParserWithUnwrappedSingleValueAndInvalidJson, ObjectMapper.getDefaultInstance().getDeserializationContext(), null);
    }

    @Test
    public void testDeserializationWithForwardReference() throws IOException {
        JsonParser jsonParserWithForwardReference = objectMapper.getFactory().createParser("[{ \"$ref\": \"#/properties/element\" }]");
        JsonParser jsonParserWithElementValue = objectMapper.getFactory().createParser("{\"element\": 1}");
        Collection<Object> expectedResult = new ArrayList<>();
        Collection<Object> result = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class),
                null, null, null, null, null).deserialize(jsonParserWithForwardReference, ObjectMapper.getDefaultInstance().getDeserializationContext(), expectedResult);
        ObjectMapper objectMapperForResolveForwardReference = new ObjectMapper();
        Collection<Object> resolvedResult = objectMapperForResolveForwardReference.readValue(jsonParserWithElementValue, Collection.class);
        result.get(0).resolveForwardReference(resolvedResult.get(0), null);
        Assert.assertEquals(expectedResult, result);
    }
}

class FromStringDeserializer {
    public static class Name {
        private String name;

        public Name(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class Deserializer extends JsonDeserializer<Name> {
        @Override
        public Name deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            String name = jsonParser.getValueAsString();
            return new Name(name);
        }
    }

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) throws Exception {
                context.addSerializer(Name.class, new SimpleSerializer<Name>() {
                    @Override
                    public void serialize(Name value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
                        jsonGenerator.writeString(value.getName());
                    }
                });
                context.addDeserializer(Name.class, new Deserializer());
            }
        });
    }
}