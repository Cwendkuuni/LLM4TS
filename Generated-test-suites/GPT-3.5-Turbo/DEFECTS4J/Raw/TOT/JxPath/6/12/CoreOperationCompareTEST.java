import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setup() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void equal_BothValuesNull_ReturnsTrue() {
        // Arrange
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        // Act
        boolean result = coreOperationCompare.equal(context, left, right);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void equal_BothValuesPointers_ReturnsTrue() {
        // Arrange
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        left.setValue(new Pointer());
        right.setValue(new Pointer());
        
        // Act
        boolean result = coreOperationCompare.equal(context, left, right);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void equal_LeftValuePointer_ReturnsTrue() {
        // Arrange
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        left.setValue(new Pointer());
        
        // Act
        boolean result = coreOperationCompare.equal(context, left, right);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void equal_RightValuePointer_ReturnsTrue() {
        // Arrange
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        right.setValue(new Pointer());
        
        // Act
        boolean result = coreOperationCompare.equal(context, left, right);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void equal_LeftValueCollection_ReturnsTrue() {
        // Arrange
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        left.setValue(new HashSet());
        
        // Act
        boolean result = coreOperationCompare.equal(context, left, right);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void equal_RightValueCollection_ReturnsTrue() {
        // Arrange
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        right.setValue(new HashSet());
        
        // Act
        boolean result = coreOperationCompare.equal(context, left, right);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void equal_LeftValueIterator_ReturnsFalse() {
        // Arrange
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        left.setValue(new Iterator());
        
        // Act
        boolean result = coreOperationCompare.equal(context, left, right);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void equal_RightValueIterator_ReturnsFalse() {
        // Arrange
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        right.setValue(new Iterator());
        
        // Act
        boolean result = coreOperationCompare.equal(context, left, right);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void equal_BothValuesIterator_ReturnsFalse() {
        // Arrange
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        left.setValue(new Iterator());
        right.setValue(new Iterator());
        
        // Act
        boolean result = coreOperationCompare.equal(context, left, right);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void contains_ValueExistsInIterator_ReturnsTrue() {
        // Arrange
        Iterator it = new Iterator();
        it.add(1);
        it.add(2);
        it.add(3);
        Object value = 1;
        
        // Act
        boolean result = coreOperationCompare.contains(it, value);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void contains_ValueDoesNotExistInIterator_ReturnsFalse() {
        // Arrange
        Iterator it = new Iterator();
        it.add(1);
        it.add(2);
        it.add(3);
        Object value = 4;
        
        // Act
        boolean result = coreOperationCompare.contains(it, value);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void findMatch_MatchExistsInIterators_ReturnsTrue() {
        // Arrange
        Iterator lit = new Iterator();
        lit.add(1);
        lit.add(2);
        lit.add(3);
        Iterator rit = new Iterator();
        rit.add(3);
        rit.add(4);
        rit.add(5);
        
        // Act
        boolean result = coreOperationCompare.findMatch(lit, rit);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void findMatch_MatchDoesNotExistInIterators_ReturnsFalse() {
        // Arrange
        Iterator lit = new Iterator();
        lit.add(1);
        lit.add(2);
        lit.add(3);
        Iterator rit = new Iterator();
        rit.add(4);
        rit.add(5);
        rit.add(6);
        
        // Act
        boolean result = coreOperationCompare.findMatch(lit, rit);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void equal_BothValuesAreBoolean_ReturnsTrue() {
        // Arrange
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        left.setValue(true);
        right.setValue(false);
        
        // Act
        boolean result = coreOperationCompare.equal(context, left, right);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void equal_BothValuesAreNumber_ReturnsTrue() {
        // Arrange
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        left.setValue(10);
        right.setValue(10);
        
        // Act
        boolean result = coreOperationCompare.equal(context, left, right);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void equal_BothValuesAreString_ReturnsTrue() {
        // Arrange
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        left.setValue("test");
        right.setValue("test");
        
        // Act
        boolean result = coreOperationCompare.equal(context, left, right);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void equal_ValuesAreDifferent_ReturnsFalse() {
        // Arrange
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        left.setValue(10);
        right.setValue(20);
        
        // Act
        boolean result = coreOperationCompare.equal(context, left, right);
        
        // Assert
        assertFalse(result);
    }

}