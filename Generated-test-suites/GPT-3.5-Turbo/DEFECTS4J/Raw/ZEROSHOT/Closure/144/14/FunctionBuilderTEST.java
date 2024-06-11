import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.jstype.*;

public class FunctionBuilderTest {

  private FunctionBuilder functionBuilder;

  @Before
  public void setUp() {
    functionBuilder = new FunctionBuilder(new JSTypeRegistry());
  }

  @Test
  public void testWithName() {
    String name = "testFunction";
    FunctionBuilder result = functionBuilder.withName(name);
    assertEquals(name, result.build().getName());
  }

  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node();
    FunctionBuilder result = functionBuilder.withSourceNode(sourceNode);
    assertEquals(sourceNode, result.build().getSourceNode());
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder params = new FunctionParamBuilder();
    Node parametersNode = params.build();
    FunctionBuilder result = functionBuilder.withParams(params);
    assertEquals(parametersNode, result.build().getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    Node parametersNode = new Node();
    FunctionBuilder result = functionBuilder.withParamsNode(parametersNode);
    assertEquals(parametersNode, result.build().getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = new JSType();
    FunctionBuilder result = functionBuilder.withReturnType(returnType);
    assertEquals(returnType, result.build().getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = new JSType();
    FunctionBuilder result = functionBuilder.withInferredReturnType(returnType);
    assertEquals(returnType, result.build().getReturnType());
    assertTrue(result.build().isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType();
    FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, result.build().getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    String templateTypeName = "template";
    FunctionBuilder result = functionBuilder.withTemplateName(templateTypeName);
    assertEquals(templateTypeName, result.build().getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder result = functionBuilder.forConstructor();
    assertTrue(result.build().isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder result = functionBuilder.forNativeType();
    assertTrue(result.build().isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType(new JSTypeRegistry(), "otherFunction",
        new Node(), new ArrowType(new JSTypeRegistry(), new Node(), new JSType(), false),
        new ObjectType(), "template", true, false);

    FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
    assertEquals(otherType.getName(), result.build().getName());
    assertEquals(otherType.getSourceNode(), result.build().getSourceNode());
    assertEquals(otherType.getParametersNode(), result.build().getParametersNode());
    assertEquals(otherType.getReturnType(), result.build().getReturnType());
    assertEquals(otherType.getTypeOfThis(), result.build().getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), result.build().getTemplateTypeName());
    assertEquals(otherType.isConstructor(), result.build().isConstructor());
    assertEquals(otherType.isNativeObjectType(), result.build().isNativeObjectType());
  }

}