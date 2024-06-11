import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.jstype.FunctionBuilder;

public class FunctionBuilderTest {

  private FunctionBuilder functionBuilder;

  @Before
  public void setUp() {
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testWithName() {
    FunctionBuilder result = functionBuilder.withName("testName");
    assertEquals("testName", result.getName());
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
    String templateTypeName = "testTemplate";
    FunctionBuilder result = functionBuilder.withTemplateName(templateTypeName);
    assertEquals(templateTypeName, result.getTemplateTypeName());
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
    FunctionType otherType = new FunctionType();
    FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
    assertEquals(otherType.getName(), result.getName());
    assertEquals(otherType.getSourceNode(), result.getSourceNode());
    assertEquals(otherType.getParametersNode(), result.getParametersNode());
    assertEquals(otherType.getReturnType(), result.getReturnType());
    assertEquals(otherType.getTypeOfThis(), result.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), result.getTemplateTypeName());
    assertEquals(otherType.isConstructor(), result.isConstructor());
    assertEquals(otherType.isNativeObjectType(), result.isNativeType());
  }

  @Test
  public void testBuild() {
    FunctionType result = functionBuilder.build();
    assertNotNull(result);
    assertNotNull(result.getRegistry());
    assertNull(result.getName());
    assertNull(result.getSourceNode());
    assertNull(result.getParametersNode());
    assertNull(result.getReturnType());
    assertNull(result.getTypeOfThis());
    assertNull(result.getTemplateTypeName());
    assertFalse(result.isConstructor());
    assertFalse(result.isNativeObjectType());
  }
}