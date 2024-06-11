import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter();
  }

  @Test
  public void testArrowTypeWithNullParametersAndUnknownReturnType() {
    Node parameters = Node.NULL();
    JSType returnType = registry.getUnknownType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    // Test isSubtype
    ArrowType arrowType1 = new ArrowType(registry, Node.NULL(), UNKNOWN_TYPE);
    assertTrue(arrowType1.isSubtype(arrowType));

    JSType stringType = registry.getStringType();
    ArrowType arrowType2 = new ArrowType(registry, Node.NULL(), stringType);
    assertFalse(arrowType1.isSubtype(arrowType2));

    // Test hasEqualParameters
    ArrowType arrowType3 = new ArrowType(registry, Node.NULL(), UNKNOWN_TYPE);
    assertTrue(arrowType.hasEqualParameters(arrowType3, true));

    // Test checkArrowEquivalenceHelper
    ArrowType arrowType4 = new ArrowType(registry, Node.NULL(), stringType, true);
    assertFalse(arrowType.checkArrowEquivalenceHelper(arrowType4, true));

    // Test hashCode
    assertEquals(arrowType.hashCode(), arrowType.hashCode());

    // Test getLeastSupertype and getGreatestSubtype (unsupported)
    // ArrowType does not implement these methods

    // Test visit
    // ArrowType does not implement this method

    // Test getPossibleToBooleanOutcomes
    assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());

    // Test resolveInternal
    JSType resolvedArrowType = arrowType.resolveInternal(errorReporter, new StaticScope<>(registry));
    assertEquals(arrowType, resolvedArrowType);

    // Test hasAnyTemplateInternal
    assertFalse(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testArrowTypeWithParametersAndKnownReturnType() {
    Node intTypeNode = registry.getIntType();
    Node stringTypeNode = registry.getStringType();
    Node parameters = Node.newList(createNode("number"), createNode("string"));
    JSType returnType = stringTypeNode;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    // Test isSubtype
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(createNode("number")), returnType);
    assertTrue(arrowType1.isSubtype(arrowType));

    ArrowType arrowType2 = new ArrowType(registry, Node.newList(createNode("number"), createNode("number")), returnType);
    assertFalse(arrowType1.isSubtype(arrowType2));

    // Test hasEqualParameters
    ArrowType arrowType3 = new ArrowType(registry, Node.newList(createNode("number"), createNode("string")), returnType);
    assertTrue(arrowType.hasEqualParameters(arrowType3, true));

    ArrowType arrowType4 = new ArrowType(registry, Node.newList(createNode("number"), Node.NULL()), returnType);
    assertFalse(arrowType.hasEqualParameters(arrowType4, true));

    // Test checkArrowEquivalenceHelper
    ArrowType arrowType5 = new ArrowType(registry, Node.newList(createNode("number"), Node.NULL()), UNKNOWN_TYPE);
    assertFalse(arrowType.checkArrowEquivalenceHelper(arrowType5, true));

    // Test hashCode
    ArrowType arrowType6 = new ArrowType(registry, Node.newList(createNode("number"), Node.NULL()), UNKNOWN_TYPE);
    assertNotEquals(arrowType.hashCode(), arrowType6.hashCode());

    // Test getLeastSupertype and getGreatestSubtype (unsupported)
    // ArrowType does not implement these methods

    // Test visit
    // ArrowType does not implement this method

    // Test getPossibleToBooleanOutcomes
    assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());

    // Test resolveInternal
    JSType resolvedArrowType = arrowType.resolveInternal(errorReporter, new StaticScope<>(registry));
    assertEquals(arrowType, resolvedArrowType);

    // Test hasAnyTemplateInternal
    assertFalse(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testArrowTypeWithOptionalParameters() {
    Node intTypeNode = registry.getIntType();
    Node stringTypeNode = registry.getStringType();
    Node parameters = Node.newList(createNode("number"), Node.createOptionalArgument(Node.createIdentifier("opt")));
    JSType returnType = stringTypeNode;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    // Test isSubtype
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(createNode("number")), returnType);
    assertTrue(arrowType1.isSubtype(arrowType));

    ArrowType arrowType2 = new ArrowType(registry, Node.newList(createNode("number"), Node.createIdentifier("opt")), returnType);
    assertTrue(arrowType2.isSubtype(arrowType));

    ArrowType arrowType3 = new ArrowType(registry, Node.newList(Node.NULL(), Node.NULL()), returnType);
    assertTrue(arrowType3.isSubtype(arrowType));

    ArrowType arrowType4 = new ArrowType(registry, Node.newList(createNode("number"), Node.createIdentifier("opt"), Node.createIdentifier("opt2")), returnType);
    assertFalse(arrowType.isSubtype(arrowType4));

    // Test hasEqualParameters
    ArrowType arrowType5 = new ArrowType(registry, Node.newList(createNode("number"), Node.createOptionalArgument(Node.createIdentifier("opt"))), returnType);
    assertTrue(arrowType.hasEqualParameters(arrowType5, true));

    ArrowType arrowType6 = new ArrowType(registry, Node.newList(Node.newList(createNode("number"), Node.createOptionalArgument(Node.createIdentifier("opt"))), Node.createIdentifier("opt2")), returnType);
    assertFalse(arrowType.hasEqualParameters(arrowType6, true));

    // Test checkArrowEquivalenceHelper
    ArrowType arrowType7 = new ArrowType(registry, Node.newList(Node.newList(Node.NULL(), Node.createOptionalArgument(Node.createIdentifier("opt"))), Node.NULL()), UNKNOWN_TYPE);
    assertFalse(arrowType.checkArrowEquivalenceHelper(arrowType7, true));

    // Test hashCode
    ArrowType arrowType8 = new ArrowType(registry, Node.newList(Node.createOptionalArgument(Node.createIdentifier("opt")), Node.NULL()), UNKNOWN_TYPE);
    assertNotEquals(arrowType.hashCode(), arrowType8.hashCode());

    // Test getLeastSupertype and getGreatestSubtype (unsupported)
    // ArrowType does not implement these methods

    // Test visit
    // ArrowType does not implement this method

    // Test getPossibleToBooleanOutcomes
    assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());

    // Test resolveInternal
    JSType resolvedArrowType = arrowType.resolveInternal(errorReporter, new StaticScope<>(registry));
    assertEquals(arrowType, resolvedArrowType);

    // Test hasAnyTemplateInternal
    assertFalse(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testArrowTypeWithVariadicParameters() {
    Node intTypeNode = registry.getIntType();
    Node stringTypeNode = registry.getStringType();
    Node parameters = Node.newList(Node.createIdentifier("arg1"), Node.createVarArg(Node.createIdentifier("args")));
    JSType returnType = stringTypeNode;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    // Test isSubtype
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.createIdentifier("arg1"), Node.createIdentifier("arg2")), returnType);
    assertFalse(arrowType1.isSubtype(arrowType));

    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.createIdentifier("arg1"), Node.NULL(), Node.createIdentifier("args")), returnType);
    assertTrue(arrowType2.isSubtype(arrowType));

    ArrowType arrowType3 = new ArrowType(registry, Node.newList(Node.createIdentifier("arg1"), Node.createList(Node.createIdentifier("arg2"), Node.createIdentifier("arg3")), Node.createIdentifier("args")), returnType);
    assertFalse(arrowType.isSubtype(arrowType3));

    // Test hasEqualParameters
    ArrowType arrowType4 = new ArrowType(registry, Node.newList(Node.createIdentifier("arg1"), Node.createVarArg(Node.createIdentifier("args"))), returnType);
    assertTrue(arrowType.hasEqualParameters(arrowType4, true));

    ArrowType arrowType5 = new ArrowType(registry, Node.newList(Node.newList(Node.createIdentifier("arg1"), Node.createVarArg(Node.createIdentifier("args"))), Node.createIdentifier("opt")), returnType);
    assertFalse(arrowType.hasEqualParameters(arrowType5, true));

    // Test checkArrowEquivalenceHelper
    ArrowType arrowType6 = new ArrowType(registry, Node.newList(Node.newList(Node.NULL(), Node.createVarArg(Node.createIdentifier("args"))), Node.NULL()), UNKNOWN_TYPE);
    assertFalse(arrowType.checkArrowEquivalenceHelper(arrowType6, true));

    // Test hashCode
    ArrowType arrowType7 = new ArrowType(registry, Node.newList(Node.createIdentifier("arg1"), Node.createVarArg(Node.createIdentifier("args"))), UNKNOWN_TYPE);
    assertNotEquals(arrowType.hashCode(), arrowType7.hashCode());

    // Test getLeastSupertype and getGreatestSubtype (unsupported)
    // ArrowType does not implement these methods

    // Test visit
    // ArrowType does not implement this method

    // Test getPossibleToBooleanOutcomes
    assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());

    // Test resolveInternal
    JSType resolvedArrowType = arrowType.resolveInternal(errorReporter, new StaticScope<>(registry));
    assertEquals(arrowType, resolvedArrowType);

    // Test hasAnyTemplateInternal
    assertFalse(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    Node intTypeNode = registry.getIntType();
    Node parameters = Node.newList(Node.createIdentifier("arg1"), Node.createIdentifier("arg2"));
    ArrowType arrowType = new ArrowType(registry, parameters, null, true);

    // Test isSubtype
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.createIdentifier("arg1"), Node.createIdentifier("arg2")), intTypeNode);
    assertTrue(arrowType1.isSubtype(arrowType));

    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.createIdentifier("arg1"), Node.createIdentifier("arg2")), registry.getBooleanType());
    assertFalse(arrowType1.isSubtype(arrowType2));

    // Test hasEqualParameters
    ArrowType arrowType3 = new ArrowType(registry, Node.newList(Node.createIdentifier("arg1"), Node.createIdentifier("arg2")), null, true);
    assertTrue(arrowType.hasEqualParameters(arrowType3, true));

    // Test checkArrowEquivalenceHelper
    ArrowType arrowType4 = new ArrowType(registry, Node.newList(Node.createIdentifier("arg1"), Node.createIdentifier("arg2")), null, false);
    assertFalse(arrowType.checkArrowEquivalenceHelper(arrowType4, true));

    // Test hashCode
    ArrowType arrowType5 = new ArrowType(registry, Node.newList(Node.createIdentifier("arg1"), Node.createIdentifier("arg2")), null, true);
    assertEquals(arrowType.hashCode(), arrowType5.hashCode());

    // Test getLeastSupertype and getGreatestSubtype (unsupported)
    // ArrowType does not implement these methods

    // Test visit
    // ArrowType does not implement this method

    // Test getPossibleToBooleanOutcomes
    assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());

    // Test resolveInternal
    JSType resolvedArrowType = arrowType.resolveInternal(errorReporter, new StaticScope<>(registry));
    assertEquals(intTypeNode, resolvedArrowType.getReturnType());

    // Test hasAnyTemplateInternal
    assertFalse(arrowType.hasAnyTemplateInternal());
  }
}