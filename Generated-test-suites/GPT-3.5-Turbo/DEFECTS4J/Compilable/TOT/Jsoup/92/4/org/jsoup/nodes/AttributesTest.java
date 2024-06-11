package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

   private Attributes attributes;

   @Before
   public void setUp() {
      attributes = new Attributes();
   }

   @Test
   public void testGet_withValidKey() {
      attributes.put("key1", "value1");
      String result = attributes.get("key1");
      assertEquals("value1", result);
   }

   @Test
   public void testGet_withInvalidKey() {
      attributes.put("key1", "value1");
      String result = attributes.get("key2");
      assertEquals("", result);
   }

   @Test
   public void testGetIgnoreCase_withValidKey() {
      attributes.put("KEY1", "value1");
      String result = attributes.getIgnoreCase("key1");
      assertEquals("value1", result);
   }

   @Test
   public void testGetIgnoreCase_withInvalidKey() {
      attributes.put("KEY1", "value1");
      String result = attributes.getIgnoreCase("key2");
      assertEquals("", result);
   }

   @Test
   public void testPut_withNewKey() {
      Attributes result = attributes.put("key1", "value1");
      assertEquals(1, result.size());
      assertTrue(result.hasKey("key1"));
   }

   @Test
   public void testPut_withExistingKey() {
      attributes.put("key1", "value1");
      Attributes result = attributes.put("key1", "value2");
      assertEquals(1, result.size());
      assertEquals("value2", result.get("key1"));
   }

   @Test
   public void testRemove_withExistingKey() {
      attributes.put("key1", "value1");
      attributes.remove("key1");
      assertFalse(attributes.hasKey("key1"));
   }

   @Test
   public void testRemove_withInvalidKey() {
      attributes.remove("key1");
      assertFalse(attributes.hasKey("key1"));
   }

   @Test
   public void testHasKey_withExistingKey() {
      attributes.put("key1", "value1");
      boolean result = attributes.hasKey("key1");
      assertTrue(result);
   }

   @Test
   public void testHasKey_withInvalidKey() {
      boolean result = attributes.hasKey("key1");
      assertFalse(result);
   }

   @Test
   public void testHasKeyIgnoreCase_withExistingKey() {
      attributes.put("KEY1", "value1");
      boolean result = attributes.hasKeyIgnoreCase("key1");
      assertTrue(result);
   }

   @Test
   public void testHasKeyIgnoreCase_withInvalidKey() {
      boolean result = attributes.hasKeyIgnoreCase("key1");
      assertFalse(result);
   }

   @Test
   public void testSize_withNewAttributes() {
      int result = attributes.size();
      assertEquals(0, result);
   }

   @Test
   public void testSize_withAddedAttributes() {
      attributes.put("key1", "value1");
      attributes.put("key2", "value2");
      int result = attributes.size();
      assertEquals(2, result);
   }

}