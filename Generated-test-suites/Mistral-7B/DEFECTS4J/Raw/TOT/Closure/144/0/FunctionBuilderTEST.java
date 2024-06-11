import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createStringLiteral;
import static com.google.javascript.rhino.NodeFactory.createList;
import static com.google.javascript.rhino.NodeFactory.createObjectLiteral;
import static com.google.javascript.rhino.NodeFactory.createBoolean;
import static com.google.javascript.rhino.NodeFactory.createNull;
import static com.google.javascript.rhino.NodeFactory.createNumber;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.NodeFactory.createCall;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectLiteral;
import com.google.javascript.rhino.StringLiteral;
import com.google.javascript.rhino.testing.JSTestCase;
import com.google.javascript.rhino.testing.PolymerTestRunner;
import com.google.javascript.rhino.testing.Runtime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(PolymerTestRunner.class)
public class FunctionBuilderTest extends JSTestCase {

  private static final String FUNCTION_NAME = "myFunction";
  private static final String PARAMETER_NAME = "param";
  private static final String INTERFACE_NAME = "MyInterface";
  private static final String TEMPLATE_TYPE_NAME = "MyFunctionTemplate";
  private FunctionBuilder functionBuilder;
  private FunctionType functionType;

  @Before
  public void setUp() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeFactory typeFactory = registry.getTypeFactory();
    functionBuilder = new FunctionBuilder(registry);

    // Test with no return type
    functionType = functionBuilder
        .withName(FUNCTION_NAME)
        .withParams(new FunctionParamBuilder().withName(PARAMETER_NAME).build())
        .build();

    // Test with one return type
    functionType = functionBuilder
        .withName(FUNCTION_NAME)
        .withParams(new FunctionParamBuilder().withName(PARAMETER_NAME).build())
        .withReturnType(stringType)
        .build();

    // Test with array return type
    Node arrayTypeNode = createObjectLiteral(createList(numberType), "Array");
    functionType = functionBuilder
        .withName(FUNCTION_NAME)
        .withParams(new FunctionParamBuilder().withName(PARAMETER_NAME).build())
        .withReturnType(arrayTypeNode)
        .build();

    // Test with string and array return types
    ObjectLiteral stringObject = createObjectLiteral(createStringLiteral("String"), "String");
    Node arrayTypeNodeWithString = createObjectLiteral(createList(stringObject, arrayTypeNode), "Array");
    functionType = functionBuilder
        .withName(FUNCTION_NAME)
        .withParams(new FunctionParamBuilder().withName(PARAMETER_NAME).build())
        .withReturnType(arrayTypeNodeWithString)
        .build();

    // Test with a custom interface return type
    functionType = functionBuilder
        .withName(FUNCTION_NAME)
        .withParams(new FunctionParamBuilder().withName(PARAMETER_NAME).build())
        .withReturnType(registry.createInterfaceType(INTERFACE_NAME))
        .build();

    // Test with a template type name
    functionType = functionBuilder
        .withName(FUNCTION_NAME)
        .withParams(new FunctionParamBuilder().withName(PARAMETER_NAME).build())
        .withTemplateName(TEMPLATE_TYPE_NAME)
        .build();

    // Test with a constructor
    functionType = functionBuilder
        .withName(FUNCTION_NAME)
        .withParams(new FunctionParamBuilder().withName(PARAMETER_NAME).build())
        .forConstructor()
        .build();

    // Test with a native type
    functionType = functionBuilder
        .withName(FUNCTION_NAME)
        .withParams(new FunctionParamBuilder().withName(PARAMETER_NAME).build())
        .forNativeType()
        .build();
  }

  @Test
  public void testFunctionType() {
    assertEquals(FUNCTION_NAME, functionType.getName());
    assertNotNull(functionType.getSource());
    assertEquals(functionType.getArity(), 1);
    assertEquals(functionType.getParameterTypes().size(), 1);
    assertEquals(functionType.getReturnType(), functionType.getParameterTypes().get(0));
    assertEquals(functionType.isConstructor(), false);
    assertEquals(functionType.isNativeType(), false);

    Node functionCallNode = createCall(functionType.getCallable(), createList(createThis(), createStringLiteral("param")));
    Node expectedResultNode = createStringLiteral("result");
    Node expectedFunctionCallNode = createCall(createFunction(stringType, createList(createIdentifier(FUNCTION_NAME), createList(createIdentifier(PARAMETER_NAME), createStringLiteral("param"))), createObjectLiteral()), createList(createThis(), createStringLiteral("param"), createStringLiteral("param")));
    assertEquals(expectedFunctionCallNode, functionCallNode);

    Object result = runtime.evaluate(functionCallNode, createObjectLiteral(), "global");
    assertEquals(expectedResultNode.getValue(), result.toString());
  }

  // Add tests for other methods and edge cases here.
}