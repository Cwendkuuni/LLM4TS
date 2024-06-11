import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  @org.junit.Test
  public void testFunctionBuilderWithBasicFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withName("param1").withType(NUMBER);
    paramBuilder.withName("param2").withType(STRING);

    functionBuilder
        .withName("myFunction")
        .withSourceNode(createMockNode())
        .withParams(paramBuilder.build())
        .withReturnType(VOID)
        .build();

    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertEquals("myFunction", functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertEquals(2, functionType.getParameterCount());
    assertEquals(VOID, functionType.getReturnType());
    assertEquals(false, functionType.isConstructor());
    assertEquals(false, functionType.isNativeObjectType());
  }

  @org.junit.Test
  public void testFunctionBuilderWithFunctionWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withName("param1").withType(NUMBER);
    paramBuilder.withName("param2").withType(STRING);

    functionBuilder
        .withName("myFunction")
        .withSourceNode(createMockNode())
        .withParams(paramBuilder.build())
        .withReturnType(UNDEFINED)
        .withInferredReturnType(NUMBER)
        .build();

    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertEquals("myFunction", functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertEquals(2, functionType.getParameterCount());
    assertEquals(NUMBER, functionType.getReturnType());
    assertEquals(false, functionType.isConstructor());
    assertEquals(false, functionType.isNativeObjectType());
  }

  @org.junit.Test
  public void testFunctionBuilderWithConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withName("param1").withType(NUMBER);
    paramBuilder.withName("param2").withType(STRING);

    functionBuilder
        .withName("MyConstructor")
        .forConstructor()
        .withParams(paramBuilder.build())
        .withTypeOfThis(OBJECT)
        .build();

    FunctionType constructorType = functionBuilder.build();

    assertNotNull(constructorType);
    assertEquals("MyConstructor", constructorType.getReferenceName());
    assertNull(constructorType.getSource());
    assertEquals(2, constructorType.getParameterCount());
    assertEquals(OBJECT, constructorType.getTypeOfThis());
    assertEquals(VOID, constructorType.getReturnType());
    assertTrue(constructorType.isConstructor());
    assertFalse(constructorType.isNativeObjectType());
  }

  @org.junit.Test
  public void testFunctionBuilderWithNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    functionBuilder.forNativeType();

    FunctionType nativeType = functionBuilder.build();

    assertNotNull(nativeType);
    assertNull(nativeType.getReferenceName());
    assertNull(nativeType.getSource());
    assertEquals(0, nativeType.getParameterCount());
    assertNull(nativeType.getReturnType());
    assertFalse(nativeType.isConstructor());
    assertTrue(nativeType.isNativeObjectType());
  }

  private Node createMockNode() {
    //TODO: Implement createMockNode method
    return null;
  }
}