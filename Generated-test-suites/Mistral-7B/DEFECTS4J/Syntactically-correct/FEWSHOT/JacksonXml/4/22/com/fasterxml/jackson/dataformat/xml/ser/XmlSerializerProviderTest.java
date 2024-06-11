package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValue() throws JsonMappingException, IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup xmlRootNameLookup = new XmlRootNameLookup();
        SerializerProvider provider = new XmlSerializerProvider(xmlRootNameLookup);
        ObjectMapper mapper = new ObjectMapper(provider);

        JavaType javaType = mapper.getTypeFactory().constructValueType(String.class, new Class[0]);
        String value = "testValue";

        // Test serializing a non-null value
        JsonGenerator gen = new TokenBuffer();
        provider.serializeValue(gen, value);
        String actual = gen.getSerializer().asJsonParser().getValueAsString();
        assertTrue(actual.contains(value));

        // Test serializing a null value
        JsonGenerator genNull = new TokenBuffer();
        provider.serializeValue(genNull, null);
        String actualNull = genNull.getSerializer().asJsonParser().getValueAsString();
        assertNull(actualNull);

        // Test serializing a non-null value with a root type
        JsonSerializer<Object> ser = mapper.findValueSerializer(String.class, config);
        provider.serializeValue(gen, value, javaType, ser);
        String actualRootType = gen.getSerializer().asJsonParser().getValueAsString();
        assertTrue(actualRootType.contains(value));

        // Test serializing a null value with a root type
        provider.serializeValue(genNull, null, javaType, ser);
        String actualNullRootType = genNull.getSerializer().asJsonParser().getValueAsString();
        assertNull(actualNullRootType);

        // Test serializing a non-null value with a custom serializer
        JsonSerializer<String> customSer = new JsonSerializer<String>() {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString("customSerializedValue");
            }
        };
        provider.serializeValue(gen, value, javaType, customSer);
        String actualCustomSer = gen.getSerializer().asJsonParser().getValueAsString();
        assertEquals("customSerializedValue", actualCustomSer);

        // Test serializing a null value with a custom serializer
        provider.serializeValue(genNull, null, javaType, customSer);
        String actualNullCustomSer = genNull.getSerializer().asJsonParser().getValueAsString();
        assertNull(actualNullCustomSer);
    }

    @Test
    public void testInitWithRootName() throws IOException, XMLStreamException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup xmlRootNameLookup = new XmlRootNameLookup();
        SerializerProvider provider = new XmlSerializerProvider(xmlRootNameLookup);

        String rootName = "root";
        ToXmlGenerator xgen = new ToXmlGenerator();
        provider._initWithRootName(xgen, new QName(null, rootName));

        String actualNamespace = xgen.getNamespace();
        String actualLocalName = xgen.getLocalName();
        assertEquals(null, actualNamespace);
        assertEquals(rootName, actualLocalName);

        // Test setting the default namespace
        String defaultNamespace = "default";
        xgen.getStaxWriter().setDefaultNamespace(defaultNamespace);
        String actualDefaultNamespace = xgen.getNamespace();
        assertEquals(defaultNamespace, actualDefaultNamespace);
    }

    @Test
    public void testStartRootArray() throws IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup xmlRootNameLookup = new XmlRootNameLookup();
        SerializerProvider provider = new XmlSerializerProvider(xmlRootNameLookup);

        ToXmlGenerator xgen = new ToXmlGenerator();
        provider._startRootArray(xgen, new QName(null, "root"));

        String actual = xgen.getSerializer().asJsonParser().getValueAsString();
        assertEquals("<root/>", actual);
    }

    @Test
    public void testSerializeXmlNull() throws IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup xmlRootNameLookup = new XmlRootNameLookup();
        SerializerProvider provider = new XmlSerializerProvider(xmlRootNameLookup);

        ToXmlGenerator xgen = new ToXmlGenerator();
        provider._serializeXmlNull(xgen);

        String actual = xgen.getSerializer().asJsonParser().getValueAsString();
        assertEquals("<null/>", actual);
    }
}