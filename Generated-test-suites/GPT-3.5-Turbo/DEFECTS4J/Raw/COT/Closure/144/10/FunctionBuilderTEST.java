import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionType functionType = builder.withName("testFunction").build();
    assertNotNull(functionType);
    assertEquals("testFunction", functionType.getReferenceName());
  }

  @Test
  public void testWithSourceNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    Node sourceNode = new Node(Token.SCRIPT);
    FunctionType functionType = builder.withSourceNode(sourceNode).build();
    assertNotNull(functionType);
    assertEquals(sourceNode, functionType.getSource());
  }

  @Test
  public void testWithParams() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    paramBuilder.addParam("param1", registry.getStringType());
    paramBuilder.addParam("param2", registry.getNumberType());
    Node parametersNode = paramBuilder.build();
    FunctionType functionType = builder.withParamsNode(parametersNode).build();
    assertNotNull(functionType);
    assertEquals(parametersNode, functionType.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    JSType returnType = registry.getNumberType();
    FunctionType functionType = builder.withReturnType(returnType).build();
    assertNotNull(functionType);
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    JSType returnType = registry.getNumberType();
    FunctionType functionType = builder.withInferredReturnType(returnType).build();
    assertNotNull(functionType);
    assertTrue(functionType.getInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    ObjectType typeOfThis = registry.createObjectType();
    FunctionType functionType = builder.withTypeOfThis(typeOfThis).build();
    assertNotNull(functionType);
    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    String templateName = "T";
    FunctionType functionType = builder.withTemplateName(templateName).build();
    assertNotNull(functionType);
    assertEquals(templateName, functionType.getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionType functionType = builder.forConstructor().build();
    assertNotNull(functionType);
    assertTrue(functionType.isConstructor());
  }

  @Test
  public void testForNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionType functionType = builder.forNativeType().build();
    assertNotNull(functionType);
    assertTrue(functionType.isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionType otherFunction = registry.createFunctionType(
        "otherFunction", null, null, null, false, false);
    FunctionType functionType = builder.copyFromOtherFunction(otherFunction).build();
    assertNotNull(functionType);
    assertEquals("otherFunction", functionType.getReferenceName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

}