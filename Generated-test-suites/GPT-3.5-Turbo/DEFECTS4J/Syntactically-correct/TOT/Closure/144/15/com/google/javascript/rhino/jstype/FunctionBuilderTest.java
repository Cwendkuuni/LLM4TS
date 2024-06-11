import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Assert;
import org.junit.Test;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);

        FunctionType functionType = builder.withName("myFunction").build();

        Assert.assertEquals("myFunction", functionType.getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);

        Node sourceNode = new Node();
        FunctionType functionType = builder.withSourceNode(sourceNode).build();

        Assert.assertEquals(sourceNode, functionType.getSource());
    }

    @Test
    public void testWithParams() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);

        FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
        Node paramsNode = paramBuilder.build();

        FunctionType functionType = builder.withParams(paramBuilder).build();

        Assert.assertEquals(paramsNode, functionType.getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);

        Node parametersNode = new Node();
        FunctionType functionType = builder.withParamsNode(parametersNode).build();

        Assert.assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);

        JSType returnType = new JSType();
        FunctionType functionType = builder.withReturnType(returnType).build();

        Assert.assertEquals(returnType, functionType.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);

        JSType returnType = new JSType();
        FunctionType functionType = builder.withInferredReturnType(returnType).build();

        Assert.assertEquals(returnType, functionType.getReturnType());
        Assert.assertTrue(functionType.isReturnTypeInferred());
    }

    @Test
    public void testWithTypeOfThis() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);

        ObjectType typeOfThis = new ObjectType();
        FunctionType functionType = builder.withTypeOfThis(typeOfThis).build();

        Assert.assertEquals(typeOfThis, functionType.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);

        String templateTypeName = "template";
        FunctionType functionType = builder.withTemplateName(templateTypeName).build();

        Assert.assertEquals(templateTypeName, functionType.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);

        FunctionType functionType = builder.forConstructor().build();

        Assert.assertTrue(functionType.isConstructor());
    }

    @Test
    public void testForNativeType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);

        FunctionType functionType = builder.forNativeType().build();

        Assert.assertTrue(functionType.isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);

        FunctionType otherFunctionType = new FunctionType(registry, "otherFunction",
                new Node(), new JSType(), new ObjectType(), "", true, false);

        FunctionType functionType = builder.copyFromOtherFunction(otherFunctionType).build();

        Assert.assertEquals("otherFunction", functionType.getReferenceName());
        Assert.assertEquals(otherFunctionType.getSource(), functionType.getSource());
        Assert.assertEquals(otherFunctionType.getParametersNode(), functionType.getParametersNode());
        Assert.assertEquals(otherFunctionType.getReturnType(), functionType.getReturnType());
        Assert.assertEquals(otherFunctionType.getTypeOfThis(), functionType.getTypeOfThis());
        Assert.assertEquals(otherFunctionType.getTemplateTypeName(), functionType.getTemplateTypeName());
        Assert.assertTrue(functionType.isConstructor());
        Assert.assertFalse(functionType.isNativeObjectType());
    }

    @Test
    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);

        FunctionType functionType = builder.build();

        Assert.assertNotNull(functionType);
    }
}