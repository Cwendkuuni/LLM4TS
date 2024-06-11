import org.junit.Before;
import org.junit.Test;

public class ClosureCodingConventionTest {

  private ClosureCodingConvention closureCodingConvention;

  @Before
  public void setUp() {
    closureCodingConvention = new ClosureCodingConvention();
  }

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType();
    FunctionType childCtor = new FunctionType();
    closureCodingConvention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node callNode = new Node();
    closureCodingConvention.getclasssDefinedByCall(callNode);
  }

  @Test
  public void testIsSuperClassReference() {
    boolean result = closureCodingConvention.isSuperClassReference("superClass_");
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = new Node();
    Node parent = new Node();
    String className = closureCodingConvention.extractClassNameIfProvide(node, parent);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = new Node();
    Node parent = new Node();
    String className = closureCodingConvention.extractClassNameIfRequire(node, parent);
  }

  @Test
  public void testGetExportPropertyFunction() {
    String exportPropertyFunction = closureCodingConvention.getExportPropertyFunction();
  }

  @Test
  public void testGetExportSymbolFunction() {
    String exportSymbolFunction = closureCodingConvention.getExportSymbolFunction();
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node n = new Node();
    List<String> typeNames = closureCodingConvention.identifyTypeDeclarationCall(n);
  }

  @Test
  public void testGetAbstractMethodName() {
    String abstractMethodName = closureCodingConvention.getAbstractMethodName();
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node callNode = new Node();
    String singletonGetterClassName = closureCodingConvention.getSingletonGetterClassName(callNode);
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = new FunctionType();
    FunctionType getterType = new FunctionType();
    ObjectType objectType = new ObjectType();
    closureCodingConvention.applySingletonGetter(functionType, getterType, objectType);
  }

  @Test
  public void testGetGlobalObject() {
    String globalObject = closureCodingConvention.getGlobalObject();
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node call = new Node();
    boolean result = closureCodingConvention.isPropertyTestFunction(call);
  }

  @Test
  public void testGetObjectLiteralCast() {
    NodeTraversal t = new NodeTraversal();
    Node callNode = new Node();
    ClosureCodingConvention.ObjectLiteralCast objectLiteralCast = closureCodingConvention.getObjectLiteralCast(t, callNode);
  }

  @Test
  public void testIsOptionalParameter() {
    Node parameter = new Node();
    boolean result = closureCodingConvention.isOptionalParameter(parameter);
  }

  @Test
  public void testIsVarArgsParameter() {
    Node parameter = new Node();
    boolean result = closureCodingConvention.isVarArgsParameter(parameter);
  }

  @Test
  public void testIsPrivate() {
    String name = "name";
    boolean result = closureCodingConvention.isPrivate(name);
  }

  @Test
  public void testGetAssertionFunctions() {
    Collection<ClosureCodingConvention.AssertionFunctionSpec> assertionFunctions = closureCodingConvention.getAssertionFunctions();
  }

  @Test
  public void testDescribeFunctionBind() {
    Node n = new Node();
    ClosureCodingConvention.Bind bind = closureCodingConvention.describeFunctionBind(n);
  }
}