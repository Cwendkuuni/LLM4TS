package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnumSerializerTest {

    @Mock
    private EnumValues enumValues;

    @Mock
    private JsonSerializer<?> jsonSerializer;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private SerializationConfig serializationConfig;

    @Test
    public void testGetEnumValues() {
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);

        EnumValues actualEnumValues = enumSerializer.getEnumValues();

        assertSame(enumValues, actualEnumValues);
    }

    @Test
    public void testSerializeAsIndex_WhenSerializeAsIndexSetToTrueAndSerializationEnabledWithWriteEnumsUsingIndexFlag() {
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        Mockito.when(serializationConfig.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);

        boolean shouldSerializeAsIndex = enumSerializer.serializeAsIndex(serializationConfig);

        assertTrue(shouldSerializeAsIndex);
    }

    @Test
    public void testSerializeAsIndex_WhenSerializeAsIndexSetToTrueAndSerializationDisabledWithWriteEnumsUsingIndexFlag() {
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        Mockito.when(serializationConfig.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(false);

        boolean shouldSerializeAsIndex = enumSerializer.serializeAsIndex(serializationConfig);

        assertTrue(shouldSerializeAsIndex);
    }

    @Test
    public void testSerializeAsIndex_WhenSerializeAsIndexSetToFalseAndSerializationEnabledWithWriteEnumsUsingIndexFlag() {
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        Mockito.when(serializationConfig.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);

        boolean shouldSerializeAsIndex = enumSerializer.serializeAsIndex(serializationConfig);

        assertFalse(shouldSerializeAsIndex);
    }

    @Test
    public void testSerializeAsIndex_WhenSerializeAsIndexSetToFalseAndSerializationDisabledWithWriteEnumsUsingIndexFlag() {
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        Mockito.when(serializationConfig.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(false);

        boolean shouldSerializeAsIndex = enumSerializer.serializeAsIndex(serializationConfig);

        assertFalse(shouldSerializeAsIndex);
    }

    @Test
    public void testSerialize_WhenShouldSerializeAsIndex() throws Exception {
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        Enum<?> enumValue = Enum.ONE;

        enumSerializer.serialize(enumValue, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeNumber(enumValue.ordinal());
    }

    @Test
    public void testSerialize_WhenShouldNotSerializeAsIndex_AndEnumsAreWrittenUsingToString() throws Exception {
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        Mockito.when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        Enum<?> enumValue = Enum.ONE;

        enumSerializer.serialize(enumValue, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeString(enumValue.toString());
    }

    @Test
    public void testSerialize_WhenShouldNotSerializeAsIndex_AndEnumsAreWrittenUsingSerializedValue() throws Exception {
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        Enum<?> enumValue = Enum.ONE;
        Mockito.when(enumValues.serializedValueFor(enumValue)).thenReturn("VALUE");

        enumSerializer.serialize(enumValue, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeString("VALUE");
    }

    private enum Enum {
        ONE
    }
}