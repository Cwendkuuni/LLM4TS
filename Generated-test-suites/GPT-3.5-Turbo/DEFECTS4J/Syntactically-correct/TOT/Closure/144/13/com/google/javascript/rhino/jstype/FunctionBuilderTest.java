import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Assert;
import org.junit.Test;

public class FunctionBuilderTest {
  
  /**
   * Test case for FunctionBuilder.withName().
   * It should set the name of the function type.
   */
  @Test
  public void testWithName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    functionBuilder.withName("testName");
    FunctionType functionType = functionBuilder.build();
    
    Assert.assertEquals("testName", functionType.getName());
  }

  /**
   * Test case for FunctionBuilder.withSourceNode().
   * It should set the source node of the function type.
   */
  @Test
  public void testWithSourceNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    Node sourceNode = new Node("SOURCE");
    functionBuilder.withSourceNode(sourceNode);
    FunctionType functionType = functionBuilder.build();
    
    Assert.assertEquals(sourceNode, functionType.getSourceNode());
  }

  /**
   * Test case for FunctionBuilder.withParams().
   * It should set the parameters of the function type from a FunctionParamBuilder.
   */
  @Test
  public void testWithParams() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.addParam("param1", JSTypes.NUMBER);
    paramBuilder.addParam("param2", JSTypes.STRING);
    functionBuilder.withParams(paramBuilder);
    FunctionType functionType = functionBuilder.build();
    
    Assert.assertEquals(paramBuilder.build(), functionType.getParametersNode());
  }

  /**
   * Test case for FunctionBuilder.withParamsNode().
   * It should set the parameters of the function type with a specially-formatted node.
   */
  @Test
  public void testWithParamsNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    Node parametersNode = new Node("PARAMETERS");
    functionBuilder.withParamsNode(parametersNode);
    FunctionType functionType = functionBuilder.build();
    
    Assert.assertEquals(parametersNode, functionType.getParametersNode());
  }

  /**
   * Test case for FunctionBuilder.withReturnType().
   * It should set the return type.
   */
  @Test
  public void testWithReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    JSType returnType = JSTypes.NUMBER;
    functionBuilder.withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    
    Assert.assertEquals(returnType, functionType.getReturnType());
  }

  /**
   * Test case for FunctionBuilder.withInferredReturnType().
   * It should set an inferred return type.
   */
  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    JSType returnType = JSTypes.UNDEFINED;
    functionBuilder.withInferredReturnType(returnType);
    FunctionType functionType = functionBuilder.build();

    Assert.assertEquals(returnType, functionType.getReturnType());
    Assert.assertTrue(functionType.isInferredReturnType());
  }

  /**
   * Test case for FunctionBuilder.withTypeOfThis().
   * It should set the "this" type.
   */
  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    ObjectType typeOfThis = new ObjectType();
    functionBuilder.withTypeOfThis(typeOfThis);
    FunctionType functionType = functionBuilder.build();
    
    Assert.assertEquals(typeOfThis, functionType.getTypeOfThis());
  }

  /**
   * Test case for FunctionBuilder.withTemplateName().
   * It should set the template name.
   */
  @Test
  public void testWithTemplateName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    String templateName = "template";
    functionBuilder.withTemplateName(templateName);
    FunctionType functionType = functionBuilder.build();
    
    Assert.assertEquals(templateName, functionType.getTemplateTypeName());
  }

  /**
   * Test case for FunctionBuilder.forConstructor().
   * It should make this a constructor.
   */
  @Test
  public void testForConstructor() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    functionBuilder.forConstructor();
    FunctionType functionType = functionBuilder.build();
    
    Assert.assertTrue(functionType.isConstructor());
  }

  /**
   * Test case for FunctionBuilder.forNativeType().
   * It should make this a native type.
   */
  @Test
  public void testForNativeType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    functionBuilder.forNativeType();
    FunctionType functionType = functionBuilder.build();
    
    Assert.assertTrue(functionType.isNativeObjectType());
  }
  
  /**
   * Test case for FunctionBuilder.copyFromOtherFunction().
   * It should copy all the information from another function type.
   */
  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType otherType = new FunctionType();
    otherType.setName("testName");
    otherType.setSourceNode(new Node("SOURCE"));
    otherType.setParametersNode(new Node("PARAMETERS"));
    otherType.setReturnType(JSTypes.STRING);
    otherType.setTypeOfThis(new ObjectType());
    otherType.setTemplateTypeName("template");
    otherType.setConstructor(true);
    otherType.setNativeObjectType(true);
    
    functionBuilder.copyFromOtherFunction(otherType);
    FunctionType functionType = functionBuilder.build();
    
    Assert.assertEquals(otherType.getName(), functionType.getName());
    Assert.assertEquals(otherType.getSourceNode(), functionType.getSourceNode());
    Assert.assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
    Assert.assertEquals(otherType.getReturnType(), functionType.getReturnType());
    Assert.assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
    Assert.assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
    Assert.assertEquals(otherType.isConstructor(), functionType.isConstructor());
    Assert.assertEquals(otherType.isNativeObjectType(), functionType.isNativeObjectType());
  }

  /**
   * Test case for FunctionBuilder.build().
   * It should construct a new function type.
   */
  @Test
  public void testBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionType functionType = functionBuilder.build();
    
    Assert.assertEquals("function", functionType.getReferenceName());
    Assert.assertNull(functionType.getName());
    Assert.assertNull(functionType.getSourceNode());
    Assert.assertNotNull(functionType.getParametersNode());
    Assert.assertNull(functionType.getReturnType());
    Assert.assertNull(functionType.getTypeOfThis());
    Assert.assertNull(functionType.getTemplateTypeName());
    Assert.assertFalse(functionType.isConstructor());
    Assert.assertFalse(functionType.isNativeObjectType());
  }
}