import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilderWithSimpleFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(registry);
    FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder(registry);

    paramBuilder1.withName("param1").withType(registry.anyType());
    paramBuilder2.withName("param2").withType(registry.anyType());

    Node paramsNode = functionBuilder.withParams(paramBuilder1, paramBuilder2).build();

    functionBuilder.withName("functionName")
        .withSourceNode(new Node("function functionName(param1, param2) {}"))
        .withParamsNode(paramsNode)
        .withReturnType(registry.anyType())
        .build();

    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertEquals("functionName", functionType.getReferenceName());
    assertEquals(new ArrowType(registry, paramsNode, registry.anyType(), false), functionType.getFunctionType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(registry);

    paramBuilder1.withName("param1").withType(registry.anyType());

    Node paramsNode = functionBuilder.withParams(paramBuilder1).build();

    functionBuilder.forConstructor()
        .withName("constructorName")
        .withSourceNode(new Node("function constructorName(param1) {}"))
        .withParamsNode(paramsNode)
        .build();

    FunctionType constructorType = functionBuilder.build();

    assertNotNull(constructorType);
    assertEquals("constructorName", constructorType.getReferenceName());
    assertEquals(new ArrowType(registry, paramsNode, registry.anyObjectType(), true), constructorType.getFunctionType());
    assertNull(constructorType.getTemplateTypeName());
    assertTrue(constructorType.isConstructor());
    assertFalse(constructorType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(registry);

    paramBuilder1.withName("param1").withType(registry.anyType());

    Node paramsNode = functionBuilder.withParams(paramBuilder1).build();

    functionBuilder.forNativeType()
        .withName("nativeFunctionName")
        .withSourceNode(new Node("// native function"))
        .withParamsNode(paramsNode)
        .build();

    FunctionType nativeFunctionType = functionBuilder.build();

    assertNotNull(nativeFunctionType);
    assertEquals("nativeFunctionName", nativeFunctionType.getReferenceName());
    assertNull(nativeFunctionType.getFunctionType());
    assertNull(nativeFunctionType.getTypeOfThis());
    assertNull(nativeFunctionType.getTemplateTypeName());
    assertTrue(nativeFunctionType.isConstructor());
    assertTrue(nativeFunctionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(registry);

    paramBuilder1.withName("param1").withType(registry.anyType());

    Node paramsNode = functionBuilder.withParams(paramBuilder1).build();

    functionBuilder.withName("functionName")
        .withSourceNode(new Node("function functionName(param1) {}"))
        .withParamsNode(paramsNode)
        .withReturnType(registry.anyType())
        .withInferredReturnType(registry.anyType())
        .build();

    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertEquals("functionName", functionType.getReferenceName());
    assertEquals(new ArrowType(registry, paramsNode, registry.anyType(), true), functionType.getFunctionType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithTemplateType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(registry);

    paramBuilder1.withName("param1").withType(registry.anyType());

    Node paramsNode = functionBuilder.withParams(paramBuilder1).build();

    functionBuilder.withName("functionName")
        .withSourceNode(new Node("function functionName<T>(param1: T) {}"))
        .withParamsNode(paramsNode)
        .withTemplateTypeName("T")
        .build();

    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertEquals("functionName", functionType.getReferenceName());
    assertNull(functionType.getFunctionType());
    assertNull(functionType.getTypeOfThis());
    assertEquals("T", functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }
}