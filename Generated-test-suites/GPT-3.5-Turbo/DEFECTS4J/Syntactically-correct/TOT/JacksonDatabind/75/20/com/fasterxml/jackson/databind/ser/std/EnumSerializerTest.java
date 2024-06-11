package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BasicSerializerFactory;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class EnumSerializerTest {

    private JsonGenerator mockJsonGenerator;
    private SerializerProvider mockSerializerProvider;
    private EnumSerializer enumSerializer;

    @Before
    public void setUp() throws Exception {
        mockJsonGenerator = mock(JsonGenerator.class);
        mockSerializerProvider = mock(SerializerProvider.class);

        EnumValues enumValues = EnumValues.constructFromName();
        enumSerializer = new EnumSerializer(enumValues, null);
    }

    @After
    public void tearDown() throws Exception {
        mockJsonGenerator = null;
        mockSerializerProvider = null;
        enumSerializer = null;
    }

    @Test
    public void serialize_WhenSerializeAsIndexEnabled_SerializesEnumOrdinal() throws IOException {
        // Arrange
        Enum<?> enumValue = MyEnum.FIRST;
        boolean serializeAsIndex = true;

        when(mockSerializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);

        // Act
        enumSerializer.serialize(enumValue, mockJsonGenerator, mockSerializerProvider);

        // Assert
        verify(mockJsonGenerator).writeNumber(enumValue.ordinal());
    }

    @Test
    public void serialize_WhenWriteEnumsUsingToStringEnabled_SerializesEnumUsingToString() throws IOException {
        // Arrange
        Enum<?> enumValue = MyEnum.FIRST;
        boolean serializeAsIndex = false;
        boolean writeEnumsUsingToString = true;

        when(mockSerializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(false);
        when(mockSerializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);

        // Act
        enumSerializer.serialize(enumValue, mockJsonGenerator, mockSerializerProvider);

        // Assert
        verify(mockJsonGenerator).writeString(enumValue.toString());
    }

    @Test
    public void serialize_WhenEnumsNotUsingToString_SerializesEnumUsingSerializedValue() throws IOException {
        // Arrange
        Enum<?> enumValue = MyEnum.FIRST;
        boolean serializeAsIndex = false;
        boolean writeEnumsUsingToString = false;

        when(mockSerializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(false);
        when(mockSerializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);

        // Act
        enumSerializer.serialize(enumValue, mockJsonGenerator, mockSerializerProvider);

        // Assert
        verify(mockJsonGenerator).writeString(enumValue.getSerializedValue());
    }

    public enum MyEnum {
        FIRST("first"),
        SECOND("second"),
        THIRD("third");

        private String serializedValue;

        MyEnum(String serializedValue) {
            this.serializedValue = serializedValue;
        }

        public String getSerializedValue() {
            return serializedValue;
        }
    }
}