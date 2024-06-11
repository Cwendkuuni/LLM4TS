import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;

public class NullPropertyPointerTest {

   @Test
   public void testGetName() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      QName expected = new QName("*");
      QName result = nullPropertyPointer.getName();
      Assert.assertEquals(expected, result);
   }
   
   @Test
   public void testGetPropertyIndex() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      nullPropertyPointer.setPropertyIndex(5);
   }
   
   @Test
   public void testGetLength() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      int expected = 0;
      int result = nullPropertyPointer.getLength();
      Assert.assertEquals(expected, result);
   }
   
   @Test
   public void testGetBaseValue() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      Object expected = null;
      Object result = nullPropertyPointer.getBaseValue();
      Assert.assertEquals(expected, result);
   }
   
   @Test
   public void testGetImmediateNode() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      Object expected = null;
      Object result = nullPropertyPointer.getImmediateNode();
      Assert.assertEquals(expected, result);
   }
   
   @Test
   public void testIsLeaf() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      boolean result = nullPropertyPointer.isLeaf();
      Assert.assertTrue(result);
   }
   
   @Test
   public void testGetValuePointer() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      NullPointer expected = new NullPointer(nullPropertyPointer,  new QName(getPropertyName()));
      NullPointer result = nullPropertyPointer.getValuePointer();
      Assert.assertEquals(expected, result);
   }
   
   @Test
   public void testIsActualProperty() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      boolean result = nullPropertyPointer.isActualProperty();
      Assert.assertFalse(result);
   }
   
   @Test
   public void testIsActual() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      boolean result = nullPropertyPointer.isActual();
      Assert.assertFalse(result);
   }
   
   @Test
   public void testIsContainer() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      boolean result = nullPropertyPointer.isContainer();
      Assert.assertTrue(result);
   }
   
   @Test(expected = JXPathInvalidAccessException.class)
   public void testSetValue() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      Object value = "test value";
      nullPropertyPointer.setValue(value);
   }
   
   @Test
   public void testCreatePath1() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      JXPathContext context = new JXPathContext();
      ParentNodePointer expected = new ParentNodePointer();
      expected.createPath(context);
      ParentNodePointer result = nullPropertyPointer.createPath(context);
      Assert.assertEquals(expected, result);
   }
   
   @Test
   public void testCreatePath2() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      JXPathContext context = new JXPathContext();
      Object value = "test value";
      ParentNodePointer.expected = new ParentNodePointer();
      expected.createPath(context);
      ParentNodePointer result = nullPropertyPointer.createPath(context, value);
      Assert.assertEquals(expected, result);
   }
   
   @Test
   public void testCreateChild1() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      JXPathContext context = new JXPathContext();
      QName name = new QName("test name");
      int index = 5;
      NullPropertyPointer.expected = new NullPropertyPointer(nullPropertyPointer, name, index);
      NullPropertyPointer result = nullPropertyPointer.createChild(context, name, index);
      Assert.assertEquals(expected, result);
   }
   
   @Test
   public void testCreateChild2() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      JXPathContext context = new JXPathContext();
      QName name = new QName("test name");
      int index = 5;
      Object value = "test value";
      NullPropertyPointer.expected = new NullPropertyPointer(nullPropertyPointer, name, index, value);
      NullPropertyPointer result = nullPropertyPointer.createChild(context, name, index, value);
      Assert.assertEquals(expected, result);
   }
   
   @Test
   public void testGetPropertyName() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      String expected = "*";
      String result = nullPropertyPointer.getPropertyName();
      Assert.assertEquals(expected, result);
   }
   
   @Test
   public void testSetPropertyName() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      String propertyName = "test property name";
      nullPropertyPointer.setPropertyName(propertyName);
      String result = nullPropertyPointer.getPropertyName();
      Assert.assertEquals(propertyName, result);
   }
   
   @Test
   public void testSetNameAttributeValue() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      String attributeValue = "test attribute value";
      nullPropertyPointer.setNameAttributeValue(attributeValue);
      String result = nullPropertyPointer.getPropertyName();
      Assert.assertEquals(attributeValue, result);
   }
   
   @Test
   public void testIsCollection() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      boolean result = nullPropertyPointer.isCollection();
      Assert.assertFalse(result);
   }
   
   @Test
   public void testGetPropertyCount() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      int expected = 0;
      int result = nullPropertyPointer.getPropertyCount();
      Assert.assertEquals(expected, result);
   }
   
   @Test
   public void testGetPropertyNames() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      String[] expected = new String[0];
      String[] result = nullPropertyPointer.getPropertyNames();
      Assert.assertArrayEquals(expected, result);
   }
   
   @Test
   public void testAsPath() {
      NodePointer parent = new ParentNodePointer();
      NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
      String expected = "parent[NullPropertyPointer[@name='*']][6]";
      String result = nullPropertyPointer.asPath();
      Assert.assertEquals(expected, result);
   }
   
   class ParentNodePointer extends NodePointer {
      
      public ParentNodePointer() {
         super(null);
      }
      
      public NodePointer createAttribute(JXPathContext context, QName name) {
         return this;
      }
      
      public NodePointer createChild(JXPathContext context, QName name, int index) {
         return this;
      }
      
      public NodePointer createChild(JXPathContext context, QName name, int index, Object value) {
         return this;
      }
   }
}