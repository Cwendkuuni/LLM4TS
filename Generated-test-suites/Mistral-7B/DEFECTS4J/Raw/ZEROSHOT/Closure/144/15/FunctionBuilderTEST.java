import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeRegistryImpl;

public class FunctionBuilderTest {

  private FunctionBuilder functionBuilder;
  private JSTypeRegistry registry;
  private ObjectType objectType;
  private FunctionParamBuilder functionParamBuilder;

  @Before
  public void setup() {
    registry = new JSTypeRegistryImpl();
    objectType = mock(ObjectType.class);
    functionParamBuilder = mock(FunctionParamBuilder.class);
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testFunctionBuilderWithName() {
    functionBuilder.withName("testFunction");
    FunctionType functionType = functionBuilder.build();
    assertEquals("testFunction", functionType.getReferenceName());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    Node sourceNode = mock(Node.class);
    functionBuilder.withSourceNode(sourceNode);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getSource());
    assertEquals(sourceNode, functionType.getSource());
  }

  @Test
  public void testFunctionBuilderWithParams() {
    JSType paramType = mock(JSType.class);
    FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);
    when(paramBuilder.build()).thenReturn(new FunctionParamType(registry, "testParam", paramType));
    functionBuilder.withParams(paramBuilder);
    functionBuilder.withParamsNode(mock(Node.class));
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getParametersNode());
    assertEquals(1, functionType.getParameters().size());
  }

  @Test
  public void testFunctionBuilderWithReturnType() {
    JSType returnType = mock(JSType.class);
    functionBuilder.withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSType returnType = mock(JSType.class);
    functionBuilder.withReturnType(returnType);
    functionBuilder.withInferredReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(true, functionType.isInferredReturnType());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    functionBuilder.withTypeOfThis(objectType);
    FunctionType functionType = functionBuilder.build();
    assertEquals(objectType, functionType.getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderWithTemplateName() {
    functionBuilder.withTemplateName("testTemplate");
    FunctionType functionType = functionBuilder.build();
    assertEquals("testTemplate", functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    functionBuilder.forConstructor();
    FunctionType functionType = functionBuilder.build();
    assertEquals(true, functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    functionBuilder.forNativeType();
    FunctionType functionType = functionBuilder.build();
    assertEquals(true, functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    String name = "testFunction";
    Node sourceNode = mock(Node.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    String templateTypeName = "testTemplate";
    boolean isConstructor = true;
    boolean isNativeType = true;

    when(functionParamBuilder.build()).thenReturn(new FunctionParamType(registry, "testParam", returnType));
    when(functionBuilder.build()).thenReturn(new FunctionType(registry, name, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, templateTypeName, isConstructor, isNativeType));

    functionBuilder.copyFromOtherFunction(new FunctionType(registry, name, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, templateTypeName, isConstructor, isNativeType));

    FunctionType functionType = functionBuilder.build();

    assertEquals(name, functionType.getReferenceName());
    assertEquals(sourceNode, functionType.getSource());
    assertEquals(new ArrowType(registry, parametersNode, returnType, false), functionType.getReturnType());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertEquals(isConstructor, functionType.isConstructor());
    assertEquals(isNativeType, functionType.isNativeObjectType());
  }
}