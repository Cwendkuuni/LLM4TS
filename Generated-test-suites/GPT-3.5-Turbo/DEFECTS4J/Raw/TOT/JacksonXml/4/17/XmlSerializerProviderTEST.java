import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider serializerProvider;
    private XmlRootNameLookup rootNameLookup;

    @Before
    public void setup() {
        rootNameLookup = mock(XmlRootNameLookup.class);
        serializerProvider = new XmlSerializerProvider(rootNameLookup);
    }

    @Test
    public void testCreateInstance() {
        // Arrange
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerFactory factory = mock(SerializerFactory.class);
        
        // Act
        DefaultSerializerProvider instance = serializerProvider.createInstance(config, factory);
        
        // Assert
        assertEquals(serializerProvider, instance.getParent());
        assertEquals(config, instance.getConfig());
        assertEquals(factory, instance.getFactory());
    }

    @Test
    public void testSerializeValue_withNullData_shouldSerializeXmlNull() throws IOException {
        // Arrange
        JsonGenerator generator = mock(JsonGenerator.class);
        
        // Act
        serializerProvider.serializeValue(generator, null);
        
        // Assert
        verify(generator).writeObject(any());
    }

    @Test
    public void testSerializeValue_withNonNullData_shouldSerializeData() throws IOException {
        // Arrange
        JsonGenerator generator = mock(JsonGenerator.class);
        Object data = new Object();
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(serializerProvider.findTypedValueSerializer(any(Class.class), anyBoolean(), any())).thenReturn(serializer);
        
        // Act
        serializerProvider.serializeValue(generator, data);
        
        // Assert
        verify(serializer).serialize(eq(data), eq(generator), eq(serializerProvider));
    }

    @Test
    public void testSerializeValue_withNullDataAndRootType_shouldSerializeXmlNull() throws IOException {
        // Arrange
        JsonGenerator generator = mock(JsonGenerator.class);
        JavaType rootType = mock(JavaType.class);
        
        // Act
        serializerProvider.serializeValue(generator, null, rootType);
        
        // Assert
        verify(generator).writeObject(any());
    }

    @Test
    public void testSerializeValue_withNonNullDataAndRootType_shouldSerializeData() throws IOException {
        // Arrange
        JsonGenerator generator = mock(JsonGenerator.class);
        Object data = new Object();
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(serializerProvider.findTypedValueSerializer(any(JavaType.class), anyBoolean(), any())).thenReturn(serializer);
        
        // Act
        serializerProvider.serializeValue(generator, data, rootType);
        
        // Assert
        verify(serializer).serialize(eq(data), eq(generator), eq(serializerProvider));
    }
    
    @Test
    public void testSerializeValue_withNullDataRootTypeAndSerializer_shouldSerializeXmlNull() throws IOException {
        // Arrange
        JsonGenerator generator = mock(JsonGenerator.class);
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        
        // Act
        serializerProvider.serializeValue(generator, null, rootType, serializer);
        
        // Assert
        verify(generator).writeObject(any());
    }

    @Test
    public void testSerializeValue_withNonNullDataRootTypeAndSerializer_shouldSerializeData() throws IOException {
        // Arrange
        JsonGenerator generator = mock(JsonGenerator.class);
        Object data = new Object();
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        
        // Act
        serializerProvider.serializeValue(generator, data, rootType, serializer);
        
        // Assert
        verify(serializer).serialize(eq(data), eq(generator), eq(serializerProvider));
    }

    @Test
    public void testSerializeXmlNull() throws IOException {
        // Arrange
        JsonGenerator generator = mock(JsonGenerator.class);
        ToXmlGenerator xmlGenerator = mock(ToXmlGenerator.class);
        when(serializerProvider._asXmlGenerator(eq(generator))).thenReturn(xmlGenerator);
        when(rootNameLookup.findRootName(any(Class.class), any())).thenReturn(new QName("null"));
        
        // Act
        serializerProvider._serializeXmlNull(generator);
        
        // Assert
        verify(xmlGenerator).initWithRootName(any(QName.class));
        verify(xmlGenerator).initGenerator();
        verify(xmlGenerator.getStaxWriter()).setDefaultNamespace(anyString());
        verify(xmlGenerator).writeObject(eq(null));
    }

    @Test
    public void testStartRootArray() throws IOException {
        // Arrange
        ToXmlGenerator generator = mock(ToXmlGenerator.class);
        QName rootName = new QName("name");
        
        // Act
        serializerProvider._startRootArray(generator, rootName);
        
        // Assert
        verify(generator).writeStartObject();
        verify(generator).writeFieldName(eq("item"));
    }

    @Test
    public void testInitWithRootName() throws IOException {
        // Arrange
        ToXmlGenerator generator = mock(ToXmlGenerator.class);
        QName rootName = new QName("name");
        
        // Act
        serializerProvider._initWithRootName(generator, rootName);
        
        // Assert
        verify(generator).setNextNameIfMissing(eq(rootName));
        verify(generator).inRoot();
        verify(generator).setNextName(eq(rootName));
        verify(generator).initGenerator();
        verify(generator).getStaxWriter();
        verify(generator.getStaxWriter()).setDefaultNamespace(anyString());
    }

    @Test
    public void testRootNameFromConfig_withNullPropertyName_shouldReturnNull() {
        // Arrange
        SerializationConfig config = mock(SerializationConfig.class);
        
        // Act
        QName rootName = serializerProvider._rootNameFromConfig();
        
        // Assert
        assertEquals(null, rootName);
    }
    
    @Test
    public void testRootNameFromConfig_withEmptyNamespace_shouldReturnQNameWithSimpleNameOnly() {
        // Arrange
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerProvider.PropertyName propertyName = mock(SerializerProvider.PropertyName.class);
        when(config.getFullRootName()).thenReturn(propertyName);
        when(propertyName.getNamespace()).thenReturn("");
        when(propertyName.getSimpleName()).thenReturn("name");
        
        // Act
        QName rootName = serializerProvider._rootNameFromConfig();
        
        // Assert
        assertEquals(new QName("name"), rootName);
    }
    
    @Test
    public void testRootNameFromConfig_withNonEmptyNamespace_shouldReturnQNameWithNamespaceAndSimpleName() {
        // Arrange
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerProvider.PropertyName propertyName = mock(SerializerProvider.PropertyName.class);
        when(config.getFullRootName()).thenReturn(propertyName);
        when(propertyName.getNamespace()).thenReturn("namespace");
        when(propertyName.getSimpleName()).thenReturn("name");
        
        // Act
        QName rootName = serializerProvider._rootNameFromConfig();
        
        // Assert
        assertEquals(new QName("namespace", "name"), rootName);
    }

    @Test(expected = JsonMappingException.class)
    public void testAsXmlGenerator_withNonToXmlGenerator_shouldThrowException() {
        // Arrange
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        // Act
        serializerProvider._asXmlGenerator(jsonGenerator);

        // Assert (expected exception)
    }

    @Test
    public void testAsXmlGenerator_withToXmlGenerator_shouldReturnXmlGenerator() {
        // Arrange
        JsonGenerator jsonGenerator = mock(ToXmlGenerator.class);

        // Act
        ToXmlGenerator result = serializerProvider._asXmlGenerator(jsonGenerator);

        // Assert
        assertEquals(jsonGenerator, result);
    }
}