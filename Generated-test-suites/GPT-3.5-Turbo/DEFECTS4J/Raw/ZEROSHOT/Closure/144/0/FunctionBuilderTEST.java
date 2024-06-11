import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class FunctionBuilderTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testWithName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionBuilder result = functionBuilder.withName("testName");
    assertEquals("testName", result.getName());
  }

  @Test
  public void testWithSourceNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    Node sourceNode = new Node();
    FunctionBuilder result = functionBuilder.withSourceNode(sourceNode);
    assertEquals(sourceNode, result.getSourceNode());
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder params = new FunctionParamBuilder().withParam("param1").withParam("param2");
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionBuilder result = functionBuilder.withParams(params);
    assertEquals(params.build(), result.getParameters());
  }

  @Test
  public void testWithParamsNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    Node parametersNode = new Node();
    FunctionBuilder result = functionBuilder.withParamsNode(parametersNode);
    assertEquals(parametersNode, result.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    JSType returnType = new JSType();
    FunctionBuilder result = functionBuilder.withReturnType(returnType);
    assertEquals(returnType, result.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    JSType returnType = new JSType();
    FunctionBuilder result = functionBuilder.withInferredReturnType(returnType);
    assertEquals(returnType, result.getReturnType());
    assertTrue(result.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    ObjectType typeOfThis = new ObjectType();
    FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, result.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionBuilder result = functionBuilder.withTemplateName("templateName");
    assertEquals("templateName", result.getTemplateName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionBuilder result = functionBuilder.forConstructor();
    assertTrue(result.isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionBuilder result = functionBuilder.forNativeType();
    assertTrue(result.isNativeType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType(registry, "otherName", new Node(), new ArrowType(registry, new Node(), new JSType(), false), new ObjectType(), "otherTemplateName", true, true);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
    assertEquals("otherName", result.getName());
    assertEquals(otherType.getSourceNode(), result.getSourceNode());
    assertEquals(otherType.getParametersNode(), result.getParametersNode());
    assertEquals(otherType.getReturnType(), result.getReturnType());
    assertEquals(otherType.getTypeOfThis(), result.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), result.getTemplateName());
    assertTrue(result.isConstructor());
    assertTrue(result.isNativeType());
  }

  @Test
  public void testBuild() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionType result = functionBuilder.build();
    // Assert properties of result type
    assertEquals(functionBuilder.getName(), result.getName());
    assertEquals(functionBuilder.getSourceNode(), result.getSource());
    assertEquals(functionBuilder.getParametersNode(), result.getParametersNode());
    assertEquals(functionBuilder.getReturnType(), result.getReturnType());
    assertEquals(functionBuilder.getTypeOfThis(), result.getTypeOfThis());
    assertEquals(functionBuilder.getTemplateName(), result.getTemplateTypeName());
    assertEquals(functionBuilder.isConstructor(), result.isConstructor());
    assertEquals(functionBuilder.isNativeType(), result.isNativeObjectType());
  }

}