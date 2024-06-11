import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {
   private NodePointer nodePointer;
   private QName qName;
   private List attributes;
   private DOMAttributeIterator domAttributeIterator;

   @Before
   public void setUp() {
      Element element = new ElementStub();
      nodePointer = new NodePointerStub(element);
      qName = new QNameStub("testPrefix", "testName");
      attributes = new ArrayList();
      domAttributeIterator = new DOMAttributeIterator(nodePointer, qName);
   }

   @Test
   public void testConstructor() {
      Assert.assertEquals(nodePointer, domAttributeIterator.parent);
      Assert.assertEquals(qName, domAttributeIterator.name);
      Assert.assertEquals(attributes, domAttributeIterator.attributes);
      Assert.assertEquals(0, domAttributeIterator.position);
   }

   @Test
   public void testTestAttr() {
      Attr attr = new AttrStub();
      boolean result = domAttributeIterator.testAttr(attr);
      Assert.assertTrue(result);
   }

   @Test
   public void testEqualStrings() {
      boolean result = domAttributeIterator.equalStrings("string1", "string1");
      Assert.assertTrue(result);
      result = domAttributeIterator.equalStrings("string1", null);
      Assert.assertFalse(result);
      result = domAttributeIterator.equalStrings(null, null);
      Assert.assertTrue(result);
   }

   @Test
   public void testGetAttribute() {
      Element element = new ElementStub();
      DOMNodePointer parentNode = new DOMNodePointer(element, null, null);
      DOMAttributeIterator domAttributeIterator = new DOMAttributeIterator(parentNode, qName);
      Attr attr = domAttributeIterator.getAttribute(element, qName);
      Assert.assertNull(attr);
   }

   @Test
   public void testGetNodePointer() {
      NodePointer expected = new DOMAttributePointer(nodePointer, (Attr) attributes.get(0));
      NodePointer result = domAttributeIterator.getNodePointer();
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testGetPosition() {
      int result = domAttributeIterator.getPosition();
      Assert.assertEquals(0, result);
   }

   @Test
   public void testSetPosition() {
      boolean result = domAttributeIterator.setPosition(1);
      Assert.assertTrue(result);
      int position = domAttributeIterator.getPosition();
      Assert.assertEquals(1, position);
      result = domAttributeIterator.setPosition(0);
      Assert.assertFalse(result);
   }
}