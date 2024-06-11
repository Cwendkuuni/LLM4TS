package com.example.jsonvalueserializer;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class JsonValueSerializerTest {

    private Object testObject;
    private JsonSerializer<Object> valueSerializerMock;
    private JsonValueSerializer jsonValueSerializer;
    private AnnotatedMethod accessorMethodMock;

    @Before
    public void setUp() throws Exception {
        testObject = new TestClass();
        valueSerializerMock = mock(JsonSerializer.class);
        accessorMethodMock = mock(AnnotatedMethod.class);
        when(accessorMethodMock.getValue(any())).thenReturn("testValue");

        jsonValueSerializer = new JsonValueSerializer(accessorMethodMock, valueSerializerMock);
    }

    @Test
    public void testDefaultConstructor() {
        JsonValueSerializer serializer = new JsonValueSerializer();
        Assert.assertNull(serializer._accessorMethod);
        Assert.assertNull(serializer._valueSerializer);
        Assert.assertNull(serializer._property);
        Assert.assertTrue(serializer._forceTypeInformation);
    }

    @Test
    public void testConstructorWithValueMethodAndSerializer() {
        JsonValueSerializer serializer = new JsonValueSerializer(accessorMethodMock, valueSerializerMock);
        Assert.assertEquals(accessorMethodMock, serializer._accessorMethod);
        Assert.assertEquals(valueSerializerMock, serializer._valueSerializer);
        Assert.assertNull(serializer._property);
        Assert.assertTrue(serializer._forceTypeInformation);
    }

    @Test
    public void testConstructorWithResolvedProperties() throws Exception {
        BeanProperty propertyMock = mock(BeanProperty.class);
        JsonSerializer<Object> resolvedSerializerMock = mock(JsonSerializer.class);

        JsonValueSerializer serializer = new JsonValueSerializer(jsonValueSerializer, propertyMock, resolvedSerializerMock, false);
        Assert.assertEquals(jsonValueSerializer, serializer);
        Assert.assertEquals(propertyMock, serializer._property);
        Assert.assertEquals(resolvedSerializerMock, serializer._valueSerializer);
        Assert.assertFalse(serializer._forceTypeInformation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullType() {
        JsonValueSerializer serializer = new JsonValueSerializer(null, valueSerializerMock);
        // The constructor should throw an IllegalArgumentException with a null type parameter
    }

    @Test
    public void testCreateContextual() throws Exception {
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        BeanProperty propertyMock = mock(BeanProperty.class);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethodMock, null);
        JsonValueSerializer serializedJsonValueSerializer = jsonValueSerializer.createContextual(mock(SerializerProvider.class), propertyMock);

        Assert.assertEquals(jsonValueSerializer, serializedJsonValueSerializer);
        Assert.assertEquals(valueSerializerMock, serializedJsonValueSerializer._valueSerializer);
        Assert.assertFalse(serializedJsonValueSerializer._forceTypeInformation);
    }

    @Test
    public void testSerialize() throws IOException {
        String expectedResult = "testValue";

        JsonGenerator generatorMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        when(valueSerializerMock.serialize(any(), eq(generatorMock), eq(providerMock))).thenReturn(expectedResult);

        jsonValueSerializer.serialize(testObject, generatorMock, providerMock);

        verify(generatorMock, times(1)).writeString(expectedResult);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        String expectedType = "expectedType";
        String expectedResult = "testValue";

        JsonGenerator generatorMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);
        TypeSerializer typeSerializerMock = mock(TypeSerializer.class);

        when(typeSerializerMock.writeTypePrefixForScalar(any(), eq(generatorMock))).thenReturn(expectedType);
        when(valueSerializerMock.serializeWithType(any(), eq(generatorMock), eq(providerMock), eq(typeSerializerMock))).thenReturn(expectedResult);

        jsonValueSerializer.serializeWithType(testObject, generatorMock, providerMock, typeSerializerMock);

        verify(generatorMock, times(1)).writeString(expectedType);
        verify(generatorMock, times(1)).writeString(expectedResult);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode expectedSchema = JsonNodeFactory.instance.textNode("expectedSchema");

        JsonSerializer<Object> valueSerializerMock = mock(JsonSerializer.class);
        when(valueSerializerMock.getSchema(any(), any())).thenReturn(expectedSchema);

        JsonNode result = jsonValueSerializer.getSchema(mock(SerializerProvider.class), null);

        Assert.assertEquals(expectedSchema, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitorMock = mock(JsonFormatVisitorWrapper.class);

        jsonValueSerializer.acceptJsonFormatVisitor(visitorMock, null);

        verify(visitorMock, times(1)).expectAnyFormat(any());
    }

    @Test
    public void testAcceptJsonFormatVisitorForEnum() throws JsonMappingException {
        JsonFormatVisitorWrapper visitorMock = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHintMock = mock(JavaType.class);
        Class<?> enumTypeMock = mock(Class.class);

        Set<String> enums = new LinkedHashSet<>();
        enums.add("enumValue1");
        enums.add("enumValue2");

        when(accessorMethodMock.getDeclaringClass()).thenReturn(enumTypeMock);
        when(enumTypeMock.getEnumConstants()).thenReturn(new Object[]{new EnumClass().getInstance("enumValue1"), new EnumClass().getInstance("enumValue2")});

        jsonValueSerializer._acceptJsonFormatVisitorForEnum(visitorMock, typeHintMock, enumTypeMock);

        verify(visitorMock, times(1)).enumTypes(eq(enums));
    }

    private static class TestClass {
        @JsonSerialize(using = JsonValueSerializer.class)
        public String getValue() {
            return "testValue";
        }
    }

    private static class EnumClass {
        @JsonValue
        public static Enum<?> getInstance(String name) {
            return Enum.valueOf("EnumClass", name);
        }

        public enum Enum {
            enumValue1,
            enumValue2
        }
    }
}