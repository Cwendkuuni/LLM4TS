package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttributesTest {

 @Test
 public void testInitialization() {
 Attributes attributes = new Attributes();
 assertEquals(0, attributes.size());
 assertNull(attributes.get("test"));
 }

 @Test
 public void testAddingAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("test", "hello");
 assertEquals(1, attributes.size());
 assertEquals("hello", attributes.get("test"));
 }

 @Test
 public void testAddingDuplicateAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("test", "hello");
 attributes.put("test", "world");
 assertEquals(1, attributes.size());
 assertEquals("world", attributes.get("test"));
 }

 @Test
 public void testAddingIgnoreCaseDuplicateAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("test", "hello");
 attributes.put("Test", "world");
 assertEquals(1, attributes.size());
 assertEquals("world", attributes.get("test"));
 }

 @Test
 public void testGettingEmptyAttributeValue() {
 Attributes attributes = new Attributes();
 assertEquals(EmptyString, attributes.get("test"));
 }

 @Test
 public void testGettingIgnoreCaseEmptyAttributeValue() {
 Attributes attributes = new Attributes();
 assertEquals(EmptyString, attributes.getIgnoreCase("test"));
 }

 @Test
 public void testGettingNullAttributeValue() {
 Attributes attributes = new Attributes();
 attributes.put("test", null);
 assertEquals(EmptyString, attributes.get("test"));
 }

 @Test
 public void testGettingIgnoreCaseNullAttributeValue() {
 Attributes attributes = new Attributes();
 attributes.put("Test", null);
 assertEquals(EmptyString, attributes.getIgnoreCase("test"));
 }

 @Test
 public void testGettingBooleanAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("test", "false");
 assertFalse(attributes.get("test"));
 attributes.put("test", "");
 assertFalse(attributes.get("test"));
 attributes.put("test", null);
 assertFalse(attributes.get("test"));
 attributes.put("test", "true");
 assertTrue(attributes.get("test"));
 attributes.put("test", "");
 assertTrue(attributes.get("test"));
 attributes.put("test", null);
 assertFalse(attributes.get("test"));
 }

 @Test
 public void testGettingIgnoreCaseBooleanAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("Test", "FALSE");
 assertFalse(attributes.getIgnoreCase("test"));
 attributes.put("Test", "");
 assertFalse(attributes.getIgnoreCase("test"));
 attributes.put("Test", null);
 assertFalse(attributes.getIgnoreCase("test"));
 attributes.put("Test", "TRUE");
 assertTrue(attributes.getIgnoreCase("test"));
 attributes.put("Test", "");
 assertTrue(attributes.getIgnoreCase("test"));
 attributes.put("Test", null);
 assertFalse(attributes.getIgnoreCase("test"));
 }

 @Test
 public void testRemovingAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("test", "hello");
 assertEquals(1, attributes.size());
 attributes.remove("test");
 assertEquals(0, attributes.size());
 }

 @Test
 public void testRemovingIgnoreCaseAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("TEST", "hello");
 assertEquals(1, attributes.size());
 attributes.removeIgnoreCase("test");
 assertEquals(0, attributes.size());
 }

 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey("test"));
 attributes.put("test", "hello");
 assertTrue(attributes.hasKey("test"));
 }

 @Test
 public void testHasIgnoreCaseKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasIgnoreCaseKey("test"));
 attributes.put("Test", "hello");
 assertTrue(attributes.hasIgnoreCaseKey("test"));
 }

 @Test
 public void testHasKeyWithEmptyStringValue() {
 Attributes attributes = new Attributes();
 attributes.put("test", "");
 assertTrue(attributes.hasKey("test"));
 }

 @Test
 public void testHasIgnoreCaseKeyWithEmptyStringValue() {
 Attributes attributes = new Attributes();
  attributes.put("Test", "");
  assertTrue(attributes.hasIgnoreCaseKey("test"));
 }

 @Test
 public void testHasKeyWithBooleanValue() {
 Attributes attributes = new Attributes();
 attributes.put("test", null);
 assertFalse(attributes.hasKey("test"));
 attributes.put("test", "");
 assertFalse(attributes.put("test", ""));
 assertFalse(attributes.hasKey("test"));
 attributes.put("test", true);
 assertFalse(attributes.hasKey("test"));
 }

 @Test
 public void testHasIgnoreCaseKeyWithBooleanValue() {
 Attributes attributes = new Attributes();
 attributes.put("Test", null);
 assertFalse(attributes.hasIgnoreCaseKey("test"));
 attributes.put("Test", "");
 assertFalse(attributes.hasIgnoreCaseKey("test"));
 attributes.put("Test", null);
 assertFalse(attributes.hasIgnoreCaseKey("test"));
 attributes.put("Test", true);
 assertFalse(attributes.hasIgnoreCaseKey("test"));
 }

 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 assertEquals(0, attributes.size());
 attributes.put("test", "hello");
 assertEquals(1, attributes.size());
 }

 @Test
 public void testAddingMultipleAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("test1", "hello");
 attributes.put("test2", "world");
 assertEquals(2, attributes.size());
 }

 @Test
 public void testAddingMultipleAttributesIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Test1", "hello");
 attributes.put("Test2", "world");
 assertEquals(2, attributes.size());
 }

 @Test
 public void testAddingAndGettingAttributesInOrder() {
 Attributes attributes = new Attributes();
 attributes.put("test1", "hello");
 attributes.put("test2", "world");
 assertEquals("hello", attributes.get("test1"));
 assertEquals("world", attributes.get("test2"));
 }

 @Test
 public void testAddingAndGettingAttributesIgnoreCaseInOrder() {
 Attributes attributes = new Attributes();
 attributes.put("Test1", "hello");
 attributes.put("Test2", "world");
 assertEquals("hello", attributes.getIgnoreCase("test1"));
 assertEquals("world", attributes.getIgnoreCase("test2"));
 }

 @Test
 public void testAddingAndRemovingAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("test1", "hello");
 assertEquals(1, attributes.size());
 attributes.remove("test1");
 assertEquals(0, attributes.size());
 }

 @Test
 public void testAddingAndRemovingAttributesIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Test1", "hello");
 assertEquals(1, attributes.size());
 attributes.removeIgnoreCase("Test1");
 assertEquals(0, attributes.size());
 }

 @Test
 public void testIteratingAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("test1", "hello");
 attributes.put("test2", "world");
 List<Map.Entry<String, String>> attributesList = Arrays.asList(
         new AbstractMap.SimpleEntry<>("test1", "hello"),
         new AbstractMap.SimpleEntry<>("test2", "world")
 );
 Iterator<Map.Entry<String, String>> attributesIterator = attributes.iterator();
 List<Map.Entry<String, String>> resultList = new ArrayList<>();
 while (attributesIterator.hasNext()) {
     resultList.add(attributesIterator.next());
 }
 assertEquals(attributesList, resultList);
 }

 @Test
 public void testIteratingAttributesIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Test1", "hello");
 attributes.put("Test2", "world");
 List<Map.Entry<String, String>> attributesList = Arrays.asList(
         new AbstractMap.SimpleEntry<>("test1", "hello"),
         new AbstractMap.SimpleEntry<>("test2", "world")
 );
 List<Map.Entry<String, String>> resultList = new ArrayList<>();
 for (Map.Entry<String, String> entry : attributes) {
     resultList.add(entry);
 }
 assertEquals(attributesList, resultList);
 }

 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 attributes.put("data-test1", "hello");
 attributes.put("data-test2", "world");
 Map<String, String> dataset = attributes.dataset();
 List<Map.Entry<String, String>> datasetList = Arrays.asList(
         new AbstractMap.SimpleEntry<>("data-test1", "hello"),
         new AbstractMap.SimpleEntry<>("data-test2", "world")
 );
 assertEquals(datasetList, dataset.entrySet().stream().collect(Collectors.toList()));
 }

 @Test
 public void testHtml() {
 Attributes attributes = new Attributes();
 attributes.put("test", "hello world");
 assertEquals(" test=\"hello world\"", attributes.html());
 }

 @Test
 public void testToString() {
 Attributes attributes = new Attributes();
 attributes.put("test", "hello world");
 assertEquals(" attributes=\" test=\"hello world\"\"", attributes.toString());
 }

 @Test
 public void testEquals() {
 Attributes attributes1 = new Attributes();
 attributes1.put("test1", "hello");
 attributes1.put("test2", "world");
 Attributes attributes2 = new Attributes();
 attributes2.put("test1", "hello");
 attributes2.put("test2", "world");
 Attributes attributes3 = new Attributes();
 attributes3.put("test1", "hello");
 attributes3.put("test3", "world");
 Attributes attributes4 = new Attributes();
 Attributes attributes5 = new Attributes();
 assertTrue(attributes1.equals(attributes1));
 assertTrue(attributes1.equals(attributes2));
 assertFalse(attributes1.equals(attributes3));
 assertFalse(attributes1.equals(attributes4));
 assertFalse(attributes1.equals(attributes5));
 }

 @Test
 public void testHashCode() {
 Attributes attributes1 = new Attributes();
 attributes1.put("test1", "hello");
 attributes1.put("test2", "world");
 Attributes attributes2 = new Attributes();
 attributes2.put("test1", "hello");
 attributes2.put("test2", "world");
 Attributes attributes3 = new Attributes();
 attributes3.put("test1", "hello");
 attributes3.put("test3", "world");
 assertEquals(attributes1.hashCode(), attributes2.hashCode());
 assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attributes attributes = new Attributes();
 attributes.put("test1", "hello");
 attributes.put("test2", "world");
 Attributes clonedAttributes = attributes.clone();
 assertEquals(attributes.size(), clonedAttributes.size());
 }

 @Test(expected = CloneNotSupportedException.class)
 public void testCloneThrowsException() throws CloneNotSupportedException {
 Attributes attributes = new Attributes();
 attributes.put("test1", "hello");
 attributes.put("test2", "world");
 Attributes clonedAttributes = (Attributes) attributes.clone();
 }

 @Test
 public void testNormalize() {
 Attributes attributes = new Attributes();
 attributes.put("test", "Hello World");
 attributes.normalize();
 assertEquals("test", attributes.get("test"));
 }
}