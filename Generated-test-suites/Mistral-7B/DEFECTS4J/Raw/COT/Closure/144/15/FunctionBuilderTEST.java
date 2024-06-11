import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;

import org.junit.Test;

public class FunctionBuilderTest {

 private FunctionBuilder funcBuilder;
 private FunctionType funcType;
 private FunctionParamBuilder paramBuilder;

 @Test
 public void testFunctionBuilderWithName() {
 funcBuilder = new FunctionBuilder(new JSTypeRegistry());
 funcBuilder.withName("testFunction");
 FunctionType expectedFuncType = new FunctionType(new JSTypeRegistry(), "testFunction", null, null, null, null, false, false);
 funcType = funcBuilder.build();
 assertEquals(expectedFuncType, funcType);
 }

 @Test
 public void testFunctionBuilderWithSourceNode() {
 Node sourceNode = new Node();
 funcBuilder = new FunctionBuilder(new JSTypeRegistry());
 funcBuilder.withSourceNode(sourceNode);
 FunctionType expectedFuncType = new FunctionType(new JSTypeRegistry(), null, sourceNode, null, null, null, false, false);
 funcType = funcBuilder.build();
 assertEquals(expectedFuncType, funcType);
 }

 @Test
 public void testFunctionBuilderWithParams() {
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 paramBuilder.withName("param1").withType(new IntType(1));
 funcBuilder = new FunctionBuilder(new JSTypeRegistry());
 funcBuilder.withParams(paramBuilder);
 FunctionType expectedFuncType = new FunctionType(new JSTypeRegistry(), null, null, new ArrowType(new JSTypeRegistry(), null, new IntType(1), false), null, null, false, false);
 funcType = funcBuilder.build();
 assertEquals(expectedFuncType, funcType);
 }

 @Test
 public void testFunctionBuilderWithParamsNode() {
 Node parametersNode = new Node();
 funcBuilder = new FunctionBuilder(new JSTypeRegistry());
 funcBuilder.withParamsNode(parametersNode);
 FunctionType expectedFuncType = new FunctionType(new JSTypeRegistry(), null, null, null, null, null, false, false);
 funcType = funcBuilder.build();
 assertNull(funcType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFunctionBuilderWithNullParamsNode() {
 Node parametersNode = null;
 funcBuilder = new FunctionBuilder(new JSTypeRegistry());
 funcBuilder.withParamsNode(parametersNode);
 }

 @Test
 public void testFunctionBuilderWithReturnType() {
 JSType returnType = new IntType(1);
 funcBuilder = new FunctionBuilder(new JSTypeRegistry());
 funcBuilder.withReturnType(returnType);
 FunctionType expectedFuncType = new FunctionType(new JSTypeRegistry(), null, null, null, null, null, false, false, null, returnType);
 funcType = funcBuilder.build();
 assertEquals(expectedFuncType, funcType);
 }

 @Test
 public void testFunctionBuilderWithInferredReturnType() {
 JSType returnType = new IntType(1);
 funcBuilder = new FunctionBuilder(new JSTypeRegistry());
 funcBuilder.withInferredReturnType(returnType);
 FunctionType expectedFuncType = new FunctionType(new JSTypeRegistry(), null, null, null, null, null, false, false, null, null, true, returnType);
 funcType = funcBuilder.build();
 assertEquals(expectedFuncType, funcType);
 }

 @Test
 public void testFunctionBuilderWithTypeOfThis() {
 ObjectType typeOfThis = new ObjectType("Object");
 funcBuilder = new FunctionBuilder(new JSTypeRegistry());
 funcBuilder.withTypeOfThis(typeOfThis);
 FunctionType expectedFuncType = new FunctionType(new JSTypeRegistry(), null, null, null, typeOfThis, null, false, false);
 funcType = funcBuilder.build();
 assertEquals(expectedFuncType, funcType);
 }

 @Test
 public void testFunctionBuilderWithTemplateName() {
 String templateTypeName = "Template";
 funcBuilder = new FunctionBuilder(new JSTypeRegistry());
 funcBuilder.withTemplateName(templateTypeName);
 FunctionType expectedFuncType = new FunctionType(new JSTypeRegistry(), null, null, null, null, templateTypeName, false, false);
 funcType = funcBuilder.build();
 assertEquals(expectedFuncType, funcType);
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 funcBuilder = new FunctionBuilder(new JSTypeRegistry());
 funcBuilder.forConstructor();
 FunctionType expectedFuncType = new FunctionType(new JSTypeRegistry(), null, null, null, null, null, true, false);
 funcType = funcBuilder.build();
 assertEquals(expectedFuncType, funcType);
 }

 @Test
 public void testFunctionBuilderForNativeType() {
 funcBuilder = new FunctionBuilder(new JSTypeRegistry());
 funcBuilder.forNativeType();
 FunctionType expectedFuncType = new FunctionType(new JSTypeRegistry(), null, null, null, null, null, false, true);
 funcType = funcBuilder.build();
 assertEquals(expectedFuncType, funcType);
 }

 @Test
 public void testFunctionBuilderCopyFromOtherFunction() {
 FunctionType otherType = new FunctionType(new JSTypeRegistry(), "testFunction", null, null, null, null, false, false);
 funcBuilder = new FunctionBuilder(new JSTypeRegistry());
 funcBuilder.copyFromOtherFunction(otherType);
 FunctionType expectedFuncType = new FunctionType(new JSTypeRegistry(), "testFunction", null, null, null, null, false, false);
 funcType = funcBuilder.build();
 assertEquals(expectedFuncType, funcType);
 }
}