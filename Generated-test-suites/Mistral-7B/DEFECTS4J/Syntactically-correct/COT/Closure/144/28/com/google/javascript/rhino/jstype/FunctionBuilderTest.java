import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Test;

public class FunctionBuilderTest {

 private JSTypeRegistry registry;
 private FunctionBuilder functionBuilder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 functionBuilder = new FunctionBuilder(registry);
 }

 @Test
 public void testFunctionBuilderWithName() {
 String name = "testFunction";
 functionBuilder.withName(name);
 FunctionType functionType = functionBuilder.build();
 assertEquals(name, functionType.getReferenceName());
 }

 @Test
 public void testFunctionBuilderWithSourceNode() {
 Node sourceNode = new Node("testSource");
 functionBuilder.withSourceNode(sourceNode);
 FunctionType functionType = functionBuilder.build();
 assertEquals(sourceNode, functionType.getSource());
 }

 @Test
 public void testFunctionBuilderWithParameters() {
 FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder();
 functionParamBuilder.withName("param1").withType(JSType.STRING);
 Node parametersNode = functionParamBuilder.build();
 functionBuilder.withParams(functionParamBuilder);
 functionBuilder.withParamsNode(parametersNode);
 FunctionType functionType = functionBuilder.build();
 assertEquals(parametersNode, functionType.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithReturnType() {
 JSType returnType = new ObjectType(registry, "TestObject");
 functionBuilder.withReturnType(returnType);
 FunctionType functionType = functionBuilder.build();
 assertEquals(returnType, functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithInferredReturnType() {
 JSType returnType = new ObjectType(registry, "TestObject");
 functionBuilder.withReturnType(returnType);
 functionBuilder.withInferredReturnType(returnType);
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isInferredReturnType());
 }

 @Test
 public void testFunctionBuilderWithTypeOfThis() {
 ObjectType typeOfThis = new ObjectType(registry, "TestObject");
 functionBuilder.withTypeOfThis(typeOfThis);
 FunctionType functionType = functionBuilder.build();
 assertEquals(typeOfThis, functionType.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderWithTemplateName() {
 String templateTypeName = "TestTemplate";
 functionBuilder.withTemplateName(templateTypeName);
 FunctionType functionType = functionBuilder.build();
 assertEquals(templateTypeName, functionType.getTemplateTypeName());
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 functionBuilder.forConstructor();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isConstructor());
 }

 @Test(expected = AssertionError.class)
 public void testFunctionBuilderForConstructorTwice() {
 functionBuilder.forConstructor();
 functionBuilder.forConstructor();
 FunctionType functionType = functionBuilder.build();
 fail("Expected AssertionError, but no exception was thrown.");
 }

 @Test
 public void testFunctionBuilderForNativeType() {
 functionBuilder.forNativeType();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isNativeObjectType());
 }

 @Test
 public void testFunctionBuilderCopyFromOtherFunction() {
 FunctionType otherFunctionType = new FunctionType(registry, "otherFunction", null, null, null, null, true, true);
 functionBuilder.copyFromOtherFunction(otherFunctionType);
 FunctionType functionType = functionBuilder.build();
 assertEquals(otherFunctionType.getReferenceName(), functionType.getReferenceName());
 assertEquals(otherFunctionType.getSource(), functionType.getSource());
 assertEquals(otherFunctionType.getParametersNode(), functionType.getParametersNode());
 assertEquals(otherFunctionType.getReturnType(), functionType.getReturnType());
 assertEquals(otherFunctionType.getTypeOfThis(), functionType.getTypeOfThis());
 assertEquals(otherFunctionType.getTemplateTypeName(), functionType.getTemplateTypeName());
 assertEquals(otherFunctionType.isConstructor(), functionType.isConstructor());
 assertEquals(otherFunctionType.isNativeObjectType(), functionType.isNativeObjectType());
}

}