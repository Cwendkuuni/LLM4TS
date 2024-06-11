package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.ToXmlGenerator;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValueNull() throws Exception {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{}, new String[0]));
        JsonGenerator gen = new TokenBuffer(new StringWriter());
        provider.serializeValue(gen, null);
        assertEquals("<null/>\n", gen.getBufferAsString());
        gen.close();
    }

    @Test
    public void testSerializeValueWithRootName() throws Exception {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup(new String[]{"root"}, new String[]{"http://example.com"});
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        ObjectNode rootNode = new ObjectMapper().readValue("{\"root\":{\"value\":\"test\"}}", ObjectNode.class);
        JsonGenerator gen = new TokenBuffer(new StringWriter());
        provider.serializeValue(gen, rootNode);
        assertEquals("<root xmlns:xsi=\"http://example.com\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><value>test</value></root>\n", gen.getBufferAsString());
        gen.close();
    }

    @Test
    public void testSerializeValueWithRootNameFromConfig() throws Exception {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup(new String[]{}, new String[0]);
        XmlSerializerProvider provider = new XmlSerializerProvider(new SerializationConfig(new DefaultSerializerProvider()), rootNameLookup);
        ObjectNode rootNode = new ObjectMapper().readValue("{\"@xml.rootElement\":\"root\"}", ObjectNode.class);
        JsonGenerator gen = new TokenBuffer(new StringWriter());
        provider.serializeValue(gen, rootNode);
        assertEquals("<root/>\n", gen.getBufferAsString());
        gen.close();
    }

    @Test
    public void testSerializeValueWithArray() throws Exception {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup(new String[]{"root"}, new String[]{"http://example.com"});
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        ObjectNode rootNode = new ObjectMapper().readValue("{\"root\":[1, 2, 3]}", ObjectNode.class);
        JsonGenerator gen = new TokenBuffer(new StringWriter());
        provider.serializeValue(gen, rootNode);
        assertEquals("<root xmlns:xsi=\"http://example.com\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><item></item><item></item><item></item></root>\n", gen.getBufferAsString());
        gen.close();
    }

    @Test
    public void testSerializeValueWithArrayAndCustomRootName() throws Exception {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup(new String[]{"myroot"}, new String[0]);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        ObjectNode rootNode = new ObjectMapper().readValue("{\"myroot\":[1, 2, 3]}", ObjectNode.class);
        JsonGenerator gen = new TokenBuffer(new StringWriter());
        provider.serializeValue(gen, rootNode);
        assertEquals("<myroot><item></item><item></item><item></item></myroot>\n", gen.getBufferAsString());
        gen.close();
    }

    @Test
    public void testSerializeValueWithCustomSerializer() throws Exception {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup(new String[]{"myroot"}, new String[0]);
        ObjectMapper mapper = new XmlMapper();
        mapper.registerSerializer(String.class, new StringXmlSerializer());
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("myroot", "test");
        JsonGenerator gen = new TokenBuffer(new StringWriter());
        provider.serializeValue(gen, rootNode);
        assertEquals("<myroot>test</myroot>\n", gen.getBufferAsString());
        gen.close();
    }

    @Test
    public void testSerializeValueWithCustomRootNameAndCustomSerializer() throws Exception {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup(new String[]{"myroot"}, new String[0]);
        ObjectMapper mapper = new XmlMapper();
        mapper.registerSerializer(String.class, new StringXmlSerializer());
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("myroot", mapper.valueToTree("test"));
        JsonGenerator gen = new TokenBuffer(new StringWriter());
        provider.serializeValue(gen, rootNode);
        assertEquals("<myroot>test</myroot>\n", gen.getBufferAsString());
        gen.close();
    }

    static class StringXmlSerializer extends JsonSerializer<String> {
        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value);
        }
    }
}