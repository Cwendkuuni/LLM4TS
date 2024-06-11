import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {

  private FunctionBuilder functionBuilder;
  private FunctionParamBuilder functionParamBuilder;
  private JSTypeRegistry registry;
  private ObjectType objectType;
  private Node node;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    functionBuilder = new FunctionBuilder(registry);
    functionParamBuilder = mock(FunctionParamBuilder.class);
    objectType = mock(ObjectType.class);
    node = mock(Node.class);
  }

  @Test
  public void testFunctionBuilderWithName() {
    String name = "testFunction";
    functionBuilder.withName(name);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getReferenceName());
    assertNotNull(functionType.getReferenceName());
    assertNotNull(functionType.getName());
    assertNotNull(functionType.getName());
    assertEquals(name, functionType.getName());
    assertEquals(name, functionType.getReferenceName());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    functionBuilder.withSourceNode(node);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getSource());
    assertEquals(node, functionType.getSource());
  }

  @Test
  public void testFunctionBuilderWithParams() {
    FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);
    when(paramBuilder.build()).thenReturn(node);
    functionBuilder.withParams(paramBuilder);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getParametersNode());
    assertEquals(node, functionType.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithReturnType() {
    JSType returnType = mock(JSType.class);
    functionBuilder.withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getReturnType());
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSType returnType = mock(JSType.class);
    functionBuilder.withInferredReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    functionBuilder.withTypeOfThis(objectType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getTypeOfThis());
    assertEquals(objectType, functionType.getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderWithTemplateName() {
    String templateTypeName = "testTemplate";
    functionBuilder.withTemplateName(templateTypeName);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getTemplateTypeName());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    functionBuilder.forConstructor();
    FunctionType functionType = functionBuilder.build();
    assertTrue(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    functionBuilder.forNativeType();
    FunctionType functionType = functionBuilder.build();
    assertTrue(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    FunctionType otherType = mock(FunctionType.class);
    when(otherType.getReferenceName()).thenReturn("testFunction");
    when(otherType.getSource()).thenReturn(node);
    when(otherType.getParametersNode()).thenReturn(node);
    when(otherType.getReturnType()).thenReturn(node);
    when(otherType.getTypeOfThis()).thenReturn(objectType);
    when(otherType.getTemplateTypeName()).thenReturn("testTemplate");
    when(otherType.isConstructor()).thenReturn(true);
    when(otherType.isNativeObjectType()).thenReturn(true);

    functionBuilder.copyFromOtherFunction(otherType);
    FunctionType functionType = functionBuilder.build();

    assertEquals("testFunction", functionType.getReferenceName());
    assertEquals("testFunction", functionType.getName());
    assertEquals(node, functionType.getSource());
    assertEquals(node, functionType.getParametersNode());
    assertEquals(node, functionType.getReturnType());
    assertEquals(objectType, functionType.getTypeOfThis());
    assertEquals("testTemplate", functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertTrue(functionType.isNativeObjectType());
  }
}