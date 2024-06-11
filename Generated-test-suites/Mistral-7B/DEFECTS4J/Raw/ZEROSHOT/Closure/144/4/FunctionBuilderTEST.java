import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;

public class FunctionBuilderTest {

 private JSTypeRegistry registry;
 private FunctionBuilder functionBuilder;
 private FunctionType functionType;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 functionBuilder = new FunctionBuilder(registry);
 }

 @Test
 public void testFunctionBuilderWithNameAndReturnType() {
 String name = "testFunction";
 JSType returnType = registry.createStringType();
 functionBuilder.withName(name).withReturnType(returnType);
 functionType = functionBuilder.build();
 assertEquals(name, functionType.getReferenceName());
 assertEquals(returnType, functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithSourceNodeAndReturnType() {
 Node sourceNode = createMockNode();
 JSType returnType = registry.createStringType();
 functionBuilder.withSourceNode(sourceNode).withReturnType(returnType);
 functionType = functionBuilder.build();
 assertEquals(sourceNode, functionType.getSource());
 assertEquals(returnType, functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameParametersAndReturnType() {
 String name = "testFunction";
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
 Node parametersNode = paramBuilder.withName("param1").build();
 JSType returnType = registry.createStringType();
 functionBuilder.withName(name).withParams(paramBuilder).withReturnType(returnType);
 functionType = functionBuilder.build();
 assertEquals(name, functionType.getReferenceName());
 assertEquals(parametersNode, functionType.getParametersNode());
 assertEquals(returnType, functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameParametersReturnTypeAndTypeOfThis() {
 String name = "testFunction";
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
 Node parametersNode = paramBuilder.withName("param1").build();
 ObjectType typeOfThis = registry.createObjectType();
 functionBuilder.withName(name).withParams(paramBuilder).withReturnType(registry.createNumberType())
 .withTypeOfThis(typeOfThis);
 functionType = functionBuilder.build();
 assertEquals(name, functionType.getReferenceName());
 assertEquals(parametersNode, functionType.getParametersNode());
 assertEquals(registry.createNumberType(), functionType.getReturnType());
 assertEquals(typeOfThis, functionType.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderWithNameParametersReturnTypeInferredReturnTypeAndTypeOfThis() {
 String name = "testFunction";
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
 Node parametersNode = paramBuilder.withName("param1").build();
 functionBuilder.withName(name).withParams(paramBuilder)
 .withReturnType(registry.createStringType()).withInferredReturnType(true)
 .withTypeOfThis(registry.createObjectType());
 functionType = functionBuilder.build();
 assertEquals(name, functionType.getReferenceName());
 assertEquals(parametersNode, functionType.getParametersNode());
 assertEquals(registry.createStringType(), functionType.getReturnType());
 assertTrue(functionType.isInferredReturnType());
 assertEquals(registry.createObjectType(), functionType.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderWithNameParametersReturnTypeTemplateTypeNameAndTypeOfThis() {
 String name = "testFunction";
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
 Node parametersNode = paramBuilder.withName("param1").build();
 String templateTypeName = "TestTemplate";
 functionBuilder.withName(name).withParams(paramBuilder)
 .withReturnType(registry.createStringType())
 .withTemplateName(templateTypeName).withTypeOfThis(registry.createObjectType());
 functionType = functionBuilder.build();
 assertEquals(name, functionType.getReferenceName());
 assertEquals(parametersNode, functionType.getParametersNode());
 assertEquals(registry.createStringType(), functionType.getReturnType());
 assertEquals(templateTypeName, functionType.getTemplateTypeName());
 assertEquals(registry.createObjectType(), functionType.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 functionBuilder.forConstructor();
 assertTrue(functionType.isConstructor());
 }

 @Test
 public void testFunctionBuilderForNativeType() {
 functionBuilder.forNativeType();
 assertTrue(functionType.isNativeObjectType());
 }

 @Test
 public void testFunctionBuilderCopyFromOtherFunction() {
 FunctionType otherType = registry.createFunctionType(registry.createObjectType(), null, null, null, null, null, true, false);
 functionBuilder.copyFromOtherFunction(otherType);
 assertEquals(otherType.getReferenceName(), functionType.getReferenceName());
 assertEquals(otherType.getSource(), functionType.getSource());
 assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
 assertEquals(otherType.getReturnType(), functionType.getReturnType());
 assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
 assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
 assertEquals(otherType.isConstructor(), functionType.isConstructor());
 assertEquals(otherType.isNativeObjectType(), functionType.isNativeObjectType());
 }
}