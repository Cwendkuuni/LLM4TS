import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testBuildFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    // Set properties
    String name = "testFunction";
    Node sourceNode = new Node();
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder();
    Node parametersNode = functionParamBuilder.build();
    JSType returnType = new IntType();
    ObjectType typeOfThis = new ObjectType();

    // Call build method
    FunctionType functionType = functionBuilder
        .withName(name)
        .withSourceNode(sourceNode)
        .withParams(functionParamBuilder)
        .withReturnType(returnType)
        .withTypeOfThis(typeOfThis)
        .build();

    // Verify the built function type
    assertEquals("testFunction", functionType.getReferenceName());
    assertEquals(sourceNode, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }
}