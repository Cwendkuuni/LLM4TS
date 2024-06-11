package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {

  @Test
  public void TestFunctionBuilderConstructor() {
    // Test with valid JSTypeRegistry
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    // Add test cases for edge case of null JSTypeRegistry
  }

  @Test
  public void TestWithName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    String name = "testFunction";
    functionBuilder.withName(name);
    assertEquals(name, functionBuilder.getName());
  }

  // Add test cases for other methods and edge cases

  @Test
  public void TestNullJSTypeRegistryInConstructor() {
    assertNull(new FunctionBuilder(null));
  }

  // Add test cases for other methods and edge cases

  @Test
  public void TestBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testFunction";
    Node sourceNode = new Node();
    FunctionParamBuilder params = new FunctionParamBuilder(registry);
    params.withName("paramName").withType(new StringType(registry, "String"));
    Node parametersNode = params.build();
    JSType returnType = new IntType(registry);
    FunctionType expectedFunctionType = new FunctionType(registry, name, sourceNode,
        new ArrowType(registry, parametersNode, returnType, false), null, null, false, false);

    FunctionType functionType = functionBuilder.build();
    assertEquals(expectedFunctionType, functionType);
  }

  // Add test cases for other methods and edge cases

}