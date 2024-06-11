import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.Type;
import com.google.javascript.rhino.types.ArrowType;
import com.google.javascript.rhino.types.FunctionType;
import com.google.javascript.rhino.types.NameType;

public class FunctionBuilderTest {

  private FunctionBuilder functionBuilder;
  private JSTypeRegistry registry;
  private Node sourceNode;
  private Node parametersNode;
  private ObjectType typeOfThis;
  private JSType returnType;
  private FunctionType expectedFunctionType;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    sourceNode = mock(Node.class);
    parametersNode = mock(Node.class);
    typeOfThis = mock(ObjectType.class);
    returnType = mock(JSType.class);

    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testNewFunctionBuilder() {
    FunctionType actualFunctionType = functionBuilder.build();
    assertNull(actualFunctionType.getName());
    assertNull(actualFunctionType.getSource());
    assertNull(actualFunctionType.getParametersNode());
    assertNull(actualFunctionType.getReturnType());
    assertNull(actualFunctionType.getTypeOfThis());
    assertNull(actualFunctionType.getTemplateTypeName());
    assertFalse(actualFunctionType.isConstructor());
    assertFalse(actualFunctionType.isNativeObjectType());
  }

  @Test
  public void testWithName() {
    String name = "testFunction";
    functionBuilder.withName(name);
    expectedFunctionType = new FunctionType(registry, name, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, null, false, false);

    FunctionType actualFunctionType = functionBuilder.build();
    assertSame(name, actualFunctionType.getName());
    assertNull(actualFunctionType.getSource());
    assertNull(actualFunctionType.getParametersNode());
    assertNull(actualFunctionType.getReturnType());
    assertNull(actualFunctionType.getTypeOfThis());
    assertNull(actualFunctionType.getTemplateTypeName());
    assertFalse(actualFunctionType.isConstructor());
    assertFalse(actualFunctionType.isNativeObjectType());
  }

  @Test
  public void testWithSourceNode() {
    functionBuilder.withSourceNode(sourceNode);
    expectedFunctionType = new FunctionType(registry, null, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, null, false, false);

    FunctionType actualFunctionType = functionBuilder.build();
    assertNull(actualFunctionType.getName());
    assertSame(sourceNode, actualFunctionType.getSource());
    assertNull(actualFunctionType.getParametersNode());
    assertNull(actualFunctionType.getReturnType());
    assertNull(actualFunctionType.getTypeOfThis());
    assertNull(actualFunctionType.getTemplateTypeName());
    assertFalse(actualFunctionType.isConstructor());
    assertFalse(actualFunctionType.isNativeObjectType());
  }

  @Test
  public void testWithParams() {
    Node parametersNode = mock(Node.class);
    FunctionParamBuilder functionParamBuilder = mock(FunctionParamBuilder.class);
    when(functionParamBuilder.build()).thenReturn(parametersNode);

    functionBuilder.withParams(functionParamBuilder);

    FunctionType expectedFunctionType = new FunctionType(registry, null, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, null, false, false);

    FunctionType actualFunctionType = functionBuilder.build();
    assertNull(actualFunctionType.getName());
    assertNull(actualFunctionType.getSource());
    assertSame(parametersNode, actualFunctionType.getParametersNode());
    assertNull(actualFunctionType.getReturnType());
    assertNull(actualFunctionType.getTypeOfThis());
    assertNull(actualFunctionType.getTemplateTypeName());
    assertFalse(actualFunctionType.isConstructor());
    assertFalse(actualFunctionType.isNativeObjectType());
  }

  @Test
  public void testWithParamsNode() {
    functionBuilder.withParamsNode(parametersNode);

    FunctionType expectedFunctionType = new FunctionType(registry, null, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, null, false, false);

    FunctionType actualFunctionType = functionBuilder.build();
    assertNull(actualFunctionType.getName());
    assertNull(actualFunctionType.getSource());
    assertSame(parametersNode, actualFunctionType.getParametersNode());
    assertNull(actualFunctionType.getReturnType());
    assertNull(actualFunctionType.getTypeOfThis());
    assertNull(actualFunctionType.getTemplateTypeName());
    assertFalse(actualFunctionType.isConstructor());
    assertFalse(actualFunctionType.isNativeObjectType());
  }

  @Test
  public void testWithReturnType() {
    functionBuilder.withReturnType(returnType);
    expectedFunctionType = new FunctionType(registry, null, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, null, false, false);

    FunctionType actualFunctionType = functionBuilder.build();
    assertNull(actualFunctionType.getName());
    assertNull(actualFunctionType.getSource());
    assertNull(actualFunctionType.getParametersNode());
    assertSame(returnType, actualFunctionType.getReturnType());
    assertNull(actualFunctionType.getTypeOfThis());
    assertNull(actualFunctionType.getTemplateTypeName());
    assertFalse(actualFunctionType.isConstructor());
    assertFalse(actualFunctionType.isNativeObjectType());
  }

