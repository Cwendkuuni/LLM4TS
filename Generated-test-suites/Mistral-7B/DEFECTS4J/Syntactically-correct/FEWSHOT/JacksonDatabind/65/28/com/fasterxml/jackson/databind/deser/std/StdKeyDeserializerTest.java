package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Test class for StdKeyDeserializer.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testForType() {
        // Test forType method with various input classes
        Class<?> inputClass1 = String.class;
        StdKeyDeserializer serializer1 = StdKeyDeserializer.forType(inputClass1);
        assertEquals(StdKeyDeserializer.TYPE_STRING, serializer1._kind);

        Class<?> inputClass2 = UUID.class;
        StdKeyDeserializer serializer2 = StdKeyDeserializer.forType(inputClass2);
        assertEquals(StdKeyDeserializer.TYPE_UUID, serializer2._kind);

        Class<?> inputClass3 = Integer.class;
        StdKeyDeserializer serializer3 = StdKeyDeserializer.forType(inputClass3);
        assertEquals(StdKeyDeserializer.TYPE_INT, serializer3._kind);

        Class<?> inputClass4 = Date.class;
        StdKeyDeserializer serializer4 = StdKeyDeserializer.forType(inputClass4);
        assertEquals(StdKeyDeserializer.TYPE_DATE, serializer4._kind);

        Class<?> inputClass5 = Calendar.class;
        StdKeyDeserializer serializer5 = StdKeyDeserializer.forType(inputClass5);
        assertEquals(StdKeyDeserializer.TYPE_CALENDAR, serializer5._kind);

        Class<?> inputClass6 = Boolean.class;
        StdKeyDeserializer serializer6 = StdKeyDeserializer.forType(inputClass6);
        assertEquals(StdKeyDeserializer.TYPE_BOOLEAN, serializer6._kind);

        Class<?> inputClass7 = Byte.class;
        StdKeyDeserializer serializer7 = StdKeyDeserializer.forType(inputClass7);
        assertEquals(StdKeyDeserializer.TYPE_BYTE, serializer7._kind);

        Class<?> inputClass8 = Character.class;
        StdKeyDeserializer serializer8 = StdKeyDeserializer.forType(inputClass8);
        assertEquals(StdKeyDeserializer.TYPE_CHAR, serializer8._kind);

        Class<?> inputClass9 = Short.class;
        StdKeyDeserializer serializer9 = StdKeyDeserializer.forType(inputClass9);
        assertEquals(StdKeyDeserializer.TYPE_SHORT, serializer9._kind);

        Class<?> inputClass10 = Float.class;
        StdKeyDeserializer serializer10 = StdKeyDeserializer.forType(inputClass10);
        assertEquals(StdKeyDeserializer.TYPE_FLOAT, serializer10._kind);

        Class<?> inputClass11 = Double.class;
        StdKeyDeserializer serializer11 = StdKeyDeserializer.forType(inputClass11);
        assertEquals(StdKeyDeserializer.TYPE_DOUBLE, serializer11._kind);

        Class<?> inputClass12 = URI.class;
        StdKeyDeserializer serializer12 = StdKeyDeserializer.forType(inputClass12);
        assertEquals(StdKeyDeserializer.TYPE_URI, serializer12._kind);

        Class<?> inputClass13 = URL.class;
        StdKeyDeserializer serializer13 = StdKeyDeserializer.forType(inputClass13);
        assertEquals(StdKeyDeserializer.TYPE_URL, serializer13._kind);

        Class<?> inputClass14 = Locale.class;
        StdKeyDeserializer serializer14 = StdKeyDeserializer.forType(inputClass14);
        assertEquals(StdKeyDeserializer.TYPE_LOCALE, serializer14._kind);

        Class<?> inputClass15 = Currency.class;
        StdKeyDeserializer serializer15 = StdKeyDeserializer.forType(inputClass15);
        assertEquals(StdKeyDeserializer.TYPE_CURRENCY, serializer15._kind);

        // Test for invalid input class
        Class<?> inputClass16 = Object.class;
        StdKeyDeserializer serializer16 = StdKeyDeserializer.forType(inputClass16);
        assertNull(serializer16);
    }

    @Test
    public void testDeserializeKey() throws IOException, JsonProcessingException {
        // Test with StdKeyDeserializer instances for various types

        Class<?> keyType1 = String.class;
        StdKeyDeserializer serializer1 = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, keyType1);
        String inputKey1 = "testKey";
        Object output1 = serializer1.deserializeKey(inputKey1, null);
        assertEquals(inputKey1, output1);

        Class<?> keyType2 = UUID.class;
        StdKeyDeserializer serializer2 = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, keyType2);
        String inputKey2 = "00000000-0000-0000-0000-000000000000";
        Object output2 = serializer2.deserializeKey(inputKey2, null);
        assertTrue(output2 instanceof UUID);
        UUID uuidOutput2 = (UUID) output2;
        assertEquals(inputKey2, uuidOutput2.toString());

        Class<?> keyType3 = Integer.class;
        StdKeyDeserializer serializer3 = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, keyType3);
        String inputKey3 = "123";
        Object output3 = serializer3.deserializeKey(inputKey3, null);
        assertEquals(Integer.parseInt(inputKey3), output3);

        Class<?> keyType4 = Date.class;
        StdKeyDeserializer serializer4 = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, keyType4);
        String inputKey4 = "2023-03-23";
        Object output4 = serializer4.deserializeKey(inputKey4, null);
        assertTrue(output4 instanceof java.util.Date);
        java.util.Date dateOutput4 = (java.util.Date) output4;
        assertEquals(inputKey4, dateOutput4.toString());

        Class<?> keyType5 = Calendar.class;
        StdKeyDeserializer serializer5 = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, keyType5);
        String inputKey5 = "2023-03-23";
        Object output5 = serializer5.deserializeKey(inputKey5, null);
        assertTrue(output5 instanceof java.util.Calendar);
        java.util.Calendar calendarOutput5 = (java.util.Calendar) output5;
        assertEquals(java.util.Calendar.getInstance().getTime(), calendarOutput5.getTime());

        Class<?> keyType6 = Boolean.class;
        StdKeyDeserializer serializer6 = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, keyType6);
        String inputKey6_true = "true";
        Object output6_true = serializer6.deserializeKey(inputKey6_true, null);
        assertTrue((boolean) output6_true);

        String inputKey6_false = "false";
        Object output6_false = serializer6.deserializeKey(inputKey6_false, null);
        assertFalse((boolean) output6_false);

        Class<?> keyType7 = Byte.class;
        StdKeyDeserializer serializer7 = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, keyType7);
        String inputKey7 = "10";
        Object output7 = serializer7.deserializeKey(inputKey7, null);
        assertEquals((byte) Byte.parseByte(inputKey7), output7);

        Class<?> keyType8 = Character.class;
        StdKeyDeserializer serializer8 = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, keyType8);
        String inputKey8 = "A";
        Object output8 = serializer8.deserializeKey(inputKey8, null);
        assertEquals(inputKey8.charAt(0), output8);

        Class<?> keyType9 = Short.class;
        StdKeyDeserializer serializer9 = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, keyType9);
        String inputKey9 = "127";
        Object output9 = serializer9.deserializeKey(inputKey9, null);
        assertEquals((short) Short.parseShort(inputKey9), output9);

        Class<?> keyType10 = Float.class;
        StdKeyDeserializer serializer10 = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, keyType10);
        String inputKey10 = "123.45";
        Object output10 = serializer10.deserializeKey(inputKey10, null);
        assertEquals(Float.parseFloat(inputKey10), output10, 0.001f);

        Class<?> keyType11 = Double.class;
        StdKeyDeserializer serializer11 = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, keyType11);
        String inputKey11 = "123.45";
        Object output11 = serializer11.deserializeKey(inputKey11, null);
        assertEquals(Double.parseDouble(inputKey11), output11, 0.001);

        Class<?> keyType12 = URI.class;
        StdKeyDeserializer serializer12 = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, keyType12);
        String inputKey12 = "http://example.com";
        Object output12 = serializer12.deserializeKey(inputKey12, null);
        assertTrue(output12 instanceof URI);

        Class<?> keyType13 = URL.class;
        StdKeyDeserializer serializer13 = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, keyType13);
        String inputKey13 = "http://example.com";
        Object output13 = serializer13.deserializeKey(inputKey13, null);
        assertTrue(output13 instanceof URL);

        Class<?> keyType14 = Locale.class;
        StdKeyDeserializer serializer14 = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, keyType14);
        String inputKey14 = "en_US";
        Object output14 = serializer14.deserializeKey(inputKey14, null);
        assertTrue(output14 instanceof Locale);

        Class<?> keyType15 = Currency.class;
        StdKeyDeserializer serializer15 = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, keyType15);
        String inputKey15 = "USD";
        Object output15 = serializer15.deserializeKey(inputKey15, null);
        assertTrue(output15 instanceof Currency);

        // Test with invalid key
        String invalidInputKey = null;
        StdKeyDeserializer serializerInvalid = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        Object outputInvalid = serializerInvalid.deserializeKey(invalidInputKey, null);
        assertNull(outputInvalid);
    }
}