import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArguments;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createJSType;
import static com.google.javascript.rhino.NodeFactory.createList;
import static com.google.javascript.rhino.NodeFactory.createNativeNode;
import static com.google.javascript.rhino.NodeFactory.createObject;
import static com.google.javascript.rhino.NodeFactory.createProperty;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.NodeFactory.createUndefined;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TestBase;

public class ArrowTypeTest extends TestBase {

  private static Node createArrowTypeNode(JSType returnType, Node... params) {
    return createFunction(createIdentifier("arrowType"), null, createList(params), createJSType(new ArrowType(null, createList(params), returnType, false)));
  }

  @org.junit.Test
  public void testArrowTypeWithNullParametersAndUnknownReturnType() {
    ArrowType arrowType = new ArrowType(getTypeRegistry(), null, null);

    Node arrowTypeNode = createArrowTypeNode(createUndefined(), (Node[]) null);

    assertEquals(arrowType, arrowTypeNode.executeAndReturnValue(context).getValue());
  }

  @org.junit.Test
  public void testArrowTypeWithEmptyParametersAndUnknownReturnType() {
    ArrowType arrowType = new ArrowType(getTypeRegistry(), null, null);

    Node arrowTypeNode = createArrowTypeNode(createUndefined(), new Node[0]);

    assertEquals(arrowType, arrowTypeNode.executeAndReturnValue(context).getValue());
  }

  @org.junit.Test
  public void testArrowTypeWithSingleParameterAndUnknownReturnType() {
    ArrowType arrowType = new ArrowType(getTypeRegistry(), createIdentifier("param"), null);

    Node arrowTypeNode = createArrowTypeNode(createUndefined(), createIdentifier("param"));

    assertEquals(arrowType, arrowTypeNode.executeAndReturnValue(context).getValue());
  }

  @org.junit.Test
  public void testArrowTypeWithSingleRequiredParameterAndUnknownReturnType() {
    ArrowType arrowType = new ArrowType(getTypeRegistry(), createIdentifier("param"), null);

    Node arrowTypeNode = createArrowTypeNode(createUndefined(), createIdentifier("param"));

    JSType expected = new ArrowType(getTypeRegistry(), createIdentifier("param"), null);

    assertEquals(expected, arrowTypeNode.executeAndReturnValue(context).getValue().getType());
  }

  @org.junit.Test
  public void testArrowTypeWithSingleVarargsParameterAndUnknownReturnType() {
    ArrowType arrowType = new ArrowType(getTypeRegistry(), null, null);

    Node arrowTypeNode = createArrowTypeNode(createUndefined(), createIdentifier("param"), null, true);

    JSType expected = new ArrowType(getTypeRegistry(), null, null);

    assertEquals(expected, arrowTypeNode.executeAndReturnValue(context).getValue().getType());
  }

  @org.junit.Test
  public void testArrowTypeWithSingleParameterAndKnownReturnType() {
    JSType returnType = getTypeRegistry().getTypes().NUMBER;

    ArrowType arrowType = new ArrowType(getTypeRegistry(), createIdentifier("param"), returnType);

    Node arrowTypeNode = createArrowTypeNode(returnType, createIdentifier("param"));

    JSType expected = new ArrowType(getTypeRegistry(), createIdentifier("param"), returnType);

    assertEquals(expected, arrowTypeNode.executeAndReturnValue(context).getValue().getType());
  }

  @org.junit.Test
  public void testArrowTypeWithSingleParameterAndInferredReturnType() {
    ArrowType arrowType = new ArrowType(getTypeRegistry(), createIdentifier("param"), null);

    Node arrowTypeNode = createArrowTypeNode(createUndefined(), createIdentifier("param"));

    JSType expected = new ArrowType(getTypeRegistry(), createIdentifier("param"), getTypeRegistry().getTypes().OBJECT);

    arrowTypeNode.setNext(createProperty(createObject(), "returnTypeInferred", createNativeNode(true)));

    JSType actual = (ArrowType) arrowTypeNode.executeAndReturnValue(context).getValue();

    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testArrowTypeEquality() {
    ArrowType arrowType1 = new ArrowType(getTypeRegistry(), createIdentifier("param1"), createJSType(JSTypeNative.INT));
    ArrowType arrowType2 = new ArrowType(getTypeRegistry(), createIdentifier("param1"), createJSType(JSTypeNative.INT));

    assertTrue(arrowType1.isEquivalentTo(arrowType2));
  }

  @org.junit.Test
  public void testArrowTypeInequality() {
    ArrowType arrowType1 = new ArrowType(getTypeRegistry(), createIdentifier("param1"), createJSType(JSTypeNative.INT));
    ArrowType arrowType2 = new ArrowType(getTypeRegistry(), createIdentifier("param1"), createJSType(JSTypeNative.STRING));

    assertFalse(arrowType1.isEquivalentTo(arrowType2));
  }

  @org.junit.Test
  public void testArrowTypeSubtyping() {
    ArrowType arrowType1 = new ArrowType(getTypeRegistry(), createIdentifier("param1"), createJSType(JSTypeNative.INT));
    ArrowType arrowType2 = new ArrowType(getTypeRegistry(), createIdentifier("param1"), createJSType(JSTypeNative.NUMBER));

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @org.junit.Test
  public void testArrowTypeNoSubtyping() {
    ArrowType arrowType1 = new ArrowType(getTypeRegistry(), createIdentifier("param1"), createJSType(JSTypeNative.INT));
    ArrowType arrowType2 = new ArrowType(getTypeRegistry(), createIdentifier("param1"), createJSType(JSTypeNative.STRING));

    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @org.junit.Test
  public void testArrowTypeWithInvalidReturnType() {
    ArrowType arrowType = new ArrowType(getTypeRegistry(), null, null);

    Node arrowTypeNode = createArrowTypeNode(createJSType(JSTypeNative.FUNCTION), new Node[0]);

    try {
      arrowTypeNode.executeAndReturnValue(context);
      fail("Expected TypeError");
    } catch (TypeError e) {
      // expected
    }
  }

  @org.junit.Test
  public void testArrowTypeWithInvalidParameterType() {
    ArrowType arrowType = new ArrowType(getTypeRegistry(), null, null);

    Node arrowTypeNode = createArrowTypeNode(createJSType(JSTypeNative.NUMBER), createJSType(JSTypeNative.STRING));

    try {
      arrowTypeNode.executeAndReturnValue(context);
      fail("Expected TypeError");
    } catch (TypeError e) {
      // expected
    }
  }

  @org.junit.Test
  public void testArrowTypeWithInvalidParameterAndReturnType() {
    ArrowType arrowType = new ArrowType(getTypeRegistry(), null, null);

    Node arrowTypeNode = createArrowTypeNode(createJSType(JSTypeNative.NUMBER), createJSType(JSTypeNative.STRING), null);

    try {
      arrowTypeNode.executeAndReturnValue(context);
      fail("Expected TypeError");
    } catch (TypeError e) {
      // expected
    }
  }
}