package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonSerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.StaxUtil;
import com.fasterxml.jackson.databind.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.XmlSerializerProviderTest;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class XmlSerializerProviderTest {

    @SuppressWarnings("unchecked")
    private static final XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup() {
        @Override
        public QName findRootName(Class<?> clazz, SerializationConfig config) {
            return new QName("root", "test");
        }
    });

    @Test
    public void testSerializeValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper(provider);
        SerializationConfig config = mapper.getSerializationConfig();

        // Test serializing a null value
        JsonGenerator gen = mapper.getFactory().createGenerator(new TokenStream(), config);
        provider.serializeValue(gen, null);
        assertTrue(gen.getOutputWritebuffers().isEmpty());

        // Test serializing a simple value
        String value = "test";
        gen = mapper.getFactory().createGenerator(new TokenStream(), config);
        provider.serializeValue(gen, value);
        String result = gen.getOutputBuffer().toString();
        assertEquals("<test>" + value + "</test>", result);

        // Test serializing a JavaBean
        TestBean bean = new TestBean("hello", 42);
        gen = mapper.getFactory().createGenerator(new TokenStream(), config);
        provider.serializeValue(gen, bean);
        result = gen.getOutputBuffer().toString();
        assertEquals("<test><name>hello</name><number>42</number></test>", result);

        // Test serializing a JavaBean with a custom root name
        config.setRootValueSeparator('[');
        gen = mapper.getFactory().createGenerator(new TokenStream(), config);
        provider.serializeValue(gen, bean);
        result = gen.getOutputBuffer().toString();
        assertEquals("[test][<name>hello</name><number>42</number>]", result);

        // Test serializing a JavaBean with an array root name
        config.setRootValueSeparator('[');
        config.setRootName("items");
        gen = mapper.getFactory().createGenerator(new TokenStream(), config);
        provider.serializeValue(gen, bean);
        result = gen.getOutputBuffer().toString();
        assertEquals("[items][<name>hello</name><number>42</number>]", result);

        // Test serializing an empty list
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        List<String> list = new ArrayList<>();
        gen = mapper.getFactory().createGenerator(new TokenStream(), config);
        provider.serializeValue(gen, list, javaType);
        result = gen.getOutputBuffer().toString();
        assertEquals("<items xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:type=\"array\">[]</items>", result);

        // Test serializing a list with one element
        list.add("world");
        gen = mapper.getFactory().createGenerator(new TokenStream(), config);
        provider.serializeValue(gen, list, javaType);
        result = gen.getOutputBuffer().toString();
        assertEquals("<items xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:type=\"array\"><item>world</item></items>", result);

        // Test serializing a JavaBean with custom serializers
        config.setDefaultSerializerProvider(provider);
        ObjectMapper mapperWithCustomSerializers = new ObjectMapper(config);
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer();
        ZonedDateTimeSerializer zonedDateTimeSerializer = new ZonedDateTimeSerializer();
        mapperWithCustomSerializers.registerSerializer(LocalDateTime.class, localDateTimeSerializer);
        mapperWithCustomSerializers.registerSerializer(ZonedDateTime.class, zonedDateTimeSerializer);
        TestBeanWithCustomSerializers beanWithCustomSerializers = new TestBeanWithCustomSerializers(LocalDateTime.now(), ZonedDateTime.now());
        gen = mapperWithCustomSerializers.getFactory().createGenerator(new TokenStream(), config);
        provider.serializeValue(gen, beanWithCustomSerializers);
        result = gen.getOutputBuffer().toString();
        assertEquals("<test><name><![CDATA[2023-03-30T14:30:30.000000000Z]]></name><number><![CDATA[2023-03-30T14:30:30.000000000+00:00]]></number></test>", result);
    }

    @Test
    public void testSerializeValueWithRootName() throws IOException {
        ObjectMapper mapper = new ObjectMapper(provider);
        SerializationConfig config = mapper.getSerializationConfig();

        // Test serializing a null value with a root name
        JsonGenerator gen = mapper.getFactory().createGenerator(new TokenStream(), config);
        provider.serializeValue(gen, null, new QName("root", "test"));
        assertTrue(gen.getOutputWritebuffers().isEmpty());

        // Test serializing a simple value with a root name
        String value = "test";
        gen = mapper.getFactory().createGenerator(new TokenStream(), config);
        provider.serializeValue(gen, value, new QName("root", "test"));
        String result = gen.getOutputBuffer().toString();
        assertEquals("<root><test>" + value + "</test></root>", result);

        // Test serializing a JavaBean with a root name
        TestBean bean = new TestBean("hello", 42);
        gen = mapper.getFactory().createGenerator(new TokenStream(), config);
        provider.serializeValue(gen, bean, new QName("root", "test"));
        result = gen.getOutputBuffer().toString();
        assertEquals("<root><test><name>hello</name><number>42</number></test></root>", result);

        // Test serializing a JavaBean with an array root name and a root value separator
        config.setRootValueSeparator('[');
        gen = mapper.getFactory().createGenerator(new TokenStream(), config);
        provider.serializeValue(gen, bean, new QName("root", "test"));
        result = gen.getOutputBuffer().toString();
        assertEquals("<root><test><name>hello</name><number>42</number></test></root>", result);

        // Test serializing an empty list with a root name
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        List<String> list = new ArrayList<>();
        gen = mapper.getFactory().createGenerator(new TokenStream(), config);
        provider.serializeValue(gen, list, javaType, new QName("root", "test"));
        result = gen.getOutputBuffer().toString();
        assertEquals("<root><test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:type=\"array\" xsi:type=\"array\"><items xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:type=\"array\"></items></test></root>", result);

        // Test serializing a list with one element with a root name
        list.add("world");
        gen = mapper.getFactory().createGenerator(new TokenStream(), config);
        provider.serializeValue(gen, list, javaType, new QName("root", "test"));
        result = gen.getOutputBuffer().toString();
        assertEquals("<root><test><name>hello</name><number>42</number></test><items xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"array\"><item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">world</item></items></test></root>", result);
    }

    @Test
    public void testInitializeWithRootName() throws IOException, XMLStreamException {
        ObjectMapper mapper = new ObjectMapper(provider);
        SerializationConfig config = mapper.getSerializationConfig();

        // Test initializing with no root name and no root element
        JsonSerializerProvider serializerProvider = new JsonSerializerProvider();
        TokenBuffer tokenBuffer = new TokenBuffer(new StringWriter());
        JsonGenerator gen = serializerProvider.getFactory().createGenerator(tokenBuffer, config);
        provider.initializeWithRootName(gen, null);
        assertNull(gen.getNextName());

        // Test initializing with a root name and no root element
        gen = mapper.getFactory().createGenerator(tokenBuffer, config);
        provider.initializeWithRootName(gen, new QName("root", "test"));
        assertFalse(gen.getNextName().getNamespaceURI().isEmpty());
        assertEquals("test", gen.getNextName().getLocalPart());

        // Test initializing with a root name and a root element
        gen = mapper.getFactory().createGenerator(tokenBuffer, config);
        provider.initializeWithRootName(gen, new QName("root", "test"));
        gen.writeStartObject();
        provider.initializeWithRootName(gen, null);
        String ns = gen.getOutputWritebuffers().get(0).getNamespaceURI();
        assertTrue(ns.isEmpty());
        assertEquals("test", gen.getNextName().getLocalPart());

        // Test initializing with a root name and a root element with a namespace
        gen = mapper.getFactory().createGenerator(tokenBuffer, config);
        provider.initializeWithRootName(gen, new QName("root", "test", "http://example.com"));
        gen.writeStartElement("root", null, "http://example.com");
        provider.initializeWithRootName(gen, null);
        ns = gen.getOutputWritebuffers().get(0).getNamespaceURI();
        assertEquals("http://example.com", ns);
        assertEquals("test", gen.getNextName().getLocalPart());
    }

    @Test
    public void testSetNextNameIfMissing() throws IOException, XMLStreamException {
        ObjectMapper mapper = new ObjectMapper(provider);
        SerializationConfig config = mapper.getSerializationConfig();

        // Test setting the next name if it's missing and we're not the root
        JsonSerializerProvider serializerProvider = new JsonSerializerProvider();
        TokenBuffer tokenBuffer = new TokenBuffer(new StringWriter());
        JsonGenerator gen = serializerProvider.getFactory().createGenerator(tokenBuffer, config);
        assertFalse(provider.setNextNameIfMissing(gen, new QName("name", "test")));

        // Test setting the next name if it's missing and we're the root
        gen = mapper.getFactory().createGenerator(tokenBuffer, config);
        assertTrue(provider.setNextNameIfMissing(gen, new QName("name", "test")));

        // Test setting the next name if it's missing and we're the root with a namespace
        gen = mapper.getFactory().createGenerator(tokenBuffer, config);
        gen.writeStartElement("root", null, "http://example.com");
        assertTrue(provider.setNextNameIfMissing(gen, new QName("name", "test")));
    }

    @Test
    public void testWriteStartObject() throws IOException, XMLStreamException {
        ObjectMapper mapper = new ObjectMapper(provider);
        SerializationConfig config = mapper.getSerializationConfig();

        // Test writing a root object without a root name
        JsonSerializerProvider serializerProvider = new JsonSerializerProvider();
        TokenBuffer tokenBuffer = new TokenBuffer(new StringWriter());
        JsonGenerator gen = serializerProvider.getFactory().createGenerator(tokenBuffer, config);
        provider.writeStartRootObject(gen);
        String result = gen.getOutputBuffer().toString();
        assertEquals("<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:type=\"object\">", result);

        // Test writing a root object with a root name
        gen = mapper.getFactory().createGenerator(tokenBuffer, config);
        provider.writeStartRootObject(gen, new QName("root", "test"));
        result = gen.getOutputBuffer().toString();
        assertEquals("<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:type=\"object\" xmlns:test=\"test\">", result);

        // Test writing a root object with a root name and a namespace
        gen = mapper.getFactory().createGenerator(tokenBuffer, config);
        provider.writeStartRootObject(gen, new QName("root", "test", "http://example.com"));
        gen.writeStartElement("root", null, "http://example.com");
        result = gen.getOutputBuffer().toString();
        assertEquals("<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:type=\"object\" xmlns:test=\"test\" xmlns:xsi:prefix=\"root\"><root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:type=\"unprefixed\">", result);
    }

    static class TestBean {
        private final String name;
        private final int number;

        public TestBean(String name, int number) {
            this.name = name;
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public int getNumber() {
            return number;
        }
    }

    static class TestBeanWithCustomSerializers {
        private final LocalDateTime dateTime;
        private final ZonedDateTime zonedDateTime;

        public TestBeanWithCustomSerializers(LocalDateTime dateTime, ZonedDateTime zonedDateTime) {
            this.dateTime = dateTime;
            this.zonedDateTime = zonedDateTime;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public ZonedDateTime getZonedDateTime() {
            return zonedDateTime;
        }
    }
}