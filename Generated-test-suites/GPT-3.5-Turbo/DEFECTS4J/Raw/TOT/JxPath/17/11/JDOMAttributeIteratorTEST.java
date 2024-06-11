import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.hamcrest.CoreMatchers;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class JDOMAttributeIteratorTest {

    private Element element;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");

        attributeIterator = new JDOMAttributeIterator(element, null);
    }

    @Test
    public void testGetNodePointer_hasNext() {
        Assert.assertTrue(attributeIterator.setPosition(1)); // position the iterator at the first attribute
        Assert.assertThat(attributeIterator.getNodePointer(), CoreMatchers.notNullValue());
    }

    @Test
    public void testGetNodePointer_noNext() {
        Assert.assertFalse(attributeIterator.setPosition(0)); // set an invalid position
        Assert.assertThat(attributeIterator.getNodePointer(), CoreMatchers.nullValue());
    }

    @Test
    public void testGetPosition_validPosition() {
        attributeIterator.setPosition(2); // set a valid position
        Assert.assertEquals(2, attributeIterator.getPosition());
    }

    @Test
    public void testGetPosition_invalidPosition() {
        attributeIterator.setPosition(0); // set an invalid position
        Assert.assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_validPosition() {
        Assert.assertTrue(attributeIterator.setPosition(3)); // set a valid position
        Assert.assertEquals(3, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_invalidPosition() {
        Assert.assertFalse(attributeIterator.setPosition(4)); // set an invalid position
        Assert.assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_lowerBound() {
        attributeIterator.setPosition(1); // set lower bound position
        Assert.assertEquals(1, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_upperBound() {
        attributeIterator.setPosition(3); // set upper bound position
        Assert.assertEquals(3, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_outOfBound() {
        Assert.assertFalse(attributeIterator.setPosition(5)); // set position out of bound
        Assert.assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testGetNodePointer_validPosition() {
        attributeIterator.setPosition(1); // set a valid position
        Assert.assertThat(attributeIterator.getNodePointer(), CoreMatchers.notNullValue());
    }

    @Test
    public void testGetNodePointer_invalidPosition() {
        attributeIterator.setPosition(0); // set an invalid position
        Assert.assertThat(attributeIterator.getNodePointer(), CoreMatchers.nullValue());
    }

    @Test
    public void testGetNodePointer_lowerBound() {
        attributeIterator.setPosition(1); // set lower bound position
        Assert.assertThat(attributeIterator.getNodePointer(), CoreMatchers.notNullValue());
    }

    @Test
    public void testGetNodePointer_upperBound() {
        attributeIterator.setPosition(3); // set upper bound position
        Assert.assertThat(attributeIterator.getNodePointer(), CoreMatchers.notNullValue());
    }

    @Test
    public void testGetNodePointer_outOfBound() {
        attributeIterator.setPosition(5); // set position out of bound
        Assert.assertThat(attributeIterator.getNodePointer(), CoreMatchers.nullValue());
    }
}