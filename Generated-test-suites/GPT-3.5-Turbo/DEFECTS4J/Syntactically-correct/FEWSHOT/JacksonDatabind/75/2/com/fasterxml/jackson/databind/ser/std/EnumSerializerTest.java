
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class EnumSerializerTest {

    @Test
    public void testSerializeAsIndex() throws JsonProcessingException {
        EnumValues values = new EnumValues(Enum.class, new String[]{"VAL1", "VAL2", "VAL3"});
        EnumSerializer serializer = new EnumSerializer(values, true);

        Enum enumVal = Enum.VAL2;
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        serializer.serialize(enumVal, generator, null);

        generator.flush();
        assertEquals("1", writer.toString());
    }

    @Test
    public void testSerializeAsString() throws JsonProcessingException {
        EnumValues values = new EnumValues(Enum.class, new String[]{"VAL1", "VAL2", "VAL3"});
        EnumSerializer serializer = new EnumSerializer(values, false);

        Enum enumVal = Enum.VAL2;
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        serializer.serialize(enumVal, generator, null);

        generator.flush();
        assertEquals("VAL2", writer.toString());
    }

    @Test
    public void testSerializeUsingToString() throws JsonProcessingException {
        EnumValues values = new EnumValues(Enum.class, new String[]{"VAL1", "VAL2", "VAL3"});
        EnumSerializer serializer = new EnumSerializer(values, null);

        Enum enumVal = Enum.VAL2;
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializationConfig config = new ObjectMapper().getSerializationConfig();

        config = config.with(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        JsonSerializer<?> contextSerializer = serializer.createContextual(config, null);
        contextSerializer.serialize(enumVal, generator, null);

        generator.flush();
        assertEquals("VAL2", writer.toString());
    }

    @Test
    public void testConstruct() {
        EnumSerializer serializer = EnumSerializer.construct(Enum.class, null, null, null);
        assertTrue(serializer instanceof EnumSerializer);
    }
}
