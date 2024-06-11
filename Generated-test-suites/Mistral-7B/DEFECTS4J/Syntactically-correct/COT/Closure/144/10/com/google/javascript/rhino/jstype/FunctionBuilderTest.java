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
 private ObjectType objectType;
 private Node node;
 private Node parametersNode;
 private FunctionParamBuilder functionParamBuilder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 functionBuilder = new FunctionBuilder(registry);
 objectType = new ObjectType(registry, "Object");
 node = new Node_String("test");
 }

 @Test
 public void testFunctionBuilderWithName() {
 functionBuilder = functionBuilder.withName("testFunction");
 FunctionType expectedFunctionType = new FunctionType(registry, "testFunction", null, null, objectType, null, false, false);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getReferenceName(), functionType.getReferenceName());
 }

 @Test
 public void testFunctionBuilderWithSourceNode() {
 functionBuilder = functionBuilder.withSourceNode(node);
 FunctionType expectedFunctionType = new FunctionType(registry, null, node, null, objectType, null, false, false);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getSource(), functionType.getSource());
 }

 @Test
 public void testFunctionBuilderWithParams() {
 FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(registry);
 functionParamBuilder = functionParamBuilder.withName("param1").withType(new NumberType(registry));
 parametersNode = functionParamBuilder.build();
 functionBuilder = functionBuilder.withParams(functionParamBuilder);
 FunctionType expectedFunctionType = new FunctionType(registry, null, null, new ArrowType(registry, parametersNode, new NumberType(registry), false), objectType, null, false, false);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getParametersNode(), functionType.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithParamsNode() {
 functionBuilder = functionBuilder.withParamsNode(parametersNode);
 FunctionType expectedFunctionType = new FunctionType(registry, null, null, new ArrowType(registry, parametersNode, new NumberType(registry), false), objectType, null, false, false);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getParametersNode(), functionType.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithReturnType() {
 functionBuilder = functionBuilder.withReturnType(new NumberType(registry));
 FunctionType expectedFunctionType = new FunctionType(registry, null, null, new ArrowType(registry, parametersNode, new NumberType(registry), false), objectType, null, false, false);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getReturnType().getClass(), functionType.getReturnType().getClass());
 }

 @Test
 public void testFunctionBuilderWithInferredReturnType() {
 functionBuilder = functionBuilder.withInferredReturnType(new NumberType(registry));
 FunctionType expectedFunctionType = new FunctionType(registry, null, null, new ArrowType(registry, parametersNode, null, true), objectType, null, false, false);
 functionType = functionBuilder.build();
 assertTrue(functionType.isInferredReturnType());
 }

 @Test
 public void testFunctionBuilderWithTypeOfThis() {
 functionBuilder = functionBuilder.withTypeOfThis(objectType);
 FunctionType expectedFunctionType = new FunctionType(registry, null, null, new ArrowType(registry, parametersNode, new NumberType(registry), false), objectType, null, false, false);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getTypeOfThis(), functionType.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 functionBuilder.forConstructor();
 FunctionType expectedFunctionType = new FunctionType(registry, "testFunction", null, null, objectType, null, true, false);
 functionType = functionBuilder.build();
 assertTrue(functionType.isConstructor());
 }

 @Test(expected = AssertionError.class)
 public void testFunctionBuilderForNativeTypeWithNullRegistry() {
 FunctionBuilder functionBuilder = new FunctionBuilder(null);
 functionBuilder.forNativeType();
 }

 @Test(expected = AssertionError.class)
 public void testFunctionBuilderWithNullRegistry() {
 FunctionBuilder functionBuilder = new FunctionBuilder(null);
 functionBuilder.build();
 }

 @Test(expected = AssertionError.class)
 public void testFunctionBuilderWithNullParametersNode() {
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 functionBuilder = functionBuilder.withParamsNode(null);
 functionBuilder.build();
 }

 @Test(expected = AssertionError.class)
 public void testFunctionBuilderWithNullReturnType() {
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 functionBuilder = functionBuilder.withReturnType(null);
 functionBuilder.build();
 }

 @Test(expected = AssertionError.class)
 public void testFunctionBuilderWithNullRegistryAndNullReturnType() {
 FunctionBuilder functionBuilder = new FunctionBuilder(null);
 functionBuilder = functionBuilder.withReturnType(null);
 functionBuilder.build();
 }

 @Test
 public void testFunctionBuilderCopyFromOtherFunction() {
 FunctionType otherFunctionType = new FunctionType(registry, "testFunction", null, null, objectType, null, false, false);
 FunctionType expectedFunctionType = new FunctionType(registry, "testFunction", null, null, objectType, null, false, false);
 functionBuilder.copyFromOtherFunction(otherFunctionType);
 functionType = functionBuilder.build();
 assertEquals(expectedFunctionType.getReferenceName(), functionType.getReferenceName());
 assertEquals(expectedFunctionType.getSource(), functionType.getSource());
 assertEquals(expectedFunctionType.getParametersNode(), functionType.getParametersNode());
 assertEquals(expectedFunctionType.getReturnType().getClass(), functionType.getReturnType().getClass());
 assertEquals(expectedFunctionType.getTypeOfThis(), functionType.getTypeOfThis());
 assertEquals(expectedFunctionType.isConstructor(), functionType.isConstructor());
 assertEquals(expectedFunctionType.isNativeType(), functionType.isNativeType());
 }
}