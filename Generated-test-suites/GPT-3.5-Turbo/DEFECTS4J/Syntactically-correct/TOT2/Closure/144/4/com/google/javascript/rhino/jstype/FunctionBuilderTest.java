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
        FunctionType functionType = functionBuilder.withName(name).build();
        assertEquals(name, functionType.getName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node();
        FunctionType functionType = functionBuilder.withSourceNode(sourceNode).build();
        assertEquals(sourceNode, functionType.getSource());
    }

    @Test
    public void testWithParams() {
        FunctionParamBuilder params = new FunctionParamBuilder()
                .addParam("param1", registry.getNativeType(JSTypeNative.STRING_TYPE));
        FunctionType functionType = functionBuilder.withParams(params).build();
        assertEquals(1, functionType.getParameters().size());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = registry.getNativeType(JSTypeNative.BOOLEAN_TYPE);
        FunctionType functionType = functionBuilder.withReturnType(returnType).build();
        assertEquals(returnType, functionType.getReturnType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherFunctionType = functionBuilder.withName("otherFunction").build();
        FunctionType functionType = new FunctionBuilder(registry).copyFromOtherFunction(otherFunctionType).build();
        assertEquals(otherFunctionType.getName(), functionType.getName());
        assertEquals(otherFunctionType.getSource(), functionType.getSource());
        assertEquals(otherFunctionType.getParametersNode(), functionType.getParametersNode());
        assertEquals(otherFunctionType.getReturnType(), functionType.getReturnType());
        assertEquals(otherFunctionType.getTypeOfThis(), functionType.getTypeOfThis());
        assertEquals(otherFunctionType.getTemplateTypeName(), functionType.getTemplateTypeName());
        assertEquals(otherFunctionType.isConstructor(), functionType.isConstructor());
        assertEquals(otherFunctionType.isNativeObjectType(), functionType.isNativeObjectType());
    }

    // Additional test cases for edge cases and error scenarios would be added here

}