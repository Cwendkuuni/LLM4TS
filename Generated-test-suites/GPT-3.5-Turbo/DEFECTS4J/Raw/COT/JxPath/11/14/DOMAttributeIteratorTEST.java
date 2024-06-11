
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DOMAttributeIteratorTest {
    
    private DOMNodePointer parent;
    private Element element;
    private DOMAttributeIterator attributeIterator;
    
    @Before
    public void setup() {
        parent = mock(DOMNodePointer.class);
        element = mock(Element.class);
        when(parent.getNode()).thenReturn(element);
    }

    @Test
    public void testConstructor_withMatchingAttributes_addsAttributesToList() {
        Attr attr1 = createMockAttr("attr1");
        Attr attr2 = createMockAttr("attr2");
        when(element.getAttributes()).thenReturn(createMockNamedNodeMap(attr1, attr2));
        
        attributeIterator = new DOMAttributeIterator(parent, new QName("*"));
        
        NodeList nodeList = attributeIterator.getNodeList();
        assertEquals(2, nodeList.getLength());
        assertEquals(attr1, nodeList.item(0));
        assertEquals(attr2, nodeList.item(1));
    }
    
    @Test
    public void testConstructor_withNonMatchingAttributes_doesNotAddAttributesToList() {
        Attr attr1 = createMockAttr("attr1");
        Attr attr2 = createMockAttr("attr2");
        when(element.getAttributes()).thenReturn(createMockNamedNodeMap(attr1, attr2));
        
        attributeIterator = new DOMAttributeIterator(parent, new QName("nonExistentAttr"));
        
        NodeList nodeList = attributeIterator.getNodeList();
        assertEquals(0, nodeList.getLength());
    }
    
    @Test
    public void testConstructor_withSpecificAttribute_addsAttributeToList() {
        Attr attr1 = createMockAttr("attr1");
        Attr attr2 = createMockAttr("attr2");
        when(element.getAttributes()).thenReturn(createMockNamedNodeMap(attr1, attr2));
        
        attributeIterator = new DOMAttributeIterator(parent, new QName("attr1"));
        
        NodeList nodeList = attributeIterator.getNodeList();
        assertEquals(1, nodeList.getLength());
        assertEquals(attr1, nodeList.item(0));
    }
    
    // Additional test cases for edge cases and exception handling scenarios can be added here

    private Attr createMockAttr(String attrName) {
        Attr attr = mock(Attr.class);
        when(attr.getName()).thenReturn(attrName);
        return attr;
    }
    
    private NamedNodeMap createMockNamedNodeMap(Attr... attributes) {
        NamedNodeMap namedNodeMap = mock(NamedNodeMap.class);
        for (int i = 0; i < attributes.length; i++) {
            when(namedNodeMap.item(i)).thenReturn(attributes[i]);
        }
        when(namedNodeMap.getLength()).thenReturn(attributes.length);
        return namedNodeMap;
    }

    // Add additional helper methods and test cases as necessary
}
