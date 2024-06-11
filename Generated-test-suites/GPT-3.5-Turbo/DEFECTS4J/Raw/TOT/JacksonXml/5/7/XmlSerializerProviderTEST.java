import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;
    private ToXmlGenerator toXmlGeneratorMock;
    private JsonGenerator jsonGeneratorMock;
    private JsonMappingException jsonMappingExceptionMock;
    private XmlRootNameLookup xmlRootNameLookupMock;

    private class TestClass {}
    
    @Before
    public void setup() {
        xmlRootNameLookupMock = mock(XmlRootNameLookup.class);
        xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookupMock);
        toXmlGeneratorMock = mock(ToXmlGenerator.class);
        jsonGeneratorMock = mock(JsonGenerator.class);
        jsonMappingExceptionMock = mock(JsonMappingException.class);
    }

    @Test
    public void test_serializeValue_NullInput_GenerateXmlNull() throws Exception {
        // Arrange
        
        // Act
        xmlSerializerProvider.serializeValue(jsonGeneratorMock, null);
        
        // Assert
        verify(jsonGeneratorMock).writeStartObject();
        verify(toXmlGeneratorMock).writeFieldName("item");
        verify(jsonGeneratorMock).writeEndObject();
    }

    @Test
    public void test_serializeValue_NullInputWithoutToXmlGenerator_DoNothing() throws Exception {
        // Arrange
        
        // Act
        xmlSerializerProvider.serializeValue(jsonGeneratorMock, null, null, null);
        
        // Assert
        verifyNoMoreInteractions(jsonGeneratorMock);
    }

    @Test
    public void test_serializeValue_TypedValue_GenerateXml() throws Exception {
        // Arrange
        TestClass testObject = new TestClass();
        JsonSerializer<Object> jsonSerializerMock = mock(JsonSerializer.class);
        
        // Act
        xmlSerializerProvider.serializeValue(jsonGeneratorMock, testObject);
        
        // Assert
        verify(jsonGeneratorMock).writeStartObject();
        verify(jsonSerializerMock).serialize(eq(testObject), eq(jsonGeneratorMock), eq(xmlSerializerProvider));
        verify(jsonGeneratorMock).writeEndObject();
    }

    @Test(expected = JsonMappingException.class)
    public void test_serializeValue_TypedValueWithException_ThrowJsonMappingException() throws Exception {
        // Arrange
        TestClass testObject = new TestClass();
        JsonSerializer<Object> jsonSerializerMock = mock(JsonSerializer.class);
        doThrow(jsonMappingExceptionMock).when(jsonSerializerMock).serialize(eq(testObject), eq(jsonGeneratorMock), eq(xmlSerializerProvider));
        
        // Act
        xmlSerializerProvider.serializeValue(jsonGeneratorMock, testObject);
    }

    @Test
    public void test_serializeValue_TypedValueWithToXmlGenerator_NoRootName_GenerateXml() throws Exception {
        // Arrange
        TestClass testObject = new TestClass();
        JsonSerializer<Object> jsonSerializerMock = mock(JsonSerializer.class);
        when(toXmlGeneratorMock.inRoot()).thenReturn(true);
        
        // Act
        xmlSerializerProvider.serializeValue(jsonGeneratorMock, testObject);
        
        // Assert
        verify(toXmlGeneratorMock).setNextNameIfMissing(any(QName.class));
    }

    @Test
    public void test_serializeValue_TypedValueWithToXmlGenerator_RootNameExists_GenerateXml() throws Exception {
        // Arrange
        TestClass testObject = new TestClass();
        JsonSerializer<Object> jsonSerializerMock = mock(JsonSerializer.class);
        when(toXmlGeneratorMock.inRoot()).thenReturn(false);
        
        // Act
        xmlSerializerProvider.serializeValue(jsonGeneratorMock, testObject);
        
        // Assert
        verify(toXmlGeneratorMock).setNextNameIfMissing(any(QName.class));
    }

    @Test
    public void test_serializeValue_TypedValueWithRootType_GenerateXml() throws Exception {
        // Arrange
        TestClass testObject = new TestClass();
        JavaType javaTypeMock = mock(JavaType.class);
        JsonSerializer<Object> jsonSerializerMock = mock(JsonSerializer.class);
        
        // Act
        xmlSerializerProvider.serializeValue(jsonGeneratorMock, testObject, javaTypeMock, jsonSerializerMock);
        
        // Assert
        verify(jsonGeneratorMock).writeStartObject();
        verify(jsonSerializerMock).serialize(eq(testObject), eq(jsonGeneratorMock), eq(xmlSerializerProvider));
        verify(jsonGeneratorMock).writeEndObject();
    }

    @Test(expected = JsonMappingException.class)
    public void test_serializeValue_TypedValueWithRootTypeAndException_ThrowJsonMappingException() throws Exception {
        // Arrange
        TestClass testObject = new TestClass();
        JavaType javaTypeMock = mock(JavaType.class);
        JsonSerializer<Object> jsonSerializerMock = mock(JsonSerializer.class);
        doThrow(jsonMappingExceptionMock).when(jsonSerializerMock).serialize(eq(testObject), eq(jsonGeneratorMock), eq(xmlSerializerProvider));
        
        // Act
        xmlSerializerProvider.serializeValue(jsonGeneratorMock, testObject, javaTypeMock, jsonSerializerMock);
    }

    @Test
    public void test_startRootArray_StartRootArray() throws Exception {
        // Arrange
        QName rootNameMock = mock(QName.class);
        
        // Act
        xmlSerializerProvider._startRootArray(toXmlGeneratorMock, rootNameMock);
        
        // Assert
        verify(toXmlGeneratorMock).writeStartObject();
        verify(toXmlGeneratorMock).writeFieldName("item");
    }

    @Test
    public void test_initWithRootName_RootNameExists_InitGenerator() throws Exception {
        // Arrange
        QName rootNameMock = mock(QName.class);
        when(toXmlGeneratorMock.setNextNameIfMissing(any(QName.class))).thenReturn(false);
        when(toXmlGeneratorMock.inRoot()).thenReturn(true);
        
        // Act
        xmlSerializerProvider._initWithRootName(toXmlGeneratorMock, rootNameMock);
        
        // Assert
        verify(toXmlGeneratorMock).setNextNameIfMissing(any(QName.class));
        verify(toXmlGeneratorMock).inRoot();
    }

    @Test
    public void test_initWithRootName_RootNameDoesNotExist_InRootExists_SetNextName() throws Exception {
        // Arrange
        QName rootNameMock = mock(QName.class);
        when(toXmlGeneratorMock.setNextNameIfMissing(any(QName.class))).thenReturn(false);
        when(toXmlGeneratorMock.inRoot()).thenReturn(false);
        
        // Act
        xmlSerializerProvider._initWithRootName(toXmlGeneratorMock, rootNameMock);
        
        // Assert
        verify(toXmlGeneratorMock).setNextNameIfMissing(any(QName.class));
        verify(toXmlGeneratorMock).inRoot();
        verify(toXmlGeneratorMock).setNextName(rootNameMock);
    }

    @Test
    public void test_initWithRootName_RootNameDoesNotExist_InRootDoesNotExist_DoNothing() throws Exception {
        // Arrange
        QName rootNameMock = mock(QName.class);
        when(toXmlGeneratorMock.setNextNameIfMissing(any(QName.class))).thenReturn(true);
        
        // Act
        xmlSerializerProvider._initWithRootName(toXmlGeneratorMock, rootNameMock);
        
        // Assert
        verify(toXmlGeneratorMock).setNextNameIfMissing(any(QName.class));
        verifyNoMoreInteractions(toXmlGeneratorMock);
    }

    @Test
    public void test_rootNameFromConfig_RootNameExists_ReturnRootName() throws Exception {
        // Arrange
        QName expectedRootName = new QName("testNamespace", "testLocalPart");
        when(xmlSerializerProvider._config.getFullRootName()).thenReturn(new PropertyName(expectedRootName.getLocalPart(), expectedRootName.getNamespaceURI()));
        
        // Act
        QName rootName = xmlSerializerProvider._rootNameFromConfig();
        
        // Assert
        assertEquals(expectedRootName.getLocalPart(), rootName.getLocalPart());
        assertEquals(expectedRootName.getNamespaceURI(), rootName.getNamespaceURI());
    }

    @Test
    public void test_rootNameFromConfig_RootNameDoesNotExist_ReturnNull() throws Exception {
        // Arrange
        
        // Act
        QName rootName = xmlSerializerProvider._rootNameFromConfig();
        
        // Assert
        assertNull(rootName);
    }

    @Test
    public void test_asXmlGenerator_GeneratorIsToXmlGenerator_ReturnToXmlGenerator() throws Exception {
        // Arrange
        
        // Act
        ToXmlGenerator toXmlGenerator = xmlSerializerProvider._asXmlGenerator(toXmlGeneratorMock);
        
        // Assert
        assertNotNull(toXmlGenerator);
    }

    @Test(expected = JsonMappingException.class)
    public void test_asXmlGenerator_GeneratorIsNotToXmlGenerator_ThrowJsonMappingException() throws Exception {
        // Arrange
        
        // Act
        xmlSerializerProvider._asXmlGenerator(jsonGeneratorMock);
    }

    @Test
    public void test_wrapAsIOE_ExceptionIsIOException_ReturnIOException() throws Exception {
        // Arrange
        IOException ioExceptionMock = mock(IOException.class);
        
        // Act
        IOException wrappedIOException = xmlSerializerProvider._wrapAsIOE(jsonGeneratorMock, ioExceptionMock);
        
        // Assert
        assertEquals(ioExceptionMock, wrappedIOException);
    }

    @Test
    public void test_wrapAsIOE_ExceptionIsNotIOException_ReturnJsonMappingExceptionWithWrappedException() throws Exception {
        // Arrange
        String exceptionMessage = "Test exception message";
        RuntimeException runtimeException = new RuntimeException(exceptionMessage);
        when(jsonMappingExceptionMock.getMessage()).thenReturn(null);
        when(jsonMappingExceptionMock.getCause()).thenReturn(runtimeException);
        
        // Act
        JsonMappingException wrappedJsonMappingException = xmlSerializerProvider._wrapAsIOE(jsonGeneratorMock, jsonMappingExceptionMock);
        
        // Assert
        assertEquals(exceptionMessage, wrappedJsonMappingException.getMessage());
        assertEquals(runtimeException, wrappedJsonMappingException.getCause());
    }

}