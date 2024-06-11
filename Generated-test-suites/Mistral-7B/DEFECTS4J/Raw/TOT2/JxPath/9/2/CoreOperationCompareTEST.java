import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqualPrecedence() {
        CoreOperationCompare op = new CoreOperationCompare(new CoreOperation("1"), new CoreOperation("1"));
        assertEquals(2, op.getPrecedence());
    }

    @Test
    public void testIsSymmetricTrue() {
        assertTrue(new CoreOperationCompare(new CoreOperation("1"), new CoreOperation("1")).isSymmetric());
    }

    @Test
    public void testEqualBooleanTrueTrue() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
            new CoreOperation("true"),
            new CoreOperation("true")
        );
        assertTrue(op.equal(context, new CoreOperation("true"), new CoreOperation("true")));
    }

    @Test
    public void testEqualBooleanFalseTrue() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
            new CoreOperation("false"),
            new CoreOperation("true")
        );
        assertFalse(op.equal(context, new CoreOperation("false"), new CoreOperation("true")));
    }

    @Test
    public void testEqualNumberEqual() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
            new CoreOperation("1.0"),
            new CoreOperation("1.0")
        );
        assertTrue(op.equal(context, new CoreOperation("1.0"), new CoreOperation("1.0")));
    }

    @Test
    public void testEqualNumberNotEqual() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
            new CoreOperation("1.0"),
            new CoreOperation("2.0")
        );
        assertFalse(op.equal(context, new CoreOperation("1.0"), new CoreOperation("2.0")));
    }

    @Test
    public void testContainsIteratorBooleanTrue() {
        Iterator<Object> it = new java.util.ArrayList<Object>() {{ add(new CoreOperation("true")); }}.iterator();
        CoreOperationCompare op = new CoreOperationCompare(it, new CoreOperation("true"));
        assertTrue(op.contains(it, new CoreOperation("true")));
    }

    @Test
    public void testContainsIteratorBooleanFalse() {
        Iterator<Object> it = new java.util.ArrayList<Object>() {{ add(new CoreOperation("false")); }}.iterator();
        CoreOperationCompare op = new CoreOperationCompare(it, new CoreOperation("true"));
        assertFalse(op.contains(it, new CoreOperation("true")));
    }

    @Test
    public void testFindMatchIteratorsEqual() {
        Iterator<Object> lit = new java.util.ArrayList<Object>() {{ add(new CoreOperation("true")); }}.iterator();
        Iterator<Object> rit = new java.util.ArrayList<Object>() {{ add(new CoreOperation("true")); }}.iterator();
        CoreOperationCompare op = new CoreOperationCompare(lit, rit);
        assertTrue(op.findMatch(lit, rit));
    }

    @Test
    public void testFindMatchIteratorsNotEqual() {
        Iterator<Object> lit = new java.util.ArrayList<Object>() {{ add(new CoreOperation("true")); }}.iterator();
        Iterator<Object> rit = new java.util.ArrayList<Object>() {{ add(new CoreOperation("false")); }}.iterator();
        CoreOperationCompare op = new CoreOperationCompare(lit, rit);
        assertFalse(op.findMatch(lit, rit));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualNullLeft() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(null, new CoreOperation("true"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualNullRight() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new CoreOperation("true"), null);
    }

    @Test
    public void testContainsEmptyCollection() {
        CoreOperationCompare op = new CoreOperationCompare(new java.util.ArrayList<Object>().iterator(), new CoreOperation("true"));
        assertFalse(op.contains(new java.util.ArrayList<Object>().iterator(), new CoreOperation("true")));
    }

    @Test(expected = NullPointerException.class)
    public void testContainsNullIterator() {
        CoreOperationCompare op = new CoreOperationCompare(null, new CoreOperation("true"));
    }

    @Test(expected = NullPointerException.class)
    public void testContainsNullValue() {
        Iterator<Object> it = new java.util.ArrayList<Object>() {{ add(new CoreOperation("true")); }}.iterator();
        CoreOperationCompare op = new CoreOperationCompare(it, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindMatchNullLeft() {
        CoreOperationCompare op = new CoreOperationCompare(null, new java.util.ArrayList<Object>() {{ add(new CoreOperation("true")); }}.iterator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindMatchNullRight() {
        CoreOperationCompare op = new CoreOperationCompare(new java.util.ArrayList<Object>() {{ add(new CoreOperation("true")); }}.iterator(), null);
    }
}