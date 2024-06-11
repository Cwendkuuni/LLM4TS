
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.Pointer;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
        };

        assertEquals(true, expression.isContextDependent());
    }

    @Test
    public void testComputeContextDependent() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
        };

        assertEquals(false, expression.computeContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }

            public Object computeValue(EvalContext context) {
                return "test value";
            }
        };

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        assertEquals("test value", result);
    }

    @Test
    public void testCompute() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }

            public Object compute(EvalContext context) {
                return "test compute";
            }
        };

        EvalContext context = new EvalContext();
        Object result = expression.compute(context);

        assertEquals("test compute", result);
    }

    @Test
    public void testIterate() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }

            public Object compute(EvalContext context) {
                List<String> values = Arrays.asList("value1", "value2", "value3");
                return values;
            }
        };

        EvalContext context = new EvalContext();
        Iterator<String> iterator = expression.iterate(context);

        List<String> expected = Arrays.asList("value1", "value2", "value3");
        List<String> actual = new ArrayList<>();

        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }

            public Object compute(EvalContext context) {
                List<String> values = Arrays.asList("value1", "value2", "value3");
                List<Pointer> pointers = new ArrayList<>();

                for (String value : values) {
                    pointers.add(new Pointer(value));
                }

                return pointers;
            }
        };

        EvalContext context = new EvalContext();
        Iterator<Pointer> iterator = expression.iteratePointers(context);

        List<String> expected = Arrays.asList("value1", "value2", "value3");
        List<String> actual = new ArrayList<>();

        while (iterator.hasNext()) {
            actual.add((String) iterator.next().getValue());
        }

        assertEquals(expected, actual);
    }
}
