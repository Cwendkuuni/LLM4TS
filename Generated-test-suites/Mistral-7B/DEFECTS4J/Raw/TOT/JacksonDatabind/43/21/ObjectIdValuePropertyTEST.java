package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Test for {@link ObjectIdValueProperty} class.
 */
public class ObjectIdValuePropertyTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 /**
 * Test for the constructor.
 */
 @Test
 public void testConstructor() throws IOException {
 String propertyName = "objectId";
 ObjectIdReader objectIdReader = new SimpleObjectIdReader(propertyName, ObjectIdType.STRING);
 PropertyMetadata metadata = new PropertyMetadata();
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

 assertEquals(propertyName, property.getName());
 assertEquals(ObjectIdType.STRING, property.getType());
 assertNull(property.getMember());
 assertNull(property.getValueDeserializer());
 assertNotNull(property._objectIdReader);
 }

 /**
 * Test for the method {@link ObjectIdValueProperty#deserializeAndSet(JsonParser, DeserializationContext, Object)}.
 */
 @Test
 public void testDeserializeAndSet() throws IOException {
 String propertyName = "objectId";
 ObjectIdReader objectIdReader = new SimpleObjectIdReader(propertyName, ObjectIdType.STRING);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put(propertyName, "123");

 ObjectIdType idType = ObjectIdType.STRING;
 Object id = new FromStringDeserializer(idType).deserialize("123", objectMapper.getFactory());
 Object value = new Object() {{
 @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
 int id = 1;
 }};
 Object instance = new Object() {{
 @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
 int id = 2;
 Object idProperty = new SimpleIdProperty();
 }};

 objectMapper.registerModule(new SimpleModule() {{
 addDeserializer(ObjectIdType.class, new SimpleObjectIdReader.StdDeserializer());
 }});

 JsonParser parser = objectMapper.getFactory().createParser(rootNode);
 JsonDeserializationContext context = objectMapper.getDeserializationContext();

 property.deserializeAndSet(parser, context, instance);

 assertEquals(id, instance.getIdProperty().get());
 }

 /**
 * Test for the method {@link ObjectIdValueProperty#deserializeSetAndReturn(JsonParser, DeserializationContext, Object)}.
 */
 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 String propertyName = "objectId";
 ObjectIdReader objectIdReader = new SimpleObjectIdReader(propertyName, ObjectIdType.INT);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put(propertyName, "123");

 ObjectIdType idType = ObjectIdType.INT;
 Object id = new IntIdGenerator().nextId();
 Object value = new Object() {{
 @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
 int id = 1;
 }};
 Object instance = new Object() {{
 @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
 int id = 2;
 Object idProperty = new SimpleIdProperty();
 }};

 objectMapper.registerModule(new SimpleModule() {{
 addDeserializer(ObjectIdType.class, new SimpleObjectIdReader.StdDeserializer());
 addDeserializer(SimpleIdProperty.class, new SimpleIdProperty.StdDeserializer());
 }});

 JsonParser parser = objectMapper.getFactory().createParser(rootNode);
 JsonDeserializationContext context = objectMapper.getDeserializationContext();
 context.findValueIdResolver(ObjectIdType.class).setCurrentValueId("123");

 Object result = property.deserializeSetAndReturn(parser, context, instance);

 assertEquals(id, result);
 assertEquals(id, instance.getIdProperty().get());
 }

 /**
 * Test for the method {@link ObjectIdValueProperty#set(Object, Object)}.
 */
 @Test
 public void testSet() throws IOException {
 String propertyName = "objectId";
 ObjectIdReader objectIdReader = new SimpleObjectIdReader(propertyName, ObjectIdType.STRING);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode rootNode = objectMapper.createObjectNode();

 ObjectIdType idType = ObjectIdType.STRING;
 Object id = new FromStringDeserializer(idType).deserialize("123", objectMapper.getFactory());
 Object value = new Object() {{
 @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
 int id = 1;
 }};
 Object instance = new Object() {{
 @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
 int id = 2;
 Object idProperty = new SimpleIdProperty();
 }};

 objectMapper.registerModule(new SimpleModule() {{
 addDeserializer(ObjectIdType.class, new SimpleObjectIdReader.StdDeserializer());
 }});

 JsonParser parser = objectMapper.getFactory().createParser(rootNode);
 JsonDeserializationContext context = objectMapper.getDeserializationContext();
 context.findValueIdResolver(ObjectIdType.class).setCurrentValueId("123");

 property.set(instance, id);

 assertEquals(id, instance.getIdProperty().get());
 }

 /**
 * Test for the method {@link ObjectIdValueProperty#setAndReturn(Object, Object)}.
 */
 @Test
 public void testSetAndReturn() throws IOException {
 String propertyName = "objectId";
 ObjectIdReader objectIdReader = new SimpleObjectIdReader(propertyName, ObjectIdType.INT);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectNode rootNode = objectMapper.createObjectNode();

 ObjectIdType idType = ObjectIdType.INT;
 Object id = new IntIdGenerator().nextId();
 Object value = new Object() {{
 @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
 int id = 1;
 }};
 Object instance = new Object() {{
 @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
 int id = 2;
 Object idProperty = new SimpleIdProperty();
 }};

 objectMapper.registerModule(new SimpleModule() {{
 addDeserializer(ObjectIdType.class, new SimpleObjectIdReader.StdDeserializer());
 addDeserializer(SimpleIdProperty.class, new SimpleIdProperty.StdDeserializer());
 }});

 JsonParser parser = objectMapper.getFactory().createParser(rootNode);
 JsonDeserializationContext context = objectMapper.getDeserializationContext();
 context.findValueIdResolver(ObjectIdType.class).setCurrentValueId("123");

 Object result = property.setAndReturn(instance, value);

 assertEquals(id, result.getId());
 assertEquals(id, instance.getIdProperty().get());
 }

 /**
 * Test for the method {@link ObjectIdValueProperty#withName(PropertyName)}.
 */
 @Test
 public void testWithName() {
 String oldPropertyName = "oldPropertyName";
 String newPropertyName = "newPropertyName";
 ObjectIdReader objectIdReader = new SimpleObjectIdReader(oldPropertyName, ObjectIdType.STRING);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdValueProperty newProperty = property.withName(new PropertyName(newPropertyName));

 assertEquals(newPropertyName, newProperty.getName());
 assertEquals(oldPropertyName, property.getName());
 }

 /**
 * Test for the method {@link ObjectIdValueProperty#withValueDeserializer(JsonDeserializer)}.
 */
 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<Object> oldValueDeserializer = new CustomObjectDeserializer();
 JsonDeserializer<Object> newValueDeserializer = new SimpleObjectIdReader.StdDeserializer();
 ObjectIdReader objectIdReader = new SimpleObjectIdReader("objectId", ObjectIdType.STRING);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdValueProperty newProperty = property.withValueDeserializer(newValueDeserializer);

 assertEquals(newValueDeserializer, newProperty.getValueDeserializer());
 assertEquals(oldValueDeserializer, property.getValueDeserializer());
 }
}

class SimpleObjectIdReader extends BaseJsonTypeDeserializer<ObjectIdType> {

 @JsonDeserialize(using = ObjectIdFactory.class)
 static class StdDeserializer extends JsonDeserializer<ObjectIdType> {
 @Override
 public ObjectIdType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return ObjectIdType.fromValue(p.getValueAsString());
 }
 }

 static class IntIdGenerator {
 private static int counter = 0;

 public int nextId() {
 return ++counter;
 }
 }

 static class SimpleIdProperty {
 @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
 private int id;

 public int get() {
 return id;
 }
 }

 static class SimpleModule extends SimpleModule {
 private static final long serialVersionUID = 1L;

 public SimpleModule() {
 super(ObjectIdValuePropertyTest.class);
 }
 }

 @JsonValue
 static class ObjectIdFactory implements JsonDeserializer<ObjectIdType> {
 @Override
 public ObjectIdType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 String idString = p.getValueAsString();
 return ObjectIdType.fromValue(idString);
 }
 }
}

class CustomObjectDeserializer extends JsonDeserializer<Object> {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 throw new IOException("Custom deserializer should not be used.");
 }
}