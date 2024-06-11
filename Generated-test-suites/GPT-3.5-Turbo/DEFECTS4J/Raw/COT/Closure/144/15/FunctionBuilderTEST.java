import org.junit.Assert;
import org.junit.Test;

public class FunctionBuilderTest {
    @Test
    public void testWithName() {
        FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
        String name = "testName";
        FunctionBuilder result = functionBuilder.withName(name);
        Assert.assertEquals(name, result.getName());
    }

    @Test
    public void testWithSourceNode() {
        FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
        Node sourceNode = new Node();
        FunctionBuilder result = functionBuilder.withSourceNode(sourceNode);
        Assert.assertEquals(sourceNode, result.getSourceNode());
    }

    @Test
    public void testWithParams() {
        // TODO: Implement this test case
    }

    @Test
    public void testWithParamsNode() {
        // TODO: Implement this test case
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
        JSType returnType = new JSType();
        FunctionBuilder result = functionBuilder.withReturnType(returnType);
        Assert.assertEquals(returnType, result.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
        JSType returnType = new JSType();
        FunctionBuilder result = functionBuilder.withInferredReturnType(returnType);
        Assert.assertEquals(returnType, result.getReturnType());
        Assert.assertTrue(result.isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
        ObjectType typeOfThis = new ObjectType();
        FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis);
        Assert.assertEquals(typeOfThis, result.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
        String templateTypeName = "testTemplate";
        FunctionBuilder result = functionBuilder.withTemplateName(templateTypeName);
        Assert.assertEquals(templateTypeName, result.getTemplateName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
        FunctionBuilder result = functionBuilder.forConstructor();
        Assert.assertTrue(result.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
        FunctionBuilder result = functionBuilder.forNativeType();
        Assert.assertTrue(result.isNativeType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
        FunctionType otherType = new FunctionType();
        FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
        // TODO: Assert that the result contains the same values as otherType
    }

    @Test
    public void testBuild() {
        // TODO: Implement this test case
    }
}