package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.*;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;
    private ObjectMapper objectMapper;
    private ObjectNode rootNode;

    @Before
    public void setup() throws JsonProcessingException {
        objectMapper = new ObjectMapper(new XMLFactory());
        xmlSerializerProvider = objectMapper.getXMLSerializerProvider();
        rootNode = objectMapper.createObjectNode();
    }

    @Test
    public void testDefaultSerializerProviderCopy() throws IOException {
        SerializerProvider src = xmlSerializerProvider;
        SerializerProvider copied = src.copy();

        assertEquals(src.getClass().getName(), copied.getClass().getName());
        assertNotSame(src, copied);
    }

    @Test
    public void testXmlSerializerProviderCreateInstance() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider instance = new XmlSerializerProvider(rootNameLookup);
        XmlSerializerProvider configInstance = new XmlSerializerProvider(instance, new SerializationConfig(), null);

        assertEquals(instance.getClass().getName(), configInstance.getClass().getName());
        assertNotSame(instance, configInstance);
    }

    @Test
    public void testXmlSerializerProviderSerialization() throws IOException {
        ObjectNode node = rootNode.putObject("person");
        node.putPOJO("name", new Person("John Doe"));

        String xml = objectMapper.writeValueAsString(node);

        XmlNode node2 = objectMapper.readTree(xml);
        Person person = node2.path("person").getPOJO(Person.class);

        assertEquals("John Doe", person.getName());
    }

    @Test
    public void testSerializeValueWithNull() throws IOException {
        JsonGenerator gen = xmlSerializerProvider.getGenerator(new StringWriter());

        xmlSerializerProvider.serializeValue(gen, null);
        String xml = gen.getBuffer().asString();

        assertEquals("<null/>", xml);
    }

    @Test
    public void testSerializeValueWithSimpleType() throws IOException {
        JsonGenerator gen = xmlSerializerProvider.getGenerator(new StringWriter());

        xmlSerializerProvider.serializeValue(gen, "Hello World");
        String xml = gen.getBuffer().asString();

        assertEquals("<string>Hello World</string>", xml);
    }

    @Test
    public void testSerializeValueWithComplexType() throws IOException {
        JsonGenerator gen = xmlSerializerProvider.getGenerator(new StringWriter());

        ObjectNode node = rootNode.putObject("person");
        node.putPOJO("name", new Person("John Doe"));
        node.putPOJO("address", new Address("1 Main St", "Anytown", "CA", 12345));

        xmlSerializerProvider.serializeValue(gen, node);
        String xml = gen.getBuffer().asString();

        String expectedXml = "<person>" +
                "<name>" +
                "<string>John Doe</string>" +
                "</name>" +
                "<address>" +
                "<street><string>1 Main St</string></street>" +
                "<city><string>Anytown</string></city>" +
                "<state><string>CA</string></state>" +
                "<postalCode><string>12345</string></postalCode>" +
                "</address>" +
                "</person>";

        assertEquals(expectedXml, xml);
    }

    @Test
    public void testSerializeValueWithArray() throws IOException {
        JsonGenerator gen = xmlSerializerProvider.getGenerator(new StringWriter());

        ObjectNode node = rootNode.putObject("numbers");
        node.put(0, 1);
        node.put(1, 2);
        node.put(2, 3);

        xmlSerializerProvider.serializeValue(gen, node);
        String xml = gen.getBuffer().asString();

        String expectedXml = "<numbers><item><int>1</int></item>" +
                "<item><int>2</int></item>" +
                "<item><int>3</int></item></numbers>";

        assertEquals(expectedXml, xml);
    }

    @Test
    public void testSerializeValueWithCustomSerializer() throws IOException {
        PersonSerializer personSerializer = new PersonSerializer();
        JsonSerializer<Person> customSerializer = new StdSerializer<Person>(Person.class) {
            @Override
            public void serialize(Person value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException {
                personSerializer.serialize(value, gen, provider);
            }
        };

        ObjectNode node = rootNode.putObject("person");
        node.setAll(Arrays.asList(new Person("John Doe"), new Person("Jane Doe")));

        JsonGenerator gen = xmlSerializerProvider.getGenerator(new StringWriter());
        xmlSerializerProvider.setSerializerInstance(Person.class, customSerializer);
        xmlSerializerProvider.serializeValue(gen, node);
        String xml = gen.getBuffer().asString();

        String expectedXml = "<person>" +
                "<person>" +
                "<name><string>John Doe</string></name>" +
                "<name><string>Jane Doe</string></name>" +
                "</person>" +
                "</person>";

        assertEquals(expectedXml, xml);
    }
}

class Person {
    private String name;

    public Person() {}

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static class PersonSerializer extends StdSerializer<Person> {
        public PersonSerializer() {
            this(null);
        }

        public PersonSerializer(Class<Person> t) {
            this(null);
        }

        public PersonSerializer(Class<Person> t, JsonSerializerProvider p) {
            this(null);
        }

        public PersonSerializer(Class<Person> t, JsonSerializerProvider p,boolean m) {
            super(t, p, m);
        }

        @Override
        public void serialize(Person value, JsonGenerator gen, SerializerProvider provider)
                throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("name");
            gen.writeString(value.getName());
            gen.writeEndObject();
        }
    }
}

class Address {
    private String street;
    private String city;
    private String state;
    private int postalCode;

    public Address() {}

    public Address(String street, String city, String state, int postalCode) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public int getPostalCode() {
        return postalCode;
    }
}