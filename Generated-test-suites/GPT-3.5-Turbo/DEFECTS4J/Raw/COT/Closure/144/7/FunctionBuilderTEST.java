import static org.junit.Assert.*;
import org.junit.*;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder();
    builder.withName("test");
    assertEquals("test", builder.getName());
  }
  
  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder();
    Node sourceNode = new Node();
    builder.withSourceNode(sourceNode);
    assertEquals(sourceNode, builder.getSourceNode());
  }
  
  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder();
    FunctionParamBuilder params = new FunctionParamBuilder();
    builder.withParams(params);
    assertEquals(params.build(), builder.getParametersNode());
  }
  
  @Test
  public void testWithParamsNode() {
    FunctionBuilder builder = new FunctionBuilder();
    Node parametersNode = new Node();
    builder.withParamsNode(parametersNode);
    assertEquals(parametersNode, builder.getParametersNode());
  }
  
  @Test
  public void testWithReturnType() {
    FunctionBuilder builder = new FunctionBuilder();
    JSType returnType = new JSType();
    builder.withReturnType(returnType);
    assertEquals(returnType, builder.getReturnType());
  }
  
  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder builder = new FunctionBuilder();
    JSType returnType = new JSType();
    builder.withInferredReturnType(returnType);
    assertEquals(returnType, builder.getReturnType());
    assertTrue(builder.isInferredReturnType());
  }
  
  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder builder = new FunctionBuilder();
    ObjectType typeOfThis = new ObjectType();
    builder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, builder.getTypeOfThis());
  }
  
  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = new FunctionBuilder();
    builder.withTemplateName("template");
    assertEquals("template", builder.getTemplateName());
  }
  
  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder();
    builder.forConstructor();
    assertTrue(builder.isConstructor());
  }
  
  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder();
    builder.forNativeType();
    assertTrue(builder.isNativeType());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder builder = new FunctionBuilder();
    FunctionType otherType = new FunctionType();
    builder.copyFromOtherFunction(otherType);
    // Check if all properties are copied correctly
    assertEquals(otherType.getName(), builder.getName());
    assertEquals(otherType.getSourceNode(), builder.getSourceNode());
    assertEquals(otherType.getParametersNode(), builder.getParametersNode());
    assertEquals(otherType.getReturnType(), builder.getReturnType());
    assertEquals(otherType.getTypeOfThis(), builder.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), builder.getTemplateName());
    assertEquals(otherType.isConstructor(), builder.isConstructor());
    assertEquals(otherType.isNativeObjectType(), builder.isNativeType());
  }
  
  @Test
  public void testBuild() {
    FunctionBuilder builder = new FunctionBuilder();
    FunctionType functionType = builder.withName("test")
                                      .withSourceNode(new Node())
                                      .withParams(new FunctionParamBuilder())
                                      .withReturnType(new JSType())
                                      .withInferredReturnType(new JSType())
                                      .withTypeOfThis(new ObjectType())
                                      .withTemplateName("template")
                                      .forConstructor()
                                      .forNativeType()
                                      .build();
    assertNotNull(functionType);
    // Check if all properties are set correctly
    assertEquals("test", functionType.getName());
    assertNotNull(functionType.getSourceNode());
    assertNotNull(functionType.getParametersNode());
    assertNotNull(functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
    assertNotNull(functionType.getTypeOfThis());
    assertEquals("template", functionType.getTemplateName());
    assertTrue(functionType.isConstructor());
    assertTrue(functionType.isNativeObjectType());
  }
}