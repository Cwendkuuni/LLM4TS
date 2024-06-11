import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder();
        FunctionType type = builder.withName("test").build();
        assertEquals("test", type.getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        FunctionBuilder builder = new FunctionBuilder();
        Node sourceNode = new Node();
        FunctionType type = builder.withSourceNode(sourceNode).build();
        assertEquals(sourceNode, type.getSource());
    }

    @Test
    public void testWithParams() {
        FunctionBuilder builder = new FunctionBuilder();
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
        paramBuilder.addParam("param1", "String");
        Node parametersNode = paramBuilder.build();
        FunctionType type = builder.withParams(paramBuilder).build();
        assertEquals(parametersNode, type.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder();
        JSType returnType = new JSType();
        FunctionType type = builder.withReturnType(returnType).build();
        assertEquals(returnType, type.getReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder builder = new FunctionBuilder();
        ObjectType typeOfThis = new ObjectType();
        FunctionType type = builder.withTypeOfThis(typeOfThis).build();
        assertEquals(typeOfThis, type.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        FunctionBuilder builder = new FunctionBuilder();
        FunctionType type = builder.withTemplateName("template").build();
        assertEquals("template", type.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder builder = new FunctionBuilder();
        FunctionType type = builder.forConstructor().build();
        assertTrue(type.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder builder = new FunctionBuilder();
        FunctionType type = builder.forNativeType().build();
        assertTrue(type.isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionBuilder builder = new FunctionBuilder();
        FunctionType otherType = new FunctionType();
        otherType.setReferenceName("test");
        otherType.setReturnType(new JSType());
        FunctionType type = builder.copyFromOtherFunction(otherType).build();
        assertEquals("test", type.getReferenceName());
        assertEquals(otherType.getReturnType(), type.getReturnType());
    }
}