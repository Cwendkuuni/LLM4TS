import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {

  @org.junit.Test
  public void testFunctionBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test building a simple function type
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.withName("addFunction")
        .withParams(new FunctionParamBuilder().withName("num1").withType(JSType.NUMBER))
        .withParams(new FunctionParamBuilder().withName("num2").withType(JSType.NUMBER))
        .withReturnType(JSType.NUMBER)
        .build();
    FunctionType addFunction = functionBuilder.build();

    // Test getting function type properties
    assertEquals("addFunction", addFunction.getReferenceName());
    assertNotNull(addFunction.getSource());
    assertEquals(2, addFunction.getParameters().size());
    assertTrue(addFunction.isFunctionType());
    assertEquals(JSType.NUMBER, addFunction.getReturnType());

    // Test building a function type with inferred return type
    FunctionBuilder functionBuilder2 = new FunctionBuilder(registry);
    functionBuilder2.withName("multiplyFunction")
        .withParams(new FunctionParamBuilder().withName("num1").withType(JSType.NUMBER))
        .withParams(new FunctionParamBuilder().withName("num2").withType(JSType.NUMBER))
        .withInferredReturnType(JSType.NUMBER)
        .build();
    FunctionType multiplyFunction = functionBuilder2.build();
    assertEquals(JSType.NUMBER, multiplyFunction.getReturnType());
    assertTrue(multiplyFunction.isInferredReturnType());

    // Test building a constructor function type
    FunctionBuilder constructorFunctionBuilder = new FunctionBuilder(registry);
    constructorFunctionBuilder.forConstructor()
        .withName("StringConstructor")
        .withParams(new FunctionParamBuilder().withName("str").withType(JSType.STRING))
        .withReturnType(ObjectType.STRING)
        .build();
    FunctionType stringConstructor = constructorFunctionBuilder.build();
    assertTrue(stringConstructor.isConstructor());

    // Test building a native function type
    FunctionBuilder nativeFunctionBuilder = new FunctionBuilder(registry);
    nativeFunctionBuilder.forNativeType();
    FunctionType nativeFunction = nativeFunctionBuilder.build();
    assertTrue(nativeFunction.isNativeObjectType());

    // Test building a function type with template type name
    FunctionBuilder templateFunctionBuilder = new FunctionBuilder(registry);
    templateFunctionBuilder.withName("ArrayFunction")
        .withParams(new FunctionParamBuilder().withName("size").withType(JSType.NUMBER))
        .withReturnType(new ArrayType(registry, JSType.NUMBER))
        .withTemplateName("Array")
        .build();
    FunctionType arrayFunction = templateFunctionBuilder.build();
    assertEquals("Array", arrayFunction.getTemplateTypeName());

    // Test building a function type with typeOfThis
    FunctionBuilder thisFunctionBuilder = new FunctionBuilder(registry);
    ObjectType objectType = new ObjectType(registry, "MyObject");
    thisFunctionBuilder.forConstructor()
        .withName("MyObjectConstructor")
        .withTypeOfThis(objectType)
        .build();
    FunctionType myObjectConstructor = thisFunctionBuilder.build();
    assertEquals(objectType, myObjectConstructor.getTypeOfThis());

    // Test building a function type with multiple parameters
    FunctionBuilder multipleParamsFunctionBuilder = new FunctionBuilder(registry);
    multipleParamsFunctionBuilder.withName("sumFunction")
        .withParams(new FunctionParamBuilder().withName("num1").withType(JSType.NUMBER))
        .withParams(new FunctionParamBuilder().withName("num2").withType(JSType.NUMBER))
        .withParams(new FunctionParamBuilder().withName("num3").withType(JSType.NUMBER))
        .withReturnType(JSType.NUMBER)
        .build();
    FunctionType sumFunction = multipleParamsFunctionBuilder.build();
    assertEquals(3, sumFunction.getParameters().size());

    // Test error cases
    try {
      new FunctionBuilder(registry).build();
      fail("FunctionBuilder must be initialized with at least a name and a return type.");
    } catch (IllegalStateException e) {
      // Expected
    }

    try {
      new FunctionBuilder(registry).withName("myFunction").build();
      fail("FunctionBuilder must have at least one parameter.");
    } catch (IllegalStateException e) {
      // Expected
    }

    try {
      new FunctionBuilder(registry).withName("myFunction")
          .withParams(new FunctionParamBuilder()).build();
      fail("FunctionBuilder must have a return type.");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}