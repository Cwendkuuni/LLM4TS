import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.withName("testFunction").build();
    Assert.assertEquals("testFunction", functionType.getReferenceName());
  }

  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node();
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.withSourceNode(sourceNode).build();
    Assert.assertSame(sourceNode, functionType.getSource());
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    Node parametersNode = paramBuilder.addParam("a").addParam("b").build();
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.withParams(paramBuilder).build();
    Assert.assertEquals(parametersNode, functionType.getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    Node parametersNode = new Node();
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.withParamsNode(parametersNode).build();
    Assert.assertSame(parametersNode, functionType.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = new JSType();
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.withReturnType(returnType).build();
    Assert.assertSame(returnType, functionType.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = new JSType();
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.withInferredReturnType(returnType).build();
    Assert.assertSame(returnType, functionType.getReturnType());
    Assert.assertTrue(functionType.hasInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType();
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.withTypeOfThis(typeOfThis).build();
    Assert.assertSame(typeOfThis, functionType.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    String templateName = "TemplateType";
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.withTemplateName(templateName).build();
    Assert.assertEquals(templateName, functionType.getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.forConstructor().build();
    Assert.assertTrue(functionType.isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.forNativeType().build();
    Assert.assertTrue(functionType.isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType(new JSTypeRegistry(), "otherFunction", new Node(), null, null, null, false, false);
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.copyFromOtherFunction(otherType).build();
    Assert.assertEquals(otherType, functionType);
  }

}