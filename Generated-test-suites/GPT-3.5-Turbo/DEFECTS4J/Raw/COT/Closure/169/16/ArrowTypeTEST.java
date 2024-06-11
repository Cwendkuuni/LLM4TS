
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype_returnTrue() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtype_returnFalse() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHasEqualParameters_returnTrue() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
  }

  @Test
  public void testHasEqualParameters_returnFalse() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertFalse(arrowType1.hasEqualParameters(arrowType2, true));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_returnTrue() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
  }

  @Test
  public void testCheckArrowEquivalenceHelper_returnFalse() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
  }

  @Test
  public void testHashCode() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertNotNull(arrowType.hashCode());
  }
  
  @Test
  public void testGetLeastSupertype() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertThrows(UnsupportedOperationException.class,
        () -> arrowType1.getLeastSupertype(arrowType2));
  }

  @Test
  public void testGetGreatestSubtype() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertThrows(UnsupportedOperationException.class,
        () -> arrowType1.getGreatestSubtype(arrowType2));
  }

  @Test
  public void testTestForEquality() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertThrows(UnsupportedOperationException.class,
        () -> arrowType1.testForEquality(arrowType2));
  }

  @Test
  public void testVisit() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertThrows(UnsupportedOperationException.class,
        () -> arrowType.visit(new Visitor<T>() { }));
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertNotNull(arrowType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testResolveInternal() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>();
    assertNotNull(arrowType.resolveInternal(t, scope));
  }

  @Test
  public void testToStringHelper() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertNotNull(arrowType.toStringHelper(true));
  }

  @Test
  public void testHasAnyTemplateInternal() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertFalse(arrowType.hasAnyTemplateInternal());
  }
}

