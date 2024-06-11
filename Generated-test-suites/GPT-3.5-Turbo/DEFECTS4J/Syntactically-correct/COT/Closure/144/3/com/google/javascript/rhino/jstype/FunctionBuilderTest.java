import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class FunctionBuilderTest {
  private FunctionBuilder functionBuilder;

  @Before
  public void setUp() {
    functionBuilder = new FunctionBuilder(new JSTypeRegistry());
  }

  @Test
  public void testWithName() {
    String name = "myFunction";
    FunctionBuilder builder = functionBuilder.withName(name);
    assertEquals(name, builder.getName());
  }

  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node();
    FunctionBuilder builder = functionBuilder.withSourceNode(sourceNode);
    assertEquals(sourceNode, builder.getSourceNode());
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder paramsBuilder = new FunctionParamBuilder();
    Node parametersNode = paramsBuilder.build();
    FunctionBuilder builder = functionBuilder.withParams(paramsBuilder);
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
    JSType returnType = new MyReturnType();
    FunctionBuilder builder = functionBuilder.withReturnType(returnType);
    assertEquals(returnType, builder.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = new MyReturnType();
    FunctionBuilder builder = functionBuilder.withInferredReturnType(returnType);
    assertEquals(returnType, builder.getReturnType());
    assertTrue(builder.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = new MyObjectType();
    FunctionBuilder builder = functionBuilder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, builder.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    String templateTypeName = "MyTemplate";
    FunctionBuilder builder = functionBuilder.withTemplateName(templateTypeName);
    assertEquals(templateTypeName, builder.getTemplateName());
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
    FunctionType otherType = new FunctionType(new JSTypeRegistry());
    otherType.setName("otherFunction");
    otherType.setSourceNode(new Node());
    otherType.setParams(new FunctionParamBuilder().build());
    otherType.setReturnType(new MyReturnType());
    otherType.setTypeOfThis(new MyObjectType());
    otherType.setTemplateName("OtherTemplate");
    otherType.setConstructor(true);
    otherType.setNativeType(true);

    FunctionBuilder builder = functionBuilder.copyFromOtherFunction(otherType);

    assertEquals("otherFunction", builder.getName());
    assertEquals(otherType.getSourceNode(), builder.getSourceNode());
    assertEquals(otherType.getParametersNode(), builder.getParametersNode());
    assertEquals(otherType.getReturnType(), builder.getReturnType());
    assertEquals(otherType.getTypeOfThis(), builder.getTypeOfThis());
    assertEquals("OtherTemplate", builder.getTemplateName());
    assertTrue(builder.isConstructor());
    assertTrue(builder.isNativeType());
  }

  @Test
  public void testBuild() {
    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertEquals(new JSTypeRegistry(), functionType.getRegistry());
    assertNull(functionType.getName());
    assertNull(functionType.getSourceNode());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }
}