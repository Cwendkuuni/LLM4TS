import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;

import org.junit.Test;

public class ArrowTypeTest {
  private static final ErrorReporter t = new ErrorReporter();
  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testArrowTypeWithNoParametersAndVoidReturnType() {
    Node arrowTypeNode = new Node("(function() {})");
    ArrowType arrowType = new ArrowType(registry, null, null, false);
    arrowType.resolveInternal(t, new StaticScope<JSType>(JSTypeNative.OBJECT_TYPE));
    arrowType.setJSType(arrowTypeNode.getJSType(registry, arrowType, t));

    ArrowType arrowTypeExpected = new ArrowType(registry, null, null, false);

    assertEquals(arrowTypeExpected, arrowType);
    assertTrue(arrowType.isSubtype(arrowTypeExpected));
    assertTrue(arrowTypeExpected.isSubtype(arrowType));
    assertTrue(arrowType.hasEqualParameters(arrowTypeExpected));
  }

  @Test
  public void testArrowTypeWithOneParameterAndNumberReturnType() {
    Node arrowTypeNode = new Node("(function(x) { return x + 1; })");
    ArrowType arrowType = new ArrowType(registry, new Node("x"), getNumberType(), false);
    arrowType.resolveInternal(t, new StaticScope<JSType>(JSTYPE_FUNCTION_TYPE));
    arrowType.setJSType(arrowTypeNode.getJSType(registry, arrowType, t));

    ArrowType arrowTypeExpected = new ArrowType(registry, new Node("x"), getNumberType(), false);

    assertEquals(arrowTypeExpected, arrowType);
    assertTrue(arrowType.isSubtype(arrowTypeExpected));
    assertTrue(arrowTypeExpected.isSubtype(arrowType));
    assertTrue(arrowType.hasEqualParameters(arrowTypeExpected));
  }

  @Test
  public void testArrowTypeWithOneVariableParameterAndNumberReturnType() {
    Node arrowTypeNode = new Node("(function(x) { var y = x; return y; })");
    ArrowType arrowType = new ArrowType(registry, new Node("x"), getNumberType(), false);
    arrowType.resolveInternal(t, new StaticScope<JSType>(JSTYPE_FUNCTION_TYPE));
    arrowType.setJSType(arrowTypeNode.getJSType(registry, arrowType, t));

    ArrowType arrowTypeExpected = new ArrowType(registry, new Node("x"), getNumberType(), false);

    assertEquals(arrowTypeExpected, arrowType);
    assertTrue(arrowType.isSubtype(arrowTypeExpected));
    assertTrue(arrowTypeExpected.isSubtype(arrowType));
    assertTrue(arrowType.hasEqualParameters(arrowTypeExpected));
  }

