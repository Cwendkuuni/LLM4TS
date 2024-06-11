import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
 String name = "myFunction";
 JSType returnType = registry.createBooleanType();
 FunctionType expectedFunctionType = new FunctionType(registry, name, null, null, null, null, false, false);

 functionBuilder.withName(name);
 functionBuilder.withReturnType(returnType);
 FunctionType actualFunctionType = functionBuilder.build();

 assertEquals(expectedFunctionType, actualFunctionType);
 }

 @Test
 public void testFunctionBuilderWithNameParametersAndReturnType() {
 String name = "myFunction";
 Node parametersNode = Node.newList();
 parametersNode.addElement(Node.newName("param1"));
 parametersNode.addElement(Node.newName("param2"));
 JSType returnType = registry.createNumberType();
 FunctionType expectedFunctionType = new FunctionType(registry, name, null, new ArrowType(registry, parametersNode, returnType, false), null, null, false, false);

 functionBuilder.withName(name);
 functionBuilder.withParams(new FunctionParamBuilder().withName("param1").build());
 functionBuilder.withParams(new FunctionParamBuilder().withName("param2").build());
 functionBuilder.withReturnType(returnType);
 FunctionType actualFunctionType = functionBuilder.build();

 assertEquals(expectedFunctionType, actualFunctionType);
 }

 @Test
 public void testFunctionBuilderWithNameParametersReturnTypeAndTypeOfThis() {
 String name = "myFunction";
 Node parametersNode = Node.newList();
 FunctionParamBuilder param1 = new FunctionParamBuilder().withName("param1").withType(registry.createStringType());
 FunctionParamBuilder param2 = new FunctionParamBuilder().withName("param2").withType(registry.createNumberType());
 parametersNode.addElement(param1.build());
 parametersNode.addElement(param2.build());
 JSType returnType = registry.createBooleanType();
 ObjectType typeOfThis = new ObjectType(registry, "MyObjectType");
 FunctionType expectedFunctionType = new FunctionType(registry, name, null, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, null, false, false);

 functionBuilder.withName(name);
 functionBuilder.withParams(param1);
 functionBuilder.withParams(param2);
 functionBuilder.withReturnType(returnType);
 functionBuilder.withTypeOfThis(typeOfThis);
 FunctionType actualFunctionType = functionBuilder.build();

 assertEquals(expectedFunctionType, actualFunctionType);
 }

 @Test
 public void testFunctionBuilderWithNameParametersReturnTypeTypeOfThisAndInferredReturnType() {
 String name = "myFunction";
 Node parametersNode = Node.newList();
 FunctionParamBuilder param1 = new FunctionParamBuilder().withName("param1").withType(registry.createStringType());
 FunctionParamBuilder param2 = new FunctionParamBuilder().withName("param2").withType(registry.createNumberType());
 parametersNode.addElement(param1.build());
 parametersNode.addElement(param2.build());
 JSType returnType = registry.createNumberType();
 ObjectType typeOfThis = new ObjectType(registry, "MyObjectType");
 functionBuilder.withName(name);
 functionBuilder.withParams(param1);
 functionBuilder.withParams(param2);
 functionBuilder.withReturnType(returnType);
 functionBuilder.withTypeOfThis(typeOfThis);
 functionBuilder.withInferredReturnType(returnType);
 FunctionType expectedFunctionType = new FunctionType(registry, name, null, new ArrowType(registry, parametersNode, returnType, true), typeOfThis, null, false, false);
 FunctionType actualFunctionType = functionBuilder.build();

 assertEquals(expectedFunctionType, actualFunctionType);
 }

 @Test
 public void testFunctionBuilderWithNameParametersReturnTypeTypeOfThisTemplateTypeNameAndIsConstructor() {
 String name = "myFunction";
 Node parametersNode = Node.newList();
 FunctionParamBuilder param1 = new FunctionParamBuilder().withName("param1").withType(registry.createStringType());
 FunctionParamBuilder param2 = new FunctionParamBuilder().withName("param2").withType(registry.createNumberType());
 parametersNode.addElement(param1.build());
 parametersNode.addElement(param2.build());
 JSType returnType = registry.createBooleanType();
 ObjectType typeOfThis = new ObjectType(registry, "MyObjectType");
 String templateTypeName = "MyTemplateTypeName";
 functionBuilder.withName(name);
 functionBuilder.withParams(param1);
 functionBuilder.withParams(param2);
 functionBuilder.withReturnType(returnType);
 functionBuilder.withTypeOfThis(typeOfThis);
 functionBuilder.withTemplateName(templateTypeName);
 functionBuilder.forConstructor();
 FunctionType expectedFunctionType = new FunctionType(registry, name, null, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, templateTypeName, true, false);
 FunctionType actualFunctionType = functionBuilder.build();

 assertEquals(expectedFunctionType, actualFunctionType);
 }

 @Test
 public void testFunctionBuilderWithNameParametersReturnTypeTypeOfThisTemplateTypeNameIsConstructorAndIsNativeType() {
 String name = "myFunction";
 Node parametersNode = Node.newList();
 FunctionParamBuilder param1 = new FunctionParamBuilder().withName("param1").withType(registry.createStringType());
 FunctionParamBuilder param2 = new FunctionParamBuilder().withName("param2").withType(registry.createNumberType());
 parametersNode.addElement(param1.build());
 parametersNode.addElement(param2.build());
 JSType returnType = registry.createBooleanType();
 ObjectType typeOfThis = new ObjectType(registry, "MyObjectType");
 String templateTypeName = "MyTemplateTypeName";
 functionBuilder.withName(name);
 functionBuilder.withParams(param1);
 functionBuilder.withParams(param2);
 functionBuilder.withReturnType(returnType);
 functionBuilder.withTypeOfThis(typeOfThis);
 functionBuilder.withTemplateName(templateTypeName);
 functionBuilder.forConstructor();
 functionBuilder.forNativeType();
 FunctionType expectedFunctionType = new FunctionType(registry, name, null, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, templateTypeName, true, true);
 FunctionType actualFunctionType = functionBuilder.build();

 assertEquals(expectedFunctionType, actualFunctionType);
 }

 @Test
 public void testFunctionBuilderWithEmptyConstructor() {
 FunctionType expectedFunctionType = new FunctionType(registry, null, null, null, null, null, true, false);
 functionBuilder.forConstructor();
 FunctionType actualFunctionType = functionBuilder.build();

 assertEquals(expectedFunctionType, actualFunctionType);
 }

 @Test
 public void testFunctionBuilderWithEmptyFunction() {
 FunctionType expectedFunctionType = new FunctionType(registry, null, null, null, null, null, false, false);
 FunctionType actualFunctionType = functionBuilder.build();

 assertEquals(expectedFunctionType, actualFunctionType);
 }
}