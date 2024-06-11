import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    FunctionBuilder builder = new FunctionBuilder(null);
    builder.withName("testFunction");
    assertEquals("testFunction", builder.build().getReferenceName());
  }

  @Test
  public void testWithSourceNode() {
    FunctionBuilder builder = new FunctionBuilder(null);
    Node node = new Node();
    builder.withSourceNode(node);
    assertSame(node, builder.build().getSource());
  }

  @Test
  public void testWithParams() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.addParameter("param1", "type1").addParameter("param2", "type2");
    builder.withParams(paramBuilder);
    assertEquals(paramBuilder.build(), builder.build().getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    FunctionBuilder builder = new FunctionBuilder(null);
    Node node = new Node();
    builder.withParamsNode(node);
    assertSame(node, builder.build().getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    JSType returnType = new JSType();
    builder.withReturnType(returnType);
    assertSame(returnType, builder.build().getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    JSType returnType = new JSType();
    builder.withInferredReturnType(returnType);
    assertSame(returnType, builder.build().getReturnType());
    assertTrue(builder.build().isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    FunctionBuilder builder = new FunctionBuilder(null);
    ObjectType typeOfThis = new ObjectType();
    builder.withTypeOfThis(typeOfThis);
    assertSame(typeOfThis, builder.build().getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = new FunctionBuilder(null);
    builder.withTemplateName("templateType");
    assertEquals("templateType", builder.build().getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder(null);
    builder.forConstructor();
    assertTrue(builder.build().isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder(null);
    builder.forNativeType();
    assertTrue(builder.build().isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder builder = new FunctionBuilder(null);
    FunctionType otherType = new FunctionType(null, "otherFunction", new Node(), new ArrowType(), new ObjectType(), "otherTemplate", false, false);
    builder.copyFromOtherFunction(otherType);
    assertEquals(otherType.getReferenceName(), builder.build().getReferenceName());
    assertSame(otherType.getSource(), builder.build().getSource());
    assertSame(otherType.getParametersNode(), builder.build().getParametersNode());
    assertSame(otherType.getReturnType(), builder.build().getReturnType());
    assertSame(otherType.getTypeOfThis(), builder.build().getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), builder.build().getTemplateTypeName());
    assertEquals(otherType.isConstructor(), builder.build().isConstructor());
    assertEquals(otherType.isNativeObjectType(), builder.build().isNativeObjectType());
  }

}