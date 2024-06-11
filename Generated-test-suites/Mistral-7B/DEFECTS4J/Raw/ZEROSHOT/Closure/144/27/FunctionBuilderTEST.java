import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createLiteral;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createObjectLiteral;
import static com.google.javascript.rhino.NodeFactory.createProperty;
import static com.google.javascript.rhino.NodeFactory.createFunctionExpression;
import static com.google.javascript.rhino.NodeFactory.createArray;
import static com.google.javascript.rhino.NodeFactory.createCallExpression;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.NodeFactory.createVariable;
import static com.google.javascript.rhino.JSTypeFactory.createBooleanType;
import static com.google.javascript.rhino.JSTypeFactory.createNumberType;
import static com.google.javascript.rhino.JSTypeFactory.createStringType;
import static com.google.javascript.rhino.JSTypeFactory.createObjectType;
import static com.google.javascript.rhino.JSTypeFactory.createFunctionType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Source;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.FunctionParamBuilder;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.ArrowType;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.FunctionBuilder;

@RunWith(JUnit4.class)
public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();

    FunctionBuilder builder = new FunctionBuilder(registry);

    FunctionType functionType = builder
        .withName("add")
        .withSourceNode(createFunctionExpression(null, null, null, null))
        .withParams(new FunctionParamBuilder().withName("x").withType(createNumberType()).build())
        .withParams(new FunctionParamBuilder().withName("y").withType(createNumberType()).build())
        .withReturnType(createNumberType())
        .build();

    assertEquals("add", functionType.getReferenceName());
    assertNotNull(functionType.getSource());
    assertEquals(3, functionType.getParameterCount());
    assertEquals(createNumberType(), functionType.getParameterType(0));
    assertEquals(createNumberType(), functionType.getParameterType(1));
    assertEquals(createNumberType(), functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithTemplateTypeName() {
    JSTypeRegistry registry = new JSTypeRegistry();

    FunctionBuilder builder = new FunctionBuilder(registry);

    FunctionType functionType = builder
        .withName("add")
        .withTemplateName("BinaryOperator")
        .withSourceNode(createFunctionExpression(null, null, null, null))
        .withParams(new FunctionParamBuilder().withName("left")
            .withType(createNumberType())
            .build())
        .withParams(new FunctionParamBuilder().withName("right")
            .withType(createNumberType())
            .build())
        .withReturnType(createNumberType())
        .build();

    assertEquals("add", functionType.getReferenceName());
    assertNotNull(functionType.getTemplateTypeName());
    assertEquals("BinaryOperator", functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    JSTypeRegistry registry = new JSTypeRegistry();

    FunctionBuilder builder = new FunctionBuilder(registry);

    ObjectType objectType = new ObjectType(registry, "MyObject");

    FunctionType functionType = builder
        .withName("myMethod")
        .withSourceNode(createFunctionExpression(null, null, null, null))
        .withParams(new FunctionParamBuilder().withName("x").withType(createNumberType()).build())
        .withReturnType(createNumberType())
        .withTypeOfThis(objectType)
        .build();

    assertEquals("myMethod", functionType.getReferenceName());
    assertEquals(objectType, functionType.getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    FunctionBuilder builder = new FunctionBuilder(registry);

    FunctionType functionType = builder
        .withName("add")
        .withSourceNode(createFunctionExpression(null, null, null, null))
        .withParams(new FunctionParamBuilder().withName("x").withType(createNumberType()).build())
        .withParams(new FunctionParamBuilder().withName("y").withType(createNumberType()).build())
        .withInferredReturnType(createNumberType())
        .build();

    assertEquals(createNumberType(), functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();

    FunctionBuilder builder = new FunctionBuilder(registry);

    ObjectType constructorType = new ObjectType(registry, "MyConstructor");

    FunctionType constructorTypeFunction = builder
        .forConstructor()
        .withName("MyConstructor")
        .withSourceNode(createFunctionExpression(null, null, null, null))
        .withParams(new FunctionParamBuilder().withName("x").withType(createNumberType()).build())
        .withParams(new FunctionParamBuilder().withName("y").withType(createNumberType()).build())
        .withTypeOfThis(constructorType)
        .build();

    assertEquals("MyConstructor", constructorTypeFunction.getReferenceName());
    assertEquals(true, constructorTypeFunction.isConstructor());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    FunctionBuilder builder = new FunctionBuilder(registry);

    FunctionType nativeFunctionType = builder.forNativeType().build();

    assertEquals(true, nativeFunctionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();

    FunctionBuilder builder = new FunctionBuilder(registry);

    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();

    FunctionType functionType = new FunctionType(registry, "add", null,
        new ArrowType(registry,
            createObjectLiteral(createProperty("x", createNumberType()),
                createProperty("y", createNumberType())),
            createNumberType()), null, null, null, true, false);

    FunctionType functionTypeCopy = builder.copyFromOtherFunction(functionType).build();

    assertEquals("add", functionTypeCopy.getReferenceName());
    assertEquals(functionType.getSource(), functionTypeCopy.getSource());
    assertEquals(functionType.getParameterCount(), functionTypeCopy.getParameterCount());
    assertEquals(functionType.getParameterType(0), functionTypeCopy.getParameterType(0));
    assertEquals(functionType.getParameterType(1), functionTypeCopy.getParameterType(1));
    assertEquals(functionType.getReturnType(), functionTypeCopy.getReturnType());
    assertEquals(functionType.getTypeOfThis(), functionTypeCopy.getTypeOfThis());
    assertEquals(functionType.getTemplateTypeName(), functionTypeCopy.getTemplateTypeName());
    assertEquals(functionType.isConstructor(), functionTypeCopy.isConstructor());
    assertEquals(functionType.isNativeObjectType(), functionTypeCopy.isNativeObjectType());
  }
}