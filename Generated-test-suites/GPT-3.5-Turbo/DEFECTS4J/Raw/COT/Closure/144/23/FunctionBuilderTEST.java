import org.junit.Test;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        builder.withName("testFunction");
        FunctionType functionType = builder.build();
        assertEquals("testFunction", functionType.getName());
    }

    @Test
    public void testWithSourceNode() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        Node sourceNode = new Node();
        builder.withSourceNode(sourceNode);
        FunctionType functionType = builder.build();
        assertEquals(sourceNode, functionType.getSourceNode());
    }

    @Test
    public void testWithParams() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
        paramBuilder.addParameter("param1", JSType.NUMBER);
        paramBuilder.addParameter("param2", JSType.STRING);
        builder.withParams(paramBuilder);
        FunctionType functionType = builder.build();
        assertEquals(paramBuilder.build(), functionType.getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        Node parametersNode = new Node();
        builder.withParamsNode(parametersNode);
        FunctionType functionType = builder.build();
        assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        JSType returnType = JSType.BOOLEAN;
        builder.withReturnType(returnType);
        FunctionType functionType = builder.build();
        assertEquals(returnType, functionType.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        JSType returnType = JSType.NULL;
        builder.withInferredReturnType(returnType);
        FunctionType functionType = builder.build();
        assertEquals(returnType, functionType.getReturnType());
        assertTrue(functionType.isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        ObjectType typeOfThis = new ObjectType();
        builder.withTypeOfThis(typeOfThis);
        FunctionType functionType = builder.build();
        assertEquals(typeOfThis, functionType.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        String templateTypeName = "TemplateType";
        builder.withTemplateName(templateTypeName);
        FunctionType functionType = builder.build();
        assertEquals(templateTypeName, functionType.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        builder.forConstructor();
        FunctionType functionType = builder.build();
        assertTrue(functionType.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        builder.forNativeType();
        FunctionType functionType = builder.build();
        assertTrue(functionType.isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType otherType = new FunctionType();
        otherType.setName("testFunction");
        otherType.setSourceNode(new Node());
        otherType.setParametersNode(new Node());
        otherType.setReturnType(JSType.NUMBER);
        otherType.setTypeOfThis(new ObjectType());
        otherType.setTemplateTypeName("TemplateType");
        otherType.setConstructor(true);
        otherType.setNativeObjectType(true);
        builder.copyFromOtherFunction(otherType);
        FunctionType functionType = builder.build();
        assertEquals(otherType.getName(), functionType.getName());
        assertEquals(otherType.getSourceNode(), functionType.getSourceNode());
        assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
        assertEquals(otherType.getReturnType(), functionType.getReturnType());
        assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
        assertTrue(functionType.isConstructor());
        assertTrue(functionType.isNativeObjectType());
    }
}