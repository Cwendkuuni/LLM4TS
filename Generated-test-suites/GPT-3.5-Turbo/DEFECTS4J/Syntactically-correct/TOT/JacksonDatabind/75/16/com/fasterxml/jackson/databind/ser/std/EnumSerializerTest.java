
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumSerializerTest {
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        JsonFactory jsonFactory = new JsonFactory();
        mapper = new ObjectMapper(jsonFactory);
        mapper.enableDefaultTyping(DefaultTyping.NON_FINAL);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);
    }

    @Test
    public void testSerializeEnumAsIndex() throws Exception {
        EnumSerializer enumSerializer = new EnumSerializer(new EnumValues(EnumClass.class));
        EnumClass enumClass = EnumClass.VALUE1;
        String result = mapper.writeValueAsString(enumClass);
        assertEquals("0", result);
    }

    @Test
    public void testSerializeEnumToString() throws Exception {
        EnumSerializer enumSerializer = new EnumSerializer(new EnumValues(EnumClass.class));
        EnumClass enumClass = EnumClass.VALUE2;
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        String result = mapper.writeValueAsString(enumClass);
        assertEquals("\"VALUE2\"", result);
    }

    @Test
    public void testSerializeEnumWithDifferentNaming() throws Exception {
        EnumSerializer enumSerializer = new EnumSerializer(new EnumValues(EnumClass.class));
        EnumClassWithCustomNaming enumClass = EnumClassWithCustomNaming.VALUE1;
        HashMap<String, String> enumPresentation = new HashMap<>();
        enumPresentation.put("VALUE1", "custom_name_value1");
        enumPresentation.put("VALUE2", "custom_name_value2");
        EnumValues enumValues = new EnumValues(EnumClassWithCustomNaming.class, enumPresentation);
        EnumSerializer enumSerializerWithCustomNaming = new EnumSerializer(enumValues);
  
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        String result = mapper.writeValueAsString(enumClass);
        assertEquals("\"custom_name_value1\"", result);
    }

    @Test
    public void testDeserializeEnumAsIndex() throws Exception {
        TestEnum expectedEnum = TestEnum.ONE;
        String enumString = "1";
        TestEnum result = mapper.readValue(enumString, TestEnum.class);
        assertEquals(expectedEnum, result);
    }

    @Test
    public void testDeserializeEnumAsString() throws Exception {
        TestEnum expectedEnum = TestEnum.TWO;
        String enumString = "\"TWO\"";
        TestEnum result = mapper.readValue(enumString, TestEnum.class);
        assertEquals(expectedEnum, result);
    }

    @Test(expected = Exception.class)
    public void testDeserializeInvalidEnumValue() throws Exception {
        String enumString = "\"INVALID\"";
        TestEnum result = mapper.readValue(enumString, TestEnum.class);
    }

    @Test
    public void testSerializeEnumWithSchema() throws Exception {
        EnumSerializer enumSerializer = new EnumSerializer(new EnumValues(EnumClass.class));
        EnumClass enumValue = EnumClass.VALUE1;
        String enumSchema = "{\"type\":\"integer\"}";
        String result = mapper.writeValueAsString(enumSerializer.getSchema(null, null));
        assertEquals(enumSchema, result);
    }

    @Test(expected = Exception.class)
    public void testAcceptJsonFormatVisitor() throws Exception {
        EnumSerializer enumSerializer = new EnumSerializer(new EnumValues(EnumClass.class));
        ObjectMapper mapper = new ObjectMapper();
        enumSerializer.acceptJsonFormatVisitor(null, null);
    }

    private enum TestEnum {
        ONE,
        TWO
    }

    private enum EnumClass {
        VALUE1,
        VALUE2
    }

    private enum EnumClassWithCustomNaming {
        VALUE1,
        VALUE2
    }
}

