import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilderWithNameAndReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testFunction";
    JSType returnType = new IntType(registry);

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withReturnType(returnType);

    FunctionType functionType = function.build();
    assertNotNull(functionType);
    assertTrue(functionType.getName().equals(name));
    assertTrue(functionType.getReturnType().equals(returnType));
  }

  @Test
  public void testFunctionBuilderWithNameAndParams() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testFunction";
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    JSType paramType = new StringType(registry);

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withParams(paramBuilder.withType(paramType));

    FunctionType functionType = function.build();
    assertNotNull(functionType);
    assertTrue(functionType.getName().equals(name));
    assertTrue(functionType.getParameters().size() > 0);
  }

  @Test
  public void testFunctionBuilderWithSourceNodeAndParams() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testFunction";
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    JSType paramType = new StringType(registry);
    Node sourceNode = new ObjectLiteralNode();

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withSourceNode(sourceNode)
        .withParams(paramBuilder.withType(paramType));

    FunctionType functionType = function.build();
    assertNotNull(functionType);
    assertTrue(functionType.getName().equals(name));
    assertTrue(functionType.getParameters().size() > 0);
  }

  @Test
  public void testFunctionBuilderWithReturnTypeAndParams() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testFunction";
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    JSType paramType = new StringType(registry);
    JSType returnType = new IntType(registry);

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withReturnType(returnType)
        .withParams(paramBuilder.withType(paramType));

    FunctionType functionType = function.build();
    assertNotNull(functionType);
    assertTrue(functionType.getName().equals(name));
    assertTrue(functionType.getParameters().size() > 0);
    assertTrue(functionType.getReturnType().equals(returnType));
  }

  @Test
  public void testFunctionBuilderWithNameReturnTypeAndParamsNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testFunction";
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    JSType paramType = new StringType(registry);
    Node parameterNode = new ObjectLiteralNode();
    Node sourceNode = new ObjectLiteralNode();
    JSType returnType = new IntType(registry);

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withSourceNode(sourceNode)
        .withParamsNode(parameterNode)
        .withReturnType(returnType);

    FunctionType functionType = function.build();
    assertNotNull(functionType);
    assertTrue(functionType.getName().equals(name));
    assertTrue(functionType.getParameters().size() > 0);
    assertTrue(functionType.getReturnType().equals(returnType));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFunctionBuilderWithNullName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = null;
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    JSType paramType = new StringType(registry);
    Node sourceNode = new ObjectLiteralNode();

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withSourceNode(sourceNode)
        .withParams(paramBuilder.withType(paramType));

    FunctionType functionType = function.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFunctionBuilderWithNullReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testFunction";
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    JSType paramType = new StringType(registry);
    Node sourceNode = new ObjectLiteralNode();
    JSType returnType = null;

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withSourceNode(sourceNode)
        .withParams(paramBuilder.withType(paramType))
        .withReturnType(returnType);

    FunctionType functionType = function.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFunctionBuilderWithNullParams() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testFunction";
    Node sourceNode = new ObjectLiteralNode();
    FunctionParamBuilder paramBuilder = null;
    JSType paramType = new StringType(registry);

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withSourceNode(sourceNode)
        .withParams(paramBuilder)
        .withParamsNode(null)
        .withReturnType(new IntType(registry));

    FunctionType functionType = function.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFunctionBuilderWithNullReturnTypeInferred() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testFunction";
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    JSType paramType = new StringType(registry);
    Node sourceNode = new ObjectLiteralNode();

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withSourceNode(sourceNode)
        .withParams(paramBuilder.withType(paramType))
        .withReturnType(null)
        .withInferredReturnType(true);

    FunctionType functionType = function.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFunctionBuilderWithNullTypeOfThis() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testFunction";
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    JSType paramType = new StringType(registry);
    Node sourceNode = new ObjectLiteralNode();
    ObjectType typeOfThis = null;

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withSourceNode(sourceNode)
        .withParams(paramBuilder.withType(paramType))
        .withReturnType(new IntType(registry))
        .withTypeOfThis(typeOfThis);

    FunctionType functionType = function.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFunctionBuilderWithNullTemplateTypeName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testFunction";
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    JSType paramType = new StringType(registry);
    Node sourceNode = new ObjectLiteralNode();
    FunctionType functionType = null;

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withSourceNode(sourceNode)
        .withParams(paramBuilder.withType(paramType))
        .withReturnType(new IntType(registry))
        .withTypeOfThis(new ObjectType(registry, "Object"))
        .withTemplateName(functionType);

    FunctionType functionTypeResult = function.build();
  }

  @Test
  public void testFunctionBuilderCopyFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String name = "testFunction";
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    JSType paramType = new StringType(registry);
    Node sourceNode = new ObjectLiteralNode();
    JSType returnType = new IntType(registry);
    ObjectType typeOfThis = new ObjectType(registry, "Object");
    FunctionType functionType = new FunctionType(registry, name, sourceNode,
        new ArrowType(registry, new ObjectArrayType(registry, paramType), returnType, false),
        typeOfThis, null, false, false);

    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionBuilder copyFunctionBuilder = functionBuilder.copyFromOtherFunction(functionType);

    FunctionType copyFunctionType = copyFunctionBuilder.build();

    assertNotNull(copyFunctionType);
    assertTrue(copyFunctionType.getName().equals(name));
    assertTrue(copyFunctionType.getSource().equals(sourceNode));
    assertTrue(copyFunctionType.getParameters().size() > 0);
    assertTrue(copyFunctionType.getReturnType().equals(returnType));
    assertTrue(copyFunctionType.getTypeOfThis().equals(typeOfThis));
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testConstructor";
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    JSType paramType = new StringType(registry);
    Node sourceNode = new ObjectLiteralNode();

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withSourceNode(sourceNode)
        .withParams(paramBuilder.withType(paramType))
        .forConstructor();

    FunctionType functionType = function.build();
    assertTrue(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testNativeFunction";
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    JSType paramType = new StringType(registry);
    Node sourceNode = new ObjectLiteralNode();

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withSourceNode(sourceNode)
        .withParams(paramBuilder.withType(paramType))
        .forNativeType();

    FunctionType functionType = function.build();
    assertTrue(functionType.isNativeObjectType());
  }
}