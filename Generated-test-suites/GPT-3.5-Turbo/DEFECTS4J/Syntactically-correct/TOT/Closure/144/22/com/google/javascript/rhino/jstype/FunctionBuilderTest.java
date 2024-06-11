import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.jstype.*;

public class FunctionBuilderTest {

    @Test
    public void testName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType function = builder.withName("testFunction").build();
        assertEquals("testFunction", function.getReferenceName());
    }

    @Test
    public void testSourceNode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);
        Node sourceNode = new Node();
        FunctionType function = builder.withSourceNode(sourceNode).build();
        assertEquals(sourceNode, function.getSource());
    }

    @Test
    public void testParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionParamBuilder params = new FunctionParamBuilder()
                                        .addParam("param1", registry.getNativeType(JSTypeNative.NUMBER_TYPE))
                                        .addParam("param2", registry.getNativeType(JSTypeNative.STRING_TYPE));
        FunctionType function = builder.withParams(params).build();
        assertEquals(params.build(), function.getParametersNode());
    }

    @Test
    public void testReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);
        JSType returnType = registry.getNativeType(JSTypeNative.BOOLEAN_TYPE);
        FunctionType function = builder.withReturnType(returnType).build();
        assertEquals(returnType, function.getReturnType());
    }

    @Test
    public void testInferredReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        FunctionType function = builder.withInferredReturnType(returnType).build();
        assertTrue(function.isInferredReturnType());
        assertEquals(returnType, function.getReturnType());
    }

    @Test
    public void testTypeOfThis() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);
        ObjectType typeOfThis = new ObjectType(registry, "ThisType");
        FunctionType function = builder.withTypeOfThis(typeOfThis).build();
        assertEquals(typeOfThis, function.getTypeOfThis());
    }

    @Test
    public void testTemplateName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);
        String templateTypeName = "Template";
        FunctionType function = builder.withTemplateName(templateTypeName).build();
        assertEquals(templateTypeName, function.getTemplateTypeName());
    }

    @Test
    public void testIsConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType function = builder.forConstructor().build();
        assertTrue(function.isConstructor());
    }

    @Test
    public void testIsNativeType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType function = builder.forNativeType().build();
        assertTrue(function.isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType otherType = new FunctionType(registry, "otherFunction", null,
                                        new ArrowType(registry, null, null, false),
                                        null, null, false, true);
        FunctionType function = builder.copyFromOtherFunction(otherType).build();
        assertEquals("otherFunction", function.getReferenceName());
        assertTrue(function.isNativeObjectType());
    }

    @Test
    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType function = builder.build();
        assertNotNull(function);
    }

}