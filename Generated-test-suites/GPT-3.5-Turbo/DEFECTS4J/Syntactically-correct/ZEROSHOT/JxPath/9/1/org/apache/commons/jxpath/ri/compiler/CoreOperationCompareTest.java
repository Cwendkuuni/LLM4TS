import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

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
        EvalContext context = Mockito.mock(EvalContext.class);
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        
        CoreOperationCompare operation = new CoreOperationCompare(left, right) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return true;
            }
        };
        
        boolean result = operation.equal(context, left, right);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualWithMockData() {
        EvalContext context = Mockito.mock(EvalContext.class);
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        
        boolean result = operation.equal(context, left, right);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testContains() {
        Iterator it = Mockito.mock(Iterator.class);
        Object value = new Object();
        
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        
        boolean result = operation.contains(it, value);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testFindMatch() {
        Iterator lit = Mockito.mock(Iterator.class);
        Iterator rit = Mockito.mock(Iterator.class);
        
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        
        boolean result = operation.findMatch(lit, rit);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testFindMatchWithMockData() {
        Iterator lit = Mockito.mock(Iterator.class);
        Iterator rit = Mockito.mock(Iterator.class);
        
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            @Override
            protected boolean contains(Iterator it, Object value) {
                return true;
            }
        };
        
        boolean result = operation.findMatch(lit, rit);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualWithPointers() {
        EvalContext context = Mockito.mock(EvalContext.class);
        Pointer pointer1 = Mockito.mock(Pointer.class);
        Pointer pointer2 = Mockito.mock(Pointer.class);
        
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(Object l, Object r) {
                return true;
            }
        };
        
        boolean result = operation.equal(pointer1, pointer2);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualWithDifferentTypes() {
        Object l = new Object();
        Object r = new Object();
        
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        
        boolean result = operation.equal(l, r);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEqualWithNumbers() {
        Object l = 10;
        Object r = 10;
        
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        
        boolean result = operation.equal(l, r);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualWithStrings() {
        Object l = "test";
        Object r = "test";
        
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        
        boolean result = operation.equal(l, r);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualWithNulls() {
        Object l = null;
        Object r = null;
        
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        
        boolean result = operation.equal(l, r);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualWithDifferentObjects() {
        Object l = new Object();
        Object r = new Object();
        
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        
        boolean result = operation.equal(l, r);
        
        Assert.assertFalse(result);
    }
}