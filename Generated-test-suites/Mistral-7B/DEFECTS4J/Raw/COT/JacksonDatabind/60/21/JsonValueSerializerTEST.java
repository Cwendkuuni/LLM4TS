package com.example.jsonvalueserializer;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializerBase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class JsonValueSerializerTest {

    private static final Class<Object> OBJECT_CLASS = Object.class;

    private JsonSerializer<Object> jsonSerializerMock;
    private BeanSerializer beanSerializerMock;

    @Before
    public void setup() {
        jsonSerializerMock = Mockito.mock(JsonSerializer.class);
        beanSerializerMock = Mockito.mock(BeanSerializer.class);
    }

    @Test
    public void testJsonValueSerializer_ConstructorWithValueMethodAndSerializer_SerializesValue() throws Exception {
        Object value = new Object();
        JsonSerializer<Object> serializer = Mockito.mock(JsonSerializer.class);
        StdSerializer<Object> jsonValueSerializer = new JsonValueSerializer(
                Mockito.mock(AnnotatedMethod.class), serializer);

        Object result = jsonValueSerializer.serialize(value, Mockito.mock(JsonGenerator.class), Mockito.mock(SerializerProvider.class));

        Mockito.verify(serializer).serialize(value, Mockito.eq(Mockito.mock(JsonGenerator.class)), Mockito.eq(Mockito.mock(SerializerProvider.class)));
        assertThat(result, is(value));
    }

    @Test
    public void testJsonValueSerializer_ConstructorWithPropertyAndSerializer_SerializesValue() throws Exception {
        Object value = new Object();
        JsonSerializer<Object> serializer = Mockito.mock(JsonSerializer.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        StdSerializer<Object> jsonValueSerializer = new JsonValueSerializer(
                this.jsonSerializerMock, property, serializer, false);

        Object result = jsonValueSerializer.serialize(value, Mockito.mock(JsonGenerator.class), Mockito.mock(SerializerProvider.class));

        Mockito.verify(serializer).serialize(value, Mockito.eq(Mockito.mock(JsonGenerator.class)), Mockito.eq(Mockito.mock(SerializerProvider.class)));
        assertThat(result, is(value));
    }

    @Test
    public void testJsonValueSerializer_CreateContextual_ReturnsSameInstance() {
        StdSerializer<Object> jsonValueSerializer = Mockito.mock(StdSerializer.class);

        StdSerializer<Object> result1 = jsonValueSerializer.createContextual(
                Mockito.mock(SerializerProvider.class), Mockito.mock(BeanProperty.class));
        StdSerializer<Object> result2 = jsonValueSerializer.createContextual(
                Mockito.mock(SerializerProvider.class), Mockito.mock(BeanProperty.class));

        assertThat(result1, is(jsonValueSerializer));
        assertThat(result1, is(result2));
    }

    @Test
    public void testJsonValueSerializer_CreateContextual_ReturnsNewInstanceWithDifferentProperty() throws Exception {
        StdSerializer<Object> jsonValueSerializer = Mockito.mock(StdSerializer.class);
        BeanProperty property1 = Mockito.mock(BeanProperty.class);
        BeanProperty property2 = Mockito.mock(BeanProperty.class);

        StdSerializer<Object> result1 = jsonValueSerializer.createContextual(
                Mockito.mock(SerializerProvider.class), property1);
        StdSerializer<Object> result2 = jsonValueSerializer.createContextual(
                Mockito.mock(SerializerProvider.class), property2);

        assertThat(result1, is(not(result2)));
    }

    @Test
    public void testJsonValueSerializer_SerializeNullValue() throws Exception {
        StdSerializer<Object> jsonValueSerializer = new JsonValueSerializer(
                Mockito.mock(AnnotatedMethod.class), null);

        JsonGenerator jsonGeneratorMock = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProviderMock = Mockito.mock(SerializerProvider.class);

        jsonValueSerializer.serialize(null, jsonGeneratorMock, serializerProviderMock);

        Mockito.verify(jsonGeneratorMock).writeNull();
    }

    @Test
    public void testJsonValueSerializer_SerializeNullValueWithTypeSerializer_WritesTypeId() throws Exception {
        JsonSerializer<Object> jsonSerializerMock = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSerializerMock = Mockito.mock(TypeSerializer.class);
        StdSerializer<Object> jsonValueSerializer = new JsonValueSerializer(
                Mockito.mock(AnnotatedMethod.class), jsonSerializerMock, typeSerializerMock, true);

        Object bean = new Object();
        JsonGenerator jsonGeneratorMock = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProviderMock = Mockito.mock(SerializerProvider.class);

        jsonValueSerializer.serialize(bean, jsonGeneratorMock, serializerProviderMock);

        Mockito.verify(typeSerializerMock).writeTypePrefixForScalar(bean, jsonGeneratorMock);
        Mockito.verify(jsonSerializerMock).serialize(Object.class, jsonGeneratorMock, serializerProviderMock);
        Mockito.verify(typeSerializerMock).writeTypeSuffixForScalar(bean, jsonGeneratorMock);
    }

    @Test
    public void testJsonValueSerializer_GetSchema_ReturnsDefaultSchema() throws Exception {
        StdSerializer<Object> jsonValueSerializer = new JsonValueSerializer(
                Mockito.mock(AnnotatedMethod.class), null);

        SerializerProvider serializerProviderMock = Mockito.mock(SerializerProvider.class);

        JsonSchema schema = jsonValueSerializer.getSchema(serializerProviderMock, OBJECT_CLASS);

        Mockito.verify(serializerProviderMock, Mockito.times(1)).getSchemaProvider();
        Mockito.verify(serializerProviderMock, Mockito.times(1)).getDefaultSchemaProvider();
        assertThat(schema, is(not(nullValue())));
    }

    @Test
    public void testJsonValueSerializer_AcceptJsonFormatVisitor_HandlesEnumTypes() throws Exception {
        Class<MyEnum> enumClass = MyEnum.class;
        StdSerializer<Object> jsonValueSerializer = new JsonValueSerializer(
                Mockito.mock(AnnotatedMethod.class), null);

        JsonFormatVisitorWrapper jsonFormatVisitorWrapperMock = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType javaTypeMock = Mockito.mock(JavaType.class);

        jsonValueSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapperMock, javaTypeMock);

        Mockito.verify(jsonFormatVisitorWrapperMock).expectStringFormat(OBJECT_CLASS);
        Mockito.verify(jsonFormatVisitorWrapperMock).enumTypes(Mockito.any(Set.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonValueSerializer_SerializeThrowsException() throws Exception {
        StdSerializer<Object> jsonValueSerializer = new JsonValueSerializer(
                Mockito.mock(AnnotatedMethod.class), null);

        Object bean = new Object();
        JsonGenerator jsonGeneratorMock = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProviderMock = Mockito.mock(SerializerProvider.class);

        Throwable throwable = new IOException("Test exception");
        when(jsonGeneratorMock.getOutputFactory()).thenThrow(throwable);

        jsonValueSerializer.serialize(bean, jsonGeneratorMock, serializerProviderMock);

        // This should throw the exception, not wrap it
        throw throwable;
    }

    @JsonSerialize(using = JsonValueSerializer.class)
    private static class MyEnum {
        private static final MyEnum VALUE = new MyEnum();

        private MyEnum() {}

        public static MyEnum value() {
            return VALUE;
        }
    }
}