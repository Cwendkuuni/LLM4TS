import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;
    private XmlRootNameLookup xmlRootNameLookup;

    @Before
    public void setUp() {
        xmlRootNameLookup = new XmlRootNameLookup();
        xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
    }

    @Test
    public void testDefaultConstructor() {
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider();
        Assert.assertNotNull(serializerProvider._rootNameLookup);
    }

    @Test
    public void testCopyConstructor() {
        XmlSerializerProvider srcSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        XmlSerializerProvider copiedSerializerProvider = new XmlSerializerProvider(srcSerializerProvider);
        Assert.assertNotSame(srcSerializerProvider, copiedSerializerProvider);
        Assert.assertEquals(srcSerializerProvider._rootNameLookup, copiedSerializerProvider._rootNameLookup);
    }

    @Test
    public void testCreateInstance() {
        SerializationConfig config = new SerializationConfig();
        SerializerFactory serializerFactory = new DefaultSerializerFactory();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(config, serializerFactory);
        Assert.assertNotNull(serializerProvider._rootNameLookup);
    }

    @Test
    public void testSerializeValueNull() throws JsonMappingException, IOException {
        JsonGenerator jsonGenerator = new TokenBuffer();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        xmlSerializerProvider.serializeValue(jsonGenerator, null);
        ObjectReader objectReader = new ObjectMapper().readerForValue("null");
        Assert.assertEquals(objectReader.readValueAsTree(jsonGenerator.asString()), objectMapper.valueToTree(new Object()));
    }

    @Test
    public void testSerializeValue() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = new TokenBuffer();
        Object obj = new Object();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        xmlSerializerProvider.serializeValue(jsonGenerator, obj);
        Assert.assertEquals(objectMapper.readTree(jsonGenerator.asString()), objectMapper.valueToTree(obj));
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueIOException() throws IOException {
        JsonGenerator jsonGenerator = new StaxMappedStreamContext();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        xmlSerializerProvider.serializeValue(jsonGenerator, new Object());
    }

    @Test
    public void testSerializeValueJsonMappingException() throws JsonMappingException {
        JsonGenerator jsonGenerator = new StaxMappedStreamContext();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        xmlSerializerProvider.serializeValue(jsonGenerator, new Object(), null);
    }

    @Test
    public void testSerializeValueNotFoundRootName() throws JsonMappingException, IOException {
        JsonGenerator jsonGenerator = new TokenBuffer();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        xmlSerializerProvider.serializeValue(jsonGenerator, new Object(), null, null);
        ObjectReader objectReader = new ObjectMapper().readerForValue("{@type null}");
        Assert.assertEquals(objectReader.readValueAsTree(jsonGenerator.asString()), objectMapper.valueToTree(new Object()));
    }

    @Test
    public void testSerializeValueWithRootName() throws JsonMappingException, IOException {
        JsonGenerator jsonGenerator = new TokenBuffer();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        QName rootName = new QName("testRootName");
        xmlSerializerProvider.serializeValue(jsonGenerator, new Object(), null, rootName);
        ObjectReader objectReader = new ObjectMapper().readerForValue("<testRootName>{@type null}</testRootName>");
        Assert.assertEquals(objectReader.readValueAsTree(jsonGenerator.asString()), objectMapper.valueToTree(new Object()).get("testRootName"));
    }

    @Test
    public void testSerializeValueArray() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = new TokenBuffer();
        Object[] objArray = new Object[5];
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        xmlSerializerProvider.serializeValue(jsonGenerator, objArray);
        Assert.assertEquals(objectMapper.readTree(jsonGenerator.asString()), objectMapper.valueToTree(objArray).get("item"));
    }

    @Test
    public void testSerializeValueArrayRootName() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = new TokenBuffer();
        Object[] objArray = new Object[5];
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        QName rootName = new QName("testRootName");
        xmlSerializerProvider.serializeValue(jsonGenerator, objArray, null, rootName);
        Assert.assertEquals(objectMapper.readTree(jsonGenerator.asString()), objectMapper.valueToTree(objArray).get("testRootName"));
    }

    @Test
    public void testFindTypedValueSerializer() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<Object> jsonSerializer = xmlSerializerProvider.findTypedValueSerializer(Object.class, true, null);
        Assert.assertNotNull(jsonSerializer);
        JsonSerializer<String> jsonSerializerString = xmlSerializerProvider.findTypedValueSerializer(String.class, true, null);
        Assert.assertNotNull(jsonSerializerString);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindTypedValueSerializerException() throws JsonMappingException {
        JsonSerializer<Object> jsonSerializer = xmlSerializerProvider.findTypedValueSerializer(new Object(), true, null);
    }

    @Test
    public void testFindTypedValueSerializerNull() throws JsonMappingException {
        JsonSerializer<Object> jsonSerializer = xmlSerializerProvider.findTypedValueSerializer(null, true, null);
        Assert.assertNull(jsonSerializer);
    }

    @Test
    public void testInitWithRootName() throws IOException, XMLStreamException {
        JsonGenerator jsonGenerator = new StaxMappedStreamContext();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        xmlSerializerProvider._initWithRootName(jsonGenerator, new QName("testRootName"));
        String expectedRootName = "<testRootName xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\">";
        String actualRootName = jsonGenerator.getXMLStreamWriter().getText();
        Assert.assertEquals(expectedRootName, actualRootName);
    }

    @Test
    public void testInitWithRootNameNamespace() throws IOException, XMLStreamException {
        JsonGenerator jsonGenerator = new StaxMappedStreamContext();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        xmlSerializerProvider._initWithRootName(jsonGenerator, new QName("testRootName", "http://test.com"));
        String expectedRootName = "<testRootName xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:test=\"http://test.com\">";
        String actualRootName = jsonGenerator.getXMLStreamWriter().getText();
        Assert.assertEquals(expectedRootName, actualRootName);
    }

    @Test
    public void testSetNextName() throws IOException, XMLStreamException {
        JsonGenerator jsonGenerator = new StaxMappedStreamContext();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        xmlSerializerProvider._setNextName(jsonGenerator, new QName("testName"));
        String expectedNextName = "<testName>";
        String actualNextName = jsonGenerator.getXMLStreamWriter().getText();
        Assert.assertEquals(expectedNextName, actualNextName);
    }

    @Test
    public void testSetNextNameEmptyNamespace() throws IOException, XMLStreamException {
        JsonGenerator jsonGenerator = new StaxMappedStreamContext();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        xmlSerializerProvider._setNextName(jsonGenerator, new QName("testName"));
        String expectedNextName = "<testName>";
        String actualNextName = jsonGenerator.getXMLStreamWriter().getText();
        Assert.assertEquals(expectedNextName, actualNextName);
    }

    @Test
    public void testSetNextNameWithNamespace() throws IOException, XMLStreamException {
        JsonGenerator jsonGenerator = new StaxMappedStreamContext();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        xmlSerializerProvider._setNextName(jsonGenerator, new QName("testName", "http://test.com"));
        String expectedNextName = "<testName xmlns=\"http://test.com\">";
        String actualNextName = jsonGenerator.getXMLStreamWriter().getText();
        Assert.assertEquals(expectedNextName, actualNextName);
    }

    @Test
    public void testSetNextNameAlreadySet() throws IOException, XMLStreamException {
        JsonGenerator jsonGenerator = new StaxMappedStreamContext();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        xmlSerializerProvider._setNextName(jsonGenerator, new QName("testName"));
        xmlSerializerProvider._setNextName(jsonGenerator, new QName("testName2"));
        String expectedNextName = "<testName xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\">";
        String actualNextName = jsonGenerator.getXMLStreamWriter().getText();
        Assert.assertEquals(expectedNextName, actualNextName);
    }

    @Test
    public void testSetNextNameIfMissing() throws IOException, XMLStreamException {
        JsonGenerator jsonGenerator = new StaxMappedStreamContext();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        boolean isRoot = jsonGenerator.inRoot();
        xmlSerializerProvider._setNextName(jsonGenerator, new QName("testName"));
        Assert.assertEquals(isRoot, jsonGenerator.inRoot());
    }

    @Test
    public void testSetNextNameIfMissingAndRoot() throws IOException, XMLStreamException {
        JsonGenerator jsonGenerator = new StaxMappedStreamContext();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        xmlSerializerProvider._setNextNameIfMissing(jsonGenerator, new QName("testName"));
        String expectedNextName = "<testName xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\">";
        String actualNextName = jsonGenerator.getXMLStreamWriter().getText();
        Assert.assertEquals(expectedNextName, actualNextName);
    }

    @Test
    public void testSetNextNameIfMissingAndNotRoot() throws IOException, XMLStreamException {
        JsonGenerator jsonGenerator = new StaxMappedStreamContext();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        xmlSerializerProvider._setNextNameIfMissing(jsonGenerator, new QName("testName"));
        String expectedNextName = "<testName>";
        String actualNextName = jsonGenerator.getXMLStreamWriter().getText();
        Assert.assertEquals(expectedNextName, actualNextName);
    }

    @Test
    public void testInitGenerator() throws IOException, XMLStreamException {
        JsonGenerator jsonGenerator = new StaxMappedStreamContext();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        xmlSerializerProvider._initGenerator(jsonGenerator);
        String expectedNextName = "<test>";
        String actualNextName = jsonGenerator.getXMLStreamWriter().getText();
        Assert.assertEquals(expectedNextName, actualNextName);
    }

    @Test
    public void testStartRootArray() throws IOException, XMLStreamException {
        JsonGenerator jsonGenerator = new StaxMappedStreamContext();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        xmlSerializerProvider._startRootArray(jsonGenerator, new QName("testName"));
        String expectedStartArray = "<testName><item>";
        String actualStartArray = jsonGenerator.getXMLStreamWriter().getText();
        Assert.assertEquals(expectedStartArray, actualStartArray);
    }

    @Test
    public void testRootName() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        QName rootName = xmlSerializerProvider._rootNameFromConfig();
        Assert.assertNull(rootName);
    }

    @Test
    public void testRootNameWithConfiguredRootName() {
        SerializationConfig config = new SerializationConfig();
        config.setRootName("testRootName");
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup, config);
        QName rootName = xmlSerializerProvider._rootNameFromConfig();
        Assert.assertEquals(new QName("testRootName"), rootName);
    }
}