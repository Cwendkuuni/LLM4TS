import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.types.ArrowType;
import com.google.javascript.rhino.jstype.types.FunctionParamBuilder;
import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

 private FunctionBuilder functionBuilder;
 private FunctionType functionType;
 private JSTypeRegistry registry;
 private ObjectType objectType;
 private Node node;
 private FunctionParamBuilder functionParamBuilder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 functionBuilder = new FunctionBuilder(registry);
 functionType = new FunctionType(registry, null, null, null, null, null, false, false);
 objectType = new ObjectType(registry, "Object");
 node = new Node();
 functionParamBuilder = new FunctionParamBuilder();
 }

 @Test
 public void testFunctionBuilder() {
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 assertEquals(registry, functionBuilder.getRegistry());
 assertNull(functionBuilder.getName());
 assertNull(functionBuilder.getSourceNode());
 assertNull(functionBuilder.getParametersNode());
 assertNull(functionBuilder.getReturnType());
 assertNull(functionBuilder.getTypeOfThis());
 assertNull(functionBuilder.getTemplateTypeName());
 assertFalse(functionBuilder.isInferredReturnType());
 assertFalse(functionBuilder.isConstructor());
 assertFalse(functionBuilder.isNativeType());
 }

 @Test
 public void testFunctionBuilderWithName() {
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 assertEquals("testFunction", functionBuilder.getName());
 }

 @Test
 public void testFunctionBuilderWithSourceNode() {
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 Node sourceNode = new Node();
 functionBuilder.withSourceNode(sourceNode);
 assertEquals(sourceNode, functionBuilder.getSourceNode());
 }

 @Test
 public void testFunctionBuilderWithParams() {
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder();
 functionParamBuilder.withName("testParam");
 functionParamBuilder.withType(registry.createStringType());
 FunctionType functionType = functionBuilder.withParams(functionParamBuilder).build();
 Node parametersNode = functionType.getParametersNode();
 assertEquals(1, parametersNode.getNumChildren());
 assertEquals("testParam", parametersNode.getFirstChild().getIdent().getName());
 assertEquals(registry.createStringType(), functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithParamsNode() {
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 Node parametersNode = new Node();
 functionBuilder.withParamsNode(parametersNode);
 FunctionType functionType = functionBuilder.build();
 assertEquals(parametersNode, functionType.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithReturnType() {
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 JSType returnType = registry.createNumberType();
 functionBuilder.withReturnType(returnType);
 assertEquals(returnType, functionBuilder.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithInferredReturnType() {
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 JSType returnType = registry.createNumberType();
 functionBuilder.withInferredReturnType(returnType);
 assertEquals(returnType, functionBuilder.getReturnType());
 assertTrue(functionBuilder.isInferredReturnType());
 }

 @Test
 public void testFunctionBuilderWithTypeOfThis() {
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 ObjectType typeOfThis = new ObjectType(registry, "TestType");
 functionBuilder.withTypeOfThis(typeOfThis);
 assertEquals(typeOfThis, functionBuilder.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderWithTemplateName() {
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withTemplateName("TestTemplate");
 assertEquals("TestTemplate", functionBuilder.getTemplateTypeName());
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 functionBuilder.forConstructor();
 assertTrue(functionBuilder.isConstructor());
 }

 @Test
 public void testFunctionBuilderForNativeType() {
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 functionBuilder.forNativeType();
 assertTrue(functionBuilder.isNativeType());
 }

 @Test
 public void testFunctionBuilderCopyFromOtherFunction() {
 FunctionType otherFunctionType = new FunctionType(registry, "testFunction", null, null, null, null, true, false);
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 functionBuilder.copyFromOtherFunction(otherFunctionType);
 assertEquals("testFunction", functionBuilder.getName());
 assertEquals(otherFunctionType.getSource(), functionBuilder.getSourceNode());
 assertEquals(otherFunctionType.getParametersNode(), functionBuilder.getParametersNode());
 assertEquals(otherFunctionType.getReturnType(), functionBuilder.getReturnType());
 assertEquals(otherFunctionType.getTypeOfThis(), functionBuilder.getTypeOfThis());
 assertEquals(otherFunctionType.getTemplateTypeName(), functionBuilder.getTemplateTypeName());
 assertTrue(functionBuilder.isConstructor());
 assertTrue(functionBuilder.isNativeType());
 }

 @Test
 public void testFunctionBuilderBuild() {
 functionBuilder.withName("testFunction");
 functionBuilder.withSourceNode(node);
 functionBuilder.withParams(functionParamBuilder);
 functionBuilder.withReturnType(registry.createNumberType());
 FunctionType functionType = functionBuilder.build();
 assertEquals("testFunction", functionType.getReferenceName());
 assertEquals(node, functionType.getSource());
 Node parametersNode = functionType.getParametersNode();
 assertEquals(1, parametersNode.getNumChildren());
 assertEquals("testParam", parametersNode.getFirstChild().getIdent().getName());
 assertEquals(registry.createNumberType(), functionType.getReturnType());
 }
}