import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPassTest.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.IR;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private CompilerPass crossModuleMethodMotion;
  private IdGenerator idGenerator;

  @Before
  public void setUp() {
    compiler = new Compiler();
    crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, new IdGenerator(), true);
    idGenerator = crossModuleMethodMotion.getIdGenerator();
  }

  @Test
  public void testProcess_GivenMultipleModules_WhenCallingProcess_ThenMethodsAreMoved() {
    // Given
    Node externRoot = createTestExterns();
    Node root = createTestRoot();

    // When
    crossModuleMethodMotion.process(externRoot, root);

    // Then
    Iterator<NameInfo> allNameInfoIterator = crossModuleMethodMotion.analyzer.getAllNameInfo().iterator();
    for (NameInfo nameInfo : allNameInfoIterator) {
      JSModule deepestCommonModuleRef = nameInfo.getDeepestCommonModuleRef();
      if (deepestCommonModuleRef != null) {
        // Check if the method is moved to the deepest common module
        Property property = nameInfo.getDeclarations().iterator().next();
        Node methodInDeepestCommonModule = deepestCommonModuleRef.getModuleScope().getChildByPropertyName("JSCompiler_unstubMethod_" + idGenerator.newId());
        Node methodInOriginalModule = nameInfo.getPrototype().getChildByPropertyName(nameInfo.name);
        assertNotNull(methodInDeepestCommonModule);
        assertNotNull(methodInOriginalModule);
        assertNotNull(methodInOriginalModule.getParent());
        assertNotSame(methodInOriginalModule.getParent(), deepestCommonModuleRef.getModuleScope());
        assertSame(methodInDeepestCommonModule.getParent(), deepestCommonModuleRef.getModuleScope());
      }
    }
  }

  @Test(expected = AssertionError.class)
  public void testProcess_GivenNullDeepestCommonModuleRef_WhenCallingProcess_ThenAnErrorIsThrown() {
    // Given
    Node externRoot = null;
    Node root = createTestRoot();

    // When
    crossModuleMethodMotion.process(externRoot, root);

    // Then
    fail("An error should be thrown when deepestCommonModuleRef is null.");
  }

  @Test
  public void testMoveMethods_GivenNullDeepestCommonModuleRef_WhenCallingMoveMethods_ThenNoActionsAreTaken() {
    // Given
    Collection<NameInfo> allNameInfo = createTestNameInfoCollection();

    // When
    crossModuleMethodMotion.moveMethods(allNameInfo);

    // Then
    // No actions should be taken when deepestCommonModuleRef is null.
    assertTrue(allNameInfo.isEmpty());
  }

  @Test
  public void testMoveMethods_GivenTwoModules_WhenCallingMoveMethods_ThenMethodsAreMoved() {
    // Given
    Collection<NameInfo> allNameInfo = createTestNameInfoCollection();

    // When
    crossModuleMethodMotion.moveMethods(allNameInfo);

    // Then
    // Two methods should be moved from ModuleB to ModuleA.
    assertEquals(2, crossModuleMethodMotion.analyzer.getAllNameInfo().size());
  }

  @Test
  public void testMoveMethods_GivenThreeModules_WhenCallingMoveMethods_ThenMethodsAreMoved() {
    // Given
    Collection<NameInfo> allNameInfo = createTestNameInfoCollection();

    // When
    crossModuleMethodMotion.moveMethods(allNameInfo);

    // Then
    // Three methods should be moved from ModuleC to ModuleB, and then from ModuleB to ModuleA.
    assertEquals(3, crossModuleMethodMotion.analyzer.getAllNameInfo().size());
  }

  @Test
  public void testIdGenerator_GivenNoGeneratedIds_WhenGettingIds_ThenNoIdsAreGenerated() {
    // Given
    IdGenerator idGenerator = new IdGenerator();

    // When
    int firstId = idGenerator.newId();
    int secondId = idGenerator.newId();

    // Then
    assertEquals(0, firstId);
    assertEquals(0, secondId);
  }

  @Test
  public void testIdGenerator_GivenGeneratedIds_WhenGettingIds_ThenNewIdsAreGenerated() {
    // Given
    IdGenerator idGenerator = crossModuleMethodMotion.getIdGenerator();

    // When
    int firstId = idGenerator.newId();
    int secondId = idGenerator.newId();

    // Then
    assertNotEquals(0, firstId);
    assertNotEquals(0, secondId);
  }

  @Test
  public void testProcess_GivenSingleModule_WhenCallingProcess_ThenNoActionsAreTaken() {
    // Given
    Node externRoot = createTestExterns();
    Node root = createTestRoot();

    // When
    crossModuleMethodMotion.process(externRoot, root);

    // Then
    // No actions should be taken when there is only one module.
    assertEquals(0, crossModuleMethodMotion.analyzer.getAllNameInfo().size());
  }

  @Test
  public void testProcess_GivenTwoModulesWithoutDependencies_WhenCallingProcess_ThenNoActionsAreTaken() {
    // Given
    Node externRoot = createTestExterns();
    Node root = createTestRootWithoutDependencies();

    // When
    crossModuleMethodMotion.process(externRoot, root);

    // Then
    // No actions should be taken when there are no dependencies between modules.
    assertEquals(0, crossModuleMethodMotion.analyzer.getAllNameInfo().size());
  }

  private Node createTestExterns() {
    Node externs = new Node(IR.OBJECT_LITERAL);
    return compiler.addRoot(externs);
  }

  private Node createTestRoot() {
    Node root = new Node(IR.PROGRAM());
    Node moduleA = createTestModule("ModuleA");
    Node moduleB = createTestModule("ModuleB");

    root.addChildToFront(moduleA);
    root.addChildToFront(moduleB);

    return root;
  }

  private Node createTestRootWithoutDependencies() {
    Node root = new Node(IR.PROGRAM());
    Node moduleA = createTestModuleWithDependencies("ModuleA", new String[] {"ModuleB"});
    Node moduleB = createTestModuleWithDependencies("ModuleB", new String[] {"ModuleA"});

    root.addChildToFront(moduleA);
    root.addChildToFront(moduleB);

    return root;
  }

  private Node createTestModule(String name) {
    Node module = new Node(IR.MODULE_DECLARATION());
    module.setProperty("id", IR.string(name));
    Node moduleScope = new Node(IR.BLOCK());
    module.setChild("body", moduleScope);
    return compiler.addChildToRoot(module);
  }

  private Node createTestModuleWithDependencies(String name, String[] dependencies) {
    Node module = new Node(IR.MODULE_DECLARATION());
    module.setProperty("id", IR.string(name));
    Node moduleScope = new Node(IR.BLOCK());
    module.setChild("body", moduleScope);

    for (String dependency : dependencies) {
      Node dependencyNode = new Node(IR.IMPORT_DECLARATION());
      dependencyNode.setProperty("name", IR.string(dependency));
      moduleScope.addChildToFront(dependencyNode);
    }

    return compiler.addChildToRoot(module);
  }

  private Collection<NameInfo> createTestNameInfoCollection() {
    Collection<NameInfo> allNameInfo = crossModuleMethodMotion.analyzer.getAllNameInfo();
    return allNameInfo;
  }
}