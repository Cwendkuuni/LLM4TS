
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Test;

public class FunctionBuilderTest {
  @Test
  public void testWithName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    builder.withName("testFunction");

    FunctionType result = builder.build();

    assertEquals("testFunction", result.getName());
  }

  @Test
  public void testWithSourceNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    Node sourceNode = new Node();
    builder.withSourceNode(sourceNode);

    FunctionType result = builder.build();

    assertEquals(sourceNode, result.getSource());
  }

  @Test
  public void testWithParams() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    Node parametersNode = paramBuilder.build();

    builder.withParams(paramBuilder);

    FunctionType result = builder.build();

    assertEquals(parametersNode, result.getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    Node parametersNode = new Node();
    builder.withParamsNode(parametersNode);

    FunctionType result = builder.build();

    assertEquals(parametersNode, result.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    JSType returnType = registry.getNativeType("string");
    builder.withReturnType(returnType);

    FunctionType result = builder.build();

    assertEquals(returnType, result.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    JSType returnType = registry.getNativeType("string");
    builder.withInferredReturnType(returnType);

    FunctionType result = builder.build();

    assertEquals(returnType, result.getReturnType());
    assertTrue(result.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    ObjectType typeOfThis = new ObjectType();
    builder.withTypeOfThis(typeOfThis);

    FunctionType result = builder.build();

    assertEquals(typeOfThis, result.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    builder.withTemplateName("templateType");

    FunctionType result = builder.build();

    assertEquals("templateType", result.getTemplateTypeName());
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
    otherType.setName("otherFunction");

    builder.copyFromOtherFunction(otherType);

    FunctionType result = builder.build();

    assertEquals("otherFunction", result.getName());
  }
}
