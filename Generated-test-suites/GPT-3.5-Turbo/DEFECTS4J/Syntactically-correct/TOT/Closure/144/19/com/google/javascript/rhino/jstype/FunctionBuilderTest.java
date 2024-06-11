import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.jstype.*;

public class FunctionBuilderTest {
  
  @Test
  public void testName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.withName("testName");
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals("testName", functionType.getReferenceName());
  }
  
  @Test
  public void testSourceNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.withSourceNode(new Node("SourceNode"));
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals("SourceNode", functionType.getSource().toString());
  }
  
  @Test
  public void testParams() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    paramBuilder.addParam("param1", registry.getNativeType(JSTypeNative.NUMBER_TYPE));
    paramBuilder.addParam("param2", registry.getNativeType(JSTypeNative.STRING_TYPE));
    functionBuilder.withParams(paramBuilder);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals(2, functionType.getParametersNode().getChildCount());
    assertEquals("param1", functionType.getParametersNode().getChildAtIndex(0).getString());
    assertEquals("param2", functionType.getParametersNode().getChildAtIndex(1).getString());
    assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE), functionType.getParametersNode().getChildAtIndex(0).getJSType());
    assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE), functionType.getParametersNode().getChildAtIndex(1).getJSType());
  }
  
  @Test
  public void testParamsNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    Node parametersNode = new Node("PARAMETERS_NODE");
    functionBuilder.withParamsNode(parametersNode);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals("PARAMETERS_NODE", functionType.getParametersNode().toString());
  }
  
  @Test
  public void testReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.withReturnType(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE));
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), functionType.getReturnType());
  }
  
  @Test
  public void testInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.withInferredReturnType(registry.getNativeType(JSTypeNative.YES_TYPE));
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals(registry.getNativeType(JSTypeNative.YES_TYPE), functionType.getReturnType());
    assertTrue(functionType.getInferredReturnType());
  }
  
  @Test
  public void testTypeOfThis() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    ObjectType objectType = new ObjectType(registry, "ObjectType");
    functionBuilder.withTypeOfThis(objectType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals(objectType, functionType.getTypeOfThis());
  }
  
  @Test
  public void testTemplateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.withTemplateName("TemplateName");
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals("TemplateName", functionType.getTemplateTypeName());
  }
  
  @Test
  public void testForConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.forConstructor();
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertTrue(functionType.isConstructor());
  }
  
  @Test
  public void testForNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.forNativeType();
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertTrue(functionType.isNativeObjectType());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionType otherType = new FunctionType(registry);
    otherType.setReferenceName("OtherType");
    otherType.setSource(new Node("OtherTypeSourceNode"));
    otherType.setParametersNode(new Node("OtherTypeParamsNode"));
    otherType.setReturnType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
    otherType.setTypeOfThis(new ObjectType(registry, "OtherTypeObjectType"));
    otherType.setTemplateTypeName("OtherTypeTemplateName");
    otherType.setConstructor(true);
    otherType.setNativeObjectType(true);
    functionBuilder.copyFromOtherFunction(otherType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals("OtherType", functionType.getReferenceName());
    assertEquals("OtherTypeSourceNode", functionType.getSource().toString());
    assertEquals("OtherTypeParamsNode", functionType.getParametersNode().toString());
    assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE), functionType.getReturnType());
    assertEquals(new ObjectType(registry, "OtherTypeObjectType"), functionType.getTypeOfThis());
    assertEquals("OtherTypeTemplateName", functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertTrue(functionType.isNativeObjectType());
  }
  
  @Test
  public void testBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
  }
}