import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValue() throws Exception {
        ObjectMapper mapper = new XmlMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup(config);
        SerializerProvider provider = new XmlSerializerProvider(rootNames);

        // Test serializing a String
        String value = "Hello World";
        ToXmlGenerator xgen = _asXmlGenerator(provider.getFactory().createGenerator());
        provider.serializeValue(xgen, value);
        String result = StaxUtil.convertToText(xgen.getWriter());
        assertEquals("<HelloWorld xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\""+XmlSerializerProvider.XML_NAMESPACE+"\">Hello World</HelloWorld>", result);

        // Test serializing a Number
        int number = 42;
        provider.serializeValue(xgen, number);
        result = StaxUtil.convertToText(xgen.getWriter());
        assertEquals("<int xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\""+XmlSerializerProvider.XML_NAMESPACE+"\">42</int>", result);

        // Test serializing a boolean
        boolean booleanValue = true;
        provider.serializeValue(xgen, booleanValue);
        result = StaxUtil.convertToText(xgen.getWriter());
        assertEquals("<boolean xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\""+XmlSerializerProvider.XML_NAMESPACE+"\">true</boolean>", result);

        // Test serializing a null value
        provider.serializeValue(xgen, null);
        result = StaxUtil.convertToText(xgen.getWriter());
        assertEquals("<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\""+XmlSerializerProvider.XML_NAMESPACE+"\"/>", result);

        // Test serializing a custom object with root name
        Object customObject = new CustomObject("Test");
        QName rootName = new QName("testRoot");
        provider.setConfig(config);
        config.setRootValueSeparator("true");
        config.setRootName(rootName);
        provider.serializeValue(xgen, customObject);
        result = StaxUtil.convertToText(xgen.getWriter());
        assertEquals("<testRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\""+XmlSerializerProvider.XML_NAMESPACE+"\"><name>Test</name></testRoot>", result);

        // Test serializing a custom object without root name
        provider.setConfig(config);
        provider.serializeValue(xgen, customObject);
        result = StaxUtil.convertToText(xgen.getWriter());
        assertEquals("<CustomObject xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\""+XmlSerializerProvider.XML_NAMESPACE+"\"><name>Test</name></CustomObject>", result);

        // Test exception handling
        try {
            provider.serializeValue(xgen, new Exception("Test Exception"));
        } catch (JsonMappingException e) {
            assertEquals("No serializer found for class java.lang.Exception and no properties discovered to serialize", e.getMessage());
        }

        try {
            provider.serializeValue(xgen, new CustomObject("Test"));
            xgen.getWriter().close();
            provider.serializeValue(xgen, new CustomObject("Test"));
        } catch (IOException e) {
            assertEquals("The Writer is already closed.", e.getMessage());
        }

        // Test serializing an array
        Object[] array = new Object[]{"Element1", "Element2"};
        provider.serializeValue(xgen, array);
        result = StaxUtil.convertToText(xgen.getWriter());
        assertEquals("<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\""+XmlSerializerProvider.XML_NAMESPACE+"\">Element1</item><item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\""+XmlSerializerProvider.XML_NAMESPACE+"\">Element2</item>", result);

        // Test serializing a map
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("Key1", "Value1");
        map.put("Key2", "Value2");
        provider.setConfig(config);
        config.setRootValueSeparator("{");
        config.setRootName(new QName("testMap"));
        provider.serializeValue(xgen, map);
        result = StaxUtil.convertToText(xgen.getWriter());
        assertEquals("<testMap xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\""+XmlSerializerProvider.XML_NAMESPACE+"\">{<item name=\"Key1\"><value>Value1</value></item><item name=\"Key2\"><value>Value2</value></item></testMap>", result);
    }

    private ToXmlGenerator _asXmlGenerator(JsonGenerator gen) throws JsonMappingException {
        if (gen == null || !(gen instanceof ToXmlGenerator)) {
            throw new JsonMappingException(gen, "Unexpected generator type: " + gen.getClass().getName());
        }
        return (ToXmlGenerator) gen;
    }

    static class CustomObject {
        private String name;

        public CustomObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static class NameSerializer extends StdSerializer<CustomObject> {
            public NameSerializer() {
                this(null);
            }

            public NameSerializer(Class<CustomObject> t) {
                this(null);
            }

            public NameSerializer(Class<CustomObject> t, PropertyName pn) {
                super(t, pn);
            }

            @Override
            public void serialize(CustomObject value, ToXmlGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeStartElement("name");
                gen.writeText(value.getName());
                gen.writeEndElement();
            }
        }

        static {
            SimpleBeanPropertySerializerBase.findAndRegisterSerializer(CustomObject.class, new NameSerializer());
        }
    }
}