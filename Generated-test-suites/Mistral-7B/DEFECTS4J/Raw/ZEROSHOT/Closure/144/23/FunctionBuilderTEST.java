import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;
  private FunctionParamBuilder functionParamBuilder;
  private Node parametersNode;
  private ObjectType objectType;

  @Before
  public void setup() {
    registry = mock(JSTypeRegistry.class);
    functionBuilder = new FunctionBuilder(registry);
    functionParamBuilder = mock(FunctionParamBuilder.class);
    objectType = mock(ObjectType.class);

    when(registry.createObjectType("TestObjectType")).thenReturn(objectType);
    when(functionParamBuilder.build()).thenReturn(parametersNode);
  }

  @Test
  public void testFunctionBuilderWithAllParameters() {
    functionBuilder.withName("testFunction")
        .withSourceNode(mock(Node.class))
        .withParams(functionParamBuilder)
        .withReturnType(registry.createBooleanType())
        .withTypeOfThis(objectType)
        .build();

    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
  }

  @Test
  public void testFunctionBuilderWithAllParametersNode() {
    functionBuilder.withName("testFunction")
        .withSourceNode(mock(Node.class))
        .withParamsNode(parametersNode)
        .withReturnType(registry.createBooleanType())
        .withTypeOfThis(objectType)
        .build();

    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
  }

  @Test
  public void testFunctionBuilderWithReturnTypeInferred() {
    functionBuilder.withName("testFunction")
        .withSourceNode(mock(Node.class))
        .withParams(functionParamBuilder)
        .withReturnType(null)
        .withInferredReturnType(registry.createNumberType())
        .build();

    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    functionBuilder.forConstructor();
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    functionBuilder.forNativeType();
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    FunctionType otherType = mock(FunctionType.class);
    functionBuilder.copyFromOtherFunction(otherType);

    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
  }
}