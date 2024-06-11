import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.Type;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

  private FunctionBuilder functionBuilder;
  private JSTypeRegistry registry;
  private FunctionParamBuilder functionParamBuilder;
  private ObjectType objectType;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    functionBuilder = new FunctionBuilder(registry);
    functionParamBuilder = mock(FunctionParamBuilder.class);
    objectType = mock(ObjectType.class);
  }

  @Test
  public void testFunctionBuilderWithName() {
    FunctionType expectedFunctionType = new FunctionType(registry, "functionName", null, null, null, null, false, false);
    FunctionType actualFunctionType = functionBuilder.withName("functionName").build();

    assertFunctionsEqual(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    Node sourceNode = mock(Node.class);
    FunctionType expectedFunctionType = new FunctionType(registry, null, sourceNode, null, null, null, false, false);
    FunctionType actualFunctionType = functionBuilder.withSourceNode(sourceNode).build();

    assertFunctionsEqual(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testFunctionBuilderWithParams() {
    Node parametersNode = mock(Node.class);
    FunctionType expectedFunctionType = new FunctionType(registry, null, null, new ArrowType(registry, parametersNode, null, false), null, null, false, false);
    when(functionParamBuilder.build()).thenReturn(parametersNode);

    FunctionType actualFunctionType = functionBuilder.withParams(functionParamBuilder).build();

    assertFunctionsEqual(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testFunctionBuilderWithParamsNode() {
    Node parametersNode = mock(Node.class);
    FunctionType expectedFunctionType = new FunctionType(registry, null, null, new ArrowType(registry, parametersNode, null, false), null, null, false, false);

    FunctionType actualFunctionType = functionBuilder.withParamsNode(parametersNode).build();

    assertFunctionsEqual(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testFunctionBuilderWithReturnType() {
    JSType returnType = mock(Type.class);
    FunctionType expectedFunctionType = new FunctionType(registry, null, null, null, null, null, false, false);
    expectedFunctionType.setReturnType(returnType);

    FunctionType actualFunctionType = functionBuilder.withReturnType(returnType).build();

    assertFunctionsEqual(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSType returnType = mock(Type.class);
    FunctionType expectedFunctionType = new FunctionType(registry, null, null, null, null, null, false, false);
    expectedFunctionType.setReturnType(returnType);
    expectedFunctionType.setInferredReturnType(true);

    FunctionType actualFunctionType = functionBuilder.withReturnType(returnType).withInferredReturnType(returnType).build();

    assertFunctionsEqual(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    when(registry.createObjectType("Object")).thenReturn(objectType);
    FunctionType expectedFunctionType = new FunctionType(registry, null, null, null, objectType, null, false, false);

    FunctionType actualFunctionType = functionBuilder.withTypeOfThis(objectType.getTypeName()).build();

    assertFunctionsEqual(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testFunctionBuilderWithTemplateName() {
    FunctionType expectedFunctionType = new FunctionType(registry, null, null, null, null, "templateName", false, false);

    FunctionType actualFunctionType = functionBuilder.withTemplateName("templateName").build();

    assertFunctionsEqual(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    FunctionType expectedFunctionType = new FunctionType(registry, null, null, null, null, null, true, false);

    FunctionType actualFunctionType = functionBuilder.forConstructor().build();

    assertFunctionsEqual(expectedFunctionType, actualFunctionType);
  }

  @Test(expected = AssertionError.class)
  public void testFunctionBuilderForNativeTypeWithInvalidNode() {
    Node sourceNode = mock(Node.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.forNativeType();
    functionBuilder.withSourceNode(sourceNode);
    functionBuilder.build();
  }

  @Test
  public void testFunctionBuilderForNativeTypeWithValidNode() {
    Node sourceNode = mock(Node.class);
    FunctionType expectedFunctionType = new FunctionType(registry, null, sourceNode, null, null, null, true, true);

    FunctionType actualFunctionType = functionBuilder.forNativeType().withSourceNode(sourceNode).build();

    assertFunctionsEqual(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    String name = "functionName";
    Node sourceNode = mock(Node.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(Type.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    String templateTypeName = "templateTypeName";
    boolean isConstructor = true;
    boolean isNativeType = true;

    FunctionType otherFunctionType = new FunctionType(registry, name, sourceNode, null, null, null, isConstructor, isNativeType);
    otherFunctionType.setReturnType(returnType);
    otherFunctionType.setTypeOfThis(typeOfThis);
    otherFunctionType.setTemplateTypeName(templateTypeName);

    FunctionType expectedFunctionType = functionBuilder.copyFromOtherFunction(otherFunctionType);

    assertFunctionsEqual(expectedFunctionType, otherFunctionType);
  }

  private void assertFunctionsEqual(FunctionType expectedFunctionType, FunctionType actualFunctionType) {
    assertNotNull(expectedFunctionType);
    assertNotNull(actualFunctionType);

    assertEquals(expectedFunctionType.getName(), actualFunctionType.getName());
    assertEquals(expectedFunctionType.getSource(), actualFunctionType.getSource());
    assertEquals(expectedFunctionType.getParametersNode(), actualFunctionType.getParametersNode());
    assertEquals(expectedFunctionType.getReturnType(), actualFunctionType.getReturnType());
    assertEquals(expectedFunctionType.getTypeOfThis(), actualFunctionType.getTypeOfThis());
    assertEquals(expectedFunctionType.getTemplateTypeName(), actualFunctionType.getTemplateTypeName());
    assertEquals(expectedFunctionType.isConstructor(), actualFunctionType.isConstructor());
    assertEquals(expectedFunctionType.isNativeObjectType(), actualFunctionType.isNativeObjectType());
  }
}