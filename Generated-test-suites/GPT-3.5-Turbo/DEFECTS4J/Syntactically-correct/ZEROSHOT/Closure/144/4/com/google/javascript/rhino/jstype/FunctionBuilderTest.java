import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {
  
  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(null);
    String name = "testFunction";
    builder.withName(name);
    assertEquals(name, builder.getName());
  }
  
  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(null);
    Node sourceNode = new Node();
    builder.withSourceNode(sourceNode);
    assertEquals(sourceNode, builder.getSourceNode());
  }

  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionParamBuilder params = new FunctionParamBuilder(null);
    builder.withParams(params);
    assertEquals(params.build(), builder.getParametersNode());
  }
  
  @Test
  public void testWithParamsNode() {
    FunctionBuilder builder = new FunctionBuilder(null);
    Node parametersNode = new Node();
    builder.withParamsNode(parametersNode);
    assertEquals(parametersNode, builder.getParametersNode());
  }
  
  @Test
  public void testWithReturnType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    JSType returnType = new JSType();
    builder.withReturnType(returnType);
    assertEquals(returnType, builder.getReturnType());
  }
  
  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    JSType returnType = new JSType();
    builder.withInferredReturnType(returnType);
    assertTrue(builder.isInferredReturnType());
    assertEquals(returnType, builder.getReturnType());
  }
  
  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder builder = new FunctionBuilder(null);
    ObjectType typeOfThis = new ObjectType();
    builder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, builder.getTypeOfThis());
  }
  
  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = new FunctionBuilder(null);
    String templateTypeName = "Template";
    builder.withTemplateName(templateTypeName);
    assertEquals(templateTypeName, builder.getTemplateName());
  }
  
  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder(null);
    builder.forConstructor();
    assertTrue(builder.isConstructor());
  }
  
  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    builder.forNativeType();
    assertTrue(builder.isNativeType());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionType otherType = new FunctionType(null, "otherFunction",
        new Node(), new ArrowType(null, new Node(), new JSType(), false),
        new ObjectType(), "Template", true, false);
    builder.copyFromOtherFunction(otherType);
    assertEquals(otherType.getName(), builder.getName());
    assertEquals(otherType.getSource(), builder.getSourceNode());
    assertEquals(otherType.getParametersNode(), builder.getParametersNode());
    assertEquals(otherType.getReturnType(), builder.getReturnType());
    assertEquals(otherType.getTypeOfThis(), builder.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), builder.getTemplateName());
    assertTrue(builder.isConstructor());
    assertFalse(builder.isNativeType());
  }
  
  @Test
  public void testBuild() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionType functionType = builder.build();
    assertNotNull(functionType);
  }
  
}