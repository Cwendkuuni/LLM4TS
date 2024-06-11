import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
  }

  @After
  public void tearDown() {
    functionBuilder = null;
    registry = null;
  }

  @Test
  public void testFunctionBuilderWithNameAndReturnType() {
    functionBuilder.withName("add")
        .withReturnType(registry.getNumberType());

    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals("add", functionType.getReferenceName());
    assertEquals(registry.getNumberType(), functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithNameAndParamsAndReturnType() {
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withName("x").withType(registry.getNumberType());
    paramBuilder.withName("y").withType(registry.getNumberType());

    functionBuilder.withName("add")
        .withParams(paramBuilder.build())
        .withReturnType(registry.getNumberType());

    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals("add", functionType.getReferenceName());
    assertEquals(2, functionType.getNumberOfParameters());
    assertEquals(registry.getNumberType(), functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithNameAndParamsNodeAndReturnType() {
    Node parametersNode = new Node_CallExpression();
    parametersNode.setArguments(new Node[] { new Node_PropertyAccessExpression(),
        new Node_PropertyAccessExpression(), new Node_NumberLiteral(2) });

    functionBuilder.withName("add")
        .withParamsNode(parametersNode)
        .withReturnType(registry.getNumberType());

    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals("add", functionType.getReferenceName());
    assertEquals(2, functionType.getNumberOfParameters());
    assertEquals(registry.getNumberType(), functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithNameAndParamsAndReturnTypeAndTypeOfThis() {
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withName("x").withType(registry.getNumberType());
    paramBuilder.withName("y").withType(registry.getNumberType());

    ObjectType objectType = new ObjectType(registry, "MyObject");

    functionBuilder.withName("add")
        .withParams(paramBuilder.build())
        .withReturnType(registry.getNumberType())
        .withTypeOfThis(objectType);

    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals("add", functionType.getReferenceName());
    assertEquals(2, functionType.getNumberOfParameters());
    assertEquals(registry.getNumberType(), functionType.getReturnType());
    assertEquals(objectType, functionType.getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    String sourceCode = "function add(x, y) { return x + y; }";
    Node sourceNode = new Node_Program();
    sourceNode.setBody(new Node_ExpressionStatement(new Node_FunctionDeclaration("add", sourceCode)));

    functionBuilder.withSourceNode(sourceNode)
        .withReturnType(registry.getNumberType());

    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals("add", functionType.getReferenceName());
    assertEquals(sourceCode, functionType.getSource().toSource());
    assertEquals(registry.getNumberType(), functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithTemplateTypeName() {
    functionBuilder.withName("add")
        .withTemplateName("MyTemplateType")
        .withReturnType(registry.getNumberType());

    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals("MyTemplateType<add>", functionType.getReferenceName());
    assertEquals(registry.getNumberType(), functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    functionBuilder.withName("add")
        .withParams(new FunctionParamBuilder().withName("x").withType(registry.getNumberType()).build())
        .withInferredReturnType(registry.getNumberType());

    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals("add", functionType.getReferenceName());
    assertEquals(registry.getNumberType(), functionType.getReturnType());
    assertEquals(true, functionType.isInferredReturnType());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    functionBuilder.withName("MyConstructor")
        .forConstructor();

    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals("MyConstructor", functionType.getReferenceName());
    assertTrue(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    functionBuilder.forNativeType();

    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertTrue(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    ObjectType objectType = new ObjectType(registry, "MyObject");
    FunctionType originalFunctionType = new FunctionType(registry, "add", null, null, objectType, null, true, false);

    FunctionType copiedFunctionType = functionBuilder.copyFromOtherFunction(originalFunctionType).build();
    assertNotNull(copiedFunctionType);
    assertEquals("add", copiedFunctionType.getReferenceName());
    assertEquals(originalFunctionType.getSource(), copiedFunctionType.getSource());
    assertEquals(originalFunctionType.getParametersNode(), copiedFunctionType.getParametersNode());
    assertEquals(originalFunctionType.getReturnType(), copiedFunctionType.getReturnType());
    assertEquals(originalFunctionType.getTypeOfThis(), copiedFunctionType.getTypeOfThis());
    assertEquals(originalFunctionType.getTemplateTypeName(), copiedFunctionType.getTemplateTypeName());
    assertEquals(originalFunctionType.isConstructor(), copiedFunctionType.isConstructor());
    assertEquals(originalFunctionType.isNativeObjectType(), copiedFunctionType.isNativeObjectType());
  }
}