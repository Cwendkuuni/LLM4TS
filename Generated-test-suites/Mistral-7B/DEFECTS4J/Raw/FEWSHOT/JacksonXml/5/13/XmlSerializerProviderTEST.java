package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializerBase;
import com.fasterxml.jackson.databind.util.JsonTestUtils;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import org.junit.Test;

public class XmlSerializerProviderTest {

    private static final ObjectNode createEmptyObjectNode() {
        ObjectMapper mapper = new ObjectMapper(new XmlFactory());
        return mapper.createObjectNode();
    }

    @Test
    public void test_newXmlSerializerProvider() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        assertNull(provider.getRootName());
    }

    @Test
    public void test_newXmlSerializerProviderWithRootName() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        QName rootName = new QName("root");
        provider.setRootName(rootName);
        assertEquals(rootName, provider.getRootName());
    }

    @Test
    public void test_copy() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        XmlSerializerProvider copiedProvider = provider.copy();
        assertEquals(provider._rootNameLookup, copiedProvider._rootNameLookup);
    }

    @Test
    public void test_serializeValue_null() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = StaxUtil.createRootXmlGenerator();
        provider.serializeValue(gen, null);
        String xml = StaxUtil.toXmlString(gen);
        assertEquals("<null/>", xml);
    }

    @Test
    public void test_serializeValue_simpleBean() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        ObjectMapper objectMapper = new ObjectMapper(provider);
        MyBean bean = new MyBean();
        ObjectNode rootNode = objectMapper.writeValueAsTree(bean);
        JsonGenerator gen = StaxUtil.createRootXmlGenerator();
        provider.serializeValue(gen, bean);
        String xml = StaxUtil.toXmlString(gen);
        String expectedXml = rootNode.toString();
        assertEquals(expectedXml, xml);
    }

    @Test
    public void test_serializeValue_javaType() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        ObjectMapper objectMapper = new ObjectMapper(provider);
        JavaType javaType = objectMapper.getTypeFactory().constructType(MyBean[].class);
        ObjectNode rootNode = objectMapper.readValue(MyBean_arrayXml, javaType);
        JsonGenerator gen = StaxUtil.createRootXmlGenerator();
        provider.serializeValue(gen, rootNode);
        String xml = StaxUtil.toXmlString(gen);
        String expectedXml = rootNode.toString();
        assertEquals(expectedXml, xml);
    }

    @Test
    public void test_serializeValue_serializer() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        SimpleBeanPropertySerializerBase mySerializer = new SimpleBeanPropertySerializerBase() {
            @Override
            protected void serializeFields(Object bean, JsonGenerator generator, SerializerProvider serializers) throws IOException {
                generator.writeFieldName("field1");
                generator.writeValue("value1");
            }
        };
        JsonSerializer<MyBean> serializer = new JsonSerializer<MyBean>() {
            @Override
            public void serialize(MyBean value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
                mySerializer.serializeFields(value, gen, serializerProvider);
            }
        };
        ObjectMapper objectMapper = new ObjectMapper(provider);
        MyBean bean = new MyBean();
        JsonSerializer<Object> serializerProviderSerializer = objectMapper.findValueSerializer(MyBean.class, null);
        assertEquals(serializerProviderSerializer, serializer);
        provider.setSerializerInstance(MyBean.class, serializer);
        ObjectNode rootNode = objectMapper.writeValueAsTree(bean);
        JsonGenerator gen = StaxUtil.createRootXmlGenerator();
        provider.serializeValue(gen, bean, null, serializer);
        String xml = StaxUtil.toXmlString(gen);
        String expectedXml = rootNode.toString();
        assertEquals(expectedXml, xml);
    }

    @Test(expected = JsonMappingException.class)
    public void test_serializeValue_nonToXmlGenerator() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper.DefaultSerializerProvider().getDefaultSerializerProvider());
        provider.serializeValue(gen, new Object());
    }

    @Test
    public void test_initWithRootName() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        ToXmlGenerator xgen = StaxUtil.createRootXmlGenerator();
        provider._initWithRootName(xgen, new QName("root"));
        String ns = xgen.getStaxWriter().getDefaultNamespace();
        assertTrue(ns != null && ns.isEmpty());
    }

    @Test
    public void test_initWithRootName_withNamespace() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        ToXmlGenerator xgen = StaxUtil.createRootXmlGenerator();
        provider._initWithRootName(xgen, new QName("root", "ns"));
        String ns = xgen.getStaxWriter().getDefaultNamespace();
        assertEquals("ns", ns);
    }

    @Test
    public void test_initWithRootName_existingName() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        ToXmlGenerator xgen = StaxUtil.createRootXmlGenerator();
        provider._initWithRootName(xgen, new QName("root"));
        provider._initWithRootName(xgen, new QName("root"));
        String ns = xgen.getStaxWriter().getDefaultNamespace();
        assertTrue(ns != null && ns.isEmpty());
    }

    @Test
    public void test_startRootArray() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        ToXmlGenerator xgen = StaxUtil.createRootXmlGenerator();
        provider._startRootArray(xgen, new QName("root"));
        String xml = StaxUtil.toXmlString(xgen);
        assertEquals("<root><item/></root>", xml);
    }

    @Test
    public void test_serializeNullRootNameFromConfig() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        provider.setSerializationConfig(new SerializationConfig(new ObjectMapper.DefaultSerializerProvider().getDefaultSerializerProvider().getDefaultSerializerProvider()));
        provider.getConfig().setRootValueSeparator("");
        JsonGenerator gen = StaxUtil.createRootXmlGenerator();
        provider.serializeValue(gen, null);
        String xml = StaxUtil.toXmlString(gen);
        assertEquals("", xml);
    }

    @Test
    public void test_serializeEmptyRootNameFromConfig() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        provider.setSerializationConfig(new SerializationConfig(new ObjectMapper.DefaultSerializerProvider().getDefaultSerializerProvider().getDefaultSerializerProvider()));
        provider.getConfig().setRootValueSeparator("root");
        JsonGenerator gen = StaxUtil.createRootXmlGenerator();
        provider.serializeValue(gen, null);
        String xml = StaxUtil.toXmlString(gen);
        assertEquals("<root/>", xml);
    }

    @Test
    public void test_serializeRootNameFromConfig() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        ObjectMapper objectMapper = new ObjectMapper(provider);
        objectMapper.setSerializationConfig(new SerializationConfig(new ObjectMapper.DefaultSerializerProvider().getDefaultSerializerProvider().getDefaultSerializerProvider()));
        objectMapper.getConfig().setRootValueSeparator("root");
        MyBean bean = new MyBean();
        ObjectNode rootNode = objectMapper.writeValueAsTree(bean);
        JsonGenerator gen = StaxUtil.createRootXmlGenerator();
        provider.serializeValue(gen, bean);
        String xml = StaxUtil.toXmlString(gen);
        String expectedXml = rootNode.toString();
        assertEquals(expectedXml, xml);
    }

    @Test
    public void test_serializeRootNameFromConfig_withNamespace() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        ObjectMapper objectMapper = new ObjectMapper(provider);
        objectMapper.setSerializationConfig(new SerializationConfig(new ObjectMapper.DefaultSerializerProvider().getDefaultSerializerProvider().getDefaultSerializerProvider()));
        objectMapper.getConfig().setRootValueSeparator("root");
        String namespace = "ns";
        objectMapper.getConfig().getDefaultSerializerProvider().setDefaultRootElementNamespace(new QName(namespace));
        MyBean bean = new MyBean();
        ObjectNode rootNode = objectMapper.writeValueAsTree(bean);
        JsonGenerator gen = StaxUtil.createRootXmlGenerator();
        provider.serializeValue(gen, bean);
        String xml = StaxUtil.toXmlString(gen);
        String expectedXml = rootNode.toString();
        assertEquals(expectedXml, xml);
        assertEquals(namespace, gen.getStaxWriter().getDefaultNamespace());
    }

    @Test
    public void test_serializeRootNameFromConfig_existingName() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        ObjectMapper objectMapper = new ObjectMapper(provider);
        objectMapper.setSerializationConfig(new SerializationConfig(new ObjectMapper.DefaultSerializerProvider().getDefaultSerializerProvider().getDefaultSerializerProvider()));
        objectMapper.getConfig().setRootValueSeparator("root");
        MyBean bean = new MyBean();
        ObjectNode rootNode = objectMapper.writeValueAsTree(bean);
        JsonGenerator gen = StaxUtil.createRootXmlGenerator();
        provider.setRootName(new QName("root"));
        provider.serializeValue(gen, bean);
        String xml = StaxUtil.toXmlString(gen);
        String expectedXml = rootNode.toString();
        assertEquals(expectedXml, xml);
    }

    @Test
    public void test_serializeRootNameFromConfig_rootNameFromObjectMapper() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        ObjectMapper objectMapper = new ObjectMapper(provider);
        objectMapper.setSerializationConfig(new SerializationConfig(new ObjectMapper.DefaultSerializerProvider().getDefaultSerializerProvider().getDefaultSerializerProvider()));
        objectMapper.getConfig().setRootValueSeparator("root");
        MyBean bean = new MyBean();
        ObjectNode rootNode = objectMapper.writeValueAsTree(bean);
        JsonGenerator gen = StaxUtil.createRootXmlGenerator();
        ObjectNode rootNodeFromObjectMapper = objectMapper.readValue(rootNode.toString(), ObjectNode.class);
        provider.setRootName(rootNodeFromObjectMapper);
        provider.serializeValue(gen, bean);
        String xml = StaxUtil.toXmlString(gen);
        String expectedXml = rootNode.toString();
        assertEquals(expectedXml, xml);
    }

    @Test
    public void test_serializeRootNameFromConfig_rootNameFromObjectMapperWithNamespace() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        ObjectMapper objectMapper = new ObjectMapper(provider);
        objectMapper.setSerializationConfig(new SerializationConfig(new ObjectMapper.DefaultSerializerProvider().getDefaultSerializerProvider().getDefaultSerializerProvider()));
        objectMapper.getConfig().setRootValueSeparator("root");
        String namespace = "ns";
        objectMapper.getConfig().getDefaultSerializerProvider().setDefaultRootElementNamespace(new QName(namespace));
        MyBean bean = new MyBean();
        ObjectNode rootNode = objectMapper.writeValueAsTree(bean);
        JsonGenerator gen = StaxUtil.createRootXmlGenerator();
        ObjectNode rootNodeFromObjectMapper = objectMapper.readValue(rootNode.toString(), ObjectNode.class);
        provider.setRootName(rootNodeFromObjectMapper);
        provider.serializeValue(gen, bean);
        String xml = StaxUtil.toXmlString(gen);
        String expectedXml = rootNode.toString();
        assertEquals(expectedXml, xml);
        assertEquals(namespace, gen.getStaxWriter().getDefaultNamespace());
    }

    private static class MyBean {
        private String field1;

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }
    }

    static class MyBean_arrayXml extends BasicJsonTest.BaseMapTest {
        @Override
        protected void _testSerializeDeserializeRoundtrip(Object value) throws Exception {
            // Intentionally left empty
        }

        @Override
        protected Object _parseValue(String xml) throws Exception {
            return mapper.readValue(xml, MyBean[].class);
        }
    }
}