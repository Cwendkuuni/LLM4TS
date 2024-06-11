import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.testing.JSTestCase;

public class ArrowTypeTest extends JSTestCase {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Override
  protected ErrorReporter getErrorReporter() {
    return ErrorReporter.NULL;
  }

  @Test
  public void testArrowTypeCreation() {
    Node functionNode = new Node(JSTypeNative.FUNCTION);
    Node arrowTypeNode = new Node(JSTypeNative.ARROW_TYPE);
    Node param1Node = new Node(JSTypeNative.INT_TYPE);
    Node param2Node = new Node(JSTypeNative.STRING_TYPE);
    Node arrowTypeParamsNode = new Node(JSTypeNative.FUNCTION_TYPE, functionNode, new Node(arrowTypeNode, param1Node, param2Node));

    ArrowType arrowType1 = new ArrowType(registry, arrowTypeParamsNode, new JSTypeNative.VoidType(registry), false);

    assertEquals("ArrowType with two parameters: int and String", arrowType1.toString(),
        "ArrowType[int, String]");

    Node arrowTypeNode2 = new Node(JSTypeNative.ARROW_TYPE, null, new JSTypeNative.IntType(registry));
    ArrowType arrowType2 = new ArrowType(registry, null, new JSTypeNative.IntType(registry), true);

    assertEquals("ArrowType with inferred return type: int", arrowType2.toString(),
        "ArrowType[?, int]");
  }

  @Test
  public void testArrowSubtyping() {
    JSType intType = new JSTypeNative.IntType(registry);
    JSType stringType = new JSTypeNative.StringType(registry);

    ArrowType arrowType1 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType2 = new ArrowType(registry, null, stringType, false);

    assertTrue("ArrowType[int] is a subtype of ArrowType[String]", arrowType1.isSubtype(arrowType2));

    ArrowType arrowType3 = new ArrowType(registry, null, new JSTypeNative.VoidType(registry), false);
    ArrowType arrowType4 = new ArrowType(registry, null, intType, false);

    assertFalse("ArrowType[void] is not a subtype of ArrowType[int]", arrowType3.isSubtype(arrowType4));

    ArrowType arrowType5 = new ArrowType(registry, null, intType, true);
    ArrowType arrowType6 = new ArrowType(registry, null, stringType, true);

    assertTrue("ArrowType[int, ?] is a subtype of ArrowType[int, String]", arrowType5.isSubtype(arrowType6));
  }

  @Test
  public void testArrowEquality() {
    JSType intType = new JSTypeNative.IntType(registry);
    JSType stringType = new JSTypeNative.StringType(registry);

    ArrowType arrowType1 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType2 = new ArrowType(registry, null, intType, false);

    assertTrue("ArrowType[int] is equal to ArrowType[int]", arrowType1.isEquivalentTo(arrowType2));

    ArrowType arrowType3 = new ArrowType(registry, null, stringType, false);

    assertFalse("ArrowType[int] is not equal to ArrowType[String]", arrowType1.isEquivalentTo(arrowType3));

    ArrowType arrowType4 = new ArrowType(registry, null, intType, true);
    ArrowType arrowType5 = new ArrowType(registry, null, stringType, true);

    assertFalse("ArrowType[int, ?] is not equal to ArrowType[String, ?]", arrowType4.isEquivalentTo(arrowType5));

    ArrowType arrowType6 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType7 = new ArrowType(registry, new Node(JSTypeNative.FUNCTION_TYPE, null, arrowType6), null, false);

    assertTrue("ArrowType[int] is equal to ArrowType[() -> int]", arrowType6.isEquivalentTo(arrowType7));
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    JSType intType = new JSTypeNative.IntType(registry);

    ArrowType arrowType1 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType2 = new ArrowType(registry, new Node(JSTypeNative.FUNCTION_TYPE, null, arrowType1), null, false);

    assertEquals("ArrowType[int] is equivalent to ArrowType[int, ...]", arrowType1.toString(), arrowType2.toString());
  }

