import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {
  
  private AttributeContext attributeContext;
  
  @Before
  public void setup() {
    attributeContext = new AttributeContext(null, new NodeNameTest("attributeName"));
  }
  
  @Test
  public void getCurrentNodePointer_WhenCalled_ReturnsCorrectNodePointer() {
    NodePointer expected = attributeContext.getCurrentNodePointer();
    
    attributeContext.nextNode();
    NodePointer actual = attributeContext.getCurrentNodePointer();
    
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void reset_WhenCalled_ResetsAttributes() {
    attributeContext.nextNode();
    attributeContext.reset();
    
    boolean actual = attributeContext.nextNode();
    
    Assert.assertFalse(actual);
  }
  
  @Test
  public void setPosition_PositionLessThanCurrentPosition_ResetsAttributesAndReturnsFalse() {
    attributeContext.nextNode();
    attributeContext.nextNode();
    
    boolean actual = attributeContext.setPosition(1);
    
    Assert.assertFalse(actual);
    Assert.assertNull(attributeContext.getCurrentNodePointer());
  }
  
  @Test
  public void setPosition_PositionMoreThanCurrentPosition_IncrementsPositionAndReturnsTrue() {
    attributeContext.nextNode();
    int currentPosition = attributeContext.getCurrentPosition();
    
    boolean actual = attributeContext.setPosition(currentPosition + 1);
    
    Assert.assertTrue(actual);
    Assert.assertEquals(currentPosition + 1, attributeContext.getCurrentPosition());
  }
  
  @Test
  public void nextNode_SetStartedIsFalse_SetsSetStartedToTrueAndReturnsFalse() {
    attributeContext.reset();
    
    boolean actual = attributeContext.nextNode();
    
    Assert.assertTrue(attributeContext.setStarted);
    Assert.assertFalse(actual);
    Assert.assertNull(attributeContext.getCurrentNodePointer());
    Assert.assertNull(attributeContext.iterator);
  }
  
  @Test
  public void nextNode_NodeTestNotInstanceOfNodeNameTest_ReturnsFalse() {
    attributeContext = new AttributeContext(null, new NodeTest() {});
    
    boolean actual = attributeContext.nextNode();
    
    Assert.assertFalse(actual);
    Assert.assertFalse(attributeContext.setStarted);
    Assert.assertNull(attributeContext.getCurrentNodePointer());
    Assert.assertNull(attributeContext.iterator);
  }
  
  @Test
  public void nextNode_IteratorIsNull_ReturnsFalse() {
    NodePointer parentPointer = new NodePointer();
    attributeContext = new AttributeContext(parentPointer, new NodeNameTest("attributeName"));
    attributeContext.iterator = null;
    
    boolean actual = attributeContext.nextNode();
    
    Assert.assertFalse(actual);
    Assert.assertTrue(attributeContext.setStarted);
    Assert.assertNull(attributeContext.getCurrentNodePointer());
  }
  
  @Test
  public void nextNode_Iterator_PositionIncrementFails_ReturnsFalse() {
    NodeIterator iterator = new NodeIterator();
    iterator.setPosition(1);
    attributeContext.iterator = iterator;
    
    boolean actual = attributeContext.nextNode();
    
    Assert.assertFalse(actual);
    Assert.assertTrue(attributeContext.setStarted);
    Assert.assertNull(attributeContext.getCurrentNodePointer());
  }
}