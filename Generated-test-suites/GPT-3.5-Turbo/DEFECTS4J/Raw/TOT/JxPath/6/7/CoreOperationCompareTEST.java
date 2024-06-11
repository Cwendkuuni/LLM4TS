import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;

public class CoreOperationCompareTest {

    @Test
    public void testEqual_BothPointersEqual_ReturnsTrue() {
        // Arrange
        Expression left = new DummyExpression();
        Expression right = new DummyExpression();
        CoreOperationCompare operation = new DummyCoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Pointer("dummyValue"));

        // Act
        boolean result = operation.equal(context, left, right);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_LeftPointerEqual_ReturnsTrue() {
        // Arrange
        Expression left = new DummyExpression();
        Expression right = new DummyExpression();
        CoreOperationCompare operation = new DummyCoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Pointer("dummyValue"));
        Object r = new Object();

        // Act
        boolean result = operation.equal(context, left, right);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_RightPointerEqual_ReturnsTrue() {
        // Arrange
        Expression left = new DummyExpression();
        Expression right = new DummyExpression();
        CoreOperationCompare operation = new DummyCoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Pointer("dummyValue"));
        Object l = new Object();

        // Act
        boolean result = operation.equal(context, left, right);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_BothPointersDifferent_ReturnsFalse() {
        // Arrange
        Expression left = new DummyExpression();
        Expression right = new DummyExpression();
        CoreOperationCompare operation = new DummyCoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Pointer("dummyValue"));
        Object l = new Object();
        Object r = new Object();

        // Act
        boolean result = operation.equal(context, left, right);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_LeftCollectionContainsRightElement_ReturnsTrue() {
        // Arrange
        Expression left = new DummyExpression();
        Expression right = new DummyExpression();
        CoreOperationCompare operation = new DummyCoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        Object l = new Object();
        Object r = new Object();
        context.setSingleNodePointer(new Pointer("dummyValue"));
        Collection<Object> collection = new ArrayList<>();
        collection.add(l);
        context.setValue(collection);

        // Act
        boolean result = operation.equal(context, left, right);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_RightCollectionContainsLeftElement_ReturnsTrue() {
        // Arrange
        Expression left = new DummyExpression();
        Expression right = new DummyExpression();
        CoreOperationCompare operation = new DummyCoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        Object l = new Object();
        Object r = new Object();
        context.setSingleNodePointer(new Pointer("dummyValue"));
        Collection<Object> collection = new ArrayList<>();
        collection.add(r);
        context.setValue(collection);

        // Act
        boolean result = operation.equal(context, left, right);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_NoPointerWithCollections_ReturnsTrue() {
        // Arrange
        Expression left = new DummyExpression();
        Expression right = new DummyExpression();
        CoreOperationCompare operation = new DummyCoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        Object l = new Object();
        Object r = new Object();
        Collection<Object> leftCollection = new ArrayList<>();
        Collection<Object> rightCollection = new ArrayList<>();
        leftCollection.add(l);
        rightCollection.add(r);

        // Act
        boolean result = operation.equal(context, left, right);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_BooleanValuesEqual_ReturnsTrue() {
        // Arrange
        Expression left = new DummyExpression();
        Expression right = new DummyExpression();
        CoreOperationCompare operation = new DummyCoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        Object l = true;
        Object r = true;
        context.setValue(l);

        // Act
        boolean result = operation.equal(context, left, right);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_BooleanValuesDifferent_ReturnsFalse() {
        // Arrange
        Expression left = new DummyExpression();
        Expression right = new DummyExpression();
        CoreOperationCompare operation = new DummyCoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        Object l = true;
        Object r = false;
        context.setValue(l);

        // Act
        boolean result = operation.equal(context, left, right);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_IntegerValuesEqual_ReturnsTrue() {
        // Arrange
        Expression left = new DummyExpression();
        Expression right = new DummyExpression();
        CoreOperationCompare operation = new DummyCoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        Object l = 10;
        Object r = 10;
        context.setValue(l);

        // Act
        boolean result = operation.equal(context, left, right);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_IntegerValuesDifferent_ReturnsFalse() {
        // Arrange
        Expression left = new DummyExpression();
        Expression right = new DummyExpression();
        CoreOperationCompare operation = new DummyCoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        Object l = 10;
        Object r = 20;
        context.setValue(l);

        // Act
        boolean result = operation.equal(context, left, right);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_StringValuesEqual_ReturnsTrue() {
        // Arrange
        Expression left = new DummyExpression();
        Expression right = new DummyExpression();
        CoreOperationCompare operation = new DummyCoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        Object l = "hello";
        Object r = "hello";
        context.setValue(l);

        // Act
        boolean result = operation.equal(context, left, right);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_StringValuesDifferent_ReturnsFalse() {
        // Arrange
        Expression left = new DummyExpression();
        Expression right = new DummyExpression();
        CoreOperationCompare operation = new DummyCoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        Object l = "hello";
        Object r = "world";
        context.setValue(l);

        // Act
        boolean result = operation.equal(context, left, right);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_LeftIsNull_ReturnsFalse() {
        // Arrange
        Expression left = new DummyExpression();
        Expression right = new DummyExpression();
        CoreOperationCompare operation = new DummyCoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        Object l = null;
        Object r = new Object();
        context.setValue(l);

        // Act
        boolean result = operation.equal(context, left, right);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_RightIsNull_ReturnsFalse() {
        // Arrange
        Expression left = new DummyExpression();
        Expression right = new DummyExpression();
        CoreOperationCompare operation = new DummyCoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        Object l = new Object();
        Object r = null;
        context.setValue(l);

        // Act
        boolean result = operation.equal(context, left, right);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_BothNull_ReturnsTrue() {
        // Arrange
        Expression left = new DummyExpression();
        Expression right = new DummyExpression();
        CoreOperationCompare operation = new DummyCoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        Object l = null;
        Object r = null;
        context.setValue(l);

        // Act
        boolean result = operation.equal(context, left, right);

        // Assert
        Assert.assertTrue(result);
    }

    private class DummyExpression extends Expression {
        @Override
        public Object compute(EvalContext context) {
            return null;
        }
    }

    private class DummyCoreOperationCompare extends CoreOperationCompare {
        public DummyCoreOperationCompare(Expression arg1, Expression arg2) {
            super(arg1, arg2);
        }
    }
}