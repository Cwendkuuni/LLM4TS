import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype_ReturnFalse_WhenOtherIsNotArrowType() {
        JSType other = new JSType();
        ArrowType arrowType = new ArrowType(null, null, null);
        boolean isSubtype = arrowType.isSubtype(other);
        Assert.assertFalse(isSubtype);
    }

    @Test
    public void testIsSubtype_ReturnTrue_WhenReturnTypesAreSubtypes() {
        JSType returnType = new JSType();
        JSType otherReturnType = new JSType();
        returnType.setSubtype(true);
        otherReturnType.setSubtype(true);
        ArrowType arrowType = new ArrowType(null, null, returnType);
        ArrowType other = new ArrowType(null, null, otherReturnType);
        boolean isSubtype = arrowType.isSubtype(other);
        Assert.assertTrue(isSubtype);
    }

    @Test
    public void testIsSubtype_ReturnFalse_WhenReturnTypesAreNotSubtypes() {
        JSType returnType = new JSType();
        JSType otherReturnType = new JSType();
        returnType.setSubtype(false);
        otherReturnType.setSubtype(true);
        ArrowType arrowType = new ArrowType(null, null, returnType);
        ArrowType other = new ArrowType(null, null, otherReturnType);
        boolean isSubtype = arrowType.isSubtype(other);
        Assert.assertFalse(isSubtype);
    }

    @Test
    public void testHasEqualParameters_ReturnTrue_WhenBothParameterListsAreNull() {
        ArrowType arrowType = new ArrowType(null, null, null);
        ArrowType other = new ArrowType(null, null, null);
        boolean isEqual = arrowType.hasEqualParameters(other);
        Assert.assertTrue(isEqual);
    }

    @Test
    public void testHasEqualParameters_ReturnFalse_WhenOneParameterListIsNullAndTheOtherIsNot() {
        Node parameters = new Node();
        ArrowType arrowType = new ArrowType(null, null, null);
        ArrowType other = new ArrowType(null, parameters, null);
        boolean isEqual = arrowType.hasEqualParameters(other);
        Assert.assertFalse(isEqual);
    }

    @Test
    public void testHasEqualParameters_ReturnFalse_WhenParameterTypesAreNotEqual() {
        JSType paramType1 = new JSType();
        JSType paramType2 = new JSType();
        Node parameters1 = new Node();
        Node parameters2 = new Node();
        Node param1 = new Node();
        Node param2 = new Node();
        param1.setJSType(paramType1);
        param2.setJSType(paramType2);
        paramType1.setEquivalentTo(false);
        paramType2.setEquivalentTo(true);
        parameters1.addChildToFront(param1);
        parameters2.addChildToFront(param2);
        ArrowType arrowType = new ArrowType(null, parameters1, null);
        ArrowType other = new ArrowType(null, parameters2, null);
        boolean isEqual = arrowType.hasEqualParameters(other);
        Assert.assertFalse(isEqual);
    }
  
    @Test
    public void testIsEquivalentTo_ReturnFalse_WhenOtherIsNotArrowType() {
        JSType other = new JSType();
        ArrowType arrowType = new ArrowType(null, null, null);
        boolean isEquivalent = arrowType.isEquivalentTo(other);
        Assert.assertFalse(isEquivalent);
    }
    
    @Test
    public void testIsEquivalentTo_ReturnFalse_WhenReturnTypesAreNotEquivalent() {
        JSType returnType = new JSType();
        JSType otherReturnType = new JSType();
        returnType.setEquivalentTo(false);
        otherReturnType.setEquivalentTo(true);
        ArrowType arrowType = new ArrowType(null, null, returnType);
        ArrowType other = new ArrowType(null, null, otherReturnType);
        boolean isEquivalent = arrowType.isEquivalentTo(other);
        Assert.assertFalse(isEquivalent);
    }
  
    @Test
    public void testIsEquivalentTo_ReturnTrue_WhenReturnTypesAreEquivalentAndBothParameterListsAreNull() {
        JSType returnType = new JSType();
        JSType otherReturnType = new JSType();
        returnType.setEquivalentTo(true);
        otherReturnType.setEquivalentTo(true);
        ArrowType arrowType = new ArrowType(null, null, returnType);
        ArrowType other = new ArrowType(null, null, otherReturnType);
        boolean isEquivalent = arrowType.isEquivalentTo(other);
        Assert.assertTrue(isEquivalent);
    }
  
    @Test
    public void testHashCode_ReturnEqualHashCode_WhenParametersAreEqual() {
        JSType paramType1 = new JSType();
        JSType paramType2 = new JSType();
        Node parameters1 = new Node();
        Node parameters2 = new Node();
        Node param1 = new Node();
        Node param2 = new Node();
        param1.setJSType(paramType1);
        param2.setJSType(paramType2);
        paramType1.setHashCode(3);
        paramType2.setHashCode(3);
        parameters1.addChildToFront(param1);
        parameters2.addChildToFront(param2);
        ArrowType arrowType = new ArrowType(null, parameters1, null);
        ArrowType other = new ArrowType(null, parameters2, null);
        boolean isEqual = arrowType.hashCode() == other.hashCode();
        Assert.assertTrue(isEqual);
    }
  
    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype_ThrowsException() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.getLeastSupertype(null);
    }
  
    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype_ThrowsException() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.getGreatestSubtype(null);
    }
  
    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality_ThrowsException() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.testForEquality(null);
    }
  
    @Test(expected = UnsupportedOperationException.class)
    public void testVisit_ThrowsException() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.visit(null);
    }
  
    @Test
    public void testGetPossibleToBooleanOutcomes_ReturnTrue() {
        ArrowType arrowType = new ArrowType(null, null, null);
        BooleanLiteralSet booleanLiteralSet = arrowType.getPossibleToBooleanOutcomes();
        Assert.assertEquals(BooleanLiteralSet.TRUE, booleanLiteralSet);
    }
  
    @Test(expected = UnsupportedOperationException.class)
    public void testResolveInternal_ThrowsException() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.resolveInternal(null, null);
    }
  
    @Test
    public void testHasUnknownParamsOrReturn_ReturnTrue_WhenParametersOrReturnAreUnknownType() {
        JSType returnType = new JSType();
        JSType paramType = new JSType();
        returnType.setUnknownType(true);
        paramType.setUnknownType(true);
        Node parameters = new Node();
        Node param = new Node();
        param.setJSType(paramType);
        parameters.addChildToFront(param);
        ArrowType arrowType = new ArrowType(null, parameters, returnType);
        boolean hasUnknown = arrowType.hasUnknownParamsOrReturn();
        Assert.assertTrue(hasUnknown);
    }
  
    @Test
    public void testToStringHelper() {
        ArrowType arrowType = new ArrowType(null, null, null);
        String toString = arrowType.toStringHelper(false);
        Assert.assertEquals(super.toString(), toString);
    }
}