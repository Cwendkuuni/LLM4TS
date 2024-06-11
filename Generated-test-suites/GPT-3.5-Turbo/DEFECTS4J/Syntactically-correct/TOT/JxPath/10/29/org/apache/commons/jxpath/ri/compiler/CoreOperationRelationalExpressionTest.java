
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_EqualObjects_ReturnsTrue() {
        // Arrange
        Object left = "abc";
        Object right = "abc";
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression();

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testComputeValue_DifferentObjects_ReturnsFalse() {
        // Arrange
        Object left = "abc";
        Object right = "def";
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression();

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testComputeValue_LeftIsNaN_ReturnsFalse() {
        // Arrange
        Object left = Double.NaN;
        Object right = 10.0;
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression();

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testComputeValue_RightIsNaN_ReturnsFalse() {
        // Arrange
        Object left = 10.0;
        Object right = Double.NaN;
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression();

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testComputeValue_LeftLessThanRight_ReturnsTrue() {
        // Arrange
        Object left = 1;
        Object right = 10;
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression();

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testComputeValue_LeftGreaterThanRight_ReturnsFalse() {
        // Arrange
        Object left = 10;
        Object right = 1;
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression();

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testComputeValue_LeftEqualsRight_ReturnsFalse() {
        // Arrange
        Object left = 10;
        Object right = 10;
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression();

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testComputeValue_LeftCollectionContainsRight_ReturnsTrue() {
        // Arrange
        Object left = new ArrayList<>();
        ((ArrayList) left).add(1);
        ((ArrayList) left).add(2);
        ((ArrayList) left).add(3);
        Object right = 2;
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression();

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testComputeValue_LeftCollectionDoesNotContainRight_ReturnsFalse() {
        // Arrange
        Object left = new ArrayList<>();
        ((ArrayList) left).add(1);
        ((ArrayList) left).add(2);
        ((ArrayList) left).add(3);
        Object right = 4;
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression();

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testComputeValue_LeftCollectionContainsRightCollection_ReturnsTrue() {
        // Arrange
        Object left = new ArrayList<>();
        ((ArrayList) left).add(1);
        ((ArrayList) left).add(2);
        ((ArrayList) left).add(3);
        Object right = new ArrayList<>();
        ((ArrayList) right).add(2);
        ((ArrayList) right).add(4);
        ((ArrayList) right).add(6);
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression();

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testComputeValue_LeftCollectionDoesNotContainRightCollection_ReturnsFalse() {
        // Arrange
        Object left = new ArrayList<>();
        ((ArrayList) left).add(1);
        ((ArrayList) left).add(2);
        ((ArrayList) left).add(3);
        Object right = new ArrayList<>();
        ((ArrayList) right).add(4);
        ((ArrayList) right).add(5);
        ((ArrayList) right).add(6);
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression();

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertFalse(result);
    }
}

class ConcreteCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
    public ConcreteCoreOperationRelationalExpression() {
        super(null);
    }

    protected boolean evaluateCompare(int compare) {
        return (compare == 0);
    }
}
