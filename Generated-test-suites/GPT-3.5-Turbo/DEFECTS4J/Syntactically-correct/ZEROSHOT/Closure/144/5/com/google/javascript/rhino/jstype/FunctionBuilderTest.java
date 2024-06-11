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
  public void setUp() {
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testWithName() {
    FunctionBuilder builder = functionBuilder.withName("testName");
    assertEquals("testName", builder.build().getReferenceName());
  }

  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node();
    FunctionBuilder builder = functionBuilder.withSourceNode(sourceNode);
    assertEquals(sourceNode, builder.build().getSource());
  }

  @Test
  public void testWithParams() {
    Node parametersNode = new Node();
    FunctionParamBuilder params = new FunctionParamBuilder(registry).withNode(parametersNode);
    FunctionBuilder builder = functionBuilder.withParams(params);
    assertEquals(parametersNode, builder.build().getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    Node parametersNode = new Node();
    FunctionBuilder builder = functionBuilder.withParamsNode(parametersNode);
    assertEquals(parametersNode, builder.build().getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = registry.getNativeType("Number");
    FunctionBuilder builder = functionBuilder.withReturnType(returnType);
    assertEquals(returnType, builder.build().getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = registry.getNativeType("String");
    FunctionBuilder builder = functionBuilder.withInferredReturnType(returnType);
    assertEquals(returnType, builder.build().getReturnType());
    assertTrue(builder.build().isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType();
    FunctionBuilder builder = functionBuilder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, builder.build().getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    FunctionBuilder builder = functionBuilder.withTemplateName("templateName");
    assertEquals("templateName", builder.build().getTemplateTypeName());
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
    FunctionType otherType = new FunctionType(registry);
    otherType.setReferenceName("otherName");
    FunctionBuilder builder = functionBuilder.copyFromOtherFunction(otherType);
    assertEquals(otherType.getReferenceName(), builder.build().getReferenceName());
    assertEquals(otherType.getSource(), builder.build().getSource());
    assertEquals(otherType.getParametersNode(), builder.build().getParametersNode());
    assertEquals(otherType.getReturnType(), builder.build().getReturnType());
    assertEquals(otherType.getTypeOfThis(), builder.build().getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), builder.build().getTemplateTypeName());
    assertEquals(otherType.isConstructor(), builder.build().isConstructor());
    assertEquals(otherType.isNativeObjectType(), builder.build().isNativeObjectType());
  }
}