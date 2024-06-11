import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class JDOMAttributeIteratorTest {

    @Mock
    private NodePointer parent;
    
    @Mock
    private Attribute attribute1;

    @Mock
    private Attribute attribute2;

    private JDOMAttributeIterator iterator;
    
    private QName name;
    
    @Before
    public void setup() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
        element.setAttribute("attr4", "value4");
        element.setAttribute("attr5", "value5");
        
        name = new QName("attr1");
        
        iterator = new JDOMAttributeIterator(parent, name);
        iterator.attributes = new ArrayList<>();
        iterator.attributes.add(attribute1);
        iterator.attributes.add(attribute2);
    }

    @Test
    public void testGetNodePointer() {
        iterator.position = 1;
        
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(JDOMAttributePointer.class, nodePointer.getClass());
    }

    @Test
    public void testGetPosition() {
        iterator.position = 3;
        
        int position = iterator.getPosition();
        
        Assert.assertEquals(3, position);
    }

    @Test
    public void testSetPosition_Positive() {
        boolean result = iterator.setPosition(1);
        
        Assert.assertTrue(result);
        Assert.assertEquals(1, iterator.position);
    }

    @Test
    public void testSetPosition_Negative() {
        boolean result = iterator.setPosition(0);
        
        Assert.assertFalse(result);
        Assert.assertEquals(0, iterator.position);
    }
    
    @Test
    public void testSetPosition_OutOfBounds() {
        boolean result = iterator.setPosition(3);
        
        Assert.assertFalse(result);
        Assert.assertEquals(3, iterator.position);
    }

    @Test
    public void testSetPosition_FirstPosition() {
        boolean result = iterator.setPosition(1);
        
        Assert.assertTrue(result);
        Assert.assertEquals(1, iterator.position);
    }
    
    @Test
    public void testSetPosition_LastPosition() {
        boolean result = iterator.setPosition(2);
        
        Assert.assertTrue(result);
        Assert.assertEquals(2, iterator.position);
    }
}