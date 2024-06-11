import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;

import org.junit.Test;

public class FunctionBuilderTest {

 private JSTypeRegistry registry = new JSTypeRegistry();
 private FunctionBuilder functionBuilder;

 @Test
 public void testFunctionBuilderWithName() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 FunctionType functionType = functionBuilder.build();
 assertEquals("testFunction", functionType.getReferenceName());
 }

 @Test
 public void testFunctionBuilderWithSourceNode() {
 Node sourceNode = new Node();
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withSourceNode(sourceNode);
 FunctionType functionType = functionBuilder.build();
 assertEquals(sourceNode, functionType.getSource());
 }

 @Test
 public void testFunctionBuilderWithParams() {
 FunctionParamBuilder paramsBuilder = new FunctionParamBuilder();
 paramsBuilder.withName("param1").withType(new StringType(registry));
 Node paramsNode = paramsBuilder.build();
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withParams(paramsBuilder);
 functionBuilder.withParamsNode(paramsNode);
 FunctionType functionType = functionBuilder.build();
 assertEquals(paramsNode, functionType.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithReturnType() {
 JSType returnType = new NumberType(registry);
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withReturnType(returnType);
 FunctionType functionType = functionBuilder.build();
 assertEquals(returnType, functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithInferredReturnType() {
 JSType returnType = new StringType(registry);
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withReturnType(returnType);
 functionBuilder.withInferredReturnType(returnType);
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isInferredReturnType());
 }

 @Test
 public void testFunctionBuilderWithTypeOfThis() {
 ObjectType typeOfThis = new ObjectType(registry, "TestObject");
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withTypeOfThis(typeOfThis);
 FunctionType functionType = functionBuilder.build();
 assertEquals(typeOfThis, functionType.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderWithTemplateName() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withTemplateName("TemplateFunction");
 FunctionType functionType = functionBuilder.build();
 assertEquals("TemplateFunction", functionType.getTemplateTypeName());
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.forConstructor();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isConstructor());
 }

 @Test
 public void testFunctionBuilderForNativeType() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.forNativeType();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isNativeType());
 }

 @Test(expected = IllegalStateException.class)
 public void testFunctionBuilderWithBothParamsAndParamsNode() {
 FunctionParamBuilder paramsBuilder = new FunctionParamBuilder();
 paramsBuilder.withName("param1").withType(new StringType(registry));
 Node paramsNode = paramsBuilder.build();
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withParams(paramsBuilder);
 functionBuilder.withParamsNode(paramsNode);
 FunctionType functionType = functionBuilder.build();
 fail("Expected IllegalStateException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFunctionBuilderWithNullTypeOfThis() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withTypeOfThis(null);
 FunctionType functionType = functionBuilder.build();
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFunctionBuilderWithNullTemplateTypeName() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withTemplateName(null);
 FunctionType functionType = functionBuilder.build();
 fail("Expected IllegalArgumentException");
 }
}