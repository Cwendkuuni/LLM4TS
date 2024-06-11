import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BehaviorBasedTestBase;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest extends BehaviorBasedTestBase {
    private StdDelegatingSerializer<Object> serializer;
    private Converter<Object, ?> converter;

    @Before
    public void setup() throws IOException {
        ObjectNode configure = mapper.readValue(getClass().getResourceAsStream("/serializer-test-config.json"), ObjectNode.class);
        converter = mapper.getTypeFactory().constructConverter(Object.class, String.class);
        serializer = new StdDelegatingSerializer<>(converter);
        ObjectNode delegateTypeNode = configure.get("delegateType");
        JavaType delegateType = mapper.getTypeFactory().constructType(delegateTypeNode.asText());
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(delegateType);
        serializer = serializer.withDelegate(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testDefaultConstructor() {
        Object value = new Object();
        Object delegateValue = serializer.convertValue(value);
        Assert.assertNotNull(delegateValue);
    }

    @Test
    public void testConstructorWithConverter() throws IOException {
        Object value = new Object();
        Object delegateValue = serializer.convertValue(value);
        Assert.assertNotNull(delegateValue);
    }

    @Test
    public void testConstructorWithConverterDelegateTypeSerializer() throws IOException {
        Object value = new Object();
        Object delegateValue = serializer.convertValue(value);
        Assert.assertNotNull(delegateValue);
        JsonSerializer<Object> delegateSerializer = serializer.getDelegatee();
        Assert.assertNotNull(delegateSerializer);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        Object value = new Object();
        serializer.resolve(null);
        Object delegateValue = serializer.convertValue(value);
        Assert.assertNotNull(delegateValue);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        Object value = new Object();
        Object delegateValue = serializer.convertValue(value);
        JsonSerializer<Object> delSer = serializer.createContextual(null, null);
        JsonSerializer<Object> expectedDelSer = serializer.getDelegatee();
        Assert.assertEquals(expectedDelSer, delSer);
    }

    @Test
    public void testGetConverter() {
        Assert.assertEquals(converter, serializer.getConverter());
    }

    @Test
    public void testGetDelegatee() {
        JsonSerializer<Object> expectedDelegatee = serializer.getDelegatee();
        Assert.assertNotNull(expectedDelegatee);
    }

    @Test
    public void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
        Object value = "hello";
        Object delegateValue = serializer.convertValue(value);
        String expectedString = "hello";
        JsonGenerator gen = createMyJsonGenerator();
        serializer.serialize(delegateValue, gen, null);
        String actualString = mapper.writeValueAsString(gen.getOutputStream().toString());
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testSerializeWithType() throws JsonGenerationException, JsonMappingException, IOException {
        Object value = "hello";
        Object delegateValue = serializer.convertValue(value);
        TypeSerializer typeSerializer = mapper.getTypeFactory().getPolymorphicTypeSerializer(String.class, null);
        serializer.serializeWithType(delegateValue, createMyJsonGenerator(), null, typeSerializer);
        String actualString = mapper.writeValueAsString(createMyJsonGenerator().getOutputStream().toString());
        ObjectNode actualNode = mapper.readValue(actualString, ObjectNode.class);
        ObjectNode expectedNode = mapper.createObjectNode();
        expectedNode.put("c", "hello");
        JsonNode actualNodeType = actualNode.get("c");
        JsonNode expectedNodeType = expectedNode.get("c");
        Assert.assertEquals(expectedNodeType, actualNodeType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullValue() throws JsonGenerationException, JsonMappingException, IOException {
        Object value = null;
        JsonGenerator gen = createMyJsonGenerator();
        serializer.serialize(value, gen, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolveWithNullDelegateSerializer() throws JsonMappingException {
        serializer.resolve(null);
        serializer._delegateSerializer = null;
        Assert.assertThrows(IllegalArgumentException.class, () -> serializer.createContextual(null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithNullDelegateSerializer() throws JsonMappingException {
        Object value = new Object();
        serializer.resolve(null);
        Object delegateValue = serializer.convertValue(value);
        serializer._delegateSerializer = null;
        JsonSerializer<Object> delSer = serializer.createContextual(null, null);
        Assert.assertThrows(IllegalArgumentException.class, () -> delSer.serialize(delegateValue, createMyJsonGenerator(), null));
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerializeWithInvalidDelegateType() throws JsonGenerationException, JsonMappingException, IOException {
        Object value = new Object();
        Object delegateValue = serializer.convertValue(value);
        TypeSerializer typeSerializer = mapper.getTypeFactory().getPolymorphicTypeSerializer(Integer.class, null);
        serializer.serializeWithType(delegateValue, createMyJsonGenerator(), null, typeSerializer);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectNode schema = serializer.getSchema(null, Object.class);
        Assert.assertNotNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mapper.getFactory().getJsonFormatVisitor(SerializerProvider.stdSerializerProvider(), null);
        serializer.acceptJsonFormatVisitor(visitor, getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorWithNullVisitor() throws JsonMappingException {
        serializer.acceptJsonFormatVisitor(null, getClass());
    }

    private JsonGenerator createMyJsonGenerator() throws JsonGenerationException, JsonMappingException {
        return mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
    }
}