import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.withName("myFunction");
    FunctionType function = builder.build();
    assertEquals("myFunction", function.getName());
  }

  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    Node sourceNode = createSourceNode();
    builder.withSourceNode(sourceNode);
    FunctionType function = builder.build();
    assertEquals(sourceNode, function.getSource());
  }

  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.addParam("param1");
    paramBuilder.addParam("param2");
    paramBuilder.addParam("param3");
    builder.withParams(paramBuilder);
    FunctionType function = builder.build();
    assertEquals(paramBuilder.build(), function.getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    Node parametersNode = createParametersNode();
    builder.withParamsNode(parametersNode);
    FunctionType function = builder.build();
    assertEquals(parametersNode, function.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    JSType returnType = createReturnType();
    builder.withReturnType(returnType);
    FunctionType function = builder.build();
    assertEquals(returnType, function.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    JSType returnType = createReturnType();
    builder.withInferredReturnType(returnType);
    FunctionType function = builder.build();
    assertEquals(returnType, function.getReturnType());
    assertTrue(function.hasInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    ObjectType typeOfThis = createTypeOfThis();
    builder.withTypeOfThis(typeOfThis);
    FunctionType function = builder.build();
    assertEquals(typeOfThis, function.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    String templateTypeName = "MyTemplateType";
    builder.withTemplateName(templateTypeName);
    FunctionType function = builder.build();
    assertEquals(templateTypeName, function.getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.forConstructor();
    FunctionType function = builder.build();
    assertTrue(function.isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.forNativeType();
    FunctionType function = builder.build();
    assertTrue(function.isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionType otherType = createOtherFunctionType();
    builder.copyFromOtherFunction(otherType);
    FunctionType function = builder.build();
    assertEquals(otherType.getName(), function.getName());
    assertEquals(otherType.getSource(), function.getSource());
    assertEquals(otherType.getParametersNode(), function.getParametersNode());
    assertEquals(otherType.getReturnType(), function.getReturnType());
    assertEquals(otherType.getTypeOfThis(), function.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), function.getTemplateTypeName());
    assertEquals(otherType.isConstructor(), function.isConstructor());
    assertEquals(otherType.isNativeObjectType(), function.isNativeObjectType());
  }

  @Test
  public void testBuild() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionType function = builder.build();
    assertNotNull(function);
  }

  // Additional test cases for edge cases and exceptions

  // Test for a function with no name

  @Test(expected = IllegalArgumentException.class)
  public void testBuildWithoutName() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.build();
  }

  // Test for a function with no source node

  @Test(expected = IllegalArgumentException.class)
  public void testBuildWithoutSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.withName("myFunction");
    builder.build();
  }

  // Test for a function with no parameters

  @Test(expected = IllegalArgumentException.class)
  public void testBuildWithoutParams() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.withName("myFunction");
    builder.withSourceNode(createSourceNode());
    builder.build();
  }

  // Test for a function with no return type

  @Test(expected = IllegalArgumentException.class)
  public void testBuildWithoutReturnType() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.withName("myFunction");
    builder.withSourceNode(createSourceNode());
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.addParam("param1");
    builder.withParams(paramBuilder);
    builder.build();
  }

  // Test for a function with no "this" type

  @Test(expected = IllegalArgumentException.class)
  public void testBuildWithoutTypeOfThis() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.withName("myFunction");
    builder.withSourceNode(createSourceNode());
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.addParam("param1");
    builder.withParams(paramBuilder);
    builder.withReturnType(createReturnType());
    builder.build();
  }

  public Node createSourceNode() {
    return new Node();
  }

  public Node createParametersNode() {
    return new Node();
  }

  public JSType createReturnType() {
    return new JSType();
  }

  public ObjectType createTypeOfThis() {
    return new ObjectType();
  }

  public FunctionType createOtherFunctionType() {
    return new FunctionType(registry, "otherFunction", new Node(),
        new ArrowType(registry, new Node(), new JSType(), false),
        new ObjectType(), "OtherTemplateType", true, false);
  }

}