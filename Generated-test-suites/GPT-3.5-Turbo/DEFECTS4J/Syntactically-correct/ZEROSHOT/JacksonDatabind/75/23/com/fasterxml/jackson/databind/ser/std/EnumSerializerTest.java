package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import org.mockito.Mockito;

public class EnumSerializerTest {
    @Test
    public void testCreateContextual() throws JsonMappingException {
        // Create a SerializerProvider
        SerializerProvider serializerProvider = new SerializerProvider();

        // Create a BeanDescription
        BeanDescription beanDescription = Mockito.mock(BeanDescription.class);

        // Create a JsonFormat.Value
        JsonFormat.Value format = Mockito.mock(JsonFormat.Value.class);

        // Create an EnumSerializer
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, (Class<Enum<?>>) DummyEnum.class), false);

        enumSerializer.createContextual(serializerProvider, beanDescription, format);
    }
}