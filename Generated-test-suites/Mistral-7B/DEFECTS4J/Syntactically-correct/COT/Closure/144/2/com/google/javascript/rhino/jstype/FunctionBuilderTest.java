import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.types.ArrowType;
import com.google.javascript.rhino.types.FunctionType;
import com.google.javascript.rhino.types.ObjectType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class FunctionBuilderTest {

  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;
  private FunctionType functionType;

  @Before
  public void setup() {
    registry = mock(JSTypeRegistry.class);
    functionBuilder = new FunctionBuilder(registry);
    functionType = mock(FunctionType.class);
  }

  @Test
  public void testFunctionBuilderWithName() {
    String name = "testFunction";
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withName(name);
    assertNotNull(functionBuilder.getName());
    assertNull(functionBuilder.getNameNode());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    Node sourceNode = mock(Node.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withSourceNode(sourceNode);
    assertNotNull(functionBuilder.getSourceNode());
  }

  @Test
  public void testFunctionBuilderWithParams() {
    FunctionParamBuilder functionParamBuilder = mock(FunctionParamBuilder.class);
    Node parametersNode = mock(Node.class);
    when(functionParamBuilder.build()).thenReturn(parametersNode);

    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withParams(functionParamBuilder);

    assertNotNull(functionBuilder.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithParamsNode() {
    Node parametersNode = mock(Node.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withParamsNode(parametersNode);

    assertNotNull(functionBuilder.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithReturnType() {
    JSType returnType = mock(JSType.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withReturnType(returnType);
    assertNotNull(functionBuilder.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSType returnType = mock(JSType.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withReturnType(returnType)
        .withInferredReturnType(returnType);

    assertNotNull(functionBuilder.getReturnType());
    assertTrue(functionBuilder.isInferredReturnType());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    ObjectType typeOfThis = mock(ObjectType.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withTypeOfThis(typeOfThis);

    assertNotNull(functionBuilder.getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderWithTemplateName() {
    String templateTypeName = "testTemplate";
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withTemplateName(templateTypeName);

    assertNull(functionBuilder.getTemplateTypeName());
    assertEquals(templateTypeName, functionBuilder.getTemplateName());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .forConstructor();

    assertTrue(functionBuilder.isConstructor());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .forNativeType();

    assertTrue(functionBuilder.isNativeType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionType otherFunctionType = mock(FunctionType.class);

    when(otherFunctionType.getReferenceName()).thenReturn("otherFunction");
    when(otherFunctionType.getSource()).thenReturn(null);
    when(otherFunctionType.getParametersNode()).thenReturn(null);
    when(otherFunctionType.getReturnType()).thenReturn(null);
    when(otherFunctionType.getTypeOfThis()).thenReturn(null);
    when(otherFunctionType.getTemplateTypeName()).thenReturn("otherTemplate");
    when(otherFunctionType.isConstructor()).thenReturn(false);
    when(otherFunctionType.isNativeType()).thenReturn(false);

    functionBuilder.copyFromOtherFunction(otherFunctionType);

    assertEquals("otherFunction", functionBuilder.getName());
    assertNull(functionBuilder.getSourceNode());
    assertNull(functionBuilder.getParametersNode());
    assertNull(functionBuilder.getReturnType());
    assertNull(functionBuilder.getTypeOfThis());
    assertEquals("otherTemplate", functionBuilder.getTemplateName());
    assertFalse(functionBuilder.isConstructor());
    assertFalse(functionBuilder.isNativeType());
  }

  @Test
  public void testFunctionBuilderBuild() {
    Node sourceNode = mock(Node.class);
    FunctionParamBuilder functionParamBuilder = mock(FunctionParamBuilder.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    String templateTypeName = "testTemplate";

    when(functionParamBuilder.build()).thenReturn(parametersNode);

    functionBuilder.withName("testFunction")
        .withSourceNode(sourceNode)
        .withParams(functionParamBuilder)
        .withReturnType(returnType);

    FunctionType expectedFunctionType = new FunctionType(registry, "testFunction",
        sourceNode, new ArrowType(registry, parametersNode, returnType, false),
        typeOfThis, templateTypeName, false, false);

    FunctionType actualFunctionType = functionBuilder.build();

    assertEquals(expectedFunctionType.getReferenceName(), actualFunctionType.getReferenceName());
    assertEquals(expectedFunctionType.getSource(), actualFunctionType.getSource());
    assertEquals(expectedFunctionType.getParametersNode(), actualFunctionType.getParametersNode());
    assertEquals(expectedFunctionType.getReturnType(), actualFunctionType.getReturnType());
    assertEquals(expectedFunctionType.getTypeOfThis(), actualFunctionType.getTypeOfThis());
    assertEquals(expectedFunctionType.getTemplateTypeName(), actualFunctionType.getTemplateTypeName());
    assertFalse(expectedFunctionType.isConstructor());
    assertFalse(expectedFunctionType.isNativeType());
  }
}