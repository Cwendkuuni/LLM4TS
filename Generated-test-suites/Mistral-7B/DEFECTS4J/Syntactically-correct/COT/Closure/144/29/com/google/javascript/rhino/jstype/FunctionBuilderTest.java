import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class FunctionBuilderTest {

 private JSTypeRegistry registry;
 private FunctionBuilder functionBuilder;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 functionBuilder = new FunctionBuilder(registry);
 }

 // Test for withName method
 @Test
 public void testWithName() {
 functionBuilder.withName("testFunction");
 FunctionType functionType = functionBuilder.build();
 assertEquals("testFunction", functionType.getReferenceName());
 }

 // Test for withSourceNode method
 @Test
 public void testWithSourceNode() {
 Node sourceNode = new Node();
 functionBuilder.withSourceNode(sourceNode);
 FunctionType functionType = functionBuilder.build();
 assertEquals(sourceNode, functionType.getSource());
 }

 // Test for withParams method
 @Test
 public void testWithParams() {
 FunctionParamBuilder paramsBuilder = new FunctionParamBuilder();
 paramsBuilder.withName("param1").withType(JSType.NUMBER);
 Node paramsNode = paramsBuilder.build();
 functionBuilder.withParams(paramsBuilder);
 FunctionType functionType = functionBuilder.build();
 assertEquals(paramsNode, functionType.getParametersNode());
 }

 // Test for withParamsNode method
 @Test
 public void testWithParamsNode() {
 Node paramsNode = new Node();
 functionBuilder.withParamsNode(paramsNode);
 FunctionType functionType = functionBuilder.build();
 assertEquals(paramsNode, functionType.getParametersNode());
 }

 // Test for withReturnType method
 @Test
 public void testWithReturnType() {
 JSType returnType = JSType.STRING;
 functionBuilder.withReturnType(returnType);
 FunctionType functionType = functionBuilder.build();
 assertEquals(returnType, functionType.getReturnType());
 }

 // Test for withInferredReturnType method
 @Test
 public void testWithInferredReturnType() {
 JSType returnType = JSType.STRING;
 functionBuilder.withInferredReturnType(returnType);
 FunctionType functionType = functionBuilder.build();
 assertEquals(returnType, functionType.getReturnType());
 assertTrue(functionType.isInferredReturnType());
 }

 // Test for withTypeOfThis method
 @Test
 public void testWithTypeOfThis() {
 ObjectType typeOfThis = new ObjectType(registry, "Object");
 functionBuilder.withTypeOfThis(typeOfThis);
 FunctionType functionType = functionBuilder.build();
 assertEquals(typeOfThis, functionType.getTypeOfThis());
 }

 // Test for withTemplateName method
 @Test
 public void testWithTemplateName() {
 functionBuilder.withTemplateName("TemplateName");
 FunctionType functionType = functionBuilder.build();
 assertEquals("TemplateName", functionType.getTemplateTypeName());
 }

 // Test for forConstructor method
 @Test
 public void testForConstructor() {
 functionBuilder.forConstructor();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isConstructor());
 }

 // Test for forNativeType method
 @Test
 public void testForNativeType() {
 functionBuilder.forNativeType();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isNativeObjectType());
 }

 // Test for copyFromOtherFunction method
 @Test
 public void testCopyFromOtherFunction() {
 FunctionType otherFunctionType = new FunctionType(registry, "otherFunction", null, null, null, null, true, false);
 functionBuilder.copyFromOtherFunction(otherFunctionType);
 FunctionType functionType = functionBuilder.build();
 assertEquals("otherFunction", functionType.getReferenceName());
 assertEquals(otherFunctionType.getSource(), functionType.getSource());
 assertEquals(otherFunctionType.getParametersNode(), functionType.getParametersNode());
 assertEquals(otherFunctionType.getReturnType(), functionType.getReturnType());
 assertEquals(otherFunctionType.getTypeOfThis(), functionType.getTypeOfThis());
 assertEquals(otherFunctionType.getTemplateTypeName(), functionType.getTemplateTypeName());
 assertTrue(functionType.isConstructor(), otherFunctionType.isConstructor());
 assertTrue(functionType.isNativeObjectType(), otherFunctionType.isNativeObjectType());
 }

 // Test for build method
 @Test(expected = IllegalStateException.class)
 public void testBuildMissingName() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withParamsNode(new Node());
 functionBuilder.build();
 fail("Expected IllegalStateException");
 }

 // Test for build method with null return type
 @Test
 public void testBuildNullReturnType() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 functionBuilder.withParamsNode(new Node());
 functionBuilder.withReturnType(null);
 FunctionType functionType = functionBuilder.build();
 assertNull(functionType.getReturnType());
 }

 // Test for build method with null typeOfThis
 @Test
 public void testBuildNullTypeOfThis() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 functionBuilder.withParamsNode(new Node());
 functionBuilder.withTypeOfThis(null);
 FunctionType functionType = functionBuilder.build();
 assertNull(functionType.getTypeOfThis());
 }

 // Test for build method with null templateTypeName
 @Test
 public void testBuildNullTemplateTypeName() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 functionBuilder.withParamsNode(new Node());
 functionBuilder.withTemplateName(null);
 FunctionType functionType = functionBuilder.build();
 assertNull(functionType.getTemplateTypeName());
 }

 // Test for build method with missing returnType and inferredReturnType
 @Test
 public void testBuildMissingReturnTypeAndInferredReturnType() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 functionBuilder.withParamsNode(new Node());
 functionType = functionBuilder.build();
 assertNull(functionType.getReturnType());
 assertFalse(functionType.isInferredReturnType());
 }

 // Test for build method with missing returnType and null inferredReturnType
 @Test
 public void testBuildMissingReturnTypeAndNullInferredReturnType() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 functionBuilder.withParamsNode(new Node());
 functionBuilder.withReturnType(null);
 functionBuilder.withInferredReturnType(null);
 functionType = functionBuilder.build();
 assertNull(functionType.getReturnType());
 assertFalse(functionType.isInferredReturnType());
 }

 // Test for build method with missing returnType and inferredReturnType
 @Test
 public void testBuildMissingReturnTypeAndInferredReturnTypeTrue() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 functionBuilder.withParamsNode(new Node());
 functionBuilder.withInferredReturnType(JSType.NUMBER);
 functionType = functionBuilder.build();
 assertNotNull(functionType.getReturnType());
 assertTrue(functionType.isInferredReturnType());
 assertEquals(JSType.NUMBER, functionType.getReturnType());
 }

 // Test for build method with missing returnType and inferredReturnType false
 @Test
 public void testBuildMissingReturnTypeAndInferredReturnTypeFalse() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 functionBuilder.withParamsNode(new Node());
 functionBuilder.withReturnType(null);
 functionBuilder.withInferredReturnType(false);
 functionType = functionBuilder.build();
 assertNull(functionType.getReturnType());
 assertFalse(functionType.isInferredReturnType());
 }

 // Test for build method with missing templateTypeName and null inferredReturnType
 @Test
 public void testBuildMissingTemplateTypeNameAndNullInferredReturnType() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 functionBuilder.withParamsNode(new Node());
 functionBuilder.withTemplateName(null);
 functionBuilder.withInferredReturnType(null);
 functionType = functionBuilder.build();
 assertNull(functionType.getReturnType());
 assertNull(functionType.getTemplateTypeName());
 }

 // Test for build method with missing templateTypeName and inferredReturnType false
 @Test
 public void testBuildMissingTemplateTypeNameAndInferredReturnTypeFalse() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 functionBuilder.withParamsNode(new Node());
 functionBuilder.withTemplateName(null);
 functionBuilder.withInferredReturnType(false);
 functionType = functionBuilder.build();
 assertNull(functionType.getReturnType());
 assertNull(functionType.getTemplateTypeName());
 }

 // Test for build method with missing templateTypeName and inferredReturnType true
 @Test
 public void testBuildMissingTemplateTypeNameAndInferredReturnTypeTrue() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 functionBuilder.withParamsNode(new Node());
 functionBuilder.withInferredReturnType(JSType.NUMBER);
 functionType = functionBuilder.build();
 assertNotNull(functionType.getReturnType());
 assertTrue(functionType.isInferredReturnType());
 assertEquals(JSType.NUMBER, functionType.getReturnType());
 assertNull(functionType.getTemplateTypeName());
 }

 // Test for build method with missing templateTypeName and inferredReturnType false
 @Test
 public void testBuildMissingTemplateTypeNameAndInferredReturnTypeFalse() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 functionBuilder.withParamsNode(new Node());
 functionBuilder.withTemplateName(null);
 functionBuilder.withInferredReturnType(false);
 functionType = functionBuilder.build();
 assertNull(functionType.getReturnType());
 assertNull(functionType.getTemplateTypeName());
 }

 // Test for build method with empty parameters
 @Test
 public void testBuildEmptyParameters() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 functionBuilder.withParamsNode(new Node());
 functionType = functionBuilder.build();
 ArrowType arrowType = (ArrowType) functionType.getReturnType();
 assertTrue(arrowType.getParameters().isEmpty());
 }

 // Test for build method with single parameter
 @Test
 public void testBuildSingleParameter() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 paramBuilder.withName("param1").withType(JSType.NUMBER);
 Node paramsNode = paramBuilder.build();
 functionBuilder.withParams(paramBuilder);
 functionType = functionBuilder.build();
 ArrowType arrowType = (ArrowType) functionType.getReturnType();
 List<FunctionParam> params = arrowType.getParameters();
 assertEquals(1, params.size());
 FunctionParam param = params.get(0);
 assertEquals("param1", param.getName());
 assertEquals(JSType.NUMBER, param.getType());
 }

 // Test for build method with multiple parameters
 @Test
 public void testBuildMultipleParameters() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder();
 paramBuilder1.withName("param1").withType(JSType.NUMBER);
 Node paramsNode1 = paramBuilder1.build();
 FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder();
 paramBuilder2.withName("param2").withType(JSType.STRING);
 Node paramsNode2 = paramBuilder2.build();
 functionBuilder.withParams(new FunctionParamBuilder().withNode(new Node().setChildren(new Node[]{paramsNode1, paramsNode2})));
 functionType = functionBuilder.build();
 ArrowType arrowType = (ArrowType) functionType.getReturnType();
 List<FunctionParam> params = arrowType.getParameters();
 assertEquals(2, params.size());
 FunctionParam param1 = params.get(0);
 assertEquals("param1", param1.getName());
 assertEquals(JSType.NUMBER, param1.getType());
 FunctionParam param2 = params.get(1);
 assertEquals("param2", param2.getName());
 assertEquals(JSType.STRING, param2.getType());
 }

 // Test for build method with return type of ArrowType
 @Test
 public void testBuildArrowTypeReturnType() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 paramBuilder.withName("param1").withType(JSType.NUMBER);
 Node paramsNode = paramBuilder.build();
 functionBuilder.withParams(paramBuilder);
 JSType returnType = new ArrowType(registry, new Node(), JSType.STRING, false);
 functionBuilder.withReturnType(returnType);
 functionType = functionBuilder.build();
 assertEquals(JSType.FUNCTION, functionType.getClass());
 ArrowType arrowType = (ArrowType) functionType.getReturnType();
 List<FunctionParam> paramsArrowType = arrowType.getParameters();
 assertEquals(1, paramsArrowType.size());
 FunctionParam param = paramsArrowType.get(0);
 assertEquals("param1", param.getName());
 assertEquals(JSType.NUMBER, param.getType());
 ArrowType arrowTypeFunctionType = (ArrowType) functionType.getReturnType();
 assertEquals(JSType.STRING, arrowTypeFunctionType.getReturnType().getType());
 }

 // Test for build method with typeOfThis set
 @Test
 public void testBuildWithTypeOfThis() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 paramBuilder.withName("param1").withType(JSType.NUMBER);
 Node paramsNode = paramBuilder.build();
 functionBuilder.withParams(paramBuilder);
 ObjectType typeOfThis = new ObjectType(registry, "Object");
 functionBuilder.withTypeOfThis(typeOfThis);
 functionType = functionBuilder.build();
 assertEquals(JSType.FUNCTION, functionType.getClass());
 ObjectType objectType = (ObjectType) functionType.getTypeOfThis();
 assertEquals("Object", objectType.getName());
 }

 // Test for build method with templateTypeName set
 @Test
 public void testBuildWithTemplateTypeName() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 paramBuilder.withName("param1").withType(JSType.NUMBER);
 Node paramsNode = paramBuilder.build();
 functionBuilder.withParams(paramBuilder);
 functionBuilder.withTemplateName("TemplateName");
 functionType = functionBuilder.build();
 assertEquals("TemplateName", functionType.getTemplateTypeName());
 }

 // Test for build method with constructor flag set
 @Test
 public void testBuildConstructor() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 paramBuilder.withName("param1").withType(JSType.NUMBER);
 Node paramsNode = paramBuilder.build();
 functionBuilder.forConstructor();
 functionType = functionBuilder.build();
 assertTrue(functionType.isConstructor());
 }

 // Test for build method with nativeType flag set
 @Test
 public void testBuildNativeType() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 paramBuilder.withName("param1").withType(JSType.NUMBER);
 Node paramsNode = paramBuilder.build();
 functionBuilder.forNativeType();
 functionType = functionBuilder.build();
 assertTrue(functionType.isNativeObjectType());
 }

}