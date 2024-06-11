import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionBuilderTest {

    private JSTypeRegistry registry;
    private FunctionBuilder functionBuilder;

    @Before
    public void setup() {
        registry = new JSTypeRegistry();
        functionBuilder = new FunctionBuilder(registry);
    }

    @Test
    public void testWithName() {
        String name = "testFunction";
        FunctionType functionType = functionBuilder.withName(name).build();
        assertEquals(name, functionType.getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node("source");
        FunctionType functionType = functionBuilder.withSourceNode(sourceNode).build();
        assertEquals(sourceNode, functionType.getSource());
    }

    @Test
    public void testWithParams() {
        Node parametersNode = new Node("params");
        FunctionType functionType = functionBuilder.withParamsNode(parametersNode).build();
        assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = registry.createBooleanType();
        FunctionType functionType = functionBuilder.withReturnType(returnType).build();
        assertEquals(returnType, functionType.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSType inferredReturnType = registry.createNumberType();
        FunctionType functionType = functionBuilder.withInferredReturnType(inferredReturnType).build();
        assertEquals(inferredReturnType, functionType.getReturnType());
        assertTrue(functionType.isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = new ObjectType();
        FunctionType functionType = functionBuilder.withTypeOfThis(typeOfThis).build();
        assertEquals(typeOfThis, functionType.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        String templateName = "template";
        FunctionType functionType = functionBuilder.withTemplateName(templateName).build();
        assertEquals(templateName, functionType.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionType functionType = functionBuilder.forConstructor().build();
        assertTrue(functionType.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionType functionType = functionBuilder.forNativeType().build();
        assertTrue(functionType.isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = new FunctionType();
        FunctionType functionType = functionBuilder.copyFromOtherFunction(otherType).build();
        assertSame(otherType.getReferenceName(), functionType.getReferenceName());
        assertSame(otherType.getSource(), functionType.getSource());
        assertSame(otherType.getParametersNode(), functionType.getParametersNode());
        assertSame(otherType.getReturnType(), functionType.getReturnType());
        assertSame(otherType.getTypeOfThis(), functionType.getTypeOfThis());
        assertSame(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
        assertSame(otherType.isConstructor(), functionType.isConstructor());
        assertSame(otherType.isNativeObjectType(), functionType.isNativeObjectType());
    }

}