import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testMakeDeclaredNamesUnique() {
    String jsCode = "function f(a, b) {" +
        "  var x = 1, y = 1, z = a + b;" +
        "  function g(c) {" +
        "    var x = 2, z = c + 1;" +
        "  }" +
        "}";

    Node rootNode = new TokenStream(jsCode).parse();
    NodeTraversal traversal = new NodeTraversal(rootNode);
    MakeDeclaredNamesUnique uniqueRenamer = new MakeDeclaredNamesUnique();

    uniqueRenamer.traverse(traversal);
    Node newRootNode = traversal.getRootNode();

    // Check that all local variables got renamed in the function
    List<Node> functions = Lists.newArrayList(
        NodeUtil.findAllDescendantsOfType(newRootNode, Token.FUNCTION));
    for (Node function : functions) {
      List<Node> vars = Lists.newArrayList(
          NodeUtil.findAllDescendantsOfType(function, Token.VAR));

      Map<String, Integer> originalNames = Maps.newHashMap();
      for (Node var : vars) {
        originalNames.put(var.getString(), null);
      }

      List<Node> renamedVars = Lists.newArrayList();
      for (Node renamedVar : NodeUtil.findAllDescendantsOfType(function, Token.NAME)) {
        String name = renamedVar.getString();
        if (originalNames.containsKey(name)) {
          renamedVars.add(renamedVar);
        }
        assertThat(name).isNotEmpty();
      }

      // Check that all variables got renamed
      Iterator<Map.Entry<String, Integer>> iterator = originalNames.entrySet().iterator();
      while (iterator.hasNext()) {
        Map.Entry<String, Integer> entry = iterator.next();
        String originalName = entry.getKey();
        int index = 1;
        String expectedName = originalName + MakeDeclaredNamesUnique.UNIQUE_ID_SEPARATOR + index++;

        boolean found = false;
        for (Node renamedVar : renamedVars) {
          if (renamedVar.getString().equals(expectedName)) {
            found = true;
            break;
          }
        }

        if (!found) {
          throw new AssertionError("Missing renaming for: " + originalName);
        }
      }
    }

    // Check that function and global names remain the same
    List<Node> functionDeclarations = Lists.newArrayList(
        NodeUtil.findAllDescendantsOfType(newRootNode, Token.FUNCTION, true));
    for (Node functionDeclaration : functionDeclarations) {
      List<Node> functionName = Lists.newArrayList(
          NodeUtil.findAllDescendantsOfType(functionDeclaration, Token.NAME, true));
      if (functionName.size() > 0) {
        Node functionNameNode = functionName.get(0);
        String functionNameString = functionNameNode.getString();
        assertThat(functionNameString).isEqualTo("f");
      }
    }

    List<Node> globalDeclarations = Lists.newArrayList(
        NodeUtil.findAllDescendantsOfType(newRootNode, NodeUtil.IS_GLOBAL_DECLARATION, true));
    for (Node globalDeclaration : globalDeclarations) {
      List<Node> globalName = Lists.newArrayList(
          NodeUtil.findAllDescendantsOfType(globalDeclaration, Token.NAME, true));
      if (globalName.size() > 0) {
        Node globalNameNode = globalName.get(0);
        String globalNameString = globalNameNode.getString();
        assertThat(globalNameString).isEqualTo("x");
      }
    }
  }

}