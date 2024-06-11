package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;

public class XmlSerializerProviderTest extends TestBase {

    @Test
    public void testSerializeValue() throws Exception {
        XmlMapper mapper = new XmlMapper();
        XmlSerializerProvider provider = mapper.getSerializerProvider();

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("stringValue", "Hello World");
        rootNode.putPOJO("person", new Person("John Doe", 30));

        StringWriter sw = new StringWriter();
        JsonGenerator gen = mapper.getFactory().createGenerator(sw);
        provider.serializeValue(gen, rootNode);
        gen.flush();

        String xml = sw.toString();
        assertEquals("<person><name>John Doe</name><age>30</age></person>", xml);
    }

    @Test
    public void testSerializeValueWithRootType() throws Exception {
        XmlMapper mapper = new XmlMapper();
        XmlSerializerProvider provider = mapper.getSerializerProvider();

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("stringValue", "Hello World");
        rootNode.putPOJO("person", new Person("John Doe", 30));

        StringWriter sw = new StringWriter();
        JsonGenerator gen = mapper.getFactory().createGenerator(sw, JsonEncoding.XML_UTF8, new SimpleXmlSerializerProvider());
        provider.serializeValue(gen, rootNode, ObjectTree.class);
        gen.flush();

        String xml = sw.toString();
        assertEquals("<root><person><name>John Doe</name><age>30</age></person><stringValue>Hello World</stringValue></root>", xml);
    }

    @Test
    public void testSerializeValueWithRootTypeAndCustomSerializer() throws Exception {
        XmlMapper mapper = new XmlMapper();
        XmlSerializerProvider provider = mapper.getSerializerProvider();

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("stringValue", "Hello World");
        rootNode.putPOJO("person", new Person("John Doe", 30));

        StringWriter sw = new StringWriter();
        JsonGenerator gen = mapper.getFactory().createGenerator(sw, JsonEncoding.XML_UTF8, new SimpleXmlSerializerProvider());
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("person");
        JsonSerializer<Object> serializer = mapper.getSerializerProvider().findValueSerializer(Object.class, filter);

        provider.setDefaultSerializer(Object.class, serializer);
        provider.serializeValue(gen, rootNode, ObjectTree.class);
        gen.flush();

        String xml = sw.toString();
        assertEquals("<person><name>John Doe</name><age>30</age></person>", xml);
    }

    @Test
    public void testSerializeValueWithNull() throws Exception {
        XmlMapper mapper = new XmlMapper();
        XmlSerializerProvider provider = mapper.getSerializerProvider();

        StringWriter sw = new StringWriter();
        JsonGenerator gen = mapper.getFactory().createGenerator(sw);
        provider.serializeValue(gen, null);
        gen.flush();

        String xml = sw.toString();
        assertEquals("<null/>", xml);
    }

    @Test
    public void testSerializeValueWithArray() throws Exception {
        XmlMapper mapper = new XmlMapper();
        XmlSerializerProvider provider = mapper.getSerializerProvider();

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("items", mapper.createValueNode(new int[]{1, 2, 3}));

        StringWriter sw = new StringWriter();
        JsonGenerator gen = mapper.getFactory().createGenerator(sw);
        provider.serializeValue(gen, rootNode);
        gen.flush();

        String xml = sw.toString();
        assertEquals("<items><item><int>1</int></item><item><int>2</int></item><item><int>3</int></item></items>", xml);
    }

    @Test
    public void testSerializeValueWithArrayAndRootType() throws Exception {
        XmlMapper mapper = new XmlMapper();
        XmlSerializerProvider provider = mapper.getSerializerProvider();

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("items", mapper.createValueNode(new int[]{1, 2, 3}));

        StringWriter sw = new StringWriter();
        JsonGenerator gen = mapper.getFactory().createGenerator(sw, JsonEncoding.XML_UTF8, new SimpleXmlSerializerProvider());
        provider.serializeValue(gen, rootNode, ArrayNode.class);
        gen.flush();

        String xml = sw.toString();
        assertEquals("<root><items><item><int>1</int></item><item><int>2</int></item><item><int>3</int></item></items></root>", xml);
    }
}

class SimpleXmlSerializerProvider extends XmlSerializerProvider {
}

class Person {
    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}