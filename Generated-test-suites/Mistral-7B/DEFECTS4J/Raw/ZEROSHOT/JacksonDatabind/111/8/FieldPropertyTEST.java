package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.NullsConstantProvider;

public class FieldPropertyTest {

 private static final class TestBean {
 private int field;
 }

 @org.junit.Test
 public void testDeserialization() throws Exception {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{ \"field\": 42 }");

 FieldProperty property = new FieldProperty(new BeanPropertyDefinition(
 new AnnotatedField(TestBean.class.getDeclaredField("field"),
 new Annotation[0]),
 new BeanPropertyDefinition(null, null, TestBean.class, null, null)),
 mapper.getTypeFactory().constructType(TestBean.class), null, null,
 new AnnotatedField(TestBean.class.getDeclaredField("field"),
 new Annotation[0]));

 when(property._field.getType()).thenReturn(Integer.TYPE);
 when(property._field.canAccess(TestBean.class)).thenReturn(true);
 when(property._nullProvider.isEnabled(null)).thenReturn(false);

 property.deserializeAndSet(parser, mock(DeserializationContext.class), bean);
 assertEquals(42, bean.field);

 parser.nextToken(); // consume remaining "}" token

 property.deserializeAndSet(parser, mock(DeserializationContext.class), bean);
 assertNull(bean.field); // should skip nulls due to NullValueProvider
 }

 @org.junit.Test
 public void testDeserializationWithNullProvider() throws Exception {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{ \"field\": null }");

 FieldProperty property = new FieldProperty(new BeanPropertyDefinition(
 new AnnotatedField(TestBean.class.getDeclaredField("field"),
 new Annotation[0]),
 new BeanPropertyDefinition(null, null, TestBean.class, null, null)),
 mapper.getTypeFactory().constructType(TestBean.class), null, null,
 new AnnotatedField(TestBean.class.getDeclaredField("field"),
 new Annotation[0]));

 when(property._field.getType()).thenReturn(Integer.TYPE);
 when(property._field.canAccess(TestBean.class)).thenReturn(true);
 when(property._nullProvider.isEnabled(null)).thenReturn(true);

 property.deserializeAndSet(parser, mock(DeserializationContext.class), bean);
 assertNull(bean.field);
 }

 @org.junit.Test
 public void testDeserializationWithTypeDeserializer() throws Exception {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{ \"field\": [1, 2, 3] }");

 FieldProperty property = new FieldProperty(new BeanPropertyDefinition(
 new AnnotatedField(TestBean.class.getDeclaredField("field"),
 new Annotation[0]),
 new BeanPropertyDefinition(null, null, TestBean.class, null, null)),
 mapper.getTypeFactory().constructType(TestBean.class),
 new SimpleTypeDeserializer(), null, null,
 new AnnotatedField(TestBean.class.getDeclaredField("field"),
 new Annotation[0]));

 when(property._field.getType()).thenReturn(List.class);
 when(property._field.getType().getComponentType()).thenReturn(Integer.TYPE);
 when(property._field.canAccess(TestBean.class)).thenReturn(true);

 property.deserializeAndSet(parser, mock(DeserializationContext.class), bean);
 assertEquals(Arrays.asList(1, 2, 3), bean.field);
 }

 @org.junit.Test
 public void testSerialization() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TestBean bean = new TestBean();
 bean.field = 42;
 ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

 FieldProperty property = new FieldProperty(new BeanPropertyDefinition(
 new AnnotatedField(TestBean.class.getDeclaredField("field"),
 new Annotation[0]),
 new BeanPropertyDefinition(null, null, TestBean.class, null, null)),
 mapper.getTypeFactory().constructType(TestBean.class), null, null,
 new AnnotatedField(TestBean.class.getDeclaredField("field"),
 new Annotation[0]));

 when(property._field.getType()).thenReturn(Integer.TYPE);
 when(property._field.canAccess(TestBean.class)).thenReturn(true);

 String json = writer.writeValueAsString(property.get(new TestBean(), 42));
 assertEquals("{\"field\":42}", json);
 }

 @org.junit.Test
 public void testSetter() throws Exception {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{ \"field\": 42 }");

 FieldProperty property = new FieldProperty(new BeanPropertyDefinition(
 new AnnotatedField(TestBean.class.getDeclaredField("field"),
 new Annotation[0]),
 new BeanPropertyDefinition(null, null, TestBean.class, null, null)),
 mapper.getTypeFactory().constructType(TestBean.class), null, null,
 new AnnotatedField(TestBean.class.getDeclaredField("field"),
 new Annotation[0]));

 when(property._field.getType()).thenReturn(Integer.TYPE);
 when(property._field.canAccess(TestBean.class)).thenReturn(true);
 when(property._nullProvider.isEnabled(null)).thenReturn(false);

 property.set(bean, 42);
 assertEquals(42, bean.field);
 }

 @org.junit.Test
 public void testSetterWithNullProvider() throws Exception {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{ \"field\": null }");

 FieldProperty property = new FieldProperty(new BeanPropertyDefinition(
 new AnnotatedField(TestBean.class.getDeclaredField("field"),
 new Annotation[0]),
 new BeanPropertyDefinition(null, null, TestBean.class, null, null)),
 mapper.getTypeFactory().constructType(TestBean.class), null, null,
 new AnnotatedField(TestBean.class.getDeclaredField("field"),
 new Annotation[0]));

 when(property._field.getType()).thenReturn(Integer.TYPE);
 when(property._field.canAccess(TestBean.class)).thenReturn(true);
 when(property._nullProvider.isEnabled(null)).thenReturn(true);

 property.set(bean, null);
 assertNull(bean.field);
 }

 @org.junit.Test
 public void testSetterWithTypeDeserializer() throws Exception {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{ \"field\": [1, 2, 3] }");

 FieldProperty property = new FieldProperty(new BeanPropertyDefinition(
 new AnnotatedField(TestBean.class.getDeclaredField("field"),
 new Annotation[0]),
 new BeanPropertyDefinition(null, null, TestBean.class, null, null)),
 mapper.getTypeFactory().constructType(TestBean.class),
 new SimpleTypeDeserializer(), null, null,
 new AnnotatedField(TestBean.class.getDeclaredField("field"),
 new Annotation[0]));

 when(property._field.getType()).thenReturn(List.class);
 when(property._field.getType().getComponentType()).thenReturn(Integer.TYPE);
 when(property._field.canAccess(TestBean.class)).thenReturn(true);

 property.set(bean, Arrays.asList(1, 2, 3));
 assertEquals(Arrays.asList(1, 2, 3), bean.field);
 }

 private static class SimpleTypeDeserializer extends StdTypeDeserializer<List<Object>> {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 List<Object> result = new ArrayList<>();
 if (p.hasToken(JsonToken.START_ARRAY)) {
 while (!p.isClosed()) {
 Object value = p.readValueAs(Object.class);
 result.add(value);
 if (p.hasToken(JsonToken.END_ARRAY)) {
 break;
 }
 }
 }
 return result;
 }
 }
}