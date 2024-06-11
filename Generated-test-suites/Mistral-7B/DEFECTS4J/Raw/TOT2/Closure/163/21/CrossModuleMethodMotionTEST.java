import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSCompiler;
import com.google.javascript.jscomp.JSModuleGraph;

public class CrossModuleMethodMotionTest {

  @ org.junit.Before
  public void setUp() {
    JSCompiler compiler = new JSCompiler();
    CompilerPass pass = new CrossModuleMethodMotion(compiler, new IdGenerator(), true);
    compiler.addPass(pass);
  }

  @ org.junit.Test
  public void testNormalFunctionMove() {
    String sourceCode = "class A {\n" +
        "  function a() {\n" +
        "    return 'A.a';\n" +
        "  }\n" +
        "}\n" +
        "class B extends A {}";

    Node rootNode = JSCompiler.parseSingleFile(sourceCode, null);
    AbstractCompiler compiler = new JSCompiler();
    compiler.compile(rootNode, null, null);
    CrossModuleMethodMotion pass = (CrossModuleMethodMotion) compiler.getPasses()[0];
    pass.process(null, rootNode);

    Node aFunction = rootNode.lookupClass("B").lookupProperty("a");
    assertTrue(aFunction.isFunction());
    assertNotNull(aFunction.getPrototype());
    String aFunctionName = aFunction.getPrototype().getName();
    assertTrue(aFunctionName.startsWith(CrossModuleMethodMotion.STUB_METHOD_NAME));
  }

  @ org.junit.Test
  public void testMoveMethodAcrossModules() {
    String sourceCode1 = "class A {\n" +
        "  function a() {\n" +
        "    return 'A.a';\n" +
        "  }\n" +
        "}\n" +
        "class B {}";

    String sourceCode2 = "class C {\n" +
        "  function b() {\n" +
        "    return 'C.b';\n" +
        "  }\n" +
        "}\n" +
        "class D extends C {}";

    Node rootNode1 = JSCompiler.parseSingleFile(sourceCode1, null);
    Node rootNode2 = JSCompiler.parseSingleFile(sourceCode2, null);
    AbstractCompiler compiler = new JSCompiler();
    compiler.addPasses(new CrossModuleMethodMotion(compiler, new IdGenerator(), true));
    compiler.compileAllInputs();

    JSModuleGraph moduleGraph = compiler.getModuleGraph();
    JSModule aModule = moduleGraph.getModuleWithName("A");
    JSModule dModule = moduleGraph.getModuleWithName("D");

    Node aFunction = aModule.getTopLevelDeclarations().get(0);
    Node dFunction = dModule.getTopLevelDeclarations().get(1);

    assertTrue(aFunction.isFunction());
    assertTrue(aFunction.getPrototype().getNumChildren() == 0);

    assertTrue(dFunction.isFunction());
    assertTrue(dFunction.getPrototype().getNumChildren() > 0);
    assertNotNull(dFunction.getPrototype().getFirstChild());
    String dFunctionName = dFunction.getPrototype().getFirstChild().getName();
    assertTrue(dFunctionName.startsWith(CrossModuleMethodMotion.STUB_METHOD_NAME));
  }

  @ org.junit.Test
  public void testNoMoveFunctions() {
    String sourceCode = "class A {\n" +
        "  function a() {\n" +
        "    return 'A.a';\n" +
        "  }\n" +
        "  function b() {\n" +
        "    return 'A.b';\n" +
        "  }\n" +
        "}\n" +
        "class B extends A {}";

    Node rootNode = JSCompiler.parseSingleFile(sourceCode, null);
    AbstractCompiler compiler = new JSCompiler();
    compiler.compile(rootNode, null, null);
    CrossModuleMethodMotion pass = (CrossModuleMethodMotion) compiler.getPasses()[0];
    pass.process(null, rootNode);

    Node aFunction = rootNode.lookupClass("B").lookupProperty("a");
    assertTrue(aFunction.isFunction());
    assertNotNull(aFunction.getPrototype());

    Node bFunction = rootNode.lookupClass("B").lookupProperty("b");
    assertTrue(bFunction.isFunction());
    assertNotNull(bFunction.getPrototype());
    String bFunctionName = bFunction.getPrototype().getName();
    assertFalse(bFunctionName.startsWith(CrossModuleMethodMotion.STUB_METHOD_NAME));
  }

  @ org.junit.Test
  public void testMoveFunctionWithGetterSetter() {
    String sourceCode = "class A {\n" +
        "  function get a() {\n" +
        "    return 'A.get_a';\n" +
        "  }\n" +
        "  function set a(value) {\n" +
        "    this.a = value;\n" +
        "  }\n" +
        "}\n" +
        "class B extends A {}";

    Node rootNode = JSCompiler.parseSingleFile(sourceCode, null);
    AbstractCompiler compiler = new JSCompiler();
    compiler.compile(rootNode, null, null);
    CrossModuleMethodMotion pass = (CrossModuleMethodMotion) compiler.getPasses()[0];
    pass.process(null, rootNode);

    Node aGetter = rootNode.lookupClass("B").lookupGetter("a");
    assertTrue(aGetter != null);
    assertFalse(aGetter.isFunction());

    Node aSetter = rootNode.lookupClass("B").lookupSetter("a");
    assertTrue(aSetter != null);
    assertFalse(aSetter.isFunction());
  }
}