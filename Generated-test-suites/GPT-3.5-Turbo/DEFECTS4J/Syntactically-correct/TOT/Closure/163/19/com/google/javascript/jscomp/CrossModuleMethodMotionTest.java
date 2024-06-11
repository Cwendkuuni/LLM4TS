import org.junit.Test;
import static org.junit.Assert.*;

public class CrossModuleMethodMotionTest {

  @Test
  public void testProcess_WithTwoModules_NoMethodsMoved() {
    // Create two modules
    JSModule module1 = new JSModule("module1");
    JSModule module2 = new JSModule("module2");

    // Create a compiler and module graph
    AbstractCompiler compiler = new AbstractCompiler();
    JSModuleGraph moduleGraph = new JSModuleGraph(ImmutableList.of(module1, module2));
    compiler.setModuleGraph(moduleGraph);

    // Create name info without any method declarations
    Collection<NameInfo> nameInfoList = new ArrayList<NameInfo>();

    // Create a CrossModuleMethodMotion instance
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, new IdGenerator(), true);

    // Invoke the process method
    crossModuleMethodMotion.process(null, null);

    // Verify that no methods were moved
    // No methods should be moved when there are only two modules
    assertEquals(0, module1.getDefinitions().size());
    assertEquals(0, module2.getDefinitions().size());
  }

  @Test
  public void testProcess_WithMoreThanTwoModules_MethodsMoved() {
      // Create three modules
      JSModule module1 = new JSModule("module1");
      JSModule module2 = new JSModule("module2");
      JSModule module3 = new JSModule("module3");

      // Create a compiler and module graph
      AbstractCompiler compiler = new AbstractCompiler();
      JSModuleGraph moduleGraph = new JSModuleGraph(ImmutableList.of(module1, module2, module3));
      compiler.setModuleGraph(moduleGraph);

      // Create name info with method declarations
      Collection<NameInfo> nameInfoList = new ArrayList<NameInfo>();

      // Create a CrossModuleMethodMotion instance
      CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, new IdGenerator(), true);

      // Invoke the process method
      crossModuleMethodMotion.process(null, null);

      // Verify that methods were moved
      // The methods should be moved to deeper modules in the module graph
      assertFalse(module1.getDefinitions().isEmpty());
      assertFalse(module2.getDefinitions().isEmpty());
  }

  @Test
  public void testProcess_UnreferencedNameInfo_NoMethodsMoved() {
    // Create a module
    JSModule module1 = new JSModule("module1");

    // Create a compiler and module graph
    AbstractCompiler compiler = new AbstractCompiler();
    JSModuleGraph moduleGraph = new JSModuleGraph(ImmutableList.of(module1));
    compiler.setModuleGraph(moduleGraph);

    // Create an unreferenced name info without any method declarations
    Collection<Symbol> declarations = new ArrayList<Symbol>();
    NameInfo nameInfo = new NameInfo("test", null, declarations, false, null);
    Collection<NameInfo> nameInfoList = new ArrayList<NameInfo>();
    nameInfoList.add(nameInfo);

    // Create a CrossModuleMethodMotion instance
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, new IdGenerator(), true);

    // Invoke the process method
    crossModuleMethodMotion.process(null, null);

    // Verify that no methods were moved
    // No methods should be moved when there are no referenced name infos
    assertEquals(0, module1.getDefinitions().size());
  }

  @Test
  public void testProcess_WithReadClosureVariables_NoMethodsMoved() {
    // Create a module
    JSModule module1 = new JSModule("module1");

    // Create a compiler and module graph
    AbstractCompiler compiler = new AbstractCompiler();
    JSModuleGraph moduleGraph = new JSModuleGraph(ImmutableList.of(module1));
    compiler.setModuleGraph(moduleGraph);

    // Create a name info with method declarations but reads closure variables
    Collection<Symbol> declarations = new ArrayList<Symbol>();
    NameInfo nameInfo = new NameInfo("test", null, declarations, true, null);
    Collection<NameInfo> nameInfoList = new ArrayList<NameInfo>();
    nameInfoList.add(nameInfo);

    // Create a CrossModuleMethodMotion instance
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, new IdGenerator(), true);

    // Invoke the process method
    crossModuleMethodMotion.process(null, null);

    // Verify that no methods were moved
    // No methods should be moved when name info reads closure variables
    assertEquals(0, module1.getDefinitions().size());
  }

  @Test
  public void testProcess_WithNullCommonModuleRef_ErrorReported() {
    // Create a module
    JSModule module1 = new JSModule("module1");

    // Create a compiler and module graph
    AbstractCompiler compiler = new AbstractCompiler();
    JSModuleGraph moduleGraph = new JSModuleGraph(ImmutableList.of(module1));
    compiler.setModuleGraph(moduleGraph);

    // Create a name info with method declarations but null common module ref
    Collection<Symbol> declarations = new ArrayList<Symbol>();
    NameInfo nameInfo = new NameInfo("test", null, declarations, false, null);
    nameInfo.setDeepestCommonModuleRef(null);
    Collection<NameInfo> nameInfoList = new ArrayList<NameInfo>();
    nameInfoList.add(nameInfo);

    // Create a CrossModuleMethodMotion instance
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, new IdGenerator(), true);

    // Invoke the process method
    crossModuleMethodMotion.process(null, null);

    // Verify that an error is reported when the deepest common module ref is null
    assertTrue(compiler.getErrors().get(0).getType().equals(NULL_COMMON_MODULE_ERROR));
  }

  @Test
  public void testMoveMethods_MethodMoved_DeclarationsCorrectlyUpdated() {
    // Create a module
    JSModule module1 = new JSModule("module1");

    // Create a compiler and module graph
    AbstractCompiler compiler = new AbstractCompiler();
    JSModuleGraph moduleGraph = new JSModuleGraph(ImmutableList.of(module1));
    compiler.setModuleGraph(moduleGraph);

    // Create a name info with method declarations
    Collection<Symbol> declarations = new ArrayList<Symbol>();
    Property property = new Property();
    property.setName("test");
    declarations.add(property);
    NameInfo nameInfo = new NameInfo("test", null, declarations, false, module1);
    Collection<NameInfo> nameInfoList = new ArrayList<NameInfo>();
    nameInfoList.add(nameInfo);

    // Create a CrossModuleMethodMotion instance
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, new IdGenerator(), true);

    // Invoke the moveMethods method
    crossModuleMethodMotion.moveMethods(nameInfoList);

    // Verify that the method declaration was correctly updated
    assertFalse(module1.getDefinitions().isEmpty());
    // Get the moved definition from the module
    Node movedDefinition = module1.getDefinitions().get(0);
    // Verify that the definition is an assignment to the property
    assertEquals(NodeUtil.isGetprop(movedDefinition.getOnlyChild()), true);
    // Verify that the definition is an assignment to JSCompiler_unstubMethod(id, body)
    assertEquals(NodeUtil.isName(movedDefinition.getOnlyChild().getFirstChild()), true);
    // Verify that the definition is an assignment to JSCompiler_unstubMethod(id, body)
    assertEquals(NodeUtil.isNumber(movedDefinition.getOnlyChild().getSecondChild()), true);
    // Verify that the definition is an assignment to JSCompiler_unstubMethod(id, body)
    assertEquals(NodeUtil.isFunction(movedDefinition.getOnlyChild().getLastChild()), true);
  }

  @Test
  public void testMoveMethods_MethodNotMoved_DeclarationsNotUpdated() {
    // Create a module
    JSModule module1 = new JSModule("module1");

    // Create a compiler and module graph
    AbstractCompiler compiler = new AbstractCompiler();
    JSModuleGraph moduleGraph = new JSModuleGraph(ImmutableList.of(module1));
    compiler.setModuleGraph(moduleGraph);

    // Create a name info with non-method declarations
    Collection<Symbol> declarations = new ArrayList<Symbol>();
    Symbol nonMethodSymbol = new Symbol();
    declarations.add(nonMethodSymbol);
    NameInfo nameInfo = new NameInfo("test", null, declarations, false, module1);
    Collection<NameInfo> nameInfoList = new ArrayList<NameInfo>();
    nameInfoList.add(nameInfo);

    // Create a CrossModuleMethodMotion instance
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, new IdGenerator(), true);

    // Invoke the moveMethods method
    crossModuleMethodMotion.moveMethods(nameInfoList);

    // Verify that no methods were moved and the non-method declarations were not updated
    assertTrue(module1.getDefinitions().isEmpty());
  }

  @Test
  public void testIdGenerator_NewIdGenerated() {
    // Create an IdGenerator instance
    IdGenerator idGenerator = new IdGenerator();

    // Invoke the newId method
    int newId = idGenerator.newId();

    // Verify that a new id is generated
    assertTrue(newId != 0);
  }

  @Test
  public void testIdGenerator_HasGeneratedAnyIds_NoIdsGenerated() {
    // Create an IdGenerator instance
    IdGenerator idGenerator = new IdGenerator();

    // Invoke the hasGeneratedAnyIds method
    boolean hasGeneratedAnyIds = idGenerator.hasGeneratedAnyIds();

    // Verify that hasGeneratedAnyIds returns false
    assertFalse(hasGeneratedAnyIds);
  }

  @Test
  public void testIdGenerator_HasGeneratedAnyIds_IdsGenerated() {
    // Create an IdGenerator instance
    IdGenerator idGenerator = new IdGenerator();

    // Invoke the newId method
    int newId = idGenerator.newId();

    // Invoke the hasGeneratedAnyIds method
    boolean hasGeneratedAnyIds = idGenerator.hasGeneratedAnyIds();

    // Verify that hasGeneratedAnyIds returns true
    assertTrue(hasGeneratedAnyIds);
  }

}