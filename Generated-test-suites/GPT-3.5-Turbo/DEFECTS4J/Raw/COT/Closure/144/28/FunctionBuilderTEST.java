import org.junit.Test;

public class FunctionBuilderTest {
    
    // test for withName() method
    @Test
    public void testWithName() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionType functionType = functionBuilder.withName("test").build();
        assert functionType.getReferenceName().equals("test");
    }
    
    // test for withSourceNode() method
    @Test
    public void testWithSourceNode() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionType functionType = functionBuilder.withSourceNode(null).build();
        assert functionType.getSource() == null;
    }
    
    // test for withParams() method
    @Test
    public void testWithParams() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionType functionType = functionBuilder.withParams(new FunctionParamBuilder()).build();
        assert functionType.getParametersNode() != null;
    }
    
    // test for withParamsNode() method
    @Test
    public void testWithParamsNode() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionType functionType = functionBuilder.withParamsNode(null).build();
        assert functionType.getParametersNode() == null;
    }
    
    // test for withReturnType() method
    @Test
    public void testWithReturnType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionType functionType = functionBuilder.withReturnType(null).build();
        assert functionType.getReturnType() == null;
    }
    
    // test for withInferredReturnType() method
    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionType functionType = functionBuilder.withInferredReturnType(null).build();
        assert functionType.getReturnType() == null;
        assert functionType.hasInferredReturnType();
    }
    
    // test for withTypeOfThis() method
    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        ObjectType objectType = new ObjectType();
        FunctionType functionType = functionBuilder.withTypeOfThis(objectType).build();
        assert functionType.getTypeOfThis() == objectType;
    }
    
    // test for withTemplateName() method
    @Test
    public void testWithTemplateName() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionType functionType = functionBuilder.withTemplateName("template").build();
        assert functionType.getTemplateTypeName().equals("template");
    }
    
    // test for forConstructor() method
    @Test
    public void testForConstructor() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionType functionType = functionBuilder.forConstructor().build();
        assert functionType.isConstructor();
    }
    
    // test for forNativeType() method
    @Test
    public void testForNativeType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionType functionType = functionBuilder.forNativeType().build();
        assert functionType.isNativeObjectType();
    }
    
    // test for copyFromOtherFunction() method
    @Test
    public void testCopyFromOtherFunction() {
        FunctionBuilder functionBuilder = new FunctionBuilder(null);
        FunctionType otherType = new FunctionType(null, "other", null, null, null, null, false, false);
        FunctionType functionType = functionBuilder.copyFromOtherFunction(otherType).build();
        assert functionType.getReferenceName().equals("other");
        assert functionType.isConstructor() == false;
    }
}