import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.NodeFactory.*;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.node.ArrayLiteral;
import com.google.javascript.rhino.node.CallExp;
import com.google.javascript.rhino.node.Identifier;
import com.google.javascript.rhino.node.ObjectLiteral;
import com.google.javascript.rhino.node.StringLiteral;

public class FunctionBuilderTest {

  private JSTypeRegistry registry;

  @org.junit.Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @org.junit.Test
  public void testBasicFunctionBuilder() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "add";
    Node sourceNode = newSource("function add(x, y) { return x + y; }");
    FunctionParamBuilder params = new FunctionParamBuilder(registry);
    params.withName("x").withType(Int());
    params.withName("y").withType(Int());
    Node parametersNode = params.build();
    JSType returnType = Int();
    FunctionType functionType = functionBuilder
        .withName(name)
        .withSourceNode(sourceNode)
        .withParams(params)
        .withReturnType(returnType)
        .build();

    FunctionType expectedFunctionType = new FunctionType(registry, name, sourceNode,
        new ArrowType(registry, parametersNode, returnType, false), null, null, false, false);

    assertEquals(expectedFunctionType, functionType);
  }

  @org.junit.Test
  public void testFunctionBuilderWithInferredReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "add";
    Node sourceNode = newSource("function add(x, y) { }");
    FunctionParamBuilder params = new FunctionParamBuilder(registry);
    params.withName("x").withType(Int());
    params.withName("y").withType(Int());
    Node parametersNode = params.build();
    FunctionType functionType = functionBuilder
        .withName(name)
        .withSourceNode(sourceNode)
        .withParams(params)
        .withInferredReturnType(Void())
        .build();

    FunctionType expectedFunctionType = new FunctionType(registry, name, sourceNode,
        new ArrowType(registry, parametersNode, Void(), true), null, null, false, false);

    assertEquals(expectedFunctionType, functionType);
  }

  @org.junit.Test
  public void testFunctionBuilderWithConstructor() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "Person";
    Node sourceNode = newSource("function Person(name) { this.name = name; }");
    FunctionParamBuilder params = new FunctionParamBuilder(registry);
    params.withName("name").withType(String());
    Node parametersNode = params.build();
    FunctionType constructorType = functionBuilder
        .withName(name)
        .forConstructor()
        .withParamsNode(parametersNode)
        .build();

    FunctionType expectedConstructorType = new FunctionType(registry, name, null,
        new ConstructorType(registry, parametersNode, null), null, null, true, false);

    assertEquals(expectedConstructorType, constructorType);
  }

  @org.junit.Test
  public void testFunctionBuilderWithNativeType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "document.write";
    Node sourceNode = newSource("function document.write(string) { }");
    FunctionParamBuilder params = new FunctionParamBuilder(registry);
    params.withName("string").withType(String());
    Node parametersNode = params.build();
    FunctionType functionType = functionBuilder
        .withName(name)
        .withSourceNode(sourceNode)
        .withParams(params)
        .forNativeType()
        .build();

    FunctionType expectedFunctionType = new FunctionType(registry, name, null,
        new NativeFunctionType(registry, parametersNode, null), null, name, true, true);

    assertEquals(expectedFunctionType, functionType);
  }

  @org.junit.Test
  public void testFunctionBuilderCopy() {
    FunctionType originalFunctionType = getFunctionType("add");
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionType copiedFunctionType = functionBuilder.copyFromOtherFunction(originalFunctionType);

    assertEquals(originalFunctionType, copiedFunctionType);
  }

  private FunctionType getFunctionType(String name) {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String source = "function " + name + "(x, y) { return x + y; }";
    Node sourceNode = newSource(source);
    FunctionParamBuilder params = new FunctionParamBuilder(registry);
    params.withName("x").withType(Int());
    params.withName("y").withType(Int());
    Node parametersNode = params.build();
    JSType returnType = Int();
    FunctionType functionType = functionBuilder
        .withName(name)
        .withSourceNode(sourceNode)
        .withParams(params)
        .withReturnType(returnType)
        .build();

    return functionType;
  }
}