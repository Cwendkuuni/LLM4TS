import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.javascript.rhino.Node;

public class FunctionBuilderTest {
    
    private JSTypeRegistry registry;
    private FunctionBuilder builder;
    
    @Before
    public void setUp() {
        registry = mock(JSTypeRegistry.class);
        builder = new FunctionBuilder(registry);
    }
    
    @Test
    public void testWithName() {
        String name = "testName";
        builder.withName(name);
        assertEquals(name, builder.build().getReferenceName());
    }
    
    @Test
    public void testWithSourceNode() {
        Node sourceNode = mock(Node.class);
        builder.withSourceNode(sourceNode);
        assertEquals(sourceNode, builder.build().getSource());
    }
    
    @Test
    public void testWithParams() {
        FunctionParamBuilder params = mock(FunctionParamBuilder.class);
        Node parametersNode = mock(Node.class);
        
        // Stubbing
        params.build();
        verify(params).build();
        
        // Mocking
        ArgumentCaptor<FunctionParamBuilder> captor = ArgumentCaptor.forClass(FunctionParamBuilder.class);
        builder.withParams(params);
        verify(params).build();
    }
    
    @Test
    public void testWithParamsNode() {
        Node parametersNode = mock(Node.class);
        builder.withParamsNode(parametersNode);
        assertEquals(parametersNode, builder.build().getParametersNode());
    }
    
    @Test
    public void testWithReturnType() {
        JSType returnType = mock(JSType.class);
        builder.withReturnType(returnType);
        assertEquals(returnType, builder.build().getReturnType());
    }
    
    @Test
    public void testWithInferredReturnType() {
        JSType returnType = mock(JSType.class);
        builder.withInferredReturnType(returnType);
        assertEquals(returnType, builder.build().getReturnType());
        assertTrue(builder.build().isInferredReturnType());
    }
    
    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = mock(ObjectType.class);
        builder.withTypeOfThis(typeOfThis);
        assertEquals(typeOfThis, builder.build().getTypeOfThis());
    }
    
    @Test
    public void testWithTemplateName() {
        String templateTypeName = "testName";
        builder.withTemplateName(templateTypeName);
        assertEquals(templateTypeName, builder.build().getTemplateTypeName());
    }
    
    @Test
    public void testForConstructor() {
        builder.forConstructor();
        assertTrue(builder.build().isConstructor());
    }
    
    @Test
    public void testForNativeType() {
        builder.forNativeType();
        assertTrue(builder.build().isNativeObjectType());
    }
    
    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = mock(FunctionType.class);
        String referenceName = "testName";
        Node sourceNode = mock(Node.class);
        Node parametersNode = mock(Node.class);
        JSType returnType = mock(JSType.class);
        ObjectType typeOfThis = mock(ObjectType.class);
        String templateTypeName = "testTemplate";
        boolean isConstructor = true;
        boolean isNativeType = false;
        
        when(otherType.getReferenceName()).thenReturn(referenceName);
        when(otherType.getSource()).thenReturn(sourceNode);
        when(otherType.getParametersNode()).thenReturn(parametersNode);
        when(otherType.getReturnType()).thenReturn(returnType);
        when(otherType.getTypeOfThis()).thenReturn(typeOfThis);
        when(otherType.getTemplateTypeName()).thenReturn(templateTypeName);
        when(otherType.isConstructor()).thenReturn(isConstructor);
        when(otherType.isNativeObjectType()).thenReturn(isNativeType);
        
        builder.copyFromOtherFunction(otherType);
        
        assertEquals(referenceName, builder.build().getReferenceName());
        assertEquals(sourceNode, builder.build().getSource());
        assertEquals(parametersNode, builder.build().getParametersNode());
        assertEquals(returnType, builder.build().getReturnType());
        assertEquals(typeOfThis, builder.build().getTypeOfThis());
        assertEquals(templateTypeName, builder.build().getTemplateTypeName());
        assertEquals(isConstructor, builder.build().isConstructor());
        assertEquals(isNativeType, builder.build().isNativeObjectType());
    }
}