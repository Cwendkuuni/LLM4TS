package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("serial")
public class NumberSerializersTest {

    @Mock
    private JsonSerializer<?> jsonSerializer;

    @Mock
    private JsonParser.NumberType numberType;

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private TypeSerializer typeSerializer;

    @Mock
    private JavaType typeHint;

    @Mock
    private BeanProperty beanProperty;

    @Mock
    private AnnotatedMember annotatedMember;

    @Mock
    private JsonFormat.Value format;

    @Mock
    private JsonFormatVisitorWrapper visitor;

    @Mock
    private JsonIntegerFormatVisitor integerVisitor;

    @Mock
    private JsonNumberFormatVisitor numberVisitor;

    @Mock
    private LongSerializer longSerializer;

    @Mock
    private IntLikeSerializer intLikeSerializer;

    @Mock
    private ShortSerializer shortSerializer;

    @Mock
    private FloatSerializer floatSerializer;

    @Mock
    private DoubleSerializer doubleSerializer;

    public NumberSerializersTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddAll() {
        // Prepare
        Map<String, JsonSerializer<?>> allDeserializers = mock(Map.class);

        // Run
        NumberSerializers.addAll(allDeserializers);

        // Verify
        verify(allDeserializers, times(1)).put(eq(Integer.class.getName()), any(IntegerSerializer.class));
        verify(allDeserializers, times(1)).put(eq(Integer.TYPE.getName()), any(IntegerSerializer.class));
        verify(allDeserializers, times(1)).put(eq(Long.class.getName()), eq(longSerializer));
        verify(allDeserializers, times(1)).put(eq(Long.TYPE.getName()), eq(longSerializer));
        verify(allDeserializers, times(1)).put(eq(Byte.class.getName()), eq(intLikeSerializer));
        verify(allDeserializers, times(1)).put(eq(Byte.TYPE.getName()), eq(intLikeSerializer));
        verify(allDeserializers, times(1)).put(eq(Short.class.getName()), eq(shortSerializer));
        verify(allDeserializers, times(1)).put(eq(Short.TYPE.getName()), eq(shortSerializer));
        verify(allDeserializers, times(1)).put(eq(Float.class.getName()), eq(floatSerializer));
        verify(allDeserializers, times(1)).put(eq(Float.TYPE.getName()), eq(floatSerializer));
        verify(allDeserializers, times(1)).put(eq(Double.class.getName()), eq(doubleSerializer));
        verify(allDeserializers, times(1)).put(eq(Double.TYPE.getName()), eq(doubleSerializer));
    }

    @Test
    public void testShortSerializer() throws IOException {
        // Prepare
        ShortSerializer shortSerializer = new ShortSerializer();
        Short value = 1;

        // Run
        shortSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Verify
        verify(jsonGenerator, times(1)).writeNumber(value.shortValue());
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        // Prepare
        IntegerSerializer integerSerializer = new IntegerSerializer();
        Integer value = 1;

        // Run
        integerSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Verify
        verify(jsonGenerator, times(1)).writeNumber(value.intValue());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        // Prepare
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        Number value = 1;

        // Run
        intLikeSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Verify
        verify(jsonGenerator, times(1)).writeNumber(value.intValue());
    }

    @Test
    public void testLongSerializer() throws IOException {
        // Prepare
        LongSerializer longSerializer = new LongSerializer();
        Long value = 1L;

        // Run
        longSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Verify
        verify(jsonGenerator, times(1)).writeNumber(value.longValue());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        // Prepare
        FloatSerializer floatSerializer = new FloatSerializer();
        Float value = 1.0f;

        // Run
        floatSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Verify
        verify(jsonGenerator, times(1)).writeNumber(value.floatValue());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        // Prepare
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        Double value = 1.0;

        // Run
        doubleSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Verify
        verify(jsonGenerator, times(1)).writeNumber(value.doubleValue());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws IOException {
        // Prepare
        NumberSerializers.Base<Object> base = new NumberSerializers.IntegerSerializer();
        when(visitor.expectIntegerFormat(typeHint)).thenReturn(integerVisitor);

        // Run
        base.acceptJsonFormatVisitor(visitor, typeHint);

        // Verify
        verify(integerVisitor, times(1)).numberType(numberType);
    }

    @Test
    public void testCreateContextualWithStringShape() throws JsonMappingException {
        // Prepare
        NumberSerializers.Base<Object> base = new NumberSerializers.IntegerSerializer();
        when(beanProperty.getMember()).thenReturn(annotatedMember);
        when(prov.getAnnotationIntrospector().findFormat(annotatedMember)).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);

        // Run
        JsonSerializer<?> result = base.createContextual(prov, beanProperty);

        // Verify
        assertEquals(ToStringSerializer.instance, result);
    }

    @Test
    public void testCreateContextualWithOtherShape() throws JsonMappingException {
        // Prepare
        NumberSerializers.Base<Object> base = new NumberSerializers.IntegerSerializer();
        when(beanProperty.getMember()).thenReturn(annotatedMember);
        when(prov.getAnnotationIntrospector().findFormat(annotatedMember)).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.ARRAY);

        // Run
        JsonSerializer<?> result = base.createContextual(prov, beanProperty);

        // Verify
        assertEquals(base, result);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        // Prepare
        NumberSerializers.Base<Object> base = new NumberSerializers.IntegerSerializer();
        Integer value = 1;

        // Run
        base.serializeWithType(value, jsonGenerator, serializerProvider, typeSerializer);

        // Verify
        verify(jsonGenerator, times(1)).serialize(value);
    }
}