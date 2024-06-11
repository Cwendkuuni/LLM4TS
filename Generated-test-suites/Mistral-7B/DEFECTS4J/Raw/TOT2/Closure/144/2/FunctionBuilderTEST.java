
import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    String expectedName = "testFunction";
    FunctionBuilder actualFunctionBuilder = functionBuilder.withName(expectedName);
    assertEquals(expectedName, actualFunctionBuilder.getName());
  }

  @Test
  public void testWithSourceNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    Node expectedSourceNode = new Node();
    FunctionBuilder actualFunctionBuilder = functionBuilder.withSourceNode(expectedSourceNode);
    assertEquals(expectedSourceNode, actualFunctionBuilder.getSourceNode());
  }

  @Test
  public void testWithParams() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(new JSTypeRegistry());
    FunctionBuilder actualFunctionBuilder = functionBuilder.withParams(functionParamBuilder);
    assertEquals(functionParamBuilder.build(), actualFunctionBuilder.getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    Node expectedParametersNode = new Node();
    FunctionBuilder actualFunctionBuilder = functionBuilder.withParamsNode(expectedParametersNode);
    assertEquals(expectedParametersNode, actualFunctionBuilder.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    JSType expectedReturnType = new IntType();
    FunctionBuilder actualFunctionBuilder = functionBuilder.withReturnType(expectedReturnType);
    assertEquals(expectedReturnType, actualFunctionBuilder.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    JSType expectedReturnType = new IntType();
    FunctionBuilder actualFunctionBuilder = functionBuilder.withInferredReturnType(expectedReturnType);
    assertEquals(expectedReturnType, actualFunctionBuilder.getReturnType());
    assertTrue(actualFunctionBuilder.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    ObjectType expectedTypeOfThis = new ObjectType();
    FunctionBuilder actualFunctionBuilder = functionBuilder.withTypeOfThis(expectedTypeOfThis);
    assertEquals(expectedTypeOfThis, actualFunctionBuilder.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    String expectedTemplateTypeName = "TestTemplate";
    FunctionBuilder actualFunctionBuilder = functionBuilder.withTemplateName(expectedTemplateTypeName);
    assertEquals(expectedTemplateTypeName, actualFunctionBuilder.getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    FunctionBuilder actualFunctionBuilder = functionBuilder.forConstructor();
    assertTrue(actualFunctionBuilder.isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    FunctionBuilder actualFunctionBuilder = functionBuilder.forNativeType();
    assertTrue(actualFunctionBuilder.isNativeType());
  }

  @Test(expected = NullPointerException.class)
  public void testWithNullName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    FunctionBuilder actualFunctionBuilder = functionBuilder.withName(null);
  }

  @Test(expected = NullPointerException.class)
  public void testWithNullSourceNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    FunctionBuilder actualFunctionBuilder = functionBuilder.withSourceNode(null);
  }

  @Test(expected = NullPointerException.class)
  public void testWithNullParametersNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    FunctionBuilder actualFunctionBuilder = functionBuilder.withParamsNode(null);
  }

  @Test(expected = NullPointerException.class)
  public void testWithNullReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    FunctionBuilder actualFunctionBuilder = functionBuilder.withReturnType(null);
  }

  @Test(expected = NullPointerException.class)
  public void testWithNullTypeOfThis() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    FunctionBuilder actualFunctionBuilder = functionBuilder.withTypeOfThis(null);
  }

  @Test
  public void testWithConstructorAndNotNativeType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    FunctionBuilder actualFunctionBuilder = functionBuilder.forConstructor();
    assertFalse(actualFunctionBuilder.isNativeType());
    assertTrue(actualFunctionBuilder.isConstructor());
  }

  @Test
  public void testWithNativeTypeAndNotConstructor() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    FunctionBuilder actualFunctionBuilder = functionBuilder.forNativeType();
    assertFalse(actualFunctionBuilder.isConstructor());
    assertTrue(actualFunctionBuilder.isNativeType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    FunctionType otherType = new FunctionType(new JSTypeRegistry(), "testFunction", null, null, null, null, false, false);
    FunctionBuilder actualFunctionBuilder = functionBuilder.copyFromOtherFunction(otherType);
    assertEquals(otherType.getName(), actualFunctionBuilder.getName());
    assertEquals(otherType.getSource(), actualFunctionBuilder.getSourceNode());
    assertEquals(otherType.getParametersNode(), actualFunctionBuilder.getParametersNode());
    assertEquals(otherType.getReturnType(), actualFunctionBuilder.getReturnType());
    assertEquals(otherType.getTypeOfThis(), actualFunctionBuilder.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), actualFunctionBuilder.getTemplateTypeName());
    assertTrue(otherType.isConstructor(), actualFunctionBuilder.isConstructor());
    assertTrue(otherType.isNativeType(), actualFunctionBuilder.isNativeType());
  }
}