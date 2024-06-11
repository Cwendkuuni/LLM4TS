package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for the MakeDeclaredNamesUnique class.
 */
public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testMakeDeclaredNamesUnique() {
    String jsCode = "function f(x) {"
        + "var a = 1; "
        + "var b = x; "
        + "var c = a; "
        + "var d = b; "
        + "var e = function g() {};"
        + "var f = function h() {};"
        + "var x = 5;"
        + "};";

    Node node = new TokenStream(jsCode).parse();
    NodeTraversal traversal = new NodeTraversal(node, new MakeDeclaredNamesUnique());
    traversal.traverse();

    Scope globalScope = traversal.getRootScope();

    // Test renamed variables
    ImmutableSet<String> expectedRenamedVariables = ImmutableSet.of(
        "a_1", "b_1", "c_1", "d_1", "x_1", "g_1", "h_1"
    );
    ImmutableSet<String> actualRenamedVariables = getRenamedVariables(globalScope);
    assertEquals(expectedRenamedVariables, actualRenamedVariables);

    // Test unchanged global variables
    ImmutableSet<String> expectedUnchangedVariables = ImmutableSet.of(
        "f"
    );
    ImmutableSet<String> actualUnchangedVariables = getGlobalVariables(globalScope);
    assertEquals(expectedUnchangedVariables, actualUnchangedVariables);
  }

  private ImmutableSet<String> getRenamedVariables(Scope scope) {
    Iterator<Map.Entry<Scope.Var, String>> iterator = scope.getVars().iterator();
    List<String> renamedVariables = Lists.newArrayList();
    while (iterator.hasNext()) {
      Map.Entry<Scope.Var, String> entry = iterator.next();
      renamedVariables.add(entry.getValue());
    }
    return ImmutableSet.copyOf(renamedVariables);
  }

  private ImmutableSet<String> getGlobalVariables(Scope scope) {
    return ImmutableSet.copyOf(getGlobalVariablesInScope(scope));
  }

  private List<String> getGlobalVariablesInScope(Scope scope) {
    if (scope.isGlobalScope()) {
      return Arrays.asList(scope.getVars().get(Token.NAME).getName());
    } else {
      return getGlobalVariablesInScope(scope.getParentScope());
    }
  }
}