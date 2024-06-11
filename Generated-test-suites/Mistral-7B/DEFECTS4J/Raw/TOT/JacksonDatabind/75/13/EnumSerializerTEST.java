package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.*;

public class EnumSerializerTest {

    @SuppressWarnings("unchecked")
    private static final EnumSerializer<DaysOfWeek> daysOfWeekSerializer =
            new EnumSerializer<>(EnumValues.constructFromName(DaysOfWeek.class), null);

    private static final ObjectMapper mapper = new ObjectMapper();

    enum DaysOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    static class MyEnumClass {
        @JsonValue
        public DaysOfWeek getDaysOfWeek() {
            return daysOfWeek;
        }

        private DaysOfWeek daysOfWeek;
    }

    @Test
    public void testSerializeEnumUsingString() throws IOException {
        MyEnumClass myEnum = new MyEnumClass();
        myEnum.daysOfWeek = DaysOfWeek.MONDAY;

        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String json = writer.writeValueAsString(myEnum);

        ObjectNode node = mapper.readTree(json);
        assertEquals("\"MONDAY\"", node.path("daysOfWeek").asText());
    }

    @Test
    public void testSerializeEnumUsingIndex() throws IOException {
        ObjectWriter writer = mapper.writer()
                .withSerializationFeature(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false)
                .withDefaultPrettyPrinter();

        MyEnumClass myEnum = new MyEnumClass();
        myEnum.daysOfWeek = DaysOfWeek.MONDAY;

        String json = writer.writeValueAsString(myEnum);
        ArrayNode node = (ArrayNode) mapper.readTree(json);

        assertEquals(0, node.size());
        assertEquals(DaysOfWeek.MONDAY.ordinal(), node.get(0).asInt());
    }

    @Test
    public void testDeserializeEnumUsingString() throws IOException {
        String json = "{\"daysOfWeek\":\"FRIDAY\"}";
        MyEnumClass myEnum = mapper.readValue(json, MyEnumClass.class);

        assertEquals(DaysOfWeek.FRIDAY, myEnum.daysOfWeek);
    }

    @Test
    public void testDeserializeEnumUsingIndex() throws IOException {
        String json = "[0]";
        MyEnumClass myEnum = mapper.readValue(json, MyEnumClass.class);

        assertEquals(DaysOfWeek.MONDAY, myEnum.daysOfWeek);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEnumInvalidValue() throws IOException {
        String json = "{\"daysOfWeek\":\"INVALID_VALUE\"}";
        MyEnumClass myEnum = mapper.readValue(json, MyEnumClass.class);

        fail("Expected JsonMappingException due to invalid value");
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeEnumWithoutEnumValues() throws IOException {
        EnumSerializer<Object> nullSerializer = new EnumSerializer<>(null, null);
        ObjectWriter writer = mapper.writer();

        Object obj = new Object();
        writer.writeValue(nullSerializer, obj);

        fail("Expected JsonMappingException due to missing EnumValues");
    }

    @Test
    public void testCreateContextualSerializer() throws IOException {
        ObjectWriter writer = mapper.writer();
        MyEnumClass myEnum = new MyEnumClass();
        myEnum.daysOfWeek = DaysOfWeek.MONDAY;

        JsonSerializer<?> serializer = mapper.findValueSerializer(MyEnumClass.class, writer.getSerializerProvider());
        assertEquals(daysOfWeekSerializer, serializer);
    }
}