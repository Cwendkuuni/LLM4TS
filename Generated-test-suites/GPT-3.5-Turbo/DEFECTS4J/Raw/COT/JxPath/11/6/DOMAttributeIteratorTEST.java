// Additional test cases for edge cases and exceptions

import org.junit.Test;

public class DOMAttributeIteratorTest {

    // ...

    @Test
    public void testEmptyAttributes() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("attr3"));
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(1, iterator.getPosition());
        DOMAttributePointer nodePointer = (DOMAttributePointer) iterator.getNodePointer();
        Assert.assertEquals("attr3", nodePointer.getAttribute().getName());
        Assert.assertEquals("", nodePointer.getAttribute().getValue());
    }

    @Test
    public void testNonExistentAttributes() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("nonexistent"));
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(1, iterator.getPosition());
        DOMAttributePointer nodePointer = (DOMAttributePointer) iterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }

    @Test
    public void testNamespaceAttributes() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("xmlns"));
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(1, iterator.getPosition());
        DOMAttributePointer nodePointer = (DOMAttributePointer) iterator.getNodePointer();
        Assert.assertNull(nodePointer);

        iterator = new DOMAttributeIterator(parent, new QName("xml"));
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(1, iterator.getPosition());
        nodePointer = (DOMAttributePointer) iterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }

    @Test
    public void testInvalidAttributeName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("attr4:attr5"));
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(1, iterator.getPosition());
        DOMAttributePointer nodePointer = (DOMAttributePointer) iterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }
}