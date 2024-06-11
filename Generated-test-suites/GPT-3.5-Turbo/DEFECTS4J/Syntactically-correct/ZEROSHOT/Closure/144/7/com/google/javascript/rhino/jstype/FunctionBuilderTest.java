import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.ArrowType;

public class FunctionBuilderTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testWithParamsNode() {
    Node parametersNode = new Node();
    FunctionType expected = new FunctionType(registry, null, null, new ArrowType(registry, parametersNode, null, false), null, null, false, false);
    FunctionType actual = new FunctionBuilder(registry).withParamsNode(parametersNode).build();
    assertEquals(expected, actual);
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder params = new FunctionParamBuilder();
    Node parametersNode = params.build();
    FunctionType expected = new FunctionType(registry, null, null, new ArrowType(registry, parametersNode, null, false), null, null, false, false);
    FunctionType actual = new FunctionBuilder(registry).withParams(params).build();
    assertEquals(expected, actual);
  }

  @Test
  public void testWithName() {
    String name = "testName";
    FunctionType expected = new FunctionType(registry, name, null, new ArrowType(registry, null, null, false), null, null, false, false);
    FunctionType actual = new FunctionBuilder(registry).withName(name).build();
    assertEquals(expected, actual);
  }

  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node();
    FunctionType expected = new FunctionType(registry, null, sourceNode, new ArrowType(registry, null, null, false), null, null, false, false);
    FunctionType actual = new FunctionBuilder(registry).withSourceNode(sourceNode).build();
    assertEquals(expected, actual);
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = new JSType();
    FunctionType expected = new FunctionType(registry, null, null, new ArrowType(registry, null, returnType, false), null, null, false, false);
    FunctionType actual = new FunctionBuilder(registry).withReturnType(returnType).build();
    assertEquals(expected, actual);
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = new JSType();
    FunctionType expected = new FunctionType(registry, null, null, new ArrowType(registry, null, returnType, true), null, null, false, false);
    FunctionType actual = new FunctionBuilder(registry).withInferredReturnType(returnType).build();
    assertEquals(expected, actual);
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType();
    FunctionType expected = new FunctionType(registry, null, null, new ArrowType(registry, null, null, false), typeOfThis, null, false, false);
    FunctionType actual = new FunctionBuilder(registry).withTypeOfThis(typeOfThis).build();
    assertEquals(expected, actual);
  }

  @Test
  public void testWithTemplateName() {
    String templateTypeName = "template";
    FunctionType expected = new FunctionType(registry, null, null, new ArrowType(registry, null, null, false), null, templateTypeName, false, false);
    FunctionType actual = new FunctionBuilder(registry).withTemplateName(templateTypeName).build();
    assertEquals(expected, actual);
  }

  @Test
  public void testForConstructor() {
    FunctionType expected = new FunctionType(registry, null, null, new ArrowType(registry, null, null, false), null, null, true, false);
    FunctionType actual = new FunctionBuilder(registry).forConstructor().build();
    assertEquals(expected, actual);
  }

  @Test
  public void testForNativeType() {
    FunctionType expected = new FunctionType(registry, null, null, new ArrowType(registry, null, null, false), null, null, false, true);
    FunctionType actual = new FunctionBuilder(registry).forNativeType().build();
    assertEquals(expected, actual);
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType(registry, "other", new Node(), new ArrowType(registry, new Node(), new JSType(), false), new ObjectType(), "template", true, false);
    FunctionType expected = new FunctionType(registry, "other", new Node(), new ArrowType(registry, new Node(), new JSType(), false), new ObjectType(), "template", true, false);
    FunctionType actual = new FunctionBuilder(registry).copyFromOtherFunction(otherType).build();
    assertEquals(expected, actual);
  }
}