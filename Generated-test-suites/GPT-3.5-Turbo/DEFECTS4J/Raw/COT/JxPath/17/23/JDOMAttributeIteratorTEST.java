import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.mockito.Mockito;
import java.util.*;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator attributeIterator;
    private NodePointer parent;

    @Before
    public void setUp() {
        parent = Mockito.mock(NodePointer.class);
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attribute"));
    }

    @Test
    public void testConstructor() {
        assertNotNull(attributeIterator);
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        attributeIterator.setPosition(2);
        assertEquals(2, attributeIterator.getPosition());
        
        attributeIterator.setPosition(0);
        assertEquals(0, attributeIterator.getPosition());
        
        attributeIterator.setPosition(10);
        assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testGetNodePointer() {
        Element element = Mockito.mock(Element.class);
        Attribute attribute = Mockito.mock(Attribute.class);
        Mockito.when(parent.getNode()).thenReturn(element);
        Mockito.when(element.getAttribute("attribute", Namespace.NO_NAMESPACE)).thenReturn(attribute);
        assertEquals(attribute, attributeIterator.getNodePointer().getNode());
    }
}