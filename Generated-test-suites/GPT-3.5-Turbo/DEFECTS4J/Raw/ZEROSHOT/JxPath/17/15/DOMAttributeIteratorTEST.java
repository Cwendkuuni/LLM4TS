import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    private NodePointer nodePointer;

    @Before
    public void setUp() {
        Element element = prepareElement();
        nodePointer = new DOMNodePointer(element);
    }

    @Test
    public void testIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*", ""));
        assertNotNull(iterator.getNodePointer());
        iterator.setPosition(1);
        assertNotNull(iterator.getNodePointer());
    }

    private Element prepareElement() {
        Element element = mock(Element.class);
        NamedNodeMap namedNodeMap = mock(NamedNodeMap.class);
        Attr attr = mock(Attr.class);
        when(element.getAttributes()).thenReturn(namedNodeMap);
        when(namedNodeMap.item(anyInt())).thenReturn(attr);
        when(attr.getName()).thenReturn("attr1");
        when(attr.getNodeValue()).thenReturn("value1");
        return element;
    }
}