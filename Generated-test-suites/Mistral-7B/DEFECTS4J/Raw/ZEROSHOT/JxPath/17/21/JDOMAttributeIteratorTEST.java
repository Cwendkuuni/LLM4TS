import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null, null), new QName("", "*"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithOneAttribute() {
 String xmlNS = "http://www.w3.org/XML/1998/namespace";
 String myNS = "my NS";
 String myPrefix = "my";
 String attrName = "attr1";
 String attrValue = "value1";

 Element rootElement = new Element("root");
 rootElement.addNamespaceDeclaration(xmlNS, xmlNS);
 rootElement.addNamespaceDeclaration(myNS, myNS);
 rootElement.setAttribute(new QName(myNS, attrName, myPrefix), attrValue);

 NodePointer root = new NodePointer(rootElement, null);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, new QName(myNS, attrName, myPrefix));

 NodePointer nodePointer = iterator.getNodePointer();
 assertEquals(attrValue, nodePointer.getNode().getValue());
 assertTrue(iterator.hasNext());

 nodePointer = iterator.next();
 assertNull(nodePointer.getNode());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 String xmlNS = "http://www.w3.org/XML/1998/namespace";
 String myNS = "my NS";
 String myPrefix = "my";
 String attrName1 = "attr1";
 String attrValue1 = "value1";
 String attrName2 = "attr2";
 String attrValue2 = "value2";

 Element rootElement = new Element("root");
 rootElement.addNamespaceDeclaration(xmlNS, xmlNS);
 rootElement.addNamespaceDeclaration(myNS, myNS);
 rootElement.setAttribute(new QName(myNS, attrName1, myPrefix), attrValue1);
 rootElement.setAttribute(new QName(myNS, attrName2, myPrefix), attrValue2);

 NodePointer root = new NodePointer(rootElement, null);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, new QName(myNS, "*", myPrefix));

 List<NodePointer> nodes = new ArrayList<>();
 for (int i = 0; i < 2; i++) {
 NodePointer nodePointer = iterator.getNodePointer();
 nodes.add(nodePointer);
 iterator.next();
 }

 for (int i = 0; i < 2; i++) {
 NodePointer nodePointer = nodes.get(i);
 String nodeValue = nodePointer.getNode().getValue();
 if (i == 0) {
 assertEquals(attrValue1, nodeValue);
 } else {
 assertEquals(attrValue2, nodeValue);
 }
 }
}

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPrefix() {
 String xmlNS = "http://www.w3.org/XML/1998/namespace";
 String myNS = "my NS";
 String myPrefix = "my";
 String attrName = "attr1";
 String attrValue = "value1";

 Element rootElement = new Element("root");
 rootElement.addNamespaceDeclaration(xmlNS, xmlNS);
 rootElement.addNamespaceDeclaration(myNS, myNS);
 rootElement.setAttribute(new QName(myNS, attrName), attrValue);

 NodePointer root = new NodePointer(rootElement, null);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, new QName("", attrName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidUri() {
 String xmlNS = "http://www.w3.org/XML/1998/namespace";
 String myNS = "my NS";
 String myPrefix = "my";
 String attrName = "attr1";
 String attrValue = "value1";

 Element rootElement = new Element("root");
 rootElement.addNamespaceDeclaration(xmlNS, xmlNS);
 rootElement.addNamespaceDeclaration(myNS, myNS);
 rootElement.setAttribute(new QName(myNS, attrName), attrValue);

 NodePointer root = new NodePointer(rootElement, null);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, new QName("", "", "random"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullParent() {
 NodePointer root = null;
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, new QName("", "*"));
 }

 @Test
 public void testNullQName() {
 NodePointer root = new NodePointer(new Element("root"), null);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(root, null);
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }
}