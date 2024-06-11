
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import org.junit.Before;
import org.junit.Test;

import javax.xml.namespace.QName;
import java.io.IOException;

import static org.mockito.Mockito.*;


public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;
    private XmlRootNameLookup xmlRootNameLookup;
    private QName rootName;

    @Before
    public void setup() {
        xmlRootNameLookup = mock(XmlRootNameLookup.class);
        rootName = new QName("namespace", "name");
        xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
    }

    @Test
    public void testCopy() {
        // Setup
        XmlSerializerProvider src = new XmlSerializerProvider(xmlRootNameLookup);
        src.setNullValueSerializer(mock(JsonSerializer.class));

        // Test
        DefaultSerializerProvider result = src.copy();

        // Verify
        assert(result instanceof XmlSerializerProvider);
        assert(src.getNullValueSerializer() == result.getNullValueSerializer());
    }

    @Test
    public void testCreateInstance() {
        // Setup
        JsonFactory jsonFactory = mock(JsonFactory.class);
        SerializerFactory serializerFactory = mock(SerializerFactory.class);
        SerializationConfig serializationConfig = mock(SerializationConfig.class);

        // Test
        DefaultSerializerProvider result = xmlSerializerProvider.createInstance(serializationConfig, serializerFactory);

        // Verify
        assert(result instanceof XmlSerializerProvider);
        assert(result.getActiveGenerator() == xmlSerializerProvider.getActiveGenerator());
        assert(result.getConfig() == serializationConfig);
        assert(result.getGeneratorFactory() == xmlSerializerProvider.getGeneratorFactory());
        assert(result.getNullValueSerializer() == xmlSerializerProvider.getNullValueSerializer());
        assert(result.getFilteredBeanProperties() == xmlSerializerProvider.getFilteredBeanProperties());
        assert(result.getDefaultNullKeySerializer() == xmlSerializerProvider.getDefaultNullKeySerializer());
        assert(result.getDefaultNullValueSerializer() == xmlSerializerProvider.getDefaultNullValueSerializer());
    }

    @Test
    public void testSerializeValue_NullValue() throws IOException {
        // Setup
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        // Test
        xmlSerializerProvider.serializeValue(jsonGenerator, null);

        // Verify
        verify(jsonGenerator).setNextNameIfMissing(any(QName.class));
    }

    @Test
    public void testSerializeValue_ObjectValue() throws IOException {
        // Setup
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        Object value = new Object();

        // Test
        xmlSerializerProvider.serializeValue(jsonGenerator, value);

        // Verify
        verify(jsonGenerator, times(1)).serialize(value, xmlSerializerProvider);
    }

    @Test
    public void testSerializeValueWithRootType_NullValue() throws IOException {
        // Setup
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        JavaType rootType = mock(JavaType.class);

        // Test
        xmlSerializerProvider.serializeValue(jsonGenerator, null, rootType, null);

        // Verify
        verify(jsonGenerator).setNextNameIfMissing(any(QName.class));
    }

    @Test
    public void testSerializeValueWithRootType_ObjectValue() throws IOException {
        // Setup
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        JavaType rootType = mock(JavaType.class);
        Object value = new Object();

        // Test
        xmlSerializerProvider.serializeValue(jsonGenerator, value, rootType, null);

        // Verify
        verify(jsonGenerator, times(1)).serialize(value, xmlSerializerProvider);
    }

    @Test
    public void testSerializeXmlNull() throws IOException {
        // Setup
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        xmlSerializerProvider.setProvider(serializerProvider);

        // Test
        xmlSerializerProvider._serializeXmlNull(jsonGenerator);

        // Verify
        verify(jsonGenerator).serialize(null, serializerProvider);
    }

    @Test
    public void testStartRootArray() throws IOException {
        // Setup
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);

        // Test
        xmlSerializerProvider._startRootArray(toXmlGenerator, rootName);

        // Verify
        verify(toXmlGenerator).writeStartObject();
        verify(jsonGenerator, times(1)).writeFieldName("item");
    }

    @Test
    public void testInitWithRootName_RootNameNotSet() throws IOException {
        // Setup
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);

        // Test
        xmlSerializerProvider._initWithRootName(toXmlGenerator, null);

        // Verify
        verify(toXmlGenerator).initGenerator();
        verify(toXmlGenerator).setNextNameIfMissing(null);
    }

    @Test
    public void testInitWithRootName_RootNameSet() throws IOException {
        // Setup
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);

        // Test
        xmlSerializerProvider._initWithRootName(toXmlGenerator, rootName);

        // Verify
        verify(toXmlGenerator).initGenerator();
        verify(toXmlGenerator).setNextNameIfMissing(rootName);
    }

    @Test
    public void testRootNameFromConfig() {
        // Setup
        PropertyName propertyName = mock(PropertyName.class);
        xmlSerializerProvider.getConfig().setFullRootName(propertyName);

        // Test
        QName result = xmlSerializerProvider._rootNameFromConfig();

        // Verify
        assert(result.getNamespaceURI().equals("namespace"));
        assert(result.getLocalPart().equals("name"));
    }

    @Test
    public void testAsXmlGenerator_NotXmlGenerator() throws JsonMappingException {
        // Setup
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        // Test
        ToXmlGenerator result = xmlSerializerProvider._asXmlGenerator(jsonGenerator);

        // Verify
        assert(result == null);
    }

    @Test(expected = JsonMappingException.class)
    public void testAsXmlGenerator_InvalidGenerator() throws JsonMappingException {
        // Setup
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        // Test
        xmlSerializerProvider._asXmlGenerator(jsonGenerator);
    }

    @Test
    public void testWrapAsIOE_IOException() {
        // Setup
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        IOException exception = new IOException("Test exception");

        // Test
        IOException result = xmlSerializerProvider._wrapAsIOE(jsonGenerator, exception);

        // Verify
        assert(result == exception);
    }

    @Test
    public void testWrapAsIOE_RuntimeException() {
        // Setup
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        RuntimeException exception = new RuntimeException("Test exception");

        // Test
        IOException result = xmlSerializerProvider._wrapAsIOE(jsonGenerator, exception);

        // Verify
        assert(result.getCause() == exception);
        assert(result.getMessage().equals("Test exception"));
    }

}

