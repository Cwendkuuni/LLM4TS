import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.ArrowType;

public class FunctionBuilderTest {

    private JSTypeRegistry registry = new JSTypeRegistry();

    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        builder.withName("testFunction");
        FunctionType functionType = builder.build();
        Assert.assertEquals("testFunction", functionType.getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        Node sourceNode = new Node();
        builder.withSourceNode(sourceNode);
        FunctionType functionType = builder.build();
        Assert.assertEquals(sourceNode, functionType.getSource());
    }

    @Test
    public void testWithParams() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionParamBuilder params = new FunctionParamBuilder();
        Node parametersNode = params.build();
        builder.withParams(params);
        FunctionType functionType = builder.build();
        Assert.assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        builder.withReturnType(returnType);
        FunctionType functionType = builder.build();
        Assert.assertEquals(returnType, functionType.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        JSType returnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        builder.withInferredReturnType(returnType);
        FunctionType functionType = builder.build();
        Assert.assertEquals(returnType, functionType.getReturnType());
        Assert.assertTrue(functionType.isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        ObjectType typeOfThis = new ObjectType(registry);
        builder.withTypeOfThis(typeOfThis);
        FunctionType functionType = builder.build();
        Assert.assertEquals(typeOfThis, functionType.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        builder.withTemplateName("template");
        FunctionType functionType = builder.build();
        Assert.assertEquals("template", functionType.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        builder.forConstructor();
        FunctionType functionType = builder.build();
        Assert.assertTrue(functionType.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        builder.forNativeType();
        FunctionType functionType = builder.build();
        Assert.assertTrue(functionType.isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType otherType = new FunctionType(registry, "otherFunction", new Node(), new ArrowType(registry), null, null, false, false);
        builder.copyFromOtherFunction(otherType);
        FunctionType functionType = builder.build();
        Assert.assertEquals("otherFunction", functionType.getReferenceName());
        Assert.assertEquals(otherType.getSource(), functionType.getSource());
        Assert.assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
        Assert.assertEquals(otherType.getReturnType(), functionType.getReturnType());
        Assert.assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
        Assert.assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
        Assert.assertEquals(otherType.isConstructor(), functionType.isConstructor());
        Assert.assertEquals(otherType.isNativeObjectType(), functionType.isNativeObjectType());
    }
}