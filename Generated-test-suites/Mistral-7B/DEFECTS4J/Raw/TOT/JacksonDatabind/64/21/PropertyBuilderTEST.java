package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig _config;
 private ObjectMapper _mapper;
 private PropertyBuilder _propertyBuilder;

 @Before
 public void setup() throws Exception {
 _config = new SerializationConfig();
 _mapper = new ObjectMapper(_config);
 _propertyBuilder = new PropertyBuilder(_config, _mapper.findBeanDescription(MyPojo.class));
 }

 @Test
 public void testDefaultValues() throws Exception {
 MyPojo defaultBean = _propertyBuilder.getDefaultBean();
 assertNull(defaultBean);

 MyPojo pojo = new MyPojo();
 pojo.setName("test");
 pojo.setNumber(1);
 pojo.setBoolean(true);
 pojo.setList(new Integer[]{1, 2, 3});
 pojo.setMap(new HashMap<>());

 Annotations annotations = _propertyBuilder.getClassAnnotations();
 BeanPropertyWriter writer = _propertyBuilder.buildWriter(_mapper.getSerializationConfig().getDefaultSerializerProvider(),
 _propertyBuilder.getBeanPropertyDefinition("name"), _propertyBuilder.getBeanPropertyDefinition("number"),
 _mapper.getSerializerProvider().findValueSerializer(MyPojo.class, "number"), null, null, _propertyBuilder.getAnnotatedMember("name"), false);

 Object nameValue = writer.getValue(pojo);
 assertEquals("test", nameValue);

 writer = _propertyBuilder.buildWriter(_mapper.getSerializationConfig().getDefaultSerializerProvider(),
 _propertyBuilder.getBeanPropertyDefinition("number"), _propertyBuilder.getBeanPropertyDefinition("number"),
 _mapper.getSerializerProvider().findValueSerializer(MyPojo.class, "number"), null, null, _propertyBuilder.getAnnotatedMember("number"), false);
 assertEquals(1, writer.getValue(pojo));

 writer = _propertyBuilder.buildWriter(_mapper.getSerializationConfig().getDefaultSerializerProvider(),
 _propertyBuilder.getBeanPropertyDefinition("boolean"), _propertyBuilder.getBeanPropertyDefinition("boolean"),
 _mapper.getSerializerProvider().findValueSerializer(MyPojo.class, "boolean"), null, null, _propertyBuilder.getAnnotatedMember("boolean"), false);
 assertTrue((Boolean) writer.getValue(pojo));

 writer = _propertyBuilder.buildWriter(_mapper.getSerializationConfig().getDefaultSerializerProvider(),
 _propertyBuilder.getBeanPropertyDefinition("list"), _propertyBuilder.getBeanPropertyDefinition("list"),
 _mapper.getSerializerProvider().findValueSerializer(MyPojo.class, "list"), null, null, _propertyBuilder.getAnnotatedMember("list"), false);
 assertArrayEquals(new Integer[]{1, 2, 3}, writer.getValue(pojo));

 writer = _propertyBuilder.buildWriter(_mapper.getSerializationConfig().getDefaultSerializerProvider(),
 _propertyBuilder.getBeanPropertyDefinition("map"), _propertyBuilder.getBeanPropertyDefinition("map"),
 _mapper.getSerializerProvider().findValueSerializer(MyPojo.class, "map"), null, null, _propertyBuilder.getAnnotatedMember("map"), false);
 assertEquals(pojo.getMap(), writer.getValue(pojo));
 }

 @Test
 public void testNullValues() throws Exception {
 MyPojo pojo = new MyPojo();
 pojo.setName(null);
 pojo.setNumber(null);
 pojo.setBoolean(null);
 pojo.setList(null);
 pojo.setMap(null);

 Annotations annotations = _propertyBuilder.getClassAnnotations();
 BeanPropertyWriter writer = _propertyBuilder.buildWriter(_mapper.getSerializationConfig().getDefaultSerializerProvider(),
 _propertyBuilder.getBeanPropertyDefinition("name"), _propertyBuilder.getBeanPropertyDefinition("name"),
 _mapper.getSerializerProvider().findValueSerializer(MyPojo.class, "name"), null, null, _propertyBuilder.getAnnotatedMember("name"), false);
 assertNull(writer.getValue(pojo));

 writer = _propertyBuilder.buildWriter(_propertyBuilder.getBeanPropertyDefinition("number"), _propertyBuilder.getBeanPropertyDefinition("number"),
 _mapper.getSerializerProvider().findValueSerializer(MyPojo.class, "number"), null, null, _propertyBuilder.getAnnotatedMember("number"), false);
 assertNull(writer.getValue(pojo));

 writer = _propertyBuilder.buildWriter(_propertyBuilder.getBeanPropertyDefinition("boolean"), _propertyBuilder.getBeanPropertyDefinition("boolean"),
 _mapper.getSerializerProvider().findValueSerializer(MyPojo.class, "boolean"), null, null, _propertyBuilder.getAnnotatedMember("boolean"), false);
 assertNull(writer.getValue(pojo));

 writer = _propertyBuilder.buildWriter(_propertyBuilder.getBeanPropertyDefinition("list"), _propertyBuilder.getBeanPropertyDefinition("list"),
 _mapper.getSerializerProvider().findValueSerializer(MyPojo.class, "list"), null, null, _propertyBuilder.getAnnotatedMember("list"), false);
 assertNull(writer.getValue(pojo));

 writer = _propertyBuilder.buildWriter(_propertyBuilder.getBeanPropertyDefinition("map"), _propertyBuilder.getBeanPropertyDefinition("map"),
 _mapper.getSerializerProvider().findValueSerializer(MyPojo.class, "map"), null, null, _propertyBuilder.getAnnotatedMember("map"), false);
 assertNull(writer.getValue(pojo));
 }

 @Test
 public void testNonDefaultValues() throws Exception {
 MyPojo pojo = new MyPojo();
 pojo.setName("test");
 pojo.setNumber(2);
 pojo.setBoolean(false);
 pojo.setList(new Integer[]{4, 5, 6});
 pojo.setMap(new HashMap<>());

 Annotations annotations = _propertyBuilder.getClassAnnotations();
 BeanPropertyWriter writer = _propertyBuilder.buildWriter(_mapper.getSerializationConfig().getDefaultSerializerProvider(),
 _propertyBuilder.getBeanPropertyDefinition("name"), _propertyBuilder.getBeanPropertyDefinition("name"),
 _mapper.getSerializerProvider().findValueSerializer(MyPojo.class, "name"), null, null, _propertyBuilder.getAnnotatedMember("name"), false);
 assertNotEquals("test", writer.getDefaultValue());
 assertEquals("test", writer.getValue(pojo));

 writer = _propertyBuilder.buildWriter(_propertyBuilder.getBeanPropertyDefinition("number"), _propertyBuilder.getBeanPropertyDefinition("number"),
 _mapper.getSerializerProvider().findValueSerializer(MyPojo.class, "number"), null, null, _propertyBuilder.getAnnotatedMember("number"), false);
 assertNotEquals(1, writer.getDefaultValue());
 assertEquals(2, writer.getValue(pojo));

 writer = _propertyBuilder.buildWriter(_propertyBuilder.getBeanPropertyDefinition("boolean"), _propertyBuilder.getBeanPropertyDefinition("boolean"),
 _mapper.getSerializerProvider().findValueSerializer(MyPojo.class, "boolean"), null, null, _propertyBuilder.getAnnotatedMember("boolean"), false);
 assertNotEquals(true, writer.getDefaultValue());
 assertFalse((Boolean) writer.getValue(pojo));

 writer = _propertyBuilder.buildWriter(_propertyBuilder.getBeanPropertyDefinition("list"), _propertyBuilder.getBeanPropertyDefinition("list"),
 _mapper.getSerializerProvider().findValueSerializer(MyPojo.class, "list"), null, null, _propertyBuilder.getAnnotatedMember("list"), false);
 assertNotSame(new Integer[]{1, 2, 3}, writer.getDefaultValue());
 assertArrayEquals(new Integer[]{4, 5, 6}, writer.getValue(pojo));

 writer = _propertyBuilder.buildWriter(_propertyBuilder.getBeanPropertyDefinition("map"), _propertyBuilder.getBeanPropertyDefinition("map"),
 _mapper.getSerializerProvider().findValueSerializer(MyPojo.class, "map"), null, null, _propertyBuilder.getAnnotatedMember("map"), false);
 assertNotSame(pojo.getMap(), writer.getDefaultValue());
 assertEquals(pojo.getMap(), writer.getValue(pojo));
 }

 @Test
 public void testJsonInclude() throws Exception {
 MyPojo pojo = new MyPojo();
 pojo.setName("test");
 pojo.setNumber(2);
 pojo.setBoolean(false);
 pojo.setList(new Integer[]{4, 5, 6});
 pojo.setMap(new HashMap<>());

 Annotations annotations = _propertyBuilder.getClassAnnotations();
 JsonInclude jsonInclude = _propertyBuilder.getBeanPropertyDefinition("number").findInclusion();
 JsonInclude.Include inclusion = jsonInclude.getValueInclusion();

 if (inclusion == JsonInclude.Include.NON_DEFAULT) {
 // test default value
 BeanPropertyWriter writer = _propertyBuilder.buildWriter(_mapper.getSerializationConfig().getDefaultSerializerProvider(),
 _propertyBuilder.getBeanPropertyDefinition("number"), _propertyBuilder.getBeanPropertyDefinition("number"),
 _mapper.getSerializerProvider().findValueSerializer(MyPojo.class, "number"), null, null, _propertyBuilder.getAnnotatedMember("number"), false);
 assertNotEquals(1, writer.getDefaultValue());
 assertNull(writer.getValue(pojo));
 }

 // test non-default value
 if (inclusion == JsonInclude.Include.NON_DEFAULT && pojo.getNumber() != 1) {
 BeanPropertyWriter writer = _propertyBuilder.buildWriter(_mapper.getSerializationConfig().getDefaultSerializerProvider(),
 _propertyBuilder.getBeanPropertyDefinition("number"), _propertyBuilder.getBeanPropertyDefinition("number"),
 _mapper.getSerializerProvider().findValueSerializer(MyPojo.class, "number"), null, null, _propertyBuilder.getAnnotatedMember("number"), false);
 assertEquals(pojo.getNumber(), writer.getValue(pojo));
 }
 }
}

class MyPojo {
 private String name;
 private Integer number;
 private Boolean booleanValue;
 private List list;
 private Map map;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public Integer getNumber() {
 return number;
 }

 public void setNumber(Integer number) {
 this.number = number;
 }

 public Boolean getBoolean() {
 return booleanValue;
 }

 public void setBoolean(Boolean booleanValue) {
 this.booleanValue = booleanValue;
 }

 public List getList() {
 return list;
 }

 public void setList(List list) {
 this.list = list;
 }

 public Map getMap() {
 return map;
 }

 public void setMap(Map map) {
 this.map = map;
 }
}