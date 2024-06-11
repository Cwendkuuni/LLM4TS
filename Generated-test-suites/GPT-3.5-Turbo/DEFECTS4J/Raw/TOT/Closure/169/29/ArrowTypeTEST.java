import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class ArrowTypeTest {

  private ArrowType arrowType;
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    Node parameters = new Node(Token.PARAM_LIST);
    JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    arrowType = new ArrowType(registry, parameters, returnType);
  }

  @Test
  public void testIsSubtype() {
    // Test when other is not an ArrowType
    assertFalse(arrowType.isSubtype(registry.getNativeType(JSTypeNative.STRING_TYPE)));

    // Test when returnType is not a subtype of that.returnType (covariant)
    JSType returnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
    ArrowType that = new ArrowType(registry, null, returnType);
    assertFalse(arrowType.isSubtype(that));

    // Test when that.paramType[i] is not a subtype of this.paramType[i] (contravariant)
    Node parameters = new Node(Token.PARAM_LIST);
    parameters.addChildToFront(new Node(Token.NAME));
    ArrowType that = new ArrowType(registry, parameters, null);
    assertFalse(arrowType.isSubtype(that));

    // Test when that is missing a required argument
    Node thatParameters = new Node(Token.PARAM_LIST);
    ArrowType that = new ArrowType(registry, parameters, null);
    assertFalse(arrowType.isSubtype(that));
  }

  @Test
  public void testHasEqualParameters() {
    // Test when either of the parameters are null
    Node parameters = new Node(Token.PARAM_LIST);
    ArrowType that = new ArrowType(registry, parameters, null);
    assertTrue(arrowType.hasEqualParameters(that, true));

    // Test when the parameter lists are equal
    JSType paramType = registry.getNativeType(JSTypeNative.STRING_TYPE);
    parameters = new Node(Token.PARAM_LIST);
    parameters.addChildToFront(new Node(Token.NAME).setJSType(paramType));
    that = new ArrowType(registry, parameters, null);
    assertTrue(arrowType.hasEqualParameters(that, true));

    // Test when the parameter lists are not equal
    Node thatParameters = new Node(Token.PARAM_LIST);
    thatParameters.addChildToFront(new Node(Token.NAME));
    that = new ArrowType(registry, thatParameters, null);
    assertFalse(arrowType.hasEqualParameters(that, true));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    // Test when returnType is not equal
    JSType returnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
    ArrowType that = new ArrowType(registry, null, returnType);
    assertFalse(arrowType.checkArrowEquivalenceHelper(that, true));

    // Test when the parameter lists are not equal
    Node parameters = new Node(Token.PARAM_LIST);
    parameters.addChildToFront(new Node(Token.NAME));
    that = new ArrowType(registry, parameters, null);
    assertFalse(arrowType.checkArrowEquivalenceHelper(that, true));

    // Test when the returnType and parameter lists are equal
    parameters = new Node(Token.PARAM_LIST);
    that = new ArrowType(registry, parameters, null);
    assertTrue(arrowType.checkArrowEquivalenceHelper(that, true));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    arrowType.getLeastSupertype(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    arrowType.getGreatestSubtype(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    arrowType.testForEquality(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    arrowType.visit(null);
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    arrowType.resolveInternal(errorReporter, scope);
    // Verify that the resolve method was called on returnType
    assertTrue(((JSType) arrowType.returnType).resolveCalled);
    // Verify that the resolve method was called on each parameter type
    for (Node paramNode = arrowType.parameters.getFirstChild();
         paramNode != null; paramNode = paramNode.getNext()) {
      JSType paramType = paramNode.getJSType();
      assertTrue(((JSType) paramType).resolveCalled);
    }
  }

  @Test
  public void testToStringHelper() {
    String expected = "[ArrowType]";
    assertEquals(expected, arrowType.toStringHelper(true));
  }

  @Test
  public void testHasAnyTemplateInternal() {
    assertFalse(arrowType.hasAnyTemplateInternal());

    // Verify that the hasAnyTemplate method was called on returnType
    assertTrue(((JSType) arrowType.returnType).hasAnyTemplateCalled);

    // Verify that the hasAnyTemplate method was called on each parameter type
    for (Node paramNode = arrowType.parameters.getFirstChild();
         paramNode != null; paramNode = paramNode.getNext()) {
      JSType paramType = paramNode.getJSType();
      assertTrue(((JSType) paramType).hasAnyTemplateCalled);
    }
  }

  private class JSType extends com.google.javascript.rhino.jstype.JSType {
    boolean resolveCalled;
    boolean hasAnyTemplateCalled;

    JSType(JSTypeRegistry registry) {
      super(registry);
    }

    @Override
    public JSType resolve(ErrorReporter t, StaticScope<JSType> scope) {
      resolveCalled = true;
      return null;
    }

    @Override
    public boolean hasAnyTemplate() {
      hasAnyTemplateCalled = true;
      return false;
    }
  }

}