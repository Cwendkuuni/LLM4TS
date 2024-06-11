import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionType functionType = builder.withName("testFunction").build();
        assertEquals("testFunction", functionType.getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        Node sourceNode = new Node();
        FunctionType functionType = builder.withSourceNode(sourceNode).build();
        assertEquals(sourceNode, functionType.getSource());
    }

    @Test
    public void testWithParams() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionParamBuilder params = new FunctionParamBuilder();
        Node parametersNode = params.build();
        FunctionType functionType = builder.withParams(params).build();
        assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        Node parametersNode = new Node();
        FunctionType functionType = builder.withParamsNode(parametersNode).build();
        assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        JSType returnType = new JSType();
        FunctionType functionType = builder.withReturnType(returnType).build();
        assertEquals(returnType, functionType.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        JSType returnType = new JSType();
        FunctionType functionType = builder.withInferredReturnType(returnType).build();
        assertEquals(returnType, functionType.getReturnType());
        assertTrue(functionType.isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        ObjectType typeOfThis = new ObjectType();
        FunctionType functionType = builder.withTypeOfThis(typeOfThis).build();
        assertEquals(typeOfThis, functionType.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        String templateTypeName = "templateType";
        FunctionType functionType = builder.withTemplateName(templateTypeName).build();
        assertEquals(templateTypeName, functionType.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionType functionType = builder.forConstructor().build();
        assertTrue(functionType.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionType functionType = builder.forNativeType().build();
        assertTrue(functionType.isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = new FunctionType(new JSTypeRegistry(), "otherFunction", new Node(), new ArrowType(new JSTypeRegistry(), new Node(), new JSType(), false), new ObjectType(), "otherTemplateType", true, false);
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionType functionType = builder.copyFromOtherFunction(otherType).build();
        assertEquals(otherType.getReferenceName(), functionType.getReferenceName());
        assertEquals(otherType.getSource(), functionType.getSource());
        assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
        assertEquals(otherType.getReturnType(), functionType.getReturnType());
        assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
        assertEquals(otherType.isConstructor(), functionType.isConstructor());
        assertEquals(otherType.isNativeObjectType(), functionType.isNativeObjectType());
    }
}