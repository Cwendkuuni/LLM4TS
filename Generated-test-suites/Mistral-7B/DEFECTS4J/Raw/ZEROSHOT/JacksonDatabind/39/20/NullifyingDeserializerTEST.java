package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerTestBase;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class NullifyingDeserializerTest extends TypeDeserializerTestBase {

 private static final NullifyingDeserializer serializer = NullifyingDeserializer.instance;

 @Test
 public void testDeserializeNullifying() throws JsonProcessingException {
 JsonParser p = createParser("[]");
 Object result = serializer.deserialize(p, context);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingWithType() throws JsonProcessingException {
 JsonParser p = createParser("[]");
 Object result = serializer.deserializeWithType(p, context, objectTypeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingObject() throws JsonProcessingException {
 JsonParser p = createParser("{\"name\":\"John\"}");
 Object result = serializer.deserialize(p, context);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingArray() throws JsonProcessingException {
 JsonParser p = createParser("[1, 2, 3]");
 Object result = serializer.deserialize(p, context);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingObjectWithType() throws JsonProcessingException {
 JsonParser p = createParser("{\"name\":\"John\"}");
 Object result = serializer.deserializeWithType(p, context, StringDeserializer.instance);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingArrayWithType() throws JsonProcessingException {
 JsonParser p = createParser("[1, 2, 3]");
 Object result = serializer.deserializeWithType(p, context, IntDeserializer.instance);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingFieldName() throws JsonProcessingException {
 JsonParser p = createParser("{\"name\": null}");
 Object result = serializer.deserializeWithType(p, context, StringDeserializer.instance);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingStartArray() throws JsonProcessingException {
 JsonParser p = createParser("[null]");
 Object result = serializer.deserializeWithType(p, context, ArrayType.constructUnboundedArrayType(IntType.Instance));
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingStartObject() throws JsonProcessingException {
 JsonParser p = createParser("{\"nullField\": null}");
 Object result = serializer.deserializeWithType(p, context, MapType.construct(StringType.INSTANCE, IntType.Instance));
 assertNull(result);
 }
}