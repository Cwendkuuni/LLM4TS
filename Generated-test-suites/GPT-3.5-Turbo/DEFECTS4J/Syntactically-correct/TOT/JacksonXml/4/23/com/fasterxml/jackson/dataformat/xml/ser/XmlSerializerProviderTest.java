import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import javax.xml.namespace.QName;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setUp() throws Exception {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        xmlSerializerProvider = new XmlSerializerProvider(rootNames);
    }

    // Tests for serializeValue(JsonGenerator gen, Object value)

    @Test
    public void testSerializeValue_ObjectIsNull() throws IOException {
        // Arrange
        StringWriter sw = new StringWriter();
        JsonFactory jf = new JsonFactory();
        ToXmlGenerator gen = new XmlMapper(jf).createGenerator(sw);

        // Act
        xmlSerializerProvider.serializeValue(gen, null);
        gen.flush();

        // Assert
        String expectedXml = "<null/>\n";
        assertEquals(expectedXml, sw.toString());
    }

    @Test
    public void testSerializeValue_ObjectIsNotNull() throws IOException {
        // Arrange
        StringWriter sw = new StringWriter();
        JsonFactory jf = new JsonFactory();
        ToXmlGenerator gen = new XmlMapper(jf).createGenerator(sw);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("key", "value");

        // Act
        xmlSerializerProvider.serializeValue(gen, objectNode);
        gen.flush();

        // Assert
        String expectedXml = "<root>\n  <key>value</key>\n</root>\n";
        assertEquals(expectedXml, sw.toString());
    }

    // Tests for serializeValue(JsonGenerator gen, Object value, JavaType rootType)

    @Test
    public void testSerializeValue_ObjectAndRootTypeAreNull() throws IOException {
        // Arrange
        StringWriter sw = new StringWriter();
        JsonFactory jf = new JsonFactory();
        ToXmlGenerator gen = new XmlMapper(jf).createGenerator(sw);

        // Act
        xmlSerializerProvider.serializeValue(gen, null, null);
        gen.flush();

        // Assert
        String expectedXml = "<null/>\n";
        assertEquals(expectedXml, sw.toString());
    }

    @Test
    public void testSerializeValue_ObjectAndRootTypeAreNotNull() throws IOException {
        // Arrange
        StringWriter sw = new StringWriter();
        JsonFactory jf = new JsonFactory();
        ToXmlGenerator gen = new XmlMapper(jf).createGenerator(sw);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("key", "value");

        // Act
        xmlSerializerProvider.serializeValue(gen, objectNode, ObjectNode.class);
        gen.flush();

        // Assert
        String expectedXml = "<root>\n  <key>value</key>\n</root>\n";
        assertEquals(expectedXml, sw.toString());
    }

    // Tests for serializeValue(JsonGenerator gen, Object value, JavaType rootType, JsonSerializer<Object> ser)

    @Test
    public void testSerializeValue_ObjectAndRootTypeAndSerializerAreNull() throws IOException {
        // Arrange
        StringWriter sw = new StringWriter();
        JsonFactory jf = new JsonFactory();
        ToXmlGenerator gen = new XmlMapper(jf).createGenerator(sw);

        // Act
        xmlSerializerProvider.serializeValue(gen, null, null, null);
        gen.flush();

        // Assert
        String expectedXml = "<null/>\n";
        assertEquals(expectedXml, sw.toString());
    }

    @Test
    public void testSerializeValue_ObjectAndRootTypeAndSerializerAreNotNull() throws IOException {
        // Arrange
        StringWriter sw = new StringWriter();
        JsonFactory jf = new JsonFactory();
        ToXmlGenerator gen = new XmlMapper(jf).createGenerator(sw);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("key", "value");

        // Act
        xmlSerializerProvider.serializeValue(gen, objectNode, ObjectNode.class, null);
        gen.flush();

        // Assert
        String expectedXml = "<root>\n  <key>value</key>\n</root>\n";
        assertEquals(expectedXml, sw.toString());
    }
}