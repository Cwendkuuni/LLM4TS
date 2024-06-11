
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProviderTest;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

public class XmlSerializerProviderTest {

    private final XmlSerializerProvider provider;
    private final XmlMapper mapper;

    public XmlSerializerProviderTest() {
        JacksonXmlModule module = new JacksonXmlModule();
        mapper = new XmlMapper(module);

        provider = new XmlSerializerProvider(new com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup());
    }

    @Test
    public void testCreateInstance() {
        SerializerFactory factory = new DefaultSerializerProvider.Factory();
        SerializationConfig config = provider.getConfig();
        DefaultSerializerProvider result = provider.createInstance(config, factory);

        assertEquals(result, result);
    }

    @Test
    public void testSerializeValue() throws IOException {
        Object value = new Object();
        JsonGenerator gen = new ToXmlGenerator();
        provider.serializeValue(gen, value);

        assertTrue(gen instanceof ToXmlGenerator);
    }

    @Test
    public void testSerializeValueWithRootType() throws IOException {
        Object value = new Object();
        JavaType rootType = mapper.constructType(Object.class);
        JsonGenerator gen = new ToXmlGenerator();

        provider.serializeValue(gen, value, rootType);

        assertTrue(gen instanceof ToXmlGenerator);
    }

    @Test
    public void testSerializeValueWithRootTypeAndSerializer() throws IOException {
        Object value = new Object();
        JavaType rootType = mapper.constructType(Object.class);
        JsonSerializer<Object> ser = new DefaultSerializerProvider.Nuller(true);
        JsonGenerator gen = new ToXmlGenerator();

        provider.serializeValue(gen, value, rootType, ser);

        assertTrue(gen instanceof ToXmlGenerator);
    }

    @Test
    public void testInitWithRootName() throws IOException {
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName("testRootName");
        provider._initWithRootName(xgen, rootName);

        assertTrue(xgen.getStaxWriter().getNamespaceContext().getNamespaceURI(0).isEmpty());
    }

    @Test
    public void testInitWithRootNameAndNamespace() throws IOException {
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName("testRootName", "http://www.example.com/");
        provider._initWithRootName(xgen, rootName);

        assertEquals("http://www.example.com/",
                xgen.getStaxWriter().getNamespaceContext().getNamespaceURI(0));
    }

    @Test
    public void testSerializeXmlNull() throws IOException {
        JsonGenerator jgen = new ToXmlGenerator();

        provider._serializeXmlNull(jgen);

        assertNull(jgen.getOutputContext());
    }

    @Test
    public void testStartRootArray() throws IOException {
        QName rootName = new QName("testRootName");
        ToXmlGenerator xgen = new ToXmlGenerator();

        provider._startRootArray(xgen, rootName);

        assertTrue(xgen.getOutputContext().inRoot());
    }

    @Test
    public void testRootNameFromConfig() {
        PropertyName name = new PropertyName("testRootName");
        provider.BASE._config = provider.getConfig().withRootName(name);

        QName result = provider._rootNameFromConfig();

        assertEquals(name.getLocalName(), result.getLocalPart());
    }

    @Test
    public void testAsXmlGenerator() throws JsonMappingException {
        JsonGenerator gen = new ToXmlGenerator();
        ToXmlGenerator result = provider._asXmlGenerator(gen);

        assertEquals(gen, result);

        try {
            gen = new TokenBuffer();
            provider._asXmlGenerator(gen);
        } catch (Exception e) {
            assertEquals(JsonMappingException.class, e.getClass());
        }
    }

}
