package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

import org.junit.*;

public class StdKeySerializersTest {

 private StdKeySerializers objUnderTest;

 @BeforeClass
 public static void setupClass() {
 objUnderTest = new StdKeySerializers();
 }

 @Test
 public void testGetStdKeySerializer() throws Exception {
 Object result = objUnderTest.getStdKeySerializer(null, null, true);
 assertNull(result);

 result = objUnderTest.getStdKeySerializer(null, String.class, true);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);

 result = objUnderTest.getStdKeySerializer(null, int[].class, true);
 assertNull(result);

 result = objUnderTest.getStdKeySerializer(null, Date.class, true);
 assertEquals(StdKeySerializers.DEFAULT, result);

 result = objUnderTest.getStdKeySerializer(null, Calendar.class, true);
 assertEquals(StdKeySerializers.DEFAULT, result);

 result = objUnderTest.getStdKeySerializer(null, java.util.UUID.class, true);
 assertEquals(StdKeySerializers.DEFAULT, result);
 }

 @Test
 public void testGetFallbackKeySerializer() throws Exception {
 Object result = objUnderTest.getFallbackKeySerializer(null, null);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);

 result = objUnderTest.getFallbackKeySerializer(null, String.class);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);

 result = objUnderTest.getFallbackKeySerializer(null, int[].class);
 assertNull(result);

 result = objUnderTest.getFallbackKeySerializer(null, Date.class);
 assertEquals(StdKeySerializers.DEFAULT, result);

 result = objUnderTest.getFallbackKeySerializer(null, Calendar.class);
 assertEquals(StdKeySerializers.DEFAULT, result);

 result = objUnderTest.getFallbackKeySerializer(null, java.util.UUID.class);
 assertEquals(StdKeySerializers.DEFAULT, result);

 result = objUnderTest.getFallbackKeySerializer(null, Enum.class);
 assertEquals(StdKeySerializers.DEFAULT, result);
 }

 @Test
 public void testGetDefault() throws Exception {
 Object result = objUnderTest.getDefault();
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
 }

 @Test
 public void testDefaultConstructor() {
 StdKeySerializers testObj = new StdKeySerializers();
 assertNotNull(testObj);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStdKeySerializerWithNullConfig() throws Exception {
 objUnderTest.getStdKeySerializer(null, String.class, true);
 fail("Expected IllegalArgumentException but got none.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStdKeySerializerWithNullType() throws Exception {
 objUnderTest.getStdKeySerializer(new Object(), null, true);
 fail("Expected IllegalArgumentException but got none.");
 }

 @Test
 public void testGetStdKeySerializerWithUnknownType() throws Exception {
 Object result = objUnderTest.getStdKeySerializer(null, MyCustomClass.class, true);
 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStdKeySerializerWithFalseUseDefault() throws Exception {
 Object result = objUnderTest.getStdKeySerializer(null, String.class, false);
 assertNull(result);
 }

 @Test
 public void testDynamicKeySerializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 PropertySerializerMap serializers = mapper.getSerializerProvider().getDefaultSerializerProvider().getSerializers();
 StdKeySerializers objUnderTest = new StdKeySerializers();
 Object result;

 result = objUnderTest._findAndAddDynamic(serializers, MyDynamicClass.class, mapper.getSerializerProvider());
 assertNotNull(result);
 assertInstanceOf(StdKeySerializers.Dynamic.class, result);

 result = objUnderTest._findAndAddDynamic(serializers, MyDynamicClass2.class, mapper.getSerializerProvider());
 assertNotNull(result);
 assertInstanceOf(StdKeySerializers.Dynamic.class, result);

 result = objUnderTest._findAndAddDynamic(serializers, MyDynamicClass3.class, mapper.getSerializerProvider());
 assertNotNull(result);
 assertInstanceOf(StdKeySerializers.Dynamic.class, result);
}

 @Test
 public void testStringKeySerializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), 128);
 StdKeySerializers objUnderTest = new StdKeySerializers();
 Object result = objUnderTest.DEFAULT_STRING_SERIALIZER;

 result.serialize(new String("test"), generator, mapper.getSerializerProvider());

 String expected = "\"test\"";
 ByteArrayOutputStream baos = (ByteArrayOutputStream) generator.getOutput();
 assertEquals(expected, baos.toString());
}

 @Test
 public void testDefaultKeySerializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), 128);
 StdKeySerializers objUnderTest = new StdKeySerializers();
 Object result = objUnderTest.DEFAULT_KEY_SERIALIZER;

 result.serialize(new Long(1), generator, mapper.getSerializerProvider());

 ByteArrayOutputStream baos = (ByteArrayOutputStream) generator.getOutput();
 assertEquals("1", baos.toString());
}

 @Test
 public void testEnumKeySerializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writer();
 StdKeySerializers objUnderTest = new StdKeySerializers();
 Object result;

 result = objUnderTest.getFallbackKeySerializer(mapper.getSerializerProvider(), EnumTest.class);
 assertInstanceOf(StdKeySerializers.Default.class, result);

 result = objUnderTest.getDefault();
 assertInstanceOf(StdKeySerializers.Default.class, result);

 Map<String, Object> map = writer.writeValueAsMap(new EnumTest[]{EnumTest.TEST1, EnumTest.TEST2});
 assertEquals("TEST1", map.keySet().iterator().next());
 assertEquals("TEST2", map.keySet().iterator().next());
}

 @Test
 public void testDateKeySerializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writer();
 StdKeySerializers objUnderTest = new StdKeySerializers();
 Object result;

 result = objUnderTest.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, true);
 assertNotNull(result);

 result = objUnderTest.getDefault();
 assertInstanceOf(StdKeySerializers.Default.class, result);

 Map<Date, Object> map = writer.writeValueAsMap(new Date[]{new Date(1234567891234L), new Date(2345678912345L)});
 assertEquals(new Date(1234567891234L), map.keySet().iterator().next());
 assertEquals(new Date(2345678912345L), map.keySet().iterator().next());
}

 @Test
 public void testCalendarKeySerializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writer();
 StdKeySerializers objUnderTest = new StdKeySerializers();
 Object result;

 result = objUnderTest.getStdKeySerializer(mapper.getSerializationConfig(), Calendar.class, true);
 assertNotNull(result);

 result = objUnderTest.getDefault();
 assertInstanceOf(StdKeySerializers.Default.class, result);

 Calendar calendar1 = Calendar.getInstance();
 calendar1.setTimeInMillis(1234567891234L);
 Calendar calendar2 = Calendar.getInstance();
 calendar2.setTimeInMillis(2345678912345L);

 Map<Calendar, Object> map = writer.writeValueAsMap(new Calendar[]{calendar1, calendar2});
 assertEquals(calendar1, map.keySet().iterator().next());
 assertEquals(calendar2, map.keySet().iterator().next());
}

 @Test
 public void testToStringKeySerializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writer();
 StdKeySerializers objUnderTest = new StdKeySerializers();
 Object result;

 result = objUnderTest.getStdKeySerializer(mapper.getSerializationConfig(), java.util.UUID.class, true);
 assertNotNull(result);

 result = objUnderTest.getDefault();
 assertInstanceOf(StdKeySerializers.Default.class, result);

 UUID uuid1 = UUID.randomUUID();
 UUID uuid2 = UUID.randomUUID();

 Map<UUID, Object> map = writer.writeValueAsMap(new UUID[]{uuid1, uuid2});
 assertEquals(uuid1, map.keySet().iterator().next());
 assertEquals(uuid2, map.keySet().iterator().next());
}
}