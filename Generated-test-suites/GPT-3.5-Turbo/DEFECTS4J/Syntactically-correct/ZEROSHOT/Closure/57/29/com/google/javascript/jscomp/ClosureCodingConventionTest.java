import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClosureCodingConventionTest {
  
  private ClosureCodingConvention codingConvention;
  
  @Before
  public void setUp() {
    codingConvention = new ClosureCodingConvention();
  }
  
  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = null;
    FunctionType childCtor = null;
    SubclassType type = null;
    
    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);
    
    // TODO: Assert the results
  }
  
  @Test
  public void testGetclasssDefinedByCall() {
    Node callNode = null;
    
    SubclassRelationship result = codingConvention.getclasssDefinedByCall(callNode);
    
    // TODO: Assert the results
  }
  
  @Test
  public void testIsSuperClassReference() {
    String propertyName = "superClass_";
    
    boolean result = codingConvention.isSuperClassReference(propertyName);
    
    Assert.assertTrue(result);
  }
  
  @Test
  public void testExtractClassNameIfProvide() {
    Node node = null;
    Node parent = null;
    
    String result = codingConvention.extractClassNameIfProvide(node, parent);
    
    // TODO: Assert the results
  }
  
  @Test
  public void testExtractClassNameIfRequire() {
    Node node = null;
    Node parent = null;
    
    String result = codingConvention.extractClassNameIfRequire(node, parent);
    
    // TODO: Assert the results
  }
  
  @Test
  public void testGetExportPropertyFunction() {
    String result = codingConvention.getExportPropertyFunction();
    
    // TODO: Assert the results
  }
  
  @Test
  public void testGetExportSymbolFunction() {
    String result = codingConvention.getExportSymbolFunction();
    
    // TODO: Assert the results
  }
  
  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node n = null;
    
    List<String> result = codingConvention.identifyTypeDeclarationCall(n);
    
    // TODO: Assert the results
  }
  
  @Test
  public void testGetAbstractMethodName() {
    String result = codingConvention.getAbstractMethodName();
    
    // TODO: Assert the results
  }
  
  @Test
  public void testGetSingletonGetterClassName() {
    Node callNode = null;
    
    String result = codingConvention.getSingletonGetterClassName(callNode);
    
    // TODO: Assert the results
  }
  
  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = null;
    FunctionType getterType = null;
    ObjectType objectType = null;
    
    codingConvention.applySingletonGetter(functionType, getterType, objectType);
    
    // TODO: Assert the results
  }
  
  @Test
  public void testGetGlobalObject() {
    String result = codingConvention.getGlobalObject();
    
    // TODO: Assert the results
  }
  
  @Test
  public void testIsPropertyTestFunction() {
    Node call = null;
    
    boolean result = codingConvention.isPropertyTestFunction(call);
    
    // TODO: Assert the results
  }
  
  @Test
  public void testGetObjectLiteralCast() {
    NodeTraversal t = null;
    Node callNode = null;
    
    ObjectLiteralCast result = codingConvention.getObjectLiteralCast(t, callNode);
    
    // TODO: Assert the results
  }
  
  @Test
  public void testIsOptionalParameter() {
    Node parameter = null;
    
    boolean result = codingConvention.isOptionalParameter(parameter);
    
    // TODO: Assert the results
  }
  
  @Test
  public void testIsVarArgsParameter() {
    Node parameter = null;
    
    boolean result = codingConvention.isVarArgsParameter(parameter);
    
    // TODO: Assert the results
  }
  
  @Test
  public void testIsPrivate() {
    String name = null;
    
    boolean result = codingConvention.isPrivate(name);
    
    // TODO: Assert the results
  }
  
  @Test
  public void testGetAssertionFunctions() {
    Collection<AssertionFunctionSpec> result = codingConvention.getAssertionFunctions();
    
    // TODO: Assert the results
  }
  
  @Test
  public void testDescribeFunctionBind() {
    Node n = null;
    
    Bind result = codingConvention.describeFunctionBind(n);
    
    // TODO: Assert the results
  }
}