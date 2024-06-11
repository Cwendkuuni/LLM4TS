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

 @Test
 public void testWithName() {
 functionBuilder.withName("testFunction");
 FunctionType functionType = functionBuilder.build();
 assertEquals("testFunction", functionType.getReferenceName());
 }

 @Test
 public void testWithSourceNode() {
 Node sourceNode = new Node();
 functionBuilder.withSourceNode(sourceNode);
 FunctionType functionType = functionBuilder.build();
 assertEquals(sourceNode, functionType.getSource());
 }

 @Test
 public void testWithParams() {
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 paramBuilder.withName("param1").withType(registry.createStringType());
 functionBuilder.withParams(paramBuilder);
 FunctionType functionType = functionBuilder.build();
 assertEquals(paramBuilder.build(), functionType.getParameters());
 }

 @Test
 public void testWithParamsNode() {
 Node parametersNode = new Node();
 functionBuilder.withParamsNode(parametersNode);
 FunctionType functionType = functionBuilder.build();
 assertEquals(parametersNode, functionType.getParametersNode());
 }

 @Test
 public void testWithReturnType() {
 JSType returnType = registry.createNumberType();
 functionBuilder.withReturnType(returnType);
 FunctionType functionType = functionBuilder.build();
 assertEquals(returnType, functionType.getReturnType());
 }

 @Test
 public void testWithInferredReturnType() {
 JSType returnType = registry.createNumberType();
 functionBuilder.withInferredReturnType(returnType);
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isInferredReturnType());
 }

 @Test
 public void testWithTypeOfThis() {
 ObjectType typeOfThis = registry.createObjectType();
 functionBuilder.withTypeOfThis(typeOfThis);
 FunctionType functionType = functionBuilder.build();
 assertEquals(typeOfThis, functionType.getTypeOfThis());
 }

 @Test
 public void testWithTemplateName() {
 functionBuilder.withTemplateName("MyTemplate");
 FunctionType functionType = functionBuilder.build();
 assertEquals("MyTemplate", functionType.getTemplateTypeName());
 }

 @Test
 public void testForConstructor() {
 functionBuilder.forConstructor();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isConstructor());
 }

 @Test
 public void testForNativeType() {
 functionBuilder.forNativeType();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isNativeObjectType());
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildWithNullRegistry() {
 functionBuilder = new FunctionBuilder(null);
 functionBuilder.build();
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildWithNullName() {
 functionBuilder.withName(null);
 functionBuilder.build();
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildWithNullSourceNode() {
 functionBuilder.withSourceNode(null);
 functionBuilder.build();
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildWithNullParametersNode() {
 functionBuilder.withParamsNode(null);
 functionBuilder.build();
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildWithNullReturnType() {
 functionBuilder.withReturnType(null);
 functionBuilder.build();
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildWithNullTypeOfThis() {
 functionBuilder.withTypeOfThis(null);
 functionBuilder.build();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithInvalidTemplateName() {
 functionBuilder.withTemplateName("123");
 functionBuilder.build();
 }
}