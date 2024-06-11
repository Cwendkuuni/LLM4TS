import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  private JSTypeRegistry registry;
  
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionType functionType = builder.withName("myFunction").build();
    
    assertEquals("myFunction", functionType.getReferenceName());
  }
  
  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    Node sourceNode = new Node("functionSource");
    FunctionType functionType = builder.withSourceNode(sourceNode).build();
    
    assertEquals(sourceNode, functionType.getSource());
  }
  
  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    Node parametersNode = paramBuilder.build();
    FunctionType functionType = builder.withParams(paramBuilder).build();
    
    assertEquals(parametersNode, functionType.getParametersNode());
  }
  
  @Test
  public void testWithParamsNode() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    Node parametersNode = new Node("paramsNode");
    FunctionType functionType = builder.withParamsNode(parametersNode).build();
    
    assertEquals(parametersNode, functionType.getParametersNode());
  }
  
  @Test
  public void testWithReturnType() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    JSType returnType = registry.getNativeType();
    FunctionType functionType = builder.withReturnType(returnType).build();
    
    assertEquals(returnType, functionType.getReturnType());
  }
  
  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    JSType returnType = registry.getNativeType();
    FunctionType functionType = builder.withInferredReturnType(returnType).build();
    
    assertEquals(returnType, functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
  }
  
  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    ObjectType typeOfThis = new ObjectType();
    FunctionType functionType = builder.withTypeOfThis(typeOfThis).build();
    
    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }
  
  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    String templateName = "myTemplate";
    FunctionType functionType = builder.withTemplateName(templateName).build();
    
    assertEquals(templateName, functionType.getTemplateTypeName());
  }
  
  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionType functionType = builder.forConstructor().build();
    
    assertTrue(functionType.isConstructor());
  }
  
  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionType functionType = builder.forNativeType().build();
    
    assertTrue(functionType.isNativeObjectType());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionType functionType = createFunctionType();
    FunctionType copiedFunctionType = builder.copyFromOtherFunction(functionType).build();
    
    assertEquals(functionType.getReferenceName(), copiedFunctionType.getReferenceName());
    assertEquals(functionType.getSource(), copiedFunctionType.getSource());
    assertEquals(functionType.getParametersNode(), copiedFunctionType.getParametersNode());
    assertEquals(functionType.getReturnType(), copiedFunctionType.getReturnType());
    assertEquals(functionType.getTypeOfThis(), copiedFunctionType.getTypeOfThis());
    assertEquals(functionType.getTemplateTypeName(), copiedFunctionType.getTemplateTypeName());
    assertEquals(functionType.isConstructor(), copiedFunctionType.isConstructor());
    assertEquals(functionType.isNativeObjectType(), copiedFunctionType.isNativeObjectType());
  }
  
  private FunctionType createFunctionType() {
    // Create a function type with some sample values
    return new FunctionType(registry, "myFunction", new Node("sourceNode"), new ArrowType(), new ObjectType(),
        "templateName", true, false);
  }

}