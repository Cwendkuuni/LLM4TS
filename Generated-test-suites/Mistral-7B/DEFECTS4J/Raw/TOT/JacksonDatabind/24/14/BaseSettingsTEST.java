package com.fasterxml.jackson.databind.cfg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BaseSettingsTest {

 private BaseSettings settings;
 private ObjectMapper mapper;

 @Before
 public void setUp() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<Object> visibilityChecker = new VisibilityChecker.WithDefault(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);
 PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.Default();
 TypeFactory typeFactory = new TypeFactory();
 TypeResolverBuilder<?> typeResolverBuilder = new SimpleTypeResolverBuilder();
 DateFormat dateFormat = new StdDateFormat();
 HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variant.STANDARD;
 settings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
 typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);
 mapper = new ObjectMapper();
 mapper.setConfiguration(settings);
 }

 @Test
 public void testSerializationWithDefaultSettings() throws JsonProcessingException {
 MyClass obj = new MyClass();
 obj.setName("Test");
 obj.setAge(25);
 obj.setDate(LocalDateTime.now());

 String json = mapper.writeValueAsString(obj);
 assertEquals("{\"name\":\"Test\",\"age\":25,\"date\":\"2022-10-24T13:25:41.841-07:00\"}", json);
 }

 @Test
 public void testSerializationWithCustomDateFormat() throws JsonProcessingException {
 DateTimeFormatter customDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
 settings = settings.withDateFormat(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z").withLocale(Locale.CANADA));
 String json = mapper.writeValueAsString(new MyClass());
 assertEquals("{\"name\":null,\"age\":null,\"date\":\"2022-10-24 13:25:41-07:00\"}", json);
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializationWithNullObject() throws JsonProcessingException {
 mapper.writeValueAsString(null);
 }

 @Test
 public void testDeserializationWithDefaultSettings() throws IOException {
 String json = "{\"name\":\"Test\",\"age\":25,\"date\":\"2022-10-24T13:25:41.841-07:00\"}";
 MyClass obj = mapper.readValue(json, MyClass.class);
 assertEquals("Test", obj.getName());
 assertEquals(25, obj.getAge());
 assertNotNull(obj.getDate());
 }

 @Test
 public void testDeserializationWithCustomDateFormat() throws IOException {
 DateTimeFormatter customDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
 settings = settings.withDateFormat(customDateFormat.withLocale(Locale.CANADA));
 String json = "{\"name\":null,\"age\":null,\"date\":\"2022-10-24 13:25:41-07:00\"}";
 MyClass obj = mapper.readValue(json, MyClass.class);
 assertNotNull(obj);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithInvalidJson() throws IOException, JsonMappingException {
 mapper.readValue("invalid json", MyClass.class);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithMissingProperty() throws IOException, JsonMappingException {
 String json = "{\"name\":\"Test\"}";
 mapper.readValue(json, MyClass.class);
 }
}

class MyClass {
 private String name;
 private int age;
 private LocalDateTime date;

 public String getName() { return name; }
 public void setName(String name) { this.name = name; }

 public int getAge() { return age; }
 public void setAge(int age) { this.age = age; }

 public LocalDateTime getDate() { return date; }
 public void setDate(LocalDateTime date) { this.date = date; }
}