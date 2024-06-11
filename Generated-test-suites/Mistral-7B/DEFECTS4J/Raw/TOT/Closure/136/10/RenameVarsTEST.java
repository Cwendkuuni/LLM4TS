import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassManager.PassManagerCallback;
import com.google.javascript.jscomp.CompilerPassManager.PassManagerPhase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.RenameVars.Assignment;
import com.google.javascript.jscomp.RenameVars.ProcessVars;
import com.google.javascript.rhino.SourceName;
import com.google.javascript.rhino.Token;

public class RenameVarsTest {

  private static final String TEST_CODE =
      "function Foo(fa, fb) {" +
      "  var c = function(d, e) { return fa; }" +
      "}";

  private static final String EXTERN_CODE =
      "var a = 1, b = 2, c = 3, d = 4, e = 5;" +
      "function foo(a, b, c) {}" +
      "function bar() {}";

  private CompilerPassManager passManager;
  private Node rootNode;

  @Before
  public void setUp() {
    passManager = new CompilerPassManager();
    rootNode = new Node(Token.PROGRAM, 0, null, null, SourceName.NONE, TEST_CODE);
  }

  @Test
  public void testRenameLocalVars() {
    String prefix = "prefix";
    RenameVars renamer = new RenameVars(new StubCompiler(), prefix, false, false,
        false, null, null, null);
    CompilerPassManager.registerPass(passManager, new PassManagerPhase() {
      @Override
      public void runPass(Node externs, Node root) {
        renamer.process(externs, root);
      }
    }, 100);
    passManager.runPasses(rootNode);

    List<Node> nodes = NodeTraversal.depthFirst(rootNode);
    for (int i = 0; i < nodes.size(); ++i) {
      Node node = nodes.get(i);
      if (node.getType() != Token.NAME) {
        continue;
      }
      String oldName = node.getString();
      if (oldName.length() == 0) {
        continue;
      }
      Assignment assignment = renamer.assignments.get(oldName);
      String newName = assignment != null ? assignment.newName : null;
      assertEquals(prefix + i, newName);
    }
  }

  @Test
  public void testRenameExternVars() {
    String prefix = "prefix";
    RenameVars renamer = new RenameVars(new StubCompiler(), prefix, false, false,
        false, null, null, null);
    CompilerPassManager.registerPass(passManager, new PassManagerPhase() {
      @Override
      public void runPass(Node externs, Node root) {
        renamer.process(externs, root);
      }
    }, 100);
    passManager.runPasses(Node.parse(EXTERN_CODE));

    Map<String, String> renameMap = renamer.getVariableMap();
    ListMultimap<String, String> map = ArrayListMultimap.create();
    for (Map.Entry<String, String> entry : renameMap.entrySet()) {
      map.put(entry.getKey(), entry.getValue());
    }
    Map<String, String> expectedMap = Maps.newHashMap();
    expectedMap.put("a", "a0");
    expectedMap.put("b", "b0");
    expectedMap.put("c", "c0");
    expectedMap.put("d", "d0");
    expectedMap.put("e", "e0");
    assertEquals(expectedMap, map);
  }

  @Test
  public void testRenameLocalVarsWithSameNames() {
    String prefix = "prefix";
    RenameVars renamer = new RenameVars(new StubCompiler(), prefix, false, false,
        false, null, null, null);
    CompilerPassManager.registerPass(passManager, new PassManagerPhase() {
      @Override
      public void runPass(Node externs, Node root) {
        renamer.process(externs, root);
      }
    }, 100);
    passManager.runPasses(Node.parse(TEST_CODE));

    List<Node> nodes = NodeTraversal.depthFirst(rootNode);
    for (int i = 0; i < nodes.size(); ++i) {
      Node node = nodes.get(i);
      if (node.getType() != Token.NAME) {
        continue;
      }
      String oldName = node.getString();
      if (oldName.length() == 0) {
        continue;
      }
      Assignment assignment = renamer.assignments.get(oldName);
      if (assignment == null) {
        continue;
      }
      String newName = assignment.newName;
      Assignment otherAssignment = null;
      for (Node otherNode : nodes) {
        if (otherNode.getType() != Token.NAME || otherNode == node) {
          continue;
        }
        String otherOldName = otherNode.getString();
        if (otherOldName.equals(oldName)) {
          otherAssignment = renamer.assignments.get(otherOldName);
          break;
        }
      }
      assertSame(newName, otherAssignment.newName);
    }
  }

