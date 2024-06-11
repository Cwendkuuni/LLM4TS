

import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

    private FunctionBuilder functionBuilder;
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        functionBuilder = new FunctionBuilder(registry);
    }

    @Test
    public void testWithName() {
        FunctionType functionType = functionBuilder.withName("testFunction").build();

        assertEquals("testFunction", functionType.getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node();
        FunctionType functionType = functionBuilder.withSourceNode(sourceNode).build();

        assertEquals(sourceNode, functionType.getSource());
    }

    @Test
    public void testWithParams() {
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
        Node parametersNode = paramBuilder.build();
        FunctionType functionType = functionBuilder.withParams(paramBuilder).build();

        assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        Node parametersNode = new Node();
        FunctionType functionType = functionBuilder.withParamsNode(parametersNode).build();

        assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = new JSType();
        FunctionType functionType = functionBuilder.withReturnType(returnType).build();

        assertEquals(returnType, functionType.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSType returnType = new JSType();
        FunctionType functionType = functionBuilder.withInferredReturnType(returnType).build();

        assertEquals(returnType, functionType.getReturnType());
        assertTrue(functionType.isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = new ObjectType();
        FunctionType functionType = functionBuilder.withTypeOfThis(typeOfThis).build();

        assertEquals(typeOfThis, functionType.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        String templateTypeName = "TemplateType";
        FunctionType functionType = functionBuilder.withTemplateName(templateTypeName).build();

        assertEquals(templateTypeName, functionType.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionType functionType = functionBuilder.forConstructor().build();

        assertTrue(functionType.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionType functionType = functionBuilder.forNativeType().build();

        assertTrue(functionType.isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherFunctionType = new FunctionType(registry, "OtherFunction", new Node(), new ArrowType(registry, new Node(), new JSType(), false), new ObjectType(), "OtherTemplateType", true, false);

        FunctionType functionType = functionBuilder.copyFromOtherFunction(otherFunctionType).build();

        assertEquals(otherFunctionType.getReferenceName(), functionType.getReferenceName());
        assertEquals(otherFunctionType.getSource(), functionType.getSource());
        assertEquals(otherFunctionType.getParametersNode(), functionType.getParametersNode());
        assertEquals(otherFunctionType.getReturnType(), functionType.getReturnType());
        assertEquals(otherFunctionType.getTypeOfThis(), functionType.getTypeOfThis());
        assertEquals(otherFunctionType.getTemplateTypeName(), functionType.getTemplateTypeName());
        assertEquals(otherFunctionType.isConstructor(), functionType.isConstructor());
        assertEquals(otherFunctionType.isNativeObjectType(), functionType.isNativeObjectType());
    }
}

