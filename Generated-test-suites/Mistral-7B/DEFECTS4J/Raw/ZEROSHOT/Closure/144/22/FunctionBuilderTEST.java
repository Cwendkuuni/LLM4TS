import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class FunctionBuilderTest {

 private JSTypeRegistry registry;
 private FunctionBuilder functionBuilder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 functionBuilder = new FunctionBuilder(registry);
 }

 @Test
 public void testFunctionBuilderWithNameAndReturnType() {
 functionBuilder.withName("testFunction")
 .withReturnType(String.class);
 FunctionType functionType = functionBuilder.build();
 assertEquals("testFunction", functionType.getReferenceName());
 assertSame(String.class, functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameAndParamsAndReturnType() {
 Node paramsNode = new Node_CallExpr(
 new Node_Ident("param1"),
 new Node_Array()[new Node_Ident("param2")]
 );
 functionBuilder.withName("testFunction")
 .withParams(new FunctionParamBuilder().withName("param1").build())
 .withParams(new FunctionParamBuilder().withName("param2").build())
 .withReturnType(String.class);
 FunctionType functionType = functionBuilder.build();
 assertEquals("testFunction", functionType.getReferenceName());
 assertEquals(NodeOperations.getChildNodes(paramsNode).size(), functionType.getParameterCount());
 assertSame(String.class, functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameAndParamsAndReturnTypeAndTypeOfThis() {
 functionBuilder.withName("testFunction")
 .withParams(new FunctionParamBuilder().withName("param1").build())
 .withParams(new FunctionParamBuilder().withName("param2").build())
 .withReturnType(String.class)
 .withTypeOfThis(JSType.getObjectType("Object"))
 .build();
 FunctionType functionType = functionRegistry.getFunctionType("testFunction");
 assertEquals("testFunction", functionType.getReferenceName());
 assertEquals(2, functionType.getParameterCount());
 assertSame(String.class, functionType.getReturnType());
 assertSame(JSType.getObjectType("Object"), functionType.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderWithNameAndParamsAndReturnTypeAndTemplateTypeName() {
 functionBuilder.withName("testFunction")
 .withParams(new FunctionParamBuilder().withName("param1").build())
 .withParams(new FunctionParamBuilder().withName("param2").build())
 .withReturnType(String.class)
 .withTemplateName("TemplateFunction")
 .build();
 FunctionType functionType = functionRegistry.getFunctionType("testFunction");
 assertEquals("testFunction", functionType.getReferenceName());
 assertEquals(2, functionType.getParameterCount());
 assertSame(String.class, functionType.getReturnType());
 assertEquals("TemplateFunction", functionType.getTemplateTypeName());
 }

 @Test
 public void testFunctionBuilderWithNameAndParamsAndReturnTypeAndInferredReturnType() {
 functionBuilder.withName("testFunction")
 .withParams(new FunctionParamBuilder().withName("param1").build())
 .withParams(new FunctionParamBuilder().withName("param2").build())
 .withReturnType(Object.class)
 .withInferredReturnType(Boolean.class);
 FunctionType functionType = functionBuilder.build();
 assertEquals("testFunction", functionType.getReferenceName());
 assertEquals(2, functionType.getParameterCount());
 assertSame(Object.class, functionType.getReturnType());
 assertTrue(functionType.isInferredReturnType());
 assertFalse(functionType.isExactReturnType());
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 functionBuilder.forConstructor();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isConstructor());
 }

 @Test
 public void testFunctionBuilderForNativeType() {
 functionBuilder.forNativeType();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isNativeObjectType());
 }

}