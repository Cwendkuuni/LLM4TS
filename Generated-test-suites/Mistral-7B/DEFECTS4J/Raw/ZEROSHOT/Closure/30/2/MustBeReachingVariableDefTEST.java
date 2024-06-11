import static org.junit.Assert.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testMustBeReachingVariableDef() {
    // Given
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);

    // When
    Node root = createTestCode();
    MustDef result = mustBeReachingVariableDef.compute(root);

    // Then
    Map<String, Node> expectedDefs = Map.ofEntries(
        Map.entry("a", createNode("a = 1")),
        Map.entry("b", createNode("b = 2")),
        Map.entry("c", createNode("c = 3")),
        Map.entry("d", createNode("d = a")),
        Map.entry("e", createNode("e = b")),
        Map.entry("f", createNode("f = c")),
        Map.entry("g", createNode("g = d")),
        Map.entry("h", createNode("h = e")),
        Map.entry("i", createNode("i = f")),
        Map.entry("j", createNode("j = g")),
        Map.entry("k", createNode("k = h")),
        Map.entry("l", createNode("l = i")),
        Map.entry("m", createNode("m = j")),
        Map.entry("n", createNode("n = k")),
        Map.entry("o", createNode("o = l")),
        Map.entry("p", createNode("p = m"))
    );

    Map<String, Node> actualDefs = new HashMap<>();
    for (String name : jsScope.getVars().stream().map(Var::getName).toList()) {
      actualDefs.put(name, mustBeReachingVariableDef.getDef(name, root));
    }

    assertEquals(expectedDefs, actualDefs);

    // Then - dependencies
    Map<String, Set<String>> expectedDependencies = Map.ofEntries(
        Map.entry("a", Sets.newHashSet()),
        Map.entry("b", Sets.newHashSet("a")),
        Map.entry("c", Sets.newHashSet("a")),
        Map.entry("d", Sets.newHashSet("a")),
        Map.entry("e", Sets.newHashSet("b")),
        Map.entry("f", Sets.newHashSet("c")),
        Map.entry("g", Sets.newHashSet("d")),
        Map.entry("h", Sets.newHashSet("e")),
        Map.entry("i", Sets.newHashSet("f")),
        Map.entry("j", Sets.newHashSet("g")),
        Map.entry("k", Sets.newHashSet("h")),
        Map.entry("l", Sets.newHashSet("i")),
        Map.entry("m", Sets.newHashSet("j")),
        Map.entry("n", Sets.newHashSet("k")),
        Map.entry("o", Sets.newHashSet("l")),
        Map.entry("p", Sets.newHashSet("m"))
    );

    Map<String, Set<String>> actualDependencies = new HashMap<>();
    for (Entry<String, Definition> entry : result.reachingDef.entrySet()) {
      Definition def = entry.getValue();
      actualDependencies.put(entry.getKey(), def.depends);
    }

    assertEquals(expectedDependencies, actualDependencies);
  }

  private Node createTestCode() {
    // Arrange
    Node root = new Node_();
    root.setType(Token.BLOCK);

    // a = 1;
    Node aAssignment = new Node_();
    aAssignment.setType(Token.ASSIGN);
    aAssignment.setFirstChild(createNode("a"));
    aAssignment.setLastChild(createNode("1"));
    root.addChild(aAssignment);

    // b = 2;
    Node bAssignment = new Node_();
    bAssignment.setType(Token.ASSIGN);
    bAssignment.setFirstChild(createNode("b"));
    bAssignment.setLastChild(createNode("2"));
    root.addChild(bAssignment);

    // c = 3;
    Node cAssignment = new Node_();
    cAssignment.setType(Token.ASSIGN);
    cAssignment.setFirstChild(createNode("c"));
    cAssignment.setLastChild(createNode("3"));
    root.addChild(cAssignment);

    // d = a;
    Node dAssignment = new Node_();
    dAssignment.setType(Token.ASSIGN);
    dAssignment.setFirstChild(createNode("d"));
    dAssignment.setLastChild(createNode("a"));
    root.addChild(dAssignment);

    // e = b;
    Node eAssignment = new Node_();
    eAssignment.setType(Token.ASSIGN);
    eAssignment.setFirstChild(createNode("e"));
    eAssignment.setLastChild(createNode("b"));
    root.addChild(eAssignment);

    // f = c;
    Node fAssignment = new Node_();
    fAssignment.setType(Token.ASSIGN);
    fAssignment.setFirstChild(createNode("f"));
    fAssignment.setLastChild(createNode("c"));
    root.addChild(fAssignment);

    // g = d;
    Node gAssignment = new Node_();
    gAssignment.setType(Token.ASSIGN);
    gAssignment.setFirstChild(createNode("g"));
    gAssignment.setLastChild(createNode("d"));
    root.addChild(gAssignment);

    // h = e;
    Node hAssignment = new Node_();
    hAssignment.setType(Token.ASSIGN);
    hAssignment.setFirstChild(createNode("h"));
    hAssignment.setLastChild(createNode("e"));
    root.addChild(hAssignment);

    // i = f;
    Node iAssignment = new Node_();
    iAssignment.setType(Token.ASSIGN);
    iAssignment.setFirstChild(createNode("i"));
    iAssignment.setLastChild(createNode("f"));
    root.addChild(iAssignment);

    // j = g;
    Node jAssignment = new Node_();
    jAssignment.setType(Token.ASSIGN);
    jAssignment.setFirstChild(createNode("j"));
    jAssignment.setLastChild(createNode("g"));
    root.addChild(jAssignment);

    // k = h;
    Node kAssignment = new Node_();
    kAssignment.setType(Token.ASSIGN);
    kAssignment.setFirstChild(createNode("k"));
    kAssignment.setLastChild(createNode("h"));
    root.addChild(kAssignment);

    // l = i;
    Node lAssignment = new Node_();
    lAssignment.setType(Token.ASSIGN);
    lAssignment.setFirstChild(createNode("l"));
    lAssignment.setLastChild(createNode("i"));
    root.addChild(lAssignment);

    // m = j;
    Node mAssignment = new Node_();
    mAssignment.setType(Token.ASSIGN);
    mAssignment.setFirstChild(createNode("m"));
    mAssignment.setLastChild(createNode("j"));
    root.addChild(mAssignment);

    // n = k;
    Node nAssignment = new Node_();
    nAssignment.setType(Token.ASSIGN);
    nAssignment.setFirstChild(createNode("n"));
    nAssignment.setLastChild(createNode("k"));
    root.addChild(nAssignment);

    // o = l;
    Node oAssignment = new Node_();
    oAssignment.setType(Token.ASSIGN);
    oAssignment.setFirstChild(createNode("o"));
    oAssignment.setLastChild(createNode("l"));
    root.addChild(oAssignment);

    // p = m;
    Node pAssignment = new Node_();
    pAssignment.setType(Token.ASSIGN);
    pAssignment.setFirstChild(createNode("p"));
    pAssignment.setLastChild(createNode("m"));
    root.addChild(pAssignment);

    return root;
  }

  private Node createNode(String node) {
    Node newNode = new Node_();
    newNode.setType(Token.NAME);
    newNode.setString(node);
    return newNode;
  }
}