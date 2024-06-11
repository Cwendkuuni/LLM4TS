package com.example;

import org.junit.*;
import static org.junit.Assert.*;

public class FunctionBuilderTest {

  @Test
  public void testEmptyConstructorWithNullRegistry() {
    new FunctionBuilder(null);
  }

  @Test
  public void testSettingNameToNullOrEmptyString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.withName(null);
    functionBuilder.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSettingSourceNodeToNull() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.withSourceNode(null);
    functionBuilder.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSettingParametersNodeToNull() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.withParamsNode(null);
    functionBuilder.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSettingReturnTypeToNull() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.withReturnType(null);
    functionBuilder.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSettingReturnTypeWithInvalidJSType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.withReturnType(new ObjectType("InvalidType"));
    functionBuilder.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSettingTypeOfThisToNull() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.withTypeOfThis(null);
    functionBuilder.build();
  }

  @Test
  public void testSettingTemplateTypeNameToNullOrEmptyString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.withTemplateName(null);
    functionBuilder.build();
    functionBuilder.withTemplateName("");
    functionBuilder.build();
  }

  @Test(expected = IllegalStateException.class)
  public void testCallingForConstructorAndForNativeTypeInIncorrectOrder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.forNativeType();
    functionBuilder.forConstructor();
    try {
      functionBuilder.build();
    } catch (Exception e) {
      // expected Exception
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCallingCopyFromOtherFunctionWithNullOrIncorrectFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionType otherType = new FunctionType(registry, null, null, null, null, null, false, false);
    functionBuilder.copyFromOtherFunction(otherType);
    try {
      functionBuilder.build();
    } catch (Exception e) {
      // expected Exception
    }
  }

  @Test
  public void testFunctionBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
  }

  @AfterClass
  public static void tearDownClass() {
    // cleanup code if needed
  }
}