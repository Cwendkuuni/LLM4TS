import static org.junit.Assert.*;
import static com.google.javascript.jscomp.type.JSType.*;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter ra;

  @Before
  public void setUp() {
    typeRegistry = new JSTypeRegistry();
    ra = new ClosureReverseAbstractInterpreter(new CodingConvention(), typeRegistry);
  }

  @Test
  public void testIsDef() {
    Node node = new Node("goog.isDef(null)");
    FlowScope blindScope = new FlowScope(null);
    Type type = ra.getType(node, blindScope);
    JSType expectedType = getNativeType(VOID_TYPE);
    assertEquals(expectedType, type);

    node = new Node("goog.isDef({})");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(BOOLEAN);
    assertTrue(expectedType.isSubtype(type));

    node = new Node("goog.isDef(1)");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(BOOLEAN);
    assertTrue(expectedType.isSubtype(type));

    node = new Node("goog.isDef('hello')");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(BOOLEAN);
    assertTrue(expectedType.isSubtype(type));

    node = new Node("goog.isDef(function() {})");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(BOOLEAN);
    assertTrue(expectedType.isSubtype(type));

    node = new Node("goog.isDef([1, 2])");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(BOOLEAN);
    assertTrue(expectedType.isSubtype(type));
  }

  @Test
  public void testIsDefReturnTypeMismatch() {
    Node node = new Node("goog.isDef(123)") {
      @Override
      public Type getType() {
        return getNativeType(NUMBER_TYPE);
      }
    };
    FlowScope blindScope = new FlowScope(null);
    JSType type = ra.getType(node, blindScope);
    JSType expectedType = getNativeType(VOID_TYPE);
    assertEquals(expectedType, type);
  }

  @Test
  public void testIsDefWithCustomFunction() {
    Node node = new Node("goog.isDef(myFunction)");
    FlowScope blindScope = new FlowScope(null);
    Type type = ra.getType(node, blindScope);
    JSType expectedType = getNativeType(BOOLEAN);
    assertTrue(expectedType.isSubtype(type));
  }

  @Test(expected = AssertionError.class)
  public void testIsDefNull() {
    Node node = new Node("goog.isDef(null)");
    FlowScope blindScope = new FlowScope(null);
    ra.getType(node, blindScope);
  }

  @Test
  public void testIsNull() {
    Node node = new Node("goog.isNull(null)");
    FlowScope blindScope = new FlowScope(null);
    Type type = ra.getType(node, blindScope);
    JSType expectedType = getNativeType(BOOLEAN);
    assertTrue(expectedType.isSubtype(type));

    node = new Node("goog.isNull(1)");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(VOID_TYPE);
    assertEquals(expectedType, type);

    node = new Node("goog.isNull({})");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(BOOLEAN);
    assertTrue(expectedType.isSubtype(type));

    node = new Node("goog.isNull('hello')");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(VOID_TYPE);
    assertEquals(expectedType, type);

    node = new Node("goog.isNull(function() {})");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(BOOLEAN);
    assertTrue(expectedType.isSubtype(type));

    node = new Node("goog.isNull([1, 2])");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(BOOLEAN);
    assertTrue(expectedType.isSubtype(type));
  }

  @Test
  public void testIsNullReturnTypeMismatch() {
    Node node = new Node("goog.isNull(123)") {
      @Override
      public Type getType() {
        return getNativeType(NUMBER_TYPE);
      }
    };
    FlowScope blindScope = new FlowScope(null);
    JSType type = ra.getType(node, blindScope);
    JSType expectedType = getNativeType(VOID_TYPE);
    assertEquals(expectedType, type);
  }

  @Test
  public void testIsNullWithCustomFunction() {
    Node node = new Node("goog.isNull(myFunction)");
    FlowScope blindScope = new FlowScope(null);
    Type type = ra.getType(node, blindScope);
    JSType expectedType = getNativeType(BOOLEAN);
    assertTrue(expectedType.isSubtype(type));
  }

  @Test(expected = AssertionError.class)
  public void testIsNullNull() {
    Node node = new Node("goog.isNull(null)");
    FlowScope blindScope = new FlowScope(null);
    ra.getType(node, blindScope);
  }

  @Test
  public void testIsDefAndNotNull() {
    Node node = new Node("goog.isDefAndNotNull(1)");
    FlowScope blindScope = new FlowScope(null);
    JSType type = ra.getType(node, blindScope);
    JSType expectedType = getNativeType(VOID_Type);
    assertEquals(expectedType, type);

    node = new Node("goog.isDefAndNotNull({})");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(BOOLEAN);
    assertTrue(expectedType.isSubtype(type));

    node = new Node("goog.isDefAndNotNull('hello')");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(BOOLEAN);
    assertTrue(expectedType.isSubtype(type));

    node = new Node("goog.isDefAndNotNull(function() {})");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(BOOLEAN);
    assertTrue(expectedType.isSubtype(type));

    node = new Node("goog.isDefAndNotNull([1, 2])");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(BOOLEAN);
    assertTrue(expectedType.isSubtype(type));
  }

  @Test(expected = AssertionError.class)
  public void testIsDefAndNotNullNull() {
    Node node = new Node("goog.isDefAndNotNull(null)");
    FlowScope blindScope = new FlowScope(null);
    ra.getType(node, blindScope);
  }

  @Test(expected = AssertionError.class)
  public void testIsDefAndNotNullUndefined() {
    Node node = new Node("goog.isDefAndNotNull(undefined)");
    FlowScope blindScope = new FlowScope(null);
    ra.getType(node, blindScope);
  }

  @Test
  public void testIsString() {
    Node node = new Node("goog.isString('hello')");
    FlowScope blindScope = new FlowScope(null);
    Type type = ra.getType(node, blindScope);
    JSType expectedType = getNativeType(BOOLEAN);
    assertTrue(expectedType.isSubtype(type));

    node = new Node("goog.isString(1)");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(VOID_TYPE);
    assertEquals(expectedType, type);

    node = new Node("goog.isString({})");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(VOID_TYPE);
    assertEquals(expectedType, type);

    node = new Node("goog.isString(function() {})");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(VOID_TYPE);
    assertEquals(expectedType, type);

    node = new Node("goog.isString([1, 2])");
    type = ra.getType(node, blindScope);
    expectedType = getNativeType(VOID_TYPE);
    assertEquals(expectedType, type);
  }

  @Test
  public void testIsStringWithCustomFunction() {
    Node node = new Node("goog.isString(myFunction)");
    FlowScope blindScope = new FlowScope(null);
    Type type = ra.getType(node, blindScope);
    JSType expectedType = getNativeType(BOOLEAN);
    assertTrue(expectedType.isSubtype(type));
  }
}