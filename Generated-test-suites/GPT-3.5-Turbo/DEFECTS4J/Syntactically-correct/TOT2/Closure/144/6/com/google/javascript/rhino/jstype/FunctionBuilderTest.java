import static org.junit.Assert.*;

import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    String expectedName = "testFunction";
    FunctionType function = builder.withName(expectedName).build();
    assertEquals(expectedName, function.getReferenceName());
  }

  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    Node parametersNode = new Node(Token.LP, new Node(Token.EMPTY));
    paramBuilder.withParam("param1").withType(new FunctionType(paramBuilder.build(), null, null, null, null, null, false, false));
    FunctionType function = builder.withParams(paramBuilder).build();
    assertEquals(parametersNode, function.getParams());
  }

  @Test
  public void testWithReturnType() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType expectedReturnType = registry.getNativeType(JSTypeNative.VOID_TYPE);
    FunctionType function = builder.withReturnType(expectedReturnType).build();
    assertEquals(expectedReturnType, function.getReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    ObjectType objectType = new ObjectType();
    FunctionType function = builder.withTypeOfThis(objectType).build();
    assertEquals(objectType, function.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    String expectedTemplateName = "templateName";
    FunctionType function = builder.withTemplateName(expectedTemplateName).build();
    assertEquals(expectedTemplateName, function.getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType function = builder.forConstructor().build();
    assertTrue(function.isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType function = builder.forNativeType().build();
    assertTrue(function.isNativeObjectType());
  }

}