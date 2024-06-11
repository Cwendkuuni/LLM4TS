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
  private FunctionBuilder builder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    builder = new FunctionBuilder(registry);
  }

  @Test
  public void testWithName() {
    builder.withName("test");
    assertEquals("test", builder.getName());
  }

  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node("SOURCE");
    builder.withSourceNode(sourceNode);
    assertEquals(sourceNode, builder.getSourceNode());
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    paramBuilder.addParam("param1", registry.getNumberType());
    paramBuilder.addParam("param2", registry.getStringType());
    builder.withParams(paramBuilder);
    assertEquals(paramBuilder.build(), builder.getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    Node parametersNode = new Node("PARAMETERS");
    builder.withParamsNode(parametersNode);
    assertEquals(parametersNode, builder.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = registry.getStringType();
    builder.withReturnType(returnType);
    assertEquals(returnType, builder.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = registry.getNumberType();
    builder.withInferredReturnType(returnType);
    assertTrue(builder.isInferredReturnType());
    assertEquals(returnType, builder.getReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType(registry);
    builder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, builder.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    String templateName = "template";
    builder.withTemplateName(templateName);
    assertEquals(templateName, builder.getTemplateName());
  }

  @Test
  public void testForConstructor() {
    builder.forConstructor();
    assertTrue(builder.isConstructor());
  }

  @Test
  public void testForNativeType() {
    builder.forNativeType();
    assertTrue(builder.isNativeType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType(registry, "other");
    builder.copyFromOtherFunction(otherType);
    assertEquals(otherType, builder.build());
  }

  @Test
  public void testBuild() {
    FunctionType expectedType = new FunctionType(registry);
    assertEquals(expectedType, builder.build());
  }

}