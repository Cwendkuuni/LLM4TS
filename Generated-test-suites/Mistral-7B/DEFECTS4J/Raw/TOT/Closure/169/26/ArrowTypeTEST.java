import static com.google.javascript.rhino.JSType.*;
import static org.junit.Assert.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.errorReporter.ErrorReporter;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeWithNullParametersAndUnknownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>(registry);

    Node parameters = null;
    JSType returnType = getNativeType(UNKNOWN_TYPE);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    arrowType.resolveInternal(t, scope);

    ArrowType resolvedArrowType = (ArrowType) arrowType.getLeastSupertype(arrowType);

    JSType expected = new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE));

    assertEquals(expected, resolvedArrowType);
  }

  @Test
  public void testArrowTypeWithEmptyParametersAndKnownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>(registry);

    Node parameters = Node.nullLiteral();
    JSType returnType = getNativeType(INT);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    arrowType.resolveInternal(t, scope);

    ArrowType resolvedArrowType = (ArrowType) arrowType.getLeastSupertype(arrowType);

    JSType expected = new ArrowType(registry, null, INT);

    assertEquals(expected, resolvedArrowType);
  }

  @Test
  public void testArrowTypeWithParameterAndKnownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>(registry);

    Node parameters = new Node(JSTypeNative.INT_TYPE_NODE);
    JSType returnType = getNativeType(INT);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    arrowType.resolveInternal(t, scope);

    ArrowType resolvedArrowType = (ArrowType) arrowType.getLeastSupertype(arrowType);

    JSType expected = new ArrowType(registry, new Node(JSTypeNative.INT_TYPE_NODE), INT);

    assertEquals(expected, resolvedArrowType);
  }

  @Test
  public void testArrowTypeWithVarArgsParameterAndKnownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>(registry);

    Node parameters = new Node(JSTypeNative.INT_TYPE_NODE,
        new Node(JSTypeNative.VAR_ARGS_NODE));
    JSType returnType = getNativeType(INT);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    arrowType.resolveInternal(t, scope);

    ArrowType resolvedArrowType = (ArrowType) arrowType.getLeastSupertype(arrowType);

    JSType expected = new ArrowType(registry,
        new Node(JSTypeNative.INT_TYPE_NODE),
        new Node(JSTypeNative.INT_TYPE_NODE, new Node(JSTypeNative.VAR_ARGS_NODE)),
        INT);

    assertEquals(expected, resolvedArrowType);
  }

  @Test
  public void testArrowTypeWithOptionalParameterAndKnownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>(registry);

    Node parameters = new Node(
        new Node(JSTypeNative.INT_TYPE_NODE),
        new Node(JSTypeNative.OPT_BOOL_TYPE_NODE));
    JSType returnType = getNativeType(INT);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    arrowType.resolveInternal(t, scope);

    ArrowType resolvedArrowType = (ArrowType) arrowType.getLeastSupertype(arrowType);

    JSType expected = new ArrowType(registry,
        new Node(JSTypeNative.INT_TYPE_NODE),
        new Node(JSTypeNative.INT_TYPE_NODE),
        INT);

    assertEquals(expected, resolvedArrowType);
  }

  @Test
  public void testArrowTypeWithFunctionReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>(registry);

    Node parameters = Node.nullLiteral();
    JSType returnType = new FunctionType(
        registry,
        new Node(JSTypeNative.INT_TYPE_NODE),
        new Node(JSTypeNative.INT_TYPE_NODE));

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    arrowType.resolveInternal(t, scope);

    ArrowType resolvedArrowType = (ArrowType) arrowType.getLeastSupertype(arrowType);

    JSType expected = new ArrowType(registry,
        Node.nullLiteral(),
        new FunctionType(
            registry,
            Node.nullLiteral(),
            new Node(JSTypeNative.INT_TYPE_NODE)));

    assertEquals(expected, resolvedArrowType);
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>(registry);

    Node parameters = new Node(JSTypeNative.INT_TYPE_NODE);
    JSType returnType = null;

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, true);

    arrowType.resolveInternal(t, scope);

    ArrowType resolvedArrowType = (ArrowType) arrowType.getLeastSupertype(arrowType);

    JSType expected = new ArrowType(registry,
        new Node(JSTypeNative.INT_TYPE_NODE),
        getNativeType(UNKNOWN_TYPE));

    assertEquals(expected, resolvedArrowType);
  }
}