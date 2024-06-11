import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {
  
  private JSTypeRegistry registry;
  private FunctionBuilder builder;
  
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    builder = new FunctionBuilder(registry);
  }
  
  @Test
  public void testWithName() {
    FunctionType functionType = builder.withName("testFunction")
                                     .build();
    
    assertEquals("testFunction", functionType.getName());
  }
  
  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node("sourceNode");
    
    FunctionType functionType = builder.withSourceNode(sourceNode)
                                     .build();
    
    assertEquals(sourceNode, functionType.getSourceNode());
  }
  
  @Test
  public void testWithParams() {
    Node parametersNode = new Node("parametersNode");
    
    FunctionType functionType = builder.withParamsNode(parametersNode)
                                     .build();
    
    assertEquals(parametersNode, functionType.getParametersNode());
  }
  
  @Test
  public void testWithReturnType() {
    JSType returnType = new JSType("integer");
    
    FunctionType functionType = builder.withReturnType(returnType)
                                     .build();
    
    assertEquals(returnType, functionType.getReturnType());
  }
  
  @Test
  public void testWithInferredReturnType() {
    JSType returnType = new JSType("string");
    
    FunctionType functionType = builder.withInferredReturnType(returnType)
                                     .build();
    
    assertEquals(returnType, functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
  }
  
  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType("MyObject");
    
    FunctionType functionType = builder.withTypeOfThis(typeOfThis)
                                     .build();
    
    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }
  
  @Test
  public void testWithTemplateName() {
    String templateTypeName = "TemplateType";
    
    FunctionType functionType = builder.withTemplateName(templateTypeName)
                                     .build();
    
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
  }
  
  @Test
  public void testForConstructor() {
    FunctionType functionType = builder.forConstructor()
                                     .build();
    
    assertTrue(functionType.isConstructor());
  }
  
  @Test
  public void testForNativeType() {
    FunctionType functionType = builder.forNativeType()
                                     .build();
    
    assertTrue(functionType.isNativeType());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherFunctionType = new FunctionType(registry, "otherType",
                                                     new Node("source"),
                                                     new ArrowType(registry, new Node("params"), new JSType("string"), false),
                                                     new ObjectType("OtherObject"), "OtherTemplate", true, false);
    
    FunctionType functionType = builder.copyFromOtherFunction(otherFunctionType)
                                     .build();
    
    assertEquals("otherType", functionType.getName());
    assertEquals(otherFunctionType.getSourceNode(), functionType.getSourceNode());
    assertEquals(otherFunctionType.getParametersNode(), functionType.getParametersNode());
    assertEquals(otherFunctionType.getReturnType(), functionType.getReturnType());
    assertEquals(otherFunctionType.getTypeOfThis(), functionType.getTypeOfThis());
    assertEquals(otherFunctionType.getTemplateTypeName(), functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertFalse(functionType.isNativeType());
  }
  
  @Test
  public void testCompleteFunction() {
    Node sourceNode = new Node("sourceNode");
    Node parametersNode = new Node("parametersNode");
    JSType returnType = new JSType("integer");
    ObjectType typeOfThis = new ObjectType("MyObject");
    String templateTypeName = "TemplateType";
    
    FunctionType functionType = builder.withName("testFunction")
                                     .withSourceNode(sourceNode)
                                     .withParamsNode(parametersNode)
                                     .withReturnType(returnType)
                                     .withTypeOfThis(typeOfThis)
                                     .withTemplateName(templateTypeName)
                                     .forConstructor()
                                     .build();
    
    assertEquals("testFunction", functionType.getName());
    assertEquals(sourceNode, functionType.getSourceNode());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertFalse(functionType.isNativeType());
  }
  
}