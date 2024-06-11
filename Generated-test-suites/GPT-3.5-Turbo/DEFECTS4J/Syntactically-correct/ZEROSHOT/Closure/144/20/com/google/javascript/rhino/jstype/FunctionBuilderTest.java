import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionBuilderTest {

    private JSTypeRegistry registry;
    private FunctionBuilder functionBuilder;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        functionBuilder = new FunctionBuilder(registry);
    }

    @Test
    public void testWithName() {
        String name = "testFunction";
        FunctionBuilder result = functionBuilder.withName(name);
        assertNotNull(result);
        // Additional assertions
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node();
        FunctionBuilder result = functionBuilder.withSourceNode(sourceNode);
        assertNotNull(result);
        // Additional assertions
    }

    @Test
    public void testWithParams() {
        FunctionParamBuilder params = new FunctionParamBuilder();
        FunctionBuilder result = functionBuilder.withParams(params);
        assertNotNull(result);
        // Additional assertions
    }

    @Test
    public void testWithParamsNode() {
        Node parametersNode = new Node();
        FunctionBuilder result = functionBuilder.withParamsNode(parametersNode);
        assertNotNull(result);
        // Additional assertions
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = new JSType();
        FunctionBuilder result = functionBuilder.withReturnType(returnType);
        assertNotNull(result);
        // Additional assertions
    }

    @Test
    public void testWithInferredReturnType() {
        JSType returnType = new JSType();
        FunctionBuilder result = functionBuilder.withInferredReturnType(returnType);
        assertNotNull(result);
        // Additional assertions
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = new ObjectType();
        FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis);
        assertNotNull(result);
        // Additional assertions
    }

    @Test
    public void testWithTemplateName() {
        String templateTypeName = "testTemplate";
        FunctionBuilder result = functionBuilder.withTemplateName(templateTypeName);
        assertNotNull(result);
        // Additional assertions
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder result = functionBuilder.forConstructor();
        assertNotNull(result);
        // Additional assertions
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder result = functionBuilder.forNativeType();
        assertNotNull(result);
        // Additional assertions
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = new FunctionType();
        FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
        assertNotNull(result);
        // Additional assertions
    }

    @Test
    public void testBuild() {
        FunctionType result = functionBuilder.build();
        assertNotNull(result);
        // Additional assertions
    }
}