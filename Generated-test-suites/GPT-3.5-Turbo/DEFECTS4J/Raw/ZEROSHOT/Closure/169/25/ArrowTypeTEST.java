import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;

public class ArrowTypeTest {
  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node();
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    JSType other = new JSType();
    boolean result = arrowType.isSubtype(other);
    assertTrue(result);
  }

  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node();
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    ArrowType that = new ArrowType(registry, parameters, returnType);
    boolean result = arrowType.hasEqualParameters(that, true);
    assertTrue(result);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node();
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    JSType that = new JSType();
    arrowType.getLeastSupertype(that);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node();
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    JSType that = new JSType();
    arrowType.getGreatestSubtype(that);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node();
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    JSType that = new JSType();
    arrowType.testForEquality(that);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node();
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    Visitor visitor = new Visitor();
    arrowType.visit(visitor);
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node();
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    BooleanLiteralSet expected = BooleanLiteralSet.TRUE;
    BooleanLiteralSet result = arrowType.getPossibleToBooleanOutcomes();
    assertEquals(expected, result);
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node();
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    ErrorReporter t = new ErrorReporter();
    StaticScope scope = new StaticScope<JSType>();
    JSType expected = arrowType;
    JSType result = arrowType.resolveInternal(t, scope);
    assertEquals(expected, result);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node();
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    boolean result = arrowType.hasUnknownParamsOrReturn();
    assertFalse(result);
  }

  @Test
  public void testHasAnyTemplateInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node();
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    boolean result = arrowType.hasAnyTemplateInternal();
    assertFalse(result);
  }

  @Test
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node();
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    int expected = arrowType.hashCode();
    int result = arrowType.hashCode();
    assertEquals(expected, result);
  }

  @Test
  public void testToStringHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node();
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    String expected = "[ArrowType]";
    String result = arrowType.toStringHelper(true);
    assertEquals(expected, result);
  }
}