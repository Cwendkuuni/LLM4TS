import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FunctionBuilderTest {

 private FunctionBuilder functionBuilder;
 private FunctionType functionType;
 private ObjectType objectType;
 private JSTypeRegistry registry;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 functionBuilder = new FunctionBuilder(registry);
 objectType = new ObjectType(registry, "ObjectType");
 }

 @Test
 public void testFunctionBuilderWithNameAndReturnType() {
 functionType = functionBuilder.withName("add")
 .withReturnType(registry.getBooleanType())
 .build();

 assertEquals("add", functionType.getReferenceName());
 assertEquals(registry.getBooleanType(), functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameAndParametersAndReturnType() {
 Node parametersNode = new Node_ArrayLiteral(registry, new Node[] {
 new Node_Identifier("a"), new Node_Identifier("b") });
 functionType = functionBuilder.withName("add")
 .withParams(new FunctionParamBuilder(registry).withName("a").build())
 .withParams(new FunctionParamBuilder(registry).withName("b").build())
 .withReturnType(registry.getNumberType())
 .build();

 assertEquals("add", functionType.getReferenceName());
 assertEquals(new ArrowType(registry, new Node[] {
 new Node_Identifier("a"), new Node_Identifier("b") },
 registry.getNumberType(), false), functionType.getPrototype().getCallSignature());
 }

 @Test
 public void testFunctionBuilderWithNameAndParametersAndReturnTypeAndThisType() {
 functionType = functionBuilder.withName("add")
 .withParams(new FunctionParamBuilder(registry).withName("a").build())
 .withParams(new FunctionParamBuilder(registry).withName("b").build())
 .withReturnType(registry.getNumberType())
 .withTypeOfThis(objectType)
 .build();

 assertEquals("add", functionType.getReferenceName());
 assertEquals(new ArrowType(registry, new Node[] {
 new Node_Identifier("a"), new Node_Identifier("b") },
 registry.getNumberType(), false), functionType.getPrototype().getCallSignature());
 assertEquals(objectType, functionType.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderWithInferredReturnType() {
 functionType = functionBuilder.withName("add")
 .withParams(new FunctionParamBuilder(registry).withName("a").build())
 .withParams(new FunctionParamBuilder(registry).withName("b").build())
 .withInferredReturnType(registry.getNumberType())
 .build();

 assertEquals("add", functionType.getReferenceName());
 assertEquals(registry.getNumberType(), functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameAndParametersAndReturnTypeAndTemplateTypeName() {
 functionType = functionBuilder.withName("add")
 .withParams(new FunctionParamBuilder(registry).withName("a").build())
 .withParams(new FunctionParamBuilder(registry).withName("b").build())
 .withReturnType(registry.getNumberType())
 .withTemplateName("MathFunction")
 .build();

 assertEquals("add", functionType.getReferenceName());
 assertEquals(new ArrowType(registry, new Node[] {
 new Node_Identifier("a"), new Node_Identifier("b") },
 registry.getNumberType(), false), functionType.getPrototype().getCallSignature());
 assertEquals("MathFunction", functionType.getTemplateTypeName());
 }

 @Test
 public void testFunctionBuilderWithNameAndParametersAndReturnTypeAndIsConstructor() {
 functionType = functionBuilder.withName("add")
 .withParams(new FunctionParamBuilder(registry).withName("a").build())
 .withParams(new FunctionParamBuilder(registry).withName("b").build())
 .withReturnType(registry.getNumberType())
 .forConstructor()
 .build();

 assertEquals("add", functionType.getReferenceName());
 assertTrue(functionType.isConstructor());
 }

 @Test
 public void testFunctionBuilderWithNameAndParametersAndReturnTypeAndIsNativeType() {
 functionType = functionBuilder.withName("add")
 .withParams(new FunctionParamBuilder(registry).withName("a").build())
 .withParams(new FunctionParamBuilder(registry).withName("b").build())
 .withReturnType(registry.getNumberType())
 .forNativeType()
 .build();

 assertEquals("add", functionType.getReferenceName());
 assertTrue(functionType.isNativeObjectType());
 }
}