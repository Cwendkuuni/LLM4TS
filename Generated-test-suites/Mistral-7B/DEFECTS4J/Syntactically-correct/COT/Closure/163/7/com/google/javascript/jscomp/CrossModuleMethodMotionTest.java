import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);

    // Test with a valid input and ensure no errors are reported
    Node externRoot = Node.object();
    Node root = Node.object();
    pass.process(externRoot, root);
    assertTrue(pass.compiler.getDiagnostics().isEmpty());
  }

  @Test(expected = IllegalStateException.class)
  public void testProcessNullExternRoot() {
    AbstractCompiler compiler = new AbstractCompiler();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);

    // Test with a null externRoot and expect an IllegalStateException to be thrown
    Node root = Node.object();
    pass.process(null, root);
  }

  @Test(expected = IllegalStateException.class)
  public void testProcessNullRoot() {
    AbstractCompiler compiler = new AbstractCompiler();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);

    // Test with a null root and expect an IllegalStateException to be thrown
    Node externRoot = Node.object();
    pass.process(externRoot, null);
  }

  @Test
  public void testMoveMethods() {
    AbstractCompiler compiler = new AbstractCompiler();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
    CrossModuleMethodMotion.IdGenerator idGenerator = pass.idGenerator;

    // Test with an empty module graph and ensure no methods are moved
    AnalyzePrototypeProperties analyzer = new AnalyzePrototypeProperties(compiler, new JSModuleGraph(), false, false);
    analyzer.process(Node.object(), Node.object());
    pass.moveMethods(analyzer.getAllNameInfo());
    assertEquals(0, idGenerator.currentId);

    // Test with a module graph containing only one module and ensure no methods are moved
    analyzer = new AnalyzePrototypeProperties(compiler, new JSModuleGraph(), false, false);
    JSModule module = new JSModule("module1");
    analyzer.process(Node.object(), Node.object());
    analyzer.getAllNameInfo().add(new AnalyzePrototypeProperties.NameInfo("module1.prototype.method1", null, null, null, module, Node.object()));
    pass.moveMethods(analyzer.getAllNameInfo());
    assertEquals(0, idGenerator.currentId);

    // Test with a module graph containing two modules and ensure a method is moved correctly
    analyzer = new AnalyzePrototypeProperties(compiler, new JSModuleGraph(), false, false);
    JSModule module1 = new JSModule("module1");
    JSModule module2 = new JSModule("module2");
    moduleGraph.addDependency(module1, module2);
    analyzer.process(Node.object(), Node.object());
    analyzer.getAllNameInfo().add(new AnalyzePrototypeProperties.NameInfo("module1.prototype.method1", null, null, null, module1, Node.object()));
    analyzer.getAllNameInfo().add(new AnalyzePrototypeProperties.NameInfo("module2.prototype.method1", null, null, null, module2, Node.object()));
    pass.moveMethods(analyzer.getAllNameInfo());
    assertEquals(1, idGenerator.currentId);
  }

  @Test
  public void testGetStubMethodId() {
    AbstractCompiler compiler = new AbstractCompiler();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
    CrossModuleMethodMotion.IdGenerator idGenerator = pass.idGenerator;

    // Test with an empty idGenerator and expect the first id to be 0
    int id = idGenerator.newId();
    assertEquals(0, id);

    // Test with an idGenerator that has already generated one id and expect the next id to be 1
    idGenerator.newId();
    id = idGenerator.newId();
    assertEquals(1, id);
  }
}