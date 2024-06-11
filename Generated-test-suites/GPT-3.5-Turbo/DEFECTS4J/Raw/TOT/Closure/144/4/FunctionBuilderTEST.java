import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Assert;
import org.junit.Test;

public class FunctionBuilderTest {

    // Test case for withName method
    @Test
    public void testWithName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        functionBuilder.withName("testFunction");
        FunctionType functionType = functionBuilder.build();
        Assert.assertEquals("testFunction", functionType.getReferenceName());
    }

    // Test case for withSourceNode method
    @Test
    public void testWithSourceNode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        Node sourceNode = new Node(Token.SCRIPT);
        functionBuilder.withSourceNode(sourceNode);
        FunctionType functionType = functionBuilder.build();
        Assert.assertEquals(sourceNode, functionType.getSource());
    }

    // Test case for withParams method
    @Test
    public void testWithParams() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        FunctionParamBuilder params = new FunctionParamBuilder();
        params.withParam("param1", registry.getNativeType(JSTypeNative.STRING_TYPE));
        functionBuilder.withParams(params);
        FunctionType functionType = functionBuilder.build();
        Assert.assertEquals(params.build(), functionType.getParametersNode());
    }

    // Test case for withParamsNode method
    @Test
    public void testWithParamsNode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        Node parametersNode = new Node(Token.PARAM_LIST);
        functionBuilder.withParamsNode(parametersNode);
        FunctionType functionType = functionBuilder.build();
        Assert.assertEquals(parametersNode, functionType.getParametersNode());
    }

    // Test case for withReturnType method
    @Test
    public void testWithReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        functionBuilder.withReturnType(returnType);
        FunctionType functionType = functionBuilder.build();
        Assert.assertEquals(returnType, functionType.getReturnType());
    }

    // Test case for withInferredReturnType method
    @Test
    public void testWithInferredReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        JSType returnType = registry.getNativeType(JSTypeNative.BOOLEAN_TYPE);
        functionBuilder.withInferredReturnType(returnType);
        FunctionType functionType = functionBuilder.build();
        Assert.assertEquals(returnType, functionType.getReturnType());
        Assert.assertTrue(functionType.isInferredReturnType());
    }

    // Test case for withTypeOfThis method
    @Test
    public void testWithTypeOfThis() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        ObjectType typeOfThis = new ObjectType();
        functionBuilder.withTypeOfThis(typeOfThis);
        FunctionType functionType = functionBuilder.build();
        Assert.assertEquals(typeOfThis, functionType.getTypeOfThis());
    }

    // Test case for withTemplateName method
    @Test
    public void testWithTemplateName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        functionBuilder.withTemplateName("TemplateType");
        FunctionType functionType = functionBuilder.build();
        Assert.assertEquals("TemplateType", functionType.getTemplateTypeName());
    }

    // Test case for forConstructor method
    @Test
    public void testForConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        functionBuilder.forConstructor();
        FunctionType functionType = functionBuilder.build();
        Assert.assertTrue(functionType.isConstructor());
    }

    // Test case for forNativeType method
    @Test
    public void testForNativeType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        functionBuilder.forNativeType();
        FunctionType functionType = functionBuilder.build();
        Assert.assertTrue(functionType.isNativeObjectType());
    }

    // Test case for copyFromOtherFunction method
    @Test
    public void testCopyFromOtherFunction() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        FunctionType otherType = new FunctionType();
        otherType.setReferenceName("otherFunction");
        otherType.setSource(new Node(Token.SCRIPT));
        FunctionParamBuilder params = new FunctionParamBuilder();
        params.withParam("param1", registry.getNativeType(JSTypeNative.STRING_TYPE));
        otherType.setParametersNode(params.build());
        otherType.setReturnType(registry.getNativeType(JSTypeNative.VOID_TYPE));
        ObjectType typeOfThis = new ObjectType();
        otherType.setTypeOfThis(typeOfThis);
        otherType.setTemplateTypeName("OtherTemplateType");
        otherType.setConstructor(true);
        otherType.setNativeObjectType(true);

        functionBuilder.copyFromOtherFunction(otherType);
        FunctionType functionType = functionBuilder.build();
        Assert.assertEquals(otherType.getReferenceName(), functionType.getReferenceName());
        Assert.assertEquals(otherType.getSource(), functionType.getSource());
        Assert.assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
        Assert.assertEquals(otherType.getReturnType(), functionType.getReturnType());
        Assert.assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
        Assert.assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
        Assert.assertEquals(otherType.isConstructor(), functionType.isConstructor());
        Assert.assertEquals(otherType.isNativeObjectType(), functionType.isNativeObjectType());
    }

    // Test case for build method
    @Test
    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        FunctionType functionType = functionBuilder.build();
        Assert.assertNotNull(functionType);
    }
}