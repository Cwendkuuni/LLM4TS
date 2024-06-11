import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {
  
  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    String name = "testFunction";
    FunctionBuilder result = builder.withName(name);
    assertEquals(name, result.build().getName());
  }
  
  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    Node sourceNode = new Node("source");
    FunctionBuilder result = builder.withSourceNode(sourceNode);
    assertEquals(sourceNode, result.build().getSourceNode());
  }
  
  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionParamBuilder params = new FunctionParamBuilder();
    Node parametersNode = params.build();
    FunctionBuilder result = builder.withParams(params);
    assertEquals(parametersNode, result.build().getParametersNode());
  }
  
  @Test
  public void testWithParamsNode() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    Node parametersNode = new Node("parameters");
    FunctionBuilder result = builder.withParamsNode(parametersNode);
    assertEquals(parametersNode, result.build().getParametersNode());
  }
  
  @Test
  public void testWithReturnType() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    JSType returnType = new JSType();
    FunctionBuilder result = builder.withReturnType(returnType);
    assertEquals(returnType, result.build().getReturnType());
  }
  
  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    JSType returnType = new JSType();
    FunctionBuilder result = builder.withInferredReturnType(returnType);
    assertTrue(result.build().isInferredReturnType());
  }
  
  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    ObjectType typeOfThis = new ObjectType();
    FunctionBuilder result = builder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, result.build().getTypeOfThis());
  }
  
  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    String templateName = "template";
    FunctionBuilder result = builder.withTemplateName(templateName);
    assertEquals(templateName, result.build().getTemplateTypeName());
  }
  
  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionBuilder result = builder.forConstructor();
    assertTrue(result.build().isConstructor());
  }
    
  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionBuilder result = builder.forNativeType();
    assertTrue(result.build().isNativeObjectType());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType otherType = new FunctionType(new JSTypeRegistry(), "", new Node("source"), new ArrowType(new JSTypeRegistry(), new Node("parameters"), new JSType(), false), new ObjectType(), "template", false, false);
    FunctionBuilder result = builder.copyFromOtherFunction(otherType);
    assertEquals(otherType.getName(), result.build().getName());
    assertEquals(otherType.getSourceNode(), result.build().getSourceNode());
    assertEquals(otherType.getParametersNode(), result.build().getParametersNode());
    assertEquals(otherType.getReturnType(), result.build().getReturnType());
    assertEquals(otherType.getTypeOfThis(), result.build().getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), result.build().getTemplateTypeName());
    assertEquals(otherType.isConstructor(), result.build().isConstructor());
    assertEquals(otherType.isNativeObjectType(), result.build().isNativeObjectType());
  }
}