import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
    FunctionBuilder builder = functionBuilder.withName("functionName");
    assertEquals("functionName", builder.build().getReferenceName());
  }

  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node();
    FunctionBuilder builder = functionBuilder.withSourceNode(sourceNode);
    assertEquals(sourceNode, builder.build().getSource());
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry)
        .addParam("param1", registry.getNativeType(JSTypeNative.STRING_TYPE));
    FunctionBuilder builder = functionBuilder.withParams(paramBuilder);
    assertEquals(paramBuilder.build(), builder.build().getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    Node parametersNode = new Node();
    FunctionBuilder builder = functionBuilder.withParamsNode(parametersNode);
    assertEquals(parametersNode, builder.build().getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    FunctionBuilder builder = functionBuilder.withReturnType(returnType);
    assertEquals(returnType, builder.build().getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    FunctionBuilder builder = functionBuilder.withInferredReturnType(returnType);
    assertTrue(builder.build().isInferredReturnType());
    assertEquals(returnType, builder.build().getReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType();
    FunctionBuilder builder = functionBuilder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, builder.build().getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    String templateTypeName = "templateType";
    FunctionBuilder builder = functionBuilder.withTemplateName(templateTypeName);
    assertEquals(templateTypeName, builder.build().getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder builder = functionBuilder.forConstructor();
    assertTrue(builder.build().isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder builder = functionBuilder.forNativeType();
    assertTrue(builder.build().isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType(registry);
    otherType.setReferenceName("otherFunction");
    otherType.setSource(new Node());
    otherType.setParametersNode(new Node());
    otherType.setReturnType(registry.getNativeType(JSTypeNative.ARRAY_TYPE));
    otherType.setTypeOfThis(new ObjectType());
    otherType.setTemplateTypeName("otherTemplateType");
    otherType.setConstructor(true);
    otherType.setNativeObjectType(true);

    FunctionBuilder builder = functionBuilder.copyFromOtherFunction(otherType);
    FunctionType functionType = builder.build();

    assertEquals(otherType.getReferenceName(), functionType.getReferenceName());
    assertEquals(otherType.getSource(), functionType.getSource());
    assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
    assertEquals(otherType.getReturnType(), functionType.getReturnType());
    assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertTrue(functionType.isNativeObjectType());
  }

  @Test
  public void testBuild() {
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);

    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }
}