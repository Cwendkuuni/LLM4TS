import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer<String> stringSerializer;
    private StdDelegatingSerializer<Integer> intSerializer;
    private JsonSerializer<Object> delegateSerializerMock;

    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        delegateSerializerMock = mock(JsonSerializer.class);

        stringSerializer = new StdDelegatingSerializer<String>(String.class, new IdentityConverter<>(), null, delegateSerializerMock);
        intSerializer = new StdDelegatingSerializer<Integer>(Integer.class, new IdentityConverter<>(), null, delegateSerializerMock);
    }

    @Test
    public void testConstructorWithConverterAndClass() {
        Assert.assertEquals(String.class, stringSerializer.getClass());
        Assert.assertEquals(String.class, stringSerializer.getType());
    }

    @Test
    public void testConstructorWithConverter() {
        Assert.assertEquals(Object.class, StdDelegatingSerializer.class);
        Assert.assertNull(stringSerializer.getType());
    }

    @Test
    public void testConstructorWithConverterConverterDelegateTypeAndDelegateSerializer() {
        Assert.assertEquals(String.class, stringSerializer.getType());
        Assert.assertEquals(String.class, stringSerializer.getClass());
    }

    @Test
    public void testIsEmptyWhenValueIsNull() throws JsonMappingException, IOException {
        when(delegateSerializerMock.isEmpty(any(Object.class), any(SerializerProvider.class))).thenReturn(true);

        Assert.assertTrue(stringSerializer.isEmpty(null));
    }

    @Test
    public void testIsEmptyWhenValueIsEmptyString() throws JsonMappingException, IOException {
        when(delegateSerializerMock.isEmpty(any(Object.class), any(SerializerProvider.class))).thenReturn(false);

        Assert.assertFalse(stringSerializer.isEmpty(""));
    }

    @Test
    public void testIsEmptyWhenValueIsEmptyArray() throws JsonMappingException, IOException {
        when(delegateSerializerMock.isEmpty(any(Object.class), any(SerializerProvider.class))).thenReturn(false);

        Assert.assertFalse(stringSerializer.isEmpty(new String[0]));
    }

    @Test
    public void testIsEmptyWhenValueIsEmptyMap() throws JsonMappingException, IOException {
        when(delegateSerializerMock.isEmpty(any(Object.class), any(SerializerProvider.class))).thenReturn(false);

        Map<String, String> emptyMap = new LinkedHashMap<>();
        Assert.assertFalse(stringSerializer.isEmpty(emptyMap));
    }

    @Test
    public void testIsEmptyWhenValueIsEmptyCollection() throws JsonMappingException, IOException {
        when(delegateSerializerMock.isEmpty(any(Object.class), any(SerializerProvider.class))).thenReturn(false);

        Assert.assertFalse(stringSerializer.isEmpty(Arrays.asList()));
    }

    @Test
    public void testConvertValue() throws JsonMappingException, IOException {
        String input = "Test Input";
        String expectedOutput = "Test Output";
        when(delegateSerializerMock.serialize(any(Object.class), any(JsonGenerator.class), any(SerializerProvider.class))).thenAnswer(invocation -> {
            ObjectNode node = new ObjectMapper().createObjectNode();
            node.put("output", expectedOutput);
            return node;
        });

        String output = stringSerializer.convertValue(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test(expected = JsonMappingException.class)
    public void testConvertValueWithNullConverter() throws JsonMappingException, IOException {
        new StdDelegatingSerializer(null, String.class, null, delegateSerializerMock);
    }

    @Test
    public void testGetDelegateSerializer() {
        Assert.assertEquals(delegateSerializerMock, stringSerializer.getDelegatee());
    }

    @Test
    public void testGetConverter() {
        Assert.assertNotNull(stringSerializer.getConverter());
    }

    @Test
    public void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
        String input = "Test Input";
        when(delegateSerializerMock.serialize(any(Object.class), any(JsonGenerator.class), any(SerializerProvider.class))).thenAnswer(invocation -> {
            ObjectGenerator generator = invocation.getArgument(1);
            generator.writeString("Test Output");
            return null;
        });

        String expectedOutput = "Test Output";

        byte[] result = stringSerializer.serialize(input);

        Assert.assertEquals(expectedOutput, new String(result, "UTF-8"));
    }

    @Test
    public void testSerializeWithType() throws JsonGenerationException, JsonMappingException, IOException {
        String input = "Test Input";
        ObjectNode expectedNode = new ObjectMapper().createObjectNode();
        expectedNode.put("output", "Test Output");

        when(delegateSerializerMock.serializeWithType(any(Object.class), any(JsonGenerator.class), any(SerializerProvider.class), any(TypeSerializer.class))).thenReturn(expectedNode);

        byte[] result = stringSerializer.serializeWithType(input, null, null, null);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode actualNode = objectMapper.readValue(result, ObjectNode.class);

        Assert.assertEquals(expectedNode, actualNode);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        when(delegateSerializerMock.getSchema(any(SerializerProvider.class), any(Type.class))).thenReturn(null);

        JsonNode actualSchema = stringSerializer.getSchema(null, null);

        Assert.assertNull(actualSchema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        when(delegateSerializerMock.acceptJsonFormatVisitor(any(JsonFormatVisitorWrapper.class), any(JavaType.class))).thenReturn(null);

        stringSerializer.acceptJsonFormatVisitor(any(JsonFormatVisitorWrapper.class), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithNullDelegateSerializer() {
        stringSerializer.serialize(null, null, null);
    }

    @Test
    public void testConvertValueWhenInputIsNull() {
        Assert.assertNull(stringSerializer.convertValue(null));
    }

    @Test
    public void testConvertValueWhenInputIsDelegateType() {
        Integer input = 5;
        when(delegateSerializerMock.serialize(eq(5), any(JsonGenerator.class), any(SerializerProvider.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Integer output = intSerializer.convertValue(input);

        Assert.assertEquals(input, output);
    }

    @Test
    public void testConvertValueWhenInputIsNotDelegateType() {
        String input = "Test Input";
        when(delegateSerializerMock.serialize(any(Object.class), any(JsonGenerator.class), any(SerializerProvider.class)))
                .thenAnswer(invocation -> "Test Output");

        String output = intSerializer.convertValue(input);

        Assert.assertEquals("Test Output", output);
    }
}