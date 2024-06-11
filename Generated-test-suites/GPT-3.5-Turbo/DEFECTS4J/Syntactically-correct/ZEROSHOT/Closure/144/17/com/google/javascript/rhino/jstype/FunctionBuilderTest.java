import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {

  private FunctionBuilder functionBuilder;

  @Before
  public void setUp() {
    functionBuilder = new FunctionBuilder(new JSTypeRegistry());
  }

  @After
  public void tearDown() {
    functionBuilder = null;
  }

  @Test
  public void testWithName() {
    String name = "testFunction";
    FunctionBuilder result = functionBuilder.withName(name);
    assertEquals(name, result.getName());
  }

  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node();
    FunctionBuilder result = functionBuilder.withSourceNode(sourceNode);
    assertEquals(sourceNode, result.getSourceNode());
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder params = new FunctionParamBuilder();
    Node parametersNode = params.build();
    FunctionBuilder result = functionBuilder.withParams(params);
    assertEquals(parametersNode, result.getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    Node parametersNode = new Node();
    FunctionBuilder result = functionBuilder.withParamsNode(parametersNode);
    assertEquals(parametersNode, result.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = new JSType();
    FunctionBuilder result = functionBuilder.withReturnType(returnType);
    assertEquals(returnType, result.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = new JSType();
    FunctionBuilder result = functionBuilder.withInferredReturnType(returnType);
    assertEquals(returnType, result.getReturnType());
    assertTrue(result.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType();
    FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, result.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    String templateTypeName = "testTemplateType";
    FunctionBuilder result = functionBuilder.withTemplateName(templateTypeName);
    assertEquals(templateTypeName, result.getTemplateName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder result = functionBuilder.forConstructor();
    assertTrue(result.isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder result = functionBuilder.forNativeType();
    assertTrue(result.isNativeType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType(new JSTypeRegistry());
    otherType.setName("otherFunction");
    otherType.setSourceNode(new Node());
    FunctionParamBuilder params = new FunctionParamBuilder();
    otherType.setParametersNode(params.build());
    JSType returnType = new JSType();
    otherType.setReturnType(returnType);
    ObjectType typeOfThis = new ObjectType();
    otherType.setTypeOfThis(typeOfThis);
    otherType.setTemplateName("otherTemplateType");
    otherType.setIsConstructor(true);
    FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
    assertEquals("otherFunction", result.getName());
    assertEquals(otherType.getSourceNode(), result.getSourceNode());
    assertEquals(params.build(), result.getParametersNode());
    assertEquals(returnType, result.getReturnType());
    assertEquals(typeOfThis, result.getTypeOfThis());
    assertEquals("otherTemplateType", result.getTemplateName());
    assertTrue(result.isConstructor());
  }

  @Test
  public void testBuild() {
    FunctionType expected = new FunctionType(new JSTypeRegistry());
    assertEquals(expected, functionBuilder.build());
  }
}