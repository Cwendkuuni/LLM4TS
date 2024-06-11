import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

 private FunctionBuilder functionBuilder;
 private FunctionType functionType;
 private JSTypeRegistry registry;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 functionBuilder = new FunctionBuilder(registry);
 }

 @Test
 public void testFunctionBuilderWithName() {
 functionType = functionBuilder.withName("testFunction")
 .build();
 assertEquals("testFunction", functionType.getReferenceName());
 }

 @Test
 public void testFunctionBuilderWithSourceNode() {
 Node sourceNode = new Node();
 functionType = functionBuilder.withSourceNode(sourceNode)
 .build();
 assertEquals(sourceNode, functionType.getSource());
 }

 @Test
 public void testFunctionBuilderWithParams() {
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 paramBuilder.withName("param1").withType(new IntType(registry));
 Node paramsNode = paramBuilder.build();
 functionType = functionBuilder.withParamsNode(paramsNode)
 .build();
 assertEquals(paramsNode, functionType.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithReturnType() {
 functionType = functionBuilder.withReturnType(new IntType(registry))
 .build();
 assertEquals(new IntType(registry), functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithInferredReturnType() {
 functionType = functionBuilder.withInferredReturnType(new IntType(registry))
 .build();
 assertTrue(functionType.isInferredReturnType());
 }

 @Test
 public void testFunctionBuilderWithTypeOfThis() {
 ObjectType typeOfThis = new ObjectType(registry, "Object");
 functionType = functionBuilder.withTypeOfThis(typeOfThis)
 .build();
 assertEquals(typeOfThis, functionType.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 functionType = functionBuilder.forConstructor().build();
 assertTrue(functionType.isConstructor());
 }

 @Test(expected = IllegalStateException.class)
 public void testFunctionBuilderForConstructorWithoutName() {
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.forConstructor();
 }

 @Test
 public void testFunctionBuilderCopyFromOtherFunction() {
 FunctionType otherType = new FunctionType(registry, "otherFunction", null, null, null, null, false, false);
 functionBuilder.copyFromOtherFunction(otherType);
 assertEquals("otherFunction", functionType.getReferenceName());
 }

 @Test(expected = IllegalStateException.class)
 public void testFunctionBuilderWithoutRegistry() {
 FunctionBuilder functionBuilder = new FunctionBuilder(null);
 }
}