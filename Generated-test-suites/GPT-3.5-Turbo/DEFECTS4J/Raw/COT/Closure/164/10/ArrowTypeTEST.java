import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    ArrowType arrow1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrow2 = new ArrowType(registry, parameters, returnType);
    assertTrue(arrow1.isSubtype(arrow2));
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType arrow1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrow2 = new ArrowType(registry, parameters, returnType);
    assertTrue(arrow1.hasEqualParameters(arrow2));
  }

  @Test
  public void testIsEquivalentTo() {
    ArrowType arrow1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrow2 = new ArrowType(registry, parameters, returnType);
    assertTrue(arrow1.isEquivalentTo(arrow2));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    ArrowType arrow1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrow2 = new ArrowType(registry, parameters, returnType);
    arrow1.getLeastSupertype(arrow2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    ArrowType arrow1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrow2 = new ArrowType(registry, parameters, returnType);
    arrow1.getGreatestSubtype(arrow2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    ArrowType arrow1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrow2 = new ArrowType(registry, parameters, returnType);
    arrow1.testForEquality(arrow2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    ArrowType arrow = new ArrowType(registry, parameters, returnType);
    arrow.visit(visitor);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrow = new ArrowType(registry, parameters, returnType);
    assertFalse(arrow.hasUnknownParamsOrReturn());
  }

  @Test
  public void testResolveInternal() {
    ArrowType arrow = new ArrowType(registry, parameters, returnType);
    JSType resolvedType = arrow.resolveInternal(t, scope);
    //assert that resolvedType is equal to the expected resolved type
  }

  @Test
  public void testToStringHelper() {
    ArrowType arrow = new ArrowType(registry, parameters, returnType);
    String toStringResult = arrow.toStringHelper(true);
    //assert that toStringResult is equal to the expected string representation
  }

}