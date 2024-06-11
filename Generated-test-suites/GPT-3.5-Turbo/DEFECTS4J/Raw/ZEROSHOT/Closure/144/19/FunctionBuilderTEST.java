import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.*;
import com.google.javascript.rhino.jstype.*;

public class FunctionBuilderTest {
  
  @Test
  public void testWithName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.withName("test");
    FunctionType result = builder.build();
    assertEquals("test", result.getReferenceName());
  }
  
  @Test
  public void testWithSourceNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    Node sourceNode = new Node(Token.NAME, "source");
    builder.withSourceNode(sourceNode);
    FunctionType result = builder.build();
    assertEquals(sourceNode, result.getSource());
  }
  
  @Test
  public void testWithParams() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    paramBuilder.withParam("param1");
    paramBuilder.withParam("param2");
    Node parametersNode = paramBuilder.build();
    builder.withParamsNode(parametersNode);
    FunctionType result = builder.build();
    assertEquals(parametersNode, result.getParametersNode());
  }
  
  @Test
  public void testWithReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    JSType returnType = new JSType(registry);
    builder.withReturnType(returnType);
    FunctionType result = builder.build();
    assertEquals(returnType, result.getReturnType());
  }
  
  @Test
  public void testWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    JSType returnType = new JSType(registry);
    builder.withInferredReturnType(returnType);
    FunctionType result = builder.build();
    assertSame(returnType, result.getReturnType());
    assertTrue(result.isInferredReturnType());
  }
  
  @Test
  public void testWithTypeOfThis() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    ObjectType typeOfThis = new ObjectType(registry);
    builder.withTypeOfThis(typeOfThis);
    FunctionType result = builder.build();
    assertEquals(typeOfThis, result.getTypeOfThis());
  }
  
  @Test
  public void testWithTemplateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.withTemplateName("template");
    FunctionType result = builder.build();
    assertEquals("template", result.getTemplateTypeName());
  }
  
  @Test
  public void testForConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.forConstructor();
    FunctionType result = builder.build();
    assertTrue(result.isConstructor());
  }
  
  @Test
  public void testForNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.forNativeType();
    FunctionType result = builder.build();
    assertTrue(result.isNativeObjectType());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionType otherType = new FunctionType(registry);
    otherType.setReferenceName("other");
    otherType.setSource(new Node(Token.NAME, "otherSource"));
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    paramBuilder.withParam("otherParam");
    Node parametersNode = paramBuilder.build();
    otherType.setParametersNode(parametersNode);
    JSType returnType = new JSType(registry);
    otherType.setReturnType(returnType);
    ObjectType typeOfThis = new ObjectType(registry);
    otherType.setTypeOfThis(typeOfThis);
    otherType.setTemplateTypeName("otherTemplate");
    otherType.setConstructor(true);
    otherType.setNativeObjectType(true);
    builder.copyFromOtherFunction(otherType);
    FunctionType result = builder.build();
    assertEquals("other", result.getReferenceName());
    assertEquals(new Node(Token.NAME, "otherSource"), result.getSource());
    assertEquals(parametersNode, result.getParametersNode());
    assertEquals(returnType, result.getReturnType());
    assertEquals(typeOfThis, result.getTypeOfThis());
    assertEquals("otherTemplate", result.getTemplateTypeName());
    assertTrue(result.isConstructor());
    assertTrue(result.isNativeObjectType());
  }
}