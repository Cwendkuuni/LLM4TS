package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {
    
    @Test
    public void testEqual() {
        Expression arg1 = new LiteralExpression("10");
        Expression arg2 = new LiteralExpression("10");
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new InitialContext();
        assertTrue(compare.equal(context, arg1, arg2));

        arg1 = new LiteralExpression("10");
        arg2 = new LiteralExpression("20");
        compare = new CoreOperationCompare(arg1, arg2);
        context = new InitialContext();
        assertTrue(!compare.equal(context, arg1, arg2));
    }

    @Test
    public void testContains() {
        Expression arg1 = new LiteralExpression(Collections.singletonList("10"));
        Expression arg2 = new LiteralExpression("10");
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new InitialContext();
        Iterator iterator = Collections.singletonList("10").iterator();
        assertTrue(compare.contains(iterator, arg2));

        arg1 = new LiteralExpression(Collections.singletonList("10"));
        arg2 = new LiteralExpression("20");
        compare = new CoreOperationCompare(arg1, arg2);
        context = new InitialContext();
        iterator = Collections.singletonList("10").iterator();
        assertTrue(!compare.contains(iterator, arg2));
    }

    @Test
    public void testFindMatch() {
        Expression arg1 = new LiteralExpression(Collections.singletonList("10"));
        Expression arg2 = new LiteralExpression(Collections.singletonList("20"));
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new InitialContext();
        Iterator iterator1 = Collections.singletonList("10").iterator();
        Iterator iterator2 = Collections.singletonList("20").iterator();
        assertTrue(!compare.findMatch(iterator1, iterator2));

        arg1 = new LiteralExpression(Arrays.asList("10", "20"));
        arg2 = new LiteralExpression(Arrays.asList("20", "30"));
        compare = new CoreOperationCompare(arg1, arg2);
        context = new InitialContext();
        iterator1 = Arrays.asList("10", "20").iterator();
        iterator2 = Arrays.asList("20", "30").iterator();
        assertTrue(compare.findMatch(iterator1, iterator2));
    }

    @Test
    public void testEqualValues() {
        CoreOperationCompare compare = new CoreOperationCompare();
        assertTrue(compare.equalValues("10", "10"));
        assertTrue(!compare.equalValues("10", "20"));
    }
}