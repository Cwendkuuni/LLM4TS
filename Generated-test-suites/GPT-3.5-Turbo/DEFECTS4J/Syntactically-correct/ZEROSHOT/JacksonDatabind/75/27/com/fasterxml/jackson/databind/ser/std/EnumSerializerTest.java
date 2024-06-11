package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EnumSerializerTest {

    private EnumSerializer enumSerializer;
    private EnumValues enumValues;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @Before
    public void setUp() {
        enumValues = mock(EnumValues.class);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        enumSerializer = new EnumSerializer(enumValues, false);
    }

    @Test
    public void testSerializeAsIndex() throws Exception {
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);

        enumSerializer.serialize(Enum.TWO, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(Enum.TWO.ordinal());
    }

    @Test
    public void testSerializeAsString() throws Exception {
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(false);
        when(enumValues.serializedValueFor(Enum.TWO)).thenReturn("TWO");

        enumSerializer.serialize(Enum.TWO, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeString("TWO");
    }
}