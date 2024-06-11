package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Random;

public class ObjectIdValuePropertyTest {

 private ObjectMapper mapper;
 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private StdSerializer<Long> longSerializer;

 @Before
 public void setup() throws Exception {
 ObjectIdReader.ObjectIdGenerator generator = new ObjectIdGenerator();
 ObjectIdReader.ObjectIdResolver resolver = new ObjectIdResolver();
 mapper = new ObjectMapper();
 objectIdReader = new ObjectIdReader(new PropertyName("id"), Long.class, null, null, generator, resolver);
 longSerializer = new StdSerializer<Long>(Long.class);
 mapper.registerSerializer(Long.class, longSerializer);
 mapper.registerDeserializer(ObjectIdNode.class, ObjectIdValueProperty.class);
 mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
 }

 @Test
 public void testDeserializeAndSetNullId() throws Exception {
 Object id = null;
 Object idInstance = mapper.readValue("{\"id\":null}", Object.class);
 ObjectIdValueProperty testProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
 testProperty.deserializeAndSet(mapper.getFactory().createParser("{\"id\":null}"), mapper.getDeserializationContext(), idInstance);
 assertNull(testProperty.get(idInstance));
 }

 @Test
 public void testDeserializeAndSetValidId() throws Exception {
 Object id = new ObjectIdNode(new Random().nextLong());
 Object idInstance = mapper.readValue("{\"id\":123}", Object.class);
 ObjectIdValueProperty testProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
 testProperty.deserializeAndSet(mapper.getFactory().createParser("{\"id\":123}"), mapper.getDeserializationContext(), idInstance);
 assertEquals(id, testProperty.get(idInstance));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetNullValue() throws Exception {
 Object idInstance = mapper.readValue("{}", Object.class);
 ObjectIdValueProperty testProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
 testProperty.set(idInstance, null);
 fail("Should not be able to set null value");
 }

 @Test
 public void testSetValidValue() throws Exception {
 Object id = new ObjectIdNode(new Random().nextLong());
 Object idInstance = mapper.readValue("{}", Object.class);
 ObjectIdValueProperty testProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
 testProperty.set(idInstance, id);
 assertEquals(id, testProperty.get(idInstance));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws Exception {
 Object idInstance = mapper.readValue("{}", Object.class);
 ObjectIdValueProperty testProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
 testProperty.setAndReturn(idInstance, new Object());
 fail("Should not be able to set without idProperty");
 }

 @Test
 public void testSetWithIdProperty() throws Exception {
 Object id = new ObjectIdNode(new Random().nextLong());
 Object idInstance = mapper.readValue("{}", Object.class);
 ObjectIdValueProperty testProperty = new ObjectIdValueProperty(objectIdReader, new PropertyName("idProperty"), PropertyMetadata.empty(), null, null);
 ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
 idProperty.set(idInstance, id);
 Object idValue = testProperty.setAndReturn(idInstance, id);
 assertEquals(id, idValue);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidJson() throws Exception {
 Object idInstance = mapper.readValue("{\"invalidJson\":123}", Object.class);
 ObjectIdValueProperty testProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
 testProperty.deserializeAndSet(mapper.getFactory().createParser("{\"invalidJson\":123}"), mapper.getDeserializationContext(), idInstance);
 fail("Should throw IOException on invalid JSON");
 }

 @Test
 public void testSerializer() throws Exception {
 ObjectIdValueProperty testProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
 Long id = new Long(123);
 Object idInstance = mapper.readValue("{}", Object.class);
 testProperty.set(idInstance, id);
 String json = mapper.writerWithView(new SimpleObjectMapper.SerializerView()).writeValueAsString(idInstance);
 ObjectIdNode expectedId = new ObjectIdNode(id.longValue());
 ObjectMapper deserializer = new ObjectMapper();
 Object deserializedInstance = deserializer.readValue(new ByteArrayInputStream(json.getBytes()), Object.class);
 ObjectIdValueProperty deserializedTestProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
 Object deserializedId = deserializedTestProperty.get(deserializedInstance);
 assertEquals(expectedId, deserializedId);
 }
}