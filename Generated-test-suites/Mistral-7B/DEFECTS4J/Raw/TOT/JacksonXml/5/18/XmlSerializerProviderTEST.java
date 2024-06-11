package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.StdName;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlRootNameLookup;

public class XmlSerializerProviderTest {

 public static void main(String[] args) throws Exception {
 XmlMapper mapper = new XmlMapper();
 mapper.setSerializerProvider(new XmlSerializerProvider(new DefaultXmlRootNameLookup()));
 // Test serialization of a simple JavaBean with no root name
 SimpleModule module = new SimpleModule();
 module.addSerializer(SimpleBean.class, new SimpleBeanSerializer());
 mapper.registerModule(module);
 ObjectWriter writer = mapper.writerWithRootFilter(SimpleBeanFilter.FILTER_NAME);
 SimpleBean bean = new SimpleBean("John Doe", 30);
 String xml = writer.writeValueAsString(bean);
 System.out.println(xml);
 // Test serialization of a null value
 SimpleBean nullBean = null;
 xml = writer.writeValueAsString(nullBean);
 System.out.println(xml);
 // Test serialization with explicitly set root name
 SimpleBean rootBean = new SimpleBean("Jane Doe", 25);
 QName rootName = new QName("http://example.com", "rootElement");
 SimpleBean rootWrapper = new SimpleBean("root", rootBean);
 ObjectWriter rootWriter = mapper.writerWithRootName(rootName);
 String rootXml = rootWriter.writeValueAsString(rootWrapper);
 System.out.println(rootXml);
 // Test deserialization of the simple JavaBean with no root name
 SimpleBean deserializedBean = mapper.readerForType(SimpleBean.class).readValue(new ByteArrayInputStream(xml.getBytes()));
 assertEquals(bean.getName(), deserializedBean.getName());
 assertEquals(bean.getAge(), deserializedBean.getAge());
 // Test deserialization of a null value
 deserializedBean = mapper.readerForType(SimpleBean.class).readValue(new ByteArrayInputStream(xml.substring(xml.indexOf("null")).getBytes()));
 assertNull(deserializedBean);
 // Test deserialization with explicitly set root name
 SimpleBean deserializedRootBean = mapper.readerForType(SimpleBean.class).readValue(new ByteArrayInputStream(rootXml.getBytes()));
 assertEquals(rootBean.getName(), deserializedRootBean.getName());
 assertEquals(rootBean.getAge(), deserializedRootBean.getAge());
 }

 static class SimpleBeanSerializer extends JsonSerializer<SimpleBean> {
 @Override
 public void serialize(SimpleBean value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeStartObject();
 gen.writeFieldName("name");
 gen.writeString(value.getName());
 gen.writeFieldName("age");
 gen.writeNumber(value.getAge());
 gen.writeEndObject();
 }
 }

 static class SimpleBeanFilter extends FilterProvider.FilterTemplate<Object> {
 static final String FILTER_NAME = "SimpleBeanFilter";

 @Override
 public SimpleBeanFilter addFilterInfo(SimpleBeanFilter baseFilter, FilterProvider filterProvider) {
 return new SimpleBeanFilter();
 }

 @Override
 public void serializeFilter(SerializerProvider serializers, FilterConfig filterConfig, JavaType type, Object value) throws IOException, JsonProcessingException {
 SimpleBean bean = (SimpleBean) value;
 SerializerFactory serializerFactory = serializers.getFactory();
 JsonSerializer<SimpleBean> serializer = serializerFactory.findValueSerializer(SimpleBean.class);
 ObjectWriter writer = serializer.getSerializerProvider().getOutputFactory().createOutput(serializer, null);
 writer.writeValue(bean);
 }
 }

 static class SimpleBean {
 private String name;
 private int age;

 public SimpleBean(String name, int age) {
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
}