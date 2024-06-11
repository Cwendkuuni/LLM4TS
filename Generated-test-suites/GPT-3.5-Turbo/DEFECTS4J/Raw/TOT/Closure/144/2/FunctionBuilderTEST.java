import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Assert;
import org.junit.Test;

public class FunctionBuilderTest {

    @Test
    public void testWithDefaultValues() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        FunctionType functionType = functionBuilder.build();

        Assert.assertNull(functionType.getName());
        Assert.assertNull(functionType.getSourceNode());
        Assert.assertNull(functionType.getParametersNode());
        Assert.assertNull(functionType.getReturnType());
        Assert.assertNull(functionType.getTypeOfThis());
        Assert.assertNull(functionType.getTemplateTypeName());
        Assert.assertFalse(functionType.isConstructor());
        Assert.assertFalse(functionType.isNativeObjectType());
    }

    @Test
    public void testWithName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        functionBuilder.withName("testFunction");
        FunctionType functionType = functionBuilder.build();

        Assert.assertEquals("testFunction", functionType.getName());
    }

    @Test
    public void testWithSourceNode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        Node sourceNode = new Node();
        functionBuilder.withSourceNode(sourceNode);
        FunctionType functionType = functionBuilder.build();

        Assert.assertEquals(sourceNode, functionType.getSourceNode());
    }

    @Test
    public void testWithParams() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        FunctionParamBuilder params = new FunctionParamBuilder();
        functionBuilder.withParams(params);
        FunctionType functionType = functionBuilder.build();

        Assert.assertEquals(params.build(), functionType.getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        Node parametersNode = new Node();
        functionBuilder.withParamsNode(parametersNode);
        FunctionType functionType = functionBuilder.build();

        Assert.assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        JSType returnType = new JSType();
        functionBuilder.withReturnType(returnType);
        FunctionType functionType = functionBuilder.build();

        Assert.assertEquals(returnType, functionType.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        JSType returnType = new JSType();
        functionBuilder.withInferredReturnType(returnType);
        FunctionType functionType = functionBuilder.build();

        Assert.assertEquals(returnType, functionType.getReturnType());
        Assert.assertTrue(functionType.getInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        ObjectType typeOfThis = new ObjectType();
        functionBuilder.withTypeOfThis(typeOfThis);
        FunctionType functionType = functionBuilder.build();

        Assert.assertEquals(typeOfThis, functionType.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        functionBuilder.withTemplateName("template");
        FunctionType functionType = functionBuilder.build();

        Assert.assertEquals("template", functionType.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        functionBuilder.forConstructor();
        FunctionType functionType = functionBuilder.build();

        Assert.assertTrue(functionType.isConstructor());
    }

    @Test
    public void testForNativeType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        functionBuilder.forNativeType();
        FunctionType functionType = functionBuilder.build();

        Assert.assertTrue(functionType.isNativeObjectType());
    }
}