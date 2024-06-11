import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOpComp = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        boolean result = coreOpComp.equal(context, left, right);
        
        // Assert result based on specific cases
        // Add more assertions based on different scenarios
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOpComp = new CoreOperationCompare(arg1, arg2);
        Iterator it = new Iterator();
        Object value = new Object();
        
        boolean result = coreOpComp.contains(it, value);
        
        // Assert result based on specific cases
        // Add more assertions based on different scenarios
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOpComp = new CoreOperationCompare(arg1, arg2);
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        
        boolean result = coreOpComp.findMatch(lit, rit);
        
        // Assert result based on specific cases
        // Add more assertions based on different scenarios
    }

    @Test
    public void testEqualValues() {
        CoreOperationCompare coreOpComp = new CoreOperationCompare(arg1, arg2);
        Object l = new Object();
        Object r = new Object();
        
        boolean result = coreOpComp.equal(l, r);
        
        // Assert result based on specific cases
        // Add more assertions based on different scenarios
    }

    // Add more test cases for other methods in the CoreOperationCompare class

}