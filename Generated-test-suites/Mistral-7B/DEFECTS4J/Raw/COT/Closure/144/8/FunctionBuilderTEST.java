import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;
  private FunctionType functionType;
  private FunctionParamBuilder functionParamBuilder;
  private ObjectType objectType;
  private Node node;

  @Before
  public void setup() {
    registry = mock(JSTypeRegistry.class);
    functionBuilder = new FunctionBuilder(registry);
    functionType = mock(FunctionType.class);
    functionParamBuilder = mock(FunctionParamBuilder.class);
    objectType = mock(ObjectType.class);
    node = mock(Node.class);
  }

  // Test for constructor
  @Test
  public void testFunctionBuilderWithRegistry() {
    functionBuilder = new FunctionBuilder(registry);
    assertNotNull(functionBuilder);
  }

  // Test for withName method
  @Test
  public void testFunctionBuilderWithName() {
    functionBuilder.withName("testFunction");
    assertNull(functionBuilder.name);
  }

  // Test for withSourceNode method
  @Test
  public void testFunctionBuilderWithSourceNode() {
    functionBuilder.withSourceNode(node);
    assertNull(functionBuilder.sourceNode);
  }

  // Test for withParams method
  @Test
  public void testFunctionBuilderWithParams() {
    when(functionParamBuilder.build()).thenReturn(node);
    functionBuilder.withParams(functionParamBuilder);
    assertNull(functionBuilder.parametersNode);
  }

  // Test for withParamsNode method
  @Test
  public void testFunctionBuilderWithParamsNode() {
    functionBuilder.withParamsNode(node);
    assertNull(functionBuilder.parametersNode);
  }

  // Test for withReturnType method
  @Test
  public void testFunctionBuilderWithReturnType() {
    when(registry.createFunctionType(node, null, null, null, null, null, false, false)).thenReturn(functionType);
    functionBuilder.withReturnType(new JSTypeRegistry() {}.createFunctionType(node, null, null, null, null, null, false, false));
    functionBuilder.build();
    assertNotNull(functionBuilder.returnType);
  }

  // Test for withInferredReturnType method
  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    when(registry.createFunctionType(node, null, null, null, null, null, false, false)).thenReturn(functionType);
    functionBuilder.withInferredReturnType(new JSTypeRegistry() {}.createFunctionType(node, null, null, null, null, null, false, false));
    functionBuilder.build();
    assertNotNull(functionBuilder.returnType);
    assertNull(functionBuilder.inferredReturnType);
  }

  // Test for withTypeOfThis method
  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    functionBuilder.withTypeOfThis(objectType);
    assertNotNull(functionBuilder.typeOfThis);
  }

  // Test for withTemplateName method
  @Test
  public void testFunctionBuilderWithTemplateName() {
    functionBuilder.withTemplateName("testTemplateName");
    assertNull(functionBuilder.templateTypeName);
  }

  // Test for forConstructor method
  @Test
  public void testFunctionBuilderForConstructor() {
    functionBuilder.forConstructor();
    assertNotNull(functionBuilder.isConstructor);
  }

  // Test for forNativeType method
  @Test
  public void testFunctionBuilderForNativeType() {
    functionBuilder.forNativeType();
    assertNotNull(functionBuilder.isNativeType);
  }

  // Test for copyFromOtherFunction method
  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    when(functionType.getReferenceName()).thenReturn("testFunction");
    when(functionType.getSource()).thenReturn(node);
    when(functionType.getParametersNode()).thenReturn(node);
    when(functionType.getReturnType()).thenReturn(node);
    when(functionType.getTypeOfThis()).thenReturn(objectType);
    when(functionType.getTemplateTypeName()).thenReturn("testTemplateName");
    when(functionType.isConstructor()).thenReturn(true);
    when(functionType.isNativeObjectType()).thenReturn(true);

    functionBuilder.copyFromOtherFunction(functionType);

    assertNotNull(functionBuilder.name);
    assertNotNull(functionBuilder.sourceNode);
    assertNotNull(functionBuilder.parametersNode);
    assertNotNull(functionBuilder.returnType);
    assertNotNull(functionBuilder.typeOfThis);
    assertNotNull(functionBuilder.templateTypeName);
    assertNotNull(functionBuilder.isConstructor);
    assertNotNull(functionBuilder.isNativeType);
  }

  // Test for build method
  @Test(expected = IllegalStateException.class)
  public void testFunctionBuilderBuildThrowsExceptionWhenSomeFieldsAreNotSet() {
    functionBuilder.build();
  }
}