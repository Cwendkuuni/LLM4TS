import org.junit.Test;
import org.junit.Before;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Element;
import org.jdom.Attribute;
import org.jdom.Namespace;

import java.util.Collections;

public class JDOMAttributeIteratorTest {

    private static final String TEST_NS = "http://www.example.com";
    private static final String TEST_PREFIX = "test";

    private Element testElement;
    private NodePointer parent;

    @Before
    public void setUp() {
        testElement = new Element("testElement", TEST_NS);
        testElement.setAttribute(new Attribute("attr1", "value1"));
        testElement.setAttribute(new Attribute(TEST_PREFIX, "attr2", TEST_NS, "value2"));
        testElement.setAttribute(new Attribute("attr3", "value3"));
        parent = new JDOMAttributePointer(new JDOMDocumentPointer(testElement.getDocument()), testElement);
    }

    @Test
    public void testSingleNodePointer() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        assert (iterator.setPosition(1));
        NodePointer nodePointer = iterator.getNodePointer();
        assert (nodePointer instanceof JDOMAttributePointer);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        assert (attributePointer.getName().equals("attr1"));
        assert (attributePointer.getNode().getValue().equals("value1"));
        assert (attributePointer.getNamespace() == null);
    }

    @Test
    public void testMultipleNodePointer() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));
        assert (iterator.setPosition(1));
        NodePointer nodePointer = iterator.getNodePointer();
        assert (nodePointer instanceof JDOMAttributePointer);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        assert (attributePointer.getName().equals("test:attr2"));
        assert (attributePointer.getNode().getValue().equals("value2"));
        assert (attributePointer.getNamespace().equals(Namespace.getNamespace(TEST_PREFIX, TEST_NS)));

        assert (iterator.setPosition(2));
        nodePointer = iterator.getNodePointer();
        assert (nodePointer instanceof JDOMAttributePointer);
        attributePointer = (JDOMAttributePointer) nodePointer;
        assert (attributePointer.getName().equals("attr1"));
        assert (attributePointer.getNode().getValue().equals("value1"));
        assert (attributePointer.getNamespace() == null);

        assert (iterator.setPosition(3));
        nodePointer = iterator.getNodePointer();
        assert (nodePointer instanceof JDOMAttributePointer);
        attributePointer = (JDOMAttributePointer) nodePointer;
        assert (attributePointer.getName().equals("attr3"));
        assert (attributePointer.getNode().getValue().equals("value3"));
        assert (attributePointer.getNamespace() == null);
    }

    @Test
    public void testEmptyIterator() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("nonexistentattr"));
        assert (!iterator.setPosition(1));
        NodePointer nodePointer = iterator.getNodePointer();
        assert (nodePointer == null);
    }

    @Test
    public void testOutOfBoundsPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        assert (!iterator.setPosition(0));
        assert (!iterator.setPosition(2));
        NodePointer nodePointer = iterator.getNodePointer();
        assert (nodePointer == null);
    }

    @Test
    public void testEmptyParent() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMDocumentPointer(Collections.EMPTY_LIST), new QName("attr1"));
        assert (!iterator.setPosition(1));
        NodePointer nodePointer = iterator.getNodePointer();
        assert (nodePointer == null);
    }

}