import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.ThoroughTest;

public class ArrowTypeTest extends ThoroughTest {
  private static final ErrorReporter TRACE_REPORTER = new ErrorReporter() {
    @Override
    public void reportError(String message, int line, int lineOffset) {
      fail("Unexpected error: " + message);
    }

    @Override
    public void reportWarning(String message, int line, int lineOffset) {
      // ignore
    }
  };

  private static JSTypeRegistry createRegistry() {
    return new JSTypeRegistry() {
      @Override
      JSType getNativeType(int type) {
        return UNKNOWN_TYPE;
      }
    };
  }

  @SuppressWarnings("unchecked")
  private static <T> T getArrowType(JSTypeRegistry registry, Node node,
      JSType... types) {
    return (T) new ArrowType(registry, node, types[0], false);
  }

  @Test
  public void testArrowType_NullParameters() {
    JSTypeRegistry registry = createRegistry();
    ArrowType arrowType = getArrowType(registry, null, INT);
    assertEquals(new ArrowType(registry, null, INT), arrowType);
  }

  @Test
  public void testArrowType_EmptyParameters() {
    JSTypeRegistry registry = createRegistry();
    ArrowType arrowType = getArrowType(registry, NodeOperations.createList(), INT);
    assertEquals(new ArrowType(registry, NodeOperations.createList(), INT), arrowType);
  }

  @Test
  public void testArrowType_SingleParameter() {
    JSTypeRegistry registry = createRegistry();
    Node parameterNode = NodeOperations.createList(INT);
    ArrowType arrowType = getArrowType(registry, parameterNode, INT);
    assertEquals(new ArrowType(registry, parameterNode, INT), arrowType);
  }

  @Test
  public void testArrowType_MultipleParameters() {
    JSTypeRegistry registry = createRegistry();
    Node parametersNode = NodeOperations.createList(
        NodeOperations.createList(INT), INT);
    ArrowType arrowType = getArrowType(registry, parametersNode, INT, INT);
    assertEquals(new ArrowType(registry, parametersNode, INT, INT), arrowType);
  }

  @Test
  public void testArrowType_VarArgs() {
    JSTypeRegistry registry = createRegistry();
    Node parametersNode = NodeOperations.createList(
        NodeOperations.createList(INT), NodeOperations.createVarargsNode());
    ArrowType arrowType = getArrowType(registry, parametersNode, INT);
    assertEquals(new ArrowType(registry, parametersNode, INT), arrowType);
  }

  @Test
  public void testArrowType_InferredReturnType() {
    JSTypeRegistry registry = createRegistry();
    ArrowType arrowType = getArrowType(registry, NodeOperations.createList(), null);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowType_SetReturnType() {
    JSTypeRegistry registry = createRegistry();
    ArrowType arrowType = getArrowType(registry, NodeOperations.createList(), VOID);
    assertFalse(arrowType.returnTypeInferred);
    assertEquals(VOID, arrowType.returnType);
  }

  @Test
  public void testArrowType_Subtyping() {
    JSTypeRegistry registry = createRegistry();
    ArrowType arrowType1 = getArrowType(registry, NodeOperations.createList(), INT);
    ArrowType arrowType2 = getArrowType(registry, NodeOperations.createList(), NUMBER);
    assertTrue(arrowType1.isSubtype(arrowType2));

    ArrowType arrowType3 = getArrowType(registry, NodeOperations.createList(INT, INT),
        INT);
    ArrowType arrowType4 = getArrowType(registry, NodeOperations.createList(NUMBER, NUMBER),
        NUMBER);
    assertTrue(arrowType3.isSubtype(arrowType4));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testArrowType_GetLeastSupertype() {
    JSTypeRegistry registry = createRegistry();
    ArrowType arrowType = getArrowType(registry, NodeOperations.createList(), INT);
    arrowType.getLeastSupertype(INT);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testArrowType_GetGreatestSubtype() {
    JSTypeRegistry registry = createRegistry();
    ArrowType arrowType = getArrowType(registry, NodeOperations.createList(), INT);
    arrowType.getGreatestSubtype(INT);
  }

  @Test
  public void testArrowType_HashCode() {
    JSTypeRegistry registry = createRegistry();
    ArrowType arrowType1 = getArrowType(registry, NodeOperations.createList(), INT);
    ArrowType arrowType2 = getArrowType(registry, NodeOperations.createList(), INT);
    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());

    ArrowType arrowType3 = getArrowType(registry, NodeOperations.createList(INT), VOID);
    assertNotEquals(arrowType1.hashCode(), arrowType3.hashCode());
  }

  @Test
  public void testArrowType_ToString() {
    JSTypeRegistry registry = createRegistry();
    ArrowType arrowType = getArrowType(registry, NodeOperations.createList(INT), INT);
    String toString = arrowType.toString();
    String expectedString = "ArrowType[parameters=" + NodeOperations.toString(arrowType.parameters) +
        ", returnType=" + INT + "]";
    assertEquals(expectedString, toString);
  }

  @Test
  public void testArrowType_Resolving() {
    JSTypeRegistry registry = createRegistry();
    ArrowType arrowType = getArrowType(registry, NodeOperations.createList(IDENT), INT);
    staticScope.put("f", INT);
    arrowType.resolveInternal(TRACE_REPORTER, staticScope);
    assertEquals(INT, arrowType.returnType);
  }

  @Test
  public void testArrowType_UnknownParamsOrReturn() {
    JSTypeRegistry registry = createRegistry();
    ArrowType arrowType = new ArrowType(registry, NodeOperations.createList(), NULL);
    assertTrue(arrowType.hasUnknownParamsOrReturn());

    arrowType = new ArrowType(registry, NodeOperations.createList(), INT, NULL);
    assertTrue(arrowType.hasUnknownParamsOrReturn());

    arrowType = new ArrowType(registry, NodeOperations.createList(), NULL, INT);
    assertTrue(arrowType.hasUnknownParamsOrReturn());

    arrowType = new ArrowType(registry, NodeOperations.createList(IDENT), NULL);
    assertTrue(arrowType.hasUnknownParamsOrReturn());

    arrowType = new ArrowType(registry, NodeOperations.createList(IDENT), INT);
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }
}