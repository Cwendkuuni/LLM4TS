import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest extends CoreOperationTest {

    @Test
    public void testEqual() {
        CoreOperationCompare op = new CoreOperationCompare(new Constant("1"), new Constant("1"));
        assertTrue(op.evaluate(context));

        op = new CoreOperationCompare(new Constant("2"), new Constant("2"));
        assertTrue(op.evaluate(context));

        op = new CoreOperationCompare(new Constant("true"), new Constant("true"));
        assertTrue(op.evaluate(context));

        op = new CoreOperationCompare(new Constant("false"), new Constant("false"));
        assertTrue(op.evaluate(context));

        op = new CoreOperationCompare(new Constant("'abc'"), new Constant("'abc'"));
        assertTrue(op.evaluate(context));

        op = new CoreOperationCompare(new Constant(1.0), new Constant(1.0));
        assertTrue(op.evaluate(context));

        op = new CoreOperationCompare(new Constant(null), new Constant(null));
        assertTrue(op.evaluate(context));

        op = new CoreOperationCompare(new NumberValue(1), new NumberValue(1));
        assertTrue(op.evaluate(context));

        op = new CoreOperationCompare(new StringValue("abc"), new StringValue("abc"));
        assertTrue(op.evaluate(context));

        op = new CoreOperationCompare(new Constant("1"), new Constant("2"));
        assertFalse(op.evaluate(context));

        op = new CoreOperationCompare(new Constant("1"), new Constant("3"));
        assertFalse(op.evaluate(context));

        op = new CoreOperationCompare(new Constant("true"), new Constant("false"));
        assertFalse(op.evaluate(context));

        op = new CoreOperationCompare(new Constant("false"), new Constant("true"));
        assertFalse(op.evaluate(context));

        op = new CoreOperationCompare(new Constant("'abc'"), new Constant("'def'"));
        assertFalse(op.evaluate(context));

        op = new CoreOperationCompare(new Constant(1.0), new Constant(2.0));
        assertFalse(op.evaluate(context));

        op = new CoreOperationCompare(new Constant(null), new Constant("abc"));
        assertFalse(op.evaluate(context));

        op = new CoreOperationCompare(new NumberValue(1), new StringValue("abc"));
        assertFalse(op.evaluate(context));

        op = new CoreOperationCompare(new StringValue("abc"), new NumberValue(1));
        assertFalse(op.evaluate(context));
    }
}