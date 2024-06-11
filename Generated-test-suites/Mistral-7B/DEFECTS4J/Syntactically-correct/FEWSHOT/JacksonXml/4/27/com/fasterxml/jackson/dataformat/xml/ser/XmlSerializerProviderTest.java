package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testSerialization() throws IOException, JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        SerializerFactory factory = new DefaultSerializerFactory();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonSerializer<Object> serializer = factory.findValueSerializer(Object.class, null);
        SerializerProvider serializedProvider = provider.createInstance(config, factory);
        ObjectWriter writer = new JsonFactory(config).writerWithRootValueSerializer(serializer);

        // Test serialization of null value
        String xml = serializedProvider.serializeValueAsString(null);
        assertEquals("<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>", xml);

        // Test serialization of simple Java object
        JavaType javaType = new TypeFactory().constructType(String.class);
        Object value = "test";
        xml = serializedProvider.serializeValueAsString(value, javaType);
        assertEquals("<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>", xml);

        // Test serialization of Java object with custom root name
        PropertyName rootNameProperty = new PropertyName("rootName");
        config.setRootValueSerializer(new RootNameSerializer(rootNameProperty.getName()));
        xml = serializedProvider.serializeValueAsString(value, javaType);
        assertEquals("<rootName><test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/></rootName>", xml);
    }

    @Test
    public void testSerializationWithArray() throws IOException, JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        SerializerFactory factory = new DefaultSerializerFactory();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonSerializer<Object> serializer = factory.findValueSerializer(Object[].class, null);
        SerializerProvider serializedProvider = provider.createInstance(config, factory);
        ObjectWriter writer = new JsonFactory(config).writerWithRootValueSerializer(serializer);

        Object value = new Object[]{"test1", "test2"};
        String xml = serializedProvider.serializeValueAsString(value, new TypeFactory().constructType(Object[].class));
        assertEquals("<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n<item><test1 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/></item>\n<item><test2 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/></item>\n</item>", xml);
    }

    @Test
    public void testSerializationWithArrayAndCustomRootName() throws IOException, JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        SerializerFactory factory = new DefaultSerializerFactory();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonSerializer<Object> serializer = factory.findValueSerializer(Object[].class, null);
        SerializerProvider serializedProvider = provider.createInstance(config, factory);
        PropertyName rootNameProperty = new PropertyName("rootName");
        config.setRootValueSerializer(new RootNameSerializer(rootNameProperty.getName()));

        Object value = new Object[]{"test1", "test2"};
        String xml = serializedProvider.serializeValueAsString(value, new TypeFactory().constructType(Object[].class));
        assertEquals("<rootName xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n<item><test1 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/></item>\n<item><test2 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/></item>\n</item>\n</rootName>", xml);
    }
}

class RootNameSerializer extends JsonSerializer<Object> {
    private final String rootName;

    public RootNameSerializer(String rootName) {
        this.rootName = rootName;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName(rootName);
        gen.writeValue(value);
        gen.writeEndObject();
    }
}