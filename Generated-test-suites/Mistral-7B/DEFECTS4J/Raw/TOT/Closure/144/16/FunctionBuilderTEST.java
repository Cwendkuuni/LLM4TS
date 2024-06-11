import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  @Test
  public void testSimpleFunctionBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();

    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();

    paramBuilder.withName("param1").withType(String.class);
    paramBuilder.withName("param2").withType(Integer.class);

    functionBuilder.withName("testFunction")
        .withParams(paramBuilder.build())
        .withReturnType(VoidType.VOID)
        .build();

    FunctionType functionType = functionBuilder.build();

    assertEquals("testFunction", functionType.getReferenceName());
    assertEquals(3, functionType.getParameterList().size());
    FunctionParam[] params = functionType.getParameterList().toArray(new FunctionParam[0]);
    assertEquals("param1", params[0].getName());
    assertEquals(String.class, params[0].getType());
    assertEquals("param2", params[1].getName());
    assertEquals(Integer.class, params[1].getType());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
    assertNull(functionType.getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderWithReturnTypeAndTypeOfThis() {
    JSTypeRegistry registry = new JSTypeRegistry();

    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();

    paramBuilder.withName("param1").withType(String.class);
    paramBuilder.withName("param2").withType(Integer.class);

    functionBuilder.withName("testFunction")
        .withParams(paramBuilder.build())
        .withReturnType(String.class)
        .withTypeOfThis(ObjectType.OBJECT)
        .build();

    FunctionType functionType = functionBuilder.build();

    assertEquals("testFunction", functionType.getReferenceName());
    assertEquals(3, functionType.getParameterList().size());
    FunctionParam[] params = functionType.getParameterList().toArray(new FunctionParam[0]);
    assertEquals("param1", params[0].getName());
    assertEquals(String.class, params[0].getType());
    assertEquals("param2", params[1].getName());
    assertEquals(Integer.class, params[1].getType());
    assertEquals("Object", functionType.getTypeOfThis().getName());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithTemplateTypeName() {
    JSTypeRegistry registry = new JSTypeRegistry();

    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();

    paramBuilder.withName("param1").withType(String.class);
    paramBuilder.withName("param2").withType(Integer.class);

    functionBuilder.withName("testFunction")
        .withParams(paramBuilder.build())
        .withReturnType(String.class)
        .withTemplateName("TestFunctionTemplate")
        .build();

    FunctionType functionType = functionBuilder.build();

    assertEquals("testFunction", functionType.getReferenceName());
    assertEquals(3, functionType.getParameterList().size());
    FunctionParam[] params = functionType.getParameterList().toArray(new FunctionParam[0]);
    assertEquals("param1", params[0].getName());
    assertEquals(String.class, params[0].getType());
    assertEquals("param2", params[1].getName());
    assertEquals(Integer.class, params[1].getType());
    assertEquals("TestFunctionTemplate", functionType.getTemplateTypeName());
    assertNull(functionType.getTypeOfThis());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();

    paramBuilder.withName("param1").withType(String.class);
    paramBuilder.withName("param2").withType(Integer.class);

    Node sourceNode = new ObjectLiteral();
    sourceNode.setClassName("MyFunction");
    sourceNode.setMember("params", new ArrayLiteral());
    sourceNode.getLastChild().setType(new ObjectType("FunctionParams"));
    sourceNode.getLastChild().getLastChild().setType(new ArrayType(FunctionType.FUNCTION_TYPE));

    FunctionBuilder functionBuilderWithSourceNode = new FunctionBuilder(registry);
    functionBuilderWithSourceNode.copyFromOtherFunction(functionBuilder.build());
    functionBuilderWithSourceNode.withSourceNode(sourceNode);

    FunctionType functionType = functionBuilderWithSourceNode.build();

    assertEquals(functionType.getReferenceName(), "MyFunction");
  }

  @Test
  public void testFunctionBuilderWithConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();

    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();

    paramBuilder.withName("param1").withType(String.class);
    paramBuilder.withName("param2").withType(Integer.class);

    functionBuilder.forConstructor()
        .withParams(paramBuilder.build())
        .withReturnType(ObjectType.OBJECT)
        .build();

    FunctionType functionType = functionBuilder.build();

    assertTrue(functionType.isConstructor());

    FunctionParam[] params = functionType.getParameterList().toArray(new FunctionParam[0]);
    assertEquals("param1", params[0].getName());
    assertEquals(String.class, params[0].getType());
    assertEquals("param2", params[1].getName());
    assertEquals(Integer.class, params[1].getType());
    assertNull(functionType.getTemplateTypeName());
    assertNull(functionType.getSource());
    assertNull(functionType.getTypeOfThis());
  }

  @Test(expected = AssertionError.class)
  public void testFunctionBuilderWithInvalidReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();

    paramBuilder.withName("param1").withType(String.class);
    paramBuilder.withName("param2").withType(Integer.class);

    functionBuilder.withName("testFunction")
        .withParams(paramBuilder.build())
        .withReturnType(null)
        .build();
  }

  @Test(expected = AssertionError.class)
  public void testFunctionBuilderWithNullSourceNode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();

    paramBuilder.withName("param1").withType(String.class);
    paramBuilder.withName("param2").withType(Integer.class);

    functionBuilder.withName("testFunction")
        .withParams(paramBuilder.build())
        .withSourceNode(null)
        .build();
  }
}