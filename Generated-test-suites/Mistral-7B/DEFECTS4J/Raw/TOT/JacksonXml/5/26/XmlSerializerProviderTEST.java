import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

    private ObjectMapper objectMapper;
    private XmlMapper xmlMapper;
    private XmlRootNameLookup rootNameLookup;

    @Before
    public void setup() {
        rootNameLookup = new XmlRootNameLookup();
        objectMapper = new ObjectMapper();
        xmlMapper = new XmlMapper(new SerializationConfig(objectMapper.getConfig())
                .withSerializerProvider(new XmlSerializerProvider(rootNameLookup)));
    }

    @Test
    public void testSerializeNull() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = xmlMapper.getFactory().createGenerator(stringWriter);

        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        provider.serializeValue(jsonGenerator, null);
        jsonGenerator.flush();

        String actual = stringWriter.toString();
        String expected = "<null></null>";

        assertEquals(expected, actual);
    }

    @Test
    public void testSerializePrimitiveType() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = xmlMapper.getFactory().createGenerator(stringWriter);

        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        provider.serializeValue(jsonGenerator, 42);
        jsonGenerator.flush();

        String actual = stringWriter.toString();
        String expected = "<int>42</int>";

        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeCustomType() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = xmlMapper.getFactory().createGenerator(stringWriter);

        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);

        MyClass myClass = new MyClass("Test");
        provider.serializeValue(jsonGenerator, myClass);
        jsonGenerator.flush();

        String actual = stringWriter.toString();
        String expected = "<myClass><name>Test</name></myClass>";

        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeArray() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = xmlMapper.getFactory().createGenerator(stringWriter);

        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);

        MyClass[] myClasses = new MyClass[] {new MyClass("Test1"), new MyClass("Test2")};
        provider.serializeValue(jsonGenerator, myClasses);
        jsonGenerator.flush();

        String actual = stringWriter.toString();
        String expected = "<myClass>\n<item>\n<name>Test1</name>\n</item>\n<item>\n<name>Test2</name>\n</item>\n</myClass>";

        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeException() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = xmlMapper.getFactory().createGenerator(stringWriter);

        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);

        String test = null;
        provider.serializeValue(jsonGenerator, test);
    }

    @Test
    public void testCopy() {
        XmlSerializerProvider originalProvider = new XmlSerializerProvider(rootNameLookup);
        XmlSerializerProvider copyProvider = originalProvider.copy();

        assertNotSame(originalProvider, copyProvider);
        assertEquals(originalProvider, copyProvider.getRootValueSerializer(String.class));
    }

    @Test
    public void testCreateInstance() {
        XmlSerializerProvider configProvider = new XmlSerializerProvider(rootNameLookup);
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(configProvider, new SerializationConfig(configProvider.getConfig()), null);

        assertNotSame(configProvider, serializerProvider);
        assertEquals(configProvider, serializerProvider.getRootValueSerializer(String.class));
    }

    @Test
    public void testOverrideMethods() {
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);

        JsonGenerator generator = provider.getFactory().createGenerator(new StringWriter());

        Object value = new Object();
        provider.serializeValue(generator, value);

        assertEquals(DefaultSerializerProvider.class, provider.getClass());
        assertEquals(provider, provider.copy().getClass());
        assertEquals(provider, provider.createInstance(new SerializationConfig(provider.getConfig()), null).getClass());
        assertEquals(provider, provider.createInstance(provider).getClass());
    }

    static class MyClass {
        private final String name;

        public MyClass(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}