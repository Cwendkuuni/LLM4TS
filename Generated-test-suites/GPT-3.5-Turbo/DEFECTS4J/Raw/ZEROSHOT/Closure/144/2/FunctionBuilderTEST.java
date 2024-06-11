import org.junit.Test;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionBuilder result = functionBuilder.withName("test");
        // assert statements
    }

    @Test
    public void testWithSourceNode() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionBuilder result = functionBuilder.withSourceNode(null);
        // assert statements
    }

    @Test
    public void testWithParams() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionParamBuilder params = new FunctionParamBuilder();
        FunctionBuilder result = functionBuilder.withParams(params);
        // assert statements
    }

    @Test
    public void testWithParamsNode() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionBuilder result = functionBuilder.withParamsNode(null);
        // assert statements
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        JSType returnType = null;
        FunctionBuilder result = functionBuilder.withReturnType(returnType);
        // assert statements
    }

    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        JSType returnType = null;
        FunctionBuilder result = functionBuilder.withInferredReturnType(returnType);
        // assert statements
    }

    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        ObjectType typeOfThis = null;
        FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis);
        // assert statements
    }

    @Test
    public void testWithTemplateName() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        String templateTypeName = null;
        FunctionBuilder result = functionBuilder.withTemplateName(templateTypeName);
        // assert statements
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionBuilder result = functionBuilder.forConstructor();
        // assert statements
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionBuilder result = functionBuilder.forNativeType();
        // assert statements
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionType otherType = null;
        FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
        // assert statements
    }

    @Test
    public void testBuild() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionType result = functionBuilder.build();
        // assert statements
    }
}