package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory.Feature;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializer;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest extends TestBase {

    @Test
    public void testSerializeValue() throws IOException {
        SerializationConfig config = new SerializationConfig();
        config.setSerializationFeatures(Feature.FAIL_ON_EMPTY_BEANS, false);
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup() {
            @Override
            public QName findRootName(JavaType type, SerializationConfig config) {
                if (type.getRawClass() == String[].class) {
                    return new QName("root", "array");
                } else if (type.getRawClass() == String.class) {
                    return new QName("root", "string");
                }
                return null;
            }
        });

        JavaType stringArrayType = mapper().getTypeFactory().constructParametricType(String[].class, String.class);
        JavaType stringType = mapper().getTypeFactory().constructType(String.class);

        ObjectMapper mapper = new ObjectMapper(xmlProvider);
        String xmlWithRoot = mapper.writeValueAsString(new String[]{"foo", "bar"});
        assertEquals("<root><item>foo</item><item>bar</item></root>", xmlWithRoot);

        xmlWithRoot = mapper.writeValueAsString("foo");
        assertEquals("<root>foo</root>", xmlWithRoot);

        xmlWithRoot = mapper.writeValueAsString((Object) null);
        assertTrue(xmlWithRoot.startsWith("<null>"));
        xmlWithRoot = xmlWithRoot.substring(7);
        assertTrue(xmlWithRoot.endsWith("</null>"));

        String xmlWithCustomName = mapper.writeValueAsString(new String[]{"foo", "bar"}, "customRoot");
        assertEquals("<customRoot><item>foo</item><item>bar</item></customRoot>", xmlWithCustomName);

        QName customRootName = new QName("customRoot", "");
        SerializerProvider provider = new XmlSerializerProvider(xmlProvider.getConfig(), null, null) {
            @Override
            public ToXmlGenerator _asXmlGenerator(JsonGenerator gen) throws JsonMappingException {
                if (!(gen instanceof ToXmlGenerator)) {
                    throw new JsonMappingException("XmlMapper does not with generators of type other than ToXmlGenerator; got: " + gen.getClass().getName());
                }
                ToXmlGenerator xmlGenerator = (ToXmlGenerator) gen;
                xmlGenerator.setNextName(customRootName);
                xmlGenerator.initGenerator();
                return xmlGenerator;
            }
        };
        SimpleBeanPropertySerializer serializer = mapper.findValueSerializer(String[].class, false, null);
        String xmlWithCustomRootAndName = mapper.writerWithRootValue("customRoot")
                .writeValueAsString(new String[]{"foo", "bar"}, provider, serializer);
        assertEquals("<customRoot><item>foo</item><item>bar</item></customRoot>", xmlWithCustomRootAndName);
    }

    @Test
    public void testInitWithRootName() throws IOException, XMLStreamException {
        SerializationConfig config = new SerializationConfig();
        config.setSerializationFeatures(Feature.FAIL_ON_EMPTY_BEANS, false);
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup() {
            @Override
            public QName findRootName(JavaType type, SerializationConfig config) {
                if (type.getRawClass() == String[].class) {
                    return new QName("root", "array");
                } else if (type.getRawClass() == String.class) {
                    return new QName("root", "string");
                }
                return null;
            }
        });

        ObjectMapper mapper = new ObjectMapper(xmlProvider);
        JsonGenerator xmlGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        xmlProvider.initWithRootName(xmlGenerator, new QName("root", ""));
        StaxUtil.writeStartDocument(xmlGenerator);
        xmlGenerator.writeStartObject();
        xmlGenerator.writeFieldName("item");

        JavaType stringArrayType = mapper().getTypeFactory().constructParametricType(String[].class, String.class);
        JavaType stringType = mapper().getTypeFactory().constructType(String.class);

        xmlProvider.serializeValue(xmlGenerator, new String[]{"foo", "bar"});
        assertEquals("<root><item>foo</item><item>bar</item></root>", mapper.writeValueAsString(new String[]{"foo", "bar"}));

        xmlProvider.serializeValue(xmlGenerator, "foo");
        assertEquals("<root>foo</root>", mapper.writeValueAsString("foo"));

        xmlProvider.serializeValue(xmlGenerator, (Object) null);
        StaxUtil.writeEndDocument(xmlGenerator);
        String xmlWithNull = xmlGenerator.getBuffer().toString();
        assertTrue(xmlWithNull.startsWith("<null>"));
        xmlWithNull = xmlWithNull.substring(7);
        assertTrue(xmlWithNull.endsWith("</null>"));

        xmlGenerator.setDefaultNamespace("customNamespace");
        xmlProvider.initWithRootName(xmlGenerator, new QName("customRoot", ""));
        assertEquals("<customNamespace:customRoot>", xmlGenerator.getDocumentEvent().getName().getLocalPart());
    }

    @Test
    public void testStartRootArray() throws IOException, XMLStreamException {
        SerializationConfig config = new SerializationConfig();
        config.setSerializationFeatures(Feature.FAIL_ON_EMPTY_BEANS, false);
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup() {
            @Override
            public QName findRootName(JavaType type, SerializationConfig config) {
                return null;
            }
        });

        ObjectMapper mapper = new ObjectMapper(xmlProvider);
        JsonGenerator xmlGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        xmlProvider.startRootArray(xmlGenerator, new QName("items", ""));
        StaxUtil.writeStartDocument(xmlGenerator);
        xmlGenerator.writeStartObject();
        xmlGenerator.writeFieldName("item");

        JavaType stringArrayType = mapper().getTypeFactory().constructParametricType(String[].class, String.class);
        JavaType stringType = mapper().getTypeFactory().constructType(String.class);

        xmlProvider.serializeValue(xmlGenerator, new String[]{"foo", "bar"});
        assertEquals("<items><item>foo</item><item>bar</item></items>", mapper.writeValueAsString(new String[]{"foo", "bar"}));

        xmlProvider.serializeValue(xmlGenerator, "foo");
        assertEquals("<items><item>foo</item></items>", mapper.writeValueAsString(new String[]{"foo"}));

        xmlProvider.serializeValue(xmlGenerator, (Object) null);
        StaxUtil.writeEndDocument(xmlGenerator);
    }
}