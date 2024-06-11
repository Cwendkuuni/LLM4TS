package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StandardSerializers;
import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

 Converter<Object, String> stringConverter;
 Converter<Object, Integer> intConverter;
 Converter<Object, Boolean> booleanConverter;

 ObjectMapper mapper;
 JsonSerializer<String> stringSerializer;
 JsonSerializer<Integer> intSerializer;
 JsonSerializer<Boolean> booleanSerializer;

 @Before
 public void setup() throws Exception {
 stringConverter = mock(Converter.class);
 intConverter = mock(Converter.class);
 booleanConverter = mock(Converter.class);
 mapper = new ObjectMapper();
 mapper.configure(FAIL_ON_EMPTY_BEANS, false);
 stringSerializer = mapper.findValueSerializer(String.class);
 intSerializer = mapper.findValueSerializer(Integer.class);
 booleanSerializer = mapper.findValueSerializer(Boolean.class);
 }

 @Test
 public void testBasic() throws Exception {
 StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<Object>(stringConverter);
 Object value = "foo";
 String expected = "foo";

 JsonGenerator gen = mock(JsonGenerator.class);
 SerializedString ser = mapper.writeValueAsString(value);
 when(stringSerializer.serialize(eq(value), eq(gen), any(SerializerProvider.class)))
     .thenReturn(ser);

 serializer.serialize(value, gen, mapper.getSerializerProvider());
 verify(stringSerializer).serialize(eq(value), eq(gen), any(SerializerProvider.class));
 assertEquals(ser.getValue(), expected);
 }

 @Test
 public void testDynamic() throws Exception {
 StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<Object>(stringConverter);
 Object value = new Object() {
 @Override
 public String toString() {
 return "foo";
 }
 };
 String expected = "foo";

 JsonGenerator gen = mock(JsonGenerator.class);
 when(stringSerializer.serialize(eq(value), eq(gen), any(SerializerProvider.class)))
     .thenReturn("foo");

 serializer.serialize(value, gen, mapper.getSerializerProvider());
 verify(stringSerializer).serialize(eq(value), eq(gen), any(SerializerProvider.class));
 assertEquals("foo", expected);
 }

 @Test
 public void testDynamicWithTypeSerializer() throws Exception {
 StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<Object>(stringConverter);
 Object value = new Object() {
 @Override
 public String toString() {
 return "foo";
 }
 };
 Type serializedType = String.class;
 TypeSerializer typeSerializer = StandardSerializers.stringSerializerWithType(serializedType);
 String expected = "foo";

 JsonGenerator gen = mock(JsonGenerator.class);
 when(stringSerializer.serialize(eq(value), eq(gen), any(SerializerProvider.class)))
     .thenReturn("foo");
 when(typeSerializer.serializeValue(eq(value), eq(gen), any(SerializerProvider.class)))
     .thenReturn("foo");

 serializer.serializeWithType(value, gen, mapper.getSerializerProvider(), typeSerializer);
 verify(stringSerializer).serialize(eq(value), eq(gen), any(SerializerProvider.class));
 verify(typeSerializer).serializeValue(eq(value), eq(gen), any(SerializerProvider.class));
 assertEquals("foo", expected);
 }

 @Test
 public void testContextualization() throws Exception {
 StdDelegatingSerializer<Object> serializer1 = new StdDelegatingSerializer<Object>(stringConverter) {
 @Override
 protected StdDelegatingSerializer<Object> withDelegate
 (Converter<Object,?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer) {
 return new StdDelegatingSerializer<Object>(intConverter);
 }
 };
 Object value1 = "foo";
 String expected1 = "42";

 JsonGenerator gen1 = mock(JsonGenerator.class);
 when(intSerializer.serialize(eq(42), eq(gen1), any(SerializerProvider.class)))
     .thenReturn("42");

 serializer1.serialize(value1, gen1, mapper.getSerializerProvider());
 verify(intSerializer).serialize(eq(42), eq(gen1), any(SerializerProvider.class));
 assertEquals("42", expected1);

 StdDelegatingSerializer<Object> serializer2 = new StdDelegatingSerializer<Object>(intConverter) {
 @Override
 protected StdDelegatingSerializer<Object> withDelegate
 (Converter<Object,?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer) {
 return new StdDelegatingSerializer<Object>(booleanConverter);
 }
 };
 Object value2 = true;
 Boolean expected2 = true;

 JsonGenerator gen2 = mock(JsonGenerator.class);
 when(booleanSerializer.serialize(eq(true), eq(gen2), any(SerializerProvider.class)))
     .thenReturn(true);

 serializer2.serialize(value2, gen2, mapper.getSerializerProvider());
 verify(booleanSerializer).serialize(eq(true), eq(gen2), any(SerializerProvider.class));
 assertEquals(true, expected2);
 }

 @Test
 public void testTypeHint() throws Exception {
 StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<Object>(stringConverter) {
 @Override
 protected StdDelegatingSerializer<Object> withDelegate
 (Converter<Object,?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer) {
 return new StdDelegatingSerializer<String>(intConverter);
 }
 };
 Object value = "foo";
 String expected = "foo";

 JsonGenerator gen = mock(JsonGenerator.class);
 when(stringSerializer.serialize(eq(value), eq(gen), any(SerializerProvider.class)))
     .thenReturn("foo");

 serializer.serialize(value, gen, mapper.getSerializerProvider());
 verify(stringSerializer).serialize(eq(value), eq(gen), any(SerializerProvider.class));
 assertEquals("foo", expected);
 }

 @Test
 public void testEmpty() throws Exception {
 StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<Object>(stringConverter);
 Object value = null;

 JsonGenerator gen = mock(JsonGenerator.class);
 when(stringSerializer.serialize(eq(null), eq(gen), any(SerializerProvider.class)))
     .thenCallRealMethod();

 serializer.serialize(value, gen, mapper.getSerializerProvider());
 verify(stringSerializer).serialize(eq(null), eq(gen), any(SerializerProvider.class));
 assertNull(mapper.readTree(gen.getOutputBuffer().toString()));
 }

 @Test
 public void testEmptyWithContextualization() throws Exception {
 StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<Object>(new SimpleBeanPropertyFilter()) {
 @Override
 protected StdDelegatingSerializer<Object> withDelegate
 (Converter<Object,?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer) {
 return new StdDelegatingSerializer<Object>(stringConverter);
 }
 };
 Object value = new Object();

 JsonGenerator gen = mock(JsonGenerator.class);
 when(stringSerializer.serialize(eq(value), eq(gen), any(SerializerProvider.class)))
     .thenCallRealMethod();

 serializer.serialize(value, gen, mapper.getSerializerProvider());
 JsonNode result = mapper.readTree(gen.getOutputBuffer().toString());
 assertNull(result.get("name"));
 assertEquals(0, result.size());
 }

 @Test
 public void testFailureOnEmptyBean() throws Exception {
 mapper.configure(FAIL_ON_EMPTY_BEANS, true);
 StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<Object>(stringConverter);
 Object value = new Object();

 JsonGenerator gen = mock(JsonGenerator.class);
 when(stringSerializer.serialize(eq(value), eq(gen), any(SerializerProvider.class)))
     .thenCallRealMethod();

 try {
 serializer.serialize(value, gen, mapper.getSerializerProvider());
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // expected
 }
 }
}