import com.google.javascript.jscomp.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private JSModuleGraph moduleGraph;
  private CrossModuleMethodMotion.IdGenerator idGenerator;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    moduleGraph = new JSModuleGraph();
    idGenerator = new CrossModuleMethodMotion.IdGenerator();
  }

  @Test
  public void testProcess_OneModule() {
    JSModule moduleA = createModule("moduleA");
    moduleGraph.addModule(moduleA);

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    pass.process(null, moduleA);

    // Assert that no methods are moved since there is only one module.
    assertEquals(0, moduleA.getScripts().size());
  }

  @Test
  public void testProcess_TwoModules() {
    JSModule moduleA = createModule("moduleA");
    JSModule moduleB = createModule("moduleB");
    moduleGraph.addModule(moduleA);
    moduleGraph.addModule(moduleB);

    // Create a method in moduleA that will be moved to moduleB.
    Node scriptA = new Node(Token.SCRIPT,
        new Node(Token.FUNCTION,
            IR.name("methodA"),
            IR.paramList(),
            IR.block()));
    moduleA.addScript(new CompilerInput(scriptA));

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    pass.process(null, moduleA);

    // Assert that the method has been moved from moduleA to moduleB.
    assertEquals(0, moduleA.getScripts().size());
    assertEquals(1, moduleB.getScripts().size());
    assertEquals(scriptA, moduleB.getScripts().get(0).getAstRoot());
  }

  @Test
  public void testProcess_InvalidCommonModule() {
    JSModule moduleA = createModule("moduleA");
    JSModule moduleB = createModule("moduleB");
    moduleGraph.addModule(moduleA);
    moduleGraph.addModule(moduleB);

    // Create a method in moduleA with invalid deepest common module.
    Node scriptA = new Node(Token.SCRIPT,
        new Node(Token.FUNCTION,
            IR.name("methodA"),
            IR.paramList(),
            IR.block()));
    moduleA.addScript(new CompilerInput(scriptA));

    NameInfo nameInfo = new NameInfo();
    Symbol symbol = new Property(scriptA, moduleA);
    nameInfo.addDeclaration(symbol);
    nameInfo.setDeepestCommonModuleRef(null);
    nameInfo.setReferenced(true);

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    pass.moveMethods(java.util.Arrays.asList(nameInfo));

    // Assert that the error is reported by the compiler.
    assertEquals(1, compiler.getErrorCount());
  }

  @Test
  public void testProcess_MoveStubMethod() {
    JSModule moduleA = createModule("moduleA");
    JSModule moduleB = createModule("moduleB");
    moduleGraph.addModule(moduleA);
    moduleGraph.addModule(moduleB);

    // Create a stub method in moduleA that will be moved to moduleB.
    Node functionNode = new Node(Token.FUNCTION,
        IR.name("methodA"),
        IR.paramList(),
        IR.block());
    Node assignNode = IR.exprResult(
        IR.assign(
            IR.getprop(IR.name("A").cloneNode(), IR.string("prototype")),
            IR.objectlit(
                IR.property(IR.string("b"), functionNode))))
        .setJSDocInfo(IR.jSDocInfo("/**
        * @type {function()}
        * @suppress {globalThis}
        */"));

    Node scriptA = IR.root(IR.jsDoc(IR.jSDocInfo("/**@const*/")), assignNode);
    moduleA.addScript(new CompilerInput(scriptA));

    NameInfo nameInfo = new NameInfo();
    Symbol symbol = new Property(functionNode, moduleA);
    nameInfo.addDeclaration(symbol);
    nameInfo.setDeepestCommonModuleRef(moduleB);
    nameInfo.setReferenced(true);

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    pass.moveMethods(java.util.Arrays.asList(nameInfo));

    // Assert that the stub method has been moved from moduleA to moduleB.
    assertEquals(0, moduleA.getScripts().size());
    assertEquals(1, moduleB.getScripts().size());
    assertFalse(moduleB.getScripts().get(0).getAstRoot().findDuplicate());
  }

  // Additional test methods for other scenarios.

  private JSModule createModule(String name) {
    // Common utils method to create JSModule with given name.
  }

}