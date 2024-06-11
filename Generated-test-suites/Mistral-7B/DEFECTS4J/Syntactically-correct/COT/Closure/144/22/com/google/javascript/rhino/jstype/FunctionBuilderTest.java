import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilderWithName() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testFunction";
    FunctionBuilder functionBuilderWithName = functionBuilder.withName(name);
    assertSame(name, functionBuilderWithName.build().getName());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    Node sourceNode = mock(Node.class);
    FunctionBuilder functionBuilderWithSourceNode = functionBuilder.withSourceNode(sourceNode);
    when(functionBuilderWithSourceNode.build().getSource()).thenReturn(sourceNode);
  }

  @Test
  public void testFunctionBuilderWithParams() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);
    Node parametersNode = mock(Node.class);
    when(paramBuilder.build()).thenReturn(parametersNode);
    FunctionBuilder functionBuilderWithParams = functionBuilder.withParams(paramBuilder);
    assertSame(parametersNode, functionBuilderWithParams.build().getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithParamsNode() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    Node parametersNode = mock(Node.class);
    FunctionBuilder functionBuilderWithParamsNode = functionBuilder.withParamsNode(parametersNode);
    assertSame(parametersNode, functionBuilderWithParamsNode.build().getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    JSType returnType = mock(JSType.class);
    FunctionBuilder functionBuilderWithReturnType = functionBuilder.withReturnType(returnType);
    when(functionBuilderWithReturnType.build().getReturnType()).thenReturn(returnType);
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    JSType returnType = mock(JSType.class);
    FunctionBuilder functionBuilderWithInferredReturnType =
        functionBuilder.withInferredReturnType(returnType);
    when(functionBuilderWithInferredReturnType.build().getReturnType()).thenReturn(returnType);
    when(functionBuilderWithInferredReturnType.build().isInferredReturnType()).thenReturn(true);
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    ObjectType typeOfThis = mock(ObjectType.class);
    FunctionBuilder functionBuilderWithTypeOfThis = functionBuilder.withTypeOfThis(typeOfThis);
    assertSame(typeOfThis, functionBuilderWithTypeOfThis.build().getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderWithTemplateName() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String templateTypeName = "testTemplateName";
    FunctionBuilder functionBuilderWithTemplateName = functionBuilder.withTemplateName(templateTypeName);
    assertSame(templateTypeName, functionBuilderWithTemplateName.build().getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionBuilder functionBuilderForConstructor = functionBuilder.forConstructor();
    assertTrue(functionBuilderForConstructor.isConstructor());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionBuilder functionBuilderForNativeType = functionBuilder.forNativeType();
    assertTrue(functionBuilderForNativeType.isNativeType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionType otherFunctionType = mock(FunctionType.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionBuilder functionBuilderCopy = functionBuilder.copyFromOtherFunction(otherFunctionType);
    assertNull(functionBuilderCopy.name);
    assertSame(otherFunctionType.getSource(), functionBuilderCopy.build().getSource());
    assertSame(otherFunctionType.getParametersNode(), functionBuilderCopy.build().getParametersNode());
    assertSame(otherFunctionType.getReturnType(), functionBuilderCopy.build().getReturnType());
    assertSame(otherFunctionType.getTypeOfThis(), functionBuilderCopy.build().getTypeOfThis());
    assertSame(otherFunctionType.getTemplateTypeName(), functionBuilderCopy.build().getTemplateTypeName());
    assertTrue(functionBuilderCopy.isConstructor());
    assertTrue(functionBuilderCopy.isNativeType());
  }

  @Test(expected = IllegalStateException.class)
  public void testFunctionBuilderBuildWithoutName() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionBuilder functionBuilderWithoutName = functionBuilder.build();
    assertNull(functionBuilderWithoutName);
  }
}