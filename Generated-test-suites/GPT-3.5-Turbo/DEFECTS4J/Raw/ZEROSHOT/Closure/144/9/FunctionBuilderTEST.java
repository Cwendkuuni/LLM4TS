import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class FunctionBuilderTest {

  private FunctionBuilder functionBuilder;

  @Before
  public void setUp() {
    JSTypeRegistry registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testWithName() {
    String name = "testName";
    FunctionBuilder result = functionBuilder.withName(name);
    assertNotNull(result);
    assertEquals(name, result.getName());
  }

  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node();
    FunctionBuilder result = functionBuilder.withSourceNode(sourceNode);
    assertNotNull(result);
    assertEquals(sourceNode, result.getSourceNode());
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder params = new FunctionParamBuilder();
    Node parametersNode = params.build();
    FunctionBuilder result = functionBuilder.withParams(params);
    assertNotNull(result);
    assertEquals(parametersNode, result.getParamsNode());
  }

  @Test
  public void testWithParamsNode() {
    Node parametersNode = new Node();
    FunctionBuilder result = functionBuilder.withParamsNode(parametersNode);
    assertNotNull(result);
    assertEquals(parametersNode, result.getParamsNode());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = new JSType();
    FunctionBuilder result = functionBuilder.withReturnType(returnType);
    assertNotNull(result);
    assertEquals(returnType, result.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = new JSType();
    FunctionBuilder result = functionBuilder.withInferredReturnType(returnType);
    assertNotNull(result);
    assertTrue(result.getInferredReturnType());
    assertEquals(returnType, result.getReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType();
    FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis);
    assertNotNull(result);
    assertEquals(typeOfThis, result.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    String templateName = "testTemplate";
    FunctionBuilder result = functionBuilder.withTemplateName(templateName);
    assertNotNull(result);
    assertEquals(templateName, result.getTemplateName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder result = functionBuilder.forConstructor();
    assertNotNull(result);
    assertTrue(result.isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder result = functionBuilder.forNativeType();
    assertNotNull(result);
    assertTrue(result.isNativeType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType();
    FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
    assertNotNull(result);
    assertEquals(otherType.getName(), result.getName());
    assertEquals(otherType.getSourceNode(), result.getSourceNode());
    assertEquals(otherType.getParamsNode(), result.getParamsNode());
    assertEquals(otherType.getReturnType(), result.getReturnType());
    assertEquals(otherType.getTypeOfThis(), result.getTypeOfThis());
    assertEquals(otherType.getTemplateName(), result.getTemplateName());
    assertEquals(otherType.isConstructor(), result.isConstructor());
    assertEquals(otherType.isNativeType(), result.isNativeType());
  }

  @Test
  public void testBuild() {
    FunctionType expectedResult = new FunctionType();
    FunctionType result = functionBuilder.build();
    assertNotNull(result);
    assertEquals(expectedResult, result);
  }
}