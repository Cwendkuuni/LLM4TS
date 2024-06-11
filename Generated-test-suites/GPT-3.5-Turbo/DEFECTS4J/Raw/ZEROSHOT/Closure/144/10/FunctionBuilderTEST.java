import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionBuilderTest {

    private FunctionBuilder functionBuilder;

    @Before
    public void setUp() {
        functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    }

    @Test
    public void testWithName() {
        String name = "testName";
        FunctionBuilder result = functionBuilder.withName(name);
        assertEquals(name, result.build().getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node();
        FunctionBuilder result = functionBuilder.withSourceNode(sourceNode);
        assertEquals(sourceNode, result.build().getSource());
    }

    @Test
    public void testWithParams() {
        FunctionParamBuilder params = new FunctionParamBuilder();
        Node parametersNode = params.build();
        FunctionBuilder result = functionBuilder.withParams(params);
        assertEquals(parametersNode, result.build().getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        Node parametersNode = new Node();
        FunctionBuilder result = functionBuilder.withParamsNode(parametersNode);
        assertEquals(parametersNode, result.build().getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = new JSType();
        FunctionBuilder result = functionBuilder.withReturnType(returnType);
        assertEquals(returnType, result.build().getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSType returnType = new JSType();
        FunctionBuilder result = functionBuilder.withInferredReturnType(returnType);
        assertEquals(returnType, result.build().getReturnType());
        assertTrue(result.build().isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = new ObjectType();
        FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis);
        assertEquals(typeOfThis, result.build().getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        String templateTypeName = "testTemplate";
        FunctionBuilder result = functionBuilder.withTemplateName(templateTypeName);
        assertEquals(templateTypeName, result.build().getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder result = functionBuilder.forConstructor();
        assertTrue(result.build().isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder result = functionBuilder.forNativeType();
        assertTrue(result.build().isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = new FunctionType(new JSTypeRegistry());
        FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
        assertEquals(otherType.getReferenceName(), result.build().getReferenceName());
        assertEquals(otherType.getSource(), result.build().getSource());
        assertEquals(otherType.getParametersNode(), result.build().getParametersNode());
        assertEquals(otherType.getReturnType(), result.build().getReturnType());
        assertEquals(otherType.getTypeOfThis(), result.build().getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), result.build().getTemplateTypeName());
        assertEquals(otherType.isConstructor(), result.build().isConstructor());
        assertEquals(otherType.isNativeObjectType(), result.build().isNativeObjectType());
    }

    @Test
    public void testBuild() {
        FunctionType result = functionBuilder.build();
        assertNotNull(result);
    }
}