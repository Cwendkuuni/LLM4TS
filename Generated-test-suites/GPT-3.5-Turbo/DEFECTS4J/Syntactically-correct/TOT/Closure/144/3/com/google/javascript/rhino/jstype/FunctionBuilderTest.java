import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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
  
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }
  
  @Test
  public void testWithParams() {
    Node paramsNode = new Node(Token.PARAM_LIST);
    FunctionBuilder builder = new FunctionBuilder(registry)
    	.withParamsNode(paramsNode);
    FunctionType functionType = builder.build();
    
    assertThat(functionType.getParametersNode(), is(equalTo(paramsNode)));
  }
  
  @Test
  public void testWithReturnType() {
    JSType returnType = registry.getNativeType("string");
    FunctionBuilder builder = new FunctionBuilder(registry)
    	.withReturnType(returnType);
    FunctionType functionType = builder.build();
    
    assertThat(functionType.getReturnType(), is(equalTo(returnType)));
  }
  
  @Test
  public void testWithInferredReturnType() {
    JSType returnType = registry.getNativeType("string");
    FunctionBuilder builder = new FunctionBuilder(registry)
    	.withInferredReturnType(returnType);
    FunctionType functionType = builder.build();
    
    assertThat(functionType.getReturnType(), is(equalTo(returnType)));
    assertThat(functionType.isInferredReturnType(), is(true));
  }
  
  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = registry.createObjectType("TestObject");
    FunctionBuilder builder = new FunctionBuilder(registry)
    	.withTypeOfThis(typeOfThis);
    FunctionType functionType = builder.build();
    
    assertThat(functionType.getTypeOfThis(), is(equalTo(typeOfThis)));
  }
  
  @Test
  public void testWithTemplateName() {
    String templateName = "T";
    FunctionBuilder builder = new FunctionBuilder(registry)
    	.withTemplateName(templateName);
    FunctionType functionType = builder.build();
    
    assertThat(functionType.getTemplateTypeName(), is(equalTo(templateName)));
  }
  
  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder(registry)
    	.forConstructor();
    FunctionType functionType = builder.build();
    
    assertThat(functionType.isConstructor(), is(true));
  }
  
  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder(registry)
    	.forNativeType();
    FunctionType functionType = builder.build();
    
    assertThat(functionType.isNativeObjectType(), is(true));
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType(registry, "otherType", 
    	new Node(Token.PARAM_LIST), registry.getNativeType("number"), null, null, false, false);
    FunctionBuilder builder = new FunctionBuilder(registry)
    	.copyFromOtherFunction(otherType);
    FunctionType functionType = builder.build();
    
    assertThat(functionType.getReferenceName(), is(equalTo("otherType")));
    assertThat(functionType.getParametersNode(), is(equalTo(otherType.getParametersNode())));
    assertThat(functionType.getReturnType(), is(equalTo(otherType.getReturnType())));
    assertThat(functionType.getTypeOfThis(), is(equalTo(otherType.getTypeOfThis())));
    assertThat(functionType.getTemplateTypeName(), is(equalTo(otherType.getTemplateTypeName())));
    assertThat(functionType.isConstructor(), is(equalTo(otherType.isConstructor())));
    assertThat(functionType.isNativeObjectType(), is(equalTo(otherType.isNativeObjectType())));
  }
  
  @Test
  public void testBuild() {
    FunctionBuilder builder = new FunctionBuilder(registry)
    	.withName("testFunction")
    	.withSourceNode(new Node(Token.FUNCTION))
    	.withParams(new FunctionParamBuilder(registry))
    	.withReturnType(registry.getNativeType("boolean"))
    	.withTypeOfThis(registry.createObjectType("ObjectType"))
    	.withTemplateName("T");
    FunctionType functionType = builder.build();
    
    assertThat(functionType.getReferenceName(), is(equalTo("testFunction")));
    assertThat(functionType.getSource(), is(notNullValue()));
    assertThat(functionType.getParametersNode(), is(notNullValue()));
    assertThat(functionType.getReturnType(), is(notNullValue()));
    assertThat(functionType.getTypeOfThis(), is(notNullValue()));
    assertThat(functionType.getTemplateTypeName(), is(notNullValue()));
    assertThat(functionType.isConstructor(), is(false));
    assertThat(functionType.isNativeObjectType(), is(false));
  }
}