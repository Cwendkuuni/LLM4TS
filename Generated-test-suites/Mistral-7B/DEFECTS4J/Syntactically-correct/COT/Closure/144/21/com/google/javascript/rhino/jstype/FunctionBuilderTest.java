import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionBuilder;

public class FunctionBuilderTest {

 private FunctionBuilder functionBuilder;
 private FunctionType functionType;

 @Test
 public void testFunctionBuilderWithName() {
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 FunctionType expectedFunctionType = new FunctionType(new JSTypeRegistry(), "testFunction", null, null, null, null, false, false);
 functionBuilder.withName("testFunction");
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType, functionType);
 }

 @Test
 public void testFunctionBuilderWithSourceNode() {
 Node sourceNode = new Node();
 FunctionType expectedFunctionType = new FunctionType(new JSTypeRegistry(), null, sourceNode, null, null, null, false, false);
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withSourceNode(sourceNode);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType, functionType);
 }

 @Test
 public void testFunctionBuilderWithParams() {
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 paramBuilder.withName("param1").withType(new JSTypeRegistry().getStringType());
 Node parametersNode = paramBuilder.build();
 FunctionType expectedFunctionType = new FunctionType(new JSTypeRegistry(), null, null, new ArrowType(new JSTypeRegistry(), parametersNode, null, false), null, null, false, false);
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withParams(paramBuilder);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType, functionType);
 }

 @Test
 public void testFunctionBuilderWithParamsNode() {
 Node parametersNode = new Node();
 FunctionType expectedFunctionType = new FunctionType(new JSTypeRegistry(), null, null, new ArrowType(new JSTypeRegistry(), parametersNode, null, false), null, null, false, false);
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withParamsNode(parametersNode);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType, functionType);
 }

 @Test
 public void testFunctionBuilderWithReturnType() {
 JSType returnType = new JSTypeRegistry().getBooleanType();
 FunctionType expectedFunctionType = new FunctionType(new JSTypeRegistry(), null, null, null, null, returnType, false, false);
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withReturnType(returnType);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType, functionType);
 }

 @Test
 public void testFunctionBuilderWithInferredReturnType() {
 JSType returnType = new JSTypeRegistry().getBooleanType();
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withReturnType(returnType);
 functionBuilder.withInferredReturnType(returnType);
 functionType = functionBuilder.build();
 assertTrue(functionType.isInferredReturnType());
 }

 @Test
 public void testFunctionBuilderWithTypeOfThis() {
 ObjectType typeOfThis = new ObjectType(new JSTypeRegistry(), "ObjectType");
 FunctionType expectedFunctionType = new FunctionType(new JSTypeRegistry(), null, null, null, typeOfThis, null, false, false);
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withTypeOfThis(typeOfThis);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType, functionType);
 }

 @Test
 public void testFunctionBuilderWithTemplateName() {
 String templateTypeName = "TemplateTypeName";
 FunctionType expectedFunctionType = new FunctionType(new JSTypeRegistry(), null, null, null, null, null, false, false, templateTypeName);
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withTemplateName(templateTypeName);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType, functionType);
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionType = functionBuilder.build();
 assertFalse(functionType.isConstructor());
 functionBuilder.forConstructor();
 functionType = functionBuilder.build();
 assertTrue(functionType.isConstructor());
 }

 @Test(expected = AssertionError.class)
 public void testFunctionBuilderForNativeTypeWithInvalidArguments() {
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.forNativeType(); // no arguments
 }

 @Test
 public void testFunctionBuilderForNativeTypeWithArguments() {
 Node sourceNode = new Node();
 Node parametersNode = new Node();
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withSourceNode(sourceNode);
 functionBuilder.withParamsNode(parametersNode);
 functionType = functionBuilder.build();
 assertTrue(functionType.isNativeType());
 }

 @Test(expected = AssertionError.class)
 public void testFunctionBuilderCopyFromOtherFunctionWithNullFunctionType() {
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionType = null;
 functionBuilder.copyFromOtherFunction((FunctionType)functionType);
 }

 @Test
 public void testFunctionBuilderCopyFromOtherFunction() {
 String name = "testFunction";
 Node sourceNode = new Node();
 Node parametersNode = new Node();
 JSType returnType = new JSTypeRegistry().getBooleanType();
 ObjectType typeOfThis = new ObjectType(new JSTypeRegistry(), "ObjectType");
 String templateTypeName = "TemplateTypeName";
 FunctionType otherFunctionType = new FunctionType(new JSTypeRegistry(), name, sourceNode, null, null, returnType, false, false);
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.copyFromOtherFunction(otherFunctionType);
 FunctionType expectedFunctionType = new FunctionType(new JSTypeRegistry(), name, sourceNode, null, null, returnType, false, false);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType, functionType);
 }
}