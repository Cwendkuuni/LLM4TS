import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

 private JSTypeRegistry registry;
 private FunctionBuilder functionBuilder;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 functionBuilder = new FunctionBuilder(registry);
 }

 @Test
 public void testFunctionBuilderWithNameAndReturnType() {
 functionBuilder.withName("myFunction")
 .withReturnType(new IntType(registry));
 FunctionType functionType = functionBuilder.build();
 assertEquals("myFunction", functionType.getReferenceName());
 assertTrue(functionType instanceof FunctionType);
 assertNotNull(functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameAndReturnTypeAndSourceNode() {
 Node sourceNode = new ObjectLiteral();
 functionBuilder.withName("myFunction")
 .withReturnType(new IntType(registry))
 .withSourceNode(sourceNode);
 FunctionType functionType = functionBuilder.build();
 assertEquals("myFunction", functionType.getReferenceName());
 assertTrue(functionType instanceof FunctionType);
 assertNotNull(functionType.getReturnType());
 assertEquals(sourceNode, functionType.getSource());
 }

 @Test
 public void testFunctionBuilderWithNameAndReturnTypeAndParameters() {
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 paramBuilder.withName("param1").withType(new IntType(registry));
 Node parametersNode = paramBuilder.build();
 functionBuilder.withName("myFunction")
 .withReturnType(new IntType(registry))
 .withParamsNode(parametersNode);
 FunctionType functionType = functionBuilder.build();
 assertEquals("myFunction", functionType.getReferenceName());
 assertTrue(functionType instanceof FunctionType);
 assertNotNull(functionType.getReturnType());
 assertEquals(parametersNode, functionType.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithNameAndReturnTypeAndInferredReturnType() {
 functionBuilder.withName("myFunction")
 .withReturnType(new IntType(registry))
 .withInferredReturnType(new IntType(registry));
 FunctionType functionType = functionBuilder.build();
 assertEquals("myFunction", functionType.getReferenceName());
 assertTrue(functionType instanceof FunctionType);
 assertNotNull(functionType.getReturnType());
 assertTrue(functionType.isInferredReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameAndTemplateTypeName() {
 functionBuilder.withName("myFunction")
 .withTemplateName("TemplateFunction");
 FunctionType functionType = functionBuilder.build();
 assertEquals("myFunction", functionType.getReferenceName());
 assertEquals("TemplateFunction", functionType.getTemplateTypeName());
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 functionBuilder.withName("myConstructor")
 .forConstructor();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType instanceof FunctionType);
 assertTrue(functionType.isConstructor());
 }

 @Test
 public void testFunctionBuilderForNativeType() {
 functionBuilder.forNativeType();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType instanceof FunctionType);
 assertTrue(functionType.isNativeType());
 }

 @Test
 public void testFunctionBuilderCopyFromOtherFunction() {
 FunctionType functionType1 = new FunctionType(registry, "function1", new ObjectLiteral(),
 new ArrowType(registry, new List(), new IntType(registry)),
 new ObjectType(registry, "Object"), null, true);
 functionBuilder.copyFromOtherFunction(functionType1);
 FunctionType functionType2 = functionBuilder.build();
 assertEquals(functionType1.getReferenceName(), functionType2.getReferenceName());
 assertEquals(functionType1.getSource(), functionType2.getSource());
 assertEquals(functionType1.getParametersNode(), functionType2.getParametersNode());
 assertEquals(functionType1.getReturnType(), functionType2.getReturnType());
 assertEquals(functionType1.getTypeOfThis(), functionType2.getTypeOfThis());
 assertEquals(functionType1.getTemplateTypeName(), functionType2.getTemplateTypeName());
 assertEquals(functionType1.isConstructor(), functionType2.isConstructor());
 assertEquals(functionType1.isNativeType(), functionType2.isNativeType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFunctionBuilderWithInvalidParameters() {
 functionBuilder.withName("myFunction")
 .withParamsNode(null);
 functionBuilder.build();
 }

}