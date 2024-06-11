package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.ToXmlGenerator;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);

        String xml = "";

        // test serializing null
        JsonGenerator gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.serializeValue(gen, null);
        assertEquals("<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"\"><null/></null>", gen.getOutput().toString());
        gen.close();

        // test serializing object with default root name
        JavaType javaType = mapper.getTypeFactory().constructValueType(String.class);
        Object object = "test";
        gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.serializeValue(gen, object);
        assertEquals("<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\">" + object + "</test>", gen.getOutput().toString());
        gen.close();

        // test serializing object with custom root name
        config.setRootValueSeparator("_");
        gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.serializeValue(gen, object, javaType);
        assertEquals("<test_ xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"\"><test>" + object + "</test></test_>", gen.getOutput().toString());
        gen.close();

        // test serializing array with default root name
        JavaType arrayType = mapper.getTypeFactory().constructArrayType(String.class);
        Object array = new Object[]{"test1", "test2"};
        gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.serializeValue(gen, array);
        assertEquals("<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\">" +
                "<item>" + "test1" + "</item>" +
                "<item>" + "test2" + "</item>" +
                "</item>", gen.getOutput().toString());
        gen.close();

        // test serializing array with custom root name
        config.setRootValueSeparator("");
        gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.serializeValue(gen, array, arrayType);
        assertEquals("<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\">" +
                "<item>" + "test1" + "</item>" +
                "<item>" + "test2" + "</item>" +
                "</item>", gen.getOutput().toString());
        gen.close();

        // test serializing object with custom root name using ToXmlGenerator
        JsonSerializer<Object> serializer = mapper.findValueSerializer(Object.class, null);
        gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.serializeValue(gen, object, javaType, serializer);
        assertEquals("<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\">" + object + "</test>", gen.getOutput().toString());
        gen.close();
    }

    @Test
    public void testSerializeValueWithNullRootType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);

        String xml = "";

        // test serializing null with null root type
        JsonGenerator gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.serializeValue(gen, null, null);
        assertEquals("<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"><null/></null>", gen.getOutput().toString());
        gen.close();

        // test serializing object with null root type
        JavaType javaType = mapper.getTypeFactory().constructValueType(String.class);
        Object object = "test";
        gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.serializeValue(gen, object, null);
        assertEquals("<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\">" + object + "</test>", gen.getOutput().toString());
        gen.close();
    }

    @Test
    public void testSerializeValueWithNullSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);

        String xml = "";

        // test serializing null with null serializer
        JsonGenerator gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.serializeValue(gen, null, null, null);
        assertEquals("<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"><null/></null>", gen.getOutput().toString());
        gen.close();

        // test serializing object with null serializer
        JavaType javaType = mapper.getTypeFactory().constructValueType(String.class);
        Object object = "test";
        gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.serializeValue(gen, object, javaType, null);
        assertEquals("<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\">" + object + "</test>", gen.getOutput().toString());
        gen.close();
    }

    @Test
    public void testInitializeWithRootName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);

        // test initializing with root name for null
        JsonGenerator gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.initializeWithRootName(gen, ROOT_NAME_FOR_NULL);
        assertEquals("<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"\"><null/></null>", gen.getOutput().toString());
        gen.close();

        // test initializing with root name for object
        JavaType javaType = mapper.getTypeFactory().constructValueType(String.class);
        Object object = "test";
        gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.initializeWithRootName(gen, javaType);
        assertEquals("<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\">" + object + "</test>", gen.getOutput().toString());
        gen.close();

        // test initializing with root name for array
        JavaType arrayType = mapper.getTypeFactory().constructArrayType(String.class);
        Object array = new Object[]{"test1", "test2"};
        gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.initializeWithRootName(gen, arrayType);
        assertEquals("<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\">" +
                "<item>" + "test1" + "</item>" +
                "<item>" + "test2" + "</item>" +
                "</item>", gen.getOutput().toString());
        gen.close();
    }

    @Test
    public void testStartRootArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator gen = mapper.getFactory().createXmlGenerator(outputStream, null);

        provider.startRootArray(gen, ROOT_NAME_FOR_NULL);
        assertEquals("<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"\">" +
                "<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">", gen.getOutput().toString());
        gen.close();

        outputStream.reset();
        gen = mapper.getFactory().createXmlGenerator(outputStream, null);
        JavaType arrayType = mapper.getTypeFactory().constructArrayType(String.class);
        provider.startRootArray(gen, arrayType);
        assertEquals("<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\">" +
                "<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">", gen.getOutput().toString());
        gen.close();
    }
}