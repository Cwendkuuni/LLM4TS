import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        String name = "myFunction";
        FunctionBuilder result = functionBuilder.withName(name);
        assertEquals(result, functionBuilder);
        assertEquals(name, functionBuilder.build().getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node();
        FunctionBuilder result = functionBuilder.withSourceNode(sourceNode);
        assertEquals(result, functionBuilder);
        assertEquals(sourceNode, functionBuilder.build().getSource());
    }

    @Test
    public void testWithParams() {
        FunctionParamBuilder params = new FunctionParamBuilder(registry);
        Node parametersNode = params.build();
        FunctionBuilder result = functionBuilder.withParams(params);
        assertEquals(result, functionBuilder);
        assertEquals(parametersNode, functionBuilder.build().getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        Node parametersNode = new Node();
        FunctionBuilder result = functionBuilder.withParamsNode(parametersNode);
        assertEquals(result, functionBuilder);
        assertEquals(parametersNode, functionBuilder.build().getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = registry.getNativeType(JSTypeNative.VOID_TYPE);
        FunctionBuilder result = functionBuilder.withReturnType(returnType);
        assertEquals(result, functionBuilder);
        assertEquals(returnType, functionBuilder.build().getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSType returnType = registry.getNativeType(JSTypeNative.VOID_TYPE);
        FunctionBuilder result = functionBuilder.withInferredReturnType(returnType);
        assertEquals(result, functionBuilder);
        assertEquals(returnType, functionBuilder.build().getReturnType());
        assertTrue(functionBuilder.build().isReturnTypeInferred());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = new ObjectType();
        FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis);
        assertEquals(result, functionBuilder);
        assertEquals(typeOfThis, functionBuilder.build().getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        String templateTypeName = "MyTemplate";
        FunctionBuilder result = functionBuilder.withTemplateName(templateTypeName);
        assertEquals(result, functionBuilder);
        assertEquals(templateTypeName, functionBuilder.build().getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder result = functionBuilder.forConstructor();
        assertEquals(result, functionBuilder);
        assertTrue(functionBuilder.build().isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder result = functionBuilder.forNativeType();
        assertEquals(result, functionBuilder);
        assertTrue(functionBuilder.build().isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = new FunctionType();
        functionBuilder.copyFromOtherFunction(otherType);
        assertEquals(otherType.getReferenceName(), functionBuilder.build().getReferenceName());
        assertEquals(otherType.getSource(), functionBuilder.build().getSource());
        assertEquals(otherType.getParametersNode(), functionBuilder.build().getParametersNode());
        assertEquals(otherType.getReturnType(), functionBuilder.build().getReturnType());
        assertEquals(otherType.getTypeOfThis(), functionBuilder.build().getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), functionBuilder.build().getTemplateTypeName());
        assertEquals(otherType.isConstructor(), functionBuilder.build().isConstructor());
        assertEquals(otherType.isNativeObjectType(), functionBuilder.build().isNativeObjectType());
    }

    @Test
    public void testBuild() {
        FunctionType functionType = functionBuilder.build();
        assertNotNull(functionType);
    }
}