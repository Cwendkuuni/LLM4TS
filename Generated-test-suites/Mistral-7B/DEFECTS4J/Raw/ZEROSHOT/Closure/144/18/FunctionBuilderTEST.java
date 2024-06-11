import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createIdent;
import static com.google.javascript.rhino.NodeFactory.createList;
import static com.google.javascript.rhino.NodeFactory.createNull;
import static com.google.javascript.rhino.NodeFactory.createObject;
import static com.google.javascript.rhino.NodeFactory.createProperty;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.JSTypeFactory.objectType;
import static com.google.javascript.rhino.JSTypeFactory.undefinedType;
import static com.google.javascript.rhino.JSTypeFactory.voidType;

import org.junit.Test;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.types.ArrowType;
import com.google.javascript.rhino.types.FunctionType;
import com.google.javascript.rhino.types.ObjectType;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilder_simpleFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    Node functionName = createIdent("myFunction");
    Node voidNode = createNull();

    // Set the name of the function type.
    functionBuilder.withName("myFunction");

    // Set the source node of the function type to null.
    // In practice, this would be set to the AST node for the function definition.

    // Set the parameters of the function type from a FunctionParamBuilder.
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder();
    Node parameterNode = functionParamBuilder.withName("param1").build();
    Node parametersList = createList(parameterNode);

    // Set the parameters of the function type with a specially-formatted node.
    functionBuilder.withParamsNode(parametersList);

    // Set the return type to void.
    functionBuilder.withReturnType(voidType);

    FunctionType functionType = functionBuilder.build();
    JSType expectedType = new FunctionType(registry, "myFunction", null,
        new ArrowType(registry, parametersList, undefinedType, false), objectType(null),
        null, false, false);

    assertEquals(expectedType, functionType);
  }

  @Test
  public void testFunctionBuilder_functionWithReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    Node functionName = createIdent("myFunction");
    Node stringType = JSTypeFactory.stringType();

    // Set the name of the function type.
    functionBuilder.withName("myFunction");

    // Set the source node of the function type to null.
    // In practice, this would be set to the AST node for the function definition.

    // Set the parameters of the function type from a FunctionParamBuilder.
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder();
    Node parameterNode = functionParamBuilder.withName("param1").build();
    Node parametersList = createList(parameterNode);

    // Set the parameters of the function type with a specially-formatted node.
    functionBuilder.withParamsNode(parametersList);

    // Set the return type and make it inferred.
    functionBuilder.withReturnType(stringType);
    functionBuilder.withInferredReturnType(stringType);

    FunctionType functionType = functionBuilder.build();
    JSType expectedType = new FunctionType(registry, "myFunction", null,
        new ArrowType(registry, parametersList, stringType, true), objectType(null),
        null, false, false);

    assertEquals(expectedType, functionType);
  }

  @Test
  public void testFunctionBuilder_constructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    Node constructorName = createIdent("MyConstructor");

    // Set the name of the constructor type.
    functionBuilder.withName("MyConstructor");

    // Set the source node of the constructor type to null.
    // In practice, this would be set to the AST node for the constructor definition.

    // Set the parameters of the constructor type from a FunctionParamBuilder.
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder();
    Node parameterNode = functionParamBuilder.withName("param1").build();
    Node parametersList = createList(parameterNode);

    // Set the parameters of the constructor type with a specially-formatted node.
    functionBuilder.withParamsNode(parametersList);

    // Set the return type to null, indicating that this is a constructor.
    functionBuilder.withReturnType(null);

    FunctionType constructorType = functionBuilder.build();
    JSType expectedType = new FunctionType(registry, "MyConstructor", null,
        new ArrowType(registry, parametersList, null, false), objectType(null),
        null, true, false);

    assertEquals(expectedType, constructorType);
  }

  @Test
  public void testFunctionBuilder_nativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    Node nativeTypeName = createIdent("Math");

    // Set the name of the native type.
    functionBuilder.withName("Math");

    // Set the source node of the native type to null.
    // In practice, this would be set to the AST node for the native type definition.

    // Set the parameters of the native type to null, as native types have no parameters.
    functionBuilder.withParamsNode(createNull());

    // Set the return type to void.
    functionBuilder.withReturnType(voidType);

    // Set the native type flag.
    functionBuilder.forNativeType();

    FunctionType nativeType = functionBuilder.build();
    JSType expectedType = new FunctionType(registry, "Math", null,
        ArrowType.VOID_ARROW_TYPE, objectType(null), null, false, true);

    assertEquals(expectedType, nativeType);
  }

  @Test
  public void testFunctionBuilder_copyFromOtherFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionType originalFunctionType = new FunctionType(registry, "myFunction", null,
        new ArrowType(registry, createList(createIdent("param1")), stringType),
        objectType(null), null, false, false);

    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.copyFromOtherFunction(originalFunctionType);

    FunctionType copiedFunctionType = functionBuilder.build();

    assertEquals(originalFunctionType, copiedFunctionType);
  }
}