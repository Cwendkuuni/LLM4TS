import static org.junit.Assert.*;

import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype_sameType_true() {
        // Arrange
        ArrowType type1 = new ArrowType(null, null, null);
        ArrowType type2 = new ArrowType(null, null, null);

        // Act
        boolean result = type1.isSubtype(type2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsSubtype_differentType_false() {
        // Arrange
        ArrowType type1 = new ArrowType(null, null, null);
        ArrowType type2 = new ArrowType(null, null, null, true);

        // Act
        boolean result = type1.isSubtype(type2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasEqualParameters_sameParametersAndReturn_true() {
        // Arrange
        Node parameters1 = null;
        Node parameters2 = null;
        ArrowType type = new ArrowType(null, parameters1, null);
        ArrowType that = new ArrowType(null, parameters2, null);

        // Act
        boolean result = type.hasEqualParameters(that, true);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasEqualParameters_differentParameters_false() {
        // Arrange
        Node parameters1 = null;
        Node parameters2 = new Node();
        ArrowType type = new ArrowType(null, parameters1, null);
        ArrowType that = new ArrowType(null, parameters2, null);

        // Act
        boolean result = type.hasEqualParameters(that, true);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testCheckArrowEquivalenceHelper_sameReturnTypeAndParameters_true() {
        // Arrange
        Node parameters1 = null;
        Node parameters2 = null;
        ArrowType type = new ArrowType(null, parameters1, null);
        ArrowType that = new ArrowType(null, parameters2, null);

        // Act
        boolean result = type.checkArrowEquivalenceHelper(that, true);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testCheckArrowEquivalenceHelper_differentReturnType_false() {
        // Arrange
        ArrowType type = new ArrowType(null, null, null);
        ArrowType that = new ArrowType(null, null, null, true);

        // Act
        boolean result = type.checkArrowEquivalenceHelper(that, true);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHashCode_sameTypeAndParametersAndReturnType_equalHashCodes() {
        // Arrange
        ArrowType type1 = new ArrowType(null, null, null);
        ArrowType type2 = new ArrowType(null, null, null);

        // Act
        int hashCode1 = type1.hashCode();
        int hashCode2 = type2.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_differentTypeAndParametersAndReturnType_differentHashCodes() {
        // Arrange
        ArrowType type1 = new ArrowType(null, null, null);
        ArrowType type2 = new ArrowType(null, null, null, true);

        // Act
        int hashCode1 = type1.hashCode();
        int hashCode2 = type2.hashCode();

        // Assert
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testGetLeastSupertype_unsupportedOperationException() {
        // Arrange
        ArrowType type = new ArrowType(null, null, null);

        try {
            // Act
            type.getLeastSupertype(null);
            fail("Expected UnsupportedOperationException was not thrown.");
        } catch (UnsupportedOperationException e) {
            // Assert
            assertEquals("Method not supported for ArrowType.", e.getMessage());
        }
    }

    @Test
    public void testGetGreatestSubtype_unsupportedOperationException() {
        // Arrange
        ArrowType type = new ArrowType(null, null, null);

        try {
            // Act
            type.getGreatestSubtype(null);
            fail("Expected UnsupportedOperationException was not thrown.");
        } catch (UnsupportedOperationException e) {
            // Assert
            assertEquals("Method not supported for ArrowType.", e.getMessage());
        }
    }

    @Test
    public void testTestForEquality_unsupportedOperationException() {
        // Arrange
        ArrowType type = new ArrowType(null, null, null);

        try {
            // Act
            type.testForEquality(null);
            fail("Expected UnsupportedOperationException was not thrown.");
        } catch (UnsupportedOperationException e) {
            // Assert
            assertEquals("Method not supported for ArrowType.", e.getMessage());
        }
    }

    @Test
    public void testVisit_unsupportedOperationException() {
        // Arrange
        ArrowType type = new ArrowType(null, null, null);

        try {
            // Act
            type.visit(null);
            fail("Expected UnsupportedOperationException was not thrown.");
        } catch (UnsupportedOperationException e) {
            // Assert
            assertEquals("Method not supported for ArrowType.", e.getMessage());
        }
    }

    @Test
    public void testHasAnyTemplateInternal_sameType_false() {
        // Arrange
        ArrowType type1 = new ArrowType(null, null, null);
        ArrowType type2 = new ArrowType(null, null, null);

        // Act
        boolean result1 = type1.hasAnyTemplateInternal();
        boolean result2 = type2.hasAnyTemplateInternal();

        // Assert
        assertFalse(result1);
        assertFalse(result2);
    }

    @Test
    public void testHasAnyTemplateInternal_differentType_false() {
        // Arrange
        ArrowType type1 = new ArrowType(null, null, null);
        ArrowType type2 = new ArrowType(null, null, null, true);

        // Act
        boolean result1 = type1.hasAnyTemplateInternal();
        boolean result2 = type2.hasAnyTemplateInternal();

        // Assert
        assertFalse(result1);
        assertFalse(result2);
    }

}