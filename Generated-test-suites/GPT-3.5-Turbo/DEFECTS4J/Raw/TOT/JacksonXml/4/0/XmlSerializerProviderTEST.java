package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.JsonParserDelegate;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;
    private JsonGenerator jsonGenerator;
    private QuotedNamesPropertyNamingStrategy propertyNamingStrategy;

    @Before
    public void setUp() {
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        xmlSerializerProvider = new XmlSerializerProvider(rootNames);
        jsonGenerator = mock(JsonGenerator.class);
        propertyNamingStrategy = new QuotedNamesPropertyNamingStrategy();
    }

    @Test
    public void testSerializeValue_NullObject_SerializesNullObject() throws IOException {
        // Arrange
        when(jsonGenerator.getCodec()).thenReturn(mock(ObjectCodec.class));
        when(jsonGenerator.getCharacterEscapes()).thenReturn(mock(CharacterEscapes.class));
        when(jsonGenerator.getOutputContext()).thenReturn(mock(JsonGenerator.class));
        when(jsonGenerator.getSerializerProvider()).thenReturn(xmlSerializerProvider);

        // Act
        xmlSerializerProvider.serializeValue(jsonGenerator, null);

        // Assert
        verify(jsonGenerator, times(1)).writeNumber(anyInt());
    }

    @Test(expected = IOException.class)
    public void testSerializeValue_ExceptionInSerializeValue_ThrowsIOException() throws IOException {
        // Arrange
        when(jsonGenerator.getCodec()).thenThrow(new IOException());
        when(jsonGenerator.getCharacterEscapes()).thenReturn(mock(CharacterEscapes.class));
        when(jsonGenerator.getOutputContext()).thenReturn(mock(JsonGenerator.class));
        when(jsonGenerator.getSerializerProvider()).thenReturn(xmlSerializerProvider);

        // Act
        xmlSerializerProvider.serializeValue(jsonGenerator, null);

        // Assert
        // IOException should be thrown
    }

    @Test
    public void testSerializeValue_WithToXmlGenerator_SerializesValue() throws IOException {
        // Arrange
        QName rootName = new QName("root");
        
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        jsonGenerator = toXmlGenerator;

        when(toXmlGenerator.getCodec()).thenReturn(mock(ObjectCodec.class));
        when(toXmlGenerator.getCharacterEscapes()).thenReturn(mock(CharacterEscapes.class));
        when(toXmlGenerator.getOutputContext()).thenReturn(mock(JsonGenerator.class));
        when(toXmlGenerator.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        when(toXmlGenerator.setNextNameIfMissing(rootName)).thenReturn(true);

        // Act
        xmlSerializerProvider.serializeValue(jsonGenerator, null);

        // Assert
        verify(toXmlGenerator, times(1)).writeStartObject();
        verify(jsonGenerator, times(1)).writeEndObject();
    }

    @Test
    public void testSerializeValue_ValueInRootArray_SerializesValueInRootArray() throws IOException {
        // Arrange
        QName rootName = new QName("root");

        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        jsonGenerator = toXmlGenerator;

        when(toXmlGenerator.getCodec()).thenReturn(mock(ObjectCodec.class));
        when(toXmlGenerator.getCharacterEscapes()).thenReturn(mock(CharacterEscapes.class));
        when(toXmlGenerator.getOutputContext()).thenReturn(mock(JsonGenerator.class));
        when(toXmlGenerator.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        when(toXmlGenerator.setNextNameIfMissing(rootName)).thenReturn(true);
        when(toXmlGenerator.inRoot()).thenReturn(true);
        when(TypeUtil.isIndexedType(any(Class.class))).thenReturn(true);

        // Act
        xmlSerializerProvider.serializeValue(jsonGenerator, null);

        // Assert
        verify(toXmlGenerator, times(1)).writeStartObject();
        verify(jsonGenerator, times(1)).writeEndObject();
    }

    @Test(expected = IOException.class)
    public void testSerializeValue_ExceptionInSuperclassSerialize_ThrowsIOException() throws IOException {
        // Arrange
        when(jsonGenerator.getCodec()).thenReturn(mock(ObjectCodec.class));
        when(jsonGenerator.getCharacterEscapes()).thenReturn(mock(CharacterEscapes.class));
        when(jsonGenerator.getOutputContext()).thenReturn(mock(JsonGenerator.class));
        when(jsonGenerator.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        when(TypeUtil.isIndexedType(any(Class.class))).thenReturn(true);
        when(xmlSerializerProvider.findTypedValueSerializer(any(Class.class), anyBoolean(), any(BeanProperty.class))).thenThrow(new IOException());

        // Act
        xmlSerializerProvider.serializeValue(jsonGenerator, null);

        // Assert
        // IOException should be thrown
    }

    @Test
    public void test_serializeValue_JsonGeneratorObjectJavaType_CallsSuperclassSerialize() throws IOException {
        // Arrange
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        jsonGenerator = toXmlGenerator;

        when(toXmlGenerator.getCodec()).thenReturn(mock(ObjectCodec.class));
        when(toXmlGenerator.getCharacterEscapes()).thenReturn(mock(CharacterEscapes.class));
        when(toXmlGenerator.getOutputContext()).thenReturn(mock(JsonGenerator.class));
        when(toXmlGenerator.getSerializerProvider()).thenReturn(xmlSerializerProvider);

        // Act
        xmlSerializerProvider.serializeValue(jsonGenerator, null, null);

        // Assert
        verify(jsonGenerator, times(1)).writeNumber(anyInt());
    }

    @Test(expected = IOException.class)
    public void test_serializeValue_JsonGeneratorObjectJavaType_ExceptionInSerialize_CallsSuperclassSerialize() throws IOException {
        // Arrange
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        jsonGenerator = toXmlGenerator;

        when(toXmlGenerator.getCodec()).thenReturn(mock(ObjectCodec.class));
        when(toXmlGenerator.getCharacterEscapes()).thenReturn(mock(CharacterEscapes.class));
        when(toXmlGenerator.getOutputContext()).thenReturn(mock(JsonGenerator.class));
        when(toXmlGenerator.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        doThrow(new IOException()).when(xmlSerializerProvider).findTypedValueSerializer(any(Class.class), anyBoolean(), any(BeanProperty.class));

        // Act
        xmlSerializerProvider.serializeValue(jsonGenerator, null, null);

        // Assert
        // IOException should be thrown
    }

    @Test
    public void test_serializeValue_JsonGeneratorObjectJavaTypeJsonSerializer_CallsSuperclassSerialize() throws IOException {
        // Arrange
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        jsonGenerator = toXmlGenerator;

        when(toXmlGenerator.getCodec()).thenReturn(mock(ObjectCodec.class));
        when(toXmlGenerator.getCharacterEscapes()).thenReturn(mock(CharacterEscapes.class));
        when(toXmlGenerator.getOutputContext()).thenReturn(mock(JsonGenerator.class));
        when(toXmlGenerator.getSerializerProvider()).thenReturn(xmlSerializerProvider);

        // Act
        xmlSerializerProvider.serializeValue(jsonGenerator, null, null, null);

        // Assert
        verify(jsonGenerator, times(1)).writeNumber(anyInt());
    }

    @Test(expected = IOException.class)
    public void test_serializeValue_JsonGeneratorObjectJavaTypeJsonSerializer_ExceptionInSerialize_CallsSuperclassSerialize() throws IOException {
        // Arrange
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        jsonGenerator = toXmlGenerator;

        when(toXmlGenerator.getCodec()).thenReturn(mock(ObjectCodec.class));
        when(toXmlGenerator.getCharacterEscapes()).thenReturn(mock(CharacterEscapes.class));
        when(toXmlGenerator.getOutputContext()).thenReturn(mock(JsonGenerator.class));
        when(toXmlGenerator.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        doThrow(new IOException()).when(jsonGenerator).writeString(any(SerializableString.class));

        // Act
        xmlSerializerProvider.serializeValue(jsonGenerator, null, null, null);

        // Assert
        // IOException should be thrown
    }

    private class QuotedNamesPropertyNamingStrategy extends PropertyNamingStrategy.PropertyNamingStrategyBase {
        @Override
        public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
            return defaultName;
        }
    }
}