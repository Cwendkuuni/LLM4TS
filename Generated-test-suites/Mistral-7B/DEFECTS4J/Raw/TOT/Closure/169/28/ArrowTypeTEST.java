import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ArrowTypeTest {

  private static JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testArrowTypeWithKnownTypes() {
    Node arrowFunctionNode = registry.parseNode("function(int x, string y) {}", 2);
    ArrowType arrowType = new ArrowType(registry, arrowFunctionNode.getFirstChild(),
        registry.findTypeFromCache(JSTypeNative.INT),
        registry.findTypeFromCache(JSTypeNative.STRING));

    JSType intType = registry.findTypeFromCache(JSTypeNative.INT);
    JSType stringType = registry.findTypeFromCache(JSTypeNative.STRING);
    JSType expectedArrowType = new ArrowType(registry, arrowFunctionNode.getFirstChild(), intType, stringType);

    assertEquals(expectedArrowType, arrowType);
  }

  @Test
  public void testArrowTypeWithUnknownTypes() {
    Node arrowFunctionNode = registry.parseNode("function(x, y) {}", 2);
    ArrowType arrowType = new ArrowType(registry, arrowFunctionNode.getFirstChild(), null, null);

    assertFalse(arrowType.isSubtype(registry.findTypeFromCache(JSTypeNative.INT)));
    assertFalse(registry.findTypeFromCache(JSTypeNative.INT).isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    Node arrowFunctionNode = registry.parseNode("function(int x, int... ints) {}", 2);
    ArrowType arrowType = new ArrowType(registry, arrowFunctionNode.getFirstChild(),
        registry.findTypeFromCache(JSTypeNative.INT), null);

    ArrowType arrowTypeWithVarArgs = new ArrowType(registry, arrowFunctionNode, null, null);

    assertTrue(arrowType.hasEqualParameters(arrowTypeWithVarArgs, false));
  }

  @Test
  public void testArrowTypeWithOptionalArgs() {
    Node arrowFunctionNode = registry.parseNode("function(int x = 1, int y) {}", 2);
    ArrowType arrowType = new ArrowType(registry, arrowFunctionNode.getFirstChild(),
        registry.findTypeFromCache(JSTypeNative.INT),
        registry.findTypeFromCache(JSTypeNative.INT));

    Node optionalArgNode = arrowFunctionNode.getChildAt(0);
    optionalArgNode.setOptionalArg(true);

    ArrowType arrowTypeWithOptionalArg = new ArrowType(registry, arrowFunctionNode, null, null);

    assertTrue(arrowType.hasEqualParameters(arrowTypeWithOptionalArg, false));
  }

  @Test
  public void testErrorReporting() {
    ErrorReporter errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, int line, int column) {
        // ignore
      }
    };

    Node arrowFunctionNode = registry.parseNode("function() {}", 0);
    ArrowType arrowType = new ArrowType(registry, arrowFunctionNode.getFirstChild(), null, false);

    assertNull(arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testUnsupportedOperations() {
    ArrowType arrowType = new ArrowType(registry, null, null, false);
    arrowType.getLeastSupertype(null);
    arrowType.getGreatestSubtype(null);
    arrowType.testForEquality(null);
  }
}