  @Test
  public void testWithInferredReturnType() {
    functionBuilder.withInferredReturnType(returnType);
    expectedFunctionType = new FunctionType(registry, null, sourceNode, new ArrowType(registry, parametersNode, returnType, true), typeOfThis, null, false, false);

    FunctionType actualFunctionType = functionBuilder.build();
    assertNull(actualFunctionType.getName());
    assertNull(actualFunctionType.getSource());
    assertNull(actualFunctionType.getParametersNode());
    assertSame(returnType, actualFunctionType.getReturnType());
    assertNull(actualFunctionType.getTypeOfThis());
    assertNull(actualFunctionType.getTemplateTypeName());
    assertFalse(actualFunctionType.isConstructor());
    assertFalse(actualFunctionType.isNativeObjectType());
  }

  @Test
  public void testWithTypeOfThis() {
    functionBuilder.withTypeOfThis(typeOfThis);
    expectedFunctionType = new FunctionType(registry, null, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, null, false, false);

    FunctionType actualFunctionType = functionBuilder.build();
    assertNull(actualFunctionType.getName());
    assertNull(actualFunctionType.getSource());
    assertNull(actualFunctionType.getParametersNode());
    assertNull(actualFunctionType.getReturnType());
    assertSame(typeOfThis, actualFunctionType.getTypeOfThis());
    assertNull(actualFunctionType.getTemplateTypeName());
    assertFalse(actualFunctionType.isConstructor());
    assertFalse(actualFunctionType.isNativeObjectType());
  }

  @Test
  public void testWithTemplateName() {
    String templateTypeName = "TemplateTypeName";
    functionBuilder.withTemplateName(templateTypeName);
    expectedFunctionType = new FunctionType(registry, null, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, templateTypeName, false, false);

    FunctionType actualFunctionType = functionBuilder.build();
    assertNull(actualFunctionType.getName());
    assertNull(actualFunctionType.getSource());
    assertNull(actualFunctionType.getParametersNode());
    assertNull(actualFunctionType.getReturnType());
    assertNull(actualFunctionType.getTypeOfThis());
    assertSame(templateTypeName, actualFunctionType.getTemplateTypeName());
    assertFalse(actualFunctionType.isConstructor());
    assertFalse(actualFunctionType.isNativeObjectType());
  }

  @Test
  public void testForConstructor() {
    functionBuilder.forConstructor();
    expectedFunctionType = new FunctionType(registry, null, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, null, true, false);

    FunctionType actualFunctionType = functionBuilder.build();
    assertNull(actualFunctionType.getName());
    assertNull(actualFunctionType.getSource());
    assertNull(actualFunctionType.getParametersNode());
    assertNull(actualFunctionType.getReturnType());
    assertNull(actualFunctionType.getTypeOfThis());
    assertNull(actualFunctionType.getTemplateTypeName());
    assertTrue(actualFunctionType.isConstructor());
    assertFalse(actualFunctionType.isNativeObjectType());
  }

  @Test(expected = AssertionError.class)
  public void testForConstructorTwice() {
    functionBuilder.forConstructor();
    functionBuilder.forConstructor();
  }

  @Test
  public void testForNativeType() {
    functionBuilder.forNativeType();
    expectedFunctionType = new FunctionType(registry, null, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, null, false, true);

    FunctionType actualFunctionType = functionBuilder.build();
    assertNull(actualFunctionType.getName());
    assertNull(actualFunctionType.getSource());
    assertNull(actualFunctionType.getParametersNode());
    assertNull(actualFunctionType.getReturnType());
    assertNull(actualFunctionType.getTypeOfThis());
    assertNull(actualFunctionType.getTemplateTypeName());
    assertFalse(actualFunctionType.isConstructor());
    assertTrue(actualFunctionType.isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    String name = "testFunction";
    Node sourceNode = mock(Node.class);
    Node parametersNode = mock(Node.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    JSType returnType = mock(JSType.class);
    FunctionType otherFunctionType = new FunctionType(registry, name, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, null, false, false);

    functionBuilder.copyFromOtherFunction(otherFunctionType);

    FunctionType expectedFunctionType = new FunctionType(registry, name, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, null, false, false);

    FunctionType actualFunctionType = functionBuilder.build();
    assertSame(name, actualFunctionType.getName());
    assertSame(sourceNode, actualFunctionType.getSource());
    assertSame(parametersNode, actualFunctionType.getParametersNode());
    assertSame(returnType, actualFunctionType.getReturnType());
    assertSame(typeOfThis, actualFunctionType.getTypeOfThis());
    assertNull(actualFunctionType.getTemplateTypeName());
    assertFalse(actualFunctionType.isConstructor());
    assertFalse(actualFunctionType.isNativeObjectType());
  }
}