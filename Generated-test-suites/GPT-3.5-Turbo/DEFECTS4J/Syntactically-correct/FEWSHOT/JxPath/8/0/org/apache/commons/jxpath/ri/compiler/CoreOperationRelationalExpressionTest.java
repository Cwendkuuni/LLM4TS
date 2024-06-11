import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    //test computeValue method
    @Test
    public void testComputeValue() {
        Expression[] args1 = new Expression[2];
        args1[0] = new Expression();
        args1[1] = new Expression();
        CoreOperationRelationalExpression core1 = new CoreOperationRelationalExpression(args1) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        EvalContext evalContext1 = new EvalContext();
        assertEquals(Boolean.FALSE, core1.computeValue(evalContext1));

        Expression[] args2 = new Expression[2];
        args2[0] = new Expression();
        args2[1] = new Expression();
        CoreOperationRelationalExpression core2 = new CoreOperationRelationalExpression(args2) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        EvalContext evalContext2 = new EvalContext();
        assertEquals(Boolean.TRUE, core2.computeValue(evalContext2));
    }
    
    //test getPrecedence method
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression core = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        assertEquals(3, core.getPrecedence());
    }
    
    //test isSymmetric method
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression core = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        assertEquals(false, core.isSymmetric());
    }
    
    //test reduce method when input is SelfContext
    @Test
    public void testReduceWithSelfContext() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression core = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        EvalContext evalContext = new EvalContext();
        SelfContext selfContext = new SelfContext(evalContext);
        Object result = core.reduce(selfContext);
        assertEquals(evalContext.getSingleNodePointer(), result);
    }
    
    //test reduce method when input is Collection
    @Test
    public void testReduceWithCollection() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression core = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Collection collection = new ArrayList();
        collection.add(1);
        collection.add(2);
        Object result = core.reduce(collection);
        Iterator iterator = collection.iterator();
        assertEquals(iterator, result);
    }
    
    //test reduce method when input is neither SelfContext nor Collection
    @Test
    public void testReduceWithOthers() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression core = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Object result = core.reduce(1);
        assertEquals(1, result);
    }
    
    //test containsMatch method when no match found
    @Test
    public void testContainsMatchNoMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression core = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Iterator iterator = new ArrayList().iterator();
        boolean result = core.containsMatch(iterator, 1);
        assertEquals(false, result);
    }
    
    //test containsMatch method when match found
    @Test
    public void testContainsMatchWithMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression core = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        List list = new ArrayList();
        list.add(1);
        Iterator iterator = list.iterator();
        boolean result = core.containsMatch(iterator, 1);
        assertEquals(true, result);
    }
    
    //test compute method when left and right are iterators
    @Test
    public void testComputeWithIterators() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression core = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        List left = new ArrayList();
        left.add(1);
        List right = new ArrayList();
        right.add(1);
        Iterator lit = left.iterator();
        Iterator rit = right.iterator();
        boolean result = core.compute(lit, rit);
        assertEquals(true, result);
    }
    
    //test compute method when left is iterator and right is not
    @Test
    public void testComputeWithIteratorAndNonIterator() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression core = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        List list = new ArrayList();
        list.add(1);
        Iterator iterator = list.iterator();
        boolean result = core.compute(iterator, 1);
        assertEquals(true, result);
    }
    
    //test compute method when left is not iterator and right is iterator
    @Test
    public void testComputeWithNonIteratorAndIterator() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression core = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        List list = new ArrayList();
        list.add(1);
        Iterator iterator = list.iterator();
        boolean result = core.compute(1, iterator);
        assertEquals(true, result);
    }
    
    //test compute method when left and right are not iterators
    @Test
    public void testComputeWithNonIterators() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression core = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        boolean result = core.compute(1, 1);
        assertEquals(true, result);
    }
    
    //test evaluateCompare method
    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression core = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        assertEquals(false, core.evaluateCompare(0));
        assertEquals(false, core.evaluateCompare(-1));
        assertEquals(false, core.evaluateCompare(1));
    }
    
    //test findMatch method when no match found
    @Test
    public void testFindMatchNoMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression core = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        List left = new ArrayList();
        left.add(1);
        Iterator lit = left.iterator();
        List right = new ArrayList();
        right.add(2);
        Iterator rit = right.iterator();
        boolean result = core.findMatch(lit, rit);
        assertEquals(false, result);
    }
    
    //test findMatch method when match found
    @Test
    public void testFindMatchWithMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression core = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        List left = new ArrayList();
        left.add(1);
        Iterator lit = left.iterator();
        List right = new ArrayList();
        right.add(1);
        Iterator rit = right.iterator();
        boolean result = core.findMatch(lit, rit);
        assertEquals(true, result);
    }
}