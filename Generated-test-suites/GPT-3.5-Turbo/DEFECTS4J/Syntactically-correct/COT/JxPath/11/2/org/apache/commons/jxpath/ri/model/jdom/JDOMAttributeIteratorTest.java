import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {
    private static final String XML = "<root><element attr1=\"value1\" attr2=\"value2\"/></root>";

    private JXPathContext context;
    private Element rootElement;

    @Before
    public void setUp() {
        context = JXPathContext.newContext(XML);
        rootElement = (Element) context.getContextNodePointer().getNode();
    }

    @Test
    public void testGetNodePointer() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context.getContextNodePointer(), new QName("*"));
        NodePointer nodePointer = iterator.getNodePointer();

        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals(rootElement.getAttribute("attr1"), ((JDOMAttributePointer) nodePointer).getJdomAttribute());
    }

    @Test
    public void testGetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context.getContextNodePointer(), new QName("*"));
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context.getContextNodePointer(), new QName("*"));

        assertFalse(iterator.setPosition(0));
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));
        assertFalse(iterator.setPosition(3));
    }
}