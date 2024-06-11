import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.PolymerTestRunner;
import com.google.javascript.rhino.testing.TestNamer;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ArrowType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(PolymerTestRunner.class)
@TestNamer(TestNamer.MethodNameNamer.class)
public class ArrowTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter() {};
  }

  @Test
  public void testArrowTypeWithNoParametersAndUnknownReturnType() {
    Node arrowTypeNode = createNode("(function() {})");
    ArrowType arrowType = new ArrowType(registry, null, null, true);
    arrowType.resolveInternal(errorReporter, new StaticScope<JSType>(null));

    // Basic test case
    assertEquals(arrowType, arrowType.resolve(errorReporter, new StaticScope<JSType>(null)));

    // Edge cases and exception handling

    // Test with no return type
    ArrowType arrowTypeWithoutReturnType = new ArrowType(registry, null, null, true);
    try {
      arrowTypeWithoutReturnType.resolveInternal(errorReporter, new StaticScope<JSType>(null));
      fail("Expected an error when resolving ArrowType without a return type");
    } catch (UnsupportedOperationException e) {
      // Expected
    }

    // Test with an invalid return type
    ArrowType invalidReturnTypeArrowType = new ArrowType(registry, null,
        new JSTypeNative(registry, JSTypeNative.NUMBER, errorReporter), true);
    try {
      invalidReturnTypeArrowType.resolveInternal(errorReporter, new StaticScope<JSType>(null));
      fail("Expected an error when resolving ArrowType with an invalid return type");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Test with unknown parameters
    ArrowType arrowTypeWithUnknownParameters = new ArrowType(registry, null,
        getNativeType(JSTypeNative.UNKNOWN_TYPE), true);
    try {
      arrowTypeWithUnknownParameters.resolveInternal(errorReporter, new StaticScope<JSType>(null));
      fail("Expected an error when resolving ArrowType with unknown parameters");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Test with an invalid parameter
    ArrowType invalidParameterArrowType = new ArrowType(registry,
        Node.createIntLiteral(1), getNativeType(JSTypeNative.NUMBER), true);
    try {
      invalidParameterArrowType.resolveInternal(errorReporter, new StaticScope<JSType>(null));
      fail("Expected an error when resolving ArrowType with an invalid parameter");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Test with a required parameter followed by a varargs parameter
    Node functionNode = Node.createFunctionExpression(
        Node.createIdentifier("function"),
        Lists.newArrayList(Node.createIdentifier("arg1"),
            Node.createIdentifier("arg2")),
        Node.createBlock(Node.createExpressionStatement(
            Node.createReturnStatement(Node.createIdentifier("result")))));
    ArrowType arrowTypeWithRequiredAndVarargsParameters = new ArrowType(
        registry, functionNode, null, false);
    try {
      arrowTypeWithRequiredAndVarargsParameters.resolveInternal(errorReporter, new StaticScope<JSType>(null));
      fail("Expected an error when resolving ArrowType with required and varargs parameters");
    } catch (UnsupportedOperationException e) {
      // Expected
    }

    // Test with a varargs parameter followed by a required parameter
    ArrowType arrowTypeWithVarargsAndRequiredParameters = new ArrowType(
        registry, Node.createFunctionExpression(
            Node.createIdentifier("function"),
            Node.createList(Node.createIdentifier("args"),
                Node.createIdentifier("arg1")),
            Node.createBlock(Node.createExpressionStatement(
                Node.createReturnStatement(Node.createIdentifier("result"))))),
        null, false);
    try {
      arrowTypeWithVarargsAndRequiredParameters.resolveInternal(errorReporter, new StaticScope<JSType>(null));
      fail("Expected an error when resolving ArrowType with varargs and required parameters");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testArrowTypeWithOneParameterAndUnknownReturnType() {
    Node arrowTypeNode = createNode("(function(p) {})");
    ArrowType arrowType = new ArrowType(registry, Node.createList(
        Node.createIdentifier("p")), null, true);
    arrowType.resolveInternal(errorReporter, new StaticScope<JSType>(null));

    // Basic test case
    assertEquals(arrowType, arrowType.resolve(errorReporter, new StaticScope<JSType>(null)));

    // Edge cases and exception handling

    // Test with an invalid return type
    ArrowType invalidReturnTypeArrowType = new ArrowType(registry, Node.createList(
        Node.createIdentifier("p")),
        new JSTypeNative(registry, JSTypeNative.NUMBER, errorReporter), true);
    try {
      invalidReturnTypeArrowType.resolveInternal(errorReporter, new StaticScope<JSType>(null));
      fail("Expected an error when resolving ArrowType with an invalid return type");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Test with an unknown parameter
    ArrowType arrowTypeWithUnknownParameter = new ArrowType(registry, Node.createList(
        Node.createIdentifier("p"), Node.createIdentifier("unknown")), null, true);
    try {
      arrowTypeWithUnknownParameter.resolveInternal(errorReporter, new StaticScope<JSType>(null));
      fail("Expected an error when resolving ArrowType with an unknown parameter");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Test with an invalid parameter
    ArrowType invalidParameterArrowType = new ArrowType(registry, Node.createList(
        Node.createIdentifier("p"), Node.createIntLiteral(1)), null, true);
    try {
      invalidParameterArrowType.resolveInternal(errorReporter, new StaticScope<JSType>(null));
      fail("Expected an error when resolving ArrowType with an invalid parameter");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Test with required parameters followed by varargs parameter
    Node functionNode = Node.createFunctionExpression(
        Node.createIdentifier("function"),
        Lists.newArrayList(Node.createIdentifier("p1"), Node.createIdentifier("p2")),
        Node.createList(Node.createExpressionStatement(
            Node.createReturnStatement(Node.createIdentifier("result"))),
            Node.createIdentifier("args")));
    ArrowType arrowTypeWithRequiredAndVarargsParameters = new ArrowType(
        registry, functionNode, null, false);
    try {
      arrowTypeWithRequiredAndVarargsParameters.resolveInternal(errorReporter, new StaticScope<JSType>(null));
      fail("Expected an error when resolving ArrowType with required and varargs parameters");
    } catch (UnsupportedOperationException e) {
      // Expected
    }

    // Test with varargs parameter followed by required parameter
    ArrowType arrowTypeWithVarargsAndRequiredParameters = new ArrowType(
        registry, Node.createFunctionExpression(
            Node.createIdentifier("function"),
            Node.createList(Node.createIdentifier("args"), Node.createIdentifier("p")),
            Node.createBlock(Node.createExpressionStatement(
                Node.createReturnStatement(Node.createIdentifier("result"))))),
        null, false);
    try {
      arrowTypeWithVarargsAndRequiredParameters.resolveInternal(errorReporter, new StaticScope<JSType>(null));
      fail("Expected an error when resolving ArrowType with varargs and required parameters");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testArrowTypeWithTwoParametersAndUnknownReturnType() {
    Node arrowTypeNode = createNode("(function(p1, p2) {})");
    ArrowType arrowType = new ArrowType(registry, Node.createList(
        Node.createIdentifier("p1"), Node.createIdentifier("p2")), null, true);
    arrowType.resolveInternal(errorReporter, new StaticScope<JSType>(null));

    // Basic test case
    assertEquals(arrowType, arrowType.resolve(errorReporter, new StaticScope<JSType>(null)));

    // Edge cases and exception handling

    // Test with an invalid return type
    ArrowType invalidReturnTypeArrowType = new ArrowType(registry, Node.createList(
        Node.createIdentifier("p1"), Node.createIdentifier("p2")),
        new JSTypeNative(registry, JSTypeNative.NUMBER, errorReporter), true);
    try {
      invalidReturnTypeArrowType.resolveInternal(errorReporter, new StaticScope<JSType>(null));
      fail("Expected an error when resolving ArrowType with an invalid return type");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Test with an unknown parameter
    ArrowType arrowTypeWithUnknownParameter = new ArrowType(registry, Node.createList(
        Node.createIdentifier("p1"), Node.createIdentifier("unknown"), Node.createIdentifier("p2")), null, true);
    try {
      arrowTypeWithUnknownParameter.resolveInternal(errorReporter, new StaticScope<JSType>(null));
      fail("Expected an error when resolving ArrowType with an unknown parameter");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Test with an invalid parameter
    ArrowType invalidParameterArrowType = new ArrowType(registry, Node.createList(
        Node.createIdentifier("p1"), Node.createIdentifier("p2"), Node.createIntLiteral(1)), null, true);
    try {
      invalidParameterArrowType.resolveInternal(errorReporter, new StaticScope<JSType>(null));
      fail("Expected an error when resolving ArrowType with an invalid parameter");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Test with required parameters followed by varargs parameter
    Node functionNode = Node.createFunctionExpression(
        Node.createIdentifier("function"),
        Lists.newArrayList(Node.createIdentifier("p1"), Node.createIdentifier("p2"),
            Node.createIdentifier("args")),
        Node.createBlock(Node.createExpressionStatement(
            Node.createReturnStatement(Node.createIdentifier("result")))));
    ArrowType arrowTypeWithRequiredAndVarargsParameters = new ArrowType(
        registry, functionNode, null, false);
    try {
      arrowTypeWithRequiredAndVarargsParameters.resolveInternal(errorReporter, new StaticScope<JSType>(null));
      fail("Expected an error when resolving ArrowType with required and varargs parameters");
    } catch (UnsupportedOperationException e) {
      // Expected
    }

    // Test with varargs parameter followed by required parameters
    ArrowType arrowTypeWithVarargsAndRequiredParameters = new ArrowType(
        registry, Node.createFunctionExpression(
            Node.createIdentifier("function"),
            Node.createList(Node.createIdentifier("args"),
                Node.createIdentifier("p1"), Node.createIdentifier("p2")),
            Node.createBlock(Node.createExpressionStatement(
                Node.createReturnStatement(Node.createIdentifier("result"))))),
        null, false);
    try {
      arrowTypeWithVarargsAndRequiredParameters.resolveInternal(errorReporter, new StaticScope<JSType>(null));
      fail("Expected an error when resolving ArrowType with varargs and required parameters");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  private Node createNode(String expression) {
    return Node.parse(expression);
  }
}