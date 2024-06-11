import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Test;

public class FunctionBuilderTest {

 private JSTypeRegistry registry = new JSTypeRegistry();
 private FunctionBuilder functionBuilder;

 @Test
 public void testFunctionBuilderWithName() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction");
 FunctionType expectedFunctionType = new FunctionType(registry, "testFunction", null, null, null, null, false, false);
 FunctionType actualFunctionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getName(), actualFunctionType.getName());
 }

 @Test
 public void testFunctionBuilderWithSourceNode() {
 Node sourceNode = new Node();
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withSourceNode(sourceNode);
 FunctionType expectedFunctionType = new FunctionType(registry, null, sourceNode, null, null, null, false, false);
 FunctionType actualFunctionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getSource(), actualFunctionType.getSource());
 }

 @Test
 public void testFunctionBuilderWithParams() {
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 paramBuilder.withName("param1").withType(registry.forEachType("Number"));
 Node paramsNode = paramBuilder.build();
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withParams(paramBuilder);
 FunctionType expectedFunctionType = new FunctionType(registry, null, null, new ArrowType(registry, paramsNode, registry.forType("Number"), false), null, null, false, false);
 FunctionType actualFunctionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getParametersNode(), actualFunctionType.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithParamsNode() {
 Node paramsNode = new Node();
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withParamsNode(paramsNode);
 FunctionType expectedFunctionType = new FunctionType(registry, null, null, new ArrowType(registry, paramsNode, null, false), null, null, false, false);
 FunctionType actualFunctionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getParametersNode(), actualFunctionType.getParametersNode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFunctionBuilderWithNullParamsNode() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withParamsNode(null);
 functionBuilder.build();
 }

 @Test
 public void testFunctionBuilderWithReturnType() {
 JSType returnType = registry.forType("String");
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withReturnType(returnType);
 FunctionType expectedFunctionType = new FunctionType(registry, null, null, null, null, null, false, false, null, returnType);
 FunctionType actualFunctionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getReturnType(), actualFunctionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithInferredReturnType() {
 JSType returnType = registry.forType("Number");
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withReturnType(returnType);
 functionBuilder.withInferredReturnType(returnType);
 FunctionType expectedFunctionType = new FunctionType(registry, null, null, null, null, null, false, false, null, returnType, true);
 FunctionType actualFunctionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getReturnType(), actualFunctionType.getReturnType());
 assertTrue(actualFunctionType.isInferredReturnType());
 }

 @Test
 public void testFunctionBuilderWithTypeOfThis() {
 ObjectType typeOfThis = new ObjectType(registry, "TestObject");
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withTypeOfThis(typeOfThis);
 FunctionType expectedFunctionType = new FunctionType(registry, null, null, null, null, null, false, false, typeOfThis, null);
 FunctionType actualFunctionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getTypeOfThis(), actualFunctionType.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderWithTemplateName() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withTemplateName("TestTemplate");
 FunctionType expectedFunctionType = new FunctionType(registry, null, null, null, null, "TestTemplate", false, false);
 FunctionType actualFunctionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getTemplateTypeName(), actualFunctionType.getTemplateTypeName());
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.forConstructor();
 FunctionType expectedFunctionType = new FunctionType(registry, null, null, null, null, null, true, false);
 FunctionType actualFunctionType = functionBuilder.build();
 assertTrue(actualFunctionType.isConstructor());
 }

 @Test
 public void testFunctionBuilderForNativeType() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.forNativeType();
 FunctionType expectedFunctionType = new FunctionType(registry, null, null, null, null, null, false, true);
 FunctionType actualFunctionType = functionBuilder.build();
 assertTrue(actualFunctionType.isNativeType());
 }

 @Test
 public void testFunctionBuilderCopyFromOtherFunction() {
 FunctionType otherType = new FunctionType(registry, "otherFunction", null, null, null, null, false, false);
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.copyFromOtherFunction(otherType);
 FunctionType expectedFunctionType = otherType;
 FunctionType actualFunctionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getName(), actualFunctionType.getName());
 assertEquals(expectedFunctionType.getSource(), actualFunctionType.getSource());
 assertEquals(expectedFunctionType.getParametersNode(), actualFunctionType.getParametersNode());
 assertEquals(expectedFunctionType.getReturnType(), actualFunctionType.getReturnType());
 assertEquals(expectedFunctionType.getTypeOfThis(), actualFunctionType.getTypeOfThis());
 assertEquals(expectedFunctionType.getTemplateTypeName(), actualFunctionType.getTemplateTypeName());
 assertEquals(expectedFunctionType.isConstructor(), actualFunctionType.isConstructor());
 assertEquals(expectedFunctionType.isNativeType(), actualFunctionType.isNativeType());
 }
}