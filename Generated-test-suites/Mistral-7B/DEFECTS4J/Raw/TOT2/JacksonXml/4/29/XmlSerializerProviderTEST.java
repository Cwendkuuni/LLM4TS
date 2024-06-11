package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializerBase;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.util.JacksonXMLTestBase;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest extends JacksonXMLTestBase {

    @Test
    public void testSerializeNull() throws Exception {
        XmlMapper mapper = new XmlMapper(new XmlSerializerProvider(new XmlRootNameLookup()));
        ObjectNode root = mapper.readValue(getClass().getResource("/test-null.xml"), ObjectNode.class);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<null/>", root.toString());

        SerializerProvider sp = new XmlSerializerProvider(new XmlRootNameLookup());
        JsonGenerator gen = sp.getFactory().createGenerator(new TokenBuffer(new StringBuilder()), getObjectMapper().getConfig());
        sp.serializeValue(gen, null);
        assertEquals("<null/>", gen.getOutputContent().toString());
    }

    @Test
    public void testSerializeValue() throws Exception {
        XmlMapper mapper = new XmlMapper(new XmlSerializerProvider(new XmlRootNameLookup()));
        ObjectNode root = mapper.readValue(getClass().getResource("/test-value.xml"), ObjectNode.class);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<value>Hello World!</value>\n</root>", root.toString());

        SerializerProvider sp = new XmlSerializerProvider(new XmlRootNameLookup());
        JsonSerializer<String> serializer = sp.findValueSerializer(String.class, null);
        ObjectNode root2 = mapper.valueToTree("Hello World!");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonFactory factory = new JsonFactory();
        JsonGenerator gen = factory.createGenerator(baos);
        serializer.serialize(new SimpleObject(new String("Hello World!")), gen, sp);
        gen.close();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<value>Hello World!</value>\n</root>", baos.toString());
    }

    @Test
    public void testSerializeValueWithRootType() throws Exception {
        XmlMapper mapper = new XmlMapper(new XmlSerializerProvider(new XmlRootNameLookup()));
        JavaType rootType = mapper.getTypeFactory().constructType("RootType");
        ObjectNode root = mapper.readValue(getClass().getResource("/test-value-with-root-type.xml"), ObjectNode.class);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<RootType>\n<value>Hello World!</value>\n</RootType>", root.toString());

        SerializerProvider sp = new XmlSerializerProvider(new XmlRootNameLookup());
        JsonSerializer<RootType> serializer = sp.findValueSerializer(RootType.class, null);
        ObjectNode root2 = mapper.valueToTree(new RootType());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonFactory factory = new JsonFactory();
        JsonGenerator gen = factory.createGenerator(baos);
        serializer.serialize(new RootType(), gen, sp);
        gen.close();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<RootType>\n<value></value>\n</RootType>", baos.toString());
    }

    @Test
    public void testSerializeValueWithSerializer() throws Exception {
        XmlMapper mapper = new XmlMapper(new XmlSerializerProvider(new XmlRootNameLookup()));
        JavaType rootType = mapper.getTypeFactory().constructType("RootType");
        ObjectNode root = mapper.readValue(getClass().getResource("/test-value-with-root-type.xml"), ObjectNode.class);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<RootType>\n<value>Hello World!</value>\n</RootType>", root.toString());

        SerializerProvider sp = new XmlSerializerProvider(new XmlRootNameLookup());
        JsonSerializer<RootType> serializer = sp.findValueSerializer(RootType.class, null);
        ObjectNode root2 = mapper.valueToTree(new RootType());
        JsonSerializer<String> customSerializer = new CustomSerializer();
        serializer.setNullValueSerializer(customSerializer);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonFactory factory = new JsonFactory();
        JsonGenerator gen = factory.createGenerator(baos);
        serializer.serialize(new RootType(), gen, sp);
        gen.close();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<RootType>\n<value>null</value>\n</RootType>", baos.toString());
    }

    @Test(expected = IOException.class)
    public void testSerializeException() throws Exception {
        XmlMapper mapper = new XmlMapper(new XmlSerializerProvider(new XmlRootNameLookup()));
        ObjectNode root = mapper.readValue(getClass().getResource("/test-exception.xml"), ObjectNode.class);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<value>Hello World!</value>\n</root>", root.toString());

        SerializerProvider sp = new XmlSerializerProvider(new XmlRootNameLookup());
        try {
            JsonSerializer<String> serializer = sp.findValueSerializer(String.class, null);
            serializer.serialize(new String("Hello World!"), null, sp);
        } catch (JsonProcessingException e) {
            fail("Expected IOException but got JsonProcessingException: " + e.getMessage());
        }
        throw new IOException("Unexpected");
    }

    @Test
    public void testDeserializeNull() throws Exception {
        XmlMapper mapper = new XmlMapper(new XmlSerializerProvider(new XmlRootNameLookup()));
        ObjectNode root = mapper.readValue(getClass().getResource("/test-null.xml"), ObjectNode.class);
        assertNull(root.get("value"));

        SerializerProvider sp = new XmlSerializerProvider(new XmlRootNameLookup());
        TokenBuffer tokenBuffer = new TokenBuffer(new StringBuilder());
        JsonParser p = sp.getFactory().createParser(getClass().getResource("/test-null.xml").openStream());
        p.nextToken();
        Object result = sp.readValue(p, null);
        assertNull(result);
    }

    @Test
    public void testDeserializeValue() throws Exception {
        XmlMapper mapper = new XmlMapper(new XmlSerializerProvider(new XmlRootNameLookup()));
        ObjectNode root = mapper.readValue(getClass().getResource("/test-value.xml"), ObjectNode.class);
        assertEquals("Hello World!", root.get("value").asText());

        SerializerProvider sp = new XmlSerializerProvider(new XmlRootNameLookup());
        ByteArrayInputStream bais = new ByteArrayInputStream(getClass().getResource("/test-value.xml").getBytes());
        JsonParser p = sp.getFactory().createParser(bais);
        Object result = sp.readValue(p, String.class);
        assertEquals("Hello World!", result);
    }

    @Test
    public void testDeserializeValueWithRootType() throws Exception {
        XmlMapper mapper = new XmlMapper(new XmlSerializerProvider(new XmlRootNameLookup()));
        ObjectNode root = mapper.readValue(getClass().getResource("/test-value-with-root-type.xml"), ObjectNode.class);
        RootType result = mapper.readValue(root, RootType.class);
        assertEquals("Hello World!", result.getValue());

        SerializerProvider sp = new XmlSerializerProvider(new XmlRootNameLookup());
        ByteArrayInputStream bais = new ByteArrayInputStream(getClass().getResource("/test-value-with-root-type.xml").getBytes());
        JsonParser p = sp.getFactory().createParser(bais);
        Object result = sp.readValue(p, RootType.class);
        assertEquals("Hello World!", ((RootType) result).getValue());
    }

    @Test
    public void testDeserializeValueWithSerializer() throws Exception {
        XmlMapper mapper = new XmlMapper(new XmlSerializerProvider(new XmlRootNameLookup()));
        ObjectNode root = mapper.readValue(getClass().getResource("/test-value-with-root-type.xml"), ObjectNode.class);
        RootType result = mapper.readValue(root, RootType.class);
        assertEquals("Hello World!", result.getValue());

        SerializerProvider sp = new XmlSerializerProvider(new XmlRootNameLookup());
        CustomSerializer customSerializer = new CustomSerializer();
        JsonSerializer<RootType> serializer = new SimpleBeanPropertySerializerBase<>(RootType.class, null, null, customSerializer, null, null);
        ByteArrayInputStream bais = new ByteArrayInputStream(getClass().getResource("/test-value-with-root-type.xml").getBytes());
        JsonParser p = sp.getFactory().createParser(bais);
        Object result = sp.readValue(p, RootType.class);
        assertEquals("Hello World!", ((RootType) result).getValue());
    }

    @Test
    public void testDeserializeException() throws Exception {
        XmlMapper mapper = new XmlMapper(new XmlSerializerProvider(new XmlRootNameLookup()));
        ObjectNode root = mapper.readValue(getClass().getResource("/test-exception.xml"), ObjectNode.class);
        assertEquals("Hello World!", root.get("value").asText());

        SerializerProvider sp = new XmlSerializerProvider(new XmlRootNameLookup());
        ByteArrayInputStream bais = new ByteArrayInputStream(getClass().getResource("/test-exception.xml").getBytes());
        JsonParser p = sp.getFactory().createParser(bais);
        try {
            p.nextToken();
            sp.readValue(p, String.class);
        } catch (JsonMappingException e) {
            assertEquals("Caught expected JsonMappingException", e.getMessage());
        }
    }

    static class SimpleObject {
        String value;

        SimpleObject(String value) {
            this.value = value;
        }
    }

    static class RootType {
        String value;

        public RootType() {}

        public String getValue() {
            return value;
        }
    }

    static class CustomSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString("null");
        }
    }
}