import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();

        // Test case 1: l and r are both null
        context.setSingleNodePointer(null);
        left.setValue(null);
        right.setValue(null);
        assertTrue(operation.equal(context, left, right));

        // Test case 2: l is null, r is not null
        context.setSingleNodePointer(null);
        left.setValue(null);
        right.setValue("test");
        assertFalse(operation.equal(context, left, right));

        // Test case 3: l is not null, r is null
        context.setSingleNodePointer("test");
        left.setValue("test");
        right.setValue(null);
        assertFalse(operation.equal(context, left, right));

        // Test case 4: l and r are equal
        context.setSingleNodePointer("test");
        left.setValue("test");
        right.setValue("test");
        assertTrue(operation.equal(context, left, right));

        // Test case 5: l and r are not equal
        context.setSingleNodePointer("test");
        left.setValue("test");
        right.setValue("test2");
        assertFalse(operation.equal(context, left, right));
    }

    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), new Expression());
        Iterator it = new Iterator() {
            private int index = 0;
            private String[] values = {"test1", "test2", "test3"};

            @Override
            public boolean hasNext() {
                return index < values.length;
            }

            @Override
            public Object next() {
                return values[index++];
            }
        };
        String value = "test2";

        // Test case 1: value is in the iterator
        assertTrue(operation.contains(it, value));

        // Test case 2: value is not in the iterator
        value = "test4";
        assertFalse(operation.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), new Expression());
        Iterator lit = new Iterator() {
            private int index = 0;
            private String[] values = {"test1", "test2", "test3"};

            @Override
            public boolean hasNext() {
                return index < values.length;
            }

            @Override
            public Object next() {
                return values[index++];
            }
        };
        Iterator rit = new Iterator() {
            private int index = 0;
            private String[] values = {"test4", "test2", "test5"};

            @Override
            public boolean hasNext() {
                return index < values.length;
            }

            @Override
            public Object next() {
                return values[index++];
            }
        };

        // Test case 1: match found between lit and rit
        assertTrue(operation.findMatch(lit, rit));

        // Test case 2: match not found between lit and rit
        lit = new Iterator() {
            private int index = 0;
            private String[] values = {"test1", "test2", "test3"};

            @Override
            public boolean hasNext() {
                return index < values.length;
            }

            @Override
            public Object next() {
                return values[index++];
            }
        };
        rit = new Iterator() {
            private int index = 0;
            private String[] values = {"test4", "test5"};

            @Override
            public boolean hasNext() {
                return index < values.length;
            }

            @Override
            public Object next() {
                return values[index++];
            }
        };
        assertFalse(operation.findMatch(lit, rit));
    }

    @Test
    public void testEqualValues() {
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), new Expression());

        // Test case 1: l and r are both pointers and equal
        Object l = new Pointer();
        Object r = new Pointer();
        assertTrue(operation.equal(l, r));

        // Test case 2: l is a pointer and r is not
        l = new Pointer();
        r = "test";
        assertFalse(operation.equal(l, r));

        // Test case 3: l is not a pointer and r is a pointer
        l = "test";
        r = new Pointer();
        assertFalse(operation.equal(l, r));

        // Test case 4: l and r are equal
        l = "test";
        r = "test";
        assertTrue(operation.equal(l, r));

        // Test case 5: l and r are not equal
        l = "test";
        r = "test2";
        assertFalse(operation.equal(l, r));

        // Test case 6: l and r are both booleans and equal
        l = true;
        r = true;
        assertTrue(operation.equal(l, r));

        // Test case 7: l and r are both booleans and not equal
        l = true;
        r = false;
        assertFalse(operation.equal(l, r));

        // Test case 8: l and r are both numbers and equal
        l = 123;
        r = 123;
        assertTrue(operation.equal(l, r));

        // Test case 9: l and r are both numbers and not equal
        l = 123;
        r = 456;
        assertFalse(operation.equal(l, r));

        // Test case 10: l and r are both strings and equal
        l = "test";
        r = "test";
        assertTrue(operation.equal(l, r));

        // Test case 11: l and r are both strings and not equal
        l = "test";
        r = "test2";
        assertFalse(operation.equal(l, r));

        // Test case 12: l and r are both null
        l = null;
        r = null;
        assertTrue(operation.equal(l, r));

        // Test case 13: l is null, r is not null
        l = null;
        r = "test";
        assertFalse(operation.equal(l, r));

        // Test case 14: l is not null, r is null
        l = "test";
        r = null;
        assertFalse(operation.equal(l, r));
    }
}