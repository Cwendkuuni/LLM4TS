import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {
  private FunctionBuilder builder;
  private Node sourceNode;
  private Node parametersNode;
  private FunctionParamBuilder paramBuilder;
  private ObjectType typeOfThis;

  @Before
  public void setUp() {
    sourceNode = mock(Node.class);
    parametersNode = mock(Node.class);
    typeOfThis = mock(ObjectType.class);
    paramBuilder = new FunctionParamBuilder(null);

    builder = new FunctionBuilder(null) //
        .withName("testFunction") //
        .withSourceNode(sourceNode) //
        .withParams(paramBuilder) //
        .withParamsNode(parametersNode) //
        .withReturnType(JSType.VOID) //
        .withTypeOfThis(typeOfThis) //
        .build();
  }

  @Test
  public void testFunctionBuilderWithName() {
    String expectedName = "testFunction";
    FunctionBuilder functionBuilder = new FunctionBuilder(null) //
        .withName(expectedName) //
        .build();
    FunctionType functionType = functionBuilder.build();
    assertEquals(expectedName, functionType.getReferenceName());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null) //
        .withSourceNode(sourceNode) //
        .build();
    FunctionType functionType = functionBuilder.build();
    assertEquals(sourceNode, functionType.getSource());
  }

  @Test
  public void testFunctionBuilderWithParams() {
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(null) //
        .withName("param1") //
        .withType(JSType.STRING) //
        .build();

    FunctionBuilder functionBuilder = new FunctionBuilder(null) //
        .withName("testFunction") //
        .withSourceNode(sourceNode) //
        .withParams(paramBuilder) //
        .build();
    FunctionType functionType = functionBuilder.build();
    assertEquals(sourceNode, functionType.getSource());
    assertSame(paramBuilder.build(), functionType.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithParamsNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null) //
        .withName("testFunction") //
        .withSourceNode(sourceNode) //
        .withParamsNode(parametersNode) //
        .build();
    FunctionType functionType = functionBuilder.build();
    assertEquals(sourceNode, functionType.getSource());
    assertSame(parametersNode, functionType.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithReturnType() {
    JSType returnType = JSType.NUMBER;
    FunctionBuilder functionBuilder = new FunctionBuilder(null) //
        .withName("testFunction") //
        .withSourceNode(sourceNode) //
        .withParamsNode(parametersNode) //
        .withReturnType(returnType) //
        .build();
    FunctionType functionType = functionBuilder.build();
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSType returnType = JSType.STRING;
    FunctionBuilder functionBuilder = new FunctionBuilder(null) //
        .withName("testFunction") //
        .withSourceNode(sourceNode) //
        .withParamsNode(parametersNode) //
        .withReturnType(returnType) //
        .withInferredReturnType(returnType) //
        .build();
    FunctionType functionType = functionBuilder.build();
    assertEquals(returnType, functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null) //
        .withName("testFunction") //
        .withSourceNode(sourceNode) //
        .withParamsNode(parametersNode) //
        .withReturnType(JSType.VOID) //
        .withTypeOfThis(typeOfThis) //
        .build();
    FunctionType functionType = functionBuilder.build();
    assertSame(typeOfThis, functionType.getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null) //
        .withName("testFunction") //
        .withSourceNode(sourceNode) //
        .withParamsNode(parametersNode) //
        .forConstructor() //
        .build();
    FunctionType functionType = functionBuilder.build();
    assertTrue(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(null) //
        .withName("testFunction") //
        .withSourceNode(sourceNode) //
        .withParamsNode(parametersNode) //
        .forNativeType() //
        .build();
    FunctionType functionType = functionBuilder.build();
    assertTrue(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    String name = "testFunction";
    Node source = mock(Node.class);
    Node parameters = mock(Node.class);
    JSType returnType = JSType.NUMBER;
    ObjectType typeOfThis = mock(ObjectType.class);
    FunctionType otherFunctionType = new FunctionType(null, name, source,
        new ArrowType(null, parameters, returnType, false), typeOfThis, null,
        false, false);

    FunctionBuilder functionBuilder = new FunctionBuilder(null) //
        .copyFromOtherFunction(otherFunctionType);
    FunctionType functionType = functionBuilder.build();
    assertEquals(name, functionType.getReferenceName());
    assertEquals(source, functionType.getSource());
    assertSame(parameters, functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
    assertSame(typeOfThis, functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }
}