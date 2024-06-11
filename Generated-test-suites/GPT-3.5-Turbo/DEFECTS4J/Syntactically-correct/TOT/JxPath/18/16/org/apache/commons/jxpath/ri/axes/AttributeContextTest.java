import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {
    
    private AttributeContext attributeContext;
    private NodeTest nodeTest;
    private EvalContext parentContext;
    
    @Before
    public void setUp() {
        parentContext = new EvalContext();
        nodeTest = new NodeNameTest("attribute");
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }
    
    @Test
    public void testGetCurrentNodePointer() {
        Assert.assertNull(attributeContext.getCurrentNodePointer());
        
        NodeIterator iterator = new NodeIterator();
        NodePointer nodePointer = new NodePointer();
        iterator.addNodePointer(nodePointer);
        attributeContext.iterator = iterator;
        
        Assert.assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }
    
    @Test
    public void testReset() {
        attributeContext.currentNodePointer = new NodePointer();
        attributeContext.setStarted = true;
        attributeContext.iterator = new NodeIterator();
        
        attributeContext.reset();
        
        Assert.assertFalse(attributeContext.setStarted);
        Assert.assertNull(attributeContext.iterator);
        Assert.assertEquals(0, attributeContext.getCurrentPosition());
    }
    
    @Test
    public void testSetPosition() {
        attributeContext.iterator = new NodeIterator();
        attributeContext.setCurrentPosition(1);
        
        Assert.assertTrue(attributeContext.setPosition(1));
        Assert.assertFalse(attributeContext.iterator.isReset());
        Assert.assertTrue(attributeContext.setPosition(2));
        Assert.assertFalse(attributeContext.iterator.isReset());
        Assert.assertFalse(attributeContext.setPosition(0));
        Assert.assertTrue(attributeContext.iterator.isReset());
    }
    
    @Test
    public void testNextNodeWithNodeNameTest() {
        NodePointer nodePointer = new NodePointer();
        NodeIterator iterator = new NodeIterator();
        iterator.addNodePointer(nodePointer);
        attributeContext.iterator = iterator;
        attributeContext.setStarted = true;
        
        Assert.assertTrue(attributeContext.nextNode());
        Assert.assertEquals(1, attributeContext.getCurrentPosition());
        Assert.assertEquals(nodePointer, attributeContext.currentNodePointer);
        
        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertEquals(1, attributeContext.getCurrentPosition());
        Assert.assertNull(attributeContext.currentNodePointer);
    }
    
    @Test
    public void testNextNodeWithoutNodeNameTest() {
        attributeContext.setStarted = true;
        
        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertEquals(1, attributeContext.getCurrentPosition());
        Assert.assertNull(attributeContext.currentNodePointer);
    }
}