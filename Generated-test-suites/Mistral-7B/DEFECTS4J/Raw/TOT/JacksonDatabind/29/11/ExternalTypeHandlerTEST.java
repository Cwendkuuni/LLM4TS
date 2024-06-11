package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.*;

/**
 * Test class for ExternalTypeHandler.
 */
public class ExternalTypeHandlerTest {

 private static class TestBean {
 private String prop1;
 private Integer prop2;
 private List<String> prop3;
 }

 @BeforeClass
 public static void setupClass() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String[].class, new StringArrayDeserializer());
 mapper.registerModule(module);
 }

 @AfterClass
 public static void teardownClass() throws Exception {
 // no teardown needed
 }

 @Before
 public void setup() {
 // no setup needed
 }

 @After
 public void teardown() {
 // no teardown needed
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[] {
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop1"), StringDeserializer.instance),
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop2"), IntegerDeserializer.instance),
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop3"), new ArrayTypeDeserializer(), "typeIds")
 }, new HashMap<String, Integer>(), null, null);
 handler.start();

 JsonParser jp = mapper.getFactory().createParser("{\"prop1\":\"test\", \"prop2\":42, \"typeIds\":[\"StringList\"]}");
 handler.handleTypePropertyValue(jp, mapper.getDeserializationContext(), "prop1", bean);
 assertEquals("test", bean.prop1);
 handler.handleTypePropertyValue(jp, mapper.getDeserializationContext(), "prop2", bean);
 assertEquals(42, bean.prop2.intValue());

 JsonNode typeIdsNode = jp.getParserNextValue();
 List<String> list = mapper.readValue(typeIdsNode, List.class);
 assertEquals(1, list.size());
 assertTrue(list.get(0).equals("StringList"));

 handler.handleTypePropertyValue(jp, mapper.getDeserializationContext(), "typeIds", bean);
 JsonParser jp2 = mapper.getFactory().createParser(list.get(0));
 handler.handlePropertyValue(jp2, mapper.getDeserializationContext(), "prop3", bean);
 assertTrue(bean.prop3 instanceof ArrayList);
 assertEquals(1, ((List) bean.prop3).size());
 assertEquals("test", ((List) bean.prop3).get(0));

 jp2.close();
 jp.close();
 handler.complete(jp, mapper.getDeserializationContext(), bean);
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[] {
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop1"), StringDeserializer.instance),
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop2"), IntegerDeserializer.instance),
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop3"), new ArrayTypeDeserializer(), "typeIds")
 }, new HashMap<String, Integer>(), null, null);
 handler.start();

 JsonParser jp = mapper.getFactory().createParser("{\"prop1\":\"test\", \"prop2\":42, \"prop3\":[1,2,3]}");
 handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "prop1", bean);
 assertEquals("test", bean.prop1);
 handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "prop2", bean);
 assertEquals(42, bean.prop2.intValue());

 JsonNode typeIdsNode = jp.getParserNextValue();
 List<String> list = mapper.readValue(typeIdsNode, List.class);
 assertEquals(1, list.size());
 assertTrue(list.get(0).equals("ArrayType"));

 JsonParser jp2 = mapper.getFactory().createParser(list.get(0));
 handler.handlePropertyValue(jp2, mapper.getDeserializationContext(), "prop3", bean);
 assertTrue(bean.prop3 instanceof ArrayList);
 assertEquals(3, ((List) bean.prop3).size());
 assertEquals(1, ((ArrayList) bean.prop3).get(0).intValue());
 assertEquals(2, ((ArrayList) bean.prop3).get(1).intValue());
 assertEquals(3, ((ArrayList) bean.prop3).get(2).intValue());

 jp2.close();
 jp.close();
 handler.complete(jp, mapper.getDeserializationContext(), bean);
 }

 @Test
 public void testHandleMissingTypePropertyValue() throws Exception {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[] {
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop1"), StringDeserializer.instance),
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop2"), IntegerDeserializer.instance),
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop3"), new ArrayTypeDeserializer(), "typeIds")
 }, new HashMap<String, Integer>(), null, null);
 handler.start();

 JsonParser jp = mapper.getFactory().createParser("{\"prop1\":\"test\", \"prop2\":42}");
 handler.handleTypePropertyValue(jp, mapper.getDeserializationContext(), "prop1", bean);
 assertEquals("test", bean.prop1);
 handler.handleTypePropertyValue(jp, mapper.getDeserializationContext(), "prop2", bean);
 assertEquals(42, bean.prop2.intValue());

 // missing "typeIds" property
 jp.nextToken();
 jp.nextToken();
 jp.nextToken();
 jp.nextToken();
 jp.nextToken();
 handler.complete(jp, mapper.getDeserializationContext(), bean);
 assertTrue(bean.prop3 == null);
 }

 @Test
 public void testHandleMissingPropertyValue() throws Exception {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[] {
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop1"), StringDeserializer.instance),
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop2"), IntegerDeserializer.instance),
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop3"), new ArrayTypeDeserializer(), "typeIds")
 }, new HashMap<String, Integer>(), null, null);
 handler.start();

 JsonParser jp = mapper.getFactory().createParser("{\"prop2\":42}");
 handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "prop1", bean);
 handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "prop2", bean);

 // missing "prop1" property
 jp.nextToken();
 jp.nextToken();
 jp.nextToken();
 handler.complete(jp, mapper.getDeserializationContext(), bean);
 assertTrue(bean.prop1 == null);
 }

 @Test
 public void testHandleMissingTypeId() throws Exception {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[] {
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop1"), StringDeserializer.instance),
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop2"), IntegerDeserializer.instance),
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop3"), new ArrayTypeDeserializer(), "typeIds")
 }, new HashMap<String, Integer>(), null, null);
 handler.start();

 JsonParser jp = mapper.getFactory().createParser("{\"prop1\":\"test\", \"prop3\":[1,2,3]}");
 handler.handleTypePropertyValue(jp, mapper.getDeserializationContext(), "prop1", bean);
 assertEquals("test", bean.prop1);
 handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "prop3", bean);

 // missing "typeIds" property
 handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "prop3", bean);
 jp.nextToken();
 jp.nextToken();
 jp.nextToken();
 jp.nextToken();
 jp.nextToken();
 handler.complete(jp, mapper.getDeserializationContext(), bean);
 assertTrue(bean.prop2 == null);
 }

 @Test
 public void testHandleMissingProperty() throws Exception {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[] {
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop1"), StringDeserializer.instance),
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop2"), IntegerDeserializer.instance),
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop3"), new ArrayTypeDeserializer(), "typeIds")
 }, new HashMap<String, Integer>(), null, null);
 handler.start();

 JsonParser jp = mapper.getFactory().createParser("{\"prop2\":42}");
 handler.handleTypePropertyValue(jp, mapper.getDeserializationContext(), "prop1", bean);
 handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "prop1", bean); // should be "prop2"
 handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "prop2", bean);

 // missing "prop3" property
 jp.nextToken();
 handler.complete(jp, mapper.getDeserializationContext(), bean);
 assertTrue(bean.prop3 == null);
 }

 @Test(expected = JsonMappingException.class)
 public void testMalformedJson() throws Exception {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[] {
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop1"), StringDeserializer.instance),
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop2"), IntegerDeserializer.instance),
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop3"), new ArrayTypeDeserializer(), "typeIds")
 }, new HashMap<String, Integer>(), null, null);
 handler.start();

 JsonParser jp = mapper.getFactory().createParser("{\"prop1\":\"test\"");
 handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "prop1", bean);
 handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "prop2", bean); // this one is missing
 handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "prop3", bean);
 handler.complete(jp, mapper.getDeserializationContext(), bean);
 }

 @Test(expected = JsonProcessingException.class)
 public void testMissingTokenAfterComplete() throws Exception {
 TestBean bean = new TestBean();
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[] {
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop1"), StringDeserializer.instance),
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop2"), IntegerDeserializer.instance),
 new ExtTypedProperty(TestBean.class.getDeclaredField("prop3"), new ArrayTypeDeserializer(), "typeIds")
 }, new HashMap<String, Integer>(), null, null);
 handler.start();

 JsonParser jp = mapper.getFactory().createParser("{\"prop1\":\"test\", \"prop2\":42}");
 handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "prop1", bean);
 handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "prop2", bean);
 handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "prop3", bean);
 handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "prop3", bean); // this one is extra
 handler.complete(jp, mapper.getDeserializationContext(), bean);
 }
}