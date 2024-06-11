import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator attributeIterator;
    private NodePointer parent;
    
    @Before
    public void setup() {
        parent = new NodePointer();
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attributeName"));
    }
    
    @Test
    public void testGetNodePointer_FirstPosition() {
        assertThat(attributeIterator.getNodePointer().getPosition(), equalTo(1));
    }
    
    @Test
    public void testGetNodePointer_AttributeAtIndex1() {
        attributeIterator.setPosition(1);
        assertThat(attributeIterator.getNodePointer().getAttribute(), instanceOf(Attribute.class));
    }
    
    @Test
    public void testGetNodePointer_InvalidPosition() {
        attributeIterator.setPosition(-1);
        assertThat(attributeIterator.getNodePointer(), nullValue());
    }
    
    @Test
    public void testGetPosition() {
        assertThat(attributeIterator.getPosition(), equalTo(0));
    }
    
    @Test
    public void testSetPosition_ValidPosition() {
        assertThat(attributeIterator.setPosition(1), equalTo(true));
        assertThat(attributeIterator.getPosition(), equalTo(1));
    }
    
    @Test
    public void testSetPosition_InvalidPosition() {
        assertThat(attributeIterator.setPosition(0), equalTo(false));
        assertThat(attributeIterator.getPosition(), equalTo(0));
    }
}