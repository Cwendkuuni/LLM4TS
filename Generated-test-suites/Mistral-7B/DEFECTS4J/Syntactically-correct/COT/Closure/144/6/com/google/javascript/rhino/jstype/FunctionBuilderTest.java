import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Test;

/**
 * Test cases for FunctionBuilder class.
 */
public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilderWithBasicParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder()
        .withName("param1")
        .withType(Number.NUMBER);
    FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder()
        .withName("param2")
        .withType(String.STRING);

    Node parametersNode = functionBuilder.withParams(paramBuilder1, paramBuilder2)
        .build();

    FunctionBuilder functionBuilder1 = new FunctionBuilder(registry)
        .withName("function1")
        .withParams(parametersNode)
        .withReturnType(Void.VOID)
        .build();

    FunctionType functionType1 = functionBuilder1.build();

    FunctionType expectedFunctionType = new FunctionType(registry, "function1", null,
        new ArrowType(registry, parametersNode, Void.VOID, false), null, null,
        false, false);

    assertEquals(expectedFunctionType, functionType1);
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder()
        .withName("param1")
        .withType(Number.NUMBER);

    Node parametersNode = functionBuilder.withParams(paramBuilder1)
        .build();

    FunctionBuilder functionBuilder1 = new FunctionBuilder(registry)
        .withName("function1")
        .withParams(parametersNode)
        .withInferredReturnType(Number.NUMBER)
        .build();

    FunctionType functionType1 = functionBuilder1.build();

    FunctionType expectedFunctionType = new FunctionType(registry, "function1", null,
        new ArrowType(registry, parametersNode, Number.NUMBER, true), null, null,
        false, false);

    assertEquals(expectedFunctionType, functionType1);
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    ObjectType thisType = new ObjectType(registry, "Object");

    FunctionBuilder functionBuilder1 = new FunctionBuilder(registry)
        .withName("function1")
        .withTypeOfThis(thisType)
        .build();

    FunctionType functionType1 = functionBuilder1.build();

    FunctionType expectedFunctionType = new FunctionType(registry, "function1", null,
        null, thisType, null, false, false);

    assertEquals(expectedFunctionType, functionType1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFunctionBuilderWithNullTypeOfThis() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionBuilder functionBuilder1 = new FunctionBuilder(registry)
        .withName("function1")
        .withTypeOfThis(null)
        .build();
  }

  @Test
  public void testFunctionBuilderWithTemplateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionBuilder functionBuilder1 = new FunctionBuilder(registry)
        .withName("function1")
        .withTemplateName("TemplateName")
        .build();

    FunctionType functionType1 = functionBuilder1.build();

    FunctionType expectedFunctionType = new FunctionType(registry, "function1", null,
        null, null, "TemplateName", false, false);

    assertEquals(expectedFunctionType, functionType1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFunctionBuilderWithEmptyTemplateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionBuilder functionBuilder1 = new FunctionBuilder(registry)
        .withName("function1")
        .withTemplateName("")
        .build();
  }

  @Test
  public void testFunctionBuilderWithNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionBuilder functionBuilder1 = new FunctionBuilder(registry)
        .withName("function1")
        .forNativeType()
        .build();

    FunctionType functionType1 = functionBuilder1.build();

    FunctionType expectedFunctionType = new FunctionType(registry, "function1", null,
        null, null, null, true, true);

    assertEquals(expectedFunctionType, functionType1);
  }

  @Test
  public void testFunctionBuilderWithConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionBuilder functionBuilder1 = new FunctionBuilder(registry)
        .withName("function1")
        .forConstructor()
        .build();

    FunctionType functionType1 = functionBuilder1.build();

    FunctionType expectedFunctionType = new FunctionType(registry, "function1", null,
        new ConstructorType(registry, null, null), null, null, true, false);

    assertEquals(expectedFunctionType, functionType1);
  }

  @Test
  public void testFunctionBuilderWithCopyFromOtherFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionType functionType1 = new FunctionType(registry, "FunctionType1", null,
        null, null, null, false, false);

    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionBuilder functionBuilder1 = new FunctionBuilder(functionBuilder)
        .copyFromOtherFunction(functionType1)
        .build();

    FunctionType functionType2 = functionBuilder1.build();

    assertEquals(functionType1, functionType2);
  }

  @Test
  public void testFunctionBuilderWithInvalidParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionBuilder functionBuilder1 = new FunctionBuilder(registry)
        .withName("function1")
        .withParams(null)
        .build();

    try {
      functionBuilder1.build();
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testFunctionBuilderWithEmptyParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionParamBuilder[] emptyParamBuilders = new FunctionParamBuilder[0];

    FunctionBuilder functionBuilder1 = new FunctionBuilder(registry)
        .withName("function1")
        .withParams(emptyParamBuilders)
        .build();

    FunctionType functionType1 = functionBuilder1.build();

    FunctionType expectedFunctionType = new FunctionType(registry, "function1", null,
        null, null, null, false, false);

    assertEquals(expectedFunctionType, functionType1);
  }

  @Test
  public void testFunctionBuilderWithMixedParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder()
        .withName("param1")
        .withType(Number.NUMBER);
    FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder()
        .withName("param2")
        .withType(String.STRING);

    Node parametersNode = functionBuilder.withParams(paramBuilder1, paramBuilder2)
        .build();

    FunctionBuilder functionBuilder1 = new FunctionBuilder(registry)
        .withName("function1")
        .withParams(parametersNode)
        .withReturnType(Void.VOID)
        .build();

    FunctionType functionType1 = functionBuilder1.build();

    FunctionType expectedFunctionType = new FunctionType(registry, "function1", null,
        new ArrowType(registry, parametersNode, Void.VOID, false), null, null,
        false, false);

    assertEquals(expectedFunctionType, functionType1);
  }
}