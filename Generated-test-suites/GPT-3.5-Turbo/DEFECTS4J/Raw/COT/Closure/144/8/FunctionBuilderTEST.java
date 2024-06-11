
import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        builder.withName("testFunction");
        FunctionType functionType = builder.build();
        
        assertEquals("testFunction", functionType.getReferenceName());
    }
    
    @Test
    public void testWithSourceNode() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        Node sourceNode = new Node();
        builder.withSourceNode(sourceNode);
        FunctionType functionType = builder.build();
        
        assertEquals(sourceNode, functionType.getSource());
    }
    
    @Test
    public void testWithParams() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionParamBuilder params = new FunctionParamBuilder(registry);
        builder.withParams(params);
        FunctionType functionType = builder.build();
        
        assertEquals(params.build(), functionType.getParametersNode());
    }
    
    @Test
    public void testWithParamsNode() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        Node parametersNode = new Node();
        builder.withParamsNode(parametersNode);
        FunctionType functionType = builder.build();
        
        assertEquals(parametersNode, functionType.getParametersNode());
    }
    
    @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        JSType returnType = new JSType();
        builder.withReturnType(returnType);
        FunctionType functionType = builder.build();
        
        assertEquals(returnType, functionType.getReturnType());
    }
    
    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        JSType returnType = new JSType();
        builder.withInferredReturnType(returnType);
        FunctionType functionType = builder.build();
        
        assertTrue(functionType.isInferredReturnType());
    }
    
    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        ObjectType typeOfThis = new ObjectType();
        builder.withTypeOfThis(typeOfThis);
        FunctionType functionType = builder.build();
        
        assertEquals(typeOfThis, functionType.getTypeOfThis());
    }
    
    @Test
    public void testWithTemplateName() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        String templateTypeName = "templateType";
        builder.withTemplateName(templateTypeName);
        FunctionType functionType = builder.build();
        
        assertEquals(templateTypeName, functionType.getTemplateTypeName());
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
        
        assertTrue(functionType.isNativeObjectType());
    }
    
    @Test
    public void testCopyFromOtherFunction() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType otherType = new FunctionType(registry);
        builder.copyFromOtherFunction(otherType);
        FunctionType functionType = builder.build();
        
        assertEquals(otherType.getReferenceName(), functionType.getReferenceName());
        assertEquals(otherType.getSource(), functionType.getSource());
        assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
        assertEquals(otherType.getReturnType(), functionType.getReturnType());
        assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
        assertEquals(otherType.isConstructor(), functionType.isConstructor());
        assertEquals(otherType.isNativeObjectType(), functionType.isNativeObjectType());
    }
}

