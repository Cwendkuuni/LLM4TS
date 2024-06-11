import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.*;
import static com.google.javascript.jscomp.JSError.*;
import static com.google.javascript.rhino.NodeTag.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions.CompilationLevel;
import com.google.javascript.jscomp.CompilerOptions.InputFileOptions;
import com.google.javascript.jscomp.CompilerOptions.JsCompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.SourceMapFileOptions;
import com.google.javascript.jscomp.CompilerOptions.WarningLevel;
import com.google.javascript.jscomp.NodeTraversingTreePrinter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.SourceFile;

@RunWith(JUnit4.class)
public class CrossModuleMethodMotionTest {

  private Compiler compiler;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = new Compiler();
    SourceFile sourceFile = new SourceFile("test.js");
    rootNode = sourceFile.getTree();

    InputFileOptions inputOptions = new InputFileOptions();
    inputOptions.setCompilationLevel(CompilationLevel.ADVANCED);
    inputOptions.setWarningLevel(WarningLevel.QUIET);
    inputOptions.setSourceMapFileOptions(new SourceMapFileOptions(true));
    JsCompilerOptions jsOptions = new JsCompilerOptions();
    jsOptions.setOutputFile("out.js");
    compiler.setOptionsForFile("test.js", inputOptions, jsOptions);
  }

  @Test
  public void testMoveMethods() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), false);
    pass.process(null, rootNode);
    assertEquals("expected number of methods moved to be greater than zero", 1, pass.idGenerator.hasGeneratedAnyIds());

    // TODO: Add assertions to check the actual movement of methods
  }

  @Test(expected = IllegalStateException.class)
  public void testNullCommonModuleError() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), false);
    pass.analyzer.setAllNameInfo(Collections.emptyList());
    pass.process(null, rootNode);
  }

  @Test
  public void testStubMethod() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), false);
    Node functionNode = NodeUtil.newFunction("foo", null, null, null, null, null, null);
    Node prototypeNode = NodeUtil.newObject("proto", null, null, null);
    Node prototypePropertyNode = NodeUtil.newProperty("bar", functionNode, null);
    prototypeNode.addChildToFront(prototypePropertyNode);
    Node objectNode = NodeUtil.newObject("obj", prototypeNode, null, null);

    rootNode.addChildToFront(objectNode);
    pass.process(null, rootNode);

    Node stubFunctionNode = compiler.getTreeUtil().findFirstDescendantByTag(rootNode, CALL_EXPR);
    Node stubMethodNode = compiler.getTreeUtil().findFirstChildByTag(stubFunctionNode, ID_LITERAL);
    assertEquals("stub method name should match", STUB_METHOD_NAME, stubMethodNode.toString());
  }

  @Test
  public void testUnstubMethod() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), false);
    Node functionNode = NodeUtil.newFunction("foo", null, null, null, null, null, null);
    Node prototypeNode = NodeUtil.newObject("proto", null, null, null);
    Node prototypePropertyNode = NodeUtil.newProperty("bar", functionNode, null);
    prototypeNode.addChildToFront(prototypePropertyNode);
    Node objectNode = NodeUtil.newObject("obj", prototypeNode, null, null);

    rootNode.addChildToFront(objectNode);
    pass.process(null, rootNode);

    Node unstubFunctionNode = compiler.getTreeUtil().findFirstDescendantByTag(rootNode, ASSIGN_EXPR);
    Node getPropertyNode = compiler.getTreeUtil().findFirstChildByTag(unstubFunctionNode, GET_PROP);
    Node stubMethodIdNode = compiler.getTreeUtil().findFirstChildByTag(getPropertyNode, INT_LITERAL);
    Node unstubFunctionBodyNode = compiler.getTreeUtil().findFirstChildByTag(unstubFunctionNode, BLOCK);

    List<Node> functionStatements = new ArrayList<>();
    NodeTraversingTreePrinter printer = new NodeTraversingTreePrinter(functionStatements);
    printer.print(unstubFunctionBodyNode);
    String functionBody = String.join("\n", functionStatements);

    // TODO: Add assertions to check the actual unstubbing of methods
    // The function body should match the original method definition
    assertEquals("unstubbed method body should match", functionNode.toString(), functionBody);
  }

  @Test
  public void testStubDeclarations() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), false);
    pass.process(null, rootNode);

    Node declarationsNode = compiler.getTreeUtil().findFirstDescendantByTag(rootNode, STMT_LIST);
    List<Node> declarations = new ArrayList<>();
    NodeTraversingTreePrinter printer = new NodeTraversingTreePrinter(declarations);
    printer.print(declarationsNode);

    // TODO: Add assertions to check the actual declaration of stub functions
    // There should be a declaration for each stubbed method
    assertEquals("number of stub function declarations should match", 1, declarations.size());
  }
}