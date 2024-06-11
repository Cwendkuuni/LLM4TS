import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        String name = "testName";
        builder.withName(name);
        FunctionType functionType = builder.build();
        assertEquals(name, functionType.getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        Node sourceNode = new Node();
        builder.withSourceNode(sourceNode);
        FunctionType functionType = builder.build();
        assertEquals(sourceNode, functionType.getSource());
    }

    @Test
    public void testWithParams() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionParamBuilder params = new FunctionParamBuilder();
        params.addParam("param1", new JSType());
        params.addParam("param2", new JSType());
        builder.withParams(params);
        FunctionType functionType = builder.build();
        assertEquals(params.build(), functionType.getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        Node parametersNode = new Node();
        builder.withParamsNode(parametersNode);
        FunctionType functionType = builder.build();
        assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        JSType returnType = new JSType();
        builder.withReturnType(returnType);
        FunctionType functionType = builder.build();
        assertEquals(returnType, functionType.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        JSType returnType = new JSType();
        builder.withInferredReturnType(returnType);
        FunctionType functionType = builder.build();
        assertEquals(returnType, functionType.getReturnType());
        assertTrue(functionType.isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        ObjectType typeOfThis = new ObjectType();
        builder.withTypeOfThis(typeOfThis);
        FunctionType functionType = builder.build();
        assertEquals(typeOfThis, functionType.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        String templateTypeName = "template";
        builder.withTemplateName(templateTypeName);
        FunctionType functionType = builder.build();
        assertEquals(templateTypeName, functionType.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.forConstructor();
        FunctionType functionType = builder.build();
        assertTrue(functionType.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.forNativeType();
        FunctionType functionType = builder.build();
        assertTrue(functionType.isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionType otherFunctionType = new FunctionType(new JSTypeRegistry(), "other", new Node(), new ArrowType(new JSTypeRegistry(), new Node(), new JSType(), false), new ObjectType(), "template", true, true);
        builder.copyFromOtherFunction(otherFunctionType);
        FunctionType functionType = builder.build();
        assertEquals(otherFunctionType.getReferenceName(), functionType.getReferenceName());
        assertEquals(otherFunctionType.getSource(), functionType.getSource());
        assertEquals(otherFunctionType.getParametersNode(), functionType.getParametersNode());
        assertEquals(otherFunctionType.getReturnType(), functionType.getReturnType());
        assertEquals(otherFunctionType.getTypeOfThis(), functionType.getTypeOfThis());
        assertEquals(otherFunctionType.getTemplateTypeName(), functionType.getTemplateTypeName());
        assertTrue(functionType.isConstructor());
        assertTrue(functionType.isNativeObjectType());
    }
}