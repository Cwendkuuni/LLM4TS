
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;

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
        FunctionBuilder builderWithName = functionBuilder.withName("testName");
        assertEquals("testName", builderWithName.getName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node("testSourceNode");
        FunctionBuilder builderWithSourceNode = functionBuilder.withSourceNode(sourceNode);
        assertEquals(sourceNode, builderWithSourceNode.getSourceNode());
    }

    @Test
    public void testWithParams() {
        FunctionParamBuilder paramsBuilder = new FunctionParamBuilder(registry);
        Node parametersNode = paramsBuilder.build();
        FunctionBuilder builderWithParams = functionBuilder.withParams(paramsBuilder);
        assertEquals(parametersNode, builderWithParams.getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        Node parametersNode = new Node("testParametersNode");
        FunctionBuilder builderWithParamsNode = functionBuilder.withParamsNode(parametersNode);
        assertEquals(parametersNode, builderWithParamsNode.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = new JSType(registry);
        FunctionBuilder builderWithReturnType = functionBuilder.withReturnType(returnType);
        assertEquals(returnType, builderWithReturnType.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSType returnType = new JSType(registry);
        FunctionBuilder builderWithInferredReturnType = functionBuilder.withInferredReturnType(returnType);
        assertEquals(returnType, builderWithInferredReturnType.getReturnType());
        assertTrue(builderWithInferredReturnType.isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = new ObjectType(registry);
        FunctionBuilder builderWithTypeOfThis = functionBuilder.withTypeOfThis(typeOfThis);
        assertEquals(typeOfThis, builderWithTypeOfThis.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        FunctionBuilder builderWithTemplateName = functionBuilder.withTemplateName("testTemplateName");
        assertEquals("testTemplateName", builderWithTemplateName.getTemplateName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder builderForConstructor = functionBuilder.forConstructor();
        assertTrue(builderForConstructor.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder builderForNativeType = functionBuilder.forNativeType();
        assertTrue(builderForNativeType.isNativeType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = new FunctionType(registry, "otherType");
        FunctionBuilder builderCopyFromOtherFunction = functionBuilder.copyFromOtherFunction(otherType);
        assertEquals(otherType.getName(), builderCopyFromOtherFunction.getName());
        assertEquals(otherType.getSourceNode(), builderCopyFromOtherFunction.getSourceNode());
        assertEquals(otherType.getParametersNode(), builderCopyFromOtherFunction.getParametersNode());
        assertEquals(otherType.getReturnType(), builderCopyFromOtherFunction.getReturnType());
        assertEquals(otherType.getTypeOfThis(), builderCopyFromOtherFunction.getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), builderCopyFromOtherFunction.getTemplateName());
        assertEquals(otherType.isConstructor(), builderCopyFromOtherFunction.isConstructor());
        assertEquals(otherType.isNativeObjectType(), builderCopyFromOtherFunction.isNativeType());
    }

    @Test
    public void testBuild() {
        FunctionType functionType = functionBuilder.build();
        assertNotNull(functionType);
    }
}
