
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class NumberSerializersTest {

    // Class to test
    private NumberSerializers.ShortSerializer shortSerializer;
    private NumberSerializers.IntegerSerializer integerSerializer;
    private NumberSerializers.IntLikeSerializer intLikeSerializer;
    private NumberSerializers.LongSerializer longSerializer;
    private NumberSerializers.FloatSerializer floatSerializer;
    private NumberSerializers.DoubleSerializer doubleSerializer;

    @Mock
    private JsonGenerator mockJsonGenerator;
    @Mock
    private SerializerProvider mockSerializerProvider;
    @Mock
    private TypeSerializer mockTypeSerializer;
    @Mock
    private BeanProperty mockBeanProperty;
    @Mock
    private JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper;
    @Mock
    private JavaType mockJavaType;
    @Mock
    private JsonIntegerFormatVisitor mockJsonIntegerFormatVisitor;
    @Mock
    private JsonNumberFormatVisitor mockJsonNumberFormatVisitor;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        shortSerializer = new NumberSerializers.ShortSerializer();
        integerSerializer = new NumberSerializers.IntegerSerializer();
        intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        longSerializer = NumberSerializers.LongSerializer.instance;
        floatSerializer = NumberSerializers.FloatSerializer.instance;
        doubleSerializer = NumberSerializers.DoubleSerializer.instance;
    }

    @Test
    public void testShortSerializerSerialize() throws IOException {
        short value = 10;
        shortSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        int value = 100;
        integerSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testIntegerSerializerSerializeWithType() throws IOException {
        int value = 100;
        integerSerializer.serializeWithType(value, mockJsonGenerator, mockSerializerProvider, mockTypeSerializer);
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        byte value = 5;
        intLikeSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testLongSerializerSerialize() throws IOException {
        long value = 1000L;
        longSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testFloatSerializerSerialize() throws IOException {
        float value = 10.5f;
        floatSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        double value = 10.989;
        doubleSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testBaseSchemaWithType() {
        Type typeHint = mock(Type.class);
        JsonSerializer<?> mockJsonSerializer = mock(JsonSerializer.class);
        when(mockJsonFormatVisitorWrapper.expectNumberFormat(typeHint)).thenReturn(mockJsonNumberFormatVisitor);
        when(mockJsonNumberFormatVisitor.numberType(JsonParser.NumberType.INTEGER)).thenReturn(mockJsonNumberFormatVisitor);

        NumberSerializers.Base<?> base = new NumberSerializers.Base<>(String.class, JsonParser.NumberType.INTEGER, "number") {

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                // intentionally left blank
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                // intentionally left blank
            }

            @Override
            public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider,
                    TypeSerializer typeSer) throws IOException {
                // intentionally left blank
            }
        };

        base.addSchemaWithType(mockJsonFormatVisitorWrapper, typeHint, mockJsonSerializer);
        verify(mockJsonNumberFormatVisitor).numberType(JsonParser.NumberType.INTEGER);
    }

    @Test
    public void testBaseSchemaWithMedata() {
        JsonSerializer<?> mockJsonSerializer = mock(JsonSerializer.class);
        when(mockJsonFormatVisitorWrapper.expectNumberFormat(mockJavaType)).thenReturn(mockJsonNumberFormatVisitor);
        when(mockJsonNumberFormatVisitor.numberType(JsonParser.NumberType.INTEGER)).thenReturn(mockJsonNumberFormatVisitor);

        NumberSerializers.Base<?> base = new NumberSerializers.Base<>(String.class, JsonParser.NumberType.INTEGER, "number") {

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                // intentionally left blank
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                // intentionally left blank
            }

            @Override
            public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider,
                    TypeSerializer typeSer) throws IOException {
                // intentionally left blank
            }
        };

        base.addSchemaWithLength(mockJsonFormatVisitorWrapper, mockJavaType, mockJsonSerializer);
        verify(mockJsonNumberFormatVisitor).numberType(JsonParser.NumberType.INTEGER);
    }

    @Test
    public void testBaseGetSchema() {
        Type typeHint = mock(Type.class);
        JsonSerializer<?> mockJsonSerializer = mock(JsonSerializer.class);
        when(mockJsonFormatVisitorWrapper.expectNumberFormat(typeHint)).thenReturn(mockJsonNumberFormatVisitor);
        when(mockJsonNumberFormatVisitor.numberType(JsonParser.NumberType.INTEGER)).thenReturn(mockJsonNumberFormatVisitor);

        NumberSerializers.Base<?> base = new NumberSerializers.Base<>(String.class, JsonParser.NumberType.INTEGER, "number") {

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                // intentionally left blank
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                // intentionally left blank
            }

            @Override
            public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider,
                    TypeSerializer typeSer) throws IOException {
                // intentionally left blank
            }
        };

        base.getSchema(mockSerializerProvider, typeHint);
        verify(mockJsonNumberFormatVisitor).numberType(JsonParser.NumberType.INTEGER);
    }

    @Test
    public void testContextualSerializer() {
        NumberSerializers.Base<?> base = new NumberSerializers.Base<>(String.class, JsonParser.NumberType.INTEGER, "number") {

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                // intentionally left blank
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                // intentionally left blank
            }

            @Override
            public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider,
                    TypeSerializer typeSer) throws IOException {
                // intentionally left blank
            }
        };

        JsonSerializer<?> contextSerializer = base.createContextual(mockSerializerProvider, mockBeanProperty);
        assertEquals(base, contextSerializer);
    }

    @Test
    public void testNumberSerializersAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(8, allDeserializers.size());
    }

}

