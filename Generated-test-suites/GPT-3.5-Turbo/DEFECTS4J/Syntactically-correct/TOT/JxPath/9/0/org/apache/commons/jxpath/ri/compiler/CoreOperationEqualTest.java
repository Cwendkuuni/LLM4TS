import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    private CoreOperationEqual coreOperationEqual;

    @Test
    public void testComputeValue_TypicalUseCase_ReturnsTrue() {
        // Setup
        Expression arg1 = new Expression("value1");
        Expression arg2 = new Expression("value1");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        // Act
        Object result = coreOperationEqual.computeValue(null);

        // Assert
        assertTrue((boolean) result);
    }

    @Test
    public void testComputeValue_TypicalUseCase_ReturnsFalse() {
        // Setup
        Expression arg1 = new Expression("value1");
        Expression arg2 = new Expression("value2");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        // Act
        Object result = coreOperationEqual.computeValue(null);

        // Assert
        assertFalse((boolean) result);
    }

    @Test
    public void testComputeValue_EdgeCase_EmptyValues_ReturnsTrue() {
        // Setup
        Expression arg1 = new Expression("");
        Expression arg2 = new Expression("");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        // Act
        Object result = coreOperationEqual.computeValue(null);

        // Assert
        assertTrue((boolean) result);
    }

    @Test
    public void testComputeValue_EdgeCase_NullValues_ReturnsTrue() {
        // Setup
        Expression arg1 = new Expression(null);
        Expression arg2 = new Expression(null);
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        // Act
        Object result = coreOperationEqual.computeValue(null);

        // Assert
        assertTrue((boolean) result);
    }

    @Test
    public void testComputeValue_ErrorScenario_OneValueNull_ReturnsFalse() {
        // Setup
        Expression arg1 = new Expression("value1");
        Expression arg2 = new Expression(null);
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        // Act
        Object result = coreOperationEqual.computeValue(null);

        // Assert
        assertFalse((boolean) result);
    }

    @Test
    public void testComputeValue_ErrorScenario_BothValuesNull_ReturnsFalse() {
        // Setup
        Expression arg1 = new Expression(null);
        Expression arg2 = new Expression(null);
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        // Act
        Object result = coreOperationEqual.computeValue(null);

        // Assert
        assertFalse((boolean) result);
    }
}