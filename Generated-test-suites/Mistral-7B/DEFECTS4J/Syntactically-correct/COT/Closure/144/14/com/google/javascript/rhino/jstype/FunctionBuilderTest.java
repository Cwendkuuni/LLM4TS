import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class FunctionBuilderTest {

 private JSTypeRegistry registry;
 private FunctionBuilder functionBuilder;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 functionBuilder = new FunctionBuilder(registry);
 }

 @Test
 public void testWithName() {
 functionBuilder.withName("testFunction");
 assertEquals("testFunction", functionBuilder.getName());
 }

 @Test
 public void testWithSourceNode() {
 Node sourceNode = new Node();
 functionBuilder.withSourceNode(sourceNode);
 assertEquals(sourceNode, functionBuilder.getSourceNode());
 }

 @Test
 public void testWithParams() {
 FunctionParamBuilder paramsBuilder = new FunctionParamBuilder();
 paramsBuilder.withName("param1").withType(new NumberType(registry));
 functionBuilder.withParams(paramsBuilder);
 FunctionType functionType = functionBuilder.build();
 FunctionType expectedFunctionType = new FunctionType(registry, "testFunction", null, new ArrowType(registry, null, new NumberType(registry), false), null, null, false, false);
 assertEquals(expectedFunctionType, functionType);
 }

 @Test
 public void testWithParamsNode() {
 Node parametersNode = new Node();
 functionBuilder.withParamsNode(parametersNode);
 FunctionType functionType = functionBuilder.build();
 FunctionType expectedFunctionType = new FunctionType(registry, "testFunction", null, new ArrowType(registry, parametersNode, new NumberType(registry), false), null, null, false, false);
 assertEquals(expectedFunctionType, functionType);
 }

 @Test
 public void testWithReturnType() {
 functionBuilder.withReturnType(new NumberType(registry));
 FunctionType functionType = functionBuilder.build();
 FunctionType expectedFunctionType = new FunctionType(registry, "testFunction", null, null, null, null, false, false);
 assertEquals(expectedFunctionType.getReturnType(), functionType.getReturnType());
 }

 @Test
 public void testWithInferredReturnType() {
 functionBuilder.withReturnType(new ObjectType(registry, "String"));
 functionBuilder.withInferredReturnType(new NumberType(registry));
 FunctionType functionType = functionBuilder.build();
 FunctionType expectedFunctionType = new FunctionType(registry, "testFunction", null, null, null, null, false, false);
 assertEquals(expectedFunctionType.getReturnType(), functionType.getReturnType());
 assertTrue(functionType.isInferredReturnType());
 }

 @Test
 public void testWithTypeOfThis() {
 functionBuilder.withTypeOfThis(new ObjectType(registry, "Object"));
 FunctionType functionType = functionBuilder.build();
 FunctionType expectedFunctionType = new FunctionType(registry, "testFunction", null, null, new ObjectType(registry, "Object"), null, false, false);
 assertEquals(expectedFunctionType, functionType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullTypeOfThis() {
 functionBuilder.withTypeOfThis(null);
 }

 @Test
 public void testWithTemplateName() {
 functionBuilder.withTemplateName("testTemplate");
 assertEquals("testTemplate", functionBuilder.getTemplateTypeName());
 }

 @Test
 public void testForConstructor() {
 functionBuilder.forConstructor();
 FunctionType functionType = functionBuilder.build();
 assertEquals(true, functionType.isConstructor());
 }

 @Test
 public void testForNativeType() {
 functionBuilder.forNativeType();
 FunctionType functionType = functionBuilder.build();
 assertEquals(true, functionType.isNativeType());
 }

 @Test
 public void testCopyFromOtherFunction() {
 FunctionType otherFunctionType = new FunctionType(registry, "otherFunction", null, null, null, null, false, false);
 functionBuilder.copyFromOtherFunction(otherFunctionType);
 FunctionType functionType = functionBuilder.build();
 assertEquals(otherFunctionType, functionType);
 }
}