import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class JDOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;
    private Element element;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        parent = Mockito.mock(NodePointer.class);
        name = Mockito.mock(QName.class);
        element = Mockito.mock(Element.class);

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("attr1", "value1"));
        attributes.add(new Attribute("attr2", "value2"));
        attributes.add(new Attribute("attr3", "value3"));
        
        when(parent.getNode()).thenReturn(element);
        when(name.getPrefix()).thenReturn(null);
        when(name.getName()).thenReturn("*");
        when(parent.getNamespaceResolver()).thenReturn(Mockito.mock(NamespaceResolver.class));
        when(element.getAttributes()).thenReturn(attributes);

        iterator = new JDOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer attribute1 = iterator.getNodePointer();
        NodePointer attribute2 = iterator.getNodePointer();
        NodePointer attribute3 = iterator.getNodePointer();
        NodePointer attribute4 = iterator.getNodePointer();

        assertEquals("attr1", ((Attribute) attribute1.getNode()).getName());
        assertEquals("attr2", ((Attribute) attribute2.getNode()).getName());
        assertEquals("attr3", ((Attribute) attribute3.getNode()).getName());
        assertEquals(null, attribute4);
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());

        iterator.setPosition(4);
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        boolean result1 = iterator.setPosition(3);
        boolean result2 = iterator.setPosition(5);

        assertEquals(true, result1);
        assertEquals(false, result2);
    }
}