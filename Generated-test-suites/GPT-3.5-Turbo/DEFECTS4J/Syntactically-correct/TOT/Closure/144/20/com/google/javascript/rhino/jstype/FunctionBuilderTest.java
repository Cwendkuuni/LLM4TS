import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;

  @Before
  public void setup() {
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testName() {
    String name = "testFunction";
    FunctionBuilder builderWithName = functionBuilder.withName(name);
    assertEquals(name, builderWithName.build().getName());
  }

  @Test
  public void testSourceNode() {
    Node sourceNode = new Node();
    FunctionBuilder builderWithSourceNode = functionBuilder.withSourceNode(sourceNode);
    assertEquals(sourceNode, builderWithSourceNode.build().getSourceNode());
  }

  @Test
  public void testParams() {
    FunctionParamBuilder params = new FunctionParamBuilder(registry);
    FunctionBuilder builderWithParams = functionBuilder.withParams(params);
    assertEquals(params.build(), builderWithParams.build().getParametersNode());
  }

  @Test
  public void testParamsNode() {
    Node parametersNode = new Node();
    FunctionBuilder builderWithParamsNode = functionBuilder.withParamsNode(parametersNode);
    assertEquals(parametersNode, builderWithParamsNode.build().getParametersNode());
  }

  @Test
  public void testReturnType() {
    JSType returnType = registry.getNativeVoidType();
    FunctionBuilder builderWithReturnType = functionBuilder.withReturnType(returnType);
    assertEquals(returnType, builderWithReturnType.build().getReturnType());
  }

  @Test
  public void testInferredReturnType() {
    JSType returnType = registry.getNativeStringType();
    FunctionBuilder builderWithInferredReturnType = functionBuilder.withInferredReturnType(returnType);
    assertTrue(builderWithInferredReturnType.build().isReturnTypeInferred());
  }

  @Test
  public void testTypeOfThis() {
    ObjectType typeOfThis = new ObjectType(registry);
    FunctionBuilder builderWithTypeOfThis = functionBuilder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, builderWithTypeOfThis.build().getTypeOfThis());
  }

  @Test
  public void testTemplateName() {
    String templateName = "Template";
    FunctionBuilder builderWithTemplateName = functionBuilder.withTemplateName(templateName);
    assertEquals(templateName, builderWithTemplateName.build().getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder builderForConstructor = functionBuilder.forConstructor();
    assertTrue(builderForConstructor.build().isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder builderForNativeType = functionBuilder.forNativeType();
    assertTrue(builderForNativeType.build().isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.withName("otherFunction");
    builder.withSourceNode(new Node());
    builder.withParams(new FunctionParamBuilder(registry));
    builder.withReturnType(registry.getNativeNumberType());
    builder.withTypeOfThis(new ObjectType(registry));
    builder.withTemplateName("OtherTemplate");
    builder.forConstructor();
    builder.forNativeType();

    FunctionBuilder copyBuilder = functionBuilder.copyFromOtherFunction(builder.build());
    assertEquals(builder.build().getName(), copyBuilder.build().getName());
    assertEquals(builder.build().getSourceNode(), copyBuilder.build().getSourceNode());
    assertEquals(builder.build().getParametersNode(), copyBuilder.build().getParametersNode());
    assertEquals(builder.build().getReturnType(), copyBuilder.build().getReturnType());
    assertEquals(builder.build().getTypeOfThis(), copyBuilder.build().getTypeOfThis());
    assertEquals(builder.build().getTemplateTypeName(), copyBuilder.build().getTemplateTypeName());
    assertEquals(builder.build().isConstructor(), copyBuilder.build().isConstructor());
    assertEquals(builder.build().isNativeObjectType(), copyBuilder.build().isNativeObjectType());
  }
}