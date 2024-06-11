
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        String expectedName = "testFunction";
        FunctionType functionType = functionBuilder.withName(expectedName).build();
        Assert.assertEquals(expectedName, functionType.getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        Node expectedSourceNode = null;
        FunctionType functionType = functionBuilder.withSourceNode(expectedSourceNode).build();
        Assert.assertEquals(expectedSourceNode, functionType.getSource());
    }

    @Test
    public void testWithParams() {
        FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder();
        Node expectedParametersNode = functionParamBuilder.build();
        FunctionType functionType = functionBuilder.withParams(functionParamBuilder).build();
        Assert.assertEquals(expectedParametersNode, functionType.getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        Node expectedParametersNode = null;
        FunctionType functionType = functionBuilder.withParamsNode(expectedParametersNode).build();
        Assert.assertEquals(expectedParametersNode, functionType.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSType expectedReturnType = registry.getNativeType(JSTypeNative.NO_TYPE);
        FunctionType functionType = functionBuilder.withReturnType(expectedReturnType).build();
        Assert.assertEquals(expectedReturnType, functionType.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSType expectedReturnType = registry.getNativeType(JSTypeNative.NO_TYPE);
        FunctionType functionType = functionBuilder.withInferredReturnType(expectedReturnType).build();
        Assert.assertEquals(expectedReturnType, functionType.getReturnType());
        Assert.assertTrue(functionType.isReturnTypeInferred());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType expectedTypeOfThis = null;
        FunctionType functionType = functionBuilder.withTypeOfThis(expectedTypeOfThis).build();
        Assert.assertEquals(expectedTypeOfThis, functionType.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        String expectedTemplateName = "Template";
        FunctionType functionType = functionBuilder.withTemplateName(expectedTemplateName).build();
        Assert.assertEquals(expectedTemplateName, functionType.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        boolean expectedIsConstructor = true;
        FunctionType functionType = functionBuilder.forConstructor().build();
        Assert.assertEquals(expectedIsConstructor, functionType.isConstructor());
    }

    @Test
    public void testForNativeType() {
        boolean expectedIsNativeType = true;
        FunctionType functionType = functionBuilder.forNativeType().build();
        Assert.assertEquals(expectedIsNativeType, functionType.isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType expectedFuncType = functionBuilder.withName("TestFunction").build();
        FunctionType functionType = functionBuilder.copyFromOtherFunction(expectedFuncType).build();
        Assert.assertEquals(expectedFuncType.getReferenceName(), functionType.getReferenceName());
        Assert.assertEquals(expectedFuncType.getSource(), functionType.getSource());
        Assert.assertEquals(expectedFuncType.getParametersNode(), functionType.getParametersNode());
        Assert.assertEquals(expectedFuncType.getReturnType(), functionType.getReturnType());
        Assert.assertEquals(expectedFuncType.getTypeOfThis(), functionType.getTypeOfThis());
        Assert.assertEquals(expectedFuncType.getTemplateTypeName(), functionType.getTemplateTypeName());
        Assert.assertEquals(expectedFuncType.isConstructor(), functionType.isConstructor());
        Assert.assertEquals(expectedFuncType.isNativeObjectType(), functionType.isNativeObjectType());
    }

}
