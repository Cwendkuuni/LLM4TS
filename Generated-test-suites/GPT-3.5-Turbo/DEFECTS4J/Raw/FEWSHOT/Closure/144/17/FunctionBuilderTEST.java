import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FunctionBuilderTest {
    private JSTypeRegistry registry = new JSTypeRegistry();

    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType type = builder.withName("test").build();
        
        assertEquals("test", type.getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        Node sourceNode = new Node();
        FunctionType type = builder.withSourceNode(sourceNode).build();
        
        assertEquals(sourceNode, type.getSource());
    }

    @Test
    public void testWithParams() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
        Node parametersNode = paramBuilder.build();
        FunctionType type = builder.withParams(paramBuilder).build();
        
        assertEquals(parametersNode, type.getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        Node parametersNode = new Node();
        FunctionType type = builder.withParamsNode(parametersNode).build();
        
        assertEquals(parametersNode, type.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        JSType returnType = new JSType();
        FunctionType type = builder.withReturnType(returnType).build();
        
        assertEquals(returnType, type.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        JSType returnType = new JSType();
        FunctionType type = builder.withInferredReturnType(returnType).build();
        
        assertEquals(returnType, type.getReturnType());
        assertEquals(true, type.isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        ObjectType typeOfThis = new ObjectType();
        FunctionType type = builder.withTypeOfThis(typeOfThis).build();
        
        assertEquals(typeOfThis, type.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType type = builder.withTemplateName("testTemplate").build();
        
        assertEquals("testTemplate", type.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType type = builder.forConstructor().build();
        
        assertEquals(true, type.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType type = builder.forNativeType().build();
        
        assertEquals(true, type.isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType otherType = new FunctionType(registry);
        FunctionType type = builder.copyFromOtherFunction(otherType).build();
        
        assertEquals(otherType.getReferenceName(), type.getReferenceName());
        assertEquals(otherType.getSource(), type.getSource());
        assertEquals(otherType.getParametersNode(), type.getParametersNode());
        assertEquals(otherType.getReturnType(), type.getReturnType());
        assertEquals(otherType.getTypeOfThis(), type.getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), type.getTemplateTypeName());
        assertEquals(otherType.isConstructor(), type.isConstructor());
        assertEquals(otherType.isNativeObjectType(), type.isNativeObjectType());
    }
}