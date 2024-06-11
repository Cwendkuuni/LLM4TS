import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

 private FunctionBuilder functionBuilder;
 private FunctionParamBuilder functionParamBuilder;
 private JSTypeRegistry registry;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 functionParamBuilder = new FunctionParamBuilder(registry);
 functionBuilder = new FunctionBuilder(registry);
 }

 // Test for withName method
 @Test
 public void testWithName() {
 String name = "testFunction";
 FunctionBuilder functionBuilder = new FunctionBuilder(registry).withName(name);
 assertEquals(name, functionBuilder.build().getName());
 }

 // Test for withSourceNode method
 @Test
 public void testWithSourceNode() {
 Node sourceNode = new Node();
 FunctionBuilder functionBuilder = new FunctionBuilder(registry).withSourceNode(sourceNode);
 assertEquals(sourceNode, functionBuilder.build().getSource());
 }

 // Test for withParams method
 @Test
 public void testWithParams() {
 FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(registry);
 functionParamBuilder.withName("paramName").withType(new StringType(registry));
 Node parametersNode = functionParamBuilder.build();
 FunctionBuilder functionBuilder = new FunctionBuilder(registry).withParams(functionParamBuilder);
 FunctionType functionType = functionBuilder.build();
 assertEquals(1, functionType.getParameterCount());
 assertEquals("paramName", functionType.getParameter(0).getName());
 }

 // Test for withParamsNode method
 @Test(expected = AssertionError.class)
 public void testWithParamsNodeNull() {
 FunctionBuilder functionBuilder = new FunctionBuilder(registry).withParamsNode(null);
 }

 // Test for withParamsNode with valid Node
 @Test
 public void testWithParamsNode() {
 Node parametersNode = new Node();
 FunctionBuilder functionBuilder = new FunctionBuilder(registry).withParamsNode(parametersNode);
 FunctionType functionType = functionBuilder.build();
 assertEquals(parametersNode, functionType.getParametersNode());
 }

 // Test for withReturnType method
 @Test
 public void testWithReturnType() {
 JSType returnType = new NumberType(registry);
 FunctionBuilder functionBuilder = new FunctionBuilder(registry).withReturnType(returnType);
 FunctionType functionType = functionBuilder.build();
 assertEquals(returnType, functionType.getReturnType());
 }

 // Test for withInferredReturnType method
 @Test
 public void testWithInferredReturnType() {
 JSType returnType = new VoidType(registry);
 FunctionBuilder functionBuilder = new FunctionBuilder(registry).withReturnType(returnType).withInferredReturnType(new StringType(registry));
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isInferredReturnType());
 }

 // Test for withTypeOfThis method
 @Test
 public void testWithTypeOfThis() {
 ObjectType typeOfThis = new ObjectType(registry);
 FunctionBuilder functionBuilder = new FunctionBuilder(registry).withTypeOfThis(typeOfThis);
 FunctionType functionType = functionBuilder.build();
 assertEquals(typeOfThis, functionType.getTypeOfThis());
 }

 // Test for forConstructor method
 @Test
 public void testForConstructor() {
 FunctionBuilder functionBuilder = new FunctionBuilder(registry).forConstructor();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isConstructor());
 }

 // Test for forNativeType method
 @Test
 public void testForNativeType() {
 FunctionBuilder functionBuilder = new FunctionBuilder(registry).forNativeType();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isNativeObjectType());
 }

 // Test for copyFromOtherFunction method
 @Test
 public void testCopyFromOtherFunction() {
 FunctionType otherType = new FunctionType(registry, "test", null, null, null, null, true, false);
 FunctionBuilder functionBuilder = new FunctionBuilder(registry).copyFromOtherFunction(otherType);
 FunctionType functionType = functionBuilder.build();
 assertEquals(otherType.getName(), functionType.getName());
 assertEquals(otherType.getSource(), functionType.getSource());
 assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
 assertEquals(otherType.getReturnType(), functionType.getReturnType());
 assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
 assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
 assertEquals(otherType.isConstructor(), functionType.isConstructor());
 assertEquals(otherType.isNativeObjectType(), functionType.isNativeObjectType());
 }
}