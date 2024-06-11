import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeNative;

public class ArrowTypeTest {
  
  @Test
  public void testArrowTypeIsSubtype() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    boolean result = arrowType1.isSubtype(arrowType2);
    Assert.assertTrue(result);
  }
  
  @Test
  public void testArrowTypeHasEqualParameters() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    boolean result = arrowType1.hasEqualParameters(arrowType2, true);
    Assert.assertTrue(result);
  }
  
  @Test
  public void testArrowTypeCheckArrowEquivalenceHelper() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);
    boolean result = arrowType1.checkArrowEquivalenceHelper(arrowType2, true);
    Assert.assertTrue(result);
  }
  
  @Test
  public void testArrowTypeHashCode() {
    ArrowType arrowType = new ArrowType(null, null, null);
    int hashCode = arrowType.hashCode();
    Assert.assertEquals(0, hashCode);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeGetLeastSupertype() {
    ArrowType arrowType = new ArrowType(null, null, null);
    arrowType.getLeastSupertype(null);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeGetGreatestSubtype() {
    ArrowType arrowType = new ArrowType(null, null, null);
    arrowType.getGreatestSubtype(null);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeTestForEquality() {
    ArrowType arrowType = new ArrowType(null, null, null);
    arrowType.testForEquality(null);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeVisit() {
    ArrowType arrowType = new ArrowType(null, null, null);
    arrowType.visit(null);
  }
  
  @Test
  public void testArrowTypeGetPossibleToBooleanOutcomes() {
    ArrowType arrowType = new ArrowType(null, null, null);
    BooleanLiteralSet result = arrowType.getPossibleToBooleanOutcomes();
    Assert.assertEquals(BooleanLiteralSet.TRUE, result);
  }
  
  @Test
  public void testArrowTypeResolveInternal() {
    ArrowType arrowType = new ArrowType(null, null, null);
    ArrowType result = arrowType.resolveInternal(null, null);
    Assert.assertEquals(arrowType, result);
  }
  
  @Test
  public void testArrowTypeHasUnknownParamsOrReturn() {
    ArrowType arrowType = new ArrowType(null, null, null);
    boolean result = arrowType.hasUnknownParamsOrReturn();
    Assert.assertFalse(result);
  }
  
  @Test
  public void testArrowTypeToStringHelper() {
    ArrowType arrowType = new ArrowType(null, null, null);
    String result = arrowType.toStringHelper(true);
    Assert.assertEquals("[ArrowType]", result);
  }
  
  @Test
  public void testArrowTypeHasAnyTemplateInternal() {
    ArrowType arrowType = new ArrowType(null, null, null);
    boolean result = arrowType.hasAnyTemplateInternal();
    Assert.assertFalse(result);
  }
}