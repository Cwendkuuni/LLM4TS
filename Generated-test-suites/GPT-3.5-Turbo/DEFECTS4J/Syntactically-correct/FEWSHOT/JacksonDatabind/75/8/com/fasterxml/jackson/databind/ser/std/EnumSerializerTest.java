package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BasicSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testConstructor() {
        EnumValues enumValuesMock = mock(EnumValues.class);
        Boolean serializeAsIndexMock = mock(Boolean.class);

        EnumSerializer enumSerializer = new EnumSerializer(enumValuesMock, serializeAsIndexMock);

        assertEquals(enumValuesMock, enumSerializer.getEnumValues());
        assertEquals(serializeAsIndexMock, enumSerializer.serializeAsIndex);
    }

    @Test
    public void testConstruct() {
        Class<?> enumClassMock = mock(Class.class);
        SerializationConfig configMock = mock(SerializationConfig.class);
        BeanDescription beanDescMock = mock(BeanDescription.class);
        JsonFormat.Value formatMock = mock(JsonFormat.Value.class);
        EnumValues enumValuesMock = mock(EnumValues.class);
        Boolean serializeAsIndexMock = mock(Boolean.class);

        when(EnumValues.constructFromName(configMock, (Class<Enum<?>>) enumClassMock)).thenReturn(enumValuesMock);
        when(_isShapeWrittenUsingIndex(enumClassMock, formatMock, true)).thenReturn(serializeAsIndexMock);

        EnumSerializer enumSerializer = EnumSerializer.construct(enumClassMock, configMock, beanDescMock, formatMock);

        assertEquals(enumValuesMock, enumSerializer.getEnumValues());
        assertEquals(serializeAsIndexMock, enumSerializer.serializeAsIndex);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonSerializer.Contextual contextualMock = mock(JsonSerializer.Contextual.class);
        SerializerProvider serializersMock = mock(SerializerProvider.class);
        BeanProperty propertyMock = mock(BeanProperty.class);
        JsonFormat.Value formatMock = mock(JsonFormat.Value.class);
        Boolean serializeAsIndexMock = mock(Boolean.class);

        when(propertyMock.getType().getRawClass()).thenReturn(Enum.class);
        when(findFormatOverrides(serializersMock, propertyMock, propertyMock.getType().getRawClass())).thenReturn(formatMock);
        when(_isShapeWrittenUsingIndex(propertyMock.getType().getRawClass(), formatMock, false)).thenReturn(serializeAsIndexMock);

        JsonSerializer result = new EnumSerializer(mock(EnumValues.class), null).createContextual(serializersMock, propertyMock);

        verify(contextualMock).createContextual(serializersMock, propertyMock);
        assertEquals(EnumSerializer.class, result.getClass());
        assertEquals(serializeAsIndexMock, result.serializeAsIndex);
    }

    @Test
    public void testSerialize_asIndex() throws IOException {
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider serializersMock = mock(SerializerProvider.class);
        EnumSerializer enumSerializer = new EnumSerializer(mock(EnumValues.class), true);

        enumSerializer.serialize(Enum.ONE, genMock, serializersMock);

        verify(genMock).writeNumber(Enum.ONE.ordinal());
    }

    @Test
    public void testSerialize_usingToString() throws IOException {
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider serializersMock = mock(SerializerProvider.class);
        EnumSerializer enumSerializer = new EnumSerializer(mock(EnumValues.class), false);

        when(serializersMock.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);

        enumSerializer.serialize(Enum.ONE, genMock, serializersMock);

        verify(genMock).writeString(Enum.ONE.toString());
    }

    @Test
    public void testSerialize_usingSerializedValue() throws IOException {
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider serializersMock = mock(SerializerProvider.class);
        EnumValues enumValuesMock = mock(EnumValues.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValuesMock, null);

        when(enumValuesMock.serializedValueFor(Enum.ONE)).thenReturn("ONE");

        enumSerializer.serialize(Enum.ONE, genMock, serializersMock);

        verify(genMock).writeString("ONE");
    }

    @Test
    public void testGetSchema_asIndex() throws IOException {
        SerializerProvider providerMock = mock(SerializerProvider.class);
        EnumSerializer enumSerializer = new EnumSerializer(mock(EnumValues.class), true);

        JsonNode result = enumSerializer.getSchema(providerMock, null);

        verify(providerMock).isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        assertEquals("integer", result.get("type").asText());
    }

    @Test
    public void testGetSchema_asEnum() throws IOException {
        SerializerProvider providerMock = mock(SerializerProvider.class);
        JavaType typeMock = mock(JavaType.class);
        EnumValues enumValuesMock = mock(EnumValues.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValuesMock, null);

        when(providerMock.constructType(null)).thenReturn(typeMock);
        when(typeMock.isEnumType()).thenReturn(true);
        when(enumValuesMock.values()).thenReturn(Collections.singletonList(mock(SerializableString.class)));

        JsonNode result = enumSerializer.getSchema(providerMock, new Type());

        verify(providerMock).constructType(new Type());
        verify(enumValuesMock).values();
        assertEquals("string", result.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_asIndex() throws IOException {
        JsonFormatVisitorWrapper visitorMock = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHintMock = mock(JavaType.class);
        SerializerProvider serializersMock = mock(SerializerProvider.class);
        EnumSerializer enumSerializer = new EnumSerializer(mock(EnumValues.class), true);

        enumSerializer.acceptJsonFormatVisitor(visitorMock, typeHintMock);

        verify(visitorMock).getProvider();
        verify(typeHintMock).getRawClass();
        verify(visitorMock).expectIntFormat(typeHintMock);
    }

    @Test
    public void testAcceptJsonFormatVisitor_asString_withToString() throws IOException {
        JsonFormatVisitorWrapper visitorMock = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHintMock = mock(JavaType.class);
        SerializerProvider serializersMock = mock(SerializerProvider.class);
        EnumSerializer enumSerializer = new EnumSerializer(mock(EnumValues.class), false);

        when(serializersMock.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);

        enumSerializer.acceptJsonFormatVisitor(visitorMock, typeHintMock);

        verify(visitorMock).getProvider();
        verify(typeHintMock).getRawClass();
        verify(visitorMock).expectStringFormat(typeHintMock);

        // Verify enum values
        Set<String> expectedEnums = new LinkedHashSet<>();
        for (Enum<?> e : enumSerializer.getEnumValues().enums()) {
            expectedEnums.add(e.toString());
        }
        verify(visitorMock).expectStringFormat(typeHintMock);
    }

    @Test
    public void testAcceptJsonFormatVisitor_asString_withSerializedValue() throws IOException {
        JsonFormatVisitorWrapper visitorMock = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHintMock = mock(JavaType.class);
        SerializerProvider serializersMock = mock(SerializerProvider.class);
        EnumSerializer enumSerializer = new EnumSerializer(mock(EnumValues.class), false);

        when(serializersMock.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);

        enumSerializer.acceptJsonFormatVisitor(visitorMock, typeHintMock);

        verify(visitorMock).getProvider();
        verify(typeHintMock).getRawClass();
        verify(visitorMock).expectStringFormat(typeHintMock);

        // Verify enum values
        Set<String> expectedEnums = new LinkedHashSet<>();
        for (SerializableString value : enumSerializer.getEnumValues().values()) {
            expectedEnums.add(value.getValue());
        }
        verify(visitorMock).expectStringFormat(typeHintMock);
        verify(visitorMock).enumTypes(expectedEnums);
    }

    protected class Enum {
        public static final Enum ONE = new Enum();
    }
}