import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.NodeFactory.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {
  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testFunctionBuilderWithNameAndReturnType() {
    FunctionParamBuilder params = new FunctionParamBuilder();
    params.withName("param").withType(String.class);

    JSType expectedFunctionType = new FunctionType(registry, "testFunction", null,
        new ArrowType(registry, new NodeFactory().createList(params.build()),
            String.class), null, null, false, false);

    functionBuilder.withName("testFunction")
        .withParams(params)
        .withReturnType(String.class)
        .build();

    FunctionType actualFunctionType = functionBuilder.build();

    assertEquals(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testFunctionBuilderWithNameAndInferredReturnType() {
    FunctionParamBuilder params = new FunctionParamBuilder();
    params.withName("param").withType(String.class);

    functionBuilder.withName("testFunction")
        .withParams(params)
        .withInferredReturnType(Number.class)
        .build();

    FunctionType actualFunctionType = functionBuilder.build();

    assertNotNull(actualFunctionType.getReturnType());
    assertEquals(Number.class, actualFunctionType.getReturnType().getType());
  }

  @Test
  public void testFunctionBuilderWithTemplateName() {
    FunctionParamBuilder params = new FunctionParamBuilder();
    params.withName("param").withType(String.class);

    functionBuilder.withName("testFunction<T>")
        .withTemplateName("FunctionType")
        .withParams(params)
        .withReturnType(Object.class)
        .build();

    FunctionType actualFunctionType = functionBuilder.build();

    assertEquals("FunctionType<T>", actualFunctionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    FunctionParamBuilder params = new FunctionParamBuilder();
    params.withName("param").withType(String.class);

    functionBuilder.forConstructor()
        .withParams(params)
        .withReturnType(String.class)
        .build();

    FunctionType actualFunctionType = functionBuilder.build();

    assertTrue(actualFunctionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    functionBuilder.forNativeType();

    FunctionType actualFunctionType = functionBuilder.build();

    assertTrue(actualFunctionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    String name = "testFunction";
    String templateTypeName = "FunctionType";
    Node sourceNode = createStringLiteral("source");
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withName("param").withType(String.class);
    Node parametersNode = paramBuilder.build();
    JSType returnType = String.class;
    ObjectType typeOfThis = Object.TYPE;

    FunctionType otherFunctionType = new FunctionType(registry, name, sourceNode,
        new ArrowType(registry, parametersNode, returnType, false), typeOfThis,
        templateTypeName, true, false);

    functionBuilder.copyFromOtherFunction(otherFunctionType);

    FunctionType actualFunctionType = functionBuilder.build();

    assertEquals(name, actualFunctionType.getReferenceName());
    assertEquals(sourceNode, actualFunctionType.getSource());
    assertEquals(parametersNode, actualFunctionType.getParametersNode());
    assertEquals(returnType, actualFunctionType.getReturnType());
    assertEquals(typeOfThis, actualFunctionType.getTypeOfThis());
    assertEquals(templateTypeName, actualFunctionType.getTemplateTypeName());
    assertTrue(actualFunctionType.isConstructor());
    assertFalse(actualFunctionType.isNativeObjectType());
  }
}