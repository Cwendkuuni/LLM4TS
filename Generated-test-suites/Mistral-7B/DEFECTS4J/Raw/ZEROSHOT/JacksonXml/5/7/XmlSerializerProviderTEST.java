package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

@RunWith(MockitoJUnitRunner.class)
public class XmlSerializerProviderTest {

 private static final QName ROOT_NAME_FOR_NULL = new QName("null");

 @InjectMocks
 private XmlSerializerProvider xmlSerializerProvider;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testSerializeValueNull() throws IOException {
 JsonGenerator mockJsonGenerator = Mockito.mock(JsonGenerator.class);
 Mockito.when(mockJsonGenerator.writeStartObject()).thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeEndObject()).thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeStartElement("", "null", ""))
 .thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeEndElement(""))
 .thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeNull()).thenReturn(mockJsonGenerator);

 ToXmlGenerator toXmlGenerator = Mockito.mock(ToXmlGenerator.class);
 Mockito.when(toXmlGenerator.writeStartObject()).thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeEndObject()).thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeStartElement("", "null", "null"))
 .thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeEndElement(""))
 .thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeNull())
 .thenReturn(toXmlGenerator);

 when(xmlSerializerProvider._asXmlGenerator(mockJsonGenerator)).thenReturn(toXmlGenerator);

 xmlSerializerProvider.serializeValue(mockJsonGenerator, null);

 verify(mockJsonGenerator).writeStartObject();
 verify(mockJsonGenerator).writeStartElement("", "null", "null");
 verify(mockJsonGenerator).writeNull();
 verify(mockJsonGenerator).writeEndElement();
 verify(mockJsonGenerator).writeEndObject();
 }

 @Test
 public void testSerializeValueObject() throws IOException {
 String jsonString = "{\"name\":\"John\",\"age\":30}";
 ObjectMapper mapper = new ObjectMapper();
 JsonNode jsonNode = mapper.readTree(jsonString);
 SimpleBean simpleBean = mapper.treeToValue(jsonNode, SimpleBean.class);

 JsonGenerator mockJsonGenerator = Mockito.mock(JsonGenerator.class);
 Mockito.when(mockJsonGenerator.writeStartObject()).thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeEndObject()).thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeStartElement("", "name", ""))
 .thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeString("John"))
 .thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeEndElement())
 .thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeStartElement("", "age", ""))
 .thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeNumber(30))
 .thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeEndElement())
 .thenReturn(mockJsonGenerator);

 ToXmlGenerator toXmlGenerator = Mockito.mock(ToXmlGenerator.class);
 Mockito.when(toXmlGenerator.writeStartObject()).thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeEndObject()).thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeStartElement("", "name", ""))
 .thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeString("John"))
 .thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeEndElement())
 .thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeStartElement("", "age", ""))
 .thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeNumber(30))
 .thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeEndElement())
 .thenReturn(toXmlGenerator);

 when(xmlSerializerProvider._asXmlGenerator(mockJsonGenerator)).thenReturn(toXmlGenerator);

 JsonSerializer<Object> jsonSerializer = Mockito.mock(JsonSerializer.class);
 Mockito.when(jsonSerializer.serialize(simpleBean, mockJsonGenerator, xmlSerializerProvider))
 .thenReturn(mockJsonGenerator);

 xmlSerializerProvider.serializeValue(mockJsonGenerator, simpleBean);

 verify(mockJsonGenerator).writeStartObject();
 verify(mockJsonGenerator).writeStartElement("", "name", "");
 verify(mockJsonGenerator).writeString("John");
 verify(mockJsonGenerator).writeEndElement();
 verify(mockJsonGenerator).writeStartElement("", "age", "");
 verify(mockJsonGenerator).writeNumber(30);
 verify(mockJsonGenerator).writeEndElement();
 verify(mockJsonGenerator).writeEndObject();
 }

 @Test
 public void testSerializeValueObjectWithRootType() throws IOException {
 SimpleBean simpleBean = new SimpleBean("John", 30);

 JsonGenerator mockJsonGenerator = Mockito.mock(JsonGenerator.class);
 Mockito.when(mockJsonGenerator.writeStartObject()).thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeEndObject()).thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeStartElement("", "name", ""))
 .thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeString("John"))
 .thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeEndElement())
 .thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeStartElement("", "age", ""))
 .thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeNumber(30))
 .thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeEndElement())
 .thenReturn(mockJsonGenerator);

 ToXmlGenerator toXmlGenerator = Mockito.mock(ToXmlGenerator.class);
 Mockito.when(toXmlGenerator.writeStartObject()).thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeEndObject()).thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeStartElement("", "name", ""))
 .thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeString("John"))
 .thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeEndElement())
 .thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeStartElement("", "age", ""))
 .thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeNumber(30))
 .thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeEndElement())
 .thenReturn(toXmlGenerator);

 when(xmlSerializerProvider._asXmlGenerator(mockJsonGenerator)).thenReturn(toXmlGenerator);

 SimpleBeanRootType simpleBeanRootType = new SimpleBeanRootType();
 JavaType javaType = mapper.getTypeFactory()
 .constructType(SimpleBean.class);

 JsonSerializer<Object> jsonSerializer = Mockito.mock(JsonSerializer.class);
 Mockito.when(jsonSerializer.serialize(simpleBean, mockJsonGenerator, xmlSerializerProvider))
 .thenReturn(mockJsonGenerator);
 Mockito.when(jsonSerializer.serialize(simpleBeanRootType, mockJsonGenerator, xmlSerializerProvider))
 .thenReturn(mockJsonGenerator);

 xmlSerializerProvider.serializeValue(mockJsonGenerator, simpleBean, javaType, jsonSerializer);

 verify(mockJsonGenerator).writeStartObject();
 verify(mockJsonGenerator).writeStartElement("name", "");
 verify(mockJsonGenerator).writeString("John");
 verify(mockJsonGenerator).writeEndElement();
 verify(mockJsonGenerator).writeStartElement("age", "");
 verify(mockJsonGenerator).writeNumber(30);
 verify(mockJsonGenerator).writeEndElement();
 verify(mockJsonGenerator).writeEndObject();
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueObjectWithInvalidGenerator() throws IOException {
 String jsonString = "{\"name\":\"John\",\"age\":30}";
 ObjectMapper mapper = new ObjectMapper();
 JsonNode jsonNode = mapper.readTree(jsonString);
 SimpleBean simpleBean = mapper.treeToValue(jsonNode, SimpleBean.class);

 JsonGenerator mockJsonGenerator = Mockito.mock(JsonGenerator.class);
 Mockito.when(mockJsonGenerator.writeStartObject()).thenThrow(new IOException());
 Mockito.when(mockJsonGenerator.writeEndObject()).thenReturn(mockJsonGenerator);

 xmlSerializerProvider.serializeValue(mockJsonGenerator, simpleBean);

 // should throw exception
 }

 @Test
 public void testSerializeValueArray() throws IOException {
 Integer[] simpleArray = {1, 2, 3};

 JsonGenerator mockJsonGenerator = Mockito.mock(JsonGenerator.class);
 Mockito.when(mockJsonGenerator.writeStartObject())
 .thenThrow(new IOException("root object generation failed"));
 Mockito.when(mockJsonGenerator.writeStartArray("item"))
 .thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeValue(1))
 .thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeValue(2))
 .thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeValue(3))
 .thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeEndArray())
 .thenReturn(mockJsonGenerator);
 Mockito.when(mockJsonGenerator.writeEndObject())
 .thenReturn(mockJsonGenerator);

 ToXmlGenerator toXmlGenerator = Mockito.mock(ToXmlGenerator.class);
 Mockito.when(toXmlGenerator.writeStartObject()).thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeEndObject()).thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeStartArray("item"))
 .thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeValue(1))
 .thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeValue(2))
 .thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeValue(3))
 .thenReturn(toXmlGenerator);
 Mockito.when(toXmlGenerator.writeEndArray())
 .thenReturn(toXmlGenerator);

 when(xmlSerializerProvider._asXmlGenerator(mockJsonGenerator)).thenReturn(toXmlGenerator);

 JsonSerializer<Object> jsonSerializer = Mockito.mock(JsonSerializer.class);
 Mockito.when(jsonSerializer.serialize(simpleArray[0], mockJsonGenerator, xmlSerializerProvider))
 .thenReturn(mockJsonGenerator);
 Mockito.when(jsonSerializer.serialize(simpleArray[1], mockJsonGenerator, xmlSerializerProvider))
 .thenReturn(mockJsonGenerator);
 Mockito.when(jsonSerializer.serialize(simpleArray[2], mockJsonGenerator, xmlSerializerProvider))
 .thenReturn(mockJsonGenerator);

 xmlSerializerProvider.serializeValue(mockJsonGenerator, simpleArray);

 verify(mockJsonGenerator).writeStartObject();
 verify(mockJsonGenerator, times(2)).writeStartArray("item");
 verify(mockJsonGenerator, times(3)).writeValue(1);
 verify(mockJsonGenerator, times(3)).writeValue(2);
 verify(mockJsonGenerator, times(3)).writeValue(3);
 verify(mockJsonGenerator).writeEndArray();
 verify(mockJsonGenerator).writeEndObject();
 }

}

class SimpleBean {
 private String name;
 private int age;

 public SimpleBean() {
 }

 public SimpleBean(String name, int age) {
 this.name = name;
 this.age = age;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public int getAge() {
 return age;
 }

 public void setAge(int age) {
 this.age = age;
 }
}

class SimpleBeanRootType {
 private SimpleBean simpleBean;

 public SimpleBean getSimpleBean() {
 return simpleBean;
 }

 public void setSimpleBean(SimpleBean simpleBean) {
 this.simpleBean = simpleBean;
 }
}