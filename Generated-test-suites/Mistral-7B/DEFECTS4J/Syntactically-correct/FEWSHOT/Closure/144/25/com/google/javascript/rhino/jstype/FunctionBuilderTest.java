package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilderWithNameAndReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "myFunction";
    JSType returnType = new IntType(registry);

    FunctionBuilder function = functionBuilder.withName(name).withReturnType(returnType).build();
    FunctionType functionType = (FunctionType) function;

    assertEquals(name, functionType.getReferenceName());
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithParams() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "myFunction";
    JSType returnType = new IntType(registry);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    paramBuilder.withName("param1").withType(new StringType(registry));

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withReturnType(returnType)
        .withParams(paramBuilder.build());

    FunctionType functionType = (FunctionType) function.build();

    assertEquals(name, functionType.getReferenceName());
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(1, functionType.getParameters().size());
    assertEquals("param1", functionType.getParameters().get(0).getName());
    assertEquals(new StringType(registry), functionType.getParameters().get(0).getType());
  }

  @Test
  public void testFunctionBuilderWithParamsNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "myFunction";
    JSType returnType = new IntType(registry);
    Node paramsNode = Node.newCall(registry, "Function", null, Node.newArray(
        Node.newObject(registry, "name", "param1", "type", new StringType(registry))));

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withReturnType(returnType)
        .withParamsNode(paramsNode);

    FunctionType functionType = (FunctionType) function.build();

    assertEquals(name, functionType.getReferenceName());
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(1, functionType.getParameters().size());
    assertEquals("param1", functionType.getParameters().get(0).getName());
    assertEquals(new StringType(registry), functionType.getParameters().get(0).getType());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "myFunction";
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    paramBuilder.withName("param1").withType(new StringType(registry));
    Node paramsNode = paramBuilder.build().build();

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withParamsNode(paramsNode)
        .withInferredReturnType(new IntType(registry));

    FunctionType functionType = (FunctionType) function.build();

    assertEquals(name, functionType.getReferenceName());
    assertTrue(functionType.isInferredReturnType());
    assertEquals(new IntType(registry), functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "myFunction";
    JSType returnType = new IntType(registry);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    paramBuilder.withName("param1").withType(new StringType(registry));
    Node paramsNode = paramBuilder.build().build();
    ObjectType typeOfThis = new ObjectType(registry, "MyObject");

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withReturnType(returnType)
        .withParamsNode(paramsNode)
        .withTypeOfThis(typeOfThis);

    FunctionType functionType = (FunctionType) function.build();

    assertEquals(name, functionType.getReferenceName());
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(1, functionType.getParameters().size());
    assertEquals("param1", functionType.getParameters().get(0).getName());
    assertEquals(new StringType(registry), functionType.getParameters().get(0).getType());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderWithTemplateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "myFunction";
    JSType returnType = new IntType(registry);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    paramBuilder.withName("param1").withType(new StringType(registry));
    Node paramsNode = paramBuilder.build().build();
    String templateTypeName = "MyTemplate";

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withReturnType(returnType)
        .withParamsNode(paramsNode)
        .withTemplateName(templateTypeName);

    FunctionType functionType = (FunctionType) function.build();

    assertEquals(name, functionType.getReferenceName());
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(1, functionType.getParameters().size());
    assertEquals("param1", functionType.getParameters().get(0).getName());
    assertEquals(new StringType(registry), functionType.getParameters().get(0).getType());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "MyConstructor";

    FunctionBuilder function = functionBuilder.forConstructor().withName(name).build();

    FunctionType functionType = (FunctionType) function;

    assertTrue(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "myFunction";

    FunctionBuilder function = functionBuilder.forNativeType().withName(name).build();

    FunctionType functionType = (FunctionType) function;

    assertFalse(functionType.isConstructor());
    assertTrue(functionType.isNativeObjectType());
  }
}