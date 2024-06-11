import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createObject;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.jstype.JSTypeRegistry.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {
  private JSTypeRegistry registry;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    nodeFactory = NodeFactory.getInitial();
  }

  // Test for constructor
  @Test
  public void testFunctionBuilderForConstructor() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.forConstructor();
    FunctionType functionType = functionBuilder.build();
    assertTrue(functionType.isConstructor());
  }

  // Test for native type
  @Test
  public void testFunctionBuilderForNativeType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.forNativeType();
    FunctionType functionType = functionBuilder.build();
    assertTrue(functionType.isNativeObjectType());
  }

  // Test for setting name
  @Test
  public void testFunctionBuilderWithName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testFunction";
    functionBuilder.withName(name);
    FunctionType functionType = functionBuilder.build();
    assertEquals(name, functionType.getReferenceName());
  }

  // Test for setting source node
  @Test
  public void testFunctionBuilderWithSourceNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    Node sourceNode = createObject();
    functionBuilder.withSourceNode(sourceNode);
    FunctionType functionType = functionBuilder.build();
    assertEquals(sourceNode, functionType.getSource());
  }

  // Test for setting parameters
  @Test
  public void testFunctionBuilderWithParams() {
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    Node parametersNode = paramBuilder.build();
    functionBuilder.withParams(paramBuilder);
    FunctionType functionType = functionBuilder.build();
    assertEquals(parametersNode, functionType.getParametersNode());
  }

  // Test for setting parameters node
  @Test
  public void testFunctionBuilderWithParamsNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    Node parametersNode = createObject();
    functionBuilder.withParamsNode(parametersNode);
    FunctionType functionType = functionBuilder.build();
    assertEquals(parametersNode, functionType.getParametersNode());
  }

  // Test for setting return type
  @Test
  public void testFunctionBuilderWithReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    JSType returnType = BooleanType.getInstance();
    functionBuilder.withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertEquals(returnType, functionType.getReturnType());
  }

  // Test for setting inferred return type
  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    JSType inferredReturnType = VoidType.getInstance();
    functionBuilder.withInferredReturnType(inferredReturnType);
    FunctionType functionType = functionBuilder.build();
    assertEquals(inferredReturnType, functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
  }

  // Test for setting "this" type
  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    ObjectType typeOfThis = new ObjectType(registry, "TestObject");
    functionBuilder.withTypeOfThis(typeOfThis);
    FunctionType functionType = functionBuilder.build();
    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }

  // Test for setting template name
  @Test
  public void testFunctionBuilderWithTemplateName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String templateTypeName = "TestTemplate";
    functionBuilder.withTemplateName(templateTypeName);
    FunctionType functionType = functionBuilder.build();
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
  }

  // Test for copying another function type
  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    String referenceName = "testFunction";
    Node sourceNode = createObject();
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    Node parametersNode = paramBuilder.build();
    JSType returnType = BooleanType.getInstance();
    ObjectType typeOfThis = new ObjectType(registry, "TestObject");
    String templateTypeName = "TestTemplate";
    FunctionType otherFunctionType = new FunctionType(registry, referenceName, sourceNode,
        new ArrowType(registry, parametersNode, returnType, false), typeOfThis,
        templateTypeName, true, false);

    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.copyFromOtherFunction(otherFunctionType);
    FunctionType functionType = functionBuilder.build();

    assertEquals(referenceName, functionType.getReferenceName());
    assertEquals(sourceNode, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }
}