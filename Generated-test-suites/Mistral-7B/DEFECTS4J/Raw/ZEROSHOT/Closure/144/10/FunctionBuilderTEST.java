import static org.junit.Assert.*;
import static com.google.javascript.rhino.node.EmptyNode.EMPTY_NODE;
import static com.google.javascript.rhino.node.NullLiteral.NULL_LITERAL;
import static com.google.javascript.rhino.types.JSType.ARRAY;
import static com.google.javascript.rhino.types.JSType.BOOLEAN;
import static com.google.javascript.rhino.types.JSType.INT;
import static com.google.javascript.rhino.types.JSType.OBJECT;
import static com.google.javascript.rhino.types.JSType.STRING;
import static com.google.javascript.rhino.types.JSType.VOID;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectFactory;
import com.google.javascript.rhino.types.ArrowType;
import com.google.javascript.rhino.types.FunctionType;
import com.google.javascript.rhino.types.ObjectType;
import com.google.javascript.rhino.types.Type;

import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {
  private FunctionBuilder functionBuilder;
  private JSTypeRegistry registry;
  private ObjectFactory objectFactory;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    objectFactory = new ObjectFactory(registry);
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testBuildFunctionWithNameAndReturnType() {
    FunctionType expectedFunction = new FunctionType(registry, "functionName", EMPTY_NODE,
        new ArrowType(registry, EMPTY_NODE, BOOLEAN, false), OBJECT, null, false, false);

    functionBuilder.withName("functionName");
    functionBuilder.withReturnType(BOOLEAN);

    FunctionType actualFunction = functionBuilder.build();

    assertEquals(expectedFunction, actualFunction);
  }

  @Test
  public void testBuildFunctionWithParamsNode() {
    Node paramsNode = objectFactory.createList(
        objectFactory.createListItem(objectFactory.createName("param1")),
        objectFactory.createListItem(objectFactory.createName("param2")));

    FunctionType expectedFunction = new FunctionType(registry, "functionName", EMPTY_NODE,
        new ArrowType(registry, paramsNode, INT, false), OBJECT, null, false, false);

    functionBuilder.withName("functionName");
    functionBuilder.withParamsNode(paramsNode);
    functionBuilder.withReturnType(INT);

    FunctionType actualFunction = functionBuilder.build();

    assertEquals(expectedFunction, actualFunction);
  }

  @Test
  public void testBuildFunctionWithInferredReturnType() {
    FunctionType expectedFunction = new FunctionType(registry, "functionName", EMPTY_NODE,
        new ArrowType(registry, EMPTY_NODE, VOID, true), OBJECT, null, false, false);

    functionBuilder.withName("functionName");
    functionBuilder.withReturnType(NULL_LITERAL);
    functionBuilder.withInferredReturnType(VOID);

    FunctionType actualFunction = functionBuilder.build();

    assertEquals(expectedFunction, actualFunction);
  }

  @Test
  public void testBuildFunctionWithTypeOfThis() {
    ObjectType expectedThisType = new ObjectType(registry, "ObjectWithThis");

    FunctionType expectedFunction = new FunctionType(registry, "functionName", EMPTY_NODE,
        new ArrowType(registry, EMPTY_NODE, INT, false), expectedThisType, null, false, false);

    functionBuilder.withName("functionName");
    functionBuilder.withReturnType(INT);
    functionBuilder.withTypeOfThis(expectedThisType);

    FunctionType actualFunction = functionBuilder.build();

    assertEquals(expectedFunction, actualFunction);
  }

  @Test
  public void testBuildFunctionWithTemplateName() {
    FunctionType expectedFunction = new FunctionType(registry, "functionName", EMPTY_NODE,
        new ArrowType(registry, EMPTY_NODE, ARRAY, false), OBJECT, "TemplateName", false, false);

    functionBuilder.withName("functionName");
    functionBuilder.withTemplateName("TemplateName");

    FunctionType actualFunction = functionBuilder.build();

    assertEquals(expectedFunction, actualFunction);
  }

  @Test
  public void testBuildConstructorFunction() {
    FunctionType expectedFunction = new FunctionType(registry, "ConstructorFunction", EMPTY_NODE,
        new ArrowType(registry, EMPTY_NODE, OBJECT, false), OBJECT, null, true, false);

    functionBuilder.withName("ConstructorFunction");
    functionBuilder.forConstructor();

    FunctionType actualFunction = functionBuilder.build();

    assertEquals(expectedFunction, actualFunction);
  }

  @Test
  public void testBuildNativeFunction() {
    FunctionType expectedFunction = new FunctionType(registry, "NativeFunction", EMPTY_NODE,
        new ArrowType(registry, EMPTY_NODE, INT, false), OBJECT, null, false, true);

    functionBuilder.withName("NativeFunction");
    functionBuilder.forNativeType();

    FunctionType actualFunction = functionBuilder.build();

    assertEquals(expectedFunction, actualFunction);
  }

  @Test
  public void testBuildFunctionWithParams() {
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    paramBuilder.withName("param1").withType(STRING);
    paramBuilder.withName("param2").withType(BOOLEAN);

    Node paramsNode = paramBuilder.build();

    FunctionType expectedFunction = new FunctionType(registry, "functionName", EMPTY_NODE,
        new ArrowType(registry, paramsNode, INT, false), OBJECT, null, false, false);

    functionBuilder.withName("functionName");
    functionBuilder.withParams(paramBuilder);
    functionBuilder.withReturnType(INT);

    FunctionType actualFunction = functionBuilder.build();

    assertEquals(expectedFunction, actualFunction);
  }
}