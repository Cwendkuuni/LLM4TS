import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import org.junit.Test;

public class FunctionBuilderTest {

 private JSTypeRegistry registry;
 private FunctionBuilder functionBuilder;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 functionBuilder = new FunctionBuilder(registry);
 }

 /**
 * Test method for {@link FunctionBuilder#FunctionBuilder(JSTypeRegistry)}
 */
 @Test
 public void testFunctionBuilderConstructor() {
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
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

 /**
 * Test method for {@link FunctionBuilder#withName(String)}
 */
 @Test
 public void testWithName() {
 functionBuilder.withName("testFunction");
 assertEquals("testFunction", functionBuilder.getName());
 }

 /**
 * Test method for {@link FunctionBuilder#withSourceNode(Node)}
 */
 @Test
 public void testWithSourceNode() {
 Node sourceNode = new Node();
 functionBuilder.withSourceNode(sourceNode);
 assertEquals(sourceNode, functionBuilder.getSourceNode());
 }

 /**
 * Test method for {@link FunctionBuilder#withParams(FunctionParamBuilder)}
 */
 @Test
 public void testWithParams() {
 FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(registry);
 functionBuilder.withParams(functionParamBuilder);
 assertNull(functionBuilder.getParametersNode());
 }

 /**
 * Test method for {@link FunctionBuilder#withParamsNode(Node)}
 */
 @Test
 public void testWithParamsNode() {
 Node parametersNode = new Node();
 functionBuilder.withParamsNode(parametersNode);
 assertEquals(parametersNode, functionBuilder.getParametersNode());
 }

 /**
 * Test method for {@link FunctionBuilder#withReturnType(JSType)}
 */
 @Test
 public void testWithReturnType() {
 JSType returnType = new IntType(registry);
 functionBuilder.withReturnType(returnType);
 assertEquals(returnType, functionBuilder.getReturnType());
 }

 /**
 * Test method for {@link FunctionBuilder#withInferredReturnType(JSType)}
 */
 @Test
 public void testWithInferredReturnType() {
 JSType returnType = new IntType(registry);
 functionBuilder.withInferredReturnType(returnType);
 assertTrue(functionBuilder.isInferredReturnType());
 assertEquals(returnType, functionBuilder.getReturnType());
 }

 /**
 * Test method for {@link FunctionBuilder#withTypeOfThis(ObjectType)}
 */
 @Test
 public void testWithTypeOfThis() {
 ObjectType typeOfThis = new ObjectType(registry, "TestObject");
 functionBuilder.withTypeOfThis(typeOfThis);
 assertEquals(typeOfThis, functionBuilder.getTypeOfThis());
 }

 /**
 * Test method for {@link FunctionBuilder#withTemplateName(String)}
 */
 @Test
 public void testWithTemplateName() {
 functionBuilder.withTemplateName("TestTemplate");
 assertEquals("TestTemplate", functionBuilder.getTemplateTypeName());
 }

 /**
 * Test method for {@link FunctionBuilder#forConstructor()}
 */
 @Test
 public void testForConstructor() {
 functionBuilder.forConstructor();
 assertTrue(functionBuilder.isConstructor());
 }

 /**
 * Test method for {@link FunctionBuilder#forNativeType()}
 */
 @Test
 public void testForNativeType() {
 functionBuilder.forNativeType();
 assertTrue(functionBuilder.isNativeType());
 }

 /**
 * Test method for {@link FunctionBuilder#copyFromOtherFunction(FunctionType)}
 */
 @Test
 public void testCopyFromOtherFunction() {
 FunctionType otherType = new FunctionType(registry, "otherFunction", null,
     new ArrowType(registry, null, new IntType(registry), false), null, null, true, false);
 functionBuilder.copyFromOtherFunction(otherType);
 assertEquals("otherFunction", functionBuilder.getName());
 assertNotNull(functionBuilder.getSourceNode());
 assertNotNull(functionBuilder.getParametersNode());
 assertEquals(otherType.getReturnType(), functionBuilder.getReturnType());
 assertEquals(otherType.getTypeOfThis(), functionBuilder.getTypeOfThis());
 assertEquals(otherType.getTemplateTypeName(), functionBuilder.getTemplateTypeName());
 assertTrue(functionBuilder.isConstructor());
 assertTrue(functionBuilder.isNativeType());
 }

 /**
 * Test method for {@link FunctionBuilder#build()}
 */
 @Test
 public void testBuild() {
 FunctionType functionType = functionBuilder.build();
 assertNotNull(functionType);
 assertNotNull(functionType.getReferenceName());
 assertNotNull(functionType.getSource());
 assertNotNull(functionType.getParametersNode());
 assertNotNull(functionType.getReturnType());
 assertNull(functionType.getTypeOfThis());
 assertNull(functionType.getTemplateTypeName());
 assertFalse(functionType.isConstructor());
 assertFalse(functionType.isNativeObjectType());
 }

 /**
 * Test method for edge case: empty FunctionBuilder
 */
 @Test
 public void testEmptyFunctionBuilder() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.copyFromOtherFunction(null);
 FunctionType functionType = functionBuilder.build();
 assertNotNull(functionType);
 assertNull(functionType.getReferenceName());
 assertNull(functionType.getSource());
 assertNull(functionType.getParametersNode());
 assertNull(functionType.getReturnType());
 assertNull(functionType.getTypeOfThis());
 assertNull(functionType.getTemplateTypeName());
 assertFalse(functionType.isConstructor());
 assertFalse(functionType.isNativeObjectType());
 }

 /**
 * Test method for edge case: FunctionBuilder with null return type
 */
 @Test
 public void testFunctionBuilderWithNullReturnType() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withReturnType(null);
 FunctionType functionType = functionBuilder.build();
 assertNotNull(functionType);
 assertNull(functionType.getReturnType());
 }

 /**
 * Test method for edge case: FunctionBuilder with null typeOfThis
 */
 @Test
 public void testFunctionBuilderWithNullTypeOfThis() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withTypeOfThis(null);
 FunctionType functionType = functionBuilder.build();
 assertNotNull(functionType);
 assertNull(functionType.getTypeOfThis());
 }

 /**
 * Test method for edge case: FunctionBuilder with empty templateTypeName
 */
 @Test
 public void testFunctionBuilderWithEmptyTemplateTypeName() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withTemplateName("");
 FunctionType functionType = functionBuilder.build();
 assertNotNull(functionType);
 assertNull(functionType.getTemplateTypeName());
 }

 /**
 * Test method for exception handling: FunctionBuilder with null registry
 */
 @Test(expected = NullPointerException.class)
 public void testFunctionBuilderWithNullRegistry() {
 functionBuilder = new FunctionBuilder(null);
 }
}