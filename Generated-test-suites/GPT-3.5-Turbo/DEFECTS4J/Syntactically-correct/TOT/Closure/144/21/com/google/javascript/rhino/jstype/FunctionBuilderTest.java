import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionBuilderTest {
  
  private FunctionBuilder functionBuilder;
  private JSTypeRegistry registry;
  
  @Before
  public void setup() {
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
  }
  
  @Test
  public void testWithName() {
    String expectedName = "myFunction";
    
    FunctionBuilder result = functionBuilder.withName(expectedName);
    
    assertNotNull(result);
    assertEquals(expectedName, result.build().getName());
  }
  
  @Test
  public void testWithName_NullName() {
    FunctionBuilder result = functionBuilder.withName(null);
    
    assertNotNull(result);
    assertNull(result.build().getName());
  }
  
  @Test
  public void testWithSourceNode() {
    Node expectedNode = new Node();
    
    FunctionBuilder result = functionBuilder.withSourceNode(expectedNode);
    
    assertNotNull(result);
    assertEquals(expectedNode, result.build().getSourceNode());
  }
  
  @Test
  public void testWithSourceNode_NullNode() {
    FunctionBuilder result = functionBuilder.withSourceNode(null);
    
    assertNotNull(result);
    assertNull(result.build().getSourceNode());
  }
  
  @Test
  public void testWithParams() {
    FunctionParamBuilder params = new FunctionParamBuilder();
    
    FunctionBuilder result = functionBuilder.withParams(params);
    
    assertNotNull(result);
    assertEquals(params.build(), result.build().getParametersNode());
    
  }
  
  @Test
  public void testWithParamsNode() {
    Node expectedNode = new Node();
    
    FunctionBuilder result = functionBuilder.withParamsNode(expectedNode);
    
    assertNotNull(result);
    assertEquals(expectedNode, result.build().getParametersNode());
  }
  
  @Test
  public void testWithReturnType() {
    JSType expectedReturnType = new JSType();
    
    FunctionBuilder result = functionBuilder.withReturnType(expectedReturnType);
    
    assertNotNull(result);
    assertEquals(expectedReturnType, result.build().getReturnType());
  }
  
  @Test
  public void testWithInferredReturnType() {
    JSType expectedReturnType = new JSType();
    
    FunctionBuilder result = functionBuilder.withInferredReturnType(expectedReturnType);
    
    assertNotNull(result);
    assertEquals(expectedReturnType, result.build().getReturnType());
    assertTrue(result.build().isInferredReturnType());
  }
  
  @Test
  public void testWithTypeOfThis() {
    ObjectType expectedType = new ObjectType();
    
    FunctionBuilder result = functionBuilder.withTypeOfThis(expectedType);
    
    assertNotNull(result);
    assertEquals(expectedType, result.build().getTypeOfThis());
  }
  
  @Test
  public void testWithTemplateName() {
    String expectedTemplateName = "MyTemplate";
    
    FunctionBuilder result = functionBuilder.withTemplateName(expectedTemplateName);
    
    assertNotNull(result);
    assertEquals(expectedTemplateName, result.build().getTemplateTypeName());
  }
  
  @Test
  public void testForConstructor() {
    FunctionBuilder result = functionBuilder.forConstructor();
    
    assertNotNull(result);
    assertTrue(result.build().isConstructor());
  }
  
  @Test
  public void testForNativeType() {
    FunctionBuilder result = functionBuilder.forNativeType();
    
    assertNotNull(result);
    assertTrue(result.build().isNativeType());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType();
    otherType.setName("myFunction");
    otherType.setSourceNode(new Node());
    otherType.setParametersNode(new Node());
    otherType.setReturnType(new JSType());
    otherType.setTypeOfThis(new ObjectType());
    otherType.setTemplateTypeName("MyTemplate");
    otherType.setIsConstructor(true);
    otherType.setIsNativeObjectType(true);
    
    FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
    
    assertNotNull(result);
    assertEquals(otherType.getName(), result.build().getName());
    assertEquals(otherType.getSourceNode(), result.build().getSourceNode());
    assertEquals(otherType.getParametersNode(), result.build().getParametersNode());
    assertEquals(otherType.getReturnType(), result.build().getReturnType());
    assertEquals(otherType.getTypeOfThis(), result.build().getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), result.build().getTemplateTypeName());
    assertEquals(otherType.isConstructor(), result.build().isConstructor());
    assertEquals(otherType.isNativeObjectType(), result.build().isNativeType());
  }
  
  @Test
  public void testBuild() {
    String expectedName = "myFunction";
    Node expectedSourceNode = new Node();
    Node expectedParametersNode = new Node();
    JSType expectedReturnType = new JSType();
    ObjectType expectedTypeOfThis = new ObjectType();
    String expectedTemplateName = "MyTemplate";
    boolean expectedIsConstructor = true;
    boolean expectedIsNativeType = true;
    
    FunctionType result = functionBuilder
                          .withName(expectedName)
                          .withSourceNode(expectedSourceNode)
                          .withParamsNode(expectedParametersNode)
                          .withReturnType(expectedReturnType)
                          .withTypeOfThis(expectedTypeOfThis)
                          .withTemplateName(expectedTemplateName)
                          .forConstructor()
                          .forNativeType()
                          .build();
    
    assertEquals(expectedName, result.getName());
    assertEquals(expectedSourceNode, result.getSourceNode());
    assertEquals(expectedParametersNode, result.getParametersNode());
    assertEquals(expectedReturnType, result.getReturnType());
    assertEquals(expectedTypeOfThis, result.getTypeOfThis());
    assertEquals(expectedTemplateName, result.getTemplateTypeName());
    assertEquals(expectedIsConstructor, result.isConstructor());
    assertEquals(expectedIsNativeType, result.isNativeType());
  }
}