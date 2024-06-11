import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMElement;
import org.apache.commons.jxpath.ri.model.jdom.JDOMNamespace;
import org.apache.commons.jxpath.ri.model.jdom.JDOMNode;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.NamespaceException;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for JDOMAttributeIterator
 */
public class JDOMAttributeIteratorTest {

    private JDOMElement rootElement;
    private NodeIterator iterator;

    @Before
    public void setUp() throws JDOMException, NamespaceException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);

        Element childElement = new Element("child");
        childElement.addAttribute("attr1", "value1");
        childElement.addAttribute("attr2", "value2", "prefix:ns");
        root.addContent(childElement);

        rootElement = (JDOMElement) root;
    }

    /**
     * Test constructor with explicit namespace and non-wildcard QName.
     */
    @Test
    public void testConstructorWithExplicitNamespaceAndNonWildcardQName() {
        QName name = new QName("prefix:ns", "attr1");
        iterator = new JDOMAttributeIterator(new NodePointer(rootElement), name);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
        Assert.assertEquals("value1", attributePointer.getValue().getValue());
        Assert.assertEquals(rootElement.getChild("child"), attributePointer.getNode().getParent());
    }

    /**
     * Test constructor with null namespace and wildcard QName.
     */
    @Test
    public void testConstructorWithNullNamespaceAndWildcardQName() {
        iterator = new JDOMAttributeIterator(new NodePointer(rootElement), null);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
        Assert.assertEquals("attr2", attributePointer.getName().getLocalPart());
        Assert.assertEquals("value2", attributePointer.getValue().getValue());
        Assert.assertEquals(rootElement.getChild("child"), attributePointer.getNode().getParent());
    }

    /**
     * Test constructor with null namespace and non-wildcard QName.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullNamespaceAndNonWildcardQName() {
        QName name = new QName("prefix:ns", "attr1");
        iterator = new JDOMAttributeIterator(new NodePointer(rootElement), name);
        iterator.setPosition(1);
        iterator.getNodePointer();
    }

    /**
     * Test getNodePointer method with valid position.
     */
    @Test
    public void testGetNodePointerWithValidPosition() {
        iterator = new JDOMAttributeIterator(new NodePointer(rootElement), null);
        iterator.setPosition(1);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
        Assert.assertEquals("attr2", attributePointer.getName().getLocalPart());
        Assert.assertEquals("value2", attributePointer.getValue().getValue());
        Assert.assertEquals(rootElement.getChild("child"), attributePointer.getNode().getParent());
    }

    /**
     * Test getNodePointer method with invalid position.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNodePointerWithInvalidPosition() {
        iterator = new JDOMAttributeIterator(new NodePointer(rootElement), null);
        iterator.setPosition(2);
        iterator.getNodePointer();
    }

    /**
     * Test getPosition method when position is set.
     */
    @Test
    public void testGetPositionWhenPositionIsSet() {
        iterator = new JDOMAttributeIterator(new NodePointer(rootElement), null);
        iterator.setPosition(1);
        Assert.assertEquals(1, iterator.getPosition());
    }

    /**
     * Test getPosition method when position is not set.
     */
    @Test
    public void testGetPositionWhenPositionIsNotSet() {
        iterator = new JDOMAttributeIterator(new NodePointer(rootElement), null);
        Assert.assertEquals(0, iterator.getPosition());
    }

    /**
     * Test setPosition method with valid position.
     */
    @Test
    public void testSetPositionWithValidPosition() {
        iterator = new JDOMAttributeIterator(new NodePointer(rootElement), null);
        iterator.setPosition(1);
        iterator.getNodePointer();
        iterator.setPosition(2);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
        Assert.assertEquals("attr2", attributePointer.getName().getLocalPart());
        Assert.assertEquals("value2", attributePointer.getValue().getValue());
        Assert.assertEquals(rootElement.getChild("child"), attributePointer.getNode().getParent());
    }

    /**
     * Test setPosition method with invalid position.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithInvalidPosition() {
        iterator = new JDOMAttributeIterator(new NodePointer(rootElement), null);
        iterator.setPosition(3);
        iterator.getNodePointer();
    }
}