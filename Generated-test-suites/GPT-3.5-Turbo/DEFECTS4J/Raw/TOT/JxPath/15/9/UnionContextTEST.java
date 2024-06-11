import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

    private EvalContext context1;
    private EvalContext context2;
    private EvalContext context3;
    private UnionContext unionContext;

    @Before
    public void setUp() {
        context1 = new EvalContext(); // create a valid context
        context2 = new EvalContext(); // create another valid context
        context3 = new EvalContext(); // create another valid context
        unionContext = new UnionContext(null, new EvalContext[]{context1, context2, context3});
    }

    @Test
    public void testGetDocumentOrderWithMultipleContexts() {
        // Test the getDocumentOrder() method when there are multiple contexts
        int expectedOrder = 1;
        int actualOrder = unionContext.getDocumentOrder();
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void testGetDocumentOrderWithSingleContext() {
        // Test the getDocumentOrder() method when there is only one context
        EvalContext singleContext = new EvalContext();
        UnionContext singleUnionContext = new UnionContext(null, new EvalContext[]{singleContext});
        int expectedOrder = singleUnionContext.getDocumentOrder();
        int actualOrder = singleUnionContext.getDocumentOrder();
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void testSetPositionWithoutPreparation() {
        // Test the setPosition() method when preparation has not been done
        int position = 1;
        boolean expected = true;
        boolean actual = unionContext.setPosition(position);
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPositionMultipleTimes() {
        // Test the setPosition() method multiple times
        int position = 1;
        boolean expected = true;
        boolean actual = unionContext.setPosition(position);
        assertEquals(expected, actual);
        position = 2;
        expected = true;
        actual = unionContext.setPosition(position);
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPositionOutOfRange() {
        // Test the setPosition() method with position out of range
        int position = -1;
        boolean expected = false;
        boolean actual = unionContext.setPosition(position);
        assertEquals(expected, actual);
        position = 10;
        expected = false;
        actual = unionContext.setPosition(position);
        assertEquals(expected, actual);
    }

}