package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Var;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testMakeDeclaredNamesUnique() {
    String jsCode = "function foo(a, b) {\n" +
        "  var x = 1;\n" +
        "  var x = 2;\n" +
        "  var y = 3;\n" +
        "  function bar() {}\n" +
        "  var y = 4;\n" +
        "}\n";

    Node rootNode = new TokenStream(jsCode).parse();

    NodeTraversal traverser = new NodeTraversal(rootNode, new MakeDeclaredNamesUnique());
    traverser.traverse();

    Scope rootScope = traverser.getScope();

    // Check that unique names have been assigned to variables
    List<Var> variables = Lists.newArrayList(rootScope.getVars().values());
    Map<String, Integer> nameCount = Maps.newHashMap();
    for (Var variable : variables) {
      String name = variable.name;
      int id = 0;
      if (name.contains(ContextualRenamer.UNIQUE_ID_SEPARATOR)) {
        id = Integer.parseInt(name.substring(name.lastIndexOf(ContextualRenamer.UNIQUE_ID_SEPARATOR) + 1));
      }
      nameCount.merge(name, 1, (e1, e2) -> e1 + e2);
    }
    assertEquals(3, nameCount.size());
    assertEquals(1, nameCount.get("x"));
    assertEquals(1, nameCount.get("y"));
    assertNull(nameCount.get("a"));
    assertNull(nameCount.get("b"));

    // Check that function names have not been modified
    Node functionNode = rootNode.getFirstChild();
    assertEquals("foo", functionNode.getFirstChild().getString());

    Node functionBody = functionNode.getLastChild();
    functionBody.getFirstChild(); // Skip 'function' token
    Var xVariable1 = (Var) functionBody.getNext().getFirstChild();
    Var xVariable2 = (Var) functionBody.getNext();
    Var yVariable1 = (Var) functionBody.getNext();
    Var yVariable2 = (Var) functionBody.getNext();
    assertEquals("x" + ContextualRenamer.UNIQUE_ID_SEPARATOR + "1", xVariable1.name);
    assertEquals("x" + ContextualRenamer.UNIQUE_ID_SEPARATOR + "2", xVariable2.name);
    assertEquals("y" + ContextualRenamer.UNIQUE_ID_SEPARATOR + "1", yVariable1.name);
    assertEquals("y" + ContextualRenamer.UNIQUE_ID_SEPARATOR + "2", yVariable2.name);
  }
}