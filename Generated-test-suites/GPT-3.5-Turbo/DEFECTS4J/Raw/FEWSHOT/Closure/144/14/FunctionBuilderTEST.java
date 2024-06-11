import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Test;

public class FunctionBuilderTest {
    private static final JSTypeRegistry registry = new JSTypeRegistry();

    @Test
    public void testWithName() {
        FunctionType result = new FunctionBuilder(registry).withName("testName").build();
        assertEquals("testName", result.getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node();
        FunctionType result = new FunctionBuilder(registry).withSourceNode(sourceNode).build();
        assertEquals(sourceNode, result.getSource());
    }

    @Test
    public void testWithParams() {
        Node parametersNode = new Node();
        FunctionType result = new FunctionBuilder(registry).withParamsNode(parametersNode).build();
        assertEquals(parametersNode, result.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = registry.getType("ReturnType");
        FunctionType result = new FunctionBuilder(registry).withReturnType(returnType).build();
        assertEquals(returnType, result.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSType returnType = registry.getType("ReturnType");
        FunctionType result = new FunctionBuilder(registry).withInferredReturnType(returnType).build();
        assertEquals(returnType, result.getReturnType());
        assertEquals(true, result.isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = registry.getObjectType("TypeOfThis");
        FunctionType result = new FunctionBuilder(registry).withTypeOfThis(typeOfThis).build();
        assertEquals(typeOfThis, result.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        FunctionType result = new FunctionBuilder(registry).withTemplateName("templateName").build();
        assertEquals("templateName", result.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionType result = new FunctionBuilder(registry).forConstructor().build();
        assertEquals(true, result.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionType result = new FunctionBuilder(registry).forNativeType().build();
        assertEquals(true, result.isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = new FunctionType(registry, "otherName", new Node(), 
            new ArrowType(registry, new Node(), registry.getType("OtherReturnType"), false), 
            registry.getObjectType("OtherTypeOfThis"), "otherTemplateName", true, false);
        FunctionType result = new FunctionBuilder(registry).copyFromOtherFunction(otherType).build();
        assertEquals("otherName", result.getReferenceName());
        assertEquals(otherType.getSource(), result.getSource());
        assertEquals(otherType.getParametersNode(), result.getParametersNode());
        assertEquals(otherType.getReturnType(), result.getReturnType());
        assertEquals(otherType.getTypeOfThis(), result.getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), result.getTemplateTypeName());
        assertEquals(otherType.isConstructor(), result.isConstructor());
        assertEquals(otherType.isNativeObjectType(), result.isNativeObjectType());
    }
}