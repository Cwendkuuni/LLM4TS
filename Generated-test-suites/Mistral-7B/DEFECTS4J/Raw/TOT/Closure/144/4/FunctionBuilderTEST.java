import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.*;
import static com.google.javascript.rhino.jstype.JSTypeFactory.*;
import static com.google.javascript.rhino.jstype.JSTypeRegistry.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;

import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {
  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;

  @Before
  public void setUp() {
    registry = getInitialTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testFunctionBuilderWithSimpleFunction() {
    Node sourceNode = newIdentifier("add");
    Node parametersNode = newArrayType(newIdentifier("number"), 2);
    functionBuilder.withName("add")
        .withSourceNode(sourceNode)
        .withParams(new FunctionParamBuilder().withName("x").withType(intType).build())
        .withParams(new FunctionParamBuilder().withName("y").withType(intType).build())
        .withReturnType(intType)
        .build();

    FunctionType addFunction = functionBuilder.build();

    assertNotNull(addFunction);
    assertEquals("add", addFunction.getReferenceName());
    assertEquals(sourceNode, addFunction.getSource());
    assertArrayEquals(new ArrowType(registry, parametersNode, intType, false).toTypeNodes(), addFunction.getSignature().getParameterTypes());
    assertEquals(intType, addFunction.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithFunctionWithVoidReturnType() {
    Node sourceNode = newIdentifier("printMessage");
    Node parametersNode = newArrayType(newIdentifier("string"), 1);
    functionBuilder.withName("printMessage")
        .withSourceNode(sourceNode)
        .withParams(new FunctionParamBuilder().withName("message").withType(stringType).build())
        .withReturnType(voidType)
        .build();

    FunctionType printMessageFunction = functionBuilder.build();

    assertNotNull(printMessageFunction);
    assertEquals("printMessage", printMessageFunction.getReferenceName());
    assertEquals(sourceNode, printMessageFunction.getSource());
    assertArrayEquals(new ArrowType(registry, parametersNode, voidType, false).toTypeNodes(), printMessageFunction.getSignature().getParameterTypes());
    assertNull(printMessageFunction.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithConstructorFunction() {
    Node sourceNode = newIdentifier("Person");
    Node parametersNode = newArrayType(
        new Identifier("name"), new Identifier("age"),
        new Identifier("city"), new Identifier("country")).toNode();
    functionBuilder.withName("Person")
        .withSourceNode(sourceNode)
        .withParams(new FunctionParamBuilder().withName("name").withType(stringType).build())
        .withParams(new FunctionParamBuilder().withName("age").withType(intType).build())
        .withParams(new FunctionParamBuilder().withName("city").withType(stringType).build())
        .withParams(new FunctionParamBuilder().withName("country").withType(stringType).build())
        .forConstructor()
        .build();

    FunctionType constructorFunction = functionBuilder.build();

    assertNotNull(constructorFunction);
    assertTrue(constructorFunction.isConstructor());
    assertNull(constructorFunction.getReturnType());
    assertEquals(sourceNode, constructorFunction.getSource());
    assertArrayEquals(new ArrowType(registry, parametersNode, objectType, false).toTypeNodes(), constructorFunction.getSignature().getParameterTypes());
  }

  @Test
  public void testFunctionBuilderWithNativeTypeFunction() {
    functionBuilder.forNativeType();

    FunctionType nativeFunction = functionBuilder.build();

    assertNotNull(nativeFunction);
    assertTrue(nativeFunction.isNativeObjectType());
    assertNull(nativeFunction.getReturnType());
    assertNull(nativeFunction.getSource());
    assertNull(nativeFunction.getSignature());
  }
}