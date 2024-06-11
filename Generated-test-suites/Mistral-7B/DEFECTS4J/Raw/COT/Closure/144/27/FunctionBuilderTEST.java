import org.junit.Test;
import org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionBuilder;

public class FunctionBuilderTest {

 private FunctionBuilder functionBuilder;
 private FunctionType functionType;

 @Test
 public void testFunctionBuilderWithName() {
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withName("testFunction");
 FunctionType expectedFunctionType = new FunctionType(new JSTypeRegistry(), "testFunction", null, null, null, null, false, false);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getReferenceName(), functionType.getReferenceName());
 }

 @Test
 public void testFunctionBuilderWithSourceNode() {
 Node sourceNode = new Node();
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withSourceNode(sourceNode);
 FunctionType expectedFunctionType = new FunctionType(new JSTypeRegistry(), null, sourceNode, null, null, null, false, false);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getSource(), functionType.getSource());
 }

 @Test
 public void testFunctionBuilderWithParams() {
 FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(new JSTypeRegistry());
 Node parametersNode = functionParamBuilder.build();
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withParams(functionParamBuilder);
 FunctionType expectedFunctionType = new FunctionType(new JSTypeRegistry(), null, null, new ArrowType(new JSTypeRegistry(), parametersNode, null, false), null, null, false, false);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getParametersNode(), functionType.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithReturnType() {
 JSType returnType = new JSTypeRegistry().getNullType();
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withReturnType(returnType);
 FunctionType expectedFunctionType = new FunctionType(new JSTypeRegistry(), null, null, null, null, null, false, false);
 expectedFunctionType.setReturnType(returnType);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getReturnType(), functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithInferredReturnType() {
 JSType returnType = new JSTypeRegistry().getNullType();
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withReturnType(returnType);
 functionBuilder.withInferredReturnType(returnType);
 FunctionType expectedFunctionType = new FunctionType(new JSTypeRegistry(), null, null, null, null, null, false, false);
 expectedFunctionType.setReturnType(returnType);
 expectedFunctionType.setInferredReturnType(true);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getReturnType(), functionType.getReturnType());
 assertEquals(expectedFunctionType.isInferredReturnType(), functionType.isInferredReturnType());
 }

 @Test
 public void testFunctionBuilderWithTypeOfThis() {
 ObjectType typeOfThis = new ObjectType(new JSTypeRegistry(), "TestType");
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withTypeOfThis(typeOfThis);
 FunctionType expectedFunctionType = new FunctionType(new JSTypeRegistry(), null, null, null, null, null, false, false);
 expectedFunctionType.setTypeOfThis(typeOfThis);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getTypeOfThis(), functionType.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderWithTemplateName() {
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withTemplateName("TestTemplate");
 FunctionType expectedFunctionType = new FunctionType(new JSTypeRegistry(), null, null, null, null, "TestTemplate", false, false);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getTemplateTypeName(), functionType.getTemplateTypeName());
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.forConstructor();
 FunctionType expectedFunctionType = new FunctionType(new JSTypeRegistry(), null, null, null, null, null, true, false);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType.isConstructor(), functionType.isConstructor());
 }

 @Test(expected = AssertionError.class)
 public void testFunctionBuilderForNativeTypeWithInvalidNode() {
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.forNativeType();
 Node invalidNode = new Node(); // Invalid Node type
 functionType = functionBuilder.build();
 fail("FunctionBuilder should throw AssertionError when building native type with invalid node.");
 }

 @Test
 public void testFunctionBuilderCopyFromOtherFunction() {
 FunctionType otherFunctionType = new FunctionType(new JSTypeRegistry(), "otherFunction", null, null, null, null, false, false);
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.copyFromOtherFunction(otherFunctionType);
 FunctionType expectedFunctionType = new FunctionType(new JSTypeRegistry(), "otherFunction", null, null, null, null, false, false);
 expectedFunctionType.setReferenceName(functionType.getReferenceName());
 expectedFunctionType.setSource(functionType.getSource());
 expectedFunctionType.setParametersNode(functionType.getParametersNode());
 expectedFunctionType.setReturnType(functionType.getReturnType());
 expectedFunctionType.setTypeOfThis(functionType.getTypeOfThis());
 expectedFunctionType.setTemplateTypeName(functionType.getTemplateTypeName());
 expectedFunctionType.setConstructor(functionType.isConstructor());
 expectedFunctionType.setNativeObjectType(functionType.isNativeObjectType());
 assertEquals(expectedFunctionType, functionType);
 }
}