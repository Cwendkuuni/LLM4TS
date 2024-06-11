import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Typical Use Case
        EvalContext context = new EvalContext();
        context.setSingleNodePointer("6"); // Object left = 6
        context.setSingleNodePointer("4"); // Object right = 4
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression();
        Object result = expression.computeValue(context);
        assertFalse((boolean) result);
        
        context.reset();
        context.setSingleNodePointer("2"); // Object left = 2
        context.setSingleNodePointer("4"); // Object right = 4
        
        result = expression.computeValue(context);
        assertTrue((boolean) result);
    }

    @Test
    public void testReduce() {
        // Typical Use Case
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression();
        Object o = new SelfContext();
        Object result = expression.reduce(o);
        assertFalse(result instanceof SelfContext);
        
        o = new InitialContext();
        result = expression.reduce(o);
        assertFalse(result instanceof InitialContext);
        
        HashSet<Object> collection = new HashSet<Object>();
        collection.add(1);
        collection.add(2);
        collection.add(3);
        o = collection;
        result = expression.reduce(o);
        assertTrue(result instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        // Typical Use Case
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression();
        Iterator<Object> iterator = Arrays.asList(1, 2, 3).iterator();
        Object value = 2;
        boolean result = expression.containsMatch(iterator, value);
        assertTrue(result);
        
        iterator = Arrays.asList(1, 2, 3).iterator();
        value = 4;
        result = expression.containsMatch(iterator, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        // Typical Use Case
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression();
        Iterator<Object> lit = Arrays.asList(1, 2, 3).iterator();
        Iterator<Object> rit = Arrays.asList(2, 3, 4).iterator();
        boolean result = expression.findMatch(lit, rit);
        assertTrue(result);
        
        lit = Arrays.asList(1, 2, 3).iterator();
        rit = Arrays.asList(4, 5, 6).iterator();
        result = expression.findMatch(lit, rit);
        assertFalse(result);
    }

}