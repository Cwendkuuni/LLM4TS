import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare operation = new CoreOperationCompare(expression1, expression2);
        
        EvalContext context = createEvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        boolean result = operation.equal(context, left, right);

        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare(expression1, expression2);
        
        Iterator it = createIterator();
        Object value = createValue();
        boolean result = operation.contains(it, value);

        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare(expression1, expression2);
        
        Iterator lit = createIterator();
        Iterator rit = createIterator();
        boolean result = operation.findMatch(lit, rit);

        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testEqualObjects() {
        CoreOperationCompare operation = new CoreOperationCompare(expression1, expression2);
        
        Object l = createObject();
        Object r = createObject();
        boolean result = operation.equal(l, r);

        assertEquals(expectedResult, result);
    }
}