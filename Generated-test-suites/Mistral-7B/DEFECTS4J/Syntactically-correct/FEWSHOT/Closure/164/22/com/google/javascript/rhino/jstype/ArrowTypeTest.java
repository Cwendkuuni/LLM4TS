package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.testing.AssertType;
import com.google.javascript.rhino.testing.AssertTypeHelper;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private static final JSTypeRegistry REGISTRY = new JSTypeRegistry();
  private static final ErrorReporter ERROR_REPORTER = new ErrorReporter();

  @Before
  public void setUp() {
    REGISTRY.addTypeFactory(new ArrowTypeFactory(REGISTRY));
  }

  @Test
  public void testArrowTypeCreation() {
    Node parameters = new NodeList();
    Node param1 = new Node("Identifier", "x");
    Node param2 = new NodeList();
    Node param3 = new Node("Identifier", "y");
    param2.appendChild(param3);
    parameters.appendChild(param1);
    parameters.appendChild(param2);

    JSType intType = REGISTRY.getIntType();
    JSType stringType = REGISTRY.getStringType();
    JSType voidType = REGISTRY.getVoidType();

    JSType arrowType1 =
        new ArrowType(REGISTRY, parameters, intType, false);
    JSType arrowType2 =
        new ArrowType(REGISTRY, parameters, stringType, true);
    JSType arrowType3 = new ArrowType(REGISTRY, null, voidType, false);

    AssertType.assertEquals("function(x: any, y: any)", arrowType1.toString());
    AssertType.assertEquals("function(x: any, y: any): string", arrowType2.toString());
    AssertType.assertEquals("function(): void", arrowType3.toString());
  }

  @Test
  public void testArrowTypeIsSubtype() {
    Node parameters1 = new NodeList();
    Node param1 = new Node("Identifier", "x");
    Node param2 = new NodeList();
    Node param3 = new Node("Identifier", "y");
    param2.appendChild(param3);
    parameters1.appendChild(param1);
    parameters1.appendChild(param2);

    Node parameters2 = new NodeList();
    Node param4 = new Node("Identifier", "x");
    JSType intType = REGISTRY.getIntType();
    JSType stringType = REGISTRY.getStringType();
    JSType arrowType1 =
        new ArrowType(REGISTRY, parameters1, intType, false);
    JSType arrowType2 =
        new ArrowType(REGISTRY, parameters2, stringType, false);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));

    Node varArgParam = new Node("Identifier", "z");
    Node varArgs = new Node("Varargs", varArgParam);
    parameters1.setFirstChild(parameters1.getFirstChild().getNext());
    parameters1.insertBefore(varArgs, parameters1.getFirstChild());

    Node intArrayType = new ArrayType(REGISTRY, intType, false);
    JSType arrowType3 =
        new ArrowType(REGISTRY, parameters1, intArrayType, false);

    assertTrue(arrowType1.isSubtype(arrowType3));
    assertTrue(arrowType3.isSubtype(arrowType1));

    JSType arrowType4 =
        new ArrowType(REGISTRY, parameters2, intType, false);

    assertFalse(arrowType1.isSubtype(arrowType4));
    assertFalse(arrowType4.isSubtype(arrowType1));

    JSType arrowType5 =
        new ArrowType(REGISTRY, null, voidType, false);

    assertTrue(arrowType1.isSubtype(arrowType5));
    assertTrue(arrowType5.isSubtype(arrowType1));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    Node parameters1 = new NodeList();
    Node param1 = new Node("Identifier", "x");
    Node param2 = new NodeList();
    Node param3 = new Node("Identifier", "y");
    param2.appendChild(param3);
    parameters1.appendChild(param1);
    parameters1.appendChild(param2);

    Node parameters2 = new NodeList();
    Node param4 = new Node("Identifier", "x");
    Node param5 = new NodeList();
    Node param6 = new Node("Identifier", "y");
    param5.appendChild(param6);
    parameters2.appendChild(param4);
    parameters2.appendChild(param5);

    ArrowType arrowType1 =
        new ArrowType(REGISTRY, parameters1, intType, false);
    ArrowType arrowType2 =
        new ArrowType(REGISTRY, parameters2, intType, false);

    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertTrue(arrowType2.hasEqualParameters(arrowType1));
  }

  @Test
  public void testArrowTypeEquals() {
    Node parameters1 = new NodeList();
    Node param1 = new Node("Identifier", "x");
    Node param2 = new NodeList();
    Node param3 = new Node("Identifier", "y");
    param2.appendChild(param3);
    parameters1.appendChild(param1);
    parameters1.appendChild(param2);

    Node parameters2 = new NodeList();
    Node param4 = new Node("Identifier", "x");
    Node param5 = new NodeList();
    Node param6 = new Node("Identifier", "y");
    param5.appendChild(param6);
    parameters2.appendChild(param4);
    parameters2.appendChild(param5);

    ArrowType arrowType1 =
        new ArrowType(REGISTRY, parameters1, intType, false);
    ArrowType arrowType2 =
        new ArrowType(REGISTRY, parameters2, intType, false);

    assertTrue(arrowType1.equals(arrowType1));
    assertTrue(arrowType1.equals(arrowType2));
    assertTrue(arrowType2.equals(arrowType1));
    assertTrue(arrowType2.equals(arrowType2));

    ArrowType arrowType3 = new ArrowType(REGISTRY, null, voidType, false);
    assertFalse(arrowType1.equals(arrowType3));
    assertFalse(arrowType3.equals(arrowType1));
    assertFalse(arrowType3.equals(arrowType3));
  }

  @Test
  public void testArrowTypeHashCode() {
    Node parameters1 = new NodeList();
    Node param1 = new Node("Identifier", "x");
    Node param2 = new NodeList();
    Node param3 = new Node("Identifier", "y");
    param2.appendChild(param3);
    parameters1.appendChild(param1);
    parameters1.appendChild(param2);

    Node parameters2 = new NodeList();
    Node param4 = new Node("Identifier", "x");
    Node param5 = new NodeList();
    Node param6 = new Node("Identifier", "y");
    param5.appendChild(param6);
    parameters2.appendChild(param4);
    parameters2.appendChild(param5);

    ArrowType arrowType1 =
        new ArrowType(REGISTRY, parameters1, intType, false);
    ArrowType arrowType2 =
        new ArrowType(REGISTRY, parameters2, intType, false);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertEquals(hashCode1, hashCode2);

    ArrowType arrowType3 = new ArrowType(REGISTRY, null, voidType, false);
    assertNotEquals(hashCode1, arrowType3.hashCode());
  }

  @Test
  public void testArrowTypeToString() {
    Node parameters1 = new NodeList();
    Node param1 = new Node("Identifier", "x");
    Node param2 = new NodeList();
    Node param3 = new Node("Identifier", "y");
    param2.appendChild(param3);
    parameters1.appendChild(param1);
    parameters1.appendChild(param2);

    JSType intType = REGISTRY.getIntType();
    JSType arrowType = new ArrowType(REGISTRY, parameters1, intType, false);

    assertEquals("function(x: any, y: any): number", arrowType.toString());
  }

  @Test
  public void testArrowTypeToStringForAnnotations() {
    Node parameters1 = new NodeList();
    Node param1 = new Node("Identifier", "x");
    Node param2 = new NodeList();
    Node param3 = new Node("Identifier", "y");
    param2.appendChild(param3);
    parameters1.appendChild(param1);
    parameters1.appendChild(param2);

    JSType intType = REGISTRY.getIntType();
    JSType arrowType = new ArrowType(REGISTRY, parameters1, intType, false);

    assertEquals("function(x: any, y: any)", arrowType.toStringHelper(true));
  }
}

class ArrowTypeFactory implements JSTypeFactory {
  private final JSTypeRegistry registry;

  ArrowTypeFactory(JSTypeRegistry registry) {
    this.registry = registry;
  }

  @Override
  public JSType createArrowType(Node parameters, JSType returnType) {
    return new ArrowType(registry, parameters, returnType, false);
  }

  @Override
  public JSType createArrowType(Node parameters, JSType returnType,
      boolean returnTypeInferred) {
    return new ArrowType(registry, parameters, returnType, returnTypeInferred);
  }
}