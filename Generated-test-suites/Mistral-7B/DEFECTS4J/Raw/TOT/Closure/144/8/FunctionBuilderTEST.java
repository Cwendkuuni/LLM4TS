import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.TypeChecker.check;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.testing.Assertions;

public class FunctionBuilderTest {

  @org.junit.Test
  public void testFunctionBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    // Test withName method
    FunctionBuilder functionBuilder1 = functionBuilder.withName("add");

    // Test withSourceNode method
    Node sourceNode = new ObjectLiteral();
    FunctionBuilder functionBuilder2 = functionBuilder1.withSourceNode(sourceNode);

    // Test withParams method
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder();
    FunctionBuilder functionBuilder3 = functionBuilder2.withParams(functionParamBuilder);

    // Test withParamsNode method
    Node parametersNode = new ArrayLiteral();
    FunctionBuilder functionBuilder4 = functionBuilder3.withParamsNode(parametersNode);

    // Test withReturnType method
    JSType returnType = registry.createStringType();
    FunctionBuilder functionBuilder5 = functionBuilder4.withReturnType(returnType);

    // Test withInferredReturnType method
    JSType inferredReturnType = registry.createNumberType();
    FunctionBuilder functionBuilder6 = functionBuilder5.withInferredReturnType(inferredReturnType);

    // Test withTypeOfThis method
    ObjectType typeOfThis = new ObjectType("Object");
    FunctionBuilder functionBuilder7 = functionBuilder6.withTypeOfThis(typeOfThis);

    // Test withTemplateName method
    FunctionBuilder functionBuilder8 = functionBuilder7.withTemplateName("TemplateFunction");

    // Test forConstructor method
    FunctionBuilder functionBuilder9 = functionBuilder8.forConstructor();

    // Test forNativeType method
    FunctionBuilder functionBuilder10 = functionBuilder9.forNativeType();

    // Test copyFromOtherFunction method
    FunctionType otherType = new FunctionType(registry, "otherFunction", null, null, null, null, true, false);
    FunctionBuilder functionBuilder11 = functionBuilder10.copyFromOtherFunction(otherType);

    // Test build method
    FunctionType functionType = functionBuilder11.build();

    // Test getReferenceName method
    assertEquals("add", functionType.getReferenceName());

    // Test getSource method
    assertEquals(sourceNode, functionType.getSource());

    // Test getParametersNode method
    assertEquals(parametersNode, functionType.getParametersNode());

    // Test getReturnType method
    assertEquals(returnType, functionType.getReturnType());

    // Test getTypeOfThis method
    assertEquals(typeOfThis, functionType.getTypeOfThis());

    // Test getTemplateTypeName method
    assertEquals("TemplateFunction", functionType.getTemplateTypeName());

    // Test isConstructor method
    assertTrue(functionType.isConstructor());

    // Test isNativeObjectType method
    assertFalse(functionType.isNativeType());

    // Test toString method
    String expected =
        "FunctionType(" +
            "registry=com.google.javascript.rhino.jstype.JSTypeRegistry@421f225b," +
            "name=add," +
            "sourceNode=ObjectLiteral@131," +
            "parametersType=ArrowType(" +
                "registry=com.google.javascript.rhino.jstype.JSTypeRegistry@421f225b," +
                "parametersNode=ArrayLiteral@163," +
                "returnType=StringType@17," +
                "inferredReturnType=false)" +
            "," +
            "typeOfThis=ObjectType(Object)," +
            "templateTypeName=null," +
            "isConstructor=true," +
            "isNativeType=false)" +
        ")";
    assertEquals(expected, functionType.toString());

    // Test check method
    check(functionType, Assertions.TYPES);
  }
}