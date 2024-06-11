import org.junit.Assert;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        // Arrange
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[] {context1, context2});
        
        // Act
        int documentOrder = unionContext.getDocumentOrder();
        
        // Assert
        Assert.assertEquals(1, documentOrder);
    }
    
    @Test
    public void testSetPosition_WhenPreparedIsFalse() {
        // Arrange
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[] {context1, context2});
        int position = 3;
        
        // Act
        boolean result = unionContext.setPosition(position);
        
        // Assert
        Assert.assertTrue(result);
    }
    
    @Test
    public void testSetPosition_WhenPreparedIsTrue() {
        // Arrange
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[] {context1, context2});
        unionContext.setPosition(3);
        int position = 5;
        
        // Act
        boolean result = unionContext.setPosition(position);
        
        // Assert
        Assert.assertTrue(result);
    }
    
    @Test
    public void testSetPosition_WhenContextsIsEmpty() {
        // Arrange
        UnionContext unionContext = new UnionContext(null, new EvalContext[] {});
        int position = 3;
        
        // Act
        boolean result = unionContext.setPosition(position);
        
        // Assert
        Assert.assertFalse(result);
    }
}