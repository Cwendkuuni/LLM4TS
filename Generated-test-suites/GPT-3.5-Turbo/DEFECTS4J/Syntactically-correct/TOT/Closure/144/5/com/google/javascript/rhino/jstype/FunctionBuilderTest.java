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
        String functionName = "testFunction";
        FunctionBuilder result = functionBuilder.withName(functionName);
        assertEquals(functionName, result.build().getName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node();
        FunctionBuilder result = functionBuilder.withSourceNode(sourceNode);
        assertEquals(sourceNode, result.build().getSourceNode());
    }

    @Test
    public void testWithParams() {
        Node parametersNode = new Node();
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder(parametersNode);
        FunctionBuilder result = functionBuilder.withParams(paramBuilder);
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
        String templateName = "Template";
        FunctionBuilder result = functionBuilder.withTemplateName(templateName);
        assertEquals(templateName, result.build().getTemplateTypeName());
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
        FunctionType otherType = new FunctionType(registry);
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
        FunctionType functionType = functionBuilder.build();
        assertNotNull(functionType);
    }
}