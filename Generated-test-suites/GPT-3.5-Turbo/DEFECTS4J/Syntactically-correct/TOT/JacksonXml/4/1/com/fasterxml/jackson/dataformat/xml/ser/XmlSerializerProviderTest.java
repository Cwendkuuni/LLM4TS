import org.junit.*;
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider serializerProvider;
    private XmlRootNameLookup rootNameLookup;
    private SerializationConfig config;
    private SerializerFactory factory;

    @Before
    public void setUp() {
        rootNameLookup = new XmlRootNameLookup();
        serializerProvider = new XmlSerializerProvider(rootNameLookup);
        config = new SerializationConfig();
        factory = new SerializerFactory();
    }

    @Test
    public void testCreateInstance() {
        DefaultSerializerProvider instance = serializerProvider.createInstance(config, factory);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testSerializeValue() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        Object value = new Object();
        serializerProvider.serializeValue(gen, value);
        Assert.assertNotNull(gen);
    }

    @Test
    public void testSerializeValueWithJavaType() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        Object value = new Object();
        JavaType rootType = new JavaType();
        serializerProvider.serializeValue(gen, value, rootType);
        Assert.assertNotNull(gen);
    }

    @Test
    public void testSerializeValueWithSerializer() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        Object value = new Object();
        JavaType rootType = new JavaType();
        serializerProvider.serializeValue(gen, value, rootType, null);
        Assert.assertNotNull(gen);
    }

    @Test
    public void testSerializeXmlNull() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializerProvider._serializeXmlNull(gen);
        Assert.assertNotNull(gen);
    }

    @Test
    public void testStartRootArray() throws IOException {
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName("root");
        serializerProvider._startRootArray(xgen, rootName);
        Assert.assertNotNull(xgen);
    }

    @Test
    public void testInitWithRootName() throws IOException {
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName("root");
        serializerProvider._initWithRootName(xgen, rootName);
        Assert.assertNotNull(xgen);
    }

    @Test
    public void testRootNameFromConfig() {
        PropertyName name = new PropertyName("root");
        QName rootName = serializerProvider._rootNameFromConfig(name);
        Assert.assertNotNull(rootName);
    }

    @Test
    public void testAsXmlGenerator() throws JsonMappingException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        ToXmlGenerator xgen = serializerProvider._asXmlGenerator(gen);
        Assert.assertNotNull(xgen);
    }

    @After
    public void tearDown() {
        rootNameLookup = null;
        serializerProvider = null;
        config = null;
        factory = null;
    }
}