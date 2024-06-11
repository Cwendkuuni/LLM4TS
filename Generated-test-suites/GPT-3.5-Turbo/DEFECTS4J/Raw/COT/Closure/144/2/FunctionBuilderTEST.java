import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        String name = "testFunction";
        builder.withName(name);
        FunctionType function = builder.build();

        assertEquals(name, function.getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        Node sourceNode = new Node();
        builder.withSourceNode(sourceNode);
        FunctionType function = builder.build();

        assertEquals(sourceNode, function.getSource());
    }

    @Test
    public void testWithParams() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
        paramBuilder.addParam("param1", "Type1");
        Node parametersNode = paramBuilder.build();
        builder.withParams(paramBuilder);
        FunctionType function = builder.build();

        assertEquals(parametersNode, function.getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        Node parametersNode = new Node();
        builder.withParamsNode(parametersNode);
        FunctionType function = builder.build();

        assertEquals(parametersNode, function.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        JSType returnType = new JSType();
        builder.withReturnType(returnType);
        FunctionType function = builder.build();

        assertEquals(returnType, function.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        JSType returnType = new JSType();
        builder.withInferredReturnType(returnType);
        FunctionType function = builder.build();

        assertEquals(returnType, function.getReturnType());
        assertTrue(function.isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        ObjectType typeOfThis = new ObjectType();
        builder.withTypeOfThis(typeOfThis);
        FunctionType function = builder.build();

        assertEquals(typeOfThis, function.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        String templateTypeName = "TemplateType";
        builder.withTemplateName(templateTypeName);
        FunctionType function = builder.build();

        assertEquals(templateTypeName, function.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.forConstructor();
        FunctionType function = builder.build();

        assertTrue(function.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.forNativeType();
        FunctionType function = builder.build();

        assertTrue(function.isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = createOtherFunction();
      
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.copyFromOtherFunction(otherType);
        FunctionType function = builder.build();

        assertEquals(otherType.getReferenceName(), function.getReferenceName());
        assertEquals(otherType.getSource(), function.getSource());
        assertEquals(otherType.getParametersNode(), function.getParametersNode());
        assertEquals(otherType.getReturnType(), function.getReturnType());
        assertEquals(otherType.getTypeOfThis(), function.getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), function.getTemplateTypeName());
        assertEquals(otherType.isConstructor(), function.isConstructor());
        assertEquals(otherType.isNativeObjectType(), function.isNativeObjectType());
    }
    
    private FunctionType createOtherFunction() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder otherBuilder = new FunctionBuilder(registry);
        String name = "otherFunction";
        otherBuilder.withName(name);
        Node sourceNode = new Node();
        otherBuilder.withSourceNode(sourceNode);
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
        paramBuilder.addParam("param1", "Type1");
        Node parametersNode = paramBuilder.build();
        otherBuilder.withParams(paramBuilder);
        JSType returnType = new JSType();
        otherBuilder.withReturnType(returnType);
        ObjectType typeOfThis = new ObjectType();
        otherBuilder.withTypeOfThis(typeOfThis);
        String templateTypeName = "TemplateType";
        otherBuilder.withTemplateName(templateTypeName);
        otherBuilder.forConstructor();
        otherBuilder.forNativeType();
        return otherBuilder.build();
    }

}