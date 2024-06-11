import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

import static org.junit.Assert.assertNotNull;

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
 // Given
 String name = "add";
 JSType returnType = registry.createNumberType();

 // When
 FunctionBuilder function = functionBuilder
 .withName(name)
 .withReturnType(returnType);

 // Then
 FunctionType functionType = function.build();
 assertNotNull(functionType);
 assertNotNull(functionType.getName());
 assertNotNull(functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameAndParams() {
 // Given
 String name = "add";
 Node parametersNode = Node.newList(
 Node.newMap("name", Node.newIdentifier("num1")),
 Node.newMap("name", Node.newIdentifier("num2")));
 JSType returnType = registry.createNumberType();

 // When
 FunctionBuilder function = functionBuilder
 .withName(name)
 .withParamsNode(parametersNode)
 .withReturnType(returnType);

 // Then
 FunctionType functionType = function.build();
 assertNotNull(functionType);
 assertNotNull(functionType.getName());
 assertNotNull(functionType.getParametersNode());
 assertNotNull(functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameAndParamsAndReturnType() {
 // Given
 String name = "add";
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 paramBuilder.withName("num1").withType(registry.createNumberType());
 paramBuilder.withName("num2").withType(registry.createNumberType());
 Node parametersNode = paramBuilder.build();
 JSType returnType = registry.createNumberType();

 // When
 FunctionBuilder function = functionBuilder
 .withName(name)
 .withParams(paramBuilder)
 .withReturnType(returnType);

 // Then
 FunctionType functionType = function.build();
 assertNotNull(functionType);
 assertNotNull(functionType.getName());
 assertNotNull(functionType.getParametersNode());
 assertNotNull(functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameAndParamsAndReturnTypeAndTypeOfThis() {
 // Given
 String name = "add";
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 paramBuilder.withName("num1").withType(registry.createNumberType());
 paramBuilder.withName("num2").withType(registry.createNumberType());
 Node parametersNode = paramBuilder.build();
 JSType returnType = registry.createNumberType();
 ObjectType typeOfThis = new ObjectType("Object");

 // When
 FunctionBuilder function = functionBuilder
 .withName(name)
 .withParams(paramBuilder)
 .withReturnType(returnType)
 .withTypeOfThis(typeOfThis);

 // Then
 FunctionType functionType = function.build();
 assertNotNull(functionType);
 assertNotNull(functionType.getName());
 assertNotNull(functionType.getParametersNode());
 assertNotNull(functionType.getReturnType());
 assertNotNull(functionType.getTypeOfThis());
 }
}