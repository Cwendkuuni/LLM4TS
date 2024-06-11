import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.impl.*;
import com.fasterxml.jackson.dataformat.xml.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper(new XmlFactory());
        xmlSerializerProvider = objectMapper.getSerializerProvider().findValueSerIALizers().get(0);
    }

    @Test
    public void testSerializeValue_Null() throws IOException {
        JsonGenerator jsonGenerator = new ToXmlGenerator(new StringWriter());
        xmlSerializerProvider.serializeValue(jsonGenerator, null);
        String xml = jsonGenerator.toString();
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><null/>", xml);
    }

    @Test
    public void testSerializeValue_Boolean() throws IOException {
        JsonGenerator jsonGenerator = new ToXmlGenerator(new StringWriter());
        Object value = true;
        xmlSerializerProvider.serializeValue(jsonGenerator, value);
        String xml = jsonGenerator.toString();
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><boolean>true</boolean>", xml);
    }

    @Test
    public void testSerializeValue_Integer() throws IOException {
        JsonGenerator jsonGenerator = new ToXmlGenerator(new StringWriter());
        Object value = 123;
        xmlSerializerProvider.serializeValue(jsonGenerator, value);
        String xml = jsonGenerator.toString();
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><integer>123</integer>", xml);
    }

    @Test
    public void testSerializeValue_String() throws IOException {
        JsonGenerator jsonGenerator = new ToXmlGenerator(new StringWriter());
        Object value = "Hello, World!";
        xmlSerializerProvider.serializeValue(jsonGenerator, value);
        String xml = jsonGenerator.toString();
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><string>Hello, World!</string>", xml);
    }

    @Test
    public void testSerializeValue_CustomClass() throws IOException {
        JsonGenerator jsonGenerator = new ToXmlGenerator(new StringWriter());
        Object value = new CustomClass("Test");
        xmlSerializerProvider.serializeValue(jsonGenerator, value);
        String xml = jsonGenerator.toString();
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><customClass><name>Test</name></customClass>", xml);
    }

    @Test
    public void testSerializeValue_Array() throws IOException {
        JsonGenerator jsonGenerator = new ToXmlGenerator(new StringWriter());
        Object value = new Integer[]{1, 2, 3};
        xmlSerializerProvider.serializeValue(jsonGenerator, value, JsonTypeFactory.valueToValueType(Integer[].class));
        String xml = jsonGenerator.toString();
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><array><item><integer>1</integer></item><item><integer>2</integer></item><item><integer>3</integer></item></array>", xml);
    }

    @Test
    public void testSerializeValue_RootName() throws IOException {
        JsonGenerator jsonGenerator = new ToXmlGenerator(new StringWriter());
        xmlSerializerProvider.setDefaultSerializerProvider(new XmlSerializerProvider(new XmlRootNameLookup(new QName("root"))));
        Object value = new Integer(123);
        xmlSerializerProvider.serializeValue(jsonGenerator, value);
        String xml = jsonGenerator.toString();
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><root><integer>123</integer></root>", xml);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValue_Exception() throws IOException {
        JsonGenerator jsonGenerator = new ToXmlGenerator(new StringWriter());
        Object value = new Exception("Test Exception");
        xmlSerializerProvider.serializeValue(jsonGenerator, value);
    }

    @Test(expected = IOException.class)
    public void testSerializeValue_IOException() throws IOException {
        JsonGenerator jsonGenerator = new ToXmlGenerator(new StringWriter());
        Object value = new IOException("Test IOException");
        xmlSerializerProvider.serializeValue(jsonGenerator, value);
    }

    @Test
    public void testSerializeValue_CustomSerializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new XmlFactory());
        SimpleSerializer simpleSerializer = new SimpleSerializer();
        objectMapper.registerSerializer(String.class, simpleSerializer);
        JsonGenerator jsonGenerator = new ToXmlGenerator(new StringWriter());
        Object value = "Hello, World!";
        ObjectWriter objectWriter = objectMapper.writerWithRootValueSerializer(simpleSerializer);
        objectWriter.writeValue(jsonGenerator, value);
        String xml = jsonGenerator.toString();
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><string>Hello, World!</string>", xml);
    }

    @Test
    public void testSerializeValue_RootArray() throws IOException {
        JsonGenerator jsonGenerator = new ToXmlGenerator(new StringWriter());
        Object value = new Integer[]{1, 2, 3};
        xmlSerializerProvider.serializeValue(jsonGenerator, value, JsonTypeFactory.valueToValueType(Integer[].class), null);
        String xml = jsonGenerator.toString();
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><array><item><integer>1</integer></item><item><integer>2</integer></item><item><integer>3</integer></item></array>", xml);
    }

    static class CustomClass {
        private final String name;

        public CustomClass(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class SimpleSerializer extends StdSerializer<String> {
        public SimpleSerializer() {
            this(null);
        }

        public SimpleSerializer(Class<String> t) {
            this(null, t);
        }

        public SimpleSerializer(Class<String> t, Config config) {
            super(t, config);
        }

        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value);
        }
    }
}