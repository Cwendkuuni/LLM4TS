import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.jxpath.ri.compiler.CoreOperationCompare.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqualMethod() {
        Expression leftExp = new LiteralExpression(new Integer(5));
        Expression rightExp = new LiteralExpression(new Integer(5));

        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExp, rightExp);

        assertTrue(coreOperationCompare.equal(context, leftExp, rightExp));
    }

    @Test
    public void testEqualMethodWithNulls() {
        Expression leftExp = new LiteralExpression(null);
        Expression rightExp = new LiteralExpression(null);

        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExp, rightExp);

        assertTrue(coreOperationCompare.equal(context, leftExp, rightExp));
    }

    @Test
    public void testEqualMethodWithDifferentTypes() {
        Expression leftExp = new LiteralExpression("string");
        Expression rightExp = new LiteralExpression(new Integer(5));

        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExp, rightExp);

        assertFalse(coreOperationCompare.equal(context, leftExp, rightExp));
    }

    @Test
    public void testEqualMethodWithIterator() {
        List list1 = new ArrayList();
        list1.add(new Integer(1));
        list1.add(new Integer(2));

        Iterator it = list1.iterator();
        Expression leftExp = new VariableExpression("list1", new EvalContext(new InitialContext(new Object[]{list1})));
        Expression rightExp = it;

        EvalContext context = new EvalContext(new InitialContext(new Object[]{list1}));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExp, rightExp);

        assertTrue(coreOperationCompare.equal(context, leftExp, rightExp));
    }

    @Test
    public void testEqualMethodWithTwoIterators() {
        List list1 = new ArrayList();
        list1.add(new Integer(1));
        list1.add(new Integer(2));

        List list2 = new ArrayList();
        list2.add(new Integer(1));
        list2.add(new Integer(3));

        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();

        Expression leftExp = new VariableExpression("list1", new EvalContext(new InitialContext(new Object[]{list1})));
        Expression rightExp = new VariableExpression("list2", new EvalContext(new InitialContext(new Object[]{list2})));

        EvalContext context = new EvalContext(new InitialContext(new Object[]{list1, list2}));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExp, rightExp);

        assertFalse(coreOperationCompare.equal(context, leftExp, rightExp));
    }

    @Test(expected = AssertionError.class)
    public void testEqualMethodWithEmptyCollectionAndIterator() {
        List list = new ArrayList();
        Iterator it = list.iterator();

        Expression leftExp = new VariableExpression("list", new EvalContext(new InitialContext(new Object[]{list})));
        Expression rightExp = it;

        EvalContext context = new EvalContext(new InitialContext(new Object[]{list}));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExp, rightExp);

        coreOperationCompare.equal(context, leftExp, rightExp);
    }

    @Test
    public void testContainsMethod() {
        List list = new ArrayList();
        list.add(new Integer(1));
        list.add(new Integer(2));

        Iterator it = list.iterator();

        Expression leftExp = new VariableExpression("list", new EvalContext(new InitialContext(new Object[]{list})));
        Expression rightExp = new LiteralExpression(new Integer(1));

        EvalContext context = new EvalContext(new InitialContext(new Object[]{list}));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExp, rightExp);

        assertTrue(coreOperationCompare.contains(it, rightExp));
    }

    @Test
    public void testFindMatchMethod() {
        List list1 = new ArrayList();
        list1.add(new Integer(1));
        list1.add(new Integer(2));

        List list2 = new ArrayList();
        list2.add(new Integer(1));
        list2.add(new Integer(2));
        list2.add(new Integer(3));

        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();

        Expression leftExp = new VariableExpression("list1", new EvalContext(new InitialContext(new Object[]{list1})));
        Expression rightExp = it2;

        EvalContext context = new EvalContext(new InitialContext(new Object[]{list1, list2}));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExp, rightExp);

        assertTrue(coreOperationCompare.findMatch(it1, it2));
    }

    @Test
    public void testFindMatchMethodWithEmptyLists() {
        List list1 = new ArrayList();
        List list2 = new ArrayList();

        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();

        Expression leftExp = new VariableExpression("list1", new EvalContext(new InitialContext(new Object[]{list1})));
        Expression rightExp = it2;

        EvalContext context = new EvalContext(new InitialContext(new Object[]{list1, list2}));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExp, rightExp);

        assertFalse(coreOperationCompare.findMatch(it1, it2));
    }

    @Test
    public void testFindMatchMethodWithIteratorsOfEmptyLists() {
        List list1 = new ArrayList();
        List list2 = new ArrayList();

        Iterator it1 = Collections.emptyIterator();
        Iterator it2 = Collections.emptyIterator();

        Expression leftExp = new VariableExpression("list1", new EvalContext(new InitialContext(new Object[]{list1})));
        Expression rightExp = it2;

        EvalContext context = new EvalContext(new InitialContext(new Object[]{list1, list2}));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExp, rightExp);

        assertFalse(coreOperationCompare.findMatch(it1, it2));
    }

}