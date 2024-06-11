import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.Node;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AttributeContextTest {

    private Element rootElement;
    private NodePointer rootNodePointer;
    private AttributeContext attributeContext;
    private NodeNameTest nodeTest;

    @Before
    public void setup() {
        rootElement = new Element("root");
        rootNodePointer = new NodePointer(rootElement);
        nodeTest = new NodeNameTest(new QName("test"));
        attributeContext = new AttributeContext(null, nodeTest);
    }

    @Test
    public void testEmptyElement() {
        JXPathContext jxpathContext = new JXPathContext(rootNodePointer);
        attributeContext.setParentContext(jxpathContext);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testAttributeExists() {
        rootElement.setAttribute("test", "value");
        JXPathContext jxpathContext = new JXPathContext(rootNodePointer);
        attributeContext.setParentContext(jxpathContext);
        assertNotNull(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testAttributeDoesNotExist() {
        JXPathContext jxpathContext = new JXPathContext(rootNodePointer);
        attributeContext.setParentContext(jxpathContext);
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testAttributeIteratorNull() {
        rootElement.setAttribute("test", null);
        JXPathContext jxpathContext = new JXPathContext(rootNodePointer);
        attributeContext.setParentContext(jxpathContext);
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testAttributeIteratorHasNext() {
        rootElement.setAttribute("test1", "value1");
        rootElement.setAttribute("test2", "value2");
        JXPathContext jxpathContext = new JXPathContext(rootNodePointer);
        attributeContext.setParentContext(jxpathContext);
        assertNotNull(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionNegative() {
        rootElement.setAttribute("test", "value");
        JXPathContext jxpathContext = new JXPathContext(rootNodePointer);
        attributeContext.setParentContext(jxpathContext);
        attributeContext.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionOutOfBounds() {
        rootElement.setAttribute("test1", "value1");
        rootElement.setAttribute("test2", "value2");
        JXPathContext jxpathContext = new JXPathContext(rootNodePointer);
        attributeContext.setParentContext(jxpathContext);
        attributeContext.setPosition(2);
    }

    @Test
    public void testSetPositionValid() {
        rootElement.setAttribute("test1", "value1");
        rootElement.setAttribute("test2", "value2");
        JXPathContext jxpathContext = new JXPathContext(rootNodePointer);
        attributeContext.setParentContext(jxpathContext);
        attributeContext.setPosition(1);
        assertNotNull(attributeContext.getCurrentNodePointer());
    }
}