  @Test
  public void testRenameWithLocalRenamingOnly() {
    String prefix = "prefix";
    RenameVars renamer = new RenameVars(new StubCompiler(), prefix, true, false,
        false, null, null, null);
    CompilerPassManager.registerPass(passManager, new PassManagerPhase() {
      @Override
      public void runPass(Node externs, Node root) {
        renamer.process(externs, root);
      }
    }, 100);
    passManager.runPasses(Node.parse(TEST_CODE));

    List<Node> nodes = NodeTraversal.depthFirst(rootNode);
    for (int i = 0; i < nodes.size(); ++i) {
      Node node = nodes.get(i);
      if (node.getType() != Token.NAME) {
        continue;
      }
      String oldName = node.getString();
      if (oldName.length() == 0) {
        continue;
      }
      Assignment assignment = renamer.assignments.get(oldName);
      String newName = assignment != null ? assignment.newName : null;
      assertEquals(oldName, newName);
    }
  }

  @Test
  public void testPreserveAnonymousFunctionNames() {
    String prefix = "prefix";
    RenameVars renamer = new RenameVars(new StubCompiler(), prefix, false, true,
        false, null, null, null);
    CompilerPassManager.registerPass(passManager, new PassManagerPhase() {
      @Override
      public void runPass(Node externs, Node root) {
        renamer.process(externs, root);
      }
    }, 100);
    passManager.runPasses(Node.parse(TEST_CODE));

    List<Node> nodes = NodeTraversal.depthFirst(rootNode);
    for (int i = 0; i < nodes.size(); ++i) {
      Node node = nodes.get(i);
      if (node.getType() != Token.NAME || node.isAnonymousFunction()) {
        continue;
      }
      String oldName = node.getString();
      if (oldName.length() == 0) {
        continue;
      }
      Assignment assignment = renamer.assignments.get(oldName);
      String newName = assignment != null ? assignment.newName : null;
      assertEquals(oldName, newName);
    }
  }

  @Test
  public void testGeneratePseudoNames() {
    String prefix = "prefix";
    RenameVars renamer = new RenameVars(new StubCompiler(), prefix, false, false,
        true, null, null, null);
    CompilerPassManager.registerPass(passManager, new PassManagerPhase() {
      @Override
      public void runPass(Node externs, Node root) {
        renamer.process(externs, root);
      }
    }, 100);
    passManager.runPasses(Node.parse(TEST_CODE));

    List<Node> nodes = NodeTraversal.depthFirst(rootNode);
    for (int i = 0; i < nodes.size(); ++i) {
      Node node = nodes.get(i);
      if (node.getType() != Token.NAME) {
        continue;
      }
      String oldName = node.getString();
      if (oldName.length() == 0) {
        continue;
      }
      Assignment assignment = renamer.assignments.get(oldName);
      String newName = assignment != null ? assignment.newName : null;
      assertTrue(newName.startsWith("$"));
    }
  }

  @Test
  public void testRenameVarsWithReservedNames() {
    String prefix = "";
    RenameVars renamer = new RenameVars(new StubCompiler(), prefix, false, false,
        false, null, null, Collections.singletonList("console"));
    CompilerPassManager.registerPass(passManager, new PassManagerPhase() {
      @Override
      public void runPass(Node externs, Node root) {
        renamer.process(externs, root);
      }
    }, 100);
    passManager.runPasses(Node.parse(TEST_CODE));

    List<Node> nodes = NodeTraversal.depthFirst(rootNode);
    boolean foundConsole = false;
    for (int i = 0; i < nodes.size(); ++i) {
      Node node = nodes.get(i);
      if (node.getType() != Token.NAME || node.isAnonymousFunction()
          || node.getString().equals("console")) {
        continue;
      }
      Assignment assignment = renamer.assignments.get(node.getString());
      String newName = assignment != null ? assignment.newName : null;
      assertNull(newName);
      foundConsole = true;
      break;
    }
    assertTrue(foundConsole);
  }

  @After
  public void tearDown() {
    passManager = null;
  }

  private static class StubCompiler implements Compiler {

    @Override
    public CompilerPassManager getPassManager() {
      return passManager;
    }

    @Override
    public void addToDebugLog(String message) {
      // no-op
    }

    @Override
    public void addToErrorLog(String message) {
      // no-op
    }

    @Override
    public int process(Node externs, Node root) {
      return -1;
    }

    @Override
    public void reportCodeChange() {
      // no-op
    }

    @Override
    public void reportError(String errorMessage) {
      // no-op
    }

    @Override
    public void reportWarning(String warningMessage) {
      // no-op
    }
  }
}