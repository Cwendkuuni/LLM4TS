package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;

/**
 * Test class for XmlSerializerProvider
 */
public class XmlSerializerProviderTest {

    private ObjectMapper mapper;
    private SerializerProvider provider;
    private XmlRootNameLookup rootNameLookup;

    @Before
    public void setup() {
        mapper = new XmlMapper();
        provider = mapper.getSerializerProvider();
        rootNameLookup = new XmlRootNameLookup(mapper.getConfiguration());
    }

    @Test
    public void testSerializeNull() throws JsonProcessingException, IOException {
        JsonGenerator gen = StaxUtil.createXmlGenerator();
        provider.serializeValue(gen, null);
        String result = gen.asString();
        assertEquals("<null/>", result);
    }

    @Test
    public void testSerializeObjectWithRootNameFromConfig() throws JsonProcessingException, IOException {
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSeparator(":");
        config.setRootName("testRoot");

        QName rootName = new QName("test", "testRoot");
        JsonGenerator gen = StaxUtil.createXmlGenerator();
        provider = new XmlSerializerProvider(rootNameLookup, config, null);
        String value = "testValue";
        provider.serializeValue(gen, value);

        String result = gen.asString();
        assertEquals("<testRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><test>" + value + "</test></testRoot>", result);
    }

    @Test
    public void testSerializeObjectWithRootNameFromLookup() throws JsonProcessingException, IOException {
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSeparator(":");
        config.setRootName("testRoot");

        JsonGenerator gen = StaxUtil.createXmlGenerator();
        provider = new XmlSerializerProvider(rootNameLookup, config, null);
        String value = "testValue";
        QName rootName = new QName("test", "testRoot");
        provider.serializeValue(gen, value, rootName);

        String result = gen.asString();
        assertEquals("<testRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><test>" + value + "</test></testRoot>", result);
    }

    @Test
    public void testSerializeObjectWithRootNameFromClass() throws JsonProcessingException, IOException {
        @SuppressWarnings("rawtypes")
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
        JsonGenerator gen = StaxUtil.createXmlGenerator();
        provider = new XmlSerializerProvider(rootNameLookup, mapper.getSerializationConfig(), null);
        TestClass testClass = new TestClass();
        provider.serializeValue(gen, testClass, javaType);

        String result = gen.asString();
        assertEquals("<root><test>test1</test><test>test2</test></root>", result);
    }

    @Test
    public void testSerializeArray() throws JsonProcessingException, IOException {
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSeparator(":");
        config.setRootName("testRoot");

        JsonGenerator gen = StaxUtil.createXmlGenerator();
        provider = new XmlSerializerProvider(rootNameLookup, config, null);
        Object[] values = new Object[]{"test1", "test2"};
        provider.serializeValue(gen, values);

        String result = gen.asString();
        assertEquals("<testRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><item><test>test1</test></item><item><test>test2</test></item></testRoot>", result);
    }

    @Test
    public void testSerializeArrayWithRootName() throws JsonProcessingException, IOException {
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSeparator(":");
        config.setRootName("testRoot");

        JsonGenerator gen = StaxUtil.createXmlGenerator();
        provider = new XmlSerializerProvider(rootNameLookup, config, null);
        Object[] values = new Object[]{"test1", "test2"};
        QName rootName = new QName("test", "testRoot");
        provider.serializeValue(gen, values, rootName);

        String result = gen.asString();
        assertEquals("<testRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><item><test>test1</test></item><item><test>test2</test></item></testRoot>", result);
    }

    @Test
    public void testSerializeArrayWithCustomSerializer() throws JsonProcessingException, IOException {
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSeparator(":");
        config.setRootName("testRoot");
        config.setSerializerProviderClass(CustomSerializerProvider.class.getName());

        JsonGenerator gen = StaxUtil.createXmlGenerator();
        provider = new XmlSerializerProvider(rootNameLookup, config, null);
        Object[] values = new Object[]{"test1", "test2"};
        provider.serializeValue(gen, values);

        String result = gen.asString();
        assertEquals("<testRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><item><test1></test1></item><item><test2></test2></item></testRoot>", result);
    }

