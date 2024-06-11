import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  private CrossModuleMethodMotion.IdGenerator idGenerator;
  private CrossModuleMethodMotion crossModuleMethodMotion;

  @Before
  public void setUp() {
    idGenerator = new CrossModuleMethodMotion.IdGenerator();
    crossModuleMethodMotion = new CrossModuleMethodMotion(null, idGenerator, true);
  }

  @Test
  public void testMoveMethods_WithNullCommonModuleError_ShouldReportError() {
    CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
    CrossModuleMethodMotion.Property prop = new CrossModuleMethodMotion.Property();
    prop.setValue(new Node(Token.STRING));
    nameInfo.addDeclaration(prop);
    nameInfo.setDeepestCommonModuleRef(null);

    crossModuleMethodMotion.moveMethods(Collections.singletonList(nameInfo));

    // Verify that the error is reported.
    assertEquals(1, compiler.getErrorCount());
    assertEquals(
        DiagnosticType.error(
            "JSC_INTERNAL_ERROR_MODULE_DEPEND",
            "null deepest common module"),
        compiler.getErrors()[0].getType());
  }

  @Test
  public void testMoveMethods_WithFunctionDeclaration_ShouldMoveMethod() {
    CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
    CrossModuleMethodMotion.Property prop = new CrossModuleMethodMotion.Property();
    prop.setValue(IR.function(IR.name("foo"), IR.block()));
    prop.setModule(new JSModule("module1"));
    nameInfo.addDeclaration(prop);
    nameInfo.setDeepestCommonModuleRef(new JSModule("module2"));

    crossModuleMethodMotion.moveMethods(Collections.singletonList(nameInfo));

    // Verify that the method is moved to the deepest common module.
    assertTrue(prop.getValue().getParent().equals(
        deepestCommonModuleRef));
  }

  @Test
  public void testMoveMethods_WithNonFunctionDeclaration_ShouldNotMoveMethod() {
    CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
    CrossModuleMethodMotion.Property prop = new CrossModuleMethodMotion.Property();
    prop.setValue(new Node(Token.STRING));
    prop.setModule(new JSModule("module1"));
    nameInfo.addDeclaration(prop);
    nameInfo.setDeepestCommonModuleRef(new JSModule("module2"));

    crossModuleMethodMotion.moveMethods(Collections.singletonList(nameInfo));

    // Verify that the method is not moved.
    assertFalse(prop.getValue().getParent().equals(
        deepestCommonModuleRef));
  }

  @Test
  public void testMoveMethods_WithGetterDeclaration_ShouldNotMoveMethod() {
    CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
    CrossModuleMethodMotion.Property prop = new CrossModuleMethodMotion.Property();
    prop.setValue(IR.getterDef("getFoo", IR.function(IR.name(""), IR.block())));
    prop.setModule(new JSModule("module1"));
    nameInfo.addDeclaration(prop);
    nameInfo.setDeepestCommonModuleRef(new JSModule("module2"));

    crossModuleMethodMotion.moveMethods(Collections.singletonList(nameInfo));

    // Verify that the method is not moved.
    assertFalse(prop.getValue().getParent().equals(
        deepestCommonModuleRef));
  }

  @Test
  public void testMoveMethods_WithSetterDeclaration_ShouldNotMoveMethod() {
    CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
    CrossModuleMethodMotion.Property prop = new CrossModuleMethodMotion.Property();
    prop.setValue(IR.setterDef("setFoo", IR.function(IR.name(""), IR.block())));
    prop.setModule(new JSModule("module1"));
    nameInfo.addDeclaration(prop);
    nameInfo.setDeepestCommonModuleRef(new JSModule("module2"));

    crossModuleMethodMotion.moveMethods(Collections.singletonList(nameInfo));

    // Verify that the method is not moved.
    assertFalse(prop.getValue().getParent().equals(
        deepestCommonModuleRef));
  }

  @Test
  public void testMoveMethods_WithStubGeneration_ShouldDeclareStubFunctions() {
    CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
    CrossModuleMethodMotion.Property prop = new CrossModuleMethodMotion.Property();
    prop.setValue(IR.function(IR.name("foo"), IR.block()));
    prop.setModule(new JSModule("module1"));
    nameInfo.addDeclaration(prop);
    nameInfo.setDeepestCommonModuleRef(new JSModule("module2"));

    crossModuleMethodMotion.moveMethods(Collections.singletonList(nameInfo));

    // Verify that the stub functions are declared in the top-most module.
    assertTrue(topModuleCode.contains("function JSCompiler_stubMethod"));
    assertTrue(topModuleCode.contains("function JSCompiler_unstubMethod"));
  }

  @Test
  public void testMoveMethods_WithNoStubGeneration_ShouldNotDeclareStubFunctions() {
    CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
    CrossModuleMethodMotion.Property prop = new CrossModuleMethodMotion.Property();
    prop.setValue(IR.function(IR.name("foo"), IR.block()));
    prop.setModule(new JSModule("module1"));
    nameInfo.addDeclaration(prop);
    nameInfo.setDeepestCommonModuleRef(new JSModule("module2"));
    idGenerator.newId();

    crossModuleMethodMotion.moveMethods(Collections.singletonList(nameInfo));

    // Verify that the stub functions are not declared.
    assertFalse(topModuleCode.contains("function JSCompiler_stubMethod"));
    assertFalse(topModuleCode.contains("function JSCompiler_unstubMethod"));
  }

}