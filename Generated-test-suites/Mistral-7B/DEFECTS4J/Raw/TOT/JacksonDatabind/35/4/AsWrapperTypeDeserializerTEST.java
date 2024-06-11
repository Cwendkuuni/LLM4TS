package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 /**
 * Test deserializing a simple JavaBean using AsWrapperTypeDeserializer.
 */
 @Test
 public void testDeserializeSimpleJavaBean() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(SimpleJavaBean.class);
 TypeDeserializer typeDeserializer = new AsWrapperTypeDeserializer(javaType, mapper.getTypeIdResolver(), "_type", true, SimpleJavaBean.class);
 String json = "{\"_type\":\"com.fasterxml.jackson.databind.jsontype.impl.SimpleJavaBean\",\"name\":\"John Doe\",\"age\":30}";
 SimpleJavaBean deserializedObject = mapper.readValue(json, SimpleJavaBean.class, typeDeserializer);
 assertEquals("John Doe", deserializedObject.getName());
 assertEquals(30, deserializedObject.getAge());
 }

 /**
 * Test deserializing a JavaBean with a null _type property.
 */
 @Test(expected = IOException.class)
 public void testDeserializeNullTypeProperty() throws Exception {
 String json = "{\"name\":\"John Doe\",\"age\":30}";
 mapper.readValue(json, SimpleJavaBean.class, new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(SimpleJavaBean.class), mapper.getTypeIdResolver(), "_type", true, SimpleJavaBean.class));
 }

 /**
 * Test deserializing a JavaBean with an invalid _type property.
 */
 @Test(expected = IOException.class)
 public void testDeserializeInvalidTypeProperty() throws Exception {
 String json = "{\"_type\":\"com.fasterxml.jackson.databind.jsontype.impl.InvalidType\",\"name\":\"John Doe\",\"age\":30}";
 mapper.getTypeIdResolver().setValueIdEventReader(mapper.getFactory().createParser("com.fasterxml.jackson.databind.jsontype.IdResolverTest$InvalidType"));
 mapper.readValue(json, SimpleJavaBean.class, new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(SimpleJavaBean.class), mapper.getTypeIdResolver(), "_type", true, SimpleJavaBean.class));
 }

 /**
 * Test deserializing a JavaBean with an empty _type property.
 */
 @Test(expected = IOException.class)
 public void testDeserializeEmptyTypeProperty() throws Exception {
 String json = "{\"_type\":\"\",\"name\":\"John Doe\",\"age\":30}";
 mapper.readValue(json, SimpleJavaBean.class, new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(SimpleJavaBean.class), mapper.getTypeIdResolver(), "_type", true, SimpleJavaBean.class));
 }

 /**
 * Test deserializing a JavaBean with an incorrectly formatted JSON.
 */
 @Test(expected = IOException.class)
 public void testDeserializeIncorrectlyFormattedJSON() throws Exception {
 String json = "{\"name\":\"John Doe\",\"age\":30_type\":\"com.fasterxml.jackson.databind.jsontype.impl.SimpleJavaBean\"}";
 mapper.readValue(json, SimpleJavaBean.class, new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(SimpleJavaBean.class), mapper.getTypeIdResolver(), "_type", true, SimpleJavaBean.class));
 }

}

class SimpleJavaBean {
 private String name;
 private int age;

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