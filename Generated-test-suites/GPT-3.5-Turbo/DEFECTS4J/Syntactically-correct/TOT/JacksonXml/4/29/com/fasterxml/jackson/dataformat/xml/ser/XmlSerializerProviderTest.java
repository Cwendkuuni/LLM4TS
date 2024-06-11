
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import org.junit.Test;

import javax.xml.namespace.QName;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValue_WithNull_ShouldSerializeAsXmlNull() throws IOException {
        // Arrange
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();

        // Act
        xmlSerializerProvider.serializeValue(mockJsonGenerator, null);

        // Assert
        verify(mockJsonGenerator).writeEndObject();
    }

    @Test
    public void testSerializeValue_WithNonNull_ShouldSerializeUsingSerializer() throws IOException {
        // Arrange
        Object mockObject = mock(Object.class);
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        DefaultSerializerProvider mockSerializerProvider = mock(DefaultSerializerProvider.class);
        SerializerFactory mockSerializerFactory = mock(SerializerFactory.class);
        JavaType mockJavaType = mock(JavaType.class);
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        SerializationConfig mockSerializationConfig = mock(SerializationConfig.class);

        when(mockSerializationConfig.getFullRootName()).thenReturn(new PropertyName(null, "Root"));
        when(mockSerializerProvider.findTypedValueSerializer(Mockito.<Class<?>>any(), Mockito.anyBoolean(), Mockito.any())).thenReturn(mock(ObjectSerializer.class));
        when(mockObjectMapper.getSerializationConfig()).thenReturn(mockSerializationConfig);

        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(
                new XmlRootNameLookup(mockObjectMapper, false));

        // Act
        xmlSerializerProvider.serializeValue(mockJsonGenerator, mockObject, mockJavaType);

        // Assert
        verify(mockJsonGenerator).writeEndObject();
        verify(mockSerializerProvider).findTypedValueSerializer(Mockito.<Class<?>>any(), Mockito.anyBoolean(), Mockito.any());
    }

    @Test
    public void testSerializeValue_WithNonNull_ShouldWrapExceptionAsJsonMappingException() throws IOException {
         // Arrange
        Object mockObject = mock(Object.class);
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        DefaultSerializerProvider mockSerializerProvider = mock(DefaultSerializerProvider.class);
        SerializerFactory mockSerializerFactory = mock(SerializerFactory.class);
        JavaType mockJavaType = mock(JavaType.class);
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        SerializationConfig mockSerializationConfig = mock(SerializationConfig.class);

        when(mockSerializationConfig.getFullRootName()).thenReturn(new PropertyName(null, "Root"));
        when(mockSerializerProvider.findTypedValueSerializer(Mockito.<Class<?>>any(), Mockito.anyBoolean(), Mockito.any())).thenThrow(new IOException());
        when(mockObjectMapper.getSerializationConfig()).thenReturn(mockSerializationConfig);

        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(
                new XmlRootNameLookup(mockObjectMapper, false));

        // Act
        try {
            xmlSerializerProvider.serializeValue(mockJsonGenerator, mockObject, mockJavaType);
        } catch (Exception e) {
            // Assert
            assertEquals(JsonMappingException.class, e.getClass());
        }
    }

    @Test
    public void testStartRootArray_ShouldWriteStartObjectAndFieldName() throws IOException {
        // Arrange
        QName mockRootName = mock(QName.class);
        ToXmlGenerator mockXmlGenerator = mock(ToXmlGenerator.class);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();

        // Act
        xmlSerializerProvider._startRootArray(mockXmlGenerator, mockRootName);

        // Assert
        verify(mockXmlGenerator).writeStartObject();
        verify(mockXmlGenerator).writeFieldName("item");
    }

    @Test
    public void testStartRootArray_WithNullRootName_ShouldWriteStartObjectAndFieldName() throws IOException {
        // Arrange
        ToXmlGenerator mockXmlGenerator = mock(ToXmlGenerator.class);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();

        // Act
        xmlSerializerProvider._startRootArray(mockXmlGenerator, null);

        // Assert
        verify(mockXmlGenerator).writeStartObject();
        verify(mockXmlGenerator).writeFieldName("item");
    }

    @Test
    public void testInitWithRootName_WithNullRootName_ShouldSetNextName() throws IOException {
        // Arrange
        ToXmlGenerator mockXmlGenerator = mock(ToXmlGenerator.class);
        QName mockRootName = mock(QName.class);
        when(mockRootName.getNamespaceURI()).thenReturn(null);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();

        // Act
        xmlSerializerProvider._initWithRootName(mockXmlGenerator, mockRootName);

        // Assert
        verify(mockXmlGenerator).setNextName(mockRootName);
    }

    @Test
    public void testInitWithRootName_WithNonRootPosition_ShouldSetNextNameIfMissing() throws IOException {
        // Arrange
        ToXmlGenerator mockXmlGenerator = mock(ToXmlGenerator.class);
        QName mockRootName = mock(QName.class);
        when(mockRootName.getNamespaceURI()).thenReturn("http://namespace");
        when(mockXmlGenerator.inRoot()).thenReturn(false);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();

        // Act
        xmlSerializerProvider._initWithRootName(mockXmlGenerator, mockRootName);

        // Assert
        verify(mockXmlGenerator).setNextNameIfMissing(mockRootName);
        verify(mockXmlGenerator, never()).setNextName(mockRootName);
    }

    @Test
    public void testInitWithRootName_WithRootPosition_ShouldSetNextName() throws IOException {
        // Arrange
        ToXmlGenerator mockXmlGenerator = mock(ToXmlGenerator.class);
        QName mockRootName = mock(QName.class);
        when(mockRootName.getNamespaceURI()).thenReturn("http://namespace");
        when(mockXmlGenerator.inRoot()).thenReturn(true);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();

        // Act
        xmlSerializerProvider._initWithRootName(mockXmlGenerator, mockRootName);

        // Assert
        verify(mockXmlGenerator).setNextName(mockRootName);
    }

    @Test(expected = IOException.class)
    public void testInitWithRootName_WithXmlStreamException_ShouldThrowIOException() throws IOException {
        // Arrange
        ToXmlGenerator mockXmlGenerator = mock(ToXmlGenerator.class);
        QName mockRootName = mock(QName.class);
        when(mockRootName.getNamespaceURI()).thenReturn("http://namespace");
        when(mockXmlGenerator.inRoot()).thenReturn(true);
        doThrow(new XMLStreamException()).when(mockXmlGenerator).setDefaultNamespace("http://namespace");
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();

        // Act
        xmlSerializerProvider._initWithRootName(mockXmlGenerator, mockRootName);
    }

    @Test
    public void testRootNameFromConfig_WithNullConfig_ShouldReturnNull() {
        // Arrange
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();

        // Act
        QName result = xmlSerializerProvider._rootNameFromConfig();

        // Assert
        assertEquals(null, result);
    }

}