    @Test
    public void testSerializeArrayWithCustomSerializerAndRootName() throws JsonProcessingException, IOException {
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSeparator(":");
        config.setRootName("testRoot");
        config.setSerializerProviderClass(CustomSerializerProvider.class.getName());

        JsonGenerator gen = StaxUtil.createXmlGenerator();
        provider = new XmlSerializerProvider(rootNameLookup, config, null);
        Object[] values = new Object[]{"test1", "test2"};
        QName rootName = new QName("test", "testRoot");
        provider.serializeValue(gen, values, rootName);

        String result = gen.asString();
        assertEquals("<testRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><item><test1></test1></item><item><test2></test2></item></testRoot>", result);
    }

    @Test
    public void testSerializeArrayWithTypeSerializer() throws JsonProcessingException, IOException {
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSeparator(":");
        config.setRootName("testRoot");

        JsonGenerator gen = StaxUtil.createXmlGenerator();
        provider = new XmlSerializerProvider(rootNameLookup, config, null);
        Object[] values = new Object[]{"test1", "test2"};
        JavaType javaType = mapper.getTypeFactory().constructType(new int[2].getClass());
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        provider.setActiveTypeSerializer(int[].class, typeSerializer);
        provider.serializeValue(gen, values, javaType);

        String result = gen.asString();
        assertEquals("<testRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><item><int-array><int>test1</int></int-array></item><item><int-array><int>test2</int></int-array></item></testRoot>", result);
    }

    @Test
    public void testSerializeArrayWithTypeSerializerAndRootName() throws JsonProcessingException, IOException {
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSeparator(":");
        config.setRootName("testRoot");

        JsonGenerator gen = StaxUtil.createXmlGenerator();
        provider = new XmlSerializerProvider(rootNameLookup, config, null);
        Object[] values = new Object[]{"test1", "test2"};
        JavaType javaType = mapper.getTypeFactory().constructType(new int[2].getClass());
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        provider.setActiveTypeSerializer(int[].class, typeSerializer);
        QName rootName = new QName("test", "testRoot");
        provider.serializeValue(gen, values, javaType, rootName);

        String result = gen.asString();
        assertEquals("<testRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><item><int-array><int>test1</int></int-array></item><item><int-array><int>test2</int></int-array></item></testRoot>", result);
    }

    @Test
    public void testSerializeObjectWithFilters() throws JsonProcessingException, IOException {
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSeparator(":");
        config.setRootName("testRoot");
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("test");
        config.setFilterProvider(filter);

        JsonGenerator gen = StaxUtil.createXmlGenerator();
        provider = new XmlSerializerProvider(rootNameLookup, config, null);
        TestClass testClass = new TestClass();
        provider.serializeValue(gen, testClass);

        String result = gen.asString();
        assertEquals("<testRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><test>test1</test></testRoot>", result);
    }

    @Test
    public void testSerializeObjectWithFiltersAndRootName() throws JsonProcessingException, IOException {
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSeparator(":");
        config.setRootName("testRoot");
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("test");
        config.setFilterProvider(filter);

        JsonGenerator gen = StaxUtil.createXmlGenerator();
        provider = new XmlSerializerProvider(rootNameLookup, config, null);
        TestClass testClass = new TestClass();
        QName rootName = new QName("test", "testRoot");
        provider.serializeValue(gen, testClass, rootName);

        String result = gen.asString();
        assertEquals("<testRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><test>test1</test></testRoot>", result);
    }

    @Test
    public void testSerializeObjectWithFiltersAndRootNameAndTypeSerializer() throws JsonProcessingException, IOException {
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSeparator(":");
        config.setRootName("testRoot");
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("test");
        config.setFilterProvider(filter);

        JsonGenerator gen = StaxUtil.createXmlGenerator();
        provider = new XmlSerializerProvider(rootNameLookup, config, null);
        TestClass testClass = new TestClass();
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        provider.setActiveTypeSerializer(TestClass.class, typeSerializer);
        QName rootName = new QName("test", "testRoot");
        provider.serializeValue(gen, testClass, javaType, rootName);

        String result = gen.asString();
        assertEquals("<testRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><test>test1</test></testRoot>", result);
    }

    static class TestClass {
        String test1 = "test1";
        String test2 = "test2";
    }

    static class CustomSerializerProvider extends DefaultSerializerProvider {
        public CustomSerializerProvider() {
            super();
            SimpleSerializer serializer = new SimpleSerializer();
            findValueSerializer(String.class).setSerializer(serializer);
        }

        static class SimpleSerializer extends JsonSerializer<String> {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serProvider) throws IOException {
                gen.writeString("" + value);
            }
        }
    }
}