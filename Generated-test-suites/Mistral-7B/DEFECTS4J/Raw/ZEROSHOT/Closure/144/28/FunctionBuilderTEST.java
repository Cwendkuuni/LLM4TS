import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArray;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createObject;
import static com.google.javascript.rhino.NodeFactory.createStringLiteral;
import static com.google.javascript.rhino.JSTypeFactory.booleanType;
import static com.google.javascript.rhino.JSTypeFactory.functionType;
import static com.google.javascript.rhino.JSTypeFactory.integerType;
import static com.google.javascript.rhino.JSTypeFactory.objectType;
import static com.google.javascript.rhino.JSTypeFactory.stringType;
import static com.google.javascript.rhino.JSTypeFactory.voidType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeRegistryImpl;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

@RunWith(JUnit4.class)
public class FunctionBuilderTest {

  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;
  private FunctionParamBuilder paramBuilder;
  private FunctionType functionType;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    registry = new JSTypeRegistryImpl();
    functionBuilder = new FunctionBuilder(registry);
    paramBuilder = new FunctionParamBuilder(registry);
  }

  @Test
  public void testBuildNormalFunction() {
    Node paramsNode = createObject(createArray(createIdentifier("param1"), createIdentifier("param2")));
    functionBuilder
        .withName("testFunction")
        .withSourceNode(NodeFactory.createScriptletNode(createStringLiteral("function testFunction(param1, param2) { return 1; }")))
        .withParamsNode(paramsNode)
        .withReturnType(integerType())
        .build();

    functionType = functionBuilder.build();

    JSType expectedFunctionType = functionType(createIdentifier("testFunction"), paramsNode, integerType());
    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testBuildFunctionWithInferredReturnType() {
    Node paramsNode = createObject(createArray(createIdentifier("param1"), createIdentifier("param2")));
    functionBuilder
        .withName("testFunction")
        .withSourceNode(NodeFactory.createScriptletNode(createStringLiteral("function testFunction(param1, param2) { }")))
        .withParamsNode(paramsNode)
        .withInferredReturnType(voidType())
        .build();

    functionType = functionBuilder.build();

    JSType expectedFunctionType = functionType(createIdentifier("testFunction"), paramsNode, voidType());
    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testBuildFunctionWithReturnTypeAndInferredReturnType() {
    Node paramsNode = createObject(createArray(createIdentifier("param1"), createIdentifier("param2")));
    functionBuilder
        .withName("testFunction")
        .withSourceNode(NodeFactory.createScriptletNode(createStringLiteral("function testFunction(param1, param2) { return 1; }")))
        .withParamsNode(paramsNode)
        .withReturnType(integerType())
        .withInferredReturnType(booleanType())
        .build();

    functionType = functionBuilder.build();

    JSType expectedFunctionType = functionType(createIdentifier("testFunction"), paramsNode, integerType());
    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testBuildFunctionWithTemplateTypeName() {
    Node paramsNode = createObject(createArray(createIdentifier("param1"), createIdentifier("param2")));
    functionBuilder
        .withName("testFunction")
        .withSourceNode(NodeFactory.createScriptletNode(createStringLiteral("function testFunction(param1, param2) { }")))
        .withParamsNode(paramsNode)
        .withTemplateName("FunctionTemplateName")
        .build();

    functionType = functionBuilder.build();

    JSType expectedFunctionType = functionType(createIdentifier("testFunction"), paramsNode, objectType().getInterface("FunctionTemplateName"));
    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testBuildFunctionWithTypeOfThis() {
    Node paramsNode = createObject(createArray(createIdentifier("param1")));
    functionBuilder
        .withName("testFunction")
        .withSourceNode(NodeFactory.createScriptletNode(createStringLiteral("function testFunction(param1) { this.foo = 1; }")))
        .withParamsNode(paramsNode)
        .withReturnType(voidType())
        .withTypeOfThis(objectType().getPrototype("Object"))
        .build();

    functionType = functionBuilder.build();

    JSType expectedFunctionType = functionType(createIdentifier("testFunction"), paramsNode, voidType(), objectType().getPrototype("Object"));
    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testBuildConstructor() {
    Node paramsNode = createObject(createArray(createIdentifier("param1")));
    functionBuilder
        .withName("testConstructor")
        .forConstructor()
        .withParamsNode(paramsNode)
        .build();

    JSType expectedFunctionType = functionType(createIdentifier("testConstructor"), paramsNode, voidType(), null, true);
    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testBuildNativeType() {
    functionBuilder.forNativeType();
    functionType = functionBuilder.build();

    JSType expectedFunctionType = functionType(voidType(), null, null, null, true);
    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testCopyFunctionType() {
    Node paramsNode = createObject(createArray(createIdentifier("param1"), createIdentifier("param2")));
    functionType = functionType(createIdentifier("function1"), paramsNode, integerType());

    functionBuilder.copyFromOtherFunction(functionType);

    JSType expectedFunctionType = functionType(createIdentifier("function1"), paramsNode, integerType());
    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testBuildFunctionWithEmptyParams() {
    functionBuilder
        .withName("testFunction")
        .withSourceNode(NodeFactory.createScriptletNode(createStringLiteral("function testFunction() { }")))
        .withReturnType(voidType())
        .build();

    JSType expectedFunctionType = functionType(createIdentifier("testFunction"), NodeFactory.createEmptyObject(), voidType());
    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testBuildFunctionWithOneParam() {
    Node paramsNode = createIdentifier("param1");
    functionBuilder
        .withName("testFunction")
        .withSourceNode(NodeFactory.createScriptletNode(createStringLiteral("function testFunction(param1) { }")))
        .withReturnType(voidType())
        .withParamsNode(paramsNode)
        .build();

    JSType expectedFunctionType = functionType(createIdentifier("testFunction"), paramsNode, voidType());
    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testBuildFunctionWithBadSourceCode() {
    functionBuilder
        .withName("testFunction")
        .withSourceNode(NodeFactory.createScriptletNode(createStringLiteral("function testFunction() { return 1; }")))
        .withReturnType(integerType())
        .withParamsNode(NodeFactory.createArray(createIdentifier("param1")))
        .build();

    try {
      functionType = functionBuilder.build();
      fail("Expected a JSTypeBuilderException.");
    } catch (Exception e) {
      assertTrue(e instanceof JSTypeBuilderException);
    }
  }

  @Test
  public void testBuildFunctionWithNullParamsNode() {
    functionBuilder
        .withName("testFunction")
        .withSourceNode(NodeFactory.createScriptletNode(createStringLiteral("function testFunction(param1) { }")))
        .withReturnType(integerType())
        .build();

    try {
      functionBuilder.withParamsNode(null);
      functionType = functionBuilder.build();
      fail("Expected a NullPointerException.");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void testBuildFunctionWithParamsNodeThatIsNotAnObject() {
    functionBuilder
        .withName("testFunction")
        .withSourceNode(NodeFactory.createScriptletNode(createStringLiteral("function testFunction(param1) { }")))
        .withReturnType(integerType())
        .withParamsNode(integerType())
        .build();

    try {
      functionType = functionBuilder.build();
      fail("Expected a JSTypeBuilderException.");
    } catch (Exception e) {
      assertTrue(e instanceof JSTypeBuilderException);
    }
  }

  @Test
  public void testBuildFunctionWithParamsNodeThatHasDuplicateParams() {
    Node paramsNode = createObject(createArray(createIdentifier("param1"), createIdentifier("param1")));
    functionBuilder
        .withName("testFunction")
        .withSourceNode(NodeFactory.createScriptletNode(createStringLiteral("function testFunction(param1) { }")))
        .withReturnType(integerType())
        .withParamsNode(paramsNode)
        .build();

    try {
      functionType = functionBuilder.build();
      fail("Expected a JSTypeBuilderException.");
    } catch (Exception e) {
      assertTrue(e instanceof JSTypeBuilderException);
    }
  }

  @Test
  public void testBuildFunctionWithParamsNodeThatHasMixedTypes() {
    Node paramsNode = createObject(createArray(createIdentifier("param1"), integerType()));
    functionBuilder
        .withName("testFunction")
        .withSourceNode(NodeFactory.createScriptletNode(createStringLiteral("function testFunction(param1) { }")))
        .withReturnType(integerType())
        .withParamsNode(paramsNode)
        .build();

    try {
      functionType = functionBuilder.build();
      fail("Expected a JSTypeBuilderException.");
    } catch (Exception e) {
      assertTrue(e instanceof JSTypeBuilderException);
    }
  }
}