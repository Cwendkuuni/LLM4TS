import static org.junit.Assert.*;
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
    public void testName() {
        FunctionBuilder builder = functionBuilder.withName("testName");
        assertEquals("testName", builder.build().getReferenceName());
    }

    @Test
    public void testSourceNode() {
        Node sourceNode = new Node();
        FunctionBuilder builder = functionBuilder.withSourceNode(sourceNode);
        assertEquals(sourceNode, builder.build().getSource());
    }

    @Test
    public void testParams() {
        Node parametersNode = new Node();
        FunctionBuilder builder = functionBuilder.withParamsNode(parametersNode);
        assertEquals(parametersNode, builder.build().getParametersNode());
    }

    @Test
    public void testReturnType() {
        JSType returnType = new JSType();
        FunctionBuilder builder = functionBuilder.withReturnType(returnType);
        assertEquals(returnType, builder.build().getReturnType());
    }

    @Test
    public void testInferredReturnType() {
        JSType returnType = new JSType();
        FunctionBuilder builder = functionBuilder.withInferredReturnType(returnType);
        assertTrue(builder.build().isInferredReturnType());
        assertEquals(returnType, builder.build().getReturnType());
    }

    @Test
    public void testTypeOfThis() {
        ObjectType typeOfThis = new ObjectType();
        FunctionBuilder builder = functionBuilder.withTypeOfThis(typeOfThis);
        assertEquals(typeOfThis, builder.build().getTypeOfThis());
    }

    @Test
    public void testTemplateName() {
        String templateTypeName = "template";
        FunctionBuilder builder = functionBuilder.withTemplateName(templateTypeName);
        assertEquals(templateTypeName, builder.build().getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder builder = functionBuilder.forConstructor();
        assertTrue(builder.build().isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder builder = functionBuilder.forNativeType();
        assertTrue(builder.build().isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = new FunctionType(registry, "other", new Node(), new ArrowType(registry), new ObjectType(), "template", true, true);
        FunctionBuilder builder = functionBuilder.copyFromOtherFunction(otherType);
        assertEquals("other", builder.build().getReferenceName());
        assertEquals(otherType.getSource(), builder.build().getSource());
        assertEquals(otherType.getParametersNode(), builder.build().getParametersNode());
        assertEquals(otherType.getReturnType(), builder.build().getReturnType());
        assertEquals(otherType.getTypeOfThis(), builder.build().getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), builder.build().getTemplateTypeName());
        assertEquals(otherType.isConstructor(), builder.build().isConstructor());
        assertEquals(otherType.isNativeObjectType(), builder.build().isNativeObjectType());
    }
}