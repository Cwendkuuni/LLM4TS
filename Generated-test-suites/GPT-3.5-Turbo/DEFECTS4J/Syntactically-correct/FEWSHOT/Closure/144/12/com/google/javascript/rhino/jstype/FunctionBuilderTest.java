import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

    private JSTypeRegistry registry;
    private FunctionBuilder builder;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        builder = new FunctionBuilder(registry);
    }

    @Test
    public void testWithName() {
        builder.withName("testName");
        assertEquals("testName", builder.build().getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node();
        builder.withSourceNode(sourceNode);
        assertEquals(sourceNode, builder.build().getSource());
    }

    @Test
    public void testWithParams() {
        FunctionParamBuilder params = new FunctionParamBuilder(registry);
        Node parametersNode = params.build();
        builder.withParams(params);
        assertEquals(parametersNode, builder.build().getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        Node parametersNode = new Node();
        builder.withParamsNode(parametersNode);
        assertEquals(parametersNode, builder.build().getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        builder.withReturnType(returnType);
        assertEquals(returnType, builder.build().getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSType returnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        builder.withInferredReturnType(returnType);
        assertEquals(returnType, builder.build().getReturnType());
        assertTrue(builder.build().isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        builder.withTypeOfThis(typeOfThis);
        assertEquals(typeOfThis, builder.build().getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        builder.withTemplateName("templateName");
        assertEquals("templateName", builder.build().getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        builder.forConstructor();
        assertTrue(builder.build().isConstructor());
    }

    @Test
    public void testForNativeType() {
        builder.forNativeType();
        assertTrue(builder.build().isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = new FunctionType(registry, "otherType");
        builder.copyFromOtherFunction(otherType);
        assertEquals(otherType.getReferenceName(), builder.build().getReferenceName());
        assertEquals(otherType.getSource(), builder.build().getSource());
        assertEquals(otherType.getParametersNode(), builder.build().getParametersNode());
        assertEquals(otherType.getReturnType(), builder.build().getReturnType());
        assertEquals(otherType.getTypeOfThis(), builder.build().getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), builder.build().getTemplateTypeName());
        assertEquals(otherType.isConstructor(), builder.build().isConstructor());
        assertEquals(otherType.isNativeObjectType(), builder.build().isNativeObjectType());
    }

    @Test
    public void testBuild() {
        FunctionType functionType = builder.build();
        assertNull(functionType.getReferenceName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeObjectType());
    }
}