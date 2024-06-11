import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

   @Test
   public void testAttributeConstructor() {
	   Attribute attribute = new Attribute("key", "value");
	   assertNotNull(attribute);
	   assertEquals("key", attribute.getKey());
	   assertEquals("value", attribute.getValue());
   }
   
   @Test
   public void testAttributeSetKey() {
	   Attribute attribute = new Attribute("key", "value");
	   attribute.setKey("newKey");
	   assertEquals("newKey", attribute.getKey());
   }
   
   @Test
   public void testAttributeSetValue() {
	   Attribute attribute = new Attribute("key", "value");
	   String oldValue = attribute.setValue("newValue");
	   assertEquals("value", oldValue);
	   assertEquals("newValue", attribute.getValue());
   }
   
   @Test
   public void testAttributeHTML() {
	   Attribute attribute = new Attribute("key", "value");
	   String html = attribute.html();
	   assertNotNull(html);
   }

   @Test
   public void testAttributeEquals() {
	   Attribute attribute1 = new Attribute("key", "value");
	   Attribute attribute2 = new Attribute("key", "value");
	   Attribute attribute3 = new Attribute("key", "newValue");
	   
	   assertEquals(attribute1, attribute2);
	   assertNotEquals(attribute1, attribute3);
   }
   
   @Test
   public void testAttributeHashCode() {
	   Attribute attribute1 = new Attribute("key", "value");
	   Attribute attribute2 = new Attribute("key", "value");
	   
	   assertEquals(attribute1.hashCode(), attribute2.hashCode());
   }
   
   @Test
   public void testAttributeClone() {
	   Attribute attribute1 = new Attribute("key", "value");
	   Attribute attribute2 = attribute1.clone();
	   
	   assertEquals(attribute1, attribute2);
   }

}