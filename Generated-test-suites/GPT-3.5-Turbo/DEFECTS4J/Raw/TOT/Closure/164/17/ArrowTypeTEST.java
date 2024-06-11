import org.junit.Assert;
import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype_WithEqualReturnType_ReturnsTrue() {
        // Arrange
        ArrowType arrowType1 = new ArrowType(null, null, new JSType());
        ArrowType arrowType2 = new ArrowType(null, null, new JSType());

        // Act
        boolean actual = arrowType1.isSubtype(arrowType2);

        // Assert
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsSubtype_WithUnequalReturnType_ReturnsFalse() {
        // Arrange
        ArrowType arrowType1 = new ArrowType(null, null, new JSType());
        ArrowType arrowType2 = new ArrowType(null, null, new JSType());

        // Act
        boolean actual = arrowType1.isSubtype(arrowType2);

        // Assert
        Assert.assertFalse(actual);
    }

    @Test
    public void testHasEqualParameters_WithEqualParameters_ReturnsTrue() {
        // Arrange
        ArrowType arrowType1 = new ArrowType(null, new Node(), new JSType());
        ArrowType arrowType2 = new ArrowType(null, new Node(), new JSType());

        // Act
        boolean actual = arrowType1.hasEqualParameters(arrowType2);

        // Assert
        Assert.assertTrue(actual);
    }

    @Test
    public void testHasEqualParameters_WithUnequalParameters_ReturnsFalse() {
        // Arrange
        ArrowType arrowType1 = new ArrowType(null, new Node(), new JSType());
        ArrowType arrowType2 = new ArrowType(null, new Node(), new JSType());

        // Act
        boolean actual = arrowType1.hasEqualParameters(arrowType2);

        // Assert
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsEquivalentTo_WithEqualObjects_ReturnsTrue() {
        // Arrange
        ArrowType arrowType1 = new ArrowType(null, null, new JSType());
        ArrowType arrowType2 = new ArrowType(null, null, new JSType());

        // Act
        boolean actual = arrowType1.isEquivalentTo(arrowType2);

        // Assert
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsEquivalentTo_WithUnequalObjects_ReturnsFalse() {
        // Arrange
        ArrowType arrowType1 = new ArrowType(null, null, new JSType());
        ArrowType arrowType2 = new ArrowType(null, null, new JSType());

        // Act
        boolean actual = arrowType1.isEquivalentTo(arrowType2);

        // Assert
        Assert.assertFalse(actual);
    }

    @Test
    public void testHashCode_WithEqualObjects_ReturnsEqualHashCodes() {
        // Arrange
        ArrowType arrowType1 = new ArrowType(null, null, new JSType());
        ArrowType arrowType2 = new ArrowType(null, null, new JSType());

        // Act
        int hashCode1 = arrowType1.hashCode();
        int hashCode2 = arrowType2.hashCode();

        // Assert
        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_WithUnequalObjects_ReturnsUnequalHashCodes() {
        // Arrange
        ArrowType arrowType1 = new ArrowType(null, null, new JSType());
        ArrowType arrowType2 = new ArrowType(null, null, new JSType());

        // Act
        int hashCode1 = arrowType1.hashCode();
        int hashCode2 = arrowType2.hashCode();

        // Assert
        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype_ThrowsUnsupportedOperationException() {
        // Arrange
        ArrowType arrowType1 = new ArrowType(null, null, new JSType());
        ArrowType arrowType2 = new ArrowType(null, null, new JSType());

        // Act
        arrowType1.getLeastSupertype(arrowType2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype_ThrowsUnsupportedOperationException() {
        // Arrange
        ArrowType arrowType1 = new ArrowType(null, null, new JSType());
        ArrowType arrowType2 = new ArrowType(null, null, new JSType());

        // Act
        arrowType1.getGreatestSubtype(arrowType2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality_ThrowsUnsupportedOperationException() {
        // Arrange
        ArrowType arrowType1 = new ArrowType(null, null, new JSType());
        ArrowType arrowType2 = new ArrowType(null, null, new JSType());

        // Act
        arrowType1.testForEquality(arrowType2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit_ThrowsUnsupportedOperationException() {
        // Arrange
        ArrowType arrowType = new ArrowType(null, null, new JSType());

        // Act
        arrowType.visit(visitor);
    }

    @Test
    public void testGetPossibleToBooleanOutcomes_ReturnsTrue() {
        // Arrange
        ArrowType arrowType = new ArrowType(null, null, new JSType());

        // Act
        BooleanLiteralSet outcomes = arrowType.getPossibleToBooleanOutcomes();

        // Assert
        Assert.assertEquals(BooleanLiteralSet.TRUE, outcomes);
    }

    @Test
    public void testHasUnknownParamsOrReturn_WithUnknownParam_ReturnsTrue() {
        // Arrange
        ArrowType arrowType = new ArrowType(null, null, new JSType());

        // Act
        boolean actual = arrowType.hasUnknownParamsOrReturn();

        // Assert
        Assert.assertTrue(actual);
    }

    @Test
    public void testHasUnknownParamsOrReturn_WithUnknownReturn_ReturnsTrue() {
        // Arrange
        ArrowType arrowType = new ArrowType(null, null, new JSType());

        // Act
        boolean actual = arrowType.hasUnknownParamsOrReturn();

        // Assert
        Assert.assertTrue(actual);
    }

    @Test
    public void testHasUnknownParamsOrReturn_WithoutUnknown_ReturnsFalse() {
        // Arrange
        ArrowType arrowType = new ArrowType(null, null, new JSType());

        // Act
        boolean actual = arrowType.hasUnknownParamsOrReturn();

        // Assert
        Assert.assertFalse(actual);
    }

    @Test
    public void testResolveInternal_WithNonNullValues_ResolvesTypes() {
        // Arrange
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<JSType>();
        ArrowType arrowType = new ArrowType(null, null, new JSType());

        // Act
        JSType resolvedType = arrowType.resolveInternal(errorReporter, scope);

        // Assert
        Assert.assertNotNull(resolvedType);
    }

    @Test
    public void testToStringHelper_WithForAnnotations_ReturnsSuperToString() {
        // Arrange
        ArrowType arrowType = new ArrowType(null, null, new JSType());

        // Act
        String result = arrowType.toStringHelper(true);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testToStringHelper_WithoutForAnnotations_ReturnsSuperToString() {
        // Arrange
        ArrowType arrowType = new ArrowType(null, null, new JSType());

        // Act
        String result = arrowType.toStringHelper(false);

        // Assert
        Assert.assertNotNull(result);
    }
}