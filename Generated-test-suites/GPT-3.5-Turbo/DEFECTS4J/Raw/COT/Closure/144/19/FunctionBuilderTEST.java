import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.Node;

public class FunctionBuilderTest {
    
    // Test build() method
    @Test
    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType functionType = builder.build();
        assertNotNull(functionType);
    }
    
    // Test withName() method
    @Test
    public void testWithName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType functionType = builder.withName("test").build();
        assertEquals("test", functionType.getReferenceName());
    }
    
    // Test withSourceNode() method
    @Test
    public void testWithSourceNode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);
        Node sourceNode = new Node();
        FunctionType functionType = builder.withSourceNode(sourceNode).build();
        assertEquals(sourceNode, functionType.getSource());
    }
    
    // Test withParams() method
    @Test
    public void testWithParams() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
        paramBuilder.withName("param1").withType(registry.getNativeType("number"));
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType functionType = builder.withParams(paramBuilder).build();
        
        Node parametersNode = functionType.getParametersNode();
        assertNotNull(parametersNode);
        assertEquals("param1", parametersNode.getFirstChild().getString());
        assertEquals(registry.getNativeType("number"), parametersNode.getFirstChild().getJSType());
    }
    
    // Test withParamsNode() method
    @Test
    public void testWithParamsNode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parametersNode = new Node(Node.PARAM_LIST);
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType functionType = builder.withParamsNode(parametersNode).build();
        assertEquals(parametersNode, functionType.getParametersNode());
    }
    
    // Test withReturnType() method
    @Test
    public void testWithReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType returnType = registry.getNativeType("boolean");
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType functionType = builder.withReturnType(returnType).build();
        assertEquals(returnType, functionType.getReturnType());
    }
    
    // Test withInferredReturnType() method
    @Test
    public void testWithInferredReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType returnType = registry.getNativeType("boolean");
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType functionType = builder.withInferredReturnType(returnType).build();
        assertEquals(returnType, functionType.getReturnType());
        assertTrue(functionType.isInferredReturnType());
    }
    
    // Test withTypeOfThis() method
    @Test
    public void testWithTypeOfThis() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ObjectType typeOfThis = new ObjectType("object");
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType functionType = builder.withTypeOfThis(typeOfThis).build();
        assertEquals(typeOfThis, functionType.getTypeOfThis());
    }
    
    // Test withTemplateName() method
    @Test
    public void testWithTemplateName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType functionType = builder.withTemplateName("template").build();
        assertEquals("template", functionType.getTemplateTypeName());
    }
    
    // Test forConstructor() method
    @Test
    public void testForConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType functionType = builder.forConstructor().build();
        assertTrue(functionType.isConstructor());
    }
    
    // Test forNativeType() method
    @Test
    public void testForNativeType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType functionType = builder.forNativeType().build();
        assertTrue(functionType.isNativeObjectType());
    }
    
    // Test copyFromOtherFunction() method
    @Test
    public void testCopyFromOtherFunction() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionType otherFunctionType = new FunctionType(registry, "otherFunction", null, null, null, null, false, false);
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType functionType = builder.copyFromOtherFunction(otherFunctionType).build();
        
        assertEquals(otherFunctionType.getReferenceName(), functionType.getReferenceName());
        assertEquals(otherFunctionType.getSource(), functionType.getSource());
        assertEquals(otherFunctionType.getParametersNode(), functionType.getParametersNode());
        assertEquals(otherFunctionType.getReturnType(), functionType.getReturnType());
        assertEquals(otherFunctionType.getTypeOfThis(), functionType.getTypeOfThis());
        assertEquals(otherFunctionType.getTemplateTypeName(), functionType.getTemplateTypeName());
        assertEquals(otherFunctionType.isConstructor(), functionType.isConstructor());
        assertEquals(otherFunctionType.isNativeObjectType(), functionType.isNativeObjectType());
    }

}