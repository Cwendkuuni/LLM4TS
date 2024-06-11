package com.example.test; // replace with your test package name

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JsonValueSerializerTest {

    private JsonSerializer<Object> valueSerializerMock;
    private ObjectNode testObjectNode;
    private JsonGenerator testJsonGenerator;
    private SerializerProvider serializerProviderMock;
    private JsonFactory jsonFactory;
    private JsonValueSerializer jsonValueSerializer;

    @Before
    public void setup() throws Exception {
        valueSerializerMock = mock(JsonSerializer.class);
        testObjectNode = mock(ObjectNode.class);
        testJsonGenerator = mock(JsonGenerator.class);
        serializerProviderMock = mock(SerializerProvider.class);
        jsonFactory = new ObjectMapper().getFactory();

        when(serializerProviderMock.findValueSerializer(any(Class.class), any(BeanProperty.class))).thenReturn(valueSerializerMock);

        jsonValueSerializer = new JsonValueSerializer(null, null, valueSerializerMock, false);
    }

    @Test
    public void testSerializeWithNullValue() throws Exception {
        jsonValueSerializer.serialize(null, testJsonGenerator, serializerProviderMock);
        verify(testJsonGenerator).writeNull();
    }

    @Test
    public void testSerializeWithValidValue() throws Exception {
        Object testValue = new Object() {
            @JsonValue
            public String getValue() {
                return "Test Value";
            }
        };

        when(valueSerializerMock.serialize(any(Object.class), eq(testJsonGenerator), eq(serializerProviderMock))).thenReturn(null);

        jsonValueSerializer.serialize(testValue, testJsonGenerator, serializerProviderMock);

        verify(valueSerializerMock).serialize(eq(testValue), eq(testJsonGenerator), eq(serializerProviderMock));
        assertEquals("\"Test Value\"", testObjectNode.getValueAsString());
    }

    @Test(expected = IOException.class)
    public void testSerializeWithThrowableValue() throws Exception {
        Object testValue = new Object() {
            @JsonValue
            public String getValue() throws IOException {
                throw new IOException("Test IOException");
            }
        };

        when(valueSerializerMock.serialize(any(Object.class), eq(testJsonGenerator), eq(serializerProviderMock))).thenThrow(new IOException("Test IOException"));

        jsonValueSerializer.serialize(testValue, testJsonGenerator, serializerProviderMock);
    }

    @Test
    public void testSerializeWithTypeInformation() throws Exception {
        Object testValue = new Object() {
            @JsonValue
            public String getValue() {
                return "Test Value";
            }
        };
        TypeSerializer typeSerializerMock = mock(TypeSerializer.class);

        when(serializerProviderMock.findTypeSerializer(any(Class.class), eq(Object.class), anyBoolean()))
                .thenReturn(typeSerializerMock);

        jsonValueSerializer = new JsonValueSerializer(null, null, valueSerializerMock, true);

        jsonValueSerializer.serializeWithType(testValue, testJsonGenerator, serializerProviderMock, typeSerializerMock);

        verify(typeSerializerMock).writeTypePrefixForScalar(any(Object.class), eq(testJsonGenerator));
        jsonValueSerializer.serialize(testValue, testJsonGenerator, serializerProviderMock);
        verify(typeSerializerMock).writeTypeSuffixForScalar(any(Object.class), eq(testJsonGenerator));
    }

    @Test
    public void testGetSchema() throws Exception {
        ObjectNode expectedSchemaNode = jsonFactory.createObjectNode();
        expectedSchemaNode.put("type", "object");
        expectedSchemaNode.put("properties", jsonFactory.createObjectNode());

        when(valueSerializerMock.getSchema(any(SerializerProvider.class), any(Type.class)))
                .thenReturn(expectedSchemaNode);

        JsonNode actualSchemaNode = jsonValueSerializer.getSchema(serializerProviderMock, Object.class);

        assertEquals(expectedSchemaNode, actualSchemaNode);
    }
}