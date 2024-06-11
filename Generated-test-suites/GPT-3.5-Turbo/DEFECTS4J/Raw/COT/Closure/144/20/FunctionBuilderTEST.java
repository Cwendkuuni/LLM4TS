import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {
  
  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
  }
  
  @Test
  public void testWithName() {
    FunctionBuilder builder = functionBuilder.withName("testFunction");
    assertEquals("testFunction", builder.getName());
  }
  
  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node();
    FunctionBuilder builder = functionBuilder.withSourceNode(sourceNode);
    assertEquals(sourceNode, builder.getSourceNode());
  }
  
  @Test
  public void testWithParams() {
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    Node parametersNode = paramBuilder.build();
    FunctionBuilder builder = functionBuilder.withParams(paramBuilder);
    assertEquals(parametersNode, builder.getParametersNode());
  }
  
  @Test
  public void testWithParamsNode() {
    Node parametersNode = new Node();
    FunctionBuilder builder = functionBuilder.withParamsNode(parametersNode);
    assertEquals(parametersNode, builder.getParametersNode());
  }
  
  @Test
  public void testWithReturnType() {
    JSType returnType = new JSType();
    FunctionBuilder builder = functionBuilder.withReturnType(returnType);
    assertEquals(returnType, builder.getReturnType());
  }
  
  @Test
  public void testWithInferredReturnType() {
    JSType returnType = new JSType();
    FunctionBuilder builder = functionBuilder.withInferredReturnType(returnType);
    assertEquals(returnType, builder.getReturnType());
    assertTrue(builder.isInferredReturnType());
  }
  
  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType();
    FunctionBuilder builder = functionBuilder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, builder.getTypeOfThis());
  }
  
  @Test
  public void testWithTemplateName() {
    String templateTypeName = "Template";
    FunctionBuilder builder = functionBuilder.withTemplateName(templateTypeName);
    assertEquals(templateTypeName, builder.getTemplateTypeName());
  }
  
  @Test
  public void testForConstructor() {
    FunctionBuilder builder = functionBuilder.forConstructor();
    assertTrue(builder.isConstructor());
  }
  
  @Test
  public void testForNativeType() {
    FunctionBuilder builder = functionBuilder.forNativeType();
    assertTrue(builder.isNativeType());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType();
    otherType.setName("otherFunction");
    otherType.setSourceNode(new Node());
    otherType.setParametersNode(new Node());
    otherType.setReturnType(new JSType());
    otherType.setTypeOfThis(new ObjectType());
    otherType.setTemplateTypeName("Template");
    otherType.setConstructor(true);
    otherType.setNativeObjectType(true);
    
    FunctionBuilder builder = functionBuilder.copyFromOtherFunction(otherType);
    assertEquals("otherFunction", builder.getName());
    assertEquals(otherType.getSourceNode(), builder.getSourceNode());
    assertEquals(otherType.getParametersNode(), builder.getParametersNode());
    assertEquals(otherType.getReturnType(), builder.getReturnType());
    assertEquals(otherType.getTypeOfThis(), builder.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), builder.getTemplateTypeName());
    assertEquals(otherType.isConstructor(), builder.isConstructor());
    assertEquals(otherType.isNativeObjectType(), builder.isNativeType());
  }
  
  @Test
  public void testBuild() {
    FunctionType functionType = functionBuilder.build();
    
    assertNull(functionType.getName());
    assertNull(functionType.getSourceNode());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }
}