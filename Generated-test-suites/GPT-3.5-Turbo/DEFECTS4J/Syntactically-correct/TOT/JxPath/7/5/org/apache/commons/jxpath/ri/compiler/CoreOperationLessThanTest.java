import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    private CoreOperationLessThan coreOperationLessThan;

    @Before
    public void setUp() {
        coreOperationLessThan = new CoreOperationLessThan(new Expression(), new Expression());
    }

    @Test
    public void computeValue_LessThan_ReturnsTrue() {
        EvalContext context = new EvalContext();
        context.setContextNode(new Node());

        Node node1 = new Node();
        node1.setValue(5);
        Node node2 = new Node();
        node2.setValue(10);

        coreOperationLessThan.args[0] = node1;
        coreOperationLessThan.args[1] = node2;

        assertTrue((Boolean) coreOperationLessThan.computeValue(context));
    }

    @Test
    public void computeValue_NotLessThan_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setContextNode(new Node());

        Node node1 = new Node();
        node1.setValue(10);
        Node node2 = new Node();
        node2.setValue(5);

        coreOperationLessThan.args[0] = node1;
        coreOperationLessThan.args[1] = node2;

        assertFalse((Boolean) coreOperationLessThan.computeValue(context));
    }

    @Test
    public void computeValue_EqualValues_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setContextNode(new Node());

        Node node1 = new Node();
        node1.setValue(5);
        Node node2 = new Node();
        node2.setValue(5);

        coreOperationLessThan.args[0] = node1;
        coreOperationLessThan.args[1] = node2;

        assertFalse((Boolean) coreOperationLessThan.computeValue(context));
    }

    @Test(expected = NullPointerException.class)
    public void computeValue_NullArguments_ThrowsNullPointerException() {
        EvalContext context = new EvalContext();
        context.setContextNode(new Node());

        coreOperationLessThan.args[0] = null;
        coreOperationLessThan.args[1] = null;

        coreOperationLessThan.computeValue(context);
    }

    @Test
    public void getSymbol_ReturnsLessThanSymbol() {
        assertEquals("<", coreOperationLessThan.getSymbol());
    }
}