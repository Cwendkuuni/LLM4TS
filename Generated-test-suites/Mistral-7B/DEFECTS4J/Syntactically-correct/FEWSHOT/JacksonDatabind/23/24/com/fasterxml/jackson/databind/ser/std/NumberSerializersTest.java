package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.JsonParser.NumberType.INT;
import static com.fasterxml.jackson.core.JsonParser.NumberType.LONG;
import static com.fasterxml.jackson.core.JsonParser.NumberType.FLOAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.*;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer is = new IntegerSerializer();
        assertEquals(Integer.TYPE, is.getParameterizedType());
        assertEquals(INT, is._numberType);
        assertEquals("integer", is._schemaType);
        assertNotNull(is.createContextual(null, null));
    }

    @Test
    public void testIntegerSerializerSerialization() throws IOException {
        Integer value = 42;
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        IntegerSerializer is = new IntegerSerializer();
        is.serialize(value, gen, null);
        assertEquals(42, gen.getOutputContent().intValue());
    }

    @Test
    public void testIntegerSerializerWithTypeSerialization() throws IOException {
        Integer value = 42;
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        TypeSerializer ts = new StandardSerializer(Integer.class).withResolved(new Type[] { Integer.TYPE, JsonType.INT });
        IntegerSerializer is = new IntegerSerializer();
        is.serializeWithType(value, gen, null, ts);
        assertEquals(42, gen.getOutputContent().intValue());
        assertEquals("number", gen.getOutputContentAsString().substring(0, 6));
    }

    @Test
    public void testIntegerSerializerContextual() {
        IntegerSerializer is = new IntegerSerializer();
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JsonFormat jsonFormat = Mockito.mock(JsonFormat.class);
        Mockito.when(jsonFormat.getShape()).thenReturn(JsonFormat.Shape.INT);
        Mockito.when(am.getAnnotation(JsonFormat.class)).thenReturn(jsonFormat);
        assertEquals(ToStringSerializer.instance, is.createContextual(null, am));
    }

    // Repeat the test cases for LongSerializer, IntLikeSerializer, FloatSerializer, and DoubleSerializer
}