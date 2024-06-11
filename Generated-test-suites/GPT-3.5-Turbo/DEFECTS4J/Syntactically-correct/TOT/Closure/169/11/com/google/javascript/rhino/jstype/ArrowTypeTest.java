import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);
    ArrowType arrowType3 = new ArrowType(registry, parameters3, returnType3);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType2.isSubtype(arrowType3));
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);
    ArrowType arrowType3 = new ArrowType(registry, parameters3, returnType3);

    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
    assertFalse(arrowType1.hasEqualParameters(arrowType3, false));
    assertFalse(arrowType2.hasEqualParameters(arrowType3, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);
    ArrowType arrowType3 = new ArrowType(registry, parameters3, returnType3);

    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType3, false));
    assertFalse(arrowType2.checkArrowEquivalenceHelper(arrowType3, false));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    JSType testType = new JSType(registry);

    arrowType.getLeastSupertype(testType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    JSType testType = new JSType(registry);

    arrowType.getGreatestSubtype(testType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    arrowType1.testForEquality(arrowType2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    Visitor<JSType> visitor = new Visitor<>();

    arrowType.visit(visitor);
  }

  @Test
  public void testHashCode() {
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertNotEquals(arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    arrowType.resolveInternal(errorReporter, scope);

    assertNotNull(arrowType.returnType);
    assertNotNull(arrowType.parameters);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasAnyTemplateInternal() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertFalse(arrowType.hasAnyTemplateInternal());
  }
}