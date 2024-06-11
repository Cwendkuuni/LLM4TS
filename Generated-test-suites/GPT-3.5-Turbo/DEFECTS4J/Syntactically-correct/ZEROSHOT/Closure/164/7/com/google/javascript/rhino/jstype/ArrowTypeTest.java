import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.Node;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, JSTypeNative.getUnknownType());
    ArrowType arrowType2 = new ArrowType(registry, null, JSTypeNative.getUnknownType());
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, JSTypeNative.getUnknownType());
    ArrowType arrowType2 = new ArrowType(registry, null, JSTypeNative.getUnknownType());
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, JSTypeNative.getUnknownType());
    ArrowType arrowType2 = new ArrowType(registry, null, JSTypeNative.getUnknownType());
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, JSTypeNative.getUnknownType());
    assertNotNull(arrowType.hashCode());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, JSTypeNative.getUnknownType());
    arrowType.getLeastSupertype(JSTypeNative.getUnknownType());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, JSTypeNative.getUnknownType());
    arrowType.getGreatestSubtype(JSTypeNative.getUnknownType());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, JSTypeNative.getUnknownType());
    arrowType.testForEquality(JSTypeNative.getUnknownType());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, JSTypeNative.getUnknownType());
    arrowType.visit(new Visitor<T>() {});
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, JSTypeNative.getUnknownType());
    assertNotNull(arrowType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, JSTypeNative.getUnknownType());
    assertNotNull(arrowType.resolveInternal(new ErrorReporter(), new StaticScope<JSType>()));
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, JSTypeNative.getUnknownType());
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testToStringHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, JSTypeNative.getUnknownType());
    assertNotNull(arrowType.toStringHelper(true));
  }

}