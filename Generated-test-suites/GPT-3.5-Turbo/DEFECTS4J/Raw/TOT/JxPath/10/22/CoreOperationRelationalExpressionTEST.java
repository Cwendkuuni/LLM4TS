import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression coreOperationRelationalExpression;

    @Before
    public void setUp() {
        coreOperationRelationalExpression = new TestCoreOperationRelationalExpression(new Expression[2]);
    }

    @Test
    public void testComputeValue_WhenLeftGreaterAndRightLesser_ShouldReturnTrue() {
        // Arrange
        EvalContext context = new EvalContext();

        // Act
        coreOperationRelationalExpression.args[0] = new TestExpression(5);
        coreOperationRelationalExpression.args[1] = new TestExpression(3);
        Object result = coreOperationRelationalExpression.computeValue(context);

        // Assert
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_WhenLeftLesserAndRightGreater_ShouldReturnFalse() {
        // Arrange
        EvalContext context = new EvalContext();

        // Act
        coreOperationRelationalExpression.args[0] = new TestExpression(3);
        coreOperationRelationalExpression.args[1] = new TestExpression(5);
        Object result = coreOperationRelationalExpression.computeValue(context);

        // Assert
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValue_WhenLeftEqualsRight_ShouldReturnFalse() {
        // Arrange
        EvalContext context = new EvalContext();

        // Act
        coreOperationRelationalExpression.args[0] = new TestExpression(5);
        coreOperationRelationalExpression.args[1] = new TestExpression(5);
        Object result = coreOperationRelationalExpression.computeValue(context);

        // Assert
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence_ShouldReturn3() {
        // Act
        int precedence = coreOperationRelationalExpression.getPrecedence();

        // Assert
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric_ShouldReturnFalse() {
        // Act
        boolean isSymmetric = coreOperationRelationalExpression.isSymmetric();

        // Assert
        assertFalse(isSymmetric);
    }
    
    @Test
    public void testReduce_WhenObjectIsSelfContext_ShouldReturnSingleNodePointer() {
        // Arrange
        EvalContext evalContext = new EvalContext();
        InitialContext initialContext = new InitialContext();
        initialContext.reset();
        
        // Act
        Object reducedObject = coreOperationRelationalExpression.reduce(initialContext);
        
        // Assert
        assertEquals(evalContext.getSingleNodePointer(), reducedObject);
    }
    
    @Test
    public void testReduce_WhenObjectIsCollection_ShouldReturnIterator() {
        // Arrange
        List<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        
        // Act
        Object reducedObject = coreOperationRelationalExpression.reduce(list);
        
        // Assert
        assertTrue(reducedObject instanceof Iterator);
    }

    @Test
    public void testContainsMatch_WhenIteratorContainsMatch_ShouldReturnTrue() {
        // Arrange
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("test1");
        hashSet.add("test2");
        
        // Act
        boolean containsMatch = coreOperationRelationalExpression.containsMatch(hashSet.iterator(), "test2");
        
        // Assert
        assertTrue(containsMatch);
    }
    
    @Test
    public void testContainsMatch_WhenIteratorDoesNotContainMatch_ShouldReturnFalse() {
        // Arrange
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("test1");
        hashSet.add("test2");
        
        // Act
        boolean containsMatch = coreOperationRelationalExpression.containsMatch(hashSet.iterator(), "test3");
        
        // Assert
        assertFalse(containsMatch);
    }
    
    @Test
    public void testFindMatch_WhenIteratorRightContainsMatch_ShouldReturnTrue() {
        // Arrange
        HashSet<String> hashSet1 = new HashSet<>();
        hashSet1.add("test1");
        hashSet1.add("test2");
        
        HashSet<String> hashSet2 = new HashSet<>();
        hashSet2.add("test2");
        
        // Act
        boolean findMatch = coreOperationRelationalExpression.findMatch(hashSet1.iterator(), hashSet2.iterator());
        
        // Assert
        assertTrue(findMatch);
    }
    
    @Test
    public void testFindMatch_WhenIteratorRightDoesNotContainMatch_ShouldReturnFalse() {
        // Arrange
        HashSet<String> hashSet1 = new HashSet<>();
        hashSet1.add("test1");
        hashSet1.add("test2");
        
        HashSet<String> hashSet2 = new HashSet<>();
        hashSet2.add("test3");
        
        // Act
        boolean findMatch = coreOperationRelationalExpression.findMatch(hashSet1.iterator(), hashSet2.iterator());
        
        // Assert
        assertFalse(findMatch);
    }
    
    private static class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {

        public TestCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return false;
        }
    }
    
    private static class TestExpression extends Expression {

        private Object value;

        public TestExpression(Object value) {
            super();
            this.value = value;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }

    }

}