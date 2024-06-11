import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {

    private FunctionBuilder functionBuilder;
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        functionBuilder = new FunctionBuilder(registry);
    }

    @Test
    public void testWithName() {
        String name = "testFunction";
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
        Node parametersNode = new Node();
        FunctionBuilder result = functionBuilder.withParamsNode(parametersNode);
        
        assertEquals(parametersNode, result.build().getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder result = functionBuilder.withReturnType(registry.getNativeType(JSTypeRegistry.JSTYPE_VOID));

        assertEquals(registry.getNativeType(JSTypeRegistry.JSTYPE_VOID), result.build().getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder result = functionBuilder.withInferredReturnType(registry.getNativeType(JSTypeRegistry.JSTYPE_VOID));

        assertTrue(result.build().isReturnTypeInferred());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = new ObjectType();
        FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis);

        assertEquals(typeOfThis, result.build().getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        String templateTypeName = "Template";
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
        FunctionType otherType = new FunctionType();
        otherType.setReferenceName("otherFunction");
        otherType.setSource(new Node());
        otherType.setParametersNode(new Node());
        otherType.setReturnType(registry.getNativeType(JSTypeRegistry.JSTYPE_VOID));
        otherType.setTypeOfThis(new ObjectType());
        otherType.setTemplateTypeName("Template");
        otherType.setConstructor(true);
        otherType.setNativeObjectType(true);

        FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
        FunctionType newFunction = result.build();

        assertEquals("otherFunction", newFunction.getReferenceName());
        assertEquals(otherType.getSource(), newFunction.getSource());
        assertEquals(otherType.getParametersNode(), newFunction.getParametersNode());
        assertEquals(otherType.getReturnType(), newFunction.getReturnType());
        assertEquals(otherType.getTypeOfThis(), newFunction.getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), newFunction.getTemplateTypeName());
        assertTrue(newFunction.isConstructor());
        assertTrue(newFunction.isNativeObjectType());
    }

    @Test
    public void testBuild() {
        FunctionType function = functionBuilder.withName("testFunction")
                .withSourceNode(new Node())
                .withParamsNode(new Node())
                .withReturnType(registry.getNativeType(JSTypeRegistry.JSTYPE_VOID))
                .withTypeOfThis(new ObjectType())
                .withTemplateName("Template")
                .forConstructor()
                .forNativeType()
                .build();

        assertEquals("testFunction", function.getReferenceName());
        assertNotNull(function.getSource());
        assertNotNull(function.getParametersNode());
        assertEquals(registry.getNativeType(JSTypeRegistry.JSTYPE_VOID), function.getReturnType());
        assertNotNull(function.getTypeOfThis());
        assertEquals("Template", function.getTemplateTypeName());
        assertTrue(function.isConstructor());
        assertTrue(function.isNativeObjectType());
    }
}