import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression operation;
    private Expression[] args;

    @Before
    public void setUp() {
        args = new Expression[2];
        operation = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
    }

    @Test
    public void testComputeValue_withGreaterValue_returnsTrue() {
        EvalContext context = createTestEvalContext(5, 3);
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_withLesserValue_returnsFalse() {
        EvalContext context = createTestEvalContext(3, 5);
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_withEqualValue_returnsFalse() {
        EvalContext context = createTestEvalContext(5, 5);
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_withInitialContext_returnsFalse() {
        EvalContext context = createTestEvalContext(new InitialContext());
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_withIterator_returnsFalse() {
        EvalContext context = createTestEvalContext(new ArrayList<Integer>().iterator());
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_withCollections_returnsFalse() {
        EvalContext context = createTestEvalContext(new ArrayList<Integer>(), new ArrayList<Integer>());
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    private EvalContext createTestEvalContext(Object value1, Object value2) {
        return new EvalContext() {
            @Override
            public Object getSingleNodePointer() {
                return value1;
            }

            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public Iterator iteratePointers() {
                return null;
            }

            @Override
            public Collection getValueCollection() {
                return null;
            }
        };
    }

    private EvalContext createTestEvalContext(InitialContext initialContext) {
        return new EvalContext() {
            @Override
            public Object getSingleNodePointer() {
                return initialContext;
            }

            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public Iterator iteratePointers() {
                return null;
            }

            @Override
            public Collection getValueCollection() {
                return null;
            }
        };
    }

    private EvalContext createTestEvalContext(Iterator iterator) {
        return new EvalContext() {
            @Override
            public Object getSingleNodePointer() {
                return iterator;
            }

            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public Iterator iteratePointers() {
                return null;
            }

            @Override
            public Collection getValueCollection() {
                return null;
            }
        };
    }

    private EvalContext createTestEvalContext(Collection collection1, Collection collection2) {
        return new EvalContext() {
            @Override
            public Object getSingleNodePointer() {
                return collection1;
            }

            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public Iterator iteratePointers() {
                return null;
            }

            @Override
            public Collection getValueCollection() {
                return collection2;
            }
        };
    }
}