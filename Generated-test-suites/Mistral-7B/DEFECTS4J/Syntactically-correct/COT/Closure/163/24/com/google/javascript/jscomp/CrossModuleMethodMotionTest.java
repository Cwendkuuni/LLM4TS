import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPasses.CrossModuleMethodMotion.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilationError;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  private Compiler compiler;
  private CompilerOptions options;
  private JSModuleGraph moduleGraph;
  private AnalyzePrototypeProperties analyzer;
  private IdGenerator idGenerator;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
    moduleGraph = compiler.getModuleGraph();
    analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph, false, false);
    idGenerator = new IdGenerator();
  }

  @Test
  public void testProcess_NoModules() {
    Node externRoot = Node.rootNode();
    Node root = Node.rootNode();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.process(externRoot, root);
    assertTrue(pass.moveMethods(analyzer.getAllNameInfo()).isEmpty());
  }

  @Test
  public void testProcess_OneModule() {
    Node externRoot = Node.rootNode();
    Node root = Node.objectLiteral();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.process(externRoot, root);
    assertTrue(pass.moveMethods(analyzer.getAllNameInfo()).isEmpty());
  }

  @Test
  public void testProcess_TwoModules() {
    Node externRoot = Node.objectLiteral();
    externRoot.setLine(1);
    externRoot.setColumn(1);
    externRoot.addChild(Node.property("A", Node.objectLiteral()));
    Node root = Node.objectLiteral();
    root.setLine(1);
    root.setColumn(1);
    root.addChild(Node.property("B", Node.objectLiteral()));
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);

    analyzer.process(externRoot, root);

    Iterator<NameInfo> nameInfos = analyzer.getAllNameInfo().iterator();
    NameInfo nameInfo = nameInfos.next();
    assertNotNull(nameInfo);
    assertEquals("A.prototype.b", nameInfo.name);
    assertEquals(externRoot, nameInfo.getDeepestCommonModuleRef());

    pass.process(externRoot, root);
    assertFalse(pass.moveMethods(analyzer.getAllNameInfo()).isEmpty());
  }

  @Test
  public void testProcess_TwoModulesWithStubs() {
    Node externRoot = Node.objectLiteral();
    externRoot.setLine(1);
    externRoot.setColumn(1);
    externRoot.addChild(Node.property("A", Node.objectLiteral()));
    Node root = Node.objectLiteral();
    root.setLine(1);
    root.setColumn(1);
    root.addChild(Node.property("B", Node.objectLiteral()));

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);

    analyzer.process(externRoot, root);

    Iterator<NameInfo> nameInfos = analyzer.getAllNameInfo().iterator();
    NameInfo nameInfo = nameInfos.next();
    assertNotNull(nameInfo);
    assertEquals("A.prototype.b", nameInfo.name);
    assertEquals(externRoot, nameInfo.getDeepestCommonModuleRef());

    pass.process(externRoot, root);
    assertFalse(pass.moveMethods(analyzer.getAllNameInfo()).isEmpty());

    Node actual = pass.moveMethods(analyzer.getAllNameInfo()).iterator().next();
    assertEquals(ID_MOVE_METHOD, actual.getType());
    assertEquals("CrossModuleMethodMotion.moveMethods", actual.getMetaInfo().getCallerName());
    assertEquals("A.prototype.b", actual.getMetaInfo().getMethodName());
    assertEquals("A", actual.getMetaInfo().getFromModuleName());
    assertEquals("B", actual.getMetaInfo().getToModuleName());
    assertEquals(CompilerPasses.MoveMethod.class, actual.getUserData());
  }

  @Test(expected = CompilationError.class)
  public void testProcess_NullCommonModule() {
    Node externRoot = Node.rootNode();
    Node root = Node.rootNode();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    pass.process(null, root);
  }

  @Test
  public void testMoveMethods() {
    Node externRoot = Node.objectLiteral();
    externRoot.setLine(1);
    externRoot.setColumn(1);
    externRoot.addChild(Node.property("A", Node.objectLiteral()));
    Node root = Node.objectLiteral();
    root.setLine(1);
    root.setColumn(1);
    root.addChild(Node.property("B", Node.objectLiteral()));
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
    analyzer.process(externRoot, root);
    Iterator<NameInfo> nameInfos = analyzer.getAllNameInfo().iterator();
    NameInfo nameInfo = nameInfos.next();
    assertNotNull(nameInfo);
    assertEquals("A.prototype.b", nameInfo.name);
    assertEquals(externRoot, nameInfo.getDeepestCommonModuleRef());

    pass.moveMethods(analyzer.getAllNameInfo());
    assertTrue(pass.moveMethods(analyzer.getAllNameInfo()).isEmpty());
  }
}