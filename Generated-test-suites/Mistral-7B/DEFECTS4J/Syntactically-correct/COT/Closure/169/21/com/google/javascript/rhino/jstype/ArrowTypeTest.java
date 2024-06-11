import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeFactory.getNativeType;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.Visitor;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setup() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter() {};
  }

  @Test
  public void testArrowTypeConstructor_ParametersAndReturnTypeNotNull() {
    Node parameters = Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER)));
    JSType returnType = getNativeType(JSTypeNative.STRING);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructor_ParametersIsNull() {
    JSType returnType = getNativeType(JSTypeNative.STRING);
    ArrowType arrowType = new ArrowType(registry, null, returnType);

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertNull(arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructor_ReturnTypeIsNull() {
    Node parameters = Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER)));
    ArrowType arrowType = new ArrowType(registry, parameters, null);

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertEquals(parameters, arrowType.parameters);
    assertNull(arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructor_ParametersAndReturnTypeInferred() {
    JSTypeRegistry registry2 = new JSTypeRegistry();
    registry2.setReturnTypeInferenceEnabled(true);
    Node parameters = Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER)));
    ArrowType arrowType = new ArrowType(registry2, parameters, null);

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertEquals(parameters, arrowType.parameters);
    assertNull(arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeConstructor_InvalidParametersType() {
    Node parameters = Node.newObject(Node.newArray(getNodeWithUnknownType()));
    JSType returnType = getNativeType(JSTypeNative.STRING);
    new ArrowType(registry, parameters, returnType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeConstructor_InvalidReturnType() {
    Node parameters = Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER)));
    new ArrowType(registry, parameters, getNodeWithUnknownType());
  }

  @Test
  public void testIsSubtype() {
    Node thisParameters = Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER)));
    Node thatParameters = Node.newObject(Node.newArray(getNativeType(JSTypeNative.STRING)));

    ArrowType arrowTypeThis = new ArrowType(registry, thisParameters, getNativeType(JSTypeNative.NUMBER));
    ArrowType arrowTypeThat = new ArrowType(registry, thatParameters, getNativeType(JSTypeNative.STRING));

    assertFalse(arrowTypeThis.isSubtype(arrowTypeThat));

    Node thisParametersWithVarArgs =
        Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER)));
    thisParametersWithVarArgs.setVarArgs(true);

    ArrowType arrowTypeThisWithVarArgs =
        new ArrowType(registry, thisParametersWithVarArgs, getNativeType(JSTypeNative.NUMBER));

    ArrowType arrowTypeThatWithVarArgs =
        new ArrowType(registry, Node.newObject(Node.newArray(getNativeType(JSTypeNative.STRING))), getNativeType(JSTypeNative.STRING));
    arrowTypeThatWithVarArgs.parameters.setVarArgs(true);

    assertTrue(arrowTypeThisWithVarArgs.isSubtype(arrowTypeThatWithVarArgs));
  }

  @Test
  public void testHasEqualParameters() {
    Node thisParameters = Node.newObject(Node.newArray(
        getNativeType(JSTypeNative.NUMBER),
        getNativeType(JSTypeNative.NUMBER)
    ));
    Node thatParameters = Node.newObject(Node.newArray(
        getNativeType(JSTypeNative.NUMBER),
        Node.newObject(Node.newArray(getNativeType(JSTypeNative.STRING)))
    ));

    ArrowType arrowTypeThis = new ArrowType(registry, thisParameters, getNativeType(JSTypeNative.NUMBER));
    ArrowType arrowTypeThat = new ArrowType(registry, thatParameters, getNativeType(JSTypeNative.STRING));

    assertFalse(arrowTypeThis.hasEqualParameters(arrowTypeThat, false));

    Node thisParametersWithVarArgs = Node.newObject(Node.newArray(
        getNativeType(JSTypeNative.NUMBER),
        Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER)))
    ));
    thisParametersWithVarArgs.setVarArgs(true);

    ArrowType arrowTypeThisWithVarArgs = new ArrowType(registry, thisParametersWithVarArgs, getNativeType(JSTypeNative.NUMBER));
    ArrowType arrowTypeThatWithVarArgs =
        new ArrowType(registry, Node.newObject(Node.newArray(getNativeType(JSTypeNative.STRING))), getNativeType(JSTypeNative.STRING));
    arrowTypeThatWithVarArgs.parameters.setVarArgs(true);

    assertFalse(arrowTypeThisWithVarArgs.hasEqualParameters(arrowTypeThatWithVarArgs, false));

    ArrowType arrowTypeThisWithOptional = new ArrowType(registry,
        Node.newObject(Node.newArray(
            Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER))).setOptionalArg(true),
            getNativeType(JSTypeNative.NUMBER)
        )), getNativeType(JSTypeNative.NUMBER));
    Node thatParametersWithOptional = Node.newObject(Node.newArray(
        getNativeType(JSTypeNative.NUMBER),
        Node.newObject(Node.newArray(getNativeType(JSTypeNative.STRING))).setOptionalArg(true)
    ));

    assertTrue(arrowTypeThisWithOptional.hasEqualParameters(arrowTypeThatWithVarArgs, false));

    ArrowType arrowTypeThisWithOptionalAndVarArgs = new ArrowType(registry,
        Node.newObject(Node.newArray(
            Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER))).setOptionalArg(true),
            Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER)))
                .setVarArgs(true)
        )), getNativeType(JSTypeNative.NUMBER));
    assertFalse(arrowTypeThisWithOptionalAndVarArgs.hasEqualParameters(arrowTypeThatWithVarArgs, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    Node thisParameters = Node.newObject(Node.newArray(
        getNativeType(JSTypeNative.NUMBER),
        getNativeType(JSTypeNative.NUMBER)
    ));
    Node thatParameters = Node.newObject(Node.newArray(
        getNativeType(JSTypeNative.NUMBER),
        Node.newObject(Node.newArray(JSTypeNative.NULL_TYPE))
    ));

    ArrowType arrowTypeThis = new ArrowType(registry, thisParameters, getNativeType(JSTypeNative.NUMBER));
    ArrowType arrowTypeThat = new ArrowType(registry, thatParameters, getNativeType(JSTypeNative.STRING));

    assertFalse(arrowTypeThis.checkArrowEquivalenceHelper(arrowTypeThat, false));

    Node thisParametersWithOptional = Node.newObject(Node.newArray(
        Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER))).setOptionalArg(true),
        getNativeType(JSTypeNative.NUMBER)
    ));
    Node thatParametersWithOptional = Node.newObject(Node.newArray(
        getNativeType(JSTypeNative.NUMBER),
        Node.newObject(Node.newArray(JSTypeNative.NULL_TYPE))
            .setOptionalArg(true)
    ));

    assertTrue(arrowTypeThis.checkArrowEquivalenceHelper(arrowTypeThat, true));
  }

  @Test
  public void testHashCode() {
    Node returnTypeNode = getNativeType(JSTypeNative.STRING);
    ArrowType arrowType1 = new ArrowType(registry, null, returnTypeNode);
    ArrowType arrowType2 = new ArrowType(registry, null, returnTypeNode);

    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());

    Node parameters1 = Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER)));
    Node parameters2 = Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER)));

    ArrowType arrowType3 = new ArrowType(registry, parameters1, getNativeType(JSTypeNative.STRING));
    ArrowType arrowType4 = new ArrowType(registry, parameters2, getNativeType(JSTypeNative.STRING));

    int hashCode = arrowType3.returnType.hashCode() + (1 * 31);
    hashCode += parameters1.hashCode();

    assertEquals(arrowType3.hashCode(), hashCode);

    assertEquals(arrowType3.hashCode(), arrowType4.hashCode());
  }

  @Test
  public void testGetLeastSupertype() {
    throw new UnsupportedOperationException("Method not implemented.");
  }

  @Test
  public void testGetGreatestSubtype() {
    throw new UnsupportedOperationException("Method not implemented.");
  }

  @Test
  public void testToStringHelper() {
    Node parameters = Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER)));
    ArrowType arrowType = new ArrowType(registry, parameters, getNativeType(JSTypeNative.STRING));

    assertEquals("[ArrowType]", arrowType.toStringHelper(false));
  }

  @Test
  public void testToStringHelperForAnnotations() {
    Node parameters = Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER)));
    ArrowType arrowType = new ArrowType(registry, parameters, getNativeType(JSTypeNative.STRING));

    assertEquals("[@type ArrowType]", arrowType.toStringHelper(true));
  }

  @Test
  public void testHasAnyTemplateInternal() {
    Node parameters = Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER)));
    ArrowType arrowType = new ArrowType(registry, parameters, getNativeType(JSTypeNative.STRING));

    assertFalse(arrowType.hasAnyTemplateInternal());

    Node parametersWithTemplate = Node.newObject(Node.newArray(
        getNativeType(JSTypeNative.NUMBER),
        Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER)))
            .setTemplate(Node.newObject(Node.newArray(Node.newString("T"))))
    ));
    ArrowType arrowTypeWithTemplate = new ArrowType(registry, parametersWithTemplate, getNativeType(JSTypeNative.STRING));

    assertTrue(arrowTypeWithTemplate.hasAnyTemplateInternal());
  }

  @Test
  public void testResloveInternal() {
    Node parameters = Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER)));
    ArrowType arrowType = new ArrowType(registry, parameters, getNativeType(JSTypeNative.STRING));

    assertNull(arrowType.resolveInternal(errorReporter, null).returnType);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    Node parameters = Node.newObject(Node.newArray(getNativeType(JSTypeNative.NUMBER)));
    ArrowType arrowType = new ArrowType(registry, parameters, getNativeType(JSTypeNative.STRING));

    assertFalse(arrowType.hasUnknownParamsOrReturn());

    Node parametersWithUnknownType = Node.newObject(Node.newArray(Node.getUnknownType()));
    ArrowType arrowTypeWithUnknownType = new ArrowType(registry, parametersWithUnknownType, getNativeType(JSTypeNative.STRING));

    assertTrue(arrowTypeWithUnknownType.hasUnknownParamsOrReturn());
  }

  private Node getNodeWithUnknownType() {
    return Node.getUnknownType();
  }
}