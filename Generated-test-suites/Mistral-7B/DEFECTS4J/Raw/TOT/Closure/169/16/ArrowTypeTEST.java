import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.TypeChecker;

public class ArrowTypeTest {

  private static JSTypeRegistry registry;

  @BeforeClass
  public static void setupClass() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testArrowTypeWithNoParametersAndUnknownReturnType() {
    Node node = new Node("(function() {})");
    ArrowType arrowType = new ArrowType(registry, null, null);
    assertEquals(arrowType, node.getJSType(registry));
  }

  @Test
  public void testArrowTypeWithOneRequiredParameterAndUnknownReturnType() {
    Node node = new Node("(function(x) {})");
    ArrowType arrowType = new ArrowType(registry, registry.createParameter("x"), null);
    assertEquals(arrowType, node.getJSType(registry));
  }

  @Test
  public void testArrowTypeWithOneRequiredParameterAndKnownReturnType() {
    Node node = new Node("(function(x) { return 1; })");
    JSType returnType = INT;
    ArrowType arrowType = new ArrowType(registry, registry.createParameter("x"), returnType);
    assertEquals(arrowType, node.getJSType(registry));
  }

  @Test
  public void testArrowTypeWithMultipleRequiredParametersAndUnknownReturnType() {
    Node node = new Node("(function(x, y) {})");
    ArrowType arrowType = new ArrowType(
        registry,
        registry.createParameters(registry.createParameter("x"), registry.createParameter("y")),
        null);
    assertEquals(arrowType, node.getJSType(registry));
  }

  @Test
  public void testArrowTypeWithMultipleRequiredParametersAndKnownReturnType() {
    Node node = new Node("(function(x, y) { return 1; })");
    JSType returnType = INT;
    ArrowType arrowType = new ArrowType(
        registry,
        registry.createParameters(registry.createParameter("x"), registry.createParameter("y")),
        returnType);
    assertEquals(arrowType, node.getJSType(registry));
  }

  @Test
  public void testArrowTypeWithVariableArguments() {
    Node node = new Node("(function(a, b, c) {})");
    ArrowType arrowType = new ArrowType(
        registry,
        registry.createParametersWithVarArgs(registry.getVoidType()),
        null);
    assertEquals(arrowType, node.getJSType(registry));
  }

  @Test
  public void testArrowTypeWithOptionalParameter() {
    Node node = new Node("(function(x, y = 0) {})");
    ArrowType arrowType = new ArrowType(
        registry,
        registry.createParameters(
            registry.createParameter("x"),
            registry.createOptionalParameter("y")),
        null);
    assertEquals(arrowType, node.getJSType(registry));
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    Node node = new Node("(function(x) { return x * x; })");
    ArrowType arrowType = new ArrowType(
        registry,
        registry.createParameter("x"),
        null,
        true);
    JSType expected = registry.createFunctionType(
        registry.createParameters(registry.createParameter("x")),
        registry.createParametersWithVarArgs(registry.getNumberType()));
    assertEquals(expected, arrowType.resolveInternal(TypeChecker.getErrorReporter(), null));
  }

  @Test
  public void testArrowTypeWithSubtypeParameters() {
    Node node = new Node("(function(x: number, y: number) { return x + y; })");
    ArrowType arrowType = new ArrowType(
        registry,
        registry.createParameters(
            registry.createParameter("x", registry.getNumberType()),
            registry.createParameter("y", registry.getNumberType())),
        registry.getNumberType());
    assertEquals(arrowType, node.getJSType(registry));
  }

  @Test
  public void testArrowTypeWithParametersSubtypeOfSupertype() {
    ArrowType superTypeArrowType = new ArrowType(
        registry,
        registry.createParameters(registry.createParameter("x", registry.getStringType())),
        registry.getStringType());

    Node node = new Node("(function(x: number) { return x; })");
    ArrowType arrowType = new ArrowType(
        registry,
        registry.createParameter("x"),
        registry.getNumberType());

    assertTrue(superTypeArrowType.isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeWithParametersSupertypeOfSubtype() {
    ArrowType arrowType = new ArrowType(
        registry,
        registry.createParameters(registry.createParameter("x", registry.getNumberType())),
        registry.getStringType());

    Node node = new Node("(function(x: string) { return x; })");
    ArrowType superTypeArrowType = new ArrowType(
        registry,
        registry.createParameter("x"),
        registry.getStringType());

    assertTrue(arrowType.isSubtype(superTypeArrowType));
  }

  @Test
  public void testArrowTypeWithInferredReturnTypeSubtype() {
    Node node1 = new Node("(function(x: number) { return x * x; })");
    ArrowType arrowType1 = new ArrowType(
        registry,
        registry.createParameter("x"),
        null,
        true);

    Node node2 = new Node("(function(x: number) { return x * x * x; })");
    ArrowType arrowType2 = new ArrowType(
        registry,
        registry.createParameter("x"),
        null,
        true);

    JSType expected = registry.createFunctionType(
        registry.createParameters(registry.createParameter("x")),
        registry.createParametersWithVarArgs(registry.getNumberType()));

    assertEquals(expected, arrowType1.resolveInternal(TypeChecker.getErrorReporter(), null));
    assertEquals(expected, arrowType2.resolveInternal(TypeChecker.getErrorReporter(), null));

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeWithMismatchingParameterTypes() {
    ArrowType arrowType = new ArrowType(
        registry,
        registry.createParameters(registry.createParameter("x", registry.getNumberType())),
        registry.getBooleanType());

    Node node = new Node("(function(x: string) { return x; })");

    assertFalse(arrowType.isSubtype(node.getJSType(registry)));
  }
}