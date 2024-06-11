import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;
  private FunctionType functionType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
    functionType = null;
  }

  @Test
  public void testWithName() {
    functionType = functionBuilder.withName("testFunction").build();
    assertEquals("testFunction", functionType.getReferenceName());
  }

  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node();
    functionType = functionBuilder.withSourceNode(sourceNode).build();
    assertEquals(sourceNode, functionType.getSource());
  }

  @Test
  public void testWithParams() {
    Node parametersNode = new Node();
    functionType = functionBuilder.withParamsNode(parametersNode).build();
    assertEquals(parametersNode, functionType.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = registry.createNumberType();
    functionType = functionBuilder.withReturnType(returnType).build();
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = registry.createNumberType();
    functionType = functionBuilder.withInferredReturnType(returnType).build();
    assertTrue(functionType.getInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = registry.createObjectType();
    functionType = functionBuilder.withTypeOfThis(typeOfThis).build();
    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    functionType = functionBuilder.withTemplateName("TestTemplate").build();
    assertEquals("TestTemplate", functionType.getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    functionType = functionBuilder.forConstructor().build();
    assertTrue(functionType.isConstructor());
  }

  @Test
  public void testForNativeType() {
    functionType = functionBuilder.forNativeType().build();
    assertTrue(functionType.isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType(registry, "otherFunction", new Node(),
        new ArrowType(registry, new Node(), registry.createStringType(), false),
        registry.createObjectType(), "OtherTemplate", true, true);
    functionType = functionBuilder.copyFromOtherFunction(otherType).build();
    assertEquals("otherFunction", functionType.getReferenceName());
    assertEquals(otherType.getSource(), functionType.getSource());
    assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
    assertEquals(otherType.getReturnType(), functionType.getReturnType());
    assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
    assertEquals(otherType.isConstructor(), functionType.isConstructor());
    assertEquals(otherType.isNativeObjectType(), functionType.isNativeObjectType());
  }

  @Test
  public void testBuild() {
    functionType = functionBuilder.build();
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

}