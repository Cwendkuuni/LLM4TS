import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.NodeUtil.isVarDeclaration;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.BaseTestCase;

public class MakeDeclaredNamesUniqueTest extends BaseTestCase {

  private AbstractCompiler compiler;

  @Override
  public void setUp() throws Exception {
    compiler = newTestCompiler();
    compiler.setCompilerOptions(
        new CompilerOptions().isStrict(true).isWarningsAsErrors(true));
  }

  public void testMakeDeclaredNamesUnique() throws Exception {
    String js =
        "function f() {" +
        "  var a = 1, b = 2, c = 3;" +
        "  function g() {}" +
        "  try {" +
        "    throw new Error('test');" +
        "  } catch (e) {" +
        "    var c = 4;" +
        "  }" +
        "  for (var i = 0; i < 10; i++) {" +
        "    var a = i;" +
        "  }" +
        "  var a = [1, 2, 3];" +
        "  for (var i = 0; i < a.length; i++) {" +
        "    var a = a[i];" +
        "  }" +
        "}";

    Node node = parseJs(compiler, js);

    MakeDeclaredNamesUnique mdnu = new MakeDeclaredNamesUnique();
    NodeTraversal nt = new NodeTraversal(compiler, node);
    nt.traverse(mdnu);

    Map<String, Integer> globalVarCounts = ImmutableSet.of(
        "a", 3,
        "b", 1,
        "c", 3,
        "g", 1).asMap();

    Deque<Scope> scopes = nt.getScopeStack();
    for (Scope scope : scopes) {
      Map<String, Integer> scopeVarCounts = Maps.newHashMap();
      for (Var var : scope.getVars()) {
        String name = var.getName();
        int count = scopeVarCounts.getOrDefault(name, 0);
        scopeVarCounts.put(name, count + 1);
      }
      assertEquals("Unexpected variable declarations in scope", globalVarCounts, scopeVarCounts);
    }

    List<Node> functions = Lists.newArrayList();
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.FUNCTION) {
        functions.add(child);
      }
    }
    assertEquals("Unexpected number of functions in code", 1, functions.size());

    Node function = functions.get(0);
    Map<String, Integer> functionVarCounts = Maps.newHashMap();
    for (Node child = function.getFirstChild(); child != null; child = child.getNext()) {
      if (isVarDeclaration(child)) {
        String name = child.getFirstChild().getString();
        int count = functionVarCounts.getOrDefault(name, 0);
        functionVarCounts.put(name, count + 1);
      }
    }
    assertEquals("Unexpected variable declarations in function", ImmutableSet.of("a", "b", "c", "i").asMap(), functionVarCounts);

    List<Node> functionBodies = Lists.newArrayList();
    for (Node child = function.getLastChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.BLOCK) {
        functionBodies.add(child);
      }
    }
    assertEquals("Unexpected number of function bodies", 1, functionBodies.size());

    Map<String, Integer> functionBodyVarCounts = Maps.newHashMap();
    for (Node child = functionBodies.get(0).getFirstChild(); child != null; child = child.getNext()) {
      if (isVarDeclaration(child)) {
        String name = child.getFirstChild().getString();
        int count = functionBodyVarCounts.getOrDefault(name, 0);
        functionBodyVarCounts.put(name, count + 1);
      }
    }
    assertEquals("Unexpected variable declarations in function body", ImmutableSet.of("a", "c").asMap(), functionBodyVarCounts);

    Map<String, Integer> catchVarCounts = Maps.newHashMap();
    for (Node child = functionBodies.get(0).getLastChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.CATCH) {
        String name = child.getFirstChild().getString();
        int count = catchVarCounts.getOrDefault(name, 0);
        catchVarCounts.put(name, count + 1);
      }
    }
    assertEquals("Unexpected catch variable declaration", 1, catchVarCounts.size());
  }

  public void testContextualRenameInverter() throws Exception {
    String js =
        "function f() {" +
        "  var a = 1, b = 2, c = 3;" +
        "  function g() {" +
        "    function h() {}" +
        "  }" +
        "  try {" +
        "    throw new Error('test');" +
        "  } catch (e) {" +
        "    var c = 4;" +
        "  }" +
        "  for (var i = 0; i < 10; i++) {" +
        "    var a = i;" +
        "  }" +
        "  for (var i = 0; i < [1, 2, 3].length; i++) {" +
        "    var a = [1, 2, 3][i];" +
        "  }" +
        "  var a = [1, 2, 3];" +
        "  for (var i = 0; i < a.length; i++) {" +
        "    var a = a[i];" +
        "  }" +
        "}";

    Node node = parseJs(compiler, js);
    MakeDeclaredNamesUnique mdnu = new MakeDeclaredNamesUnique();
    NodeTraversal nt = new NodeTraversal(compiler, node);
    nt.traverse(mdnu);

    ContextualRenameInverter cri = new ContextualRenameInverter(compiler);
    nt.traverse(cri);

    Map<String, Integer> globalVarCounts = ImmutableSet.of(
        "a", 3,
        "b", 1,
        "c", 3,
        "g", 1).asMap();

    Deque<Scope> scopes = nt.getScopeStack();
    for (Scope scope : scopes) {
      Map<String, Integer> scopeVarCounts = Maps.newHashMap();
      for (Var var : scope.getVars()) {
        String name = var.getName();
        int count = scopeVarCounts.getOrDefault(name, 0);
        scopeVarCounts.put(name, count + 1);
      }
      assertEquals("Unexpected variable declarations in scope", globalVarCounts, scopeVarCounts);
    }

    List<Node> functions = Lists.newArrayList();
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.FUNCTION) {
        functions.add(child);
      }
    }
    assertEquals("Unexpected number of functions in code", 1, functions.size());

    Node function = functions.get(0);
    Map<String, Integer> functionVarCounts = Maps.newHashMap();
    for (Node child = function.getFirstChild(); child != null; child = child.getNext()) {
      if (isVarDeclaration(child)) {
        String name = child.getFirstChild().getString();
        int count = functionVarCounts.getOrDefault(name, 0);
        functionVarCounts.put(name, count + 1);
      }
    }
    assertEquals("Unexpected variable declarations in function", ImmutableSet.of("a", "b", "c", "i").asMap(), functionVarCounts);

    List<Node> functionBodies = Lists.newArrayList();
    for (Node child = function.getLastChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.BLOCK) {
        functionBodies.add(child);
      }
    }
    assertEquals("Unexpected number of function bodies", 1, functionBodies.size());

    Map<String, Integer> functionBodyVarCounts = Maps.newHashMap();
    for (Node child = functionBodies.get(0).getFirstChild(); child != null; child = child.getNext()) {
      if (isVarDeclaration(child)) {
        String name = child.getFirstChild().getString();
        int count = functionBodyVarCounts.getOrDefault(name, 0);
        functionBodyVarCounts.put(name, count + 1);
      }
    }
    assertEquals("Unexpected variable declarations in function body", ImmutableSet.of("a", "c").asMap(), functionBodyVarCounts);

    Map<String, Integer> catchVarCounts = Maps.newHashMap();
    for (Node child = functionBodies.get(0).getLastChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.CATCH) {
        String name = child.getFirstChild().getString();
        int count = catchVarCounts.getOrDefault(name, 0);
        catchVarCounts.put(name, count + 1);
      }
    }
    assertEquals("Unexpected catch variable declaration", 1, catchVarCounts.size());

    Map<String, Integer> functionGVarCounts = Maps.newHashMap();
    for (Scope scope = nt.getScopeStack().getLast(); scope != null; scope = scope.getOuterScope()) {
      for (Var var : scope.getVars()) {
        String name = var.getName();
        if (!var.isGlobal() && !var.getNameNode().isLocal()) {
          int count = functionGVarCounts.getOrDefault(name, 0);
          functionGVarCounts.put(name, count + 1);
        }
      }
    }
    assertEquals("Unexpected number of global variables", 5, functionGVarCounts.size());

    Map<String, Integer> functionGVarFunctionCounts = Maps.newHashMap();
    for (Node child = function.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.FUNCTION) {
        String name = child.getFirstChild().getString();
        int count = functionGVarFunctionCounts.getOrDefault(name, 0);
        functionGVarFunctionCounts.put(name, count + 1);
      }
    }
    assertEquals("Unexpected number of functions renaming global variables", 2, functionGVarFunctionCounts.size());

    Map<String, Integer> functionGVarCatchCounts = Maps.newHashMap();
    for (Node child = functionBodies.get(0).getLastChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.CATCH) {
        String name = child.getFirstChild().getString();
        int count = functionGVarCatchCounts.getOrDefault(name, 0);
        functionGVarCatchCounts.put(name, count + 1);
      }
    }
    assertEquals("Unexpected number of catch variables renaming global variables", 1, functionGVarCatchCounts.size());

    Map<String, Integer> functionHVarCounts = Maps.newHashMap();
    for (Node child = Lists.get(functionBodies, 0).getFirstChild().getLastChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.VAR) {
        String name = child.getFirstChild().getString();
        int count = functionHVarCounts.getOrDefault(name, 0);
        functionHVarCounts.put(name, count + 1);
      }
    }
    assertEquals("Unexpected number of variables in function h", 0, functionHVarCounts.size());

    Map<String, Integer> functionGVarCountsAfter = Maps.newHashMap();
    for (Scope scope = nt.getScopeStack().getLast(); scope != null; scope = scope.getOuterScope()) {
      for (Var var : scope.getVars()) {
        String name = var.getName();
        if (!var.isGlobal() && !var.getNameNode().isLocal()) {
          int count = functionGVarCountsAfter.getOrDefault(name, 0);
          functionGVarCountsAfter.put(name, count + 1);
        }
      }
    }
    Map<String, Integer> expectedFunctionGVarCountsAfter = ImmutableSet.of(
        "a", 1,
        "b", 1,
        "c", 1,
        "g", 1,
        "h", 1).asMap();
    assertEquals("Unexpected number of global variables after renaming", expectedFunctionGVarCountsAfter, functionGVarCountsAfter);
  }
}