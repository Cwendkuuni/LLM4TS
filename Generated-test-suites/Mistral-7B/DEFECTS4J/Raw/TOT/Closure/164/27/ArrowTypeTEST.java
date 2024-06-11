import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.testing.TestException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArrowTypeTest {

  private static final ErrorReporter errors = new ErrorReporter();
  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testArrowType() {
    Node functionNode = new Node("function(int x, int y) { }");
    JSType functionType = new ArrowType(registry, functionNode.getParameters(),
        JSTypeNative.getIntType(), false);

    // Test isSubtype with a subtype
    JSType subType = new ArrowType(registry, functionNode.getParameters(),
        JSTypeNative.getIntType(), false);
    assertEquals(true, functionType.isSubtype(subType));

    // Test isSubtype with a supertype
    JSType superType = new ArrowType(registry, functionNode.getParameters(),
        JSTypeNative.getObjectType(registry), false);
    assertEquals(true, superType.isSubtype(functionType));

    // Test isSubtype with an unrelated type
    JSType unrelatedType = new FunctionType(registry, new NativeType(registry,
        "Object"), new NativeType(registry, "Object"), false);
    assertEquals(false, functionType.isSubtype(unrelatedType));

    // Test hasEqualParameters with a similar ArrowType
    Node similarFunctionNode = new Node("function(number x, number y) { }");
    JSType similarFunctionType = new ArrowType(registry, similarFunctionNode.getParameters(),
        JSTypeNative.getNumberType(), false);
    assertEquals(true, functionType.hasEqualParameters(similarFunctionType));

    // Test hasEqualParameters with a dissimilar ArrowType
    Node dissimilarFunctionNode = new Node("function(int x, boolean y) { }");
    JSType dissimilarFunctionType = new ArrowType(registry, dissimilarFunctionNode.getParameters(),
        JSTypeNative.getBooleanType(), false);
    assertEquals(false, functionType.hasEqualParameters(dissimilarFunctionType));
  }

  @Test(expected = TestException.class)
  public void testInvalidArrowType() {
    Node invalidFunctionNode = new Node("function() { }");
    JSType arrowType = new ArrowType(registry, invalidFunctionNode.getParameters(),
        null, false);
    arrowType.resolveInternal(errors, null);
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    Node functionNode = new Node("function(int x) { return x; }");
    JSType functionType = new ArrowType(registry, functionNode.getParameters(),
        null, true);

    // Test isSubtype with a subtype
    JSType subType = new ArrowType(registry, functionNode.getParameters(),
        JSTypeNative.getIntType(), false);
    assertEquals(true, functionType.isSubtype(subType));

    // Test isSubtype with a supertype
    JSType superType = new ArrowType(registry, functionNode.getParameters(),
        JSTypeNative.getObjectType(registry), false);
    assertEquals(true, superType.isSubtype(functionType));

    // Test isSubtype with an unrelated type
    JSType unrelatedType = new FunctionType(registry, new NativeType(registry,
        "Object"), new NativeType(registry, "Object"), false);
    assertEquals(false, functionType.isSubtype(unrelatedType));

    // Test hasEqualParameters with a similar ArrowType
    Node similarFunctionNode = new Node("function(number x) { return x; }");
    JSType similarFunctionType = new ArrowType(registry, similarFunctionNode.getParameters(),
        null, true);
    assertEquals(true, functionType.hasEqualParameters(similarFunctionType));

    // Test hasEqualParameters with a dissimilar ArrowType
    Node dissimilarFunctionNode = new Node("function(int x) { return x + 1; }");
    JSType dissimilarFunctionType = new ArrowType(registry, dissimilarFunctionNode.getParameters(),
        null, true);
    assertEquals(false, functionType.hasEqualParameters(dissimilarFunctionType));
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    Node functionNode = new Node("function(int x, ...int ys) { }");
    JSType functionType = new ArrowType(registry, functionNode.getParameters(),
        JSTypeNative.getIntType(), false);

    // Test isSubtype with a subtype
    JSType subType = new ArrowType(registry, functionNode.getParameters(),
        JSTypeNative.getIntType(), false);
    assertEquals(true, functionType.isSubtype(subType));

    // Test isSubtype with a supertype
    JSType superType = new ArrowType(registry, functionNode.getParameters(),
        JSTypeNative.getObjectType(registry), false);
    assertEquals(true, superType.isSubtype(functionType));

    // Test isSubtype with an unrelated type
    JSType unrelatedType = new FunctionType(registry, new NativeType(registry,
        "Object"), new NativeType(registry, "Object"), false);
    assertEquals(false, functionType.isSubtype(unrelatedType));

    // Test hasEqualParameters with a similar ArrowType
    Node similarFunctionNode = new Node("function(int x, ...int ys) { }");
    JSType similarFunctionType = new ArrowType(registry, similarFunctionNode.getParameters(),
        JSTypeNative.getIntType(), false);
    assertEquals(true, functionType.hasEqualParameters(similarFunctionType));

    // Test hasEqualParameters with a dissimilar ArrowType
    Node dissimilarFunctionNode = new Node("function(string x, ...int ys) { }");
    JSType dissimilarFunctionType = new ArrowType(registry, dissimilarFunctionNode.getParameters(),
        JSTypeNative.getIntType(), false);
    assertEquals(false, functionType.hasEqualParameters(dissimilarFunctionType));
  }
}