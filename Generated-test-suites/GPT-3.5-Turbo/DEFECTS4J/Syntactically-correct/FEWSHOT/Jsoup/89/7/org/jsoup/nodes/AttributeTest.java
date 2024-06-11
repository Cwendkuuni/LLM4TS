package org.jsoup.nodes;
  
 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertNotEquals;
 
 import org.junit.After;
 import org.junit.Before;
 import org.junit.Test;
 
 public class AttributeTest {
 
     private Attribute attribute;
 
     @Before
     public void setUp() {
         attribute = new Attribute("key", "value");
     }
 
     @After
     public void tearDown() {
         attribute = null;
     }
 
     @Test
     public void testGetKey() {
         assertEquals("key", attribute.getKey());
     }
 
     @Test
     public void testSetKey() {
         attribute.setKey("newKey");
         assertEquals("newKey", attribute.getKey());
     }
 
     @Test(expected = IllegalArgumentException.class)
     public void testSetKeyWithNullKey() {
         attribute.setKey(null);
     }
 
     @Test(expected = IllegalArgumentException.class)
     public void testSetKeyWithEmptyKey() {
         attribute.setKey("");
     }
 
     // Test other methods...
     
 }