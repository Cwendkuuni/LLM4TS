import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {
    
    private CoreOperationCompare operationCompare;

    @Before
    public void setup() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operationCompare = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        // Test when l and r are equal Objects
        Object l = 10;
        Object r = 10;
        assertTrue(operationCompare.equal(context, left, right));

        // Test when l and r are different Objects
        l = "abc";
        r = "def";
        assertFalse(operationCompare.equal(context, left, right));

        // Test when l and r are Collections
        l = new ArrayList<>(Arrays.asList(1, 2, 3));
        r = new ArrayList<>(Arrays.asList(2, 3, 4));
        assertFalse(operationCompare.equal(context, left, right));
        
        l = new ArrayList<>(Arrays.asList(1, 2, 3));
        r = new ArrayList<>(Arrays.asList(2, 3, 1));
        assertTrue(operationCompare.equal(context, left, right));
        
        // Test when l and r are Iterators
        l = Arrays.asList(1, 2, 3).iterator();
        r = Arrays.asList(2, 3, 4).iterator();
        assertFalse(operationCompare.equal(context, left, right));
        
        l = Arrays.asList(1, 2, 3).iterator();
        r = Arrays.asList(2, 3, 1).iterator();
        assertTrue(operationCompare.equal(context, left, right));
        
        // Test when l and r are Pointers
        l = new Pointer("test");
        r = new Pointer("test");
        assertTrue(operationCompare.equal(context, left, right));
        
        l = new Pointer("test1");
        r = new Pointer("test2");
        assertFalse(operationCompare.equal(context, left, right));

        // Test when l is a Pointer and r is not
        l = new Pointer("test");
        r = "test";
        assertTrue(operationCompare.equal(context, left, right));
        
        l = new Pointer("test1");
        r = "test2";
        assertFalse(operationCompare.equal(context, left, right));
        
        // Test when l is not a Pointer and r is a Pointer
        l = "test";
        r = new Pointer("test");
        assertTrue(operationCompare.equal(context, left, right));
        
        l = "test1";
        r = new Pointer("test2");
        assertFalse(operationCompare.equal(context, left, right));
        
        // Test when l and r are Booleans
        l = true;
        r = true;
        assertTrue(operationCompare.equal(context, left, right));
        
        l = true;
        r = false;
        assertFalse(operationCompare.equal(context, left, right));

        // Test when l and r are Numbers
        l = 10;
        r = 10;
        assertTrue(operationCompare.equal(context, left, right));
        
        l = 10.5;
        r = 10.5;
        assertTrue(operationCompare.equal(context, left, right));

        // Test when l and r are Strings
        l = "abc";
        r = "abc";
        assertTrue(operationCompare.equal(context, left, right));
        
        l = "abc";
        r = "def";
        assertFalse(operationCompare.equal(context, left, right));

        // Test when l is null and r is not null
        l = null;
        r = "abc";
        assertFalse(operationCompare.equal(context, left, right));
        
        // Test when l is not null and r is null
        l = "abc";
        r = null;
        assertFalse(operationCompare.equal(context, left, right));
        
        // Test when l and r are null
        l = null;
        r = null;
        assertTrue(operationCompare.equal(context, left, right));
    }
    
    @Test
    public void testContains() {
        Iterator it = Arrays.asList(1, 2, 3).iterator();
        
        // Test when iterator contains the value
        Object value = 2;
        assertTrue(operationCompare.contains(it, value));

        // Test when iterator does not contain the value
        value = 4;
        assertFalse(operationCompare.contains(it, value));
    }
    
    @Test
    public void testFindMatch() {
        Iterator lit = Arrays.asList(1, 2, 3).iterator();
        Iterator rit = Arrays.asList(3, 4, 5).iterator();
        
        // Test when there is a matching element 
        assertTrue(operationCompare.findMatch(lit, rit));

        lit = Arrays.asList(1, 2, 3).iterator();
        rit = Arrays.asList(4, 5, 6).iterator();
        
        // Test when there is no matching element
        assertFalse(operationCompare.findMatch(lit, rit));

    }
    
    @Test
    public void testEqualWithDifferentTypes() {
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        // Test when l and r are Booleans
        Object l = true;
        Object r = 1;
        assertFalse(operationCompare.equal(context, left, right));

        l = false;
        r = 0;
        assertFalse(operationCompare.equal(context, left, right));

        // Test when l and r are Numbers
        l = 10;
        r = "10";
        assertFalse(operationCompare.equal(context, left, right));
        
        l = 10.5;
        r = "10.5";
        assertFalse(operationCompare.equal(context, left, right));

        // Test when l and r are Strings
        l = "10";
        r = 10;
        assertFalse(operationCompare.equal(context, left, right));
        
        l = "10.5";
        r = 10.5;
        assertFalse(operationCompare.equal(context, left, right));
    }
    
    @Test
    public void testEqualWithNull() {
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        // Test when l is null and r is not null
        Object l = null;
        Object r = "abc";
        assertFalse(operationCompare.equal(context, left, right));
        
        // Test when l is not null and r is null
        l = "abc";
        r = null;
        assertFalse(operationCompare.equal(context, left, right));
        
        // Test when l and r are null
        l = null;
        r = null;
        assertTrue(operationCompare.equal(context, left, right));
    }
    
    private class Expression extends CoreOperation {
        public Expression() {
            super(new Expression[0]);
        }

        @Override
        public Object compute(EvalContext evalContext) {
            // TODO: Implement compute method
            return null;
        }
    }
}