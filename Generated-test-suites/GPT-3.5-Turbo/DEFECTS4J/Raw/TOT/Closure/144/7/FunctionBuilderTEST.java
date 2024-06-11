import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.objecttype.ObjectType;

import org.junit.Before;
import org.junit.Test;

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
    String name = "myFunction";
    FunctionType functionType = functionBuilder.withName(name).build();
    assertEquals(functionType.getReferenceName(), name);
  }
  
  @Test
  public void testSourceNode() {
    Node sourceNode = new Node(Node.SCRIPT);
    FunctionType functionType = functionBuilder.withSourceNode(sourceNode).build();
    assertEquals(functionType.getSource(), sourceNode);
  }
  
  @Test
  public void testParams() {
    Node parametersNode = new Node(Node.SCRIPT);
    FunctionType functionType = functionBuilder.withParamsNode(parametersNode).build();
    assertEquals(functionType.getParametersNode(), parametersNode);
  }
  
  @Test
  public void testReturnType() {
    JSType returnType = new JSType();
    FunctionType functionType = functionBuilder.withReturnType(returnType).build();
    assertEquals(functionType.getReturnType(), returnType);
  }
  
  @Test
  public void testInferredReturnType() {
    JSType returnType = new JSType();
    FunctionType functionType = functionBuilder.withInferredReturnType(returnType).build();
    assertEquals(functionType.getReturnType(), returnType);
    assertTrue(functionType.isReturnTypeInferred());
  }
  
  @Test
  public void testTypeOfThis() {
    ObjectType typeOfThis = new ObjectType();
    FunctionType functionType = functionBuilder.withTypeOfThis(typeOfThis).build();
    assertEquals(functionType.getTypeOfThis(), typeOfThis);
  }
  
  @Test
  public void testTemplateName() {
    String templateName = "myTemplate";
    FunctionType functionType = functionBuilder.withTemplateName(templateName).build();
    assertEquals(functionType.getTemplateTypeName(), templateName);
  }
  
  @Test
  public void testForConstructor() {
    FunctionType functionType = functionBuilder.forConstructor().build();
    assertTrue(functionType.isConstructor());
  }
  
  @Test
  public void testForNativeType() {
    FunctionType functionType = functionBuilder.forNativeType().build();
    assertTrue(functionType.isNativeObjectType());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType(registry, "otherFunction", new Node(Node.SCRIPT), 
        new ArrowType(registry, new Node(Node.SCRIPT), new JSType(), false), 
        new ObjectType(), "otherTemplateType", true, true);
    
    FunctionType functionType = functionBuilder.copyFromOtherFunction(otherType).build();
    assertEquals(functionType.getReferenceName(), otherType.getReferenceName());
    assertEquals(functionType.getSource(), otherType.getSource());
    assertEquals(functionType.getParametersNode(), otherType.getParametersNode());
    assertEquals(functionType.getReturnType(), otherType.getReturnType());
    assertEquals(functionType.getTypeOfThis(), otherType.getTypeOfThis());
    assertEquals(functionType.getTemplateTypeName(), otherType.getTemplateTypeName());
    assertEquals(functionType.isConstructor(), otherType.isConstructor());
    assertEquals(functionType.isNativeObjectType(), otherType.isNativeObjectType());
  }
  
  @Test
  public void testDefaultBuild() {
    FunctionType functionType = functionBuilder.build();
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