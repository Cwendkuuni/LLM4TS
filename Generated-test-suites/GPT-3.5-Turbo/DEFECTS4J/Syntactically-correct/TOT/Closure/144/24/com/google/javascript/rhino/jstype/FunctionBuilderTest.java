import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

    private JSTypeRegistry registry = new JSTypeRegistry();

    @Test
    public void testWithParams() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        Node params = new Node(Token.PARAM_LIST);
        builder.withParamsNode(params);
        FunctionType functionType = builder.build();
        assertSame(params, functionType.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        JSType returnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        builder.withReturnType(returnType);
        FunctionType functionType = builder.build();
        assertSame(returnType, functionType.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        JSType returnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        builder.withInferredReturnType(returnType);
        FunctionType functionType = builder.build();
        assertSame(returnType, functionType.getReturnType());
        assertTrue(functionType.isReturnTypeInferred());
    }

    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        ObjectType typeOfThis = new ObjectType(registry, "ThisType");
        builder.withTypeOfThis(typeOfThis);
        FunctionType functionType = builder.build();
        assertSame(typeOfThis, functionType.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        String templateName = "TemplateName";
        builder.withTemplateName(templateName);
        FunctionType functionType = builder.build();
        assertEquals(templateName, functionType.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        builder.forConstructor();
        FunctionType functionType = builder.build();
        assertTrue(functionType.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        builder.forNativeType();
        FunctionType functionType = builder.build();
        assertTrue(functionType.isNativeType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType otherType = createOtherFunctionType();
        builder.copyFromOtherFunction(otherType);
        FunctionType functionType = builder.build();
        assertSame(otherType.getReferenceName(), functionType.getReferenceName());
        assertSame(otherType.getSource(), functionType.getSource());
        assertSame(otherType.getParametersNode(), functionType.getParametersNode());
        assertSame(otherType.getReturnType(), functionType.getReturnType());
        assertSame(otherType.getTypeOfThis(), functionType.getTypeOfThis());
        assertSame(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
        assertEquals(otherType.isConstructor(), functionType.isConstructor());
        assertEquals(otherType.isNativeObjectType(), functionType.isNativeObjectType());
    }

    private FunctionType createOtherFunctionType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        builder.withName("otherFunction");
        builder.withSourceNode(new Node(Token.SOURCE_NAME, "source.js"));
        Node params = new Node(Token.PARAM_LIST);
        builder.withParamsNode(params);
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        builder.withReturnType(returnType);
        ObjectType typeOfThis = new ObjectType(registry, "ThisType");
        builder.withTypeOfThis(typeOfThis);
        builder.withTemplateName("OtherTemplate");
        builder.forConstructor();
        builder.forNativeType();
        return builder.build();
    }

    @Test
    public void testCompleteBuild() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        builder.withName("completeFunction");
        builder.withSourceNode(new Node(Token.SOURCE_NAME, "source.js"));
        Node params = new Node(Token.PARAM_LIST);
        builder.withParamsNode(params);
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        builder.withReturnType(returnType);
        ObjectType typeOfThis = new ObjectType(registry, "ThisType");
        builder.withTypeOfThis(typeOfThis);
        builder.withTemplateName("CompleteTemplate");
        builder.forConstructor();

        FunctionType functionType = builder.build();
        assertEquals("completeFunction", functionType.getReferenceName());
        assertSame(Token.SOURCE_NAME, functionType.getSource().getType());
        assertSame(params, functionType.getParametersNode());
        assertSame(returnType, functionType.getReturnType());
        assertSame(typeOfThis, functionType.getTypeOfThis());
        assertEquals("CompleteTemplate", functionType.getTemplateTypeName());
        assertTrue(functionType.isConstructor());
        assertFalse(functionType.isNativeObjectType());
    }

}