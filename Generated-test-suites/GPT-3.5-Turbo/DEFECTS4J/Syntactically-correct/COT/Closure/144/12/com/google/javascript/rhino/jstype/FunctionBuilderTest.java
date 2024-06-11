import static org.junit.Assert.*;

import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionType functionType = builder.withName("test").build();
    
    assertEquals("test", functionType.getReferenceName());
  }
  
  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node();
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionType functionType = builder.withSourceNode(sourceNode).build();
    
    assertEquals(sourceNode, functionType.getSource());
  }
  
  @Test
  public void testWithParams() {
    Node parametersNode = new Node();
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionType functionType = builder.withParamsNode(parametersNode).build();
    
    assertEquals(parametersNode, functionType.getParametersNode());
  }
  
  @Test
  public void testWithReturnType() {
    JSType returnType = new JSType();
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionType functionType = builder.withReturnType(returnType).build();
    
    assertEquals(returnType, functionType.getReturnType());
  }
  
  @Test
  public void testWithInferredReturnType() {
    JSType returnType = new JSType();
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionType functionType = builder.withInferredReturnType(returnType).build();
    
    assertEquals(returnType, functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
  }
  
  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType();
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionType functionType = builder.withTypeOfThis(typeOfThis).build();
    
    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }
  
  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionType functionType = builder.withTemplateName("template").build();
    
    assertEquals("template", functionType.getTemplateTypeName());
  }
  
  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionType functionType = builder.forConstructor().build();
    
    assertTrue(functionType.isConstructor());
  }
  
  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionType functionType = builder.forNativeType().build();
    
    assertTrue(functionType.isNativeObjectType());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType();
    FunctionType functionType = new FunctionType();
    
    FunctionBuilder builder = new FunctionBuilder(null);
    builder.copyFromOtherFunction(otherType);
    builder.build();
    
    assertEquals(otherType.getReferenceName(), functionType.getReferenceName());
    assertEquals(otherType.getSource(), functionType.getSource());
    assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
    assertEquals(otherType.getReturnType(), functionType.getReturnType());
    assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
    assertEquals(otherType.isConstructor(), functionType.isConstructor());
    assertEquals(otherType.isNativeObjectType(), functionType.isNativeObjectType());
  }
}