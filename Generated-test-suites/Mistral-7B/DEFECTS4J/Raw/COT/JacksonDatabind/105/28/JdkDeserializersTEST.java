package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.TextNode;

@RunWith(MockitoJUnitRunner.class)
public class JdkDeserializersTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFindNull() {
 JsonDeserializer<?> result = JdkDeserializers.find(null, null);
 assertNull(result);
 }

 @Test
 public void testFindUnknownType() throws Exception {
 JsonDeserializer<?> result = JdkDeserializers.find(Object.class, "UnknownType");
 assertNull(result);
 }

 @Test
 public void testFindUUID() throws Exception {
 JsonDeserializer<UUID> result = (JsonDeserializer<UUID>) JdkDeserializers.find(UUID.class, UUID.class.getName());
 assertNotNull(result);
 UUID actual = objectMapper.readValue(new TextNode("uuid-1"), UUID.class);
 UUID expected = new UUID(1, 2);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindAtomicBoolean() throws Exception {
 JsonDeserializer<AtomicBoolean> result = (JsonDeserializer<AtomicBoolean>) JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 assertNotNull(result);
 AtomicBoolean actual = objectMapper.readValue(new TextNode("true"), AtomicBoolean.class);
 AtomicBoolean expected = new AtomicBoolean(true);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindStackTraceElement() throws Exception {
 JsonDeserializer<StackTraceElement> result = (JsonDeserializer<StackTraceElement>) JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 assertNotNull(result);
 StackTraceElement actual = objectMapper.readValue(new TextNode("methodName:file:lineNumber"), StackTraceElement.class);
 StackTraceElement expected = new StackTraceElement("methodName", "file", "file:lineNumber");
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByteBuffer() throws Exception {
 JsonDeserializer<ByteBuffer> result = (JsonDeserializer<ByteBuffer>) JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 assertNotNull(result);
 ByteBuffer actual = objectMapper.readValue(new TextNode("[B:1,2,3,4,5]"), ByteBuffer.class);
 ByteBuffer expected = ByteBuffer.allocate(5);
 expected.put((byte) 1).put((byte) 2).put((byte) 3).put((byte) 4).put((byte) 5);
 expected.flip();
 byte[] bytes = new byte[5];
 expected.get(bytes, 0, 5);
 assertArrayEquals(bytes, actual.array());
 }

 @Test(expected = JsonMappingException.class)
 public void testFindMissingClass() throws Exception {
 JsonDeserializer<?> result = JdkDeserializers.find(Map.class, "MissingClass");
 fail("Should not reach this point");
 }

 @Test
 public void testFindFromStringDeserializer() throws Exception {
 JsonDeserializer<?> result = JdkDeserializers.find(String.class, String.class.getName());
 assertEquals(FromStringDeserializer.class, result.getClass());
 }
}