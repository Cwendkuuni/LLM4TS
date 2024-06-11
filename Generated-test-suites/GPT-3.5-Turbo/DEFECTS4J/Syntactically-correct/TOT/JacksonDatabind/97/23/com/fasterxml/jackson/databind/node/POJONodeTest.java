import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class POJONodeTest {
    private POJONode pojoNode;

    @BeforeEach
    void setup() {
        pojoNode = new POJONode("test");
    }

    @AfterEach
    void teardown() {
        pojoNode = null;
    }

    @Test
    void getNodeType() {
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    void asToken() {
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    void binaryValue_returnsByteArray_whenValueIsByteArray() throws IOException {
        byte[] byteArray = {0x01, 0x02, 0x03};
        POJONode pojoNode = new POJONode(byteArray);

        byte[] result = pojoNode.binaryValue();

        assertArrayEquals(byteArray, result);
    }

    @Test
    void binaryValue_returnsSuperBinaryValue_whenValueIsNotByteArray() throws IOException {
        pojoNode = Mockito.spy(pojoNode);

        pojoNode.binaryValue();

        Mockito.verify(pojoNode).binaryValue();
    }

    @Test
    void asText_returnsNull_whenValueIsNull() {
        POJONode pojoNode = new POJONode(null);

        String result = pojoNode.asText();

        assertNull(result);
    }

    @Test
    void asText_returnsValueAsString_whenValueIsNotNull() {
        String result = pojoNode.asText();

        assertEquals("test", result);
    }

    @Test
    void asText_withDefaultValue_returnsDefaultValue_whenValueIsNull() {
        POJONode pojoNode = new POJONode(null);

        String result = pojoNode.asText("default");

        assertEquals("default", result);
    }

    @Test
    void asText_withDefaultValue_returnsValueAsString_whenValueIsNotNull() {
        String result = pojoNode.asText("default");

        assertEquals("test", result);
    }

    @Test
    void asBoolean_returnsDefaultValue_whenValueIsNull() {
        boolean result = pojoNode.asBoolean(true);

        assertTrue(result);
    }

    @Test
    void asBoolean_returnsValueAsBoolean_whenValueIsBoolean() {
        POJONode pojoNode = new POJONode(true);

        boolean result = pojoNode.asBoolean(false);

        assertTrue(result);
    }

    @Test
    void asBoolean_returnsDefaultValue_whenValueIsNotBoolean() {
        boolean result = pojoNode.asBoolean(false);

        assertFalse(result);
    }

    @Test
    void asInt_returnsDefaultValue_whenValueIsNotNumber() {
        int result = pojoNode.asInt(123);

        assertEquals(123, result);
    }

    @Test
    void asInt_returnsValueAsInt_whenValueIsNumber() {
        POJONode pojoNode = new POJONode(123);

        int result = pojoNode.asInt(0);

        assertEquals(123, result);
    }

    @Test
    void asLong_returnsDefaultValue_whenValueIsNotNumber() {
        long result = pojoNode.asLong(123L);

        assertEquals(123L, result);
    }

    @Test
    void asLong_returnsValueAsLong_whenValueIsNumber() {
        POJONode pojoNode = new POJONode(123L);

        long result = pojoNode.asLong(0L);

        assertEquals(123L, result);
    }

    @Test
    void asDouble_returnsDefaultValue_whenValueIsNotNumber() {
        double result = pojoNode.asDouble(1.23);

        assertEquals(1.23, result, 0.0001);
    }

    @Test
    void asDouble_returnsValueAsDouble_whenValueIsNumber() {
        POJONode pojoNode = new POJONode(1.23);

        double result = pojoNode.asDouble(0.0);

        assertEquals(1.23, result, 0.0001);
    }

    @Test
    void serialize_callsDefaultSerializeNull_whenValueIsNull() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        POJONode pojoNode = new POJONode(null);

        pojoNode.serialize(generator, provider);

        Mockito.verify(provider).defaultSerializeNull(generator);
    }

    @Test
    void serialize_callsSerializeOnValue_whenValueIsJsonSerializable() throws IOException {
        JsonSerializable jsonSerializable = Mockito.mock(JsonSerializable.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        POJONode pojoNode = new POJONode(jsonSerializable);

        pojoNode.serialize(generator, provider);

        Mockito.verify(jsonSerializable).serialize(generator, provider);
    }

    @Test
    void serialize_callsWriteObjectOnGenerator_whenValueIsNotJsonSerializable() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        pojoNode.serialize(generator, provider);

        Mockito.verify(generator).writeObject(pojoNode.getPojo());
    }

    @Test
    void getPojo_returnsValue() {
        assertEquals("test", pojoNode.getPojo());
    }

    @Test
    void equals_returnsTrueWhenObjectsAreSame() {
        assertTrue(pojoNode.equals(pojoNode));
    }

    @Test
    void equals_returnsTrueWhenValuesAreEqual() {
        POJONode pojoNode1 = new POJONode("test");
        POJONode pojoNode2 = new POJONode("test");

        assertTrue(pojoNode1.equals(pojoNode2));
    }

    @Test
    void equals_returnsFalseWhenValuesAreNotEqual() {
        POJONode pojoNode1 = new POJONode("test");
        POJONode pojoNode2 = new POJONode("different");

        assertFalse(pojoNode1.equals(pojoNode2));
    }

    @Test
    void equals_returnsFalseWhenValueIsNull() {
        POJONode pojoNode1 = new POJONode(null);
        POJONode pojoNode2 = new POJONode("test");

        assertFalse(pojoNode1.equals(pojoNode2));
    }

    @Test
    void equals_returnsFalseWhenValueIsDifferentType() {
        POJONode pojoNode1 = new POJONode(true);
        POJONode pojoNode2 = new POJONode("test");

        assertFalse(pojoNode1.equals(pojoNode2));
    }

    @Test
    void hashCode_returnsHashCodeOfValue() {
        int expectedHashCode = "test".hashCode();

        int hashCode = pojoNode.hashCode();

        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    void toString_returnsValueAsString() {
        String result = pojoNode.toString();

        assertEquals("test", result);
    }
}