import org.junit.*;
import static org.junit.Assert.*;

public class FunctionBuilderTest {
  
  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.withName("test");
    FunctionType function = builder.build();
    assertEquals("test", function.getName());
  }
  
  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    Node sourceNode = new Node();
    builder.withSourceNode(sourceNode);
    FunctionType function = builder.build();
    assertEquals(sourceNode, function.getSourceNode());
  }
  
  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionParamBuilder params = new FunctionParamBuilder(registry);
    params.withParam("param1", "string");
    params.withParam("param2", "number");
    builder.withParams(params);
    FunctionType function = builder.build();
    assertEquals(params.build(), function.getParametersNode());
  }
  
  @Test
  public void testWithParamsNode() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    Node parametersNode = new Node();
    builder.withParamsNode(parametersNode);
    FunctionType function = builder.build();
    assertEquals(parametersNode, function.getParametersNode());
  }
  
  @Test
  public void testWithReturnType() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    JSType returnType = new JSType();
    builder.withReturnType(returnType);
    FunctionType function = builder.build();
    assertEquals(returnType, function.getReturnType());
  }
  
  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    JSType returnType = new JSType();
    builder.withInferredReturnType(returnType);
    FunctionType function = builder.build();
    assertEquals(returnType, function.getReturnType());
    assertTrue(function.isInferredReturnType());
  }
  
  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    ObjectType typeOfThis = new ObjectType();
    builder.withTypeOfThis(typeOfThis);
    FunctionType function = builder.build();
    assertEquals(typeOfThis, function.getTypeOfThis());
  }
  
  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.withTemplateName("template");
    FunctionType function = builder.build();
    assertEquals("template", function.getTemplateName());
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
    FunctionType otherType = new FunctionType();
    builder.copyFromOtherFunction(otherType);
    FunctionType function = builder.build();
    // Test that all properties are copied correctly
    assertEquals(otherType.getReferenceName(), function.getName());
    assertEquals(otherType.getSource(), function.getSourceNode());
    assertEquals(otherType.getParametersNode(), function.getParametersNode());
    assertEquals(otherType.getReturnType(), function.getReturnType());
    assertEquals(otherType.getTypeOfThis(), function.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), function.getTemplateName());
    assertEquals(otherType.isConstructor(), function.isConstructor());
    assertEquals(otherType.isNativeObjectType(), function.isNativeObjectType());
  }
  
}