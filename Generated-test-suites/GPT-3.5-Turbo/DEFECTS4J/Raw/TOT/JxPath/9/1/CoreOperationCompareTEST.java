import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import java.util.*;

public class CoreOperationCompareTest {

    // Test the equal method with two pointers as arguments
    @Test
    public void testEqualWithPointers() {
        Expression arg1 = new Expression() {
            public Object compute(EvalContext context) {
                return context.getSingleNodePointer();
            }
        };
        Expression arg2 = new Expression() {
            public Object compute(EvalContext context) {
                return context.getSingleNodePointer();
            }
        };

        CoreOperationCompare operator = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Pointer() {
            public Object getValue() {
                return "value";
            }
        });

        assert operator.equal(context, arg1, arg2);
    }

    // Test the equal method with two booleans as arguments
    @Test
    public void testEqualWithBooleans() {
        Expression arg1 = new Expression() {
            public Object compute(EvalContext context) {
                return true;
            }
        };
        Expression arg2 = new Expression() {
            public Object compute(EvalContext context) {
                return false;
            }
        };

        CoreOperationCompare operator = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();

        assert !operator.equal(context, arg1, arg2);
    }

    // Test the equal method with two numbers as arguments
    @Test
    public void testEqualWithNumbers() {
        Expression arg1 = new Expression() {
            public Object compute(EvalContext context) {
                return 10;
            }
        };
        Expression arg2 = new Expression() {
            public Object compute(EvalContext context) {
                return 10.0;
            }
        };

        CoreOperationCompare operator = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();

        assert operator.equal(context, arg1, arg2);
    }

    // Test the equal method with two strings as arguments
    @Test
    public void testEqualWithStrings() {
        Expression arg1 = new Expression() {
            public Object compute(EvalContext context) {
                return "abc";
            }
        };
        Expression arg2 = new Expression() {
            public Object compute(EvalContext context) {
                return "abc";
            }
        };

        CoreOperationCompare operator = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();

        assert operator.equal(context, arg1, arg2);
    }

    // Test the equal method with null arguments
    @Test
    public void testEqualWithNull() {
        Expression arg1 = new Expression() {
            public Object compute(EvalContext context) {
                return null;
            }
        };
        Expression arg2 = new Expression() {
            public Object compute(EvalContext context) {
                return null;
            }
        };

        CoreOperationCompare operator = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();

        assert operator.equal(context, arg1, arg2);
    }

    // Test the contains method with an iterator and an object
    @Test
    public void testContainsWithIteratorAndObject() {
        List<Object> list = new ArrayList<>();
        list.add("abc");
        list.add("def");

        Expression arg1 = new Expression() {
            public Object compute(EvalContext context) {
                return list.iterator();
            }
        };
        Expression arg2 = new Expression() {
            public Object compute(EvalContext context) {
                return "abc";
            }
        };

        CoreOperationCompare operator = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();

        assert operator.contains((Iterator) arg1.compute(context), arg2.compute(context));
    }

    // Test the findMatch method with two iterators
    @Test
    public void testFindMatchWithIterators() {
        List<Object> list1 = new ArrayList<>();
        list1.add("abc");
        list1.add("def");
        List<Object> list2 = new ArrayList<>();
        list2.add("xyz");
        list2.add("abc");

        Expression arg1 = new Expression() {
            public Object compute(EvalContext context) {
                return list1.iterator();
            }
        };
        Expression arg2 = new Expression() {
            public Object compute(EvalContext context) {
                return list2.iterator();
            }
        };

        CoreOperationCompare operator = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();

        assert operator.findMatch((Iterator) arg1.compute(context), (Iterator) arg2.compute(context));
    }

    // Test the equal method with an invalid object
    @Test
    public void testEqualWithInvalidObject() {
        Expression arg1 = new Expression() {
            public Object compute(EvalContext context) {
                return new Object();
            }
        };
        Expression arg2 = new Expression() {
            public Object compute(EvalContext context) {
                return new Object();
            }
        };

        CoreOperationCompare operator = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();

        assert !operator.equal(context, arg1, arg2);
    }

}