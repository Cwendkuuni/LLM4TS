package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

/**
 * Unit tests for {@link XmlSerializerProvider} class.
 */
public class XmlSerializerProviderTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testXmlSerializerProvider() throws JsonMappingException, IOException {
 // Test constructor with root name lookup
 XmlRootNameLookup rootNames = new XmlRootNameLookup();
 XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
 assertNotNull(provider);
 assertEquals(XmlSerializerProvider.class, provider.getClass());
 // Test constructor with source provider
 XmlSerializerProvider sourceProvider = new XmlSerializerProvider(new DefaultSerializerProvider(), MAPPER.getSerializationConfig(), MAPPER.getDefaultSerializerFactory());
 assertNotNull(sourceProvider);
 assertEquals(XmlSerializerProvider.class, sourceProvider.getClass());
 // Test serializeValue() with null value and no root name
 JsonGenerator gen = MAPPER.getFactory().createXmlGenerator(new File("test.xml"), null);
 XmlSerializerProvider provider1 = new XmlSerializerProvider(MAPPER.getSerializationConfig(), MAPPER.getDefaultSerializerFactory());
 provider1.serializeValue(gen, null);
 String xml = gen.getXMLFormatReader().readValueAsString();
 assertEquals("<null/>", xml);
 // Test serializeValue() with non-null value and root name from config
 SerializationConfig config = MAPPER.getSerializationConfig();
 config.setRootValueSeparator("root");
 provider1 = new XmlSerializerProvider(MAPPER.getSerializationConfig(), MAPPER.getDefaultSerializerFactory());
 Object value = new Object();
 provider1.serializeValue(gen, value);
 xml = gen.getXMLFormatReader().readValueAsString();
 assertEquals("<root/><value></value>", xml);
 // Test serializeValue() with non-null value and root name from lookup
 provider1 = new XmlSerializerProvider(MAPPER.getSerializationConfig(), MAPPER.getDefaultSerializerFactory(), rootNames);
 Object value2 = new Object();
 provider1.serializeValue(gen, value2);
 xml = gen.getXMLFormatReader().readValueAsString();
 assertEquals("<rootName:value></rootName:value>", xml);
 // Test serializeValue() with non-null value, root type and serializer
 config.setRootValueSeparator(null);
 JavaType rootType = MAPPER.getTypeFactory().constructType(Object[].class);
 JsonSerializer<Object> ser = MAPPER.findValueSerializer(Object[].class, null);
 provider1 = new XmlSerializerProvider(MAPPER.getSerializationConfig(), MAPPER.getDefaultSerializerFactory(), rootType, ser);
 provider1.serializeValue(gen, value, rootType, ser);
 xml = gen.getXMLFormatReader().readValueAsString();
 assertEquals("<value></value>", xml);
 // Test serializeValue() with non-null value, root type and null serializer
 provider1 = new XmlSerializerProvider(MAPper.getSerializationConfig(), MAPper.getDefaultSerializerFactory(), rootType, null);
 provider1.serializeValue(gen, value, rootType);
 xml = gen.getXMLFormatReader().readValueAsString();
 // This can be customized by implementing a custom serializer for Object type
 assertEquals("<value></value>", xml);
 // Test _asXmlGenerator() method
 JsonGenerator gen2 = MAPPER.getFactory().createXmlGenerator(new File("test2.xml"), null);
 provider1 = new XmlSerializerProvider(MAPper.getSerializationConfig(), MAPper.getDefaultSerializerFactory());
 assertNull(provider1._asXmlGenerator(gen2));
 // Test _asXmlGenerator() method with ToXmlGenerator
 JsonGenerator gen3 = MAPPER.getFactory().createGenerator(new File("test3.xml"), null);
 provider1 = new XmlSerializerProvider(MAPper.getSerializationConfig(), MAPper.getDefaultSerializerFactory());
 assertNotNull(provider1._asXmlGenerator(gen3));
 }

}