  @Test
  public void testArrowTypeWithEmptyParameterList() {
    Node arrowTypeNode = new Node(JSTypeNative.ARROW_TYPE, null, new JSTypeNative.VoidType(registry));
    ArrowType arrowType = new ArrowType(registry, null, null, false);

    assertEquals("ArrowType[] is equivalent to ArrowType[]", arrowType.toString(), arrowTypeNode.toString());
  }

  @Test
  public void testArrowTypeWithUnknownReturnType() {
    Node arrowTypeNode = new Node(JSTypeNative.ARROW_TYPE, null, new Node(JSTypeNative.UNKNOWN_TYPE));
    ArrowType arrowType = new ArrowType(registry, null, null, false);

    assertEquals("ArrowType[?, ?] is equivalent to ArrowType[]", arrowType.toString(), arrowTypeNode.toString());
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    Node arrowTypeNode = new Node(JSTypeNative.ARROW_TYPE, null, new Node(JSTypeNative.UNKNOWN_TYPE));
    ArrowType arrowType = new ArrowType(registry, null, null, true);

    assertEquals("ArrowType[?, ?] is equivalent to ArrowType[, ?]", arrowType.toString(), arrowTypeNode.toString());
  }

  @Test
  public void testArrowTypeWithInferredReturnTypeAndVarArgs() {
    Node arrowTypeNode = new Node(JSTypeNative.ARROW_TYPE, null, new Node(JSTypeNative.UNKNOWN_TYPE), new Node(JSTypeNative.ELLIPSIS));
    ArrowType arrowType = new ArrowType(registry, null, null, true);

    assertEquals("ArrowType[?, ...] is equivalent to ArrowType[, ...]", arrowType.toString(), arrowTypeNode.toString());
  }

  @Test
  public void testArrowTypeWithEmptyParameterListAndInferredReturnType() {
    Node arrowTypeNode = new Node(JSTypeNative.ARROW_TYPE, null, new Node(JSTypeNative.UNKNOWN_TYPE), null, new Node(JSTypeNative.ELLIPSIS));
    ArrowType arrowType = new ArrowType(registry, null, null, true);

    assertEquals("ArrowType[] is equivalent to ArrowType[, ...]", arrowType.toString(), arrowTypeNode.toString());
  }

  @Test
  public void testArrowTypeWithInferredReturnTypeAndVarArgsAtEnd() {
    Node arrowTypeNode = new Node(JSTypeNative.ARROW_TYPE, null, null, new Node(JSTypeNative.ELLIPSIS), new Node(JSTypeNative.UNKNOWN_TYPE));
    ArrowType arrowType = new ArrowType(registry, null, null, true);

    assertEquals("ArrowType[, ?] is equivalent to ArrowType[, ...]", arrowType.toString(), arrowTypeNode.toString());
  }

  @Test
  public void testArrowTypeWithUnknownParamsAndInferredReturnType() {
    Node arrowTypeNode = new Node(JSTypeNative.ARROW_TYPE, null, new Node(JSTypeNative.UNKNOWN_TYPE), null, new Node(JSTypeNative.ELLIPSIS));
    ArrowType arrowType = new ArrowType(registry, null, null, true);

    assertEquals("ArrowType[?, ...] is equivalent to ArrowType[?, ?]", arrowType.toString(), arrowTypeNode.toString());
  }

  @Test
  public void testArrowTypeWithUnknownParamsAndReturnType() {
    Node arrowTypeNode = new Node(JSTypeNative.ARROW_TYPE, null, null, null, new Node(JSTypeNative.UNKNOWN_TYPE));
    ArrowType arrowType = new ArrowType(registry, null, null, false);

    assertEquals("ArrowType[?, ?] is equivalent to ArrowType[, ?]", arrowType.toString(), arrowTypeNode.toString());
  }

  @Test
  public void testArrowTypeWithUnknownParamsAndReturnTypeAndVarArgs() {
    Node arrowTypeNode = new Node(JSTypeNative.ARROW_TYPE, null, null, null, new Node(JSTypeNative.UNKNOWN_TYPE), new Node(JSTypeNative.ELLIPSIS));
    ArrowType arrowType = new ArrowType(registry, null, null, false);

    assertEquals("ArrowType[?, ...] is equivalent to ArrowType[?, ?]", arrowType.toString(), arrowTypeNode.toString());
  }
}