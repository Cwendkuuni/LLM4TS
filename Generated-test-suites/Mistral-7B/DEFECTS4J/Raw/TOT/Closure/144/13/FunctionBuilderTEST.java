package com.google.javascript.rhino.jstype.test;

import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {

 @Test
 public void testFunctionBuilderWithSimpleFunction() {
 FunctionBuilder functionBuilder = new FunctionBuilder(JSTypeRegistry.getInstance());
 FunctionType expectedFunctionType = JSType.getFunctionType(new String[]{"arg1"}, new VoidType(), null);

 functionBuilder.withName("testFunction")
 .withSourceNode(null) // can be null for simple functions
 .withParams(new FunctionParamBuilder().withName("arg1").build())
 .withReturnType(new VoidType());
 FunctionType actualFunctionType = functionBuilder.build();

 assertEquals(expectedFunctionType, actualFunctionType);
 }

 @Test
 public void testFunctionBuilderWithConstructor() {
 FunctionBuilder functionBuilder = new FunctionBuilder(JSTypeRegistry.getInstance());
 FunctionType expectedFunctionType = JSType.getConstructorType(new String[]{"arg1"}, new ObjectType(JSType.getReferenceType("String")), null);

 functionBuilder.withName("testConstructor")
 .forConstructor()
 .withParams(new FunctionParamBuilder().withName("arg1").withType(JSType.getReferenceType("String")).build());
 FunctionType actualFunctionType = functionBuilder.build();

 assertEquals(expectedFunctionType, actualFunctionType);
 }

 @Test
 public void testFunctionBuilderWithErrorScenarios() {
 FunctionBuilder functionBuilder = new FunctionBuilder(JSTypeRegistry.getInstance());

 assertNull(functionBuilder.build()); // builder must have name set
 assertNull(functionBuilder.withName(null).build()); // name cannot be null

 functionBuilder.withName("testFunction")
 .withParams(null); // parameters must be set
 assertNull(functionBuilder.build());

 functionBuilder.withName("testFunction")
 .withParams(new FunctionParamBuilder().build()); // functions must have at least one parameter
 assertNull(functionBuilder.build());

 functionBuilder.withName("testFunction")
 .withParams(new FunctionParamBuilder().withName("arg1").build())
 .withReturnType(null); // return type must be set
 FunctionType actualFunctionType = functionBuilder.build();

 assertNotNull(actualFunctionType);
 assertNull(actualFunctionType.getReturnType()); // return type cannot be null
 }
}