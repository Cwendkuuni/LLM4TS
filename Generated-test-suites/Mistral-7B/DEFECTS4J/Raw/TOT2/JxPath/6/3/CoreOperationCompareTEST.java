import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {

    @org.junit.Test
    public void testEqual() {
        Expression left = new StringLiteralExpression("1");
        Expression right = new StringLiteralExpression("1");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompareCompare(left, right);

        // Test with basic values
        boolean result = coreOperationCompare.evaluate(new EvalContext(), new InitialContext(), new SelfContext());
        assertTrue(result);

        left = new StringLiteralExpression("true");
        right = new StringLiteralExpression("true");
        result = coreOperationCompare.evaluate(new EvalContext(), new InitialContext(), new SelfContext());
        assertTrue(result);

        // Test with null values
        left = new NullExpression();
        result = coreOperationCompare.evaluate(new EvalContext(), new InitialContext(), new SelfContext());
        assertFalse(result);

        left = new NullExpression();
        right = new NullExpression();
        result = coreOperationCompare.evaluate(new EvalContext(), new InitialContext(), new SelfContext());
        assertTrue(result);

        // Test with different types
        left = new NumberLiteralExpression(1.0);
        right = new StringLiteralExpression("1");
        result = coreOperationCompare.evaluate(new EvalContext(), new InitialContext(), new SelfContext());
        assertFalse(result);

        // Test with collections
        Collection<Object> collection1 = new HashSet<>();
        collection1.add(new Integer(1));
        collection1.add(new Integer(2));
        Expression collectionExpression1 = new CollectionExpression(collection1);

        Collection<Object> collection2 = new HashSet<>();
        collection2.add(new Integer(1));
        collection2.add(new Integer(3));
        Expression collectionExpression2 = new CollectionExpression(collection2);

        result = coreOperationCompare.evaluate(new EvalContext(), collectionExpression1, collectionExpression2);
        assertFalse(result);

        collection1 = new HashSet<>();
        collection1.add(new Integer(1));
        collection1.add(new Integer(2));
        collectionExpression1 = new CollectionExpression(collection1);

        collection2 = new HashSet<>();
        collection2.add(new Integer(1));
        collection2.add(new Integer(1));
        collectionExpression2 = new CollectionExpression(collection2);

        result = coreOperationCompare.evaluate(new EvalContext(), collectionExpression1, collectionExpression2);
        assertTrue(result);
    }
}