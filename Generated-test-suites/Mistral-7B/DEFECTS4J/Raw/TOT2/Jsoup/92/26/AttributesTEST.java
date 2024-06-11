import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

 // Test case for get() method
 @Test
 public void testGetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("color", "red");
 String value = attributes.get("color");
 assertEquals("red", value);
 }

 // Test case for getIgnoreCase() method
 @Test
 public void testGetIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("Color", "red");
 String value = attributes.getIgnoreCase("color");
 assertEquals("red", value);
 }

 // Test case for put() method
 @Test
 public void testPutMethod() {
 Attributes attributes = new Attributes();
 attributes.put("color", "red");
 attributes.put("size", "large");
 String colorValue = attributes.get("color");
 String sizeValue = attributes.get("size");
 assertEquals("red", colorValue);
 assertEquals("large", sizeValue);
 }

 // Test case for put() method with boolean value
 @Test
 public void testPutBooleanMethod() {
 Attributes attributes = new Attributes();
 attributes.put("checked", true);
 assertTrue(attributes.hasKey("checked"));
 assertFalse(attributes.get("checked").isEmpty());
 }

 // Test case for put() method with existing key
 @Test(expected = IllegalArgumentException.class)
 public void testPutExistingKeyMethod() {
 Attributes attributes = new Attributes();
 attributes.put("color", "red");
 attributes.put("color", "blue");
 }

 // Test case for remove() method
 @Test
 public void testRemoveMethod() {
 Attributes attributes = new Attributes();
 attributes.put("color", "red");
 attributes.remove("color");
 assertFalse(attributes.hasKey("color"));
 }

 // Test case for remove() method with non-existent key
 @Test
 public void testRemoveNonExistentKeyMethod() {
 Attributes attributes = new Attributes();
 attributes.remove("size");
 assertFalse(attributes.hasKey("size"));
 }

 // Test case for removeIgnoreCase() method
 @Test
 public void testRemoveIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("Color", "red");
 attributes.removeIgnoreCase("color");
 assertFalse(attributes.hasKey("Color"));
 }

 // Test case for addAll() method
 @Test
 public void testAddAllMethod() {
 Attributes attributes1 = new Attributes();
 attributes1.put("color1", "red1");
 Attributes attributes2 = new Attributes();
 attributes2.put("color2", "red2");
 Attributes result = new Attributes();
 result.addAll(attributes1);
 result.addAll(attributes2);
 assertTrue(result.size() == 4);
 }

 // Test case for hasKey() method
 @Test
 public void testHasKeyMethod() {
 Attributes attributes = new Attributes();
 attributes.put("color", "red");
 assertTrue(attributes.hasKey("color"));
 }

 // Test case for hasKey() method with non-existent key
 @Test
 public void testHasKeyNonExistentKeyMethod() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey("size"));
 }

 // Test case for hasKeyIgnoreCase() method
 @Test
 public void testHasKeyIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("Color", "red");
 assertTrue(attributes.hasKeyIgnoreCase("color"));
 }

 // Test case for size() method
 @Test
 public void testSizeMethod() {
 Attributes attributes = new Attributes();
 attributes.put("color1", "red1");
 attributes.put("size1", "large1");
 assertEquals(2, attributes.size());
 }

 // Test case for clone() method
 @Test
 public void testCloneMethod() {
 Attributes attributes1 = new Attributes();attributes1.put("color1", "red1");Attributes attributes2 = attributes1.clone();assertEquals(attributes1.size(), attributes2.size());assertEquals(attributes1.get("color1"), attributes2.get("color1"));}

 // Test case for dataset() method
 @Test
 public void testDatasetMethod() {
 Attributes attributes = new Attributes();attributes.put("data-color", "red");Map<String, String> dataset = attributes.dataset();assertEquals("red", dataset.get("color"));}

 // Test case for html() method
 @Test
 public void testHtmlMethod() {
 Attributes attributes = new Attributes();attributes.put("color", "red");String html = attributes.html();assertTrue(html.contains(" color=\"red\""));}

 // Test case for equals() method
 @Test
 public void testEqualsMethod() {
 Attributes attributes1 = new Attributes();attributes1.put("color1", "red1");Attributes attributes2 = new Attributes();attributes2.put("color2", "red2");assertFalse(attributes1.equals(attributes2));

 Attributes attributes3 = new Attributes();attributes3.put("color1", "red1");Attributes attributes4 = new Attributes();attributes4.put("color1", "red1");assertTrue(attributes3.equals(attributes4));}
}