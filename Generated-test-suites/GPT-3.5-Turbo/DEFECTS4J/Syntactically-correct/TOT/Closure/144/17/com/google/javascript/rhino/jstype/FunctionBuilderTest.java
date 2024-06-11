import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {
  
  @Test
  public void testWithName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    String name = "testFunction";
    builder.withName(name);
    assertEquals(name, builder.build().getName());
  }
  
  @Test
  public void testWithSourceNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    Node sourceNode = new Node("sourceNode");
    builder.withSourceNode(sourceNode);
    assertEquals(sourceNode, builder.build().getSourceNode());
  }
  
  @Test
  public void testWithParams() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionParamBuilder params = new FunctionParamBuilder();
    Node parametersNode = params.build();
    builder.withParams(params);
    assertEquals(parametersNode, builder.build().getParametersNode());
  }
  
  @Test
  public void testWithParamsNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    Node parametersNode = new Node("parametersNode");
    builder.withParamsNode(parametersNode);
    assertEquals(parametersNode, builder.build().getParametersNode());
  }
  
  @Test
  public void testWithReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    JSType returnType = registry.createNumberType();
    builder.withReturnType(returnType);
    assertEquals(returnType, builder.build().getReturnType());
  }
  
  @Test
  public void testWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    JSType returnType = registry.createNumberType();
    builder.withInferredReturnType(returnType);
    assertEquals(returnType, builder.build().getReturnType());
    assertTrue(builder.build().hasInferredReturnType());
  }
  
  @Test
  public void testWithTypeOfThis() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    ObjectType typeOfThis = registry.createObjectType();
    builder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, builder.build().getTypeOfThis());
  }
  
  @Test
  public void testWithTemplateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    String templateTypeName = "template";
    builder.withTemplateName(templateTypeName);
    assertEquals(templateTypeName, builder.build().getTemplateTypeName());
  }
  
  @Test
  public void testForConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.forConstructor();
    assertTrue(builder.build().isConstructor());
  }
  
  @Test
  public void testForNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.forNativeType();
    assertTrue(builder.build().isNativeObjectType());
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    FunctionType otherType = generateOtherFunctionType();
    builder.copyFromOtherFunction(otherType);
    assertEquals(otherType.getReferenceName(), builder.build().getReferenceName());
    assertEquals(otherType.getSource(), builder.build().getSource());
    assertEquals(otherType.getParametersNode(), builder.build().getParametersNode());
    assertEquals(otherType.getReturnType(), builder.build().getReturnType());
    assertEquals(otherType.getTypeOfThis(), builder.build().getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), builder.build().getTemplateTypeName());
    assertEquals(otherType.isConstructor(), builder.build().isConstructor());
    assertEquals(otherType.isNativeObjectType(), builder.build().isNativeObjectType());
  }
  
  private FunctionType generateOtherFunctionType() {
    // Generate a mock FunctionType for testing copyFromOtherFunction()
    JSTypeRegistry registry = new JSTypeRegistry();
    String name = "otherFunction";
    Node sourceNode = new Node("otherSourceNode");
    FunctionParamBuilder params = new FunctionParamBuilder();
    Node parametersNode = params.build();
    JSType returnType = registry.createStringType();
    ObjectType typeOfThis = registry.createObjectType();
    String templateTypeName = "otherTemplate";
    boolean isConstructor = true;
    boolean isNativeType = true;
    
    return new FunctionType(registry, name, sourceNode,
        new ArrowType(registry, parametersNode, returnType, false),
        typeOfThis, templateTypeName, isConstructor, isNativeType);
  }
}