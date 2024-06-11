import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Slt;
import org.apache.commons.jxpath.ri.compiler.SltEq;
import org.apache.commons.jxpath.ri.compiler.Sgt;
import org.apache.commons.jxpath.ri.compiler.SgtEq;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression coreOperationRelationalExpression;
    private Expression[] args;

    @Before
    public void setUp() {
        args = new Expression[2];
        args[0] = mock(Expression.class);
        args[1] = mock(Expression.class);
        coreOperationRelationalExpression = new Slt(args);
    }

    @Test
    public void testComputeValue() {
        NodePointer nodePointerMock = mock(NodePointer.class);

        when(args[0].compute(any())).thenReturn(10);
        when(args[1].compute(any())).thenReturn(5);

        assertFalse((Boolean) coreOperationRelationalExpression.computeValue(nodePointerMock));        
    }

    @Test
    public void testEvaluateCompare() {
        assertTrue(coreOperationRelationalExpression.evaluateCompare(-1));
        assertFalse(coreOperationRelationalExpression.evaluateCompare(0));
        assertFalse(coreOperationRelationalExpression.evaluateCompare(1));
    }

    @Test
    public void testReduce() {
        NodeIterator nodeIteratorMock = mock(NodeIterator.class);

        Object result = coreOperationRelationalExpression.reduce(nodeIteratorMock);
        assertEquals(nodeIteratorMock, result);

        Object result2 = coreOperationRelationalExpression.reduce(Arrays.asList(1, 2, 3));
        assertTrue(result2 instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        Iterator<Integer> iterator = list.iterator();

        assertTrue(coreOperationRelationalExpression.containsMatch(iterator, 3));
        assertFalse(coreOperationRelationalExpression.containsMatch(iterator, 10));
    }

    @Test
    public void testFindMatch() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list2.add(2);
        list2.add(3);

        Iterator<Integer> iterator1 = list1.iterator();
        Iterator<Integer> iterator2 = list2.iterator();

        assertTrue(coreOperationRelationalExpression.findMatch(iterator1, iterator2));
    }
}