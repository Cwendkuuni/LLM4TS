import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test when both values are equal
        assertTrue(operation.equal(context, arg1, arg2));
        
        // Test when values are different
        assertFalse(operation.equal(context, arg1, new Expression()));
        
        // Test when one value is null
        assertFalse(operation.equal(context, arg1, null));
    }

    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare();
        Iterator it = new Iterator();
        
        // Test when iterator contains the value
        assertTrue(operation.contains(it, "value"));
        
        // Test when iterator does not contain the value
        assertFalse(operation.contains(it, "different"));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare();
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        
        // Test when there is a match between two iterators
        assertTrue(operation.findMatch(lit, rit));
        
        // Test when there is no match between two iterators
        assertFalse(operation.findMatch(lit, new Iterator()));
    }

    @Test
    public void testEqualValues() {
        CoreOperationCompare operation = new CoreOperationCompare();
        
        // Test when values are equal
        assertTrue(operation.equal("value", "value"));
        
        // Test when values are not equal
        assertFalse(operation.equal("value", "different"));
    }

}