import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.jstype.*;

public class FunctionBuilderTest {
  
  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.withName("testName").build();
    String expectedName = "testName";
    assertEquals(expectedName, functionType.getReferenceName());
  }
  
  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    Node sourceNode = new Node();
    FunctionType functionType = builder.withSourceNode(sourceNode).build();
    Node expectedNode = sourceNode;
    assertEquals(expectedNode, functionType.getSource());
  }
  
  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionParamBuilder paramsBuilder = new FunctionParamBuilder(new JSTypeRegistry());
    paramsBuilder.withParamName("param1").withParamType(new JSType());
    Node paramsNode = paramsBuilder.build();
    FunctionType functionType = builder.withParamsNode(paramsNode).build();
    Node expectedParamsNode = paramsNode;
    assertEquals(expectedParamsNode, functionType.getParametersNode());
  }
  
  @Test
  public void testWithReturnType() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    JSType returnType = new JSType();
    FunctionType functionType = builder.withReturnType(returnType).build();
    JSType expectedReturnType = returnType;
    assertEquals(expectedReturnType, functionType.getReturnType());
  }
  
  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    JSType returnType = new JSType();
    FunctionType functionType = builder.withInferredReturnType(returnType).build();
    JSType expectedReturnType = returnType;
    assertTrue(functionType.getInferredReturnType());
    assertEquals(expectedReturnType, functionType.getReturnType());
  }
  
  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    ObjectType typeOfThis = new ObjectType();
    FunctionType functionType = builder.withTypeOfThis(typeOfThis).build();
    ObjectType expectedTypeOfThis = typeOfThis;
    assertEquals(expectedTypeOfThis, functionType.getTypeOfThis());
  }
  
  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    String templateName = "template";
    FunctionType functionType = builder.withTemplateName(templateName).build();
    String expectedTemplateName = templateName;
    assertEquals(expectedTemplateName, functionType.getTemplateTypeName());
  }
  
  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.forConstructor().build();
    assertTrue(functionType.isConstructor());
  }
  
  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType functionType = builder.forNativeType().build();
    assertTrue(functionType.isNativeObjectType());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType otherType = new FunctionType();
    otherType.setReferenceName("otherName");
    otherType.setSource(new Node());
    FunctionParamBuilder paramsBuilder = new FunctionParamBuilder(new JSTypeRegistry());
    paramsBuilder.withParamName("param1").withParamType(new JSType());
    Node paramsNode = paramsBuilder.build();
    otherType.setParametersNode(paramsNode);
    otherType.setReturnType(new JSType());
    otherType.setTypeOfThis(new ObjectType());
    otherType.setTemplateTypeName("otherTemplate");
    otherType.setConstructor(true);
    otherType.setNativeObjectType(true);
    FunctionType functionType = builder.copyFromOtherFunction(otherType).build();
    assertEquals(otherType.getReferenceName(), functionType.getReferenceName());
    assertEquals(otherType.getSource(), functionType.getSource());
    assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
    assertEquals(otherType.getReturnType(), functionType.getReturnType());
    assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertTrue(functionType.isNativeObjectType());
  }
  
}