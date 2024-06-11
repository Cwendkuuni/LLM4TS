import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

    @Test
    public void testConstructor() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        assertSame(parentContext, attributeContext.getParentContext());
        assertSame(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.reset();

        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
        assertEquals(0, attributeContext.getPosition());
    }

    @Test
    public void testSetPositionBelowCurrent() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        
        attributeContext.setPosition(2);
        attributeContext.setPosition(1);

        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testSetPositionAboveCurrent() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.setPosition(1);
        attributeContext.setPosition(2);

        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWithValidIterator() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodeIterator iterator = new NodeIterator();
        iterator.setPosition(1);
        attributeContext.setIterator(iterator);

        assertTrue(attributeContext.nextNode());
        assertSame(iterator.getNodePointer(), attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithNullIterator() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithInvalidIterator() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodeIterator iterator = new NodeIterator();
        iterator.setPosition(1);
        iterator.setEndPosition(1);
        attributeContext.setIterator(iterator);

        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

}