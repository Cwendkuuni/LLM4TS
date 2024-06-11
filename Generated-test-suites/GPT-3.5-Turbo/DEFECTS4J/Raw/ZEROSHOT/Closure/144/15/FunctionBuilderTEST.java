import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

  private FunctionBuilder functionBuilder;

  @Before
  public void setUp() {
    functionBuilder = new FunctionBuilder(new JSTypeRegistry());
  }

  @After
  public void tearDown() {
    functionBuilder = null;
  }

  @Test
  public void testWithName() {
    String name = "testName";
    FunctionBuilder builderWithName = functionBuilder.withName(name);
    Assert.assertEquals(name, builderWithName.getName());
  }

  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node("testSourceNode");
    FunctionBuilder builderWithSourceNode = functionBuilder.withSourceNode(sourceNode);
    Assert.assertEquals(sourceNode, builderWithSourceNode.getSourceNode());
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder params = new FunctionParamBuilder();
    Node parametersNode = params.build();
    FunctionBuilder builderWithParams = functionBuilder.withParams(params);
    Assert.assertEquals(parametersNode, builderWithParams.getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    Node parametersNode = new Node("testParamsNode");
    FunctionBuilder builderWithParamsNode = functionBuilder.withParamsNode(parametersNode);
    Assert.assertEquals(parametersNode, builderWithParamsNode.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = new JSType();
    FunctionBuilder builderWithReturnType = functionBuilder.withReturnType(returnType);
    Assert.assertEquals(returnType, builderWithReturnType.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = new JSType();
    FunctionBuilder builderWithInferredReturnType = functionBuilder.withInferredReturnType(returnType);
    Assert.assertEquals(returnType, builderWithInferredReturnType.getReturnType());
    Assert.assertTrue(builderWithInferredReturnType.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType();
    FunctionBuilder builderWithTypeOfThis = functionBuilder.withTypeOfThis(typeOfThis);
    Assert.assertEquals(typeOfThis, builderWithTypeOfThis.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    String templateTypeName = "testTemplateTypeName";
    FunctionBuilder builderWithTemplateName = functionBuilder.withTemplateName(templateTypeName);
    Assert.assertEquals(templateTypeName, builderWithTemplateName.getTemplateName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder builderForConstructor = functionBuilder.forConstructor();
    Assert.assertTrue(builderForConstructor.isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder builderForNativeType = functionBuilder.forNativeType();
    Assert.assertTrue(builderForNativeType.isNativeType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType(new JSTypeRegistry());
    FunctionBuilder builderCopyFromOtherFunction = functionBuilder.copyFromOtherFunction(otherType);
    Assert.assertEquals(otherType.getReferenceName(), builderCopyFromOtherFunction.getName());
    Assert.assertEquals(otherType.getSource(), builderCopyFromOtherFunction.getSourceNode());
    Assert.assertEquals(otherType.getParametersNode(), builderCopyFromOtherFunction.getParametersNode());
    Assert.assertEquals(otherType.getReturnType(), builderCopyFromOtherFunction.getReturnType());
    Assert.assertEquals(otherType.getTypeOfThis(), builderCopyFromOtherFunction.getTypeOfThis());
    Assert.assertEquals(otherType.getTemplateTypeName(), builderCopyFromOtherFunction.getTemplateName());
    Assert.assertEquals(otherType.isConstructor(), builderCopyFromOtherFunction.isConstructor());
    Assert.assertEquals(otherType.isNativeObjectType(), builderCopyFromOtherFunction.isNativeType());
  }
}