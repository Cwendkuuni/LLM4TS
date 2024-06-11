package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.jstype.JSTypeNative;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;
  private ArrowType arrowType;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter();
    nodeFactory = new NodeFactory(registry, errorReporter);
  }

  @Test
  public void testCreateArrowType() {
    // Test simple ArrowType with null parameters and null returnType
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    assertEquals("ArrowType", arrowType1.toString());
    assertNull(arrowType1.parameters);
    assertNull(arrowType1.returnType);
    assertFalse(arrowType1.returnTypeInferred);

    // Test ArrowType with null parameters and non-null returnType
    Node paramNode = nodeFactory.createIdentifier("param");
    JSType paramType = registry.createType(JSTypeNative.INT);
    ArrowType arrowType2 = new ArrowType(registry, null, paramType);
    assertEquals("ArrowType[param: INT]", arrowType2.toString());
    assertNull(arrowType2.parameters);
    assertEquals(paramType, arrowType2.returnType);
    assertTrue(arrowType2.returnTypeInferred);

    // Test ArrowType with non-null parameters and null returnType
    Node paramNode1 = nodeFactory.createIdentifier("param1");
    Node paramNode2 = nodeFactory.createIdentifier("param2");
    Node[] params = {paramNode1, paramNode2};
    ArrowType arrowType3 = new ArrowType(registry, nodeFactory.createList(params), null);
    assertEquals("ArrowType[param1: ?, param2: ?]", arrowType3.toString());
    assertEquals(nodeFactory.createList(params), arrowType3.parameters);
    assertNull(arrowType3.returnType);
    assertFalse(arrowType3.returnTypeInferred);

    // Test ArrowType with non-null parameters and non-null returnType
    ArrowType arrowType4 = new ArrowType(registry, nodeFactory.createList(params), paramType);
    assertEquals("ArrowType[param1: ?, param2: ?] -> INT", arrowType4.toString());
    assertEquals(nodeFactory.createList(params), arrowType4.parameters);
    assertEquals(paramType, arrowType4.returnType);
    assertFalse(arrowType4.returnTypeInferred);
  }

  @Test
  public void testIsSubtypeArrowType() {
    Node paramNode1 = nodeFactory.createIdentifier("param1");
    Node paramNode2 = nodeFactory.createIdentifier("param2");
    Node[] params = {paramNode1, paramNode2};
    Node returnNode = nodeFactory.createIdentifier("return");
    JSType paramType1 = registry.createType(JSTypeNative.INT);
    JSType paramType2 = registry.createType(JSTypeNative.STRING);
    JSType returnType1 = registry.createType(JSTypeNative.INT);
    JSType returnType2 = registry.createType(JSTypeNative.NUMBER);

    ArrowType arrowType1 = new ArrowType(registry, null, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, nodeFactory.createList(params), returnType2);

    // ArrowType with null parameters and null returnType is a subtype of all other ArrowTypes
    assertTrue(arrowType1.isSubtype(arrowType2));

    // ArrowType with null parameters and non-null returnType is a subtype of ArrowType with null parameters and the same returnType
    assertTrue(arrowType1.isSubtype(new ArrowType(registry, null, returnType1)));

    // ArrowType with non-null parameters and null returnType is a subtype of ArrowType with the same parameters and null returnType
    ArrowType arrowType3 = new ArrowType(registry, nodeFactory.createList(params), null);
    assertTrue(arrowType3.isSubtype(arrowType1));

    // ArrowType with non-null parameters and non-null returnType is a subtype of ArrowType with the same parameters and a subtype returnType
    assertTrue(arrowType4.isSubtype(new ArrowType(registry, nodeFactory.createList(params), paramType1)));

    // ArrowType with the same parameters and a supertype returnType is a subtype of ArrowType with the same parameters and the original returnType
    assertTrue(arrowType4.isSubtype(new ArrowType(registry, nodeFactory.createList(params), returnType1)));

    // ArrowType with the same parameters and a subtype returnType is a subtype of ArrowType with the same parameters and the original returnType
    assertTrue(arrowType4.isSubtype(new ArrowType(registry, nodeFactory.createList(params), returnType2)));

    // ArrowType with different parameters is not a subtype of ArrowType with the same parameters
    ArrowType arrowType5 = new ArrowType(registry, nodeFactory.createList(new Node[]{paramNode1}), null);
    assertFalse(arrowType4.isSubtype(arrowType5));

    // ArrowType with the same parameters but with a different number of optional parameters is not a subtype of ArrowType with the same parameters
    Node optionalParamNode = nodeFactory.createIdentifier("optional");
    Node[] paramsWithOptional = {paramNode1, paramNode2, optionalParamNode};
    ArrowType arrowType6 = new ArrowType(registry, nodeFactory.createList(paramsWithOptional), returnType1);
    assertFalse(arrowType4.isSubtype(arrowType6));
  }

  @Test
  public void testHasEqualParameters() {
    Node paramNode1 = nodeFactory.createIdentifier("param1");
    Node paramNode2 = nodeFactory.createIdentifier("param2");
    Node paramNode3 = nodeFactory.createIdentifier("param3");
    Node[] params1 = {paramNode1, paramNode2};
    Node[] params2 = {paramNode1, paramNode2, paramNode3};

    ArrowType arrowType1 = new ArrowType(registry, nodeFactory.createList(params1), null);
    ArrowType arrowType2 = new ArrowType(registry, nodeFactory.createList(params2), null);

    // ArrowTypes with the same parameters should have equal parameters
    assertTrue(arrowType1.hasEqualParameters(arrowType1, true));
    assertTrue(arrowType1.hasEqualParameters(arrowType1, false));
    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));

    // ArrowTypes with different parameters should not have equal parameters
    ArrowType arrowType3 = new ArrowType(registry, nodeFactory.createList(params2), null);
    assertFalse(arrowType1.hasEqualParameters(arrowType3, true));
    assertFalse(arrowType1.hasEqualParameters(arrowType3, false));

    // ArrowTypes with optional parameters should have equal parameters if tolerateUnknowns is true
    ArrowType arrowType4 = new ArrowType(registry, nodeFactory.createList(params2), null);
    assertTrue(arrowType1.hasEqualParameters(arrowType4, true));
    assertFalse(arrowType1.hasEqualParameters(arrowType4, false));

    // ArrowTypes with unknown parameters should have equal parameters if tolerateUnknowns is true
    ArrowType arrowType5 = new ArrowType(registry, null, null);
    Node unknownParamNode = nodeFactory.createIdentifier("_");
    Node[] params5 = {unknownParamNode};
    ArrowType arrowType6 = new ArrowType(registry, nodeFactory.createList(params5), null);
    assertTrue(arrowType1.hasEqualParameters(arrowType5, true));
    assertTrue(arrowType1.hasEqualParameters(arrowType6, true));
    assertFalse(arrowType1.hasEqualParameters(arrowType5, false));
    assertFalse(arrowType1.hasEqualParameters(arrowType6, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    Node paramNode1 = nodeFactory.createIdentifier("param1");
    Node paramNode2 = nodeFactory.createIdentifier("param2");
    Node returnNode = nodeFactory.createIdentifier("return");
    JSType paramType1 = registry.createType(JSTypeNative.INT);
    JSType paramType2 = registry.createType(JSTypeNative.STRING);
    JSType returnType1 = registry.createType(JSTypeNative.INT);
    JSType returnType2 = registry.createType(JSTypeNative.NUMBER);

    ArrowType arrowType1 = new ArrowType(registry, null, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, nodeFactory.createList(new Node[]{paramNode1}), returnType2);
    ArrowType arrowType3 = new ArrowType(registry, nodeFactory.createList(new Node[]{paramNode2}), returnType1);
    ArrowType arrowType4 = new ArrowType(registry, nodeFactory.createList(new Node[]{paramNode1, paramNode2}), returnType2);

    // ArrowType with null parameters and null returnType should consider ArrowType with the same null parameters and null returnType as equivalent
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType1, true));
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType1, false));

    // ArrowType with null parameters and non-null returnType should not consider ArrowType with non-null parameters and null returnType as equivalent
    assertFalse(arrowType1.checkArrowEquivalenceHelper(new ArrowType(registry, null, null), true));
    assertFalse(arrowType1.checkArrowEquivalenceHelper(new ArrowType(registry, null, null), false));

    // ArrowType with non-null parameters and null returnType should not consider ArrowType with null parameters and non-null returnType as equivalent
    assertFalse(arrowType3.checkArrowEquivalenceHelper(arrowType1, true));
    assertFalse(arrowType3.checkArrowEquivalenceHelper(arrowType1, false));

    // ArrowType with non-null parameters and non-null returnType should consider ArrowType with the same number of parameters and a subtype returnType as equivalent
    assertTrue(arrowType4.checkArrowEquivalenceHelper(arrowType2, true));
    assertTrue(arrowType4.checkArrowEquivalenceHelper(arrowType2, false));

    // ArrowType with the same parameters and a subtype returnType should consider ArrowType with the same parameters and a supertype returnType as equivalent
    assertTrue(arrowType4.checkArrowEquivalenceHelper(arrowType1, true));
    assertTrue(arrowType4.checkArrowEquivalenceHelper(arrowType1, false));

    // ArrowType with the same parameters and a null returnType should consider ArrowType with the same parameters and a null returnType as equivalent
    assertTrue(arrowType4.checkArrowEquivalenceHelper(new ArrowType(registry, nodeFactory.createList(params1), null), true));
    assertTrue(arrowType4.checkArrowEquivalenceHelper(new ArrowType(registry, nodeFactory.createList(params1), null), false));

    // ArrowType with the same parameters and unknown returnType should not consider ArrowType with the same parameters and a null returnType as equivalent
    ArrowType arrowType5 = new ArrowType(registry, nodeFactory.createList(params1), null);
    assertFalse(arrowType4.checkArrowEquivalenceHelper(arrowType5, true));
    assertFalse(arrowType4.checkArrowEquivalenceHelper(arrowType5, false));

    // ArrowType with unknown parameters should not consider ArrowType with known parameters as equivalent
    ArrowType arrowType6 = new ArrowType(registry, null, null);
    assertFalse(arrowType4.checkArrowEquivalenceHelper(arrowType1, true));
    assertFalse(arrowType4.checkArrowEquivalenceHelper(arrowType1, false));
  }

  @Test
  public void testHashCode() {
    Node paramNode1 = nodeFactory.createIdentifier("param1");
    Node paramNode2 = nodeFactory.createIdentifier("param2");
    Node returnNode = nodeFactory.createIdentifier("return");
    JSType paramType1 = registry.createType(JSTypeNative.INT);
    JSType paramType2 = registry.createType(JSTypeNative.STRING);
    JSType returnType1 = registry.createType(JSTypeNative.INT);
    JSType returnType2 = registry.createType(JSTypeNative.NUMBER);

    ArrowType arrowType1 = new ArrowType(registry, null, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, nodeFactory.createList(new Node[]{paramNode1}), returnType2);
    ArrowType arrowType3 = new ArrowType(registry, nodeFactory.createList(new Node[]{paramNode2}), returnType1);
    ArrowType arrowType4 = new ArrowType(registry, nodeFactory.createList(new Node[]{paramNode1, paramNode2}), returnType2);

    // ArrowType with null parameters and null returnType should have the same hashcode as itself
    assertEquals(arrowType1.hashCode(), arrowType1.hashCode());

    // ArrowType with null parameters and non-null returnType should have a different hashcode than itself
    int hashcode1 = arrowType1.hashCode();
    assertNotEquals(hashcode1, arrowType1.hashCode());

    // ArrowType with non-null parameters and null returnType should have a different hashcode than itself
    int hashcode2 = arrowType3.hashCode();
    assertNotEquals(hashcode2, arrowType3.hashCode());

    // ArrowType with non-null parameters and non-null returnType should have a different hashcode than itself
    int hashcode3 = arrowType4.hashCode();
    assertNotEquals(hashcode3, arrowType4.hashCode());

    // ArrowType with the same parameters and a subtype returnType should have the same hashcode as itself
    int hashcode4 = arrowType4.hashCode();
    assertEquals(hashcode4, arrowType4.hashCode());

    // ArrowType with the same parameters and a supertype returnType should have the same hashcode as itself
    int hashcode5 = arrowType2.hashCode();
    assertEquals(hashcode5, arrowType2.hashCode());

    // ArrowType with the same parameters and a null returnType should have the same hashcode as itself
    int hashcode6 = arrowType1.hashCode();
    assertEquals(hashcode6, arrowType1.hashCode());

    // ArrowType with the same parameters and an unknown returnType should have the same hashcode as itself
    ArrowType arrowType5 = new ArrowType(registry, nodeFactory.createList(params1), null);
    assertEquals(arrowType5.hashCode(), arrowType5.hashCode());
  }
}