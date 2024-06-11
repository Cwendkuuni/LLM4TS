
import org.junit.Assert;
import org.junit.Test;

public class ArrowTypeTest {
    @Test
    public void testIsSubtype() {
        ArrowType arrowType1 = new ArrowType();
        ArrowType arrowType2 = new ArrowType();
        Assert.assertTrue(arrowType1.isSubtype(arrowType2));
    }
    
    @Test
    public void testHasEqualParameters() {
        ArrowType arrowType1 = new ArrowType();
        ArrowType arrowType2 = new ArrowType();
        Assert.assertTrue(arrowType1.hasEqualParameters(arrowType2));
    }
    
    @Test
    public void testIsEquivalentTo() {
        ArrowType arrowType1 = new ArrowType();
        ArrowType arrowType2 = new ArrowType();
        Assert.assertTrue(arrowType1.isEquivalentTo(arrowType2));
    }
    
    @Test
    public void testHashCode() {
        ArrowType arrowType = new ArrowType();
        Assert.assertNotNull(arrowType.hashCode());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        ArrowType arrowType = new ArrowType();
        arrowType.getLeastSupertype(arrowType);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        ArrowType arrowType = new ArrowType();
        arrowType.getGreatestSubtype(arrowType);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        ArrowType arrowType = new ArrowType();
        arrowType.testForEquality(arrowType);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        ArrowType arrowType = new ArrowType();
        arrowType.visit(new Visitor<>());
    }
    
    @Test
    public void testGetPossibleToBooleanOutcomes() {
        ArrowType arrowType = new ArrowType();
        Assert.assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testResolveInternal() {
        ArrowType arrowType = new ArrowType();
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
        arrowType.resolveInternal(errorReporter, scope);
    }
    
    @Test
    public void testHasUnknownParamsOrReturn() {
        ArrowType arrowType = new ArrowType();
        Assert.assertFalse(arrowType.hasUnknownParamsOrReturn());
    }
    
    @Test
    public void testToStringHelper() {
        ArrowType arrowType = new ArrowType();
        Assert.assertNotNull(arrowType.toStringHelper(true));
    }
}
