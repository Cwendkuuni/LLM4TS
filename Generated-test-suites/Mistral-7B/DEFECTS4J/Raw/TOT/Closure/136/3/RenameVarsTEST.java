import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassManager.PassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class RenameVarsTest {

  @Test
  public void testRenameVars() {
    String originalCode = "function foo() {" +
            "  var a = 1, b = 2, c = 3;" +
            "  function bar(x) {" +
            "    var d = x + 1;" +
            "  }" +
            "  var e = 4, f = 5;" +
            "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(true, null);
    PassManager passManager = new CompilerPassManager(compiler, options);

    // Add the RenameVars compiler pass
    RenameVars renameVars = new RenameVars(
        passManager.getCompiler(),
        "",
        false,
        false,
        false,
        null,
        null,
        null);
    passManager.addPass(renameVars);

    Node ast = NodeTraversal.parseCompilationUnit(NodeFactory.getFactory(), originalCode);
    Node entryNode = ast.getFirstChild();

    // Process the code
    passManager.process(entryNode, null);

    // Check the renamed variables
    CompilerPass result = passManager.getPass(CompilerPassManager.RENAME_VARS);
    RenameVars renamedVars = (RenameVars) result;

    // Check global variables renaming
    NodeTraversal.traverse(compiler, ast, new AbstractPostOrderCallback() {
      int globalCount = 0;

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (!(n.getType() == NAME)) {
          return;
        }
        String name = n.getString();
        if (renamedVars.getVariableMap().containsKey(name)) {
          assertEquals("Unexpected global name: " + name,
              renamedVars.getVariableMap().get(name), name);
          globalCount++;
        }
      }
    });
    assertEquals("Unexpected number of global variables: " + globalCount, 6, globalCount);

    // Check local variables renaming
    Node foo = NodeUtil.getUniqueChildOfType(ast, FUNCTION);
    NodeTraversal.traverse(compiler, foo, new AbstractPostOrderCallback() {
      int localCount = 0;

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (!(n.getType() == VAR)) {
          return;
        }
        String name = n.getString();
        if (renamedVars.getVariableMap().containsKey(name)) {
          assertEquals("Unexpected local name: " + name,
              renamedVars.getVariableMap().get(name), name);
          localCount++;
        }
      }
    });
    assertEquals("Unexpected number of local variables: " + localCount, 6, localCount);

    // Check anonymous function names preservation
    Node bar = NodeUtil.getUniqueChildOfType(foo, FUNCTION);
    assertEquals("Unexpected anonymous function name: ", "bar", bar.getFirstChild().getString());

    // Check extern variables are not renamed
    Set<String> externNames = renamedVars.getExternNames();
    assertTrue("Unexpected number of extern variables: " + externNames.size(), externNames.containsAll(Sets.newHashSet("a", "b", "c", "d", "e", "f")));

    // Check reserved variables are not renamed
    Set<String> reservedNames = renamedVars.getReservedNames();
    assertTrue("Unexpected number of reserved variables: " + reservedNames.size(), reservedNames.containsAll(Sets.newHashSet("function", "return", "this", "var")));

    // Check local variables are renamed to temporary names
    int localVarIndex = 0;
    NodeTraversal.traverse(compiler, foo, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (!(n.getType() == VAR)) {
          return;
        }
        String name = n.getString();
        if (name.startsWith(RenameVars.LOCAL_VAR_PREFIX)) {
          assertEquals("Unexpected local variable index: " + localVarIndex, localVarIndex, Integer.parseInt(name.substring(1)));
          localVarIndex++;
        }
      }
    });
  }
}