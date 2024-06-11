package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {
     @Test
     public void testJDOMAttributeIteratorConstructionWithEmptyElement() {
         NodePointer parent = new JDOMPointer(new JDOMDocument(), new Element("empty"));
         JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));
         assertNull(iterator.getNodePointer());
     }

     @Test
     public void testJDOMAttributeIteratorConstructionWithElementAndGenericQName() {
         String xmlNamespace = "http://www.w3.org/XML/1998/namespace";
         String xlinkNamespace = "http://www.w3.org/1999/xlink";
         NodePointer parent = new JDOMPointer(new JDOMDocument(), new Element("root", new QName("root")));
         QName name = new QName("*");
         JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
         List attributes = iterator.getAttributes();
         assertEquals(3, attributes.size());
         assertNotNull(attributes.get(0));
         assertSame(new QName("xml:version"), attributes.get(0).getName());
         assertSame(Namespace.XML_NAMESPACE, attributes.get(0).getNamespace());
         assertNull(attributes.get(0).getValue());
         assertNotNull(attributes.get(1));
         assertSame(new QName("xml:lang"), attributes.get(1).getName());
         assertSame(Namespace.XML_NAMESPACE, attributes.get(1).getNamespace());
         assertEquals("en", attributes.get(1).getValue());
         assertNotNull(attributes.get(2));
         assertSame(new QName("xml:base"), attributes.get(2).getName());
         assertSame(Namespace.XML_NAMESPACE, attributes.get(2).getNamespace());
         assertEquals("file:test.xml", attributes.get(2).getValue());
         NodePointer attributePointer = iterator.getNodePointer();
         assertNotNull(attributePointer);
         assertSame(attributes.get(0), attributePointer.getNode());
         iterator.next();
         assertSame(attributes.get(1), attributePointer.getNode());
         iterator.next();
         assertSame(attributes.get(2), attributePointer.getNode());
         iterator.next();
         assertNull(iterator.getNodePointer());
     }

     @Test
     public void testJDOMAttributeIteratorConstructionWithElementAndSpecificQName() {
         String xmlNamespace = "http://www.w3.org/XML/1998/namespace";
         String xlinkNamespace = "http://www.w3.org/1999/xlink";
         NodePointer parent = new JDOMPointer(new JDOMDocument(), new Element("root", new QName("root")));
         QName name = new QName("xml:lang");
         JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
         List attributes = iterator.getAttributes();
         assertEquals(1, attributes.size());
         assertNotNull(attributes.get(0));
         assertSame(name, attributes.get(0).getName());
         assertSame(Namespace.XML_NAMESPACE, attributes.get(0).getNamespace());
         assertEquals("en", attributes.get(0).getValue());
         NodePointer attributePointer = iterator.getNodePointer();
         assertNotNull(attributePointer);
         assertSame(attributes.get(0), attributePointer.getNode());
         iterator.next();
         assertNull(iterator.getNodePointer());
     }

     @Test
     public void testJDOMAttributeIteratorConstructionWithElementAndNamespace() {
         String xmlNamespace = "http://www.w3.org/XML/1998/namespace";
         String xlinkNamespace = "http://www.w3.org/1999/xlink";
         NodePointer parent = new JDOMPointer(new JDOMDocument(), new Element("root", new QName("root")));
         Namespace ns = Namespace.getNamespace("xml");
         JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "xml:lang"));
         List attributes = iterator.getAttributes();
         assertEquals(1, attributes.size());
         assertNotNull(attributes.get(0));
         assertSame(new QName("xml:lang"), attributes.get(0).getName());
         assertSame(ns, attributes.get(0).getNamespace());
         assertEquals("en", attributes.get(0).getValue());
         NodePointer attributePointer = iterator.getNodePointer();
         assertNotNull(attributePointer);
         assertSame(attributes.get(0), attributePointer.getNode());
         iterator.next();
         assertNull(iterator.getNodePointer());
     }

     @Test
     public void testJDOMAttributeIteratorConstructionWithElementAndEmptyNamespace() {
         String xmlNamespace = "http://www.w3.org/XML/1998/namespace";
         String xlinkNamespace = "http://www.w3.org/1999/xlink";
         NodePointer parent = new JDOMPointer(new JDOMDocument(), new Element("root", new QName("root")));
         Namespace ns = Namespace.NO_NAMESPACE;
         JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
         List attributes = iterator.getAttributes();
         assertEquals(2, attributes.size());
         assertNotNull(attributes.get(0));
         assertSame(new QName("xml:version"), attributes.get(0).getName());
         assertSame(Namespace.XML_NAMESPACE, attributes.get(0).getNamespace());
         assertNull(attributes.get(0).getValue());
         assertNotNull(attributes.get(1));
         assertSame(new QName("xml:lang"), attributes.get(1).getName());
         assertSame(Namespace.XML_NAMESPACE, attributes.get(1).getNamespace());
         assertEquals("en", attributes.get(1).getValue());
         NodePointer attributePointer = iterator.getNodePointer();
         assertNotNull(attributePointer);
         assertSame(attributes.get(0), attributePointer.getNode());
         iterator.next();
         assertSame(attributes.get(1), attributePointer.getNode());
         iterator.next();
         assertNull(iterator.getNodePointer());
     }

     @Test
     public void testJDOMAttributeIteratorConstructionWithElementAndNamespaceAndGenericQName() {
         String xmlNamespace = "http://www.w3.org/XML/1998/namespace";
         String xlinkNamespace = "http://www.w3.org/1999/xlink";
         NodePointer parent = new JDOMPointer(new JDOMDocument(), new Element("root", new QName("root")));
         Namespace ns = Namespace.getNamespace("xml");
         JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "*"));
         List attributes = iterator.getAttributes();
         assertEquals(3, attributes.size());
         assertNotNull(attributes.get(0));
         assertSame(new QName("xml:version"), attributes.get(0).getName());
         assertSame(Namespace.XML_NAMESPACE, attributes.get(0).getNamespace());
         assertNull(attributes.get(0).getValue());
         assertNotNull(attributes.get(1));
         assertSame(new QName("xml:lang"), attributes.get(1).getName());
         assertSame(Namespace.XML_NAMESPACE, attributes.get(1).getNamespace());
         assertEquals("en", attributes.get(1).getValue());
         assertNotNull(attributes.get(2));
         assertSame(new QName("xml:base"), attributes.get(2).getName());
         assertSame(Namespace.XML_NAMESPACE, attributes.get(2).getNamespace());
         assertEquals("file:test.xml", attributes.get(2).getValue());
         NodePointer attributePointer = iterator.getNodePointer();
         assertNotNull(attributePointer);
         assertSame(attributes.get(0), attributePointer.getNode());
         iterator.next();
         assertSame(attributes.get(1), attributePointer.getNode());
         iterator.next();
         assertSame(attributes.get(2), attributePointer.getNode());
         iterator.next();
         assertNull(iterator.getNodePointer());
     }

     @Test
     public void testJDOMAttributeIteratorConstructionWithElementAndNamespaceAndSpecificQName() {
         String xmlNamespace = "http://www.w3.org/XML/1998/namespace";
         String xlinkNamespace = "http://www.w3.org/1999/xlink";
         NodePointer parent = new JDOMPointer(new JDOMDocument(), new Element("root", new QName("root")));
         Namespace ns = Namespace.getNamespace("xml");
         JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "lang"));
         List attributes = iterator.getAttributes();
         assertEquals(1, attributes.size());
         assertNotNull(attributes.get(0));
         assertSame(new QName("xml:lang"), attributes.get(0).getName());
         assertSame(ns, attributes.get(0).getNamespace());
         assertEquals("en", attributes.get(0).getValue());
         NodePointer attributePointer = iterator.getNodePointer();
         assertNotNull(attributePointer);
         assertSame(attributes.get(0), attributePointer.getNode());
         iterator.next();
         assertNull(iterator.getNodePointer());
     }

     @Test
     public void testJDOMAttributeIteratorWithEmptyList() {
         String xmlNamespace = "http://www.w3.org/XML/1998/namespace";
         String xlinkNamespace = "http://www.w3.org/1999/xlink";
         NodePointer parent = new JDOMPointer(new JDOMDocument(), new Element("root", new QName("root")));
         JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("no:such:name"));
         List attributes = iterator.getAttributes();
         assertEquals(0, attributes.size());
         NodePointer attributePointer = iterator.getNodePointer();
         assertNull(attributePointer);
     }
}