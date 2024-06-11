import org.apache.commons.jxpath.ri.CompilerContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IteratorSupport;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Value;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;
    private CompilerContext compilerContext;

    @Before
    public void setUp() {
        compilerContext = Mockito.spy(new CompilerContext() {
            @Override
            public IteratorSupport getIteratorSupport(Object expression, int axis, String[] steps) {
                return new IteratorSupport() {
                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public Object next() {
                        return null;
                    }
                };
            }
        });
        expression = Mockito.spy(new CoreOperationRelationalExpression(new CoreExpression[]{
                Mockito.mock(CoreExpression.class),
                Mockito.mock(CoreExpression.class)
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        });
    }

    @Test
    public void testComputeValue() {
        NodeInfo leftNode = Mockito.mock(NodeInfo.class);
        Value leftValue = Mockito.mock(Value.class);
        Mockito.when(leftNode.getValue()).thenReturn(leftValue);

        NodeInfo rightNode = Mockito.mock(NodeInfo.class);
        Value rightValue = Mockito.mock(Value.class);
        Mockito.when(rightNode.getValue()).thenReturn(rightValue);

        Mockito.when(compilerContext.getSingleNodePointer(ArgumentMatchers.any(NodeInfo.class))).thenReturn(leftNode);
        Mockito.when(compilerContext.getSingleNodePointer(ArgumentMatchers.any(NodeInfo.class))).thenReturn(rightNode);

        EvalContext context = Mockito.spy(new EvalContext(compilerContext, new InitialContext(), null));

        Mockito.when(leftNode.getValue()).thenReturn(leftValue);
        Mockito.when(rightNode.getValue()).thenReturn(rightValue);

        Object result = expression.computeValue(context);

        Mockito.verify(context).push(ArgumentMatchers.eq(Boolean.TRUE));
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithIterators() {
        List<Object> leftValues = Arrays.asList(1.0, 2.0, 3.0);
        List<Object> rightValues = Arrays.asList(2.0, 3.0, 4.0);

        Iterator<Object> leftIterator = leftValues.iterator();
        Iterator<Object> rightIterator = rightValues.iterator();

        Mockito.when(compilerContext.iteratorSupport(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.anyStringArray()))
                .thenReturn(new IteratorSupport() {
                    @Override
                    public boolean hasNext() {
                        return leftIterator.hasNext() && rightIterator.hasNext();
                    }

                    @Override
                    public Object next() {
                        Object left = leftIterator.next();
                        Object right = rightIterator.next();
                        Mockito.verify(compilerContext).iteratorSupport(left, ArgumentMatchers.anyStringArray());
                        Mockito.verify(compilerContext).iteratorSupport(right, ArgumentMatchers.anyStringArray());
                        return left;
                    }
                });

        EvalContext context = Mockito.spy(new EvalContext(compilerContext, new InitialContext(), null));
        Object result = expression.computeValue(context);

        Mockito.verify(context, Mockito.times(leftValues.size())).push(ArgumentMatchers.eq(Boolean.TRUE));
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithCollections() {
        List<Object> leftValues = Collections.singletonList(1.0);
        List<Object> rightValues = Collections.singletonList(1.0);

        Mockito.when(compilerContext.iteratorSupport(ArgumentMatchers.any(), ArgumentMatchers.anyStringArray()))
                .thenReturn(Mockito.mock(IteratorSupport.class));

        EvalContext context = Mockito.spy(new EvalContext(compilerContext, new InitialContext(), null));
        Object result = expression.computeValue(context);

        Mockito.verify(context).push(ArgumentMatchers.eq(Boolean.TRUE));
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithDifferentTypes() {
        NodeInfo leftNode = Mockito.mock(NodeInfo.class);
        Value leftValue = Mockito.mock(Value.class);
        Mockito.when(leftNode.getValue()).thenReturn(leftValue);
        Mockito.when(leftValue.getValueTypeName()).thenReturn("string");

        NodeInfo rightNode = Mockito.mock(NodeInfo.class);
        Value rightValue = Mockito.mock(Value.class);
        Mockito.when(rightNode.getValue()).thenReturn(rightValue);
        Mockito.when(rightValue.getValueTypeName()).thenReturn("number");

        Mockito.when(compilerContext.getSingleNodePointer(ArgumentMatchers.any(NodeInfo.class))).thenReturn(leftNode);
        Mockito.when(compilerContext.getSingleNodePointer(ArgumentMatchers.any(NodeInfo.class))).thenReturn(rightNode);

        EvalContext context = Mockito.spy(new EvalContext(compilerContext, new InitialContext(), null));

        Mockito.when(leftNode.getValue()).thenReturn(leftValue);
        Mockito.when(rightNode.getValue()).thenReturn(rightValue);

        try {
            expression.computeValue(context);
            Mockito.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testComputeValueWithDifferentTypesAndIterators() {
        List<Object> leftValues = Arrays.asList("1", "2", "3");
        List<Object> rightValues = Arrays.asList(1.0, 2.0, 3.0);

        Iterator<Object> leftIterator = leftValues.iterator();
        Iterator<Object> rightIterator = rightValues.iterator();

        Mockito.when(compilerContext.iteratorSupport(ArgumentMatchers.any(), ArgumentMatchers.anyStringArray()))
                .thenReturn(new IteratorSupport() {
                    @Override
                    public boolean hasNext() {
                        return leftIterator.hasNext() && rightIterator.hasNext();
                    }

                    @Override
                    public Object next() {
                        Object left = leftIterator.next();
                        Object right = rightIterator.next();
                        Mockito.verify(compilerContext).iteratorSupport(left, ArgumentMatchers.anyStringArray());
                        Mockito.verify(compilerContext).iteratorSupport(right, ArgumentMatchers.anyStringArray());
                        return left;
                    }
                });

        EvalContext context = Mockito.spy(new EvalContext(compilerContext, new InitialContext(), null));

        try {
            expression.computeValue(context);
            Mockito.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}