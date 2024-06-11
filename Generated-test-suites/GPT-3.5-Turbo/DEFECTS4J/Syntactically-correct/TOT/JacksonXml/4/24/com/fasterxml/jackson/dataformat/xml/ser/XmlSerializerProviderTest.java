
package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import org.junit.Before;
import org.junit.Test;

import javax.xml.namespace.QName;

import static org.mockito.Mockito.*;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider serializerProvider;
    private SerializationConfig serializationConfig;
    private SerializerFactory serializerFactory;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProviderMock;
    private ObjectMapper objectMapper;
    private Object value;

    @Before
    public void setup() {
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);
        serializerProviderMock = mock(DefaultSerializerProvider.class);
        serializationConfig = mock(SerializationConfig.class);
        serializerFactory = mock(SerializerFactory.class);
        jsonGenerator = mock(JsonGenerator.class);
        value = new Object();

        serializerProvider = new XmlSerializerProvider(rootNameLookup);
        serializerProvider = new XmlSerializerProvider(serializerProvider, serializationConfig, serializerFactory);

        objectMapper = new ObjectMapper();
        objectMapper.setSerializerProvider(serializerProviderMock);
    }

    @Test
    public void testSerializeValueWithNull() throws Exception {
        //Arrange
        serializerProvider._serializeXmlNull(jsonGenerator);

        //Act
        serializerProvider.serializeValue(jsonGenerator, null, JavaType.VOID);

        //Assert
        verify(jsonGenerator, times(1)).writeNull();
    }

    @Test
    public void testSerializeValueWithRootNameFromConfigAndNotNull() throws Exception {
        //Arrange
        QName rootName = new QName("root");
        when(serializationConfig.getFullRootName()).thenReturn(new PropertyName("root"));
        when(serializerProviderMock.findTypedValueSerializer(JavaType.VOID, true, null)).thenReturn(mock(JsonSerializer.class));
        when(serializerProviderMock.createInstance(serializationConfig, serializerFactory)).thenReturn(mock(DefaultSerializerProvider.class));
        when(serializerProviderMock.findTypedValueSerializer(JavaType.VOID, true, null)).thenReturn(mock(JsonSerializer.class));

        //Act
        serializerProvider.serializeValue(jsonGenerator, new Object(), JavaType.VOID);

        //Assert
        verify(jsonGenerator, times(1)).writeStartObject();
        verify(jsonGenerator, times(1)).writeEndObject();
        verify(serializerProviderMock, times(1)).findTypedValueSerializer(JavaType.VOID, true, null);
    }

    @Test
    public void testSerializeValueWithRootNameFromRootTypeAndNotNull() throws Exception {
        //Arrange
        JavaType rootType = JavaType.VOID;
        QName rootName = new QName("root");
        when(serializerProviderMock.findTypedValueSerializer(JavaType.VOID, true, null)).thenReturn(mock(JsonSerializer.class));
        when(serializerProviderMock.createInstance(serializationConfig, serializerFactory)).thenReturn(mock(DefaultSerializerProvider.class));
        when(serializerProviderMock.findTypedValueSerializer(JavaType.VOID, true, null)).thenReturn(mock(JsonSerializer.class));

        //Act
        serializerProvider.serializeValue(jsonGenerator, new Object(), rootType);

        //Assert
        verify(jsonGenerator, times(1)).writeStartObject();
        verify(jsonGenerator, times(1)).writeEndObject();
        verify(serializerProviderMock, times(1)).findTypedValueSerializer(JavaType.VOID, true, null);
    }

    @Test
    public void testSerializeValueWithRootNameFromRootTypeAndNotNullAndSerializer() throws Exception {
        //Arrange
        JavaType rootType = JavaType.VOID;
        QName rootName = new QName("root");
        when(serializerProviderMock.findTypedValueSerializer(JavaType.VOID, true, null)).thenReturn(mock(JsonSerializer.class));
        when(serializerProviderMock.createInstance(serializationConfig, serializerFactory)).thenReturn(mock(DefaultSerializerProvider.class));
        when(serializerProviderMock.findTypedValueSerializer(JavaType.VOID, true, null)).thenReturn(mock(JsonSerializer.class));

        //Act
        serializerProvider.serializeValue(jsonGenerator, new Object(), rootType, mock(JsonSerializer.class));

        //Assert
        verify(jsonGenerator, times(1)).writeStartObject();
        verify(jsonGenerator, times(1)).writeEndObject();
        verify(serializerProviderMock, times(1)).findTypedValueSerializer(JavaType.VOID, true, null);
    }
}
