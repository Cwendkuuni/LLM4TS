import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;

public class FunctionBuilderTest {
  
  private FunctionBuilder functionBuilder;
  
  @Before
  public void setUp() {
    JSTypeRegistry registry = new JSTypeRegistry();
    
    functionBuilder = new FunctionBuilder(registry);
  }
  
  @Test
  public void testName() {
    String expected = "testName";
    
    FunctionBuilder result = functionBuilder.withName(expected);
    
    Assert.assertEquals(expected, result.build().getName());
  }
  
  @Test
  public void testSourceNode() {
    Node expected = new Node();
    
    FunctionBuilder result = functionBuilder.withSourceNode(expected);
    
    Assert.assertEquals(expected, result.build().getSourceNode());
  }
  
  @Test
  public void testParams() {
    FunctionParamBuilder params = new FunctionParamBuilder();
    
    Node expected = params.build();
    
    FunctionBuilder result = functionBuilder.withParams(params);
    
    Assert.assertEquals(expected, result.build().getParametersNode());
  }
  
  @Test
  public void testParamsNode() {
    Node expected = new Node();
    
    FunctionBuilder result = functionBuilder.withParamsNode(expected);
    
    Assert.assertEquals(expected, result.build().getParametersNode());
  }
  
  @Test
  public void testReturnType() {
    JSType expected = new JSType();
    
    FunctionBuilder result = functionBuilder.withReturnType(expected);
    
    Assert.assertEquals(expected, result.build().getReturnType());
  }
  
  @Test
  public void testInferredReturnType() {
    JSType returnType = new JSType();
    FunctionBuilder result = functionBuilder.withInferredReturnType(returnType);
    
    Assert.assertTrue(result.build().isInferredReturnType());
    Assert.assertEquals(returnType, result.build().getReturnType());
  }
  
  @Test
  public void testTypeOfThis() {
    ObjectType expected = new ObjectType();
    
    FunctionBuilder result = functionBuilder.withTypeOfThis(expected);
    
    Assert.assertEquals(expected, result.build().getTypeOfThis());
  }
  
  @Test
  public void testTemplateName() {
    String expected = "testTemplate";
    
    FunctionBuilder result = functionBuilder.withTemplateName(expected);
    
    Assert.assertEquals(expected, result.build().getTemplateTypeName());
  }
  
  @Test
  public void testForConstructor() {
    FunctionBuilder result = functionBuilder.forConstructor();
    
    Assert.assertTrue(result.build().isConstructor());
  }
  
  @Test
  public void testForNativeType() {
    FunctionBuilder result = functionBuilder.forNativeType();
    
    Assert.assertTrue(result.build().isNativeObjectType());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType();
    
    FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
    
    Assert.assertEquals(otherType.getReferenceName(), result.build().getReferenceName());
    Assert.assertEquals(otherType.getSource(), result.build().getSource());
    Assert.assertEquals(otherType.getParametersNode(), result.build().getParametersNode());
    Assert.assertEquals(otherType.getReturnType(), result.build().getReturnType());
    Assert.assertEquals(otherType.getTypeOfThis(), result.build().getTypeOfThis());
    Assert.assertEquals(otherType.getTemplateTypeName(), result.build().getTemplateTypeName());
    Assert.assertEquals(otherType.isConstructor(), result.build().isConstructor());
    Assert.assertEquals(otherType.isNativeObjectType(), result.build().isNativeObjectType());
  }
  
}