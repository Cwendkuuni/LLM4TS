
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

public class EnumSerializerTest {

    private EnumSerializer enumSerializer;
    private Enum<?> enumInstance;
    private JsonGenerator jsonGeneratorMock;
    private SerializerProvider serializerProviderMock;
    private SerializationConfig serializationConfigMock;
    private BeanDescription beanDescriptionMock;
    private TypeFactory typeFactoryMock;

    private EnumValues enumValues;

    @Before
    public void setUp() {
        enumValues = EnumValues.constructFromName(
                serializationConfigMock, (Class<Enum<?>>) enumInstance.getClass()
        );
        enumSerializer = new EnumSerializer(enumValues, true);

        jsonGeneratorMock = mock(JsonGenerator.class);
        serializerProviderMock = mock(SerializerProvider.class);
        serializationConfigMock = mock(SerializationConfig.class);
        beanDescriptionMock = mock(BeanDescription.class);
        typeFactoryMock = mock(TypeFactory.class);
    }

    @Test
    public void testSerializeAsIndex() throws IOException {
        when(serializerProviderMock.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX))
                .thenReturn(true);
        when(enumInstance.ordinal()).thenReturn(0);

        enumSerializer.serialize(enumInstance, jsonGeneratorMock, serializerProviderMock);

        verify(jsonGeneratorMock).writeNumber(enumInstance.ordinal());
    }

    @Test
    public void testSerializeAsString() throws IOException {
        when(serializerProviderMock.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING))
                .thenReturn(true);
        when(enumInstance.toString()).thenReturn("ENUM_VALUE");

        enumSerializer.serialize(enumInstance, jsonGeneratorMock, serializerProviderMock);

        verify(jsonGeneratorMock).writeString(enumInstance.toString());
    }

}
