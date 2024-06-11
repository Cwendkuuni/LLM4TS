package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.QName;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

@RunWith(PowerMockRunner.class)
@PrepareForTest({XmlSerializerProvider.class})
public class XmlSerializerProviderTest {

 private XmlSerializerProvider provider;
 private JsonGenerator mockJsonGenerator;
 private JsonSerializer<Object> mockSerializer;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 mockJsonGenerator = Mockito.mock(JsonGenerator.class);
 mockSerializer = Mockito.mock(JsonSerializer.class);
 provider = new XmlSerializerProvider(new XmlRootNameLookup() {
 @Override
 public QName findRootName(Class<?> clazz, SerializationConfig config) {
 return PowerMockito.mock(QName.class);
 }
 });
 PowerMockito.whenNew(XmlSerializerProvider.class)
 .withArguments(new XmlRootNameLookup() {
 @Override
 public QName findRootName(Class<?> clazz, SerializationConfig config) {
 return PowerMockito.mock(QName.class);
 }
 }).thenReturn(provider);
 }

 @Test
 public void testSerializeValueNull() throws Exception {
 provider.serializeValue(mockJsonGenerator, null);
 PowerMockito.verifyNew(QName.class).withArguments("null");
 PowerMockito.verifyZeroInteractions(mockJsonGenerator);
 }

 @Test
 public void testSerializeValueObject() throws Exception {
 Object value = new Object();
 provider.serializeValue(mockJsonGenerator, value);
 PowerMockito.verifyNew(JsonSerializer.class).withArguments(value.getClass(), true, null);
 PowerMockito.verify(mockJsonSerializer).serialize(value, mockJsonGenerator, provider);
 }

 @Test
 public void testSerializeValueObjectWithRootType() throws Exception {
 Object value = new Object();
 JavaType rootType = Mockito.mock(JavaType.class);
 JsonSerializer<Object> ser = Mockito.mock(JsonSerializer.class);
 PowerMockito.whenNew(JsonSerializer.class).withArguments(value.getClass(), true, null).thenReturn(ser);
 provider.serializeValue(mockJsonGenerator, value, rootType, ser);
 PowerMockito.verifyNew(JsonSerializer.class).withArguments(value.getClass(), true, null);
 PowerMockito.verify(mockJsonSerializer).serialize(value, mockJsonGenerator, provider);
 }

 @Test
 public void testSerializeValueNullWithRootType() throws Exception {
 Object value = null;
 JavaType rootType = Mockito.mock(JavaType.class);
 provider.serializeValue(mockJsonGenerator, value, rootType, null);
 PowerMockito.verifyNew(QName.class).withArguments("null");
 PowerMockito.verifyZeroInteractions(mockJsonGenerator);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithInvalidGenerator() throws Exception {
 provider.serializeValue(Mockito.mock(ObjectWriter.class), new Object());
 fail("Expected JsonMappingException");
 }

 @Test
 public void testSerializeValueWithNullSerializer() throws Exception {
 provider.serializeValue(mockJsonGenerator, "test", null);
 PowerMockito.verifyZeroInteractions(mockJsonGenerator);
 }

 @Test
 public void testSerializeValueWithExceptionInSerializer() throws Exception {
 provider.serializeValue(mockJsonGenerator, new Object(), mockSerializer, Mockito.mock(IOException.class));
 PowerMockito.verifyNew(JsonMappingException.class).withArguments(mockJsonGenerator, "[ioe]", Mockito.any(IOException.class));
 }

 @Test
 public void test_initWithRootName() throws IOException, XMLStreamException {
 ToXmlGenerator xgen = Mockito.mock(ToXmlGenerator.class);
 QName rootName = Mockito.mock(QName.class);
 PowerMockito.when(xgen.getStaxWriter()).thenReturn(Mockito.mock(XMLStreamWriter.class));
 provider._initWithRootName(xgen, rootName);
 PowerMockito.verify(xgen).setNextName(rootName);
 PowerMockito.verify(xgen).initGenerator();
 PowerMockito.verify(xgen.getStaxWriter()).setDefaultNamespace(rootName.getNamespaceURI());
 }

 @Test(expected = XMLStreamException.class)
 public void test_initWithRootName_XMLStreamException() throws IOException, XMLStreamException {
 ToXmlGenerator xgen = Mockito.mock(ToXmlGenerator.class);
 QName rootName = Mockito.mock(QName.class);
 PowerMockito.when(xgen.getStaxWriter()).thenThrow(XMLStreamException.class);
 provider._initWithRootName(xgen, rootName);
 fail("Expected XMLStreamException");
 }
}