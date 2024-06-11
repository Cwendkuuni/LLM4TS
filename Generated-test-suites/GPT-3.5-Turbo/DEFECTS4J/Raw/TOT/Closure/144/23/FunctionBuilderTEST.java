import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    String functionName = "testFunction";
    FunctionType functionType = functionBuilder.withName(functionName).build();
    
    assertNotNull(functionType);
    assertEquals(functionName, functionType.getReferenceName());
  }
  
  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node(Token.FUNCTION, new Node(Token.NAME, "testFunction"));
    FunctionType functionType = functionBuilder.withSourceNode(sourceNode).build();
    
    assertNotNull(functionType);
    assertEquals(sourceNode, functionType.getSource());
  }
  
  @Test
  public void testWithParams() {
    FunctionParamBuilder paramsBuilder = new FunctionParamBuilder(registry);
    paramsBuilder.addParam("param1", JSType.UNKNOWN);
    paramsBuilder.addParam("param2", JSType.NUMBER);
    Node parametersNode = paramsBuilder.build();
    
    FunctionType functionType = functionBuilder.withParams(paramsBuilder).build();
  
    assertNotNull(functionType);
    assertEquals(parametersNode, functionType.getParametersNode());
  }
  
  @Test
  public void testWithReturnType() {
    JSType returnType = JSType.STRING;
    FunctionType functionType = functionBuilder.withReturnType(returnType).build();
  
    assertNotNull(functionType);
    assertEquals(returnType, functionType.getReturnType());
  }
  
  @Test
  public void testWithInferredReturnType() {
    JSType returnType = JSType.UNKNOWN;
    FunctionType functionType = functionBuilder.withInferredReturnType(returnType).build();
  
    assertNotNull(functionType);
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(true, functionType.isReturnTypeInferred());
  }
  
  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType();
    FunctionType functionType = functionBuilder.withTypeOfThis(typeOfThis).build();
  
    assertNotNull(functionType);
    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }
  
  @Test
  public void testWithTemplateName() {
    String templateTypeName = "TemplateType";
    FunctionType functionType = functionBuilder.withTemplateName(templateTypeName).build();
  
    assertNotNull(functionType);
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
  }
  
  @Test
  public void testForConstructor() {
    FunctionType functionType = functionBuilder.forConstructor().build();
  
    assertNotNull(functionType);
    assertEquals(true, functionType.isConstructor());
  }
  
  @Test
  public void testForNativeType() {
    FunctionType functionType = functionBuilder.forNativeType().build();
  
    assertNotNull(functionType);
    assertEquals(true, functionType.isNativeObjectType());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherFunctionType = new FunctionType(registry, "otherFunction", null, null, null, null, false, false);
    FunctionType functionType = functionBuilder.copyFromOtherFunction(otherFunctionType).build();
  
    assertNotNull(functionType);
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