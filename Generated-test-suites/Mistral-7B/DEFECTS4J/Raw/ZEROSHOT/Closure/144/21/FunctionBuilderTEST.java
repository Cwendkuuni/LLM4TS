import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.types.JSType;
import com.google.javascript.rhino.types.ObjectType;
import com.google.javascript.rhino.types.FunctionType;
import com.google.javascript.rhino.types.arrow.ArrowType;

public class FunctionBuilderTest {

  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testFunctionBuilderWithAllProperties() {
    String name = "testFunction";
    Node sourceNode = mock(Node.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    String templateTypeName = "testTemplateType";
    functionBuilder.withName(name)
        .withSourceNode(sourceNode)
        .withParams(new FunctionParamBuilder().withName("paramName").build())
        .withReturnType(returnType)
        .withTypeOfThis(typeOfThis)
        .withTemplateName(templateTypeName)
        .forConstructor();

    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertNotNull(functionType.getName());
    assertNotNull(functionType.getSource());
    assertNotNull(functionType.getParametersNode());
    assertNotNull(functionType.getReturnType());
    assertNotNull(functionType.getTypeOfThis());
    assertNotNull(functionType.getTemplateTypeName());
    assertNotNull(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderWithOnlyReturnType() {
    JSType returnType = mock(JSType.class);
    functionBuilder.withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNotNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderWithOnlyName() {
    String name = "testFunction";
    functionBuilder.withName(name);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals(name, functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSType returnType = mock(JSType.class);
    functionBuilder.withInferredReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNotNull(functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
  }

  @Test
  public void testFunctionBuilderWithNoProperties() {
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderWithNativeType() {
    functionBuilder.forNativeType();
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertTrue(functionType.isNativeType());
  }

  @Test
  public void testFunctionBuilderWithConstructor() {
    functionBuilder.forConstructor();
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderWithTemplateTypeName() {
    String templateTypeName = "testTemplateType";
    functionBuilder.withTemplateName(templateTypeName);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    ObjectType typeOfThis = mock(ObjectType.class);
    functionBuilder.withTypeOfThis(typeOfThis);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderWithParameters() {
    String paramName = "testParam";
    Node paramNode = mock(Node.class);
    functionBuilder.withParamsNode(paramNode);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder().withName(paramName);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    ArrowType arrowType = (ArrowType) functionType.getReturnType();
    assertNotNull(arrowType);
    assertEquals(1, arrowType.getParametersNode().getChildCount());
    Node paramTypeNode = arrowType.getParametersNode().getChild(0);
    assertNotNull(paramTypeNode);
    JSType paramType = paramTypeNode.getFirstChild().getValue();
    assertNotNull(paramType);
    assertEquals(paramName, paramType.getName());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderWithReturnTypeAndInferred() {
    JSType returnType = mock(JSType.class);
    functionBuilder.withInferredReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    ArrowType arrowType = (ArrowType) functionType.getReturnType();
    assertNotNull(arrowType);
    assertEquals(0, arrowType.getParametersNode().getChildCount());
    assertNotNull(arrowType.getReturnType());
    assertEquals(returnType, arrowType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderWithReturnTypeAndNotInferred() {
    JSType returnType = mock(JSType.class);
    functionBuilder.withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    ArrowType arrowType = (ArrowType) functionType.getReturnType();
    assertNotNull(arrowType);
    assertEquals(0, arrowType.getParametersNode().getChildCount());
    assertNotNull(arrowType.getReturnType());
    assertEquals(returnType, arrowType.getReturnType());
    assertFalse(functionType.isInferredReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThisAndReturnType() {
    ObjectType typeOfThis = mock(ObjectType.class);
    JSType returnType = mock(JSType.class);
    functionBuilder.withTypeOfThis(typeOfThis);
    functionBuilder.withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    ArrowType arrowType = (ArrowType) functionType.getReturnType();
    assertNotNull(arrowType);
    assertEquals(0, arrowType.getParametersNode().getChildCount());
    assertNotNull(arrowType.getReturnType());
    assertEquals(returnType, arrowType.getReturnType());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderWithTemplateTypeNameAndReturnType() {
    String templateTypeName = "testTemplateType";
    JSType returnType = mock(JSType.class);
    functionBuilder.withTemplateName(templateTypeName);
    functionBuilder.withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    ArrowType arrowType = (ArrowType) functionType.getReturnType();
    assertNotNull(arrowType);
    assertEquals(0, arrowType.getParametersNode().getChildCount());
    assertNotNull(arrowType.getReturnType());
    assertEquals(returnType, arrowType.getReturnType());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThisAndTemplateTypeName() {
    ObjectType typeOfThis = mock(ObjectType.class);
    String templateTypeName = "testTemplateType";
    functionBuilder.withTypeOfThis(typeOfThis);
    functionBuilder.withTemplateName(templateTypeName);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThisAndReturnTypeAndTemplateTypeName() {
    ObjectType typeOfThis = mock(ObjectType.class);
    JSType returnType = mock(JSType.class);
    String templateTypeName = "testTemplateType";
    functionBuilder.withTypeOfThis(typeOfThis);
    functionBuilder.withReturnType(returnType);
    functionBuilder.withTemplateName(templateTypeName);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    ArrowType arrowType = (ArrowType) functionType.getReturnType();
    assertNotNull(arrowType);
    assertEquals(0, arrowType.getParametersNode().getChildCount());
    assertNotNull(arrowType.getReturnType());
    assertEquals(returnType, arrowType.getReturnType());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
  }
}