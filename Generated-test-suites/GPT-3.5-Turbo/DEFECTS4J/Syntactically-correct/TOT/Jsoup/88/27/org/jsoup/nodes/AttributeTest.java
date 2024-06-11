// Import necessary packages
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
   AttributeKeyTest.class,
   AttributeValueTest.class,
   AttributeParentTest.class
})
public class AttributeTest {
   // Empty - Test suite
}

// Class for testing attribute keys
public class AttributeKeyTest {
   Attribute attribute;

   @Before
   public void setUp() {
      attribute = new Attribute("key", "value");
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
   public void testSetKeyNull() {
      attribute.setKey(null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSetKeyEmpty() {
      attribute.setKey("");
   }

    // Other test cases for key validity and uniqueness can be added here
}

// Class for testing attribute values
public class AttributeValueTest {
   Attribute attribute;

   @Before
   public void setUp() {
      attribute = new Attribute("key", "value");
   }

   @Test
   public void testGetValue() {
      assertEquals("value", attribute.getValue());
   }

   @Test
   public void testSetValue() {
      attribute.setValue("newValue");
      assertEquals("newValue", attribute.getValue());
   }

   @Test
   public void testSetValueParent() {
      Attributes parent = new Attributes();
      parent.put("key", "value");
      attribute.parent = parent;
      attribute.setValue("newValue");
      assertEquals("newValue", parent.get("key"));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSetValueNull() {
      attribute.setValue(null);
   }

    // Other test cases for value validity and uniqueness can be added here
}

// Class for testing attribute parent
public class AttributeParentTest {
   Attribute attribute;

   @Before
   public void setUp() {
      attribute = new Attribute("key", "value");
   }

   @Test
   public void testSetKeyUpdateParent() {
      Attributes parent = new Attributes();
      parent.put("key", "value");
      attribute.parent = parent;
      attribute.setKey("newKey");
      assertNull(parent.get("key"));
      assertEquals("value", parent.get("newKey"));
   }

   @Test
   public void testSetValueUpdateParent() {
      Attributes parent = new Attributes();
      parent.put("key", "value");
      attribute.parent = parent;
      attribute.setValue("newValue");
      assertNull(parent.get("key"));
      assertEquals("newValue", parent.get("key"));
   }

   @Test
   public void testSetValueParentNull() {
      attribute.parent = null;
      attribute.setValue("newValue");
      assertEquals("newValue", attribute.getValue());
   }

   // Other test cases for parent functionality can be added here
}