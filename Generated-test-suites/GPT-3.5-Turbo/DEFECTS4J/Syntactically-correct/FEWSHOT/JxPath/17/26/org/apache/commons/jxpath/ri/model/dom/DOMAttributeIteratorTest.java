import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.mockito.Mock;

public class DOMAttributeIteratorTest {
    @Mock
    NodePointer parent;

    @Mock
    QName name;

    @Mock
    Node node;

    @Mock
    Element element;

    DOMAttributeIterator iterator;

    @Before
    public void setup() {
        parent = mock(NodePointer.class);
        name = mock(QName.class);
        iterator = new DOMAttributeIterator(parent, name);

        when(parent.getNode()).thenReturn(node);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(name.getName()).thenReturn("*");
    }

    @Test
    public void testGetNodePointer() {
        NamedNodeMap map = mock(NamedNodeMap.class);
        Attr attr1 = mock(Attr.class);
        Attr attr2 = mock(Attr.class);

        when(node.getAttributes()).thenReturn(map);
        when(map.getLength()).thenReturn(2);
        when(map.item(0)).thenReturn(attr1);
        when(map.item(1)).thenReturn(attr2);

        when(testAttr(attr1)).thenReturn(true);
        when(testAttr(attr2)).thenReturn(true);

        NodePointer pointer1 = mock(NodePointer.class);
        NodePointer pointer2 = mock(NodePointer.class);

        when(parent.getNamespaceURI(any(String.class))).thenReturn(null);
        when(parent.getNamespaceResolver().getNamespaceURI(any(String.class))).thenReturn(null);
        when(parent.getNamespaceResolver().getNamespaceURI(any())).thenReturn(null);

        when(parent.getNamespaceURI(any())).thenReturn(null);
        when(parent.getNamespaceResolver().getNamespaceURI(any())).thenReturn(null);

        iterator.setPosition(1);
        when(attr1.getNodeName()).thenReturn("attr1");
        when(attr2.getNodeName()).thenReturn("attr2");

        when(parent.getNamespaceURI(any())).thenReturn("");
        when(parent.getNamespaceURI(any())).thenReturn("");
        when(parent.getNamespaceResolver().getNamespaceURI(any())).thenReturn("");

        when(parent.getNamespaceURI(any())).thenReturn("namespaceURI");
        iterator.setPosition(1);
        when(attr1.getNodeName()).thenReturn("attr1");
        assertNull(iterator.getNodePointer()); 

        assertNull(iterator.getNodePointer()); 
        iterator.setPosition(1);
        when(attr1.getNodeName()).thenReturn("attr1");

        when(attr2.getNodeName()).thenReturn("attr2");
        iterator.setPosition(2);

        assertNull(iterator.getNodePointer()); 

        assertNull(iterator.getNodePointer()); 
        iterator.setPosition(1);
        when(attr1.getNodeName()).thenReturn("attr1");
        when(attr2.getNodeName()).thenReturn("attr2");

        when(parent.getNamespaceURI(any())).thenReturn("");
        when(parent.getNamespaceURI(any())).thenReturn("");
        when(parent.getNamespaceResolver().getNamespaceURI(any())).thenReturn("");

        when(parent.getNamespaceURI(any())).thenReturn("namespaceURI");
        iterator.setPosition(1);
        when(attr1.getNodeName()).thenReturn("attr1");
        assertNull(iterator.getNodePointer());

        assertNull(iterator.getNodePointer());
        iterator.setPosition(1);
        when(attr1.getNodeName()).thenReturn("attr1");
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());

        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }

    private boolean testAttr(Attr attr) {
        String nodePrefix = getPrefix(attr);
        String nodeLocalName = getLocalName(attr);

        if (nodePrefix != null && nodePrefix.equals("xmlns")) {
            return false;
        }

        if (nodePrefix == null && nodeLocalName.equals("xmlns")) {
            return false;
        }

        String testLocalName = name.getName();
        if (testLocalName.equals("*") || testLocalName.equals(nodeLocalName)) {
            String testPrefix = name.getPrefix();

            if (equalStrings(testPrefix, nodePrefix)) {
                return true;
            }
            String testNS = null;
            if (testPrefix != null) {
                testNS = parent.getNamespaceURI(testPrefix);
            }
            String nodeNS = null;
            if (nodePrefix != null) {
                nodeNS = parent.getNamespaceURI(nodePrefix);
            }
            return equalStrings(testNS, nodeNS);
        }
        return false;
    }

    private static boolean equalStrings(String s1, String s2) {
        return s1 == s2 || s1 != null && s1.equals(s2);
    }

    private String getPrefix(Attr attr) {
        // TODO Auto-generated method stub
        return null;
    }

    private String getLocalName(Attr attr) {
        // TODO Auto-generated method stub
        return null;
    }
}