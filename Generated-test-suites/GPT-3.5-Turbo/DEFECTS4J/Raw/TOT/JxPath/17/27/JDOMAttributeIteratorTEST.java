import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.Arrays;
import java.util.Collections;

public class JDOMAttributeIteratorTest {

  @Test
  public void testAttributeIteratorWithSingleAttribute() {
    Element element = new Element("testElement");

    Attribute attribute = new Attribute("testAttribute", "value");
    element.setAttribute(attribute);

    NodePointer parent = new JDOMNodePointer(element);

    JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, new QName("testAttribute"));
    
    // Check if the iterator has a single attribute
    Assert.assertTrue(attributeIterator.setPosition(1));

    NodePointer nodePointer = attributeIterator.getNodePointer();

    Assert.assertNotNull(nodePointer);
    Assert.assertEquals(attribute, ((JDOMAttributePointer) nodePointer).getAttribute());
  }

  @Test
  public void testAttributeIteratorWithMultipleAttributes() {
    Element element = new Element("testElement");

    Attribute attribute1 = new Attribute("attribute1", "value1");
    Attribute attribute2 = new Attribute("attribute2", "value2");
    Attribute attribute3 = new Attribute("attribute3", "value3");

    element.setAttributes(Arrays.asList(attribute1, attribute2, attribute3));

    NodePointer parent = new JDOMNodePointer(element);

    JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, new QName("*"));
    
    // Check if the iterator has multiple attributes
    Assert.assertTrue(attributeIterator.setPosition(1));
    NodePointer attributeNodePointer1 = attributeIterator.getNodePointer();

    Assert.assertTrue(attributeIterator.setPosition(2));
    NodePointer attributeNodePointer2 = attributeIterator.getNodePointer();

    Assert.assertTrue(attributeIterator.setPosition(3));
    NodePointer attributeNodePointer3 = attributeIterator.getNodePointer();

    Attribute[] attributes = new Attribute[]{attribute1, attribute2, attribute3};

    Assert.assertNotNull(attributeNodePointer1);
    Assert.assertNotNull(attributeNodePointer2);
    Assert.assertNotNull(attributeNodePointer3);
    Assert.assertTrue(Arrays.asList(attributes).contains(((JDOMAttributePointer) attributeNodePointer1).getAttribute()));
    Assert.assertTrue(Arrays.asList(attributes).contains(((JDOMAttributePointer) attributeNodePointer2).getAttribute()));
    Assert.assertTrue(Arrays.asList(attributes).contains(((JDOMAttributePointer) attributeNodePointer3).getAttribute()));
  }

  @Test
  public void testAttributeIteratorWithNamespace() {
    Element element = new Element("testElement");

    Namespace namespace1 = Namespace.getNamespace("prefix1", "http://www.example.com/ns1");
    Namespace namespace2 = Namespace.getNamespace("prefix2", "http://www.example.com/ns2");

    Attribute attribute1 = new Attribute("attribute1", "value1", namespace1);
    Attribute attribute2 = new Attribute("attribute2", "value2", namespace2);
    Attribute attribute3 = new Attribute("attribute3", "value3", Namespace.NO_NAMESPACE);

    element.setAttributes(Arrays.asList(attribute1, attribute2, attribute3));

    NodePointer parent = new JDOMNodePointer(element);

    JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, new QName("*"));

    // Check if the iterator returns attributes in the specified namespace
    Assert.assertTrue(attributeIterator.setPosition(1));
    NodePointer attributeNodePointer1 = attributeIterator.getNodePointer();

    Assert.assertTrue(attributeIterator.setPosition(2));
    NodePointer attributeNodePointer2 = attributeIterator.getNodePointer();

    Attribute[] attributes = new Attribute[]{attribute1, attribute2};

    Assert.assertNotNull(attributeNodePointer1);
    Assert.assertNotNull(attributeNodePointer2);
    Assert.assertTrue(Arrays.asList(attributes).contains(((JDOMAttributePointer) attributeNodePointer1).getAttribute()));
    Assert.assertTrue(Arrays.asList(attributes).contains(((JDOMAttributePointer) attributeNodePointer2).getAttribute()));
  }

  @Test
  public void testAttributeIteratorWithInvalidNamespacePrefix() {
    Element element = new Element("testElement");

    Attribute attribute1 = new Attribute("attribute1", "value1");
    element.setAttribute(attribute1);

    NodePointer parent = new JDOMNodePointer(element);

    // Set namespace with invalid prefix
    QName qName = new QName("invalidPrefix:attribute1");

    JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, qName);

    // Check if the iterator returns no attributes
    Assert.assertFalse(attributeIterator.setPosition(1));
    Assert.assertNull(attributeIterator.getNodePointer());
  }

  @Test
  public void testAttributeIteratorWithEmptyIterator() {
    Element element = new Element("testElement");

    NodePointer parent = new JDOMNodePointer(element);

    JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, new QName("*"));

    // Check if the iterator returns no attributes
    Assert.assertFalse(attributeIterator.setPosition(1));
    Assert.assertNull(attributeIterator.getNodePointer());
  }

  @Test
  public void testAttributeIteratorWithOutOfBoundsPosition() {
    Element element = new Element("testElement");

    Attribute attribute = new Attribute("testAttribute", "value");
    element.setAttribute(attribute);

    NodePointer parent = new JDOMNodePointer(element);

    JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, new QName("testAttribute"));

    // Check if the iterator returns null for out of bounds position
    Assert.assertFalse(attributeIterator.setPosition(0));
    Assert.assertNull(attributeIterator.getNodePointer());

    Assert.assertFalse(attributeIterator.setPosition(2));
    Assert.assertNull(attributeIterator.getNodePointer());
  }
}