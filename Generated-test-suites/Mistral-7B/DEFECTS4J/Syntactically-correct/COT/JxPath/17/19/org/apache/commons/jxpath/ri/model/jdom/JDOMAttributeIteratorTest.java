import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private NodePointer parentNode;
 private QName attributeName;
 private JDOMAttributeIterator attributeIterator;

 @Test
 public void testJDOMAttributeIteratorConstructorWithSpecificAttribute() {
     Element rootElement = new Element("root");
     Attribute specificAttribute = new Attribute("specific", "specificValue", "specificNamespace");
     rootElement.addAttribute(specificAttribute);

     parentNode = new JDOMNodePointer(rootElement);
     attributeName = new QName("specific", "specificNamespace");

     attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
     NodePointer nodePointer = attributeIterator.getNodePointer();
     assertEquals(specificAttribute, nodePointer.getNode());
     assertTrue(attributeIterator.hasNext());
 }

 @Test
 public void testJDOMAttributeIteratorConstructorWithEmptyList() {
     Element rootElement = new Element("root");
     parentNode = new JDOMNodePointer(rootElement);
     attributeName = new QName("nonExistent", null);

     attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
     assertFalse(attributeIterator.hasNext());
 }

 @Test
 public void testJDOMAttributeIteratorConstructorWithAllAttributes() {
     Element rootElement = new Element("root");
     rootElement.setAttribute("attr1", "val1");
     rootElement.setAttribute("attr2", "val2");
     rootElement.setAttribute("attr3", "val3");

     parentNode = new JDOMNodePointer(rootElement);
     attributeName = new QName("*", null);

     attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
     int position = 0;
     while (attributeIterator.hasNext()) {
         NodePointer nodePointer = attributeIterator.getNodePointer();
         Attribute attribute = (Attribute) nodePointer.getNode();
         assertEquals(position, attribute.getValue().getValue());
         position++;
         attributeIterator.next();
     }
     assertEquals(3, position);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJDOMAttributeIteratorOutOfBoundsException() {
     Element rootElement = new Element("root");
     rootElement.setAttribute("attr1", "val1");
     rootElement.setAttribute("attr2", "val2");
     rootElement.setAttribute("attr3", "val3");

     parentNode = new JDOMNodePointer(rootElement);
     attributeName = new QName("*", null);

     attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
     attributeIterator.next();
     attributeIterator.next();
     attributeIterator.next();
     attributeIterator.next(); // IndexOutOfBoundsException should be thrown here
 }

 @Test
 public void testJDOMAttributeIteratorReset() {
     Element rootElement = new Element("root");
     rootElement.setAttribute("attr1", "val1");
     rootElement.setAttribute("attr2", "val2");
     rootElement.setAttribute("attr3", "val3");

     parentNode = new JDOMNodePointer(rootElement);
     attributeName = new QName("*", null);

     attributeIterator = new JDOMAttributeIterator(parentNode, attributeName);
     int position = 0;
     while (attributeIterator.hasNext()) {
         NodePointer nodePointer = attributeIterator.getNodePointer();
         Attribute attribute = (Attribute) nodePointer.getNode();
         assertEquals(position, attribute.getValue().getValue());
         position++;
         attributeIterator.next();
     }
     assertEquals(3, position);
     attributeIterator.reset();
     position = 0;
     while (attributeIterator.hasNext()) {
         NodePointer nodePointer = attributeIterator.getNodePointer();
         Attribute attribute = (Attribute) nodePointer.getNode();
         assertEquals(position, attribute.getValue().getValue());
         position++;
         attributeIterator.next();
     }
     assertEquals(3, position);
 }

}