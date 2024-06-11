package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.QName;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testDefaultConstructor() {
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(null);
 assertNotNull(xmlProvider);
 }

 @Test
 public void testXmlSerializerProviderWithRootNames() throws Exception {
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup(null));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializationConfig config = mapper.getSerializationConfig();
 config.setSerializationConfig(xmlProvider);
 mapper.writeValue(gen, new Integer(1));
 gen.close();
 }

 @Test
 public void testXmlSerializerProviderWithRootNamesAndRootType() throws Exception {
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup(null));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
 SerializationConfig config = mapper.getSerializationConfig();
 config.setSerializationConfig(xmlProvider);
 mapper.writeValue(gen, new String[]{"Hello", "World"}, rootType);
 gen.close();
 }

 @Test
 public void testXmlSerializerProviderCopy() throws Exception {
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(mapper.getSerializationConfig(), mapper.getDefaultSerializerProvider());
 XmlSerializerProvider copiedProvider = new XmlSerializerProvider(xmlProvider);
 assertNotSame(xmlProvider, copiedProvider);
 assertEquals(xmlProvider.getRootNameLookup(), copiedProvider.getRootNameLookup());
}

 @Test
 public void testSerializeValueWithNullValue() throws Exception {
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(mapper.getSerializationConfig(), mapper.getDefaultSerializerProvider());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider.serializeValue(gen, null);
 gen.close();
}

 @Test
 public void testSerializeValueWithNonArrayType() throws Exception {
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(mapper.getSerializationConfig(), mapper.getDefaultSerializerProvider());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 Object value = new Object();
 xmlProvider.serializeValue(gen, value);
 gen.close();
}

 @Test
 public void testSerializeValueWithArrayType() throws Exception {
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(mapper.getSerializationConfig(), mapper.getDefaultSerializerProvider());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 Object[] values = new Object[] {new Object(), new Object()};
 xmlProvider.serializeValue(gen, values);
 gen.writeEndObject();
 gen.close();
}

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithInvalidType() throws Exception {
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(mapper.getSerializationConfig(), mapper.getDefaultSerializerProvider());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 String invalidType = "InvalidType";
 xmlProvider.serializeValue(gen, invalidType);
 gen.close();
}

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithUnsupportedRootType() throws Exception {
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(mapper.getSerializationConfig(), mapper.getDefaultSerializerProvider());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 JavaType rootType = mapper.getTypeFactory().constructType(String.class);
 xmlProvider.serializeValue(gen, new Object(), rootType, null);
 gen.close();
}

 @Test
 public void testSerializeValueWithNullRootType() throws Exception {
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(mapper.getSerializationConfig(), mapper.getDefaultSerializerProvider());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 JavaType rootType = null;
 xmlProvider.serializeValue(gen, new Object(), rootType, null);
 gen.close();
}

 @Test
 public void testSerializeValueWithNonExistentRootName() throws Exception {
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(mapper.getSerializationConfig(), mapper.getDefaultSerializerProvider());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 Object value = new Object();
 xmlProvider.serializeValue(gen, value, new QName("non-existent"));
 gen.close();
}

 @Test
 public void testInitializeWithRootName() throws Exception {
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(mapper.getSerializationConfig(), mapper.getDefaultSerializerProvider());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, new QName("RootElement"));
 xmlProvider.initWithRootName(gen, new QName("RootElement"));
 xmlProvider.serializeValue(gen, new Object());
 gen.writeEndObject();
 gen.close();
}

 @Test(expected = IOException.class)
 public void testInitializeWithNullGenerator() throws Exception {
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(mapper.getSerializationConfig(), mapper.getDefaultSerializerProvider());
 xmlProvider.initWithRootName(null, new QName("RootElement"));
}

 @Test(expected = IOException.class)
 public void testInitializeWithNullRootName() throws Exception {
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(mapper.getSerializationConfig(), mapper.getDefaultSerializerProvider());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider.initWithRootName(gen, null);
}

 @Test(expected = XMLStreamException.class)
 public void testSetDefaultNamespace() throws Exception {
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(mapper.getSerializationConfig(), mapper.getDefaultSerializerProvider());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider.initWithRootName(gen, new QName("RootElement"));
 xmlProvider.setDefaultNamespace(gen, "http://example.com");
 xmlProvider.serializeValue(gen, new Object());
 gen.writeStartDocument(); // This should throw XMLStreamException
 gen.close();
}
}