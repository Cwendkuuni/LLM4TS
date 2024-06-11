import static com.google.javascript.rhino.NodeFactory.createArrowType;
import static com.google.javascript.rhino.NodeFactory.createIntLiteral;
import static com.google.javascript.rhino.NodeFactory.createNullLiteral;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createNumberLiteral;
import static com.google.javascript.rhino.NodeFactory.createObjectLiteral;
import static com.google.javascript.rhino.NodeFactory.createStringLiteral;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.NodeFactory.createTypeLiteral;
import static org.junit.Assert.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StringType;

import java.util.Arrays;

public class ArrowTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @org.junit.Test
  public void testArrowTypeWithSingleParameterAndSingleReturn() {
    JSType returnType = registry.createPrimitiveType(Number.class);
    Node parametersNode = registry.createParametersWithVarArgs(
        createTypeLiteral(String.class));
    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    JSType expected = new FunctionType(
        createTypeLiteral(String.class),
        createTypeLiteral(Number.class));

    assertTrue(arrowType.isSubtype(expected));
    assertEquals(expected, arrowType.resolve(null, null));
  }

  @org.junit.Test
  public void testArrowTypeWithNoParametersAndSingleReturn() {
    ArrowType arrowType = new ArrowType(registry, null, null);

    Node expected = registry.createPrimitiveType(null);

    assertTrue(arrowType.isSubtype(expected));
    assertEquals(expected, arrowType.resolve(null, null));
  }

  @org.junit.Test
  public void testArrowTypeWithSingleParameterAndNoReturn() {
    JSType parameterType = registry.createPrimitiveType(Number.class);
    Node parametersNode = registry.createParametersWithVarArgs(parameterType);
    ArrowType arrowType = new ArrowType(registry, parametersNode, null);

    assertTrue(arrowType.isSubtype(new FunctionType(
        createTypeLiteral(void.class),
        createTypeLiteral(parameterType))));
    assertEquals(new FunctionType(
        createTypeLiteral(void.class),
        createTypeLiteral(parameterType)),
        arrowType.resolve(null, null));
  }

  @org.junit.Test
  public void testArrowTypeWithMultipleParametersAndSingleReturn() {
    JSType returnType = registry.createPrimitiveType(Number.class);
    Node parametersNode = registry.createParameters(
        createTypeLiteral(String.class),
        createTypeLiteral(Number.class),
        createTypeLiteral(Object.class));
    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    JSType expected = new FunctionType(
        createTypeLiteral(Object.class, String.class, Number.class),
        createTypeLiteral(Number.class));

    assertTrue(arrowType.isSubtype(expected));
    assertEquals(expected, arrowType.resolve(null, null));
  }

  @org.junit.Test
  public void testArrowTypeWithMultipleParametersAndNoReturn() {
    Node parametersNode = registry.createParameters(
        createTypeLiteral(String.class),
        createTypeLiteral(Number.class),
        createTypeLiteral(Object.class));
    ArrowType arrowType = new ArrowType(registry, parametersNode, null);

    JSType expected = new FunctionType(
        createTypeLiteral(void.class),
        createTypeLiteral(String.class, Number.class, Object.class));

    assertTrue(arrowType.isSubtype(expected));
    assertEquals(expected, arrowType.resolve(null, null));
  }

  @org.junit.Test
  public void testArrowTypeWithSingleParameterAndInferredReturn() {
    Node parametersNode = createNode("(s: string) -> ");
    ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);

    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(void.class)));
    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(Number.class)));
    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(Boolean.class)));
    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(String.class)));
    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(Object.class)));
  }

  @org.junit.Test
  public void testArrowTypeWithMultipleParametersAndInferredReturn() {
    Node parametersNode = createNode("(s: string, n: number) -> ");
    ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);

    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(void.class)));
    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(Number.class)));
    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(Boolean.class)));
    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(String.class)));

    JSType stringType = registry.createPrimitiveType(String.class);
    JSType numberType = registry.createPrimitiveType(Number.class);
    FunctionType expected = new FunctionType(
        createTypeLiteral(Object.class, String.class, Number.class),
        createTypeLiteral(Object.class));

    assertEquals(expected, arrowType.resolve(null, null));
  }

  @org.junit.Test
  public void testArrowTypeWithMultipleParametersAndReturnTypeWithTemplate() {
    Node parametersNode = createNode("(s: string, n: number<10>) -> T");
    ArrowType arrowType = new ArrowType(registry, parametersNode, null);

    JSType functionType = new FunctionType(
        createTypeLiteral(Object.class, String.class, JSTypeNative.NUMBER),
        createTypeLiteral(StringType.getInstance().getTemplateParameterType()));

    assertTrue(arrowType.isSubtype(functionType));
  }

  @org.junit.Test
  public void testArrowTypeWithSingleParameterWithOptionalParameterAndInferredReturn() {
    Node parametersNode = createNode("(s?: string) -> ");
    ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);

    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(void.class)));
    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(Number.class)));
    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(Boolean.class)));
    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(String.class)));
    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(Object.class)));

    JSType expected = new FunctionType(
        createTypeLiteral(void.class),
        createTypeLiteral(String.class),
        createTypeLiteral(Boolean.class),
        createTypeLiteral(Number.class),
        createTypeLiteral(Object.class),
        createTypeLiteral(Object.class));

    assertEquals(expected, arrowType.resolve(null, null));
  }

  @org.junit.Test
  public void testArrowTypeWithSingleParameterWithVarArgsAndInferredReturn() {
    Node parametersNode = createNode("(s: string, ...ns: number[]) -> ");
    ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);

    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(void.class)));
    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(Number.class)));
    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(Boolean.class)));
    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(String.class)));
    assertTrue(arrowType.isSubtype(registry.createPrimitiveType(Object.class)));

    FunctionType functionType = new FunctionType(
        createTypeLiteral(Object.class),
        createTypeLiteral(String.class),
        createTypeLiteral(Number[].class));

    assertEquals(functionType, arrowType.resolve(null, null));
  }

  @org.junit.Test
  public void testArrowTypeWithSingleParameterWithOptionalAndVarArgsAndInferredReturn() {
    Node parametersNode = createNode("(s: string, ...ns: number[], i?: number) -> ");
    ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);

    FunctionType functionType = new FunctionType(
        createTypeLiteral(Object.class),
        createTypeLiteral(String.class),
        createTypeLiteral(Number[].class),
        createTypeLiteral(Number.class));

    assertEquals(functionType, arrowType.resolve(null, null));
  }

  @org.junit.Test
  public void testArrowTypeEquivalence() {
    JSType returnType = registry.createPrimitiveType(Number.class);
    Node parametersNode1 = registry.createParametersWithVarArgs(
        createTypeLiteral(String.class));
    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, returnType);

    Node parametersNode2 = registry.createParametersWithVarArgs(
        createTypeLiteral(String.class));
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, returnType);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
    assertEquals(arrowType1, arrowType2);

    Node parametersNode3 = registry.createParameters(
        createTypeLiteral(String.class),
        createStringLiteral("hello"));
    ArrowType arrowType3 = new ArrowType(registry, parametersNode3, returnType);

    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType3.isSubtype(arrowType1));
    assertNotEquals(arrowType1, arrowType3);

    Node parametersNode4 = registry.createParametersWithVarArgs(
        createTypeLiteral(Number.class));
    ArrowType arrowType4 = new ArrowType(registry, parametersNode4, returnType);

    assertFalse(arrowType1.isSubtype(arrowType4));
    assertFalse(arrowType4.isSubtype(arrowType1));
    assertNotEquals(arrowType1, arrowType4);

    Node parametersNode5 = registry.createParameters(
        createTypeLiteral(Boolean.class),
        createNumberLiteral(42));
    ArrowType arrowType5 = new ArrowType(registry, parametersNode5, returnType);

    assertFalse(arrowType1.isSubtype(arrowType5));
    assertFalse(arrowType5.isSubtype(arrowType1));
    assertNotEquals(arrowType1, arrowType5);
  }

  @org.junit.Test
  public void testArrowTypeHashCode() {
    JSType returnType = registry.createPrimitiveType(Number.class);
    Node parametersNode1 = registry.createParametersWithVarArgs(
        createTypeLiteral(String.class));
    ArrowType arrowType1 = new ArrowType(registry, parametersNode1, returnType);

    Node parametersNode2 = registry.createParametersWithVarArgs(
        createTypeLiteral(String.class));
    ArrowType arrowType2 = new ArrowType(registry, parametersNode2, returnType);

    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());

    Node parametersNode3 = registry.createParameters(
        createTypeLiteral(String.class),
        createStringLiteral("hello"));
    ArrowType arrowType3 = new ArrowType(registry, parametersNode3, returnType);

    assertNotEquals(arrowType1.hashCode(), arrowType3.hashCode());

    Node parametersNode4 = registry.createParametersWithVarArgs(
        createTypeLiteral(Number.class));
    ArrowType arrowType4 = new ArrowType(registry, parametersNode4, returnType);

    assertNotEquals(arrowType1.hashCode(), arrowType4.hashCode());

    Node parametersNode5 = registry.createParameters(
        createTypeLiteral(Boolean.class),
        createNumberLiteral(42));
    ArrowType arrowType5 = new ArrowType(registry, parametersNode5, returnType);

    assertNotEquals(arrowType1.hashCode(), arrowType5.hashCode());
  }
}