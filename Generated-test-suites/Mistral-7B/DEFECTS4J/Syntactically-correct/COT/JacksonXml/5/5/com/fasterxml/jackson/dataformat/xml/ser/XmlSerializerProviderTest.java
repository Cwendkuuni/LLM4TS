import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.ser.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup.RootName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class XmlSerializerProviderTest {

    private XmlRootNameLookup rootNameLookup;
    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setup() {
        rootNameLookup = new XmlRootNameLookup();
        xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
    }

    // Test for default constructor
    @Test
    public void testDefaultConstructor() {
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider();
        Assert.assertNull(serializerProvider._rootNameLookup);
    }

    // Test for constructor with XmlRootNameLookup
    @Test
    public void testConstructorWithXmlRootNameLookup() {
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        Assert.assertSame(rootNameLookup, serializerProvider._rootNameLookup);
    }

    // Test for copy constructor
    @Test
    public void testCopyConstructor() {
        XmlSerializerProvider serializerProvider1 = new XmlSerializerProvider(rootNameLookup);
        XmlSerializerProvider serializerProvider2 = new XmlSerializerProvider(serializerProvider1);
        Assert.assertSame(serializerProvider1._rootNameLookup, serializerProvider2._rootNameLookup);
    }

    // Test for serializeValue() with null value
    @Test
    public void testSerializeValueWithNullValue() throws IOException {
        JsonGenerator gen = new TokenBuffer();
        xmlSerializerProvider.serializeValue(gen, null);
        Assert.assertEquals("<null/>", gen.getAsString());
    }

    // Test for serializeValue() with non-null value
    @Test
    public void testSerializeValueWithNonNullValue() throws IOException {
        String value = "test";
        JsonGenerator gen = new TokenBuffer();
        xmlSerializerProvider.serializeValue(gen, value);
        Assert.assertEquals("<test/>", gen.getAsString());
    }

    // Test for serializeValue() with JavaType and JsonSerializer
    @Test
    public void testSerializeValueWithJavaTypeAndJsonSerializer() throws IOException {
        String value = "test";
        JavaType javaType = ObjectMapper.getDefaultInstance().typeFactory.constructType(String.class);
        JsonSerializer<Object> jsonSerializer = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(value.toString());
            }
        };
        JsonGenerator gen = new TokenBuffer();
        xmlSerializerProvider.serializeValue(gen, value, javaType, jsonSerializer);
        Assert.assertEquals("<test/>", gen.getAsString());
    }

    // Test for _serializeXmlNull()
    @Test
    public void testSerializeXmlNull() throws IOException, XMLStreamException {
        JsonGenerator gen = new TokenBuffer();
        xmlSerializerProvider._serializeXmlNull(gen);
        Assert.assertEquals("<null/>", gen.getAsString());
    }

    // Test for _startRootArray()
    @Test
    public void testStartRootArray() throws IOException {
        QName rootName = new QName("test", "item");
        ToXmlGenerator xgen = new ToXmlGenerator() {
            @Override
            public void writeStartObject() throws IOException, XMLStreamException {
                Assert.fail();
            }

            @Override
            public void writeStartArray() throws IOException, XMLStreamException {
                Assert.fail();
            }

            @Override
            public void writeFieldName(String name) throws IOException, XMLStreamException {
                Assert.assertEquals("item", name);
            }

            @Override
            public void writeStartObject(String name) throws IOException, XMLStreamException {
                xmlSerializerProvider._startRootArray(xgen, rootName);
            }
        };
        xmlSerializerProvider._startRootArray(xgen, rootName);
    }

    // Test for _initWithRootName()
    @Test
    public void testInitWithRootName() throws IOException, XMLStreamException {
        QName rootName = new QName("test", "item");
        ToXmlGenerator xgen = new ToXmlGenerator() {
            @Override
            public String getNamespaceURI() {
                Assert.fail();
                return null;
            }

            @Override
            public void initGenerator() throws IOException, XMLStreamException {
                xmlSerializerProvider._initWithRootName(xgen, rootName);
            }
        };
        xmlSerializerProvider._initWithRootName(xgen, rootName);
    }

    // Test for _rootNameFromConfig()
    @Test
    public void testRootNameFromConfig() {
        SerializationConfig config = new SerializationConfig();
        PropertyName rootName = new PropertyName("test", "item");
        config.setFullRootName(rootName);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        QName result = xmlSerializerProvider._rootNameFromConfig();
        RootName expected = new RootName("test", "item");
        Assert.assertEquals(expected.getNamespaceURI(), result.getNamespaceURI());
        Assert.assertEquals(expected.getLocalPart(), result.getLocalPart());
    }

    // Test for _asXmlGenerator()
    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorWithInvalidGenerator() throws IOException {
        JsonGenerator gen = new JsonGenerator() {};
        xmlSerializerProvider._asXmlGenerator(gen);
    }

    // Test for _wrapAsIOE()
    @Test
    public void testWrapAsIOE() {
        IOException ioException = new IOException();
        JsonGenerator gen = new TokenBuffer();
        SerializationException serializationException = new SerializationException(ioException, gen, null);
        IOException result = xmlSerializerProvider._wrapAsIOE(gen, serializationException);
        Assert.assertSame(ioException, result.getCause());
    }
}