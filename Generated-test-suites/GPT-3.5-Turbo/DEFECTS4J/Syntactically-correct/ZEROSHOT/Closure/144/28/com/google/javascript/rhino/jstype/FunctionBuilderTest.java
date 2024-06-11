import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FunctionBuilderTest {

  @Mock
  private JSTypeRegistry registry;

  private FunctionBuilder functionBuilder;

  @Before
  public void setUp() {
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testWithName() {
    String name = "functionName";
    functionBuilder.withName(name);
    FunctionType functionType = functionBuilder.build();
    assertEquals(name, functionType.getReferenceName());
  }

  @Test
  public void testWithSourceNode() {
    Node sourceNode = Mockito.mock(Node.class);
    functionBuilder.withSourceNode(sourceNode);
    FunctionType functionType = functionBuilder.build();
    assertEquals(sourceNode, functionType.getSource());
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder params = Mockito.mock(FunctionParamBuilder.class);
    Node parametersNode = Mockito.mock(Node.class);
    Mockito.when(params.build()).thenReturn(parametersNode);
    functionBuilder.withParams(params);
    FunctionType functionType = functionBuilder.build();
    assertEquals(parametersNode, functionType.getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    Node parametersNode = Mockito.mock(Node.class);
    functionBuilder.withParamsNode(parametersNode);
    FunctionType functionType = functionBuilder.build();
    assertEquals(parametersNode, functionType.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = Mockito.mock(JSType.class);
    functionBuilder.withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = Mockito.mock(JSType.class);
    functionBuilder.withInferredReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(true, functionType.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = Mockito.mock(ObjectType.class);
    functionBuilder.withTypeOfThis(typeOfThis);
    FunctionType functionType = functionBuilder.build();
    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    String templateTypeName = "template";
    functionBuilder.withTemplateName(templateTypeName);
    FunctionType functionType = functionBuilder.build();
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    functionBuilder.forConstructor();
    FunctionType functionType = functionBuilder.build();
    assertEquals(true, functionType.isConstructor());
  }

  @Test
  public void testForNativeType() {
    functionBuilder.forNativeType();
    FunctionType functionType = functionBuilder.build();
    assertEquals(true, functionType.isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = Mockito.mock(FunctionType.class);
    String name = "functionName";
    Node sourceNode = Mockito.mock(Node.class);
    Node parametersNode = Mockito.mock(Node.class);
    JSType returnType = Mockito.mock(JSType.class);
    ObjectType typeOfThis = Mockito.mock(ObjectType.class);
    String templateTypeName = "template";
    boolean isConstructor = true;
    boolean isNativeType = true;

    Mockito.when(otherType.getReferenceName()).thenReturn(name);
    Mockito.when(otherType.getSource()).thenReturn(sourceNode);
    Mockito.when(otherType.getParametersNode()).thenReturn(parametersNode);
    Mockito.when(otherType.getReturnType()).thenReturn(returnType);
    Mockito.when(otherType.getTypeOfThis()).thenReturn(typeOfThis);
    Mockito.when(otherType.getTemplateTypeName()).thenReturn(templateTypeName);
    Mockito.when(otherType.isConstructor()).thenReturn(isConstructor);
    Mockito.when(otherType.isNativeObjectType()).thenReturn(isNativeType);

    functionBuilder.copyFromOtherFunction(otherType);
    FunctionType functionType = functionBuilder.build();

    assertEquals(name, functionType.getReferenceName());
    assertEquals(sourceNode, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertEquals(isConstructor, functionType.isConstructor());
    assertEquals(isNativeType, functionType.isNativeObjectType());
  }
}