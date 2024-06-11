import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class FunctionBuilderTest {

  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;
  private FunctionParamBuilder functionParamBuilder;
  private ObjectType objectType;

  @Before
  public void setup() {
    registry = mock(JSTypeRegistry.class);
    functionBuilder = new FunctionBuilder(registry);
    functionParamBuilder = mock(FunctionParamBuilder.class);
    objectType = mock(ObjectType.class);
  }

  @Test
  public void testFunctionBuilderWithName() {
    String name = "myFunction";
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withName(name);
    assertEquals(name, functionBuilder.getName());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    Node sourceNode = mock(Node.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withSourceNode(sourceNode);
    assertSame(sourceNode, functionBuilder.getSourceNode());
  }

  @Test
  public void testFunctionBuilderWithParamsNode() {
    Node parametersNode = mock(Node.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withParamsNode(parametersNode);
    assertSame(parametersNode, functionBuilder.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithReturnType() {
    JSType returnType = mock(JSType.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withReturnType(returnType);
    assertEquals(returnType, functionBuilder.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withTypeOfThis(objectType);
    assertSame(objectType, functionBuilder.getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).forConstructor();
    assertTrue(functionBuilder.isConstructor());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).forNativeType();
    assertTrue(functionBuilder.isNativeType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    String name = "myFunction";
    Node sourceNode = mock(Node.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    FunctionType otherType = new FunctionType(registry, name, sourceNode, null, returnType, typeOfThis, true, false);

    FunctionBuilder functionBuilder = new FunctionBuilder(registry).copyFromOtherFunction(otherType);

    assertEquals(name, functionBuilder.getName());
    assertSame(sourceNode, functionBuilder.getSourceNode());
    assertSame(parametersNode, functionBuilder.getParametersNode());
    assertSame(returnType, functionBuilder.getReturnType());
    assertSame(typeOfThis, functionBuilder.getTypeOfThis());
    assertTrue(functionBuilder.isConstructor());
    assertFalse(functionBuilder.isNativeType());
  }

  @Test(expected = IllegalStateException.class)
  public void testFunctionBuilderWithParamsAfterReturnType() {
    JSType returnType = mock(JSType.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withReturnType(returnType);
    functionBuilder.withParamsNode(mock(Node.class));
  }

  @Test
  public void testFunctionBuilderWithParams() {
    FunctionParamBuilder functionParamBuilder = mock(FunctionParamBuilder.class);
    when(functionParamBuilder.build()).thenReturn(mock(Node.class));

    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withParams(functionParamBuilder);
    Node parametersNode = functionBuilder.getParametersNode();

    assertEquals(parametersNode, functionBuilder.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSType returnType = mock(JSType.class);
    FunctionBuilder functionBuilder =
        new FunctionBuilder(registry).withReturnType(returnType).withInferredReturnType(returnType);

    assertEquals(returnType, functionBuilder.getReturnType());
    assertTrue(functionBuilder.isInferredReturnType());
  }

  @Test
  public void testFunctionBuilderWithTemplateName() {
    String templateTypeName = "myTemplate";
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withTemplateName(templateTypeName);
    assertEquals(templateTypeName, functionBuilder.getTemplateTypeName());
  }
}