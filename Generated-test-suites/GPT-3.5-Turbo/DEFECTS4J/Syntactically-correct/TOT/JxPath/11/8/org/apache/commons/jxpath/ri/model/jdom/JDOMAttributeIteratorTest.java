import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    private NodePointer parent;
    private Element parentElement;
    private JXPathContext context;

    @Before
    public void setUp() {
        parentElement = new Element("parent");
        parentElement.addContent(new Attribute("attr1", "value1"));
        parentElement.addContent(new Element("child"));
        parent = mock(NodePointer.class);
        context = JXPathContext.newContext(null, parent);
    }

    @Test
    public void testConstructor_ElementNoNamespace() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        assertTrue(iterator.setPosition(1));
        assertEquals("value1", iterator.getNodePointer().getValue());
        assertFalse(iterator.setPosition(2));
    }

    @Test
    public void testConstructor_ElementWithNamespace() {
        Namespace ns = Namespace.getNamespace("test", "http://www.example.com");
        parentElement.addNamespaceDeclaration(ns);
        parentElement.setAttribute(new Attribute("test:attr2", ns));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr2", "test"));
        assertTrue(iterator.setPosition(1));
        assertEquals(ns, iterator.getNodePointer().getValue());
        assertFalse(iterator.setPosition(2));
    }

    @Test
    public void testConstructor_ElementWildcard() {
        parentElement.setAttribute(new Attribute("attr2", "value2"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));
        assertTrue(iterator.setPosition(1));
        assertEquals("value1", iterator.getNodePointer().getValue());
        assertTrue(iterator.setPosition(2));
        assertEquals("value2", iterator.getNodePointer().getValue());
        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testConstructor_NonElement() {
        parentElement = new Element("parent");
        parent = mock(NodePointer.class);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        assertFalse(iterator.setPosition(1));
        assertEquals(null, iterator.getNodePointer());
        assertFalse(iterator.setPosition(2));
    }

    @Test(expected = JXPathException.class)
    public void testSetPosition_InvalidPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        iterator.setPosition(0);
    }

    @Test
    public void testGetNodePointer_Attribute() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        iterator.setPosition(1);
        Attribute attribute = (Attribute) iterator.getNodePointer().getNode();
        assertEquals("attr1", attribute.getName());
        assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testGetNodePointer_NoAttributes() {
        parentElement = new Element("parent");
        parent = mock(NodePointer.class);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        assertFalse(iterator.setPosition(1));
        assertEquals(null, iterator.getNodePointer());
    }
}