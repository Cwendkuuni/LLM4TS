package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MappingIteratorTest {

 private ObjectMapper objectMapper;
 private JsonFactory jsonFactory;

 @BeforeMethod
 public void setUp() {
 objectMapper = new ObjectMapper();
 jsonFactory = objectMapper.getFactory();
 }

 @Test
 public void testEmptyIterator() throws Exception {
 MappingIterator<Object> iterator = MappingIterator.emptyIterator();
 Assert.assertFalse(iterator.hasNext(), "Should not have next element");
 }

 @Test
 public void testIterateOverSingleElement() throws Exception {
 ObjectNode sourceNode = jsonFactory.createObjectNode();
 sourceNode.put("value", "singleElement");
 JsonParser parser = jsonFactory.createParser(sourceNode);
 JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
 MappingIterator<String> iterator = objectMapper.readerForListOf(String.class).readValues(parser);
 String expectedValue = "singleElement";
 String actualValue = iterator.next();
 Assert.assertEquals(expectedValue, actualValue, "Should have returned correct value");
 Assert.assertFalse(iterator.hasNext(), "Should not have any more values");
 }

 @Test
 public void testIterateOverMultipleElements() throws Exception {
 ObjectNode sourceNode = jsonFactory.createObjectNode();
 sourceNode.put("values", jsonFactory.createValueFactory().createArrayNode()
 .add("firstElement")
 .add("secondElement")
 .add("thirdElement"));
 JsonParser parser = jsonFactory.createParser(sourceNode);
 JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
 MappingIterator<String> iterator = objectMapper.readerForListOf(String.class).readValues(parser);
 String expectedFirstValue = "firstElement";
 String firstValue = iterator.next();
 Assert.assertEquals(expectedFirstValue, firstValue, "Should have returned first correct value");
 String expectedSecondValue = "secondElement";
 String secondValue = iterator.next();
 Assert.assertEquals(expectedSecondValue, secondValue, "Should have returned second correct value");
 String expectedThirdValue = "thirdElement";
 String thirdValue = iterator.next();
 Assert.assertEquals(expectedThirdValue, thirdValue, "Should have returned third correct value");
 Assert.assertFalse(iterator.hasNext(), "Should not have any more values");
 }

 @Test(expectedExceptions = IOException.class)
 public void testClosedParser() throws Exception {
 ObjectNode sourceNode = jsonFactory.createObjectNode();
 JsonParser parser = jsonFactory.createParser(sourceNode);
 parser.close();
 MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, true, null);
 }

 @Test(expectedExceptions = JsonMappingException.class)
 public void testNonMatchingType() throws Exception {
 ObjectNode sourceNode = jsonFactory.createObjectNode();
 sourceNode.put("values", jsonFactory.createValueFactory().createArrayNode()
 .add(1)
 .add(true)
 .add("stringValue"));
 JsonParser parser = jsonFactory.createParser(sourceNode);
 JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
 MappingIterator<Integer> iterator = objectMapper.readerForListOf(Integer.class).readValues(parser);
 }
}