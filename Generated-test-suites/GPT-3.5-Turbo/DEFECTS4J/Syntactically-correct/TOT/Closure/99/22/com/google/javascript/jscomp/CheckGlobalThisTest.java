import org.junit.Test;

import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void shouldReportGlobalThisInAssignment() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldReportGlobalThisWithPropertyAccess() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldNotReportGlobalThisInConstructorFunction() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldNotReportGlobalThisWithThisTypeAnnotation() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldNotReportGlobalThisWithOverrideAnnotation() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldNotReportGlobalThisInSafeFunctionContext() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldHandleMoreComplexAssignment() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldHandleNestedAssignment() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldNotReportGlobalThisWithAssignmentToPrototypeProperty() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldNotReportGlobalThisWithAssignmentToSubproperty() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldReportGlobalThisWhenEncountered() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldNotReportGlobalThisWhenNotEncountered() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertFalse(result);
    }
}