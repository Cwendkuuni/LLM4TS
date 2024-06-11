import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;

public class ArrowTypeTest {

  @org.junit.Test
  public void testArrowTypeconstructionWithParametersAndReturnType() {
    Node parametersNode = Node.newList();
    JSType parameterType1 = JSTypeNative.createBooleanType();
    Node parameterNode1 = Node.newExpression(parametersNode, "boolean", parameterType1);
    parametersNode.addChild(parameterNode1);

    JSType returnType = JSTypeNative.createNumberType();

    ArrowType arrowType = new ArrowType(null, parametersNode, returnType);

    assertEquals("ArrowType with one parameter and one return type", arrowType.toString(),
        "[ArrowType]\n" +
        "[ArrowType.parameters=\n" +
        "[ParameterType]\n" +
        "[ParameterType.node=Node{type=LIST, children=[Node{type=EXPRESSION, children=[Node{type=NAME, name=\"boolean\"}, Node{type=JSTYPE, children=[Node{type=NAME, name=\"booleanType\"}]}]}]}]\n" +
        "]\n" +
        ", ArrowType.returnType=NumberType]\n");

    assertEquals("ArrowType returnType is correct", arrowType.returnType, returnType);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testArrowTypeconstructionWithNullParameters() {
    JSType returnType = JSTypeNative.createNumberType();

    new ArrowType(null, null, returnType);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testArrowTypeconstructionWithNullReturnType() {
    Node parametersNode = Node.newList();
    JSType parameterType1 = JSTypeNative.createBooleanType();
    Node parameterNode1 = Node.newExpression(parametersNode, "boolean", parameterType1);
    parametersNode.addChild(parameterNode1);

    new ArrowType(null, parametersNode, null);
  }

  @org.junit.Test
  public void testArrowTypeIsSubtype() {
    Node parametersNode1 = Node.newList();
    JSType parameterType1_1 = JSTypeNative.createBooleanType();
    Node parameterNode1_1 = Node.newExpression(parametersNode1, "boolean", parameterType1_1);
    parametersNode1.addChild(parameterNode1_1);

    JSType returnType1 = JSTypeNative.createNumberType();

    ArrowType arrowType1 = new ArrowType(null, parametersNode1, returnType1);

    Node parametersNode2 = Node.newList();
    JSType parameterType2_1 = JSTypeNative.createBooleanType();
    Node parameterNode2_1 = Node.newExpression(parametersNode2, "boolean", parameterType2_1);
    parametersNode2.addChild(parameterNode2_1);

    JSType returnType2 = JSTypeNative.createNumberType();

    ArrowType arrowType2 = new ArrowType(null, parametersNode2, returnType2);

    ArrowType arrowType3 = new ArrowType(null, parametersNode1, JSTypeNative.createVoidType());

    ArrowType arrowType4 = new ArrowType(null, parametersNode2, returnType1);

    ArrowType arrowType5 = new ArrowType(null, parametersNode2, JSTypeNative.createStringType());

    ArrowType arrowType6 = new ArrowType(null, Node.newList(), returnType1);

    ArrowType arrowType7 = new ArrowType(null, parametersNode1, returnType2);

    assertTrue("ArrowType1 is subtype of ArrowType2", arrowType1.isSubtype(arrowType2));
    assertTrue("ArrowType1 is subtype of ArrowType3", arrowType1.isSubtype(arrowType3));
    assertFalse("ArrowType1 is not a subtype of ArrowType4", arrowType1.isSubtype(arrowType4));
    assertFalse("ArrowType1 is not a subtype of ArrowType5", arrowType1.isSubtype(arrowType5));
    assertFalse("ArrowType1 is not a subtype of ArrowType6", arrowType1.isSubtype(arrowType6));
    assertFalse("ArrowType1 is not a subtype of ArrowType7", arrowType1.isSubtype(arrowType7));
  }
}