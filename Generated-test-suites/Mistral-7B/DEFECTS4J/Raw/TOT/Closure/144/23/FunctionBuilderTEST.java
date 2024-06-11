import org.junit.Test;
import org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionBuilder;

public class FunctionBuilderTest {

 private JSTypeRegistry registry;

 @BeforeClass
 public static void setUp() {
 registry = new JSTypeRegistry();
 }

 @Test
 public void testFunctionBuilder() {
 // Test creating a simple function type
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(registry, "param1", JSType.INT);
 FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder(registry, "param2", JSType.STRING);
 functionBuilder.withName("myFunction")
 .withParams(paramBuilder1.build())
 .withParams(paramBuilder2.build())
 .withReturnType(JSType.BOOLEAN);
 FunctionType functionType = functionBuilder.build();

 // Test getters
 assertEquals("myFunction", functionType.getReferenceName());
 assertEquals(functionBuilder.sourceNode, functionType.getSource());
 assertEquals(functionBuilder.parametersNode, functionType.getParametersNode());
 assertEquals(functionBuilder.returnType, functionType.getReturnType());
 assertEquals(functionBuilder.typeOfThis, functionType.getTypeOfThis());
 assertEquals(functionBuilder.templateTypeName, functionType.getTemplateTypeName());
 assertEquals(functionBuilder.isConstructor, functionType.isConstructor());
 assertEquals(functionBuilder.isNativeType, functionType.isNativeObjectType());

 // Test building a function type with no name or source node
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.forConstructor();
 functionBuilder.withParams(paramBuilder1.build())
 .withParams(paramBuilder2.build())
 .withReturnType(JSType.BOOLEAN);
 FunctionType constructorType = functionBuilder.build();
 assertNull(constructorType.getReferenceName());
 assertNull(constructorType.getSource());

 // Test building a function type with no return type
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("myFunction")
 .withParams(paramBuilder1.build())
 .withParams(paramBuilder2.build());
 try {
 functionBuilder.build();
 fail("Expected an exception when no return type is specified");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test building a function type with an invalid return type
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("myFunction")
 .withParams(paramBuilder1.build())
 .withParams(paramBuilder2.build())
 .withReturnType(JSType.NULL);
 try {
 functionBuilder.build();
 fail("Expected an exception when an invalid return type is specified");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test building a function type with an invalid parameter type
 functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("myFunction")
 .withParams(paramBuilder1.build())
 .forNativeType()
 .withParamsNode(new Node()) // An empty node is not a valid parameters node
 .withReturnType(JSType.BOOLEAN);
 try {
 functionBuilder.build();
 fail("Expected an exception when an invalid parameters node is specified");
 } catch (IllegalArgumentException e) {
 // Expected
 }
}
}