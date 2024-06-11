import org.junit.Test;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        functionBuilder.withName("test");
        // Add assertions to check the functionality of withName()
    }

    @Test
    public void testWithSourceNode() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        functionBuilder.withSourceNode(null);
        // Add assertions to check the functionality of withSourceNode()
    }

    @Test
    public void testWithParams() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder();
        functionBuilder.withParams(functionParamBuilder);
        // Add assertions to check the functionality of withParams()
    }

    @Test
    public void testWithParamsNode() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        functionBuilder.withParamsNode(null);
        // Add assertions to check the functionality of withParamsNode()
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        functionBuilder.withReturnType(null);
        // Add assertions to check the functionality of withReturnType()
    }

    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        functionBuilder.withInferredReturnType(null);
        // Add assertions to check the functionality of withInferredReturnType()
    }

    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        functionBuilder.withTypeOfThis(null);
        // Add assertions to check the functionality of withTypeOfThis()
    }

    @Test
    public void testWithTemplateName() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        functionBuilder.withTemplateName(null);
        // Add assertions to check the functionality of withTemplateName()
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        functionBuilder.forConstructor();
        // Add assertions to check the functionality of forConstructor()
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        functionBuilder.forNativeType();
        // Add assertions to check the functionality of forNativeType()
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionType functionType = new FunctionType(null, null, null, null, null, null, false, false);
        functionBuilder.copyFromOtherFunction(functionType);
        // Add assertions to check the functionality of copyFromOtherFunction()
    }

    @Test
    public void testBuild() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionType functionType = functionBuilder.build();
        // Add assertions to check the functionality of build()
    }

}