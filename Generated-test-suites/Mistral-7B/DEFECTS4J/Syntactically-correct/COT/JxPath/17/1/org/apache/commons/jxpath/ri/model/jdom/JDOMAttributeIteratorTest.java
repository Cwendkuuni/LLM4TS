import org.apache.commons.jxpath.ri.model.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.jdom.Namespace;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.NamespaceException;
import org.jdom.jxpath.JDOMXPath;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_Empty() {
        NodePointer parent = new JDOMNodePointer(null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_SingleAttribute() {
        String xmlNS = "http://www.w3.org/XML/1998/namespace";
        String prefix = "xml";
        String localName = "xmlns";
        Element element = new Element("root");
        element.addAttribute(new Attribute(localName, "http://www.w3.org/2000/xmlns/xhtml", xmlNS));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));
        Assert.assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        Assert.assertTrue(next instanceof JDOMAttributePointer);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) next;
        Assert.assertEquals(localName, attributePointer.getName().getLocalPart());
        Assert.assertEquals(xmlNS, attributePointer.getName().getNamespaceURI());
        Assert.assertNull(attributePointer.getNode());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes() {
        String xmlNS = "http://www.w3.org/XML/1998/namespace";
        String prefix = "xml";
        String localName1 = "xmlns";
        String localName2 = "class";
        Element element = new Element("root");
        element.addAttribute(new Attribute(localName1, "http://www.w3.org/2000/xmlns/xhtml", xmlNS));
        element.addAttribute(new Attribute(localName2, null, "foo"));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, "*"));
        Assert.assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        Assert.assertTrue(next instanceof JDOMAttributePointer);
        JDOMAttributePointer attributePointer1 = (JDOMAttributePointer) next;
        Assert.assertEquals(localName1, attributePointer1.getName().getLocalPart());
        Assert.assertEquals(xmlNS, attributePointer1.getName().getNamespaceURI());
        Assert.assertNull(attributePointer1.getNode());
        Assert.assertTrue(iterator.hasNext());
        next = iterator.next();
        Assert.assertTrue(next instanceof JDOMAttributePointer);
        JDOMAttributePointer attributePointer2 = (JDOMAttributePointer) next;
        Assert.assertEquals(localName2, attributePointer2.getName().getLocalPart());
        Assert.assertNull(attributePointer2.getName().getNamespaceURI());
        Assert.assertNull(attributePointer2.getNode());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testJDOMAttributeIterator_NegativePosition() {
        String xmlNS = "http://www.w3.org/XML/1998/namespace";
        String prefix = "xml";
        String localName = "xmlns";
        Element element = new Element("root");
        element.addAttribute(new Attribute(localName, "http://www.w3.org/2000/xmlns/xhtml", xmlNS));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));
        iterator.setPosition(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_NullParent() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName("", "*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_NullName() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(null), null);
    }

    @Test(expected = NamespaceException.class)
    public void testJDOMAttributeIterator_UnknownNamespace() {
        String prefix = "unknown";
        String localName = "xmlns";
        Element element = new Element("root");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_EmptyNamespaceURI() {
        String prefix = "empty";
        String localName = "xmlns";
        Element element = new Element("root");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));
    }

    @Test
    public void testJDOMAttributeIterator_XMLNSAttribute() {
        String xmlNS = "http://www.w3.org/XML/1998/namespace";
        String prefix = "xml";
        String localName = "xmlns:xhtml";
        Element element = new Element("root");
        element.addAttribute(new Attribute(localName, xmlNS));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));
        Assert.assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        Assert.assertTrue(next instanceof JDOMAttributePointer);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) next;
        Assert.assertEquals(localName, attributePointer.getName().getLocalPart());
        Assert.assertEquals(xmlNS, attributePointer.getName().getNamespaceURI());
        Assert.assertNull(attributePointer.getNode());
        Assert.assertFalse(iterator.hasNext());

        // Test with prefix "xml:"
        iterator = new JDOMAttributeIterator(parent, new QName("xml:", localName));
        Assert.assertTrue(iterator.hasNext());
        next = iterator.next();
        Assert.assertEquals(localName, next.getName().getLocalPart());
        Assert.assertEquals(xmlNS, next.getName().getNamespaceURI());
        Assert.assertNull(next.getNode());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_WildcardAttribute() {
        String xmlNS = "http://www.w3.org/XML/1998/namespace";
        String prefix = null;
        String localName = "xmlns";
        Element element = new Element("root");
        element.addAttribute(new Attribute(localName, xmlNS));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, "*"));
        Assert.assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        Assert.assertTrue(next instanceof JDOMAttributePointer);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) next;
        Assert.assertEquals(localName, attributePointer.getName().getLocalPart());
        Assert.assertEquals(xmlNS, attributePointer.getName().getNamespaceURI());
        Assert.assertNull(attributePointer.getNode());
        Assert.assertTrue(iterator.hasNext());
        next = iterator.next();
        Assert.assertNull(next);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_NamespaceResolution() {
        String xmlNS = "http://www.w3.org/XML/1998/namespace";
        String xmlNS2 = "http://www.w3.org/2000/xmlns/xhtml";
        String prefix = "xml";
        String localName = "xmlns";
        Element element = new Element("root");
        NodePointer parent = new JDOMNodePointer(element);
        JXPathContext context = new JXPathContext(parent.getNode());
        context.setNamespaceContext(new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                if (prefix.equals(xmlNS)) {
                    return xmlNS;
                }
                if (prefix.equals(xmlNS2)) {
                    return xmlNS2;
                }
                return null;
            }

            @Override
            public String getPrefix(String uri) {
                if (uri.equals(xmlNS)) {
                    return xmlNS;
                }
                if (uri.equals(xmlNS2)) {
                    return xmlNS2;
                }
                return null;
            }
        });
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlNS, localName));
        Assert.assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        Assert.assertTrue(next instanceof JDOMAttributePointer);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) next;
        Assert.assertEquals(localName, attributePointer.getName().getLocalPart());
        Assert.assertEquals(xmlNS, attributePointer.getName().getNamespaceURI());
        Assert.assertNull(attributePointer.getNode());
        Assert.assertFalse(iterator.hasNext());

        iterator = new JDOMAttributeIterator(parent, new QName(xmlNS2, localName));
        Assert.assertTrue(iterator.hasNext());
        next = iterator.next();
        Assert.assertTrue(next instanceof JDOMAttributePointer);
        attributePointer = (JDOMAttributePointer) next;
        Assert.assertEquals(localName, attributePointer.getName().getLocalPart());
        Assert.assertEquals(xmlNS2, attributePointer.getName().getNamespaceURI());
        Assert.assertNull(attributePointer.getNode());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_MixedAttributes() {
        String xmlNS = "http://www.w3.org/XML/1998/namespace";
        String xmlNS2 = "http://www.w3.org/2000/xmlns/xhtml";
        String prefix = "xml";
        String localName1 = "xmlns";
        String localName2 = "class";
        Element element = new Element("root");
        element.addAttribute(new Attribute(localName1, xmlNS));
        element.addAttribute(new Attribute(localName2, null, "foo"));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, "*"));
        Assert.assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        Assert.assertTrue(next instanceof JDOMAttributePointer);
        JDOMAttributePointer attributePointer1 = (JDOMAttributePointer) next;
        Assert.assertEquals(localName1, attributePointer1.getName().getLocalPart());
        Assert.assertEquals(xmlNS, attributePointer1.getName().getNamespaceURI());
        Assert.assertNull(attributePointer1.getNode());
        Assert.assertTrue(iterator.hasNext());
        next = iterator.next();
        Assert.assertTrue(next instanceof JDOMAttributePointer);
        JDOMAttributePointer attributePointer2 = (JDOMAttributePointer) next;
        Assert.assertEquals(localName2, attributePointer2.getName().getLocalPart());
        Assert.assertNull(attributePointer2.getName().getNamespaceURI());
        Assert.assertNull(attributePointer2.getNode());
        Assert.assertFalse(iterator.hasNext());
    }
}