import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionType type = builder.withName("testFunction").build();
        assertEquals("testFunction", type.getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        Node sourceNode = new Node();
        FunctionType type = builder.withSourceNode(sourceNode).build();
        assertEquals(sourceNode, type.getSource());
    }

    @Test
    public void testWithParams() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
        Node params = paramBuilder.withParam("param1", new JSType()).build();
        FunctionType type = builder.withParams(paramBuilder).build();
        assertEquals(params, type.getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        Node params = new Node();
        FunctionType type = builder.withParamsNode(params).build();
        assertEquals(params, type.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        JSType returnType = new JSType();
        FunctionType type = builder.withReturnType(returnType).build();
        assertEquals(returnType, type.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        JSType returnType = new JSType();
        FunctionType type = builder.withInferredReturnType(returnType).build();
        assertTrue(type.isInferredReturnType());
        assertEquals(returnType, type.getReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        ObjectType typeOfThis = new ObjectType();
        FunctionType type = builder.withTypeOfThis(typeOfThis).build();
        assertEquals(typeOfThis, type.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        String templateTypeName = "Template";
        FunctionType type = builder.withTemplateName(templateTypeName).build();
        assertEquals(templateTypeName, type.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionType type = builder.forConstructor().build();
        assertTrue(type.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionType type = builder.forNativeType().build();
        assertTrue(type.isNativeObjectType());
    }
}


