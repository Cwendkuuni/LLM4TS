
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
        assertEquals("Function name should match", "testFunction", functionBuilder.withName("testFunction").build().getReferenceName());
    }
    
    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node(Token.FUNCTION);
        assertEquals("Source node should match", sourceNode, functionBuilder.withSourceNode(sourceNode).build().getSource());
    }
    
    @Test
    public void testWithParams() {
        FunctionParamBuilder params = new FunctionParamBuilder(registry);
        Node parametersNode = params.build();
        assertEquals("Parameters node should match", parametersNode, functionBuilder.withParams(params).build().getParametersNode());
    }
    
    @Test
    public void testWithParamsNode() {
        Node parametersNode = new Node(Token.PARAM_LIST);
        assertEquals("Parameters node should match", parametersNode, functionBuilder.withParamsNode(parametersNode).build().getParametersNode());
    }
    
    @Test
    public void testWithReturnType() {
        JSType returnType = registry.createNumberType();
        assertEquals("Return type should match", returnType, functionBuilder.withReturnType(returnType).build().getReturnType());
    }
    
    @Test
    public void testWithInferredReturnType() {
        JSType returnType = registry.createInferredType();
        assertTrue("Inferred return type should be true", functionBuilder.withInferredReturnType(returnType).build().isInferredReturnType());
    }
    
    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = registry.createObject();
        assertEquals("Type of 'this' should match", typeOfThis, functionBuilder.withTypeOfThis(typeOfThis).build().getTypeOfThis());
    }
    
    @Test
    public void testWithTemplateName() {
        String templateTypeName = "TemplateType";
        assertEquals("Template name should match", templateTypeName, functionBuilder.withTemplateName(templateTypeName).build().getTemplateTypeName());
    }
    
    @Test
    public void testForConstructor() {
        assertTrue("Is constructor flag should be true", functionBuilder.forConstructor().build().isConstructor());
    }
    
    @Test
    public void testForNativeType() {
        assertTrue("Is native type flag should be true", functionBuilder.forNativeType().build().isNativeObjectType());
    }
    
    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = new FunctionType(registry, "otherFunction", null,
                new ArrowType(registry, null, registry.createVoidType(), false),
                registry.createObject(), "OtherTemplateType", true, false);
        FunctionType functionType = functionBuilder.copyFromOtherFunction(otherType).build();
        
        assertEquals("Function name should match", "otherFunction", functionType.getReferenceName());
        assertNull("Source node should be null", functionType.getSource());
        assertNull("Parameters node should be null", functionType.getParametersNode());
        assertEquals("Return type should match", registry.createVoidType(), functionType.getReturnType());
        assertEquals("Type of 'this' should match", registry.createObject(), functionType.getTypeOfThis());
        assertEquals("Template name should match", "OtherTemplateType", functionType.getTemplateTypeName());
        assertTrue("Is constructor flag should be true", functionType.isConstructor());
        assertFalse("Is native type flag should be false", functionType.isNativeObjectType());
    }
    
    @Test
    public void testBuild() {
        JSType returnType = registry.createNumberType();
        Node parametersNode = new Node(Token.PARAM_LIST);
        ObjectType typeOfThis = registry.createObject();
        String templateTypeName = "TemplateType";
        
        FunctionType functionType = functionBuilder.withName("testFunction")
                .withSourceNode(new Node(Token.FUNCTION))
                .withParamsNode(parametersNode)
                .withReturnType(returnType)
                .withTypeOfThis(typeOfThis)
                .withTemplateName(templateTypeName)
                .forConstructor()
                .forNativeType()
                .build();
        
        assertEquals("Function name should match", "testFunction", functionType.getReferenceName());
        assertNotNull("Source node should not be null", functionType.getSource());
        assertEquals("Parameters node should match", parametersNode, functionType.getParametersNode());
        assertEquals("Return type should match", returnType, functionType.getReturnType());
        assertEquals("Type of 'this' should match", typeOfThis, functionType.getTypeOfThis());
        assertEquals("Template name should match", templateTypeName, functionType.getTemplateTypeName());
        assertTrue("Is constructor flag should be true", functionType.isConstructor());
        assertTrue("Is native type flag should be true", functionType.isNativeObjectType());
    }
}

