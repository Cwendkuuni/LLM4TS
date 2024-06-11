package com.example; // replace with your test package

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JsonValueSerializerTest {

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private TypeSerializer typeSerializer;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private JsonValueSerializer jsonValueSerializer;

    @Test
    public void testJsonValueSerializer_serializesValueMethodCall() throws Exception {
        Class<MyClass> clazz = MyClass.class;
        Method valueMethod = MyClass.class.getMethod("getValueMethod");
        Object bean = new MyClass();
        Object value = valueMethod.invoke(bean);

        when(serializerProvider.findValueSerializer(eq(value.getClass()), any(BeanProperty.class)))
                .thenReturn(createMock(JsonSerializer.class));

        jsonValueSerializer.serialize(bean, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeObject(value);
    }

    @Test
    public void testJsonValueSerializer_handlesNullValue() throws Exception {
        Class<MyClass> clazz = MyClass.class;
        Method valueMethod = MyClass.class.getMethod("getValueMethod");

        jsonValueSerializer.serialize(new MyClass(), jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNull();
    }

    @Test
    public void testJsonValueSerializer_handlesException() throws Exception {
        Class<MyClass> clazz = MyClass.class;
        Method valueMethod = MyClass.class.getMethod("getValueMethod");

        doThrow(new RuntimeException("Test Exception"))
                .when(valueMethod).invoke(any(Object.class));

        jsonValueSerializer.serialize(new MyClass(), jsonGenerator, serializerProvider);

        verify(jsonGenerator, never()).writeObject(any());
        verifyZeroInteractions(jsonGenerator, serializerProvider);
    }

    @Test
    public void testJsonValueSerializer_serializesWithType() throws Exception {
        Class<MyClass> clazz = MyClass.class;
        Method valueMethod = MyClass.class.getMethod("getValueMethod");
        Object bean = new MyClass();
        Object value = valueMethod.invoke(bean);

        JsonSerializer<Object> jsonSerializer = createMock(JsonSerializer.class);
        when(serializerProvider.findTypedValueSerializer(eq(value.getClass()), true, any(BeanProperty.class)))
                .thenReturn(jsonSerializer);

        when(jsonSerializer.serializeWithType(any(Object.class), any(JsonGenerator.class), any(SerializerProvider.class), eq(typeSerializer)))
                .thenAnswer(invocation -> {
                    JsonGenerator gen = invocation.getArgument(1);
                    TypeSerializer typeSerializer = invocation.getArgument(3);
                    BeanProperty property = invocation.getArgument(2);

                    gen.writeStartObject();
                    gen.writeFieldName("typeId");
                    typeSerializer.writeTypePrefixForScalar(bean, gen);
                    gen.writeFieldName("value");
                    jsonSerializer.serialize(value, gen, serializerProvider);
                    gen.writeEndObject();

                    return null;
                });

        jsonValueSerializer.serializeWithType(bean, jsonGenerator, serializerProvider, typeSerializer);

        verify(jsonGenerator, times(1)).writeStartObject();
        verify(jsonGenerator, times(1)).writeFieldName("typeId");
        verify(jsonGenerator, times(1)).writeFieldName("value");
    }

    @Test
    public void testJsonValueSerializer_getsSchema() throws Exception {
        Class<MyClass> clazz = MyClass.class;
        Method valueMethod = MyClass.class.getMethod("getValueMethod");

        when(objectMapper.getFactory().findValueSerializer(eq(valueMethod.getReturnType()), any(BeanProperty.class)))
                .thenReturn(createMock(JsonSerializer.class));

        JsonSchema schema = jsonValueSerializer.getSchema(serializerProvider, null);

        assertEquals(JsonSchema.getDefaultSchemaNode(), schema);
    }

    @Test
    public void testJsonValueSerializer_acceptsJsonFormatVisitor() throws Exception {
        Class<MyEnum> clazz = MyEnum.class;
        Method valueMethod = MyEnum.class.getMethod("getValueMethod");

        JsonStringFormatVisitor jsonStringFormatVisitor = mock(JsonStringFormatVisitor.class);
        when(serializerProvider.getProvider().isEnabled(eq(MapperFeature.USE_STATIC_TYPING)))
                .thenReturn(true);

        jsonValueSerializer.acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), clazz);

        verify(jsonStringFormatVisitor).enumTypes(any(Set.class));
    }

    @Test
    public void testJsonValueSerializer_serializesEnumWithValueMethod() throws Exception {
        Class<MyEnum> clazz = MyEnum.class;

        JsonStringFormatVisitor jsonStringFormatVisitor = mock(JsonStringFormatVisitor.class);
        Set<String> expectedEnumNames = EnumSet.allOf(clazz).stream()
                .map(e -> String.valueOf(e.getValueMethod().invoke(e)))
                .collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);

        when(serializerProvider.getProvider().isEnabled(eq(MapperFeature.USE_STATIC_TYPING)))
                .thenReturn(true);

        jsonValueSerializer.acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), clazz);

        verify(jsonStringFormatVisitor, times(1)).enumValues(eq(expectedEnumNames.toArray(new String[0])));
    }

    @Test
    public void testJsonValueSerializer_doesNotSerializesEnumWithoutValueMethod() throws Exception {
        Class<MyEnum> clazz = MyEnum.class;

        JsonStringFormatVisitor jsonStringFormatVisitor = mock(JsonStringFormatVisitor.class);
        Set<String> expectedEnumNames = EnumSet.allOf(clazz).stream()
                .map(e -> e.name())
                .collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);

        jsonValueSerializer.acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), clazz);

        verify(jsonStringFormatVisitor, never()).enumValues(any());
        verify(jsonStringFormatVisitor, times(1)).expectAnyFormat(clazz);
    }

    private JsonSerializer<Object> createMock(Class<JsonSerializer<Object>> clazz) {
        return mock(clazz, RETURNS_DEEP_STUBS);
    }

    private static class MyClass {
        public static Object getValueMethod(Object obj) {
            return obj;
        }
    }

    private static class MyEnum {
        public String getValueMethod() {
            return name();
        }

        public enum TestEnum {
            VALUE_1, VALUE_2, VALUE_3;
        }
    }
}