  @Test
  public void testArrowTypeWithOneRequiredParameterAndNumberReturnType() {
    Node arrowTypeNode = new Node("(function(x) { return x + 1; })");
    ArrowType arrowType = new ArrowType(registry, new Node("x"), getNumberType(), false);
    arrowType.resolveInternal(t, new StaticScope<JSType>(JSTYPE_FUNCTION_TYPE));
    arrowType.setJSType(arrowTypeNode.getJSType(registry, arrowType, t));

    ArrowType arrowTypeWithNullParameter = new ArrowType(registry, null, getNumberType(), false);

    assertFalse(arrowType.isSubtype(arrowTypeWithNullParameter));
    assertFalse(arrowTypeWithNullParameter.isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeWithOneVariableParameterAndVoidReturnType() {
    Node arrowTypeNode = new Node("(function(x) {})");
    ArrowType arrowType = new ArrowType(registry, new Node("x"), null, false);
    arrowType.resolveInternal(t, new StaticScope<JSType>(JSTYPE_FUNCTION_TYPE));
    arrowType.setJSType(arrowTypeNode.getJSType(registry, arrowType, t));

    ArrowType arrowTypeExpected = new ArrowType(registry, new Node("x"), null, false);

    assertEquals(arrowTypeExpected, arrowType);
    assertTrue(arrowType.isSubtype(arrowTypeExpected));
    assertTrue(arrowTypeExpected.isSubtype(arrowType));
    assertTrue(arrowType.hasEqualParameters(arrowTypeExpected));
  }

  @Test
  public void testArrowTypeWithOneRequiredParameterAndVoidReturnType() {
    Node arrowTypeNode = new Node("(function(x) {})");
    ArrowType arrowType = new ArrowType(registry, new Node("x"), null, false);
    arrowType.resolveInternal(t, new StaticScope<JSType>(JSTYPE_FUNCTION_TYPE));
    arrowType.setJSType(arrowTypeNode.getJSType(registry, arrowType, t));

    ArrowType arrowTypeWithNullParameter = new ArrowType(registry, null, null, false);

    assertFalse(arrowType.isSubtype(arrowTypeWithNullParameter));
    assertFalse(arrowTypeWithNullParameter.isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeWithVariadicParameterAndNumberReturnType() {
    Node arrowTypeNode = new Node("(function(x, ...y) { return x + Array.prototype.sum(y); })");
    ArrowType arrowType = new ArrowType(registry, new Node("x"), getNumberType(), false);
    arrowType.resolveInternal(t, new StaticScope<JSType>(JSTYPE_FUNCTION_TYPE));
    arrowType.setJSType(arrowTypeNode.getJSType(registry, arrowType, t));

    Node variadicParameter = arrowType.parameters.getLastChild();
    assertEquals(JSTypeNative.VAR_ARGS_TYPE, variadicParameter.getJSType());

    ArrowType arrowTypeExpected = new ArrowType(registry, new Node("x"), getNumberType(), false);
    arrowTypeExpected.parameters = new Node("x", new Node("..."), null);

    assertEquals(arrowTypeExpected, arrowType);
    assertTrue(arrowType.isSubtype(arrowTypeExpected));
    assertTrue(arrowTypeExpected.isSubtype(arrowType));
    assertTrue(arrowType.hasEqualParameters(arrowTypeExpected));
  }

  @Test
  public void testArrowTypeWithVariableParametersAndVoidReturnType() {
    Node arrowTypeNode = new Node("(function(x, ...y) {})");
    ArrowType arrowType = new ArrowType(registry, new Node("x"), null, false);
    arrowType.resolveInternal(t, new StaticScope<JSType>(JSTYPE_FUNCTION_TYPE));
    arrowType.setJSType(arrowTypeNode.getJSType(registry, arrowType, t));

    Node variadicParameter = arrowType.parameters.getLastChild();
    assertEquals(JSTypeNative.VAR_ARGS_TYPE, variadicParameter.getJSType());

    ArrowType arrowTypeExpected = new ArrowType(registry, new Node("x"), null, false);
    arrowTypeExpected.parameters = new Node("x", new Node("..."), null);

    assertEquals(arrowTypeExpected, arrowType);
    assertTrue(arrowType.isSubtype(arrowTypeExpected));
    assertTrue(arrowTypeExpected.isSubtype(arrowType));
    assertTrue(arrowType.hasEqualParameters(arrowTypeExpected));
  }

  @Test
  public void testArrowTypeWithOneRequiredParameterAndUnknownReturnType() {
    Node arrowTypeNode = new Node("(function(x) {})");
    ArrowType arrowType = new ArrowType(registry, new Node("x"), null, false);
    arrowType.resolveInternal(t, new StaticScope<JSType>(JSTYPE_FUNCTION_TYPE));
    arrowType.setJSType(arrowTypeNode.getJSType(registry, arrowType, t));

    ArrowType arrowTypeWithUnknownReturnType =
        new ArrowType(registry, new Node("x"), null, true);

    assertTrue(arrowType.isSubtype(arrowTypeWithUnknownReturnType));
    assertTrue(arrowTypeWithUnknownReturnType.isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeWithOneVariableParameterAndUnknownReturnType() {
    Node arrowTypeNode = new Node("(function(x) {})");
    ArrowType arrowType = new ArrowType(registry, new Node("x"), null, false);
    arrowType.resolveInternal(t, new StaticScope<JSType>(JSTYPE_FUNCTION_TYPE));
    arrowType.setJSType(arrowTypeNode.getJSType(registry, arrowType, t));

    ArrowType arrowTypeWithUnknownReturnType =
        new ArrowType(registry, new Node("x"), null, true);

    assertTrue(arrowType.isSubtype(arrowTypeWithUnknownReturnType));
    assertTrue(arrowTypeWithUnknownReturnType.isSubtype(arrowType));
  }
}