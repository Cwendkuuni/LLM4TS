import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FunctionBuilderTest {
    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(null);
        builder.withName("test");
        assertEquals("test", builder.build().getName());
    }

    @Test
    public void testWithSourceNode() {
        FunctionBuilder builder = new FunctionBuilder(null);
        Node sourceNode = new Node();
        builder.withSourceNode(sourceNode);
        assertEquals(sourceNode, builder.build().getSourceNode());
    }

    @Test
    public void testWithParams() {
        FunctionBuilder builder = new FunctionBuilder(null);
        builder.withParams(new FunctionParamBuilder(null));
        assertEquals(new Node(), builder.build().getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        FunctionBuilder builder = new FunctionBuilder(null);
        Node parametersNode = new Node();
        builder.withParamsNode(parametersNode);
        assertEquals(parametersNode, builder.build().getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder(null);
        JSType returnType = new JSType();
        builder.withReturnType(returnType);
        assertEquals(returnType, builder.build().getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder builder = new FunctionBuilder(null);
        JSType returnType = new JSType();
        builder.withInferredReturnType(returnType);
        assertEquals(returnType, builder.build().getReturnType());
        assertEquals(true, builder.build().isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder builder = new FunctionBuilder(null);
        ObjectType typeOfThis = new ObjectType();
        builder.withTypeOfThis(typeOfThis);
        assertEquals(typeOfThis, builder.build().getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        FunctionBuilder builder = new FunctionBuilder(null);
        builder.withTemplateName("template");
        assertEquals("template", builder.build().getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder builder = new FunctionBuilder(null);
        builder.forConstructor();
        assertEquals(true, builder.build().isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder builder = new FunctionBuilder(null);
        builder.forNativeType();
        assertEquals(true, builder.build().isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionType otherType = new FunctionType(
            registry, "test", new Node(), new ArrowType(registry, new Node(), new JSType(), false),
            new ObjectType(), "template", true, false
        );

        FunctionBuilder builder = new FunctionBuilder(null);
        builder.copyFromOtherFunction(otherType);
        FunctionType builtType = builder.build();

        assertEquals("test", builtType.getName());
        assertEquals(registry, builtType.getRegistry());
        assertEquals(otherType.getSourceNode(), builtType.getSourceNode());
        assertEquals(otherType.getParametersNode(), builtType.getParametersNode());
        assertEquals(otherType.getReturnType(), builtType.getReturnType());
        assertEquals(otherType.getTypeOfThis(), builtType.getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), builtType.getTemplateTypeName());
        assertEquals(otherType.isConstructor(), builtType.isConstructor());
        assertEquals(otherType.isNativeObjectType(), builtType.isNativeObjectType());
    }
}