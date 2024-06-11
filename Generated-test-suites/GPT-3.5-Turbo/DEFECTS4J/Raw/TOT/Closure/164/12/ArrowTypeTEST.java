import org.junit.Assert;
import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype_validTypes_returnsTrue() {
        // Arrange
        Node parameters = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType1 = new ArrowType(parameters, returnType);
        ArrowType arrowType2 = new ArrowType(parameters, returnType);

        // Act
        boolean result = arrowType1.isSubtype(arrowType2);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsSubtype_invalidTypes_returnsFalse() {
        // Arrange
        Node parameters1 = new Node();
        Node parameters2 = new Node();
        JSType returnType1 = new JSType();
        JSType returnType2 = new JSType();
        ArrowType arrowType1 = new ArrowType(parameters1, returnType1);
        ArrowType arrowType2 = new ArrowType(parameters2, returnType2);

        // Act
        boolean result = arrowType1.isSubtype(arrowType2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEquivalentTo_equalTypes_returnsTrue() {
        // Arrange
        Node parameters = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType1 = new ArrowType(parameters, returnType);
        ArrowType arrowType2 = new ArrowType(parameters, returnType);

        // Act
        boolean result = arrowType1.isEquivalentTo(arrowType2);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEquivalentTo_notEqualTypes_returnsFalse() {
        // Arrange
        Node parameters1 = new Node();
        Node parameters2 = new Node();
        JSType returnType1 = new JSType();
        JSType returnType2 = new JSType();
        ArrowType arrowType1 = new ArrowType(parameters1, returnType1);
        ArrowType arrowType2 = new ArrowType(parameters2, returnType2);

        // Act
        boolean result = arrowType1.isEquivalentTo(arrowType2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testHasEqualParameters_equalParameters_returnsTrue() {
        // Arrange
        Node parameters = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType1 = new ArrowType(parameters, returnType);
        ArrowType arrowType2 = new ArrowType(parameters, returnType);

        // Act
        boolean result = arrowType1.hasEqualParameters(arrowType2);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testHasEqualParameters_notEqualParameters_returnsFalse() {
        // Arrange
        Node parameters1 = new Node();
        Node parameters2 = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType1 = new ArrowType(parameters1, returnType);
        ArrowType arrowType2 = new ArrowType(parameters2, returnType);

        // Act
        boolean result = arrowType1.hasEqualParameters(arrowType2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testHashCode_equalTypes_sameHashCodes() {
        // Arrange
        Node parameters = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType1 = new ArrowType(parameters, returnType);
        ArrowType arrowType2 = new ArrowType(parameters, returnType);

        // Act
        int hashCode1 = arrowType1.hashCode();
        int hashCode2 = arrowType2.hashCode();

        // Assert
        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_notEqualTypes_differentHashCodes() {
        // Arrange
        Node parameters1 = new Node();
        Node parameters2 = new Node();
        JSType returnType1 = new JSType();
        JSType returnType2 = new JSType();
        ArrowType arrowType1 = new ArrowType(parameters1, returnType1);
        ArrowType arrowType2 = new ArrowType(parameters2, returnType2);

        // Act
        int hashCode1 = arrowType1.hashCode();
        int hashCode2 = arrowType2.hashCode();

        // Assert
        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype_notSupported_throwsException() {
        // Arrange
        ArrowType arrowType = new ArrowType();

        // Act
        arrowType.getLeastSupertype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype_notSupported_throwsException() {
        // Arrange
        ArrowType arrowType = new ArrowType();

        // Act
        arrowType.getGreatestSubtype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality_notSupported_throwsException() {
        // Arrange
        ArrowType arrowType = new ArrowType();

        // Act
        arrowType.testForEquality(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit_notSupported_throwsException() {
        // Arrange
        ArrowType arrowType = new ArrowType();

        // Act
        arrowType.visit(null);
    }
}