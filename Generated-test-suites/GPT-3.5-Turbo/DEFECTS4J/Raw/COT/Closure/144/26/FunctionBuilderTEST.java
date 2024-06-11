import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.jstype.*;

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
    FunctionBuilder builder = functionBuilder.withName("testFunction");
    assertEquals("testFunction", builder.build().getReferenceName());
  }

  @Test
  public void testWithSourceNode() {
    Node sourceNode = Node.newString("source");
    FunctionBuilder builder = functionBuilder.withSourceNode(sourceNode);
    assertSame(sourceNode, builder.build().getSource());
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    Node parametersNode = paramBuilder.build();
    FunctionBuilder builder = functionBuilder.withParams(paramBuilder);
    assertSame(parametersNode, builder.build().getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    Node parametersNode = Node.newString("params");
    FunctionBuilder builder = functionBuilder.withParamsNode(parametersNode);
    assertSame(parametersNode, builder.build().getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = registry.createStringType();
    FunctionBuilder builder = functionBuilder.withReturnType(returnType);
    assertSame(returnType, builder.build().getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = registry.createStringType();
    FunctionBuilder builder = functionBuilder.withInferredReturnType(returnType);
    assertSame(returnType, builder.build().getReturnType());
    assertTrue(builder.build().isReturnTypeInferred());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = registry.createObjectObjectType();
    FunctionBuilder builder = functionBuilder.withTypeOfThis(typeOfThis);
    assertSame(typeOfThis, builder.build().getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = functionBuilder.withTemplateName("template");
    assertEquals("template", builder.build().getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder builder = functionBuilder.forConstructor();
    assertTrue(builder.build().isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder builder = functionBuilder.forNativeType();
    assertTrue(builder.build().isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType(registry, "otherFunction",
        Node.newString("source"), new ArrowType(registry, Node.newString("params"),
        registry.createStringType(), false), registry.createObjectObjectType(),
        "template", true, true);
    FunctionBuilder builder = functionBuilder.copyFromOtherFunction(otherType);
    assertEquals("otherFunction", builder.build().getReferenceName());
    assertSame(otherType.getSource(), builder.build().getSource());
    assertSame(otherType.getParametersNode(), builder.build().getParametersNode());
    assertSame(otherType.getReturnType(), builder.build().getReturnType());
    assertSame(otherType.getTypeOfThis(), builder.build().getTypeOfThis());
    assertEquals("template", builder.build().getTemplateTypeName());
    assertTrue(builder.build().isConstructor());
    assertTrue(builder.build().isNativeObjectType());
  }

}