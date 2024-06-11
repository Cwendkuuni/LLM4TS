package com.example.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import org.junit.*;
import org.junit.runner.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(JUnitRunners.JUnit4.class)
public class StdKeySerializerTest {

    @Before
    public void setUp() {
        // initialize mocks, if needed
    }

    @After
    public void tearDown() {
        // clean up mocks, if needed
    }

    @Test
    public void testSerialize() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new FileOutputStream("output.json"), JsonEncoding.UTF8);
        SerializerProvider provider = mapper.getSerializerProvider();

        Object testValue = new Date();
        serializer.serialize(testValue, gen, provider);

        gen.close();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File("output.json"));

        assertEquals("expected serialization output", testValue.toString(), jsonNode.asText());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonNode expectedSchema = createSchemaNode("string");

        JsonNode actualSchema = serializer.getSchema(mock(SerializerProvider.class), new TypeReference<JsonNode>() {});

        assertEquals("expected schema", expectedSchema, actualSchema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);

        serializer.acceptJsonFormatVisitor(visitor, new TypeReference<JsonNode>() {});

        verify(visitor).expectStringFormat(new TypeReference<JsonNode>() {});
    }

    // test cases for edge cases and exceptions

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullJsonGenerator() {
        StdKeySerializer serializer = new StdKeySerializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = null;
        SerializerProvider provider = mapper.getSerializerProvider();

        serializer.serialize(new Date(), gen, provider);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullSerializerProvider() {
        StdKeySerializer serializer = new StdKeySerializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new FileOutputStream("output.json"), JsonEncoding.UTF8);
        SerializerProvider provider = null;

        serializer.serialize(new Date(), gen, provider);
    }

    @Test(expected = NullPointerException.class)
    public void testSerializeWithNullValue() {
        StdKeySerializer serializer = new StdKeySerializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new FileOutputStream("output.json"), JsonEncoding.UTF8);
        SerializerProvider provider = mapper.getSerializerProvider();

        serializer.serialize(null, gen, provider);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetSchemaWithNullSerializerProvider() {
        StdKeySerializer serializer = new StdKeySerializer();

        try {
            serializer.getSchema(null, new TypeReference<JsonNode>() {});
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testGetSchemaWithNullTypeHint() {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = mock(SerializerProvider.class);

        try {
            serializer.getSchema(provider, null);
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorWithNullJsonFormatVisitorWrapper() {
        StdKeySerializer serializer = new StdKeySerializer();

        try {
            serializer.acceptJsonFormatVisitor(null, new TypeReference<JsonNode>() {});
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorWithNullTypeHint() {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);

        try {
            serializer.acceptJsonFormatVisitor(visitor, null);
        } catch (JsonMappingException e) {
            // expected
        }
    }
}