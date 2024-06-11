import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

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
 .withReturnType(registry.createBooleanType());
 FunctionType functionType = functionBuilder.build();
 assertEquals("myFunction", functionType.getReferenceName());
 assertEquals(registry.createBooleanType(), functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameAndParamsAndReturnType() {
 Node parametersNode = new FunctionParamBuilder(registry)
 .withName("param1")
 .withType(registry.createNumberType())
 .withDefaultValue(registry.createNull())
 .build();
 functionBuilder.withName("myFunction")
 .withParamsNode(parametersNode)
 .withReturnType(registry.createBooleanType());
 FunctionType functionType = functionBuilder.build();
 assertEquals("myFunction", functionType.getReferenceName());
 assertEquals(parametersNode, functionType.getParametersNode());
 assertEquals(registry.createBooleanType(), functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameAndParamsAndReturnTypeAndTypeOfThis() {
 Node parametersNode = new FunctionParamBuilder(registry)
 .withName("param1")
 .withType(registry.createNumberType())
 .build();
 functionBuilder.withName("myFunction")
 .withParamsNode(parametersNode)
 .withReturnType(registry.createBooleanType())
 .withTypeOfThis(registry.createStringType());
 FunctionType functionType = functionBuilder.build();
 assertEquals("myFunction", functionType.getReferenceName());
 assertEquals(parametersNode, functionType.getParametersNode());
 assertEquals(registry.createBooleanType(), functionType.getReturnType());
 assertEquals(registry.createStringType(), functionType.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderWithNameAndParamsAndReturnTypeAndTemplateTypeName() {
 Node parametersNode = new FunctionParamBuilder(registry)
 .withName("param1")
 .withType(registry.createNumberType())
 .build();
 functionBuilder.withName("myFunction")
 .withParamsNode(parametersNode)
 .withReturnType(registry.createBooleanType())
 .withTemplateName("MyTemplate");
 FunctionType functionType = functionBuilder.build();
 assertEquals("myFunction", functionType.getReferenceName());
 assertEquals(parametersNode, functionType.getParametersNode());
 assertEquals(registry.createBooleanType(), functionType.getReturnType());
 assertEquals("MyTemplate", functionType.getTemplateTypeName());
 }

 @Test
 public void testFunctionBuilderWithNameAndParamsAndInferredReturnType() {
 Node parametersNode = new FunctionParamBuilder(registry)
 .withName("param1")
 .withType(registry.createNumberType())
 .build();
 functionBuilder.withName("myFunction")
 .withParamsNode(parametersNode)
 .withInferredReturnType(registry.createBooleanType());
 FunctionType functionType = functionBuilder.build();
 assertEquals("myFunction", functionType.getReferenceName());
 assertEquals(parametersNode, functionType.getParametersNode());
 assertEquals(registry.createBooleanType(), functionType.getReturnType());
 assertTrue(functionType.isInferredReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameAndParamsAndReturnTypeAndIsConstructor() {
 Node parametersNode = new FunctionParamBuilder(registry)
 .withName("param1")
 .withType(registry.createNumberType())
 .build();
 functionBuilder.withName("myFunction")
 .withParamsNode(parametersNode)
 .withReturnType(registry.createBooleanType())
 .forConstructor();
 FunctionType functionType = functionBuilder.build();
 assertEquals("myFunction", functionType.getReferenceName());
 assertTrue(functionType.isConstructor());
 }

 @Test
 public void testFunctionBuilderWithNameAndParamsAndReturnTypeAndIsNativeType() {
 Node parametersNode = new FunctionParamBuilder(registry)
 .withName("param1")
 .withType(registry.createNumberType())
 .build();functionBuilder.withName("myFunction")
 .withParamsNode(parametersNode)
 .withReturnType(registry.createBooleanType())
 .forNativeType();
 FunctionType functionType = functionBuilder.build();
 assertEquals("myFunction", functionType.getReferenceName());
 assertTrue(functionType.isNativeType());
 }
}