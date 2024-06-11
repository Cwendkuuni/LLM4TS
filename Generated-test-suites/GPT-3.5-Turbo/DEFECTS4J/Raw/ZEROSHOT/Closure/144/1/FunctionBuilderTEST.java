import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Assert;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testWithParams() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    Node parametersNode = new Node();
    
    // Act
    FunctionBuilder result = functionBuilder.withParamsNode(parametersNode);
    
    // Assert
    Assert.assertEquals(functionBuilder, result);
    Assert.assertEquals(parametersNode, functionBuilder.getParamsNode());
  }

  @Test
  public void testWithReturnType() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    JSType returnType = new JSType();
    
    // Act
    FunctionBuilder result = functionBuilder.withReturnType(returnType);
    
    // Assert
    Assert.assertEquals(functionBuilder, result);
    Assert.assertEquals(returnType, functionBuilder.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    JSType returnType = new JSType();
    
    // Act
    FunctionBuilder result = functionBuilder.withInferredReturnType(returnType);
    
    // Assert
    Assert.assertEquals(functionBuilder, result);
    Assert.assertEquals(returnType, functionBuilder.getReturnType());
    Assert.assertTrue(functionBuilder.getInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    ObjectType typeOfThis = new ObjectType();
    
    // Act
    FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis);
    
    // Assert
    Assert.assertEquals(functionBuilder, result);
    Assert.assertEquals(typeOfThis, functionBuilder.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String templateTypeName = "template";
    
    // Act
    FunctionBuilder result = functionBuilder.withTemplateName(templateTypeName);
    
    // Assert
    Assert.assertEquals(functionBuilder, result);
    Assert.assertEquals(templateTypeName, functionBuilder.getTemplateName());
  }

  @Test
  public void testForConstructor() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    
    // Act
    FunctionBuilder result = functionBuilder.forConstructor();
    
    // Assert
    Assert.assertEquals(functionBuilder, result);
    Assert.assertTrue(functionBuilder.isConstructor());
  }

  @Test
  public void testForNativeType() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    
    // Act
    FunctionBuilder result = functionBuilder.forNativeType();
    
    // Assert
    Assert.assertEquals(functionBuilder, result);
    Assert.assertTrue(functionBuilder.isNativeType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionType otherType = new FunctionType();
    
    // Act
    FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
    
    // Assert
    Assert.assertEquals(functionBuilder, result);
    Assert.assertEquals(otherType.getReferenceName(), functionBuilder.getName());
    Assert.assertEquals(otherType.getSource(), functionBuilder.getSourceNode());
    Assert.assertEquals(otherType.getParametersNode(), functionBuilder.getParamsNode());
    Assert.assertEquals(otherType.getReturnType(), functionBuilder.getReturnType());
    Assert.assertEquals(otherType.getTypeOfThis(), functionBuilder.getTypeOfThis());
    Assert.assertEquals(otherType.getTemplateTypeName(), functionBuilder.getTemplateName());
    Assert.assertEquals(otherType.isConstructor(), functionBuilder.isConstructor());
    Assert.assertEquals(otherType.isNativeObjectType(), functionBuilder.isNativeType());
  }

  @Test
  public void testBuild() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    
    // Act
    FunctionType result = functionBuilder.build();
    
    // Assert
    Assert.assertNotNull(result);
    Assert.assertEquals(registry, result.getRegistry());
    Assert.assertEquals(functionBuilder.getName(), result.getReferenceName());
    Assert.assertEquals(functionBuilder.getSourceNode(), result.getSource());
    Assert.assertEquals(functionBuilder.getParamsNode(), result.getParametersNode());
    Assert.assertEquals(functionBuilder.getReturnType(), result.getReturnType());
    Assert.assertEquals(functionBuilder.getTypeOfThis(), result.getTypeOfThis());
    Assert.assertEquals(functionBuilder.getTemplateName(), result.getTemplateTypeName());
    Assert.assertEquals(functionBuilder.isConstructor(), result.isConstructor());
    Assert.assertEquals(functionBuilder.isNativeType(), result.isNativeObjectType());
  }
}