import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {
  
  private JSTypeRegistry registry;
  private FunctionBuilder builder;
  private FunctionParamBuilder paramBuilder;
  
  @Before
  public void setup() {
    registry = new JSTypeRegistry();
    builder = new FunctionBuilder(registry);
    paramBuilder = new FunctionParamBuilder(registry);
  }
  
  @Test
  public void testWithName() {
    String name = "testFunction";
    
    FunctionType functionType = builder.withName(name).build();
    
    assertEquals(name, functionType.getName());
  }
  
  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node("testSourceNode");
    
    FunctionType functionType = builder.withSourceNode(sourceNode).build();
    
    assertSame(sourceNode, functionType.getSource());
  }
  
  @Test
  public void testWithParams() {
    Node paramNode = new Node("testParamNode");
    
    FunctionType functionType = builder.withParamsNode(paramNode).build();
    
    assertSame(paramNode, functionType.getParametersNode());
  }
  
  @Test
  public void testWithReturnType() {
    JSType returnType = new JSType("testReturnType");
    
    FunctionType functionType = builder.withReturnType(returnType).build();
    
    assertSame(returnType, functionType.getReturnType());
  }
  
  @Test
  public void testWithInferredReturnType() {
    JSType returnType = new JSType("testReturnType");
    
    FunctionType functionType = builder.withInferredReturnType(returnType).build();
    
    assertSame(returnType, functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
  }
  
  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType();
    
    FunctionType functionType = builder.withTypeOfThis(typeOfThis).build();
    
    assertSame(typeOfThis, functionType.getTypeOfThis());
  }
  
  @Test
  public void testWithTemplateName() {
    String templateTypeName = "testTemplateType";
    
    FunctionType functionType = builder.withTemplateName(templateTypeName).build();
    
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
  }
  
  @Test
  public void testForConstructor() {
    FunctionType functionType = builder.forConstructor().build();
    
    assertTrue(functionType.isConstructor());
  }
  
  @Test
  public void testForNativeType() {
    FunctionType functionType = builder.forNativeType().build();
    
    assertTrue(functionType.isNativeObjectType());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType();
    
    FunctionType functionType = builder.copyFromOtherFunction(otherType).build();
    
    assertSame(otherType.getReferenceName(), functionType.getName());
    assertSame(otherType.getSource(), functionType.getSource());
    assertSame(otherType.getParametersNode(), functionType.getParametersNode());
    assertSame(otherType.getReturnType(), functionType.getReturnType());
    assertSame(otherType.getTypeOfThis(), functionType.getTypeOfThis());
    assertSame(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
    assertEquals(otherType.isConstructor(), functionType.isConstructor());
    assertEquals(otherType.isNativeObjectType(), functionType.isNativeObjectType());
  }
  
  // Include additional test cases for all methods
  
}