import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionBuilder;

public class FunctionBuilderTest {

 private FunctionBuilder functionBuilder;
 private FunctionType functionType;

 @Before
 public void setUp() {
 // Initialize the FunctionBuilder with a JSTypeRegistry for testing
 FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 this.functionBuilder = functionBuilder;
 }

 // Test method for constructor
 @Test
 public void testFunctionBuilderConstructor() {
 // Test creating a FunctionBuilder with a JSTypeRegistry
 FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 assertNotNull(functionBuilder);
 }

 // Test methods for setting name
 @Test
 public void testWithName() {
 String name = "testFunction";
 functionBuilder.withName(name);
 FunctionType functionType = functionBuilder.build();
 assertEquals(name, functionType.getReferenceName());
 }

 // Test methods for setting sourceNode
 @Test
 public void testWithSourceNode() {
 Node sourceNode = new Node("testSource");
 functionBuilder.withSourceNode(sourceNode);
 FunctionType functionType = functionBuilder.build();
 assertEquals(sourceNode, functionType.getSource());
 }

 // Test methods for setting parameters
 FunctionParamBuilder paramsBuilder = new FunctionParamBuilder();
 @Test
 public void testWithParams() {
 JSType parameterType = new JSType();
 paramsBuilder.withName("testParam").withType(parameterType);
 functionBuilder.withParams(paramsBuilder);
 FunctionType functionType = functionBuilder.build();
 assertEquals(paramsBuilder.build(), functionType.getParameters());
 }

 // Test methods for setting parametersNode
 Node parametersNode = new Node("testParametersNode");
 @Test
 public void testWithParamsNode() {
 functionBuilder.withParamsNode(parametersNode);
 FunctionType functionType = functionBuilder.build();
 assertEquals(parametersNode, functionType.getParametersNode());
 }

 // Test methods for setting returnType
 @Test
 public void testWithReturnType() {
 JSType returnType = new JSType();
 functionBuilder.withReturnType(returnType);
 FunctionType functionType = functionBuilder.build();
 assertEquals(returnType, functionType.getReturnType());
 }

 // Test methods for setting inferredReturnType
 @Test
 public void testWithInferredReturnType() {
 JSType inferredReturnType = new JSType();
 functionBuilder.withInferredReturnType(inferredReturnType);
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isInferredReturnType());
 assertEquals(inferredReturnType, functionType.getReturnType());
 }

 // Test methods for setting typeOfThis
 @Test
 public void testWithTypeOfThis() {
 ObjectType typeOfThis = new ObjectType(new JSTypeRegistry());
 functionBuilder.withTypeOfThis(typeOfThis);
 FunctionType functionType = functionBuilder.build();
 assertEquals(typeOfThis, functionType.getTypeOfThis());
 }

 // Test methods for setting templateTypeName
 @Test
 public void testWithTemplateName() {
 String templateTypeName = "testTemplateName";
 functionBuilder.withTemplateName(templateTypeName);
 FunctionType functionType = functionBuilder.build();
 assertEquals(templateTypeName, functionType.getTemplateTypeName());
 }

 // Test methods for forConstructor
 @Test
 public void testForConstructor() {
 functionBuilder.forConstructor();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isConstructor());
 }

 // Test methods for forNativeType
 @Test
 public void testForNativeType() {
 functionBuilder.forNativeType();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isNativeObjectType());
 }

 // Test methods for copyFromOtherFunction
 FunctionType otherFunctionType = new FunctionType(new JSTypeRegistry(), null, null, null, null, null, true, false);
 @Test
 public void testCopyFromOtherFunction() {
 functionBuilder.copyFromOtherFunction(otherFunctionType);
 FunctionType functionType = functionBuilder.build();
 assertEquals(otherFunctionType.getReferenceName(), functionType.getReferenceName());
 assertEquals(otherFunctionType.getSource(), functionType.getSource());
 assertEquals(otherFunctionType.getParameters(), functionType.getParameters());
 assertEquals(otherFunctionType.getReturnType(), functionType.getReturnType());
 assertEquals(otherFunctionType.getTypeOfThis(), functionType.getTypeOfThis());
 assertEquals(otherFunctionType.getTemplateTypeName(), functionType.getTemplateTypeName());
 assertEquals(otherFunctionType.isConstructor(), functionType.isConstructor());
 assertEquals(otherFunctionType.isNativeObjectType(), functionType.isNativeObjectType());
}

 // Edge case testing
 @Test(expected = IllegalStateException.class)
 public void testBuilderWithoutRegistry() {
 FunctionBuilder functionBuilder = new FunctionBuilder(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullName() {
 functionBuilder.withName(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSourceNode() {
 functionBuilder.withSourceNode(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullParametersNode() {
 functionBuilder.withParamsNode(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullParameterType() {
 FunctionParamBuilder paramsBuilder = new FunctionParamBuilder();
 paramsBuilder.withName(null);
 functionBuilder.withParams(paramsBuilder);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullReturnType() {
 functionBuilder.withReturnType(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTypeOfThis() {
 functionBuilder.withTypeOfThis(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTemplateTypeName() {
 functionBuilder.withTemplateName(null);
 }

 // Test methods for isConstructor and isNativeType
 @Test
 public void testIsConstructorFalse() {
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.forNativeType();
 FunctionType functionType = functionBuilder.build();
 assertFalse(functionType.isConstructor());
 }

 @Test
 public void testIsNativeTypeFalse() {
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.forConstructor();
 FunctionType functionType = functionBuilder.build();
 assertFalse(functionType.isNativeObjectType());
 }

 @Test
 public void testIsConstructorTrue() {
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.forConstructor();
 functionBuilder.copyFromOtherFunction(new FunctionType(new JSTypeRegistry(), null, null, null, null, null, true, false));
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isConstructor());
 }

 @Test
 public void testIsNativeTypeTrue() {
 functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.forNativeType();
 functionBuilder.copyFromOtherFunction(new FunctionType(new JSTypeRegistry(), null, null, null, null, null, false, true));
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isNativeObjectType());
 }

}