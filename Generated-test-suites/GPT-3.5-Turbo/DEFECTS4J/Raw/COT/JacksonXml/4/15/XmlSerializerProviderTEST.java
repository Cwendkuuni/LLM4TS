
import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testXmlSerializerProvider() {
        QName rootName = new QName("root");
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        
        Assert.assertNotNull(provider);
    }

    @Test
    public void testCreateInstance() {
        QName rootName = new QName("root");
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        SerializationConfig config = new SerializationConfig(null);
        SerializerFactory factory = new SerializerFactory(config);
        
        XmlSerializerProvider newInstance = provider.createInstance(config, factory);
        
        Assert.assertNotNull(newInstance);
    }

    @Test
    public void testSerializeValueWithNull() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        
        provider.serializeValue(jsonGenerator, null);
        
        jsonGenerator.close();
    }

    @Test
    public void testSerializeValue() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        Object value = new Object();
        
        provider.serializeValue(jsonGenerator, value);
        
        jsonGenerator.close();
    }

    @Test
    public void testSerializeValueWithRootType() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        Object value = new Object();
        JavaType rootType = JavaType.fromClass(Object.class);
        
        provider.serializeValue(jsonGenerator, value, rootType);
        
        jsonGenerator.close();
    }

    @Test
    public void testSerializeValueWithSerializer() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        Object value = new Object();
        JavaType rootType = JavaType.fromClass(Object.class);
        JsonSerializer<Object> serializer = new JsonSerializer<Object>() {
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };
        
        provider.serializeValue(jsonGenerator, value, rootType, serializer);
        
        jsonGenerator.close();
    }

    @Test
    public void test_serializeXmlNull() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        
        provider.serializeXmlNull(jsonGenerator);
        
        jsonGenerator.close();
    }

    @Test
    public void test_startRootArray() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        QName rootName = new QName("root");
        
        provider.startRootArray(jsonGenerator, rootName);
        
        jsonGenerator.close();
    }

    @Test
    public void test_initWithRootName() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        QName rootName = new QName("root");
        
        provider.initWithRootName(jsonGenerator, rootName);
        
        jsonGenerator.close();
    }

    @Test
    public void test_rootNameFromConfig() {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        
        QName rootName = provider.rootNameFromConfig();
        
        Assert.assertNull(rootName);
    }

    @Test
    public void test_asXmlGeneratorWithToXmlGenerator() throws JsonMappingException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = new ToXmlGenerator(null, 0, null);
        
        ToXmlGenerator xmlGenerator = provider.asXmlGenerator(jsonGenerator);
        
        Assert.assertNotNull(xmlGenerator);
    }

    @Test(expected = JsonMappingException.class)
    public void test_asXmlGeneratorWithInvalidGenerator() throws JsonMappingException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        
        provider.asXmlGenerator(jsonGenerator);
    }
}

