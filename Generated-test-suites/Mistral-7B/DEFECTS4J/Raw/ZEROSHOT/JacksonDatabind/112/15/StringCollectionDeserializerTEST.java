package com.example.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

public class StringCollectionDeserializerTest {

 private StringCollectionDeserializer deserializer;
 private JsonDeserializer<String> stringDeserializer;
 private ValueInstantiator valueInstantiator;
 private ObjectMapper objectMapper;
 private JsonParser jsonParser;
 private JsonFactory jsonFactory;
 private ContextualDeserializer contextualDeserializer;
 private NullValueProvider nullValueProvider;
 private TypeDeserializer typeDeserializer;

 @Before
 public void setUp() throws Exception {
 stringDeserializer = mock(JsonDeserializer.class);
 valueInstantiator = mock(ValueInstantiator.class);
 contextualDeserializer = mock(ContextualDeserializer.class);
 nullValueProvider = mock(NullValueProvider.class);
 typeDeserializer = mock(TypeDeserializer.class);
 objectMapper = new ObjectMapper();
 jsonFactory = objectMapper.getFactory();
 jsonParser = jsonFactory.createParser("[]");
 deserializer = new StringCollectionDeserializer(
 new SimpleType(Collection.class), stringDeserializer, valueInstantiator, contextualDeserializer, null, null);
 }

 @Test
 public void testEmptyCollectionDeserializer() throws Exception {
 Object result = deserializer.deserialize(jsonParser, null);
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testDeserializeCollectionWithSingleValue() throws Exception {
 when(stringDeserializer.deserialize(jsonParser, any(DeserializationContext.class)))
 .thenReturn("test");
 List<String> result = deserializer.deserialize(jsonParser, Collections.emptyList());
 assertEquals(Arrays.asList("test"), result);
 }

 @Test
 public void testDeserializeCollectionWithCustomDeserializer() throws Exception {
 when(stringDeserializer.deserialize(jsonParser, any(DeserializationContext.class)))
 .then(new Answers.CallableAnswer<String>() {
 @SuppressWarnings("unchecked")
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return (String) invocation.getArgumentAt(0, 1).readValueAs(String.class);
 }
 });
 List<String> result = deserializer.deserialize(jsonParser, Collections.emptyList());
 JsonParser delegateParser = jsonFactory.createParser("{\"test\":1}");
 JsonDeserializer<Object> delegateDeserializer = new StandardDeserializer(Object.class);
 when(contextualDeserializer.createContextual(any(DeserializationContext.class), any(BeanProperty.class)))
 .thenReturn(delegateDeserializer);
 result = deserializer.deserialize(jsonParser, Collections.emptyList());
 assertEquals(Arrays.asList("1"), result);
 }

 @Test
 public void testDeserializeCollectionWithNullValue() throws Exception {
 when(nullValueProvider.getNullValue(any(DeserializationContext.class)))
 .thenReturn(null);
 List<String> result = deserializer.deserialize(jsonParser, Collections.emptyList());
 assertEquals(Collections.emptyList(), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeCollectionWithInvalidJson() throws Exception {
 jsonParser = jsonFactory.createParser("{\"test\":1}");
 deserializer.deserialize(jsonParser, Collections.emptyList());
 }

 @Test
 public void testDeserializeCollectionWithCustomTypeDeserializer() throws Exception {
 when(typeDeserializer.deserializeTypedFromArray(jsonParser, any(DeserializationContext.class)))
 .thenReturn(Arrays.asList("test1", "test2"));
 List<List<String>> result = deserializer
 .deserializeWithType(jsonParser, null, typeDeserializer);
 assertEquals(Arrays.asList(Arrays.asList("test1"), Arrays.asList("test2")), result);
 }

 @Test
 public void testDeserializeCollectionWithConverter() throws Exception {
 when(stringDeserializer.deserialize(jsonParser, any(DeserializationContext.class)))
 .then(new StdConverter.StdConverterAnswer<String, Integer>() {
 @Override
 public Integer fromValue(String value) {
 return Integer.parseInt(value);
 }

 @Override
 public String toValue(Integer value) {
 return Integer.toString(value);
 }
 });
 List<Integer> result = deserializer.deserialize(jsonParser, new ArrayList<>());
 assertEquals(Arrays.asList(1, 2), result);
 }
}