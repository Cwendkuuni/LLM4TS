import org.junit.Assert;
import org.junit.Test;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType function = builder.withName("testFunction").build();
        Assert.assertEquals("testFunction", function.getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        Node sourceNode = new Node();
        FunctionType function = builder.withSourceNode(sourceNode).build();
        Assert.assertEquals(sourceNode, function.getSource());
    }

    @Test
    public void testWithParams() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionParamBuilder params = new FunctionParamBuilder();
        Node parametersNode = params.build();
        FunctionType function = builder.withParams(params).build();
        Assert.assertEquals(parametersNode, function.getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        Node parametersNode = new Node();
        FunctionType function = builder.withParamsNode(parametersNode).build();
        Assert.assertEquals(parametersNode, function.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        JSType returnType = new JSType();
        FunctionType function = builder.withReturnType(returnType).build();
        Assert.assertEquals(returnType, function.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        JSType returnType = new JSType();
        FunctionType function = builder.withInferredReturnType(returnType).build();
        Assert.assertEquals(returnType, function.getReturnType());
        Assert.assertTrue(function.isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        ObjectType typeOfThis = new ObjectType();
        FunctionType function = builder.withTypeOfThis(typeOfThis).build();
        Assert.assertEquals(typeOfThis, function.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType function = builder.withTemplateName("templateName").build();
        Assert.assertEquals("templateName", function.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType function = builder.forConstructor().build();
        Assert.assertTrue(function.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType function = builder.forNativeType().build();
        Assert.assertTrue(function.isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType otherType = new FunctionType();
        FunctionType function = builder.copyFromOtherFunction(otherType).build();
        Assert.assertEquals(otherType.getReferenceName(), function.getReferenceName());
        Assert.assertEquals(otherType.getSource(), function.getSource());
        Assert.assertEquals(otherType.getParametersNode(), function.getParametersNode());
        Assert.assertEquals(otherType.getReturnType(), function.getReturnType());
        Assert.assertEquals(otherType.getTypeOfThis(), function.getTypeOfThis());
        Assert.assertEquals(otherType.getTemplateTypeName(), function.getTemplateTypeName());
        Assert.assertEquals(otherType.isConstructor(), function.isConstructor());
        Assert.assertEquals(otherType.isNativeObjectType(), function.isNativeObjectType());
    }

}