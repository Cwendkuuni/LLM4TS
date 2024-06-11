import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.newIdent;
import static com.google.javascript.rhino.NodeFactory.newList;
import static com.google.javascript.rhino.NodeFactory.newObject;
import static com.google.javascript.rhino.NodeFactory.newString;
import static com.google.javascript.rhino.NodeFactory.newThis;
import static com.google.javascript.rhino.NodeFactory.newVoid;
import static com.google.javascript.rhino.JSType.JSTYPE_ANY;
import static com.google.javascript.rhino.JSType.JSTYPE_BOOLEAN;
import static com.google.javascript.rhino.JSType.JSTYPE_INT;
import static com.google.javascript.rhino.JSType.JSTYPE_OBJECT;
import static com.google.javascript.rhino.JSType.JSTYPE_STRING;
import static com.google.javascript.rhino.JSType.JSTYPE_VOID;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.FunctionParamBuilder;
import com.google.javascript.rhino.FunctionBuilder;

public class FunctionBuilderTest {
  private JSTypeRegistry registry;
  private FunctionBuilder builder;
  private Node sourceNode;
  private Node parametersNode;
  private FunctionType functionType;

  @Before
  public void setUp() {
    registry = JSTypeFactory.getSingleton();
    builder = new FunctionBuilder(registry);
    sourceNode = newIdent("functionName");
    parametersNode = newList(newIdent("p1"), newIdent("p2"));
  }

  @Test
  public void testBasicFunctionType() {
    builder.withName("functionName")
        .withSourceNode(sourceNode)
        .withParams(new FunctionParamBuilder().withName("p1").build())
        .withParams(new FunctionParamBuilder().withName("p2").build())
        .withReturnType(JSTYPE_INT)
        .build();

    functionType = builder.build();

    JSType expectedType = new ArrowType(registry, parametersNode, JSTYPE_INT, false);
    assertEquals(expectedType, functionType.getReturnType());
  }

  @Test
  public void testFunctionTypeWithVoidReturnType() {
    builder.withName("functionName")
        .withSourceNode(sourceNode)
        .withParams(new FunctionParamBuilder().withName("p1").build())
        .withParams(new FunctionParamBuilder().withName("p2").build())
        .withReturnType(JSTYPE_VOID)
        .build();

    functionType = builder.build();

    JSType expectedType = new ArrowType(registry, parametersNode, JSTYPE_VOID, false);
    assertEquals(expectedType, functionType.getReturnType());
  }

  @Test
  public void testFunctionTypeWithInferredReturnType() {
    builder.withName("functionName")
        .withSourceNode(sourceNode)
        .withParams(new FunctionParamBuilder().withName("p1").build())
        .withParams(new FunctionParamBuilder().withName("p2").build())
        .withReturnType(null)
        .withInferredReturnType(JSTYPE_STRING)
        .build();

    functionType = builder.build();

    JSType expectedType = new ArrowType(registry, parametersNode, JSTYPE_STRING, true);
    assertEquals(expectedType, functionType.getReturnType());
  }

  @Test
  public void testFunctionTypeWithTemplateTypeName() {
    builder.withName("functionName")
        .withSourceNode(sourceNode)
        .withParams(new FunctionParamBuilder().withName("p1").build())
        .withParams(new FunctionParamBuilder().withName("p2").build())
        .withReturnType(JSTYPE_ANY)
        .withTemplateName("MyTemplateType")
        .build();

    functionType = builder.build();

    String expectedTemplateTypeName = "MyTemplateType";
    assertEquals(expectedTemplateTypeName, functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionTypeWithTypeOfThis() {
    ObjectType objectType = new ObjectType(registry, "MyObjectType");
    builder.withName("functionName")
        .withSourceNode(sourceNode)
        .withParams(new FunctionParamBuilder().withName("p1").build())
        .withParams(new FunctionParamBuilder().withName("p2").build())
        .withReturnType(JSTYPE_VOID)
        .withTypeOfThis(objectType)
        .build();

    functionType = builder.build();

    ObjectType expectedTypeOfThis = objectType;
    assertEquals(expectedTypeOfThis, functionType.getTypeOfThis());
  }

  @Test
  public void testConstructorFunctionType() {
    builder.withName("MyConstructorFunctionName")
        .forConstructor()
        .withParams(new FunctionParamBuilder().withName("p1").build())
        .withParams(new FunctionParamBuilder().withName("p2").build())
        .withReturnType(JSTYPE_OBJECT)
        .build();

    functionType = builder.build();

    assertTrue(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testNativeFunctionType() {
    builder.withName("myNativeFunction")
        .forNativeType()
        .withParams(new FunctionParamBuilder().withName("p1").build())
        .withParams(new FunctionParamBuilder().withName("p2").build())
        .withReturnType(JSTYPE_INT)
        .build();

    functionType = builder.build();

    assertTrue(functionType.isNativeObjectType());
  }

  @Test
  public void testCopyFunctionType() {
    FunctionType originalFunctionType = createFunctionType();
    builder.copyFromOtherFunction(originalFunctionType);

    FunctionType copiedFunctionType = builder.build();

    assertEquals(originalFunctionType.getReferenceName(), copiedFunctionType.getReferenceName());
    assertEquals(originalFunctionType.getSource(), copiedFunctionType.getSource());
    assertEquals(originalFunctionType.getParametersNode(), copiedFunctionType.getParametersNode());
    assertEquals(originalFunctionType.getReturnType(), copiedFunctionType.getReturnType());
    assertEquals(originalFunctionType.getTypeOfThis(), copiedFunctionType.getTypeOfThis());
    assertEquals(originalFunctionType.getTemplateTypeName(), copiedFunctionType.getTemplateTypeName());
    assertEquals(originalFunctionType.isConstructor(), copiedFunctionType.isConstructor());
    assertEquals(originalFunctionType.isNativeObjectType(), copiedFunctionType.isNativeObjectType());
  }

  private FunctionType createFunctionType() {
    FunctionType functionType = new FunctionType(registry, null, sourceNode,
        new ArrowType(registry, parametersNode, JSTYPE_INT, false),
        newObject(), null, false, false);

    functionType.setReferenceName("functionName");
    functionType.setSource(sourceNode);
    functionType.setParametersNode(parametersNode);
    functionType.setReturnType(JSTYPE_INT);
    functionType.setTypeOfThis(newObject());

    return functionType;
  }
}