package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {
    @Test
    public void testEqual() {
        Expression arg1 = new TestExpression();
        Expression arg2 = new TestExpression();
        
        CoreOperationCompare op = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test when values are equal
        context.addValue("arg1", "abc");
        context.addValue("arg2", "abc");
        assertTrue(op.equal(context, arg1, arg2));

        // Test when values are not equal
        context.addValue("arg2", "def");
        assertFalse(op.equal(context, arg1, arg2));

        // Test when values are different types
        context.addValue("arg2", 123);
        assertFalse(op.equal(context, arg1, arg2));
    }

    @Test
    public void testContains() {
        CoreOperationCompare op = new CoreOperationCompare(null, null);
        Iterator<String> it = Arrays.asList("abc", "def", "ghi").iterator();
        
        // Test when value is contained in the iterator
        assertTrue(op.contains(it, "abc"));

        // Test when value is not contained in the iterator
        assertFalse(op.contains(it, "jkl"));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare op = new CoreOperationCompare(null, null);
        Iterator<String> lit = Arrays.asList("abc", "def", "ghi").iterator();
        Iterator<String> rit = Arrays.asList("xyz", "def", "uvw").iterator();
        
        // Test when a matching value is found
        assertTrue(op.findMatch(lit, rit));

        // Test when no matching value is found
        assertFalse(op.findMatch(lit, Arrays.asList("xyz", "uvw").iterator()));
    }

    @Test
    public void testEqualWithPointers() {
        CoreOperationCompare op = new CoreOperationCompare(null, null);
        
        // Test when pointers are equal
        assertTrue(op.equal(new Pointer("abc"), new Pointer("abc")));
        
        // Test when pointers are not equal
        assertFalse(op.equal(new Pointer("abc"), new Pointer("def")));
    }
    
    class TestExpression extends Expression {
        @Override
        public Object compute(EvalContext context) {
            return context.getValue("arg1");
        }
    }
}