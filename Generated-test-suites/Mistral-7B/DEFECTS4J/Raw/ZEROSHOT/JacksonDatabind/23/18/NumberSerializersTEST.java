package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializersTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testAddAll() throws Exception {
 Map<String, JsonSerializer<?>> allDeserializers = Mockito.mock(Map.class);
 NumberSerializers.addAll(allDeserializers);

 JsonSerializer<?> intSerializer = Mockito.argThat(s -> s.getClass() == IntegerSerializer.class);
 JsonSerializer<?> longSerializer = Mockito.argThat(s -> s.getClass() == LongSerializer.class);
 JsonSerializer<?> shortSerializer = Mockito.argThat(s -> s.getClass() == ShortSerializer.class);
 JsonSerializer<?> floatSerializer = Mockito.argThat(s -> s.getClass() == FloatSerializer.class);
 JsonSerializer<?> doubleSerializer = Mockito.argThat(s -> s.getClass() == DoubleSerializer.class);
 JsonSerializer<?> intLikeSerializer = Mockito.argThat(s -> s.getClass() == IntLikeSerializer.class);

 Mockito.verify(allDeserializers, Mockito.times(1)).put(intSerializer);
 Mockito.verify(allDeserializers, Mockito.times(1)).put(longSerializer);
 Mockito.verify(allDeserializers, Mockito.times(1)).put(shortSerializer);
 Mockito.verify(allDeserializers, Mockito.times(1)).put(floatSerializer);
 Mockito.verify(allDeserializers, Mockito.times(1)).put(doubleSerializer);
 Mockito.verify(allDeserializers, Mockito.times(1)).put(intLikeSerializer);
 }

 @Test
 public void testIntegerSerializer() throws Exception {
 Integer value = 5;
 JsonNode result = mapper.readTree(mapper.writeValueAsString(value));
 assertEquals(ValueFactory.numberValue(5), result);
 }

 @Test
 public void testLongSerializer() throws Exception {
 Long value = 10L;
 JsonNode result = mapper.readTree(mapper.writeValueAsString(value));
 assertEquals(ValueFactory.numberValue(10), result);
 }

 @Test
 public void testShortSerializer() throws Exception {
 Short value = 3;
 JsonNode result = mapper.readTree(mapper.writeValueAsString(value));
 assertEquals(ValueFactory.numberValue(3), result);
 }

 @Test
 public void testFloatSerializer() throws Exception {
 Float value = 3.14f;
 JsonNode result = mapper.readTree(mapper.writeValueAsString(value));
 assertEquals(ValueFactory.numberValue(3.14f), result);
 }

 @Test
 public void testDoubleSerializer() throws Exception {
 Double value = 3.14;
 JsonNode result = mapper.readTree(mapper.writeValueAsString(value));
 assertEquals(ValueFactory.numberValue(3.14), result);
 }

 @Test
 public void testIntLikeSerializer() throws Exception {
 Byte value = 5;
 JsonNode result = mapper.readTree(mapper.writeValueAsString(value));
 assertEquals(ValueFactory.numberValue(5), result);
 }
}