
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  @Test
  public void testBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node sourceNode = new Node();
    Node parametersNode = new Node();
    JSType returnType = registry.createType("ReturnType");
    ObjectType typeOfThis = registry.createObjectType("TypeOfThis");
    String templateTypeName = "TemplateTypeName";

    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withName("testFunction")
        .withSourceNode(sourceNode)
        .withParams(new FunctionParamBuilder(parametersNode))
        .withReturnType(returnType)
        .withTypeOfThis(typeOfThis)
        .withTemplateName(templateTypeName)
        .forConstructor();

    FunctionType functionType = functionBuilder.build();

    assertEquals("testFunction", functionType.getReferenceName());
    assertEquals(sourceNode, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertEquals(true, functionType.isConstructor());
    assertEquals(false, functionType.isNativeObjectType());
  }
}