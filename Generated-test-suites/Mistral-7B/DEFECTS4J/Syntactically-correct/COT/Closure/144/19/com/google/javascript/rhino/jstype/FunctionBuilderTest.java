import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

 private FunctionBuilder functionBuilder;
 private FunctionType functionType;
 private JSTypeRegistry registry;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 functionBuilder = new FunctionBuilder(registry);
 }

 @Test
 public void testFunctionBuilderWithNameAndReturnType() {
 // Basic test case for the withName and withReturnType methods
 functionBuilder.withName("testFunction");
 functionBuilder.withReturnType(registry.createStringType());
 functionType = functionBuilder.build();
 assertEquals("testFunction", functionType.getReferenceName());
 assertEquals(registry.createStringType(), functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithParams() {
 // Basic test case for the withParams method
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 paramBuilder.withType(registry.createNumberType());
 paramBuilder.withName("param1");
 Node paramNode = paramBuilder.build();

 functionBuilder.withName("testFunction");
 functionBuilder.withParams(paramBuilder);
 functionType = functionBuilder.build();
 assertEquals("testFunction", functionType.getReferenceName());
 assertEquals(new ArrowType(registry, paramNode, registry.createVoidType(), false), functionType.getFunctionType());
 }

 @Test(expected = AssertionError.class)
 public void testFunctionBuilderWithNullName() {
 // Test for null name exception
 functionBuilder.withName(null);
 functionType = functionBuilder.build();
 fail("Expected an AssertionError, but no exception was thrown.");
 }

 @Test(expected = AssertionError.class)
 public void testFunctionBuilderWithNullReturnType() {
 // Test for null return type exception
 functionBuilder.withReturnType(null);
 functionType = functionBuilder.build();
 fail("Expected an AssertionError, but no exception was thrown.");
 }

 @Test(expected = AssertionError.class)
 public void testFunctionBuilderWithNullParametersNode() {
 // Test for null parametersNode exception
 functionBuilder.withParamsNode(null);
 functionType = functionBuilder.build();
 fail("Expected an AssertionError, but no exception was thrown.");
 }

 @Test
 public void testFunctionBuilderWithTemplateName() {
 // Basic test case for the withTemplateName method
 functionBuilder.withName("testFunction");
 functionBuilder.withTemplateName("TestFuncTemplate");
 functionType = functionBuilder.build();
 assertEquals("testFunction", functionType.getReferenceName());
 assertEquals("TestFuncTemplate", functionType.getTemplateTypeName());
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 functionBuilder.forConstructor();
 functionType = functionBuilder.build();
 assertTrue(functionType.isConstructor());
 }

 @Test
 public void testFunctionBuilderForNativeType() {
 functionBuilder.forNativeType();
 functionType = functionBuilder.build();
 assertTrue(functionType.isNativeType());
 }
}