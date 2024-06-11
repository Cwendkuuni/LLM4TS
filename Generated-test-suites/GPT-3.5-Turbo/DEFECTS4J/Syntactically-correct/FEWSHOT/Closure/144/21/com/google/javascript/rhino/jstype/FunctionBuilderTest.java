import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder fb = new FunctionBuilder(null);
        fb.withName("test");
        assertEquals("test", fb.build().getName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node();
        FunctionBuilder fb = new FunctionBuilder(null);
        fb.withSourceNode(sourceNode);
        assertEquals(sourceNode, fb.build().getSourceNode());
    }

    @Test
    public void testWithParams() {
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder(null);
        Node parametersNode = paramBuilder.build();
        FunctionBuilder fb = new FunctionBuilder(null);
        fb.withParams(paramBuilder);
        assertEquals(parametersNode, fb.build().getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        Node parametersNode = new Node();
        FunctionBuilder fb = new FunctionBuilder(null);
        fb.withParamsNode(parametersNode);
        assertEquals(parametersNode, fb.build().getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = new JSType();
        FunctionBuilder fb = new FunctionBuilder(null);
        fb.withReturnType(returnType);
        assertEquals(returnType, fb.build().getReturnType());
    }
    
    @Test
    public void testWithInferredReturnType() {
        JSType returnType = new JSType();
        FunctionBuilder fb = new FunctionBuilder(null);
        fb.withInferredReturnType(returnType);
        assertEquals(returnType, fb.build().getReturnType());
        assertTrue(fb.build().isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = new ObjectType();
        FunctionBuilder fb = new FunctionBuilder(null);
        fb.withTypeOfThis(typeOfThis);
        assertEquals(typeOfThis, fb.build().getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        String templateName = "template";
        FunctionBuilder fb = new FunctionBuilder(null);
        fb.withTemplateName(templateName);
        assertEquals(templateName, fb.build().getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder fb = new FunctionBuilder(null);
        fb.forConstructor();
        assertTrue(fb.build().isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder fb = new FunctionBuilder(null);
        fb.forNativeType();
        assertTrue(fb.build().isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = new FunctionType();
        FunctionBuilder fb = new FunctionBuilder(null);
        fb.copyFromOtherFunction(otherType);
        assertEquals(otherType.getReferenceName(), fb.build().getName());
        assertEquals(otherType.getSource(), fb.build().getSourceNode());
        assertEquals(otherType.getParametersNode(), fb.build().getParametersNode());
        assertEquals(otherType.getReturnType(), fb.build().getReturnType());
        assertEquals(otherType.getTypeOfThis(), fb.build().getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), fb.build().getTemplateTypeName());
        assertEquals(otherType.isConstructor(), fb.build().isConstructor());
        assertEquals(otherType.isNativeObjectType(), fb.build().isNativeObjectType());
    }
}