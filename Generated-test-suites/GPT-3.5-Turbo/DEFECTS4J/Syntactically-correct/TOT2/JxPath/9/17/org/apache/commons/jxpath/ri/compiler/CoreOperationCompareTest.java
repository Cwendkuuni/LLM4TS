import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Pointer(null));

        // Testing for equality of two equal values
        assertTrue(coreOperationCompare.equal(context, new CustomExpression("value"), new CustomExpression("value")));
        
        // Testing for equality of two different values
        assertFalse(coreOperationCompare.equal(context, new CustomExpression("value1"), new CustomExpression("value2")));

        // Testing for collection equality
        ArrayList<String> list1 = new ArrayList<>(Arrays.asList("item1", "item2", "item3"));
        ArrayList<String> list2 = new ArrayList<>(Arrays.asList("item1", "item2", "item3"));
        assertTrue(coreOperationCompare.equal(context, new CustomExpression(list1), new CustomExpression(list2)));
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        Iterator it = Arrays.asList("item1", "item2", "item3").iterator();

        // Testing for presence of an element in the collection
        assertTrue(coreOperationCompare.contains(it, "item2"));

        // Testing for absence of an element in the collection
        assertFalse(coreOperationCompare.contains(it, "item4"));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        Iterator it1 = Arrays.asList("item1", "item2", "item3").iterator();
        Iterator it2 = Arrays.asList("item3", "item4", "item5").iterator();

        // Testing for matching elements in two collections
        assertTrue(coreOperationCompare.findMatch(it1, it2));

        // Testing for no matching elements in two collections
        assertFalse(coreOperationCompare.findMatch(Collections.emptyIterator(), it2));
    }

    @Test
    public void testOtherMethods() {
        // Test cases for other methods in the CoreOperationCompare class can be added here.
    }

    private static class CustomExpression extends org.apache.commons.jxpath.ri.compiler.Expression {

        private final Object value;

        public CustomExpression(Object value) {
            this.value = value;
        }

        @Override
        public Object compute(EvalContext context) {
            return value;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }
}