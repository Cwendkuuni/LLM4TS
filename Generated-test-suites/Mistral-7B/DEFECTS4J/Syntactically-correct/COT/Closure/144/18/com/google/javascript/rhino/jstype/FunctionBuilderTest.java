import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionBuilder;

public class FunctionBuilderTest {

 private FunctionBuilder functionBuilder;
 private FunctionType expectedFunctionType;

 @Before
 public void setUp() {
 JSTypeRegistry registry = new JSTypeRegistry();
 this.functionBuilder = new FunctionBuilder(registry);

 // Set up a function type with some basic values
 this.functionBuilder.withName("testFunction");
 Node parametersNode = FunctionParamBuilder.createList(
 new FunctionParamBuilder().withName("param1").withType(Int.TYPE),
 new FunctionParamBuilder().withName("param2").withType(String.TYPE)
 ).build();
 this.functionBuilder.withParams(parametersNode);
 this.functionBuilder.withReturnType(String.TYPE);
 this.expectedFunctionType = this.functionBuilder.build();
 }

 @Test
 public void testFunctionBuilderWithName() {
 String name = "testFunction";
 FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withName(name);
 FunctionType result = functionBuilder.build();
 assertEquals(name, result.getReferenceName());
 }

 @Test
 public void testFunctionBuilderWithSourceNode() {
 Node sourceNode = new Node();
 FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withSourceNode(sourceNode);
 FunctionType result = functionBuilder.build();
 assertEquals(sourceNode, result.getSource());
 }

 @Test
 public void testFunctionBuilderWithParams() {
 FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 FunctionParamBuilder paramBuilder1 = FunctionParamBuilder.create("param1", Int.TYPE);
 FunctionParamBuilder paramBuilder2 = FunctionParamBuilder.create("param2", String.TYPE);
 functionBuilder.withParams(FunctionParamBuilder.createList(paramBuilder1, paramBuilder2).build());
 FunctionType result = functionBuilder.build();
 assertEquals(paramBuilder1.build().getType(), result.getParameters()[0].getType());
 assertEquals(paramBuilder2.build().getType(), result.getParameters()[1].getType());
 }

 @Test
 public void testFunctionBuilderWithParamsNode() {
 Node parametersNode = FunctionParamBuilder.createList(
 new FunctionParamBuilder().withName("param1").withType(Int.TYPE),
 new FunctionParamBuilder().withName("param2").withType(String.TYPE)
 ).build();
 FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withParamsNode(parametersNode);
 FunctionType result = functionBuilder.build();
 assertEquals(parametersNode, result.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithReturnType() {
 JSType returnType = String.TYPE;
 FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withReturnType(returnType);
 FunctionType result = functionBuilder.build();
 assertEquals(returnType, result.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithInferredReturnType() {
 JSType returnType = Int.TYPE;
 FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withReturnType(returnType);
 functionBuilder.withInferredReturnType(returnType);
 FunctionType result = functionBuilder.build();
 assertTrue(result.isInferredReturnType());
 }

 @Test
 public void testFunctionBuilderWithTypeOfThis() {
 ObjectType typeOfThis = new ObjectType(new JSTypeRegistry(), "Object");
 FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withTypeOfThis(typeOfThis);
 FunctionType result = functionBuilder.build();
 assertEquals(typeOfThis, result.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderWithTemplateName() {
 String templateTypeName = "Template";
 FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.withTemplateName(templateTypeName);
 FunctionType result = functionBuilder.build();
 assertEquals(templateTypeName, result.getTemplateTypeName());
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
 functionBuilder.forConstructor();
 FunctionType result = functionBuilder.build();
 assertTrue(result.isConstructor());
 }

 @Test
 public void testFunctionBuilderForNativeType() {
 FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());functionBuilder.forNativeType(); FunctionType result = functionBuilder.build(); assertTrue(result.isNativeType()); }

 @Test
 public void testFunctionBuilderCopyFromOtherFunction() {
 FunctionType otherFunctionType = new FunctionType(new JSTypeRegistry(), "otherFunction", null, new ArrowType(new JSTypeRegistry(), null, String.TYPE, false), null, null, true, false); FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry()); functionBuilder.copyFromOtherFunction(otherFunctionType); FunctionType result = functionBuilder.build(); assertEquals(otherFunctionType.getReferenceName(), result.getReferenceName()); assertEquals(otherFunctionType.getSource(), result.getSource()); assertEquals(otherFunctionType.getParametersNode(), result.getParametersNode()); assertEquals(otherFunctionType.getReturnType(), result.getReturnType()); assertEquals(otherFunctionType.getTypeOfThis(), result.getTypeOfThis()); assertEquals(otherFunctionType.getTemplateTypeName(), result.getTemplateTypeName()); assertTrue(otherFunctionType.isConstructor(), result.isConstructor()); assertTrue(otherFunctionType.isNativeType(), result.isNativeType()); }

 @Test
 public void testFunctionBuilderBuild() {
 FunctionType result = this.functionBuilder.build(); assertEquals(this.expectedFunctionType.getReferenceName(), result.getReferenceName()); assertEquals(this.expectedFunctionType.getSource(), result.getSource()); Node[] expectedParameters = this.expectedFunctionType.getParameters(); Node[] resultParameters = result.getParameters(); assertArrayEquals(expectedParameters, resultParameters); assertEquals(this.expectedFunctionType.getReturnType(), result.getReturnType()); assertEquals(this.expectedFunctionType.getTypeOfThis(), result.getTypeOfThis()); assertEquals(this.expectedFunctionType.getTemplateTypeName(), result.getTemplateTypeName()); assertTrue(this.expectedFunctionType.isConstructor(), result.isConstructor()); assertTrue(this.expectedFunctionType.isNativeType(), result.isNativeType()); }

}