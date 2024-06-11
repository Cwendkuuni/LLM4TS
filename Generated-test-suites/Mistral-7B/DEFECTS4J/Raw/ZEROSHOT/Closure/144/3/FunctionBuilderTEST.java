import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilder() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionType functionType = functionBuilder.build();
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithName() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String name = "testFunction";
    FunctionType functionType = functionBuilder.withName(name).build();

    assertEquals(name, functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    Node sourceNode = mock(Node.class);
    FunctionType functionType = functionBuilder.withSourceNode(sourceNode).build();

    when(sourceNode.getFirstChild()).thenReturn(null);
    assertEquals(sourceNode, functionType.getSource());
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithParams() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionParamBuilder paramBuilder1 = mock(FunctionParamBuilder.class);
    FunctionParamBuilder paramBuilder2 = mock(FunctionParamBuilder.class);

    when(paramBuilder1.build()).thenReturn(mock(Node.class));
    when(paramBuilder2.build()).thenReturn(mock(Node.class));

    Node parametersNode = mock(Node.class);
    when(parametersNode.getFirstChild()).thenReturn(paramBuilder1.build());
    when(parametersNode.getLastChild()).thenReturn(paramBuilder2.build());

    FunctionType functionType = functionBuilder.withParams(paramBuilder1).withParams(paramBuilder2)
        .withParamsNode(parametersNode).build();

    assertEquals(parametersNode, functionType.getParametersNode());
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    JSType returnType = mock(JSType.class);

    FunctionType functionType = functionBuilder.withReturnType(returnType).build();

    assertEquals(returnType, functionType.getReturnType());
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    JSType returnType = mock(JSType.class);

    FunctionType functionType = functionBuilder.withReturnType(returnType)
        .withInferredReturnType(returnType).build();

    assertEquals(returnType, functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    ObjectType typeOfThis = mock(ObjectType.class);

    FunctionType functionType = functionBuilder.withTypeOfThis(typeOfThis).build();

    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithTemplateTypeName() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String templateTypeName = "testTemplate";

    FunctionType functionType = functionBuilder.withTemplateName(templateTypeName).build();

    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionType functionType = functionBuilder.forConstructor().build();

    assertTrue(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionType functionType = functionBuilder.forNativeType().build();

    assertFalse(functionType.isConstructor());
    assertTrue(functionType.isNativeObjectType());
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String name = "testFunction";
    Node sourceNode = mock(Node.class);
    Node parametersNode = mock(Node.class);
    ArrowType returnType = mock(ArrowType.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    String templateTypeName = "testTemplate";
    boolean isConstructor = true;

    FunctionType otherFunctionType = new FunctionType(registry, name, sourceNode, returnType, typeOfThis,
        templateTypeName, isConstructor, false);

    FunctionType functionType = functionBuilder.copyFromOtherFunction(otherFunctionType).build();

    assertEquals(otherFunctionType.getReferenceName(), functionType.getReferenceName());
    assertEquals(otherFunctionType.getSource(), functionType.getSource());
    assertEquals(otherFunctionType.getParametersNode(), functionType.getParametersNode());
    assertEquals(otherFunctionType.getReturnType(), functionType.getReturnType());
    assertEquals(otherFunctionType.getTypeOfThis(), functionType.getTypeOfThis());
    assertEquals(otherFunctionType.getTemplateTypeName(), functionType.getTemplateTypeName());
    assertEquals(otherFunctionType.isConstructor(), functionType.isConstructor());
    assertEquals(otherFunctionType.isNativeObjectType(), functionType.isNativeObjectType());
  }
}