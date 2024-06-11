package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringKD;
import com.fasterxml.jackson.databind.deser.std.DelegatingKD;
import com.fasterxml.jackson.databind.deser.std.EnumKD;
import com.fasterxml.jackson.databind.deser.std.StringCtorKeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringFactoryKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @After
 public void tearDown() throws Exception {
 mapper = null;
 }

 @Test
 public void testBooleanDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 String key = "true";
 Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(Boolean.TRUE, result);

 key = "false";
 result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testByteDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 String key = "10";
 Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(Byte.valueOf((byte) 10), result);
 }

 @Test
 public void testShortDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
 String key = "100";
 Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(Short.valueOf((short) 100), result);
 }

 @Test
 public void testCharDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
 String key = "A";
 Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(Character.valueOf('A'), result);
 }

 @Test
 public void testIntDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 String key = "1000";
 Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(Integer.valueOf(1000), result);
 }

 @Test
 public void testLongDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
 String key = "1000000";
 Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(Long.valueOf(1000000), result);
 }

 @Test
 public void testFloatDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
 String key = "123.5";
 Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(Float.valueOf(123.5f), result);
 }

 @Test
 public void testDoubleDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
 String key = "123.5";
 Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(Double.valueOf(123.5), result);
 }

 @Test
 public void testLocaleDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 String key = "en_US";
 Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(Locale.forLanguageTag("en_US"), result);
 }

 @Test
 public void testCurrencyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Currency.class);
 String key = "USD";
 Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(Currency.getInstance("USD"), result);
 }

 @Test
 public void testURIDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
 String key = "http://example.com";
 Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(new URI(key), result);
 }

 @Test
 public void testURLDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
 String key = "http://example.com";
 Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(new URL(key), result);
 }

 @Test
 public void testClassDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
 String key = "[Ljava.lang.String;";
 Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(new String[0].getClass(), result.getClass());
 }

 @Test
 public void testEnumDeserializer() throws Exception {
 Class<?> enumClass = MyEnum.class;
 AnnotatedClass ac = mapper.getAnnotationIntrospector().findAnnotation(enumClass);
 EnumKD enumKeyDeserializer = (EnumKD) StdKeyDeserializer.forType(enumClass);
 String key = MyEnum.VALUE_1.name();
 Object result = enumKeyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(MyEnum.VALUE_1, result);

 String anotherKey = MyOtherEnum.VALUE_2.name();
 AnnotatedMethod am = ac.getDeclaredMembers().findMethod(MyOtherEnum.class.getMethod("getValue"));
 StringFactoryKeyDeserializer enumFactoryKeyDeserializer = new StringFactoryKeyDeserializer(am);
 result = enumFactoryKeyDeserializer.deserializeKey(anotherKey, mapper.getDeserializationContext());
 assertEquals(MyOtherEnum.VALUE_2, result);
 }

 @Test
 public void testDelegatingDeserializer() throws Exception {
 Class<?> targetClass = TestBean.class;
 AnnotatedField af = mapper.getSerializationConfig().getIntrospector().findSerializedMember(TestBean.class, "id");
 String fieldName = af.getName();
 TypeDeserializer idDeserializer = mapper.getTypeFactory().constructDeserializer(Long.class);
 DelegatingKD delegatingKeyDeserializer = new DelegatingKD(Long.class, idDeserializer);
 String key = "1";
 Object result = delegatingKeyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(Long.valueOf(1), result);
 }

 @Test
 public void testNullValueProvider() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
 String key = null;
 Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
 assertNull(result);

 StdKeyDeserializer nullValueProviderKeyDeserializer = new StdKeyDeserializer(-1, String.class, mapper.getDeserializationConfig().getNullValueProvider());
 String nullKey = null;
 result = nullValueProviderKeyDeserializer.deserializeKey(nullKey, mapper.getDeserializationContext());
 assertNull(result);
 }
}