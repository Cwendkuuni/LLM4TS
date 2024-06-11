import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.*;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.graph.AbstractGraphNode;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testMustBeReachingVariableDef() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);

    // Test empty case
    Node useNode = Node.newNull();
    Node defNode = analysis.getDef(null, useNode);
    assertNull(defNode);

    // Test local variable
    String varName = "x";
    Node defNode1 = Node.newName(varName);
    Node useNode1 = Node.newName(varName);
    analysis.addToDefIfLocal(varName, defNode1, null, analysis.createEntryLattice());

    Node defNode2 = Node.newName(varName);
    Node useNode2 = Node.newName(varName);
    analysis.addToDefIfLocal(varName, defNode2, null, analysis.createEntryLattice());

    Node defNodeExpected = Node.newName(varName);
    assertEquals(defNodeExpected, analysis.getDef(varName, useNode1));
    assertEquals(defNodeExpected, analysis.getDef(varName, useNode2));

    // Test multiple definitions
    String varName2 = "y";
    Node defNode3 = Node.newName(varName2);
    Node useNode3 = Node.newName(varName2);
    analysis.addToDefIfLocal(varName2, defNode3, null, analysis.createEntryLattice());

    Node defNode4 = Node.newName(varName2);
    Node useNode4 = Node.newName(varName2);
    analysis.addToDefIfLocal(varName2, defNode4, null, analysis.createEntryLattice());

    Node defNodeExpected2 = Node.newName(varName2);
    Node defNodeAlternative = Node.newName(varName2);
    assertEquals(defNodeExpected2, analysis.getDef(varName2, useNode3));
    assertEquals(defNodeAlternative, analysis.getDef(varName2, useNode4));

    // Test dependencies
    String varName3 = "z";
    Node defNode5 = Node.newName(varName3);
    Node useNode5 = Node.newName(varName3);
    analysis.addToDefIfLocal(varName3, defNode5, null, analysis.createEntryLattice());

    Var varDep = jsScope.getVar(varName);
    Definition def = analysis.createEntryLattice().reachingDef.get(varDep);
    assertEquals(0, def.depends.size());

    Node defNode6 = Node.newName(varName3);
    Node useNode6 = Node.newName(varName3);
    analysis.addToDefIfLocal(varName3, defNode6, null, analysis.createEntryLattice());

    def = analysis.createEntryLattice().reachingDef.get(varDep);
    Set<Var> dependencies = def.depends;
    assertEquals(1, dependencies.size());
    Var depVar = dependencies.iterator().next();
    assertEquals(varName3, depVar.getName());

    // Test dependencies on outer scope variables
    String varNameOuter = "outerVar";
    Node defNodeOuter = Node.newName(varNameOuter);
    Node useNodeOuter = Node.newName(varNameOuter);
    analysis.addToDefIfLocal(varNameOuter, defNodeOuter, null, analysis.createEntryLattice());

    Var outerVar = jsScope.getVar(varNameOuter);
    def = analysis.createEntryLattice().reachingDef.get(outerVar);
    assertFalse(analysis.dependsOnOuterScopeVars(varName, useNode1));
    assertTrue(analysis.dependsOnOuterScopeVars(varNameOuter, useNode1));

    // Test escaping variables
    String varNameEscaped = "escapedVar";
    Var escapedVar = jsScope.setVar(varNameEscaped, Node.newName(varNameEscaped), null);
    analysis.escaped.add(escapedVar);

    Node defNodeEscaped = Node.newName(varNameEscaped);
    Node useNodeEscaped = Node.newName(varNameEscaped);
    analysis.addToDefIfLocal(varNameEscaped, defNodeEscaped, null, analysis.createEntryLattice());

    Node defNodeExpectedEscaped = null;
    assertEquals(defNodeExpectedEscaped, analysis.getDef(varNameEscaped, useNodeEscaped));
  }
}