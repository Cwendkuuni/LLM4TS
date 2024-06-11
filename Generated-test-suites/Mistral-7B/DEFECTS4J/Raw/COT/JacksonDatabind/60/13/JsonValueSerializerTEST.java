package com.example; // replace with your test package

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonValueSerializerTest {

    private JsonValueSerializer serializer;
    private AnnotatedMethod accessorMethod;
    private Object objectToSerialize;
    private JsonSerializer<Object> valueSerializer;

    @Before
    public void setup() throws Exception {
        accessorMethod = mock(AnnotatedMethod.class);
        when(accessorMethod.getValue(any())).thenReturn("testValue");
        valueSerializer = mock(JsonSerializer.class);

        Type returnType = mock(Type.class);
        Type serializedType = mock(Type.class);
        when(accessorMethod.getType()).thenReturn(returnType);
        when(returnType.getRawClass()).thenReturn(String.class);

        serializer = new JsonValueSerializer(accessorMethod, valueSerializer);
        objectToSerialize = new Object();
    }

    @Test
    public void testSerializeObjectWithValueSerializer() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(objectToSerialize, generator, provider);

        verify(valueSerializer).serialize("testValue", generator, provider);
    }

    @Test
    public void testSerializeObjectWithNullValue() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(accessorMethod.getValue(any())).thenReturn(null);

        serializer.serialize(objectToSerialize, generator, provider);

        provider.defaultSerializeNull(generator);
        verifyZeroInteractions(valueSerializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeObjectWithException() throws IOException, InvocationTargetException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(accessorMethod.getValue(any())).thenThrow(new RuntimeException("Test Exception"));

        serializer.serialize(objectToSerialize, generator, provider);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        serializer.serializeWithType(objectToSerialize, generator, provider, typeSerializer);

        typeSerializer.writeTypePrefixForScalar(objectToSerialize, generator);
        verify(valueSerializer).serialize("testValue", generator, provider);
        typeSerializer.writeTypeSuffixForScalar(objectToSerialize, generator);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType javaType = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, javaType);

        verify(valueSerializer).acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testAcceptJsonFormatVisitorForEnum() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType javaType = mock(JavaType.class);
        Class<?> enumType = mock(Class.class);

        when(javaType.getRawClass()).thenReturn(enumType);

        serializer._acceptJsonFormatVisitorForEnum(visitor, javaType, enumType);

        JsonStringFormatVisitor stringVisitor = visitor.expectStringFormat(javaType);
        Set<String> enums = new LinkedHashSet<String>();
        enums.add("EnumValue1");
        enums.add("EnumValue2");
        stringVisitor.enumTypes(enums);
    }

    @Test
    public void testIsNaturalTypeWithStdHandling() {
        assertThat(serializer.isNaturalTypeWithStdHandling(Integer.TYPE, valueSerializer), is(true));
        assertThat(serializer.isNaturalTypeWithStdHandling(String.class, valueSerializer), is(true));
        assertThat(serializer.isNaturalTypeWithStdHandling(Double.TYPE, valueSerializer), is(true));
        assertThat(serializer.isNaturalTypeWithStdHandling(Object.class, valueSerializer), is(false));
    }
}