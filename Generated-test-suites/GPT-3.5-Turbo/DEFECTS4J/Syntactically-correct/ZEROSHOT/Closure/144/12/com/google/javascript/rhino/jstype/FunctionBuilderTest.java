import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionBuilder result = builder.withName("testName");
    assertEquals("testName", result.name);
  }

  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(null);
    Node node = new Node();
    FunctionBuilder result = builder.withSourceNode(node);
    assertEquals(node, result.sourceNode);
  }

  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionParamBuilder params = new FunctionParamBuilder(null);
    FunctionBuilder result = builder.withParams(params);
    assertEquals(params.build(), result.parametersNode);
  }

  @Test
  public void testWithParamsNode() {
    FunctionBuilder builder = new FunctionBuilder(null);
    Node node = new Node();
    FunctionBuilder result = builder.withParamsNode(node);
    assertEquals(node, result.parametersNode);
  }

  @Test
  public void testWithReturnType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    JSType returnType = new JSType();
    FunctionBuilder result = builder.withReturnType(returnType);
    assertEquals(returnType, result.returnType);
  }

  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    JSType returnType = new JSType();
    FunctionBuilder result = builder.withInferredReturnType(returnType);
    assertEquals(returnType, result.returnType);
    assertTrue(result.inferredReturnType);
  }

  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder builder = new FunctionBuilder(null);
    ObjectType typeOfThis = new ObjectType();
    FunctionBuilder result = builder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, result.typeOfThis);
  }

  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = new FunctionBuilder(null);
    String templateTypeName = "template";
    FunctionBuilder result = builder.withTemplateName(templateTypeName);
    assertEquals(templateTypeName, result.templateTypeName);
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionBuilder result = builder.forConstructor();
    assertTrue(result.isConstructor);
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionBuilder result = builder.forNativeType();
    assertTrue(result.isNativeType);
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionType otherType = new FunctionType(null, "other", null, null, null, null, false, false);
    FunctionBuilder result = builder.copyFromOtherFunction(otherType);
    assertEquals("other", result.name);
    assertEquals(null, result.sourceNode);
    assertEquals(null, result.parametersNode);
    assertEquals(null, result.returnType);
    assertEquals(null, result.typeOfThis);
    assertEquals(null, result.templateTypeName);
    assertFalse(result.isConstructor);
    assertFalse(result.isNativeType);
  }

  @Test
  public void testBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionType result = builder.build();
    assertNotNull(result);
    assertEquals(registry, result.registry);
  }
}