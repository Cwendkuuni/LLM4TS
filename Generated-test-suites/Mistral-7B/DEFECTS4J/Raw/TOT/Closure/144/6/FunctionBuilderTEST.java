import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import static org.junit.Assert.*;

public class FunctionBuilderTest {
  private FunctionBuilder functionBuilder;
  private FunctionType functionType;

  @Before
  public void setUp() {
    JSTypeRegistry registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testBasicFunctionType() {
    // Set the name, parameters, return type, and build the function type
    String functionName = "myFunction";
    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(registry, "param1", null);
    FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder(registry, "param2", null);
    Node parametersNode = new Node.SequenceNode(new Node[] {paramBuilder1.build(), paramBuilder2.build()});
    JSType returnType = new NumberType(registry);
    functionType = functionBuilder
        .withName(functionName)
        .withParams(parametersNode)
        .withReturnType(returnType)
        .build();

    // Assert the name, parameters, and return type of the function type
    assertEquals(functionName, functionType.getReferenceName());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testConstructorFunctionType() {
    // Set the name, parameters, return type, and make it a constructor
    String constructorName = "MyConstructor";
    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(registry, "param1", null);
    FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder(registry, "param2", null);
    Node parametersNode = new Node.SequenceNode(new Node[] {paramBuilder1.build(), paramBuilder2.build()});
    JSType returnType = null;
    functionType = functionBuilder
        .withName(constructorName)
        .withParams(parametersNode)
        .forConstructor()
        .build();

    // Assert the name, parameters, and return type of the constructor type
    assertTrue(functionType instanceof ConstructorType);
    assertEquals(constructorName, functionType.getReferenceName());
    assertNull(functionType.getReturnType());
  }

  @Test
  public void testNativeFunctionType() {
    // Set the name and make it a native type
    String nativeFunctionName = "nativeFunction";
    functionType = functionBuilder
        .withName(nativeFunctionName)
        .forNativeType()
        .build();

    // Assert the name and return type of the native type (which should be ObjectType)
    assertTrue(functionType instanceof NativeObjectType);
    assertEquals(nativeFunctionName, functionType.getReferenceName());
    assertEquals(ObjectType.OBJECT, functionType.getReturnType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMissingName() {
    // Try to build a function type without setting a name
    functionType = functionBuilder.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMissingReturnType() {
    // Try to build a function type without setting a return type
    String functionName = "myFunction";
    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(registry, "param1", null);
    Node parametersNode = new Node.SequenceNode(new Node[] {paramBuilder1.build()});
    functionType = functionBuilder
        .withName(functionName)
        .withParams(parametersNode)
        .build();
  }
}