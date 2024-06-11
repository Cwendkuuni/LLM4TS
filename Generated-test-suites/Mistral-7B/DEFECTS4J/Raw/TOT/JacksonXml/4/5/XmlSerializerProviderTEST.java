package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProvider.SerializerProviderTestBase;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.xmlserializer.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest extends SerializerProviderTestBase {

    private ObjectMapper objectMapper;
    private XmlRootNameLookup rootNameLookup;

    @Override
    protected SerializerProvider createSerializerProvider() {
        rootNameLookup = new XmlRootNameLookup();
        return new XmlSerializerProvider(rootNameLookup);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        objectMapper = new ObjectMapper();
        objectMapper.setSerializationConfig(createConfig());
        objectMapper.setSerializerProvider(createSerializerProvider());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        objectMapper = null;
    }

    @SuppressWarnings("unchecked")
    private SerializationConfig createConfig() {
        return new SerializationConfig(StdSerializer.instance(Object.class))
                .withRootName("TestRoot")
                .withSerializers(new StdSerializer<Object>(Object.class) {
                    @Override
                    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                            throws IOException, JsonProcessingException {
                        jgen.writeString("TestValue");
                    }
                })
                .withFilterProvider(new SimpleFilterProvider()
                        .addFilter("TestFilter", new SimpleBeanPropertyFilter("TestProperty")))
                .withDefaultRootName("TestRootWithDefault")
                .withoutFeatures(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Test
    public void testSerializeValueNull() throws IOException {
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);

        XmlSerializerProvider provider = (XmlSerializerProvider) createSerializerProvider();
        provider.setRootNameLookup(rootNameLookup);
        provider.serializeValue(gen, null);

        String xml = gen.getOutputContentAsString();
        assertEquals("<null/>", xml);

        gen.close();
    }

    @Test
    public void testSerializeValueSingleObject() throws IOException {
        JsonGenerator gen = objectMapper.createXmlGenerator();

        XmlSerializerProvider provider = (XmlSerializerProvider) createSerializerProvider();
        provider.setRootNameLookup(rootNameLookup);

        String testInput = "TestInput";
        Object testObject = new Object() {
            String property = testInput;
        };

        provider.serializeValue(gen, testObject);

        String xml = gen.getOutputContentAsString();
        assertEquals("<TestRoot><property>" + testInput + "</property></TestRoot>", xml);

        gen.close();
    }

    @Test
    public void testSerializeValueArray() throws IOException {
        JsonGenerator gen = objectMapper.createXmlGenerator();

        XmlSerializerProvider provider = (XmlSerializerProvider) createSerializerProvider();
        provider.setRootNameLookup(rootNameLookup);

        Object[] testObjects = new Object[] { new Object() {
            String property = "TestValue1";
        }, new Object() {
            String property = "TestValue2";
        } };

        provider.serializeValue(gen, testObjects);

        String xml = gen.getOutputContentAsString();
        assertEquals("<TestRoot><item><property>TestValue1</property></item><item><property>TestValue2</property></item></TestRoot>", xml);

        gen.close();
    }

    @Test
    public void testSerializeValueArrayWithFilter() throws IOException {
        JsonGenerator gen = objectMapper.createXmlGenerator();

        XmlSerializerProvider provider = (XmlSerializerProvider) createSerializerProvider();
        provider.setRootNameLookup(rootNameLookup);

        Object[] testObjects = new Object[] { new Object() {
            String property1 = "TestValue1";
            String property2 = "TestValue2";
        }, new Object() {
            String property1 = "TestValue3";
            String property2 = null;
        } };

        provider.setConfig(createConfig());

        provider.serializeValue(gen, testObjects, JavaType.constructArrayType(Object.class));

        String xml = gen.getOutputContentAsString();
        assertEquals("<TestRoot><item><property1>TestValue1</property1></item><item><property1>TestValue3</property1></item></TestRoot>", xml);

        gen.close();
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueNullRootName() throws IOException {
        JsonGenerator gen = objectMapper.createXmlGenerator();

        XmlSerializerProvider provider = (XmlSerializerProvider) createSerializerProvider();

        provider.serializeValue(gen, new Object());

        try {
            String xml = gen.getOutputContentAsString();
            fail("Expected JsonMappingException but got: " + xml);
        } catch (JsonProcessingException e) {
            // expected
            String msg = e.getMessage();
            assertTrue(msg.startsWith("No root name specified for serialization"));
            throw e;
        }

        gen.close();
    }

    @Test
    public void testSerializeValueWithRootName() throws IOException {
        JsonGenerator gen = objectMapper.createXmlGenerator();

        XmlSerializerProvider provider = (XmlSerializerProvider) createSerializerProvider();
        provider.setRootNameLookup(rootNameLookup);
        provider.setRootName("TestRootWithCustomName");

        String testInput = "TestInput";
        Object testObject = new Object() {
            String property = testInput;
        };

        provider.serializeValue(gen, testObject);

        String xml = gen.getOutputContentAsString();
        assertEquals("<TestRootWithCustomName><property>" + testInput + "</property></TestRootWithCustomName>", xml);

        gen.close();
    }

    @Test
    public void testSerializeValueWithRootNameAndNamespace() throws IOException {
        JsonGenerator gen = objectMapper.createXmlGenerator();

        XmlSerializerProvider provider = (XmlSerializerProvider) createSerializerProvider();
        provider.setRootNameLookup(rootNameLookup);
        provider.setRootName(new QName("http://test.com/namespace", "TestWithCustomName"));

        String testInput = "TestInput";
        Object testObject = new Object() {
            String property = testInput;
        };

        provider.serializeValue(gen, testObject);

        String xml = gen.getOutputContentAsString();
        assertEquals("<TestWithCustomName xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><property>" + testInput + "</property></TestWithCustomName>", xml);

        gen.close();
    }

    @Test
    public void testSerializeValueWithNullRootNameAndNamespace() throws IOException {
        JsonGenerator gen = objectMapper.createXmlGenerator();

        XmlSerializerProvider provider = (XmlSerializerProvider) createSerializerProvider();
        provider.setRootNameLookup(rootNameLookup);
        provider.setRootName(null);
        provider.setRootName("http://test.com/namespace");

        String testInput = "TestInput";
        Object testObject = new Object() {
            String property = testInput;
        };

        provider.serializeValue(gen, testObject);

        String xml = gen.getOutputContentAsString();
        assertEquals("<null:TestInput><property>" + testInput + "</property></null:TestInput>", xml);

        gen.close();
    }

    @Test
    public void testSerializeValueWithRootNameAndNamespaceAndEmptyNamespace() throws IOException {
        JsonGenerator gen = objectMapper.createXmlGenerator();

        XmlSerializerProvider provider = (XmlSerializerProvider) createSerializerProvider();
        provider.setRootNameLookup(rootNameLookup);
        provider.setRootName(new QName("", "TestWithCustomName"));

        String testInput = "TestInput";
        Object testObject = new Object() {
            String property = testInput;
        };

        provider.serializeValue(gen, testObject);

        String xml = gen.getOutputContentAsString();
        assertEquals("<TestWithCustomName><property>" + testInput + "</property></TestWithCustomName>", xml);

        gen.close();
    }

    @Test
    public void testSerializeValueWithNullRootNameAndNamespaceAndEmptyName() throws IOException {
        JsonGenerator gen = objectMapper.createXmlGenerator();

        XmlSerializerProvider provider = (XmlSerializerProvider) createSerializerProvider();
        provider.setRootNameLookup(rootNameLookup);
        provider.setRootName(null);
        provider.setRootName(null);

        String testInput = "TestInput";
        Object testObject = new Object() {
            String property = testInput;
        };

        provider.serializeValue(gen, testObject);

        String xml = gen.getOutputContentAsString();
        assertEquals("<null:null><property>" + testInput + "</property></null:null>", xml);

        gen.close();
    }
}