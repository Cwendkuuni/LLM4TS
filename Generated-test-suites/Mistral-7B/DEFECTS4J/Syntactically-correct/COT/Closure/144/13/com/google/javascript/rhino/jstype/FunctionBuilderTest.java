import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.types.ObjectType;
import com.google.javascript.rhino.types.FunctionType;
import com.google.javascript.rhino.types.ArrowType;

public class FunctionBuilderTest {

 private JSTypeRegistry registry;
 private FunctionBuilder functionBuilder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 functionBuilder = new FunctionBuilder(registry);
 }

 @Test
 public void testFunctionBuilderWithName() {
 FunctionBuilder funcBuilder = new FunctionBuilder(registry).withName("testFunction");
 assertNotNull(funcBuilder.getName());
 }

 @Test
 public void testFunctionBuilderWithSourceNode() {
 Node node = new Node();
 FunctionBuilder funcBuilder = new FunctionBuilder(registry).withSourceNode(node);
 assertNotNull(funcBuilder.getSourceNode());
 }

 @Test
 public void testFunctionBuilderWithParams() {
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 FunctionBuilder funcBuilder = new FunctionBuilder(registry)
 .withParams(paramBuilder);
 assertNotNull(funcBuilder.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithParamsNode() {
 Node parametersNode = new Node();
 FunctionBuilder funcBuilder = new FunctionBuilder(registry)
 .withParamsNode(parametersNode);
 assertNotNull(funcBuilder.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithReturnType() {
 JSType returnType = new JSType();
 FunctionBuilder funcBuilder = new FunctionBuilder(registry)
 .withReturnType(returnType);
 assertNotNull(funcBuilder.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithInferredReturnType() {
 JSType returnType = new JSType();
 FunctionBuilder funcBuilder = new FunctionBuilder(registry)
 .withReturnType(returnType).withInferredReturnType(returnType);
 assertNotNull(funcBuilder.getReturnType());
 assertTrue(funcBuilder.isInferredReturnType());
 }

 @Test
 public void testFunctionBuilderWithTypeOfThis() {
 ObjectType typeOfThis = new ObjectType("Object");
 FunctionBuilder funcBuilder = new FunctionBuilder(registry)
 .withTypeOfThis(typeOfThis);
 assertNotNull(funcBuilder.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderWithTemplateName() {
 FunctionBuilder funcBuilder = new FunctionBuilder(registry)
 .withTemplateName("testTemplate");
 assertNotNull(funcBuilder.getTemplateTypeName());
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 FunctionBuilder funcBuilder = new FunctionBuilder(registry).forConstructor();
 assertTrue(funcBuilder.isConstructor());
 }

 @Test
 public void testFunctionBuilderForNativeType() {
 FunctionBuilder funcBuilder = new FunctionBuilder(registry).forNativeType();
 assertTrue(funcBuilder.isNativeType());
 }

 @Test
 public void testFunctionBuilderCopyFromOtherFunction() {
 FunctionType otherType = new FunctionType(registry, "testFunction",
 new Node(), new ArrowType(registry, new Node(), new JSType(), false),
 new ObjectType("Object"), "testTemplate", true, false);
 FunctionBuilder funcBuilder = new FunctionBuilder(registry)
 .copyFromOtherFunction(otherType);
 assertNotNull(funcBuilder.getName());
 assertNotNull(funcBuilder.getSourceNode());
 assertNotNull(funcBuilder.getParametersNode());
 assertNotNull(funcBuilder.getReturnType());
 assertNotNull(funcBuilder.getTypeOfThis());
 assertNotNull(funcBuilder.getTemplateTypeName());
 assertTrue(funcBuilder.isConstructor());
 assertFalse(funcBuilder.isNativeType());
 }

 @Test
 public void testFunctionBuilderBuild() {
 FunctionBuilder funcBuilder = new FunctionBuilder(registry)
 .withName("testFunction")
 .withSourceNode(new Node())
 .withParams(new FunctionParamBuilder().withName("param1").build())
 .withReturnType(new JSType());
 FunctionType funcType = funcBuilder.build();
 assertNotNull(funcType);
 }

 @Test(expected = IllegalStateException.class)
 public void testFunctionBuilderBuildWithNullParamsNode() {
 FunctionBuilder funcBuilder = new FunctionBuilder(registry)
 .withName("testFunction")
 .withSourceNode(new Node())
 .withParamsNode(null);
 FunctionType funcType = funcBuilder.build();
 }

 @Test(expected = IllegalStateException.class)
 public void testFunctionBuilderBuildWithNullReturnType() {
 FunctionBuilder funcBuilder = new FunctionBuilder(registry)
 .withName("testFunction")
 .withSourceNode(new Node())
 .withParams(new FunctionParamBuilder().build())
 .withReturnType(null);
 FunctionType funcType = funcBuilder.build();
 }
}