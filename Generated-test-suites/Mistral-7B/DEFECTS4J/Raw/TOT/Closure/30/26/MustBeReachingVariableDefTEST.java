import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testSimpleAssignment() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();
    AbstractCompiler compiler = new CompilerPass();
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = new Node(Token.BLOCK, null);
    Node n1 = new Node(Token.VAR, null, "a");
    Node n2 = new Node(Token.ASSIGN, null, n1, new Node(Token.NAME, null, "b"));
    Node n3 = new Node(Token.ASSIGN, null, n1, new Node(Token.NUM, null, 5));
    root.addChild(n1);
    root.addChild(n2);
    root.addChild(n3);

    cfg.buildGraphFromSingleEntry(root);
    Node useNode = cfg.getEntryNode().getFirstSuccessor();

    assertSame(cfg.getNode(n2).getAnnotation().getIn().node,
        analyzer.getDef("b", useNode));
    assertNull(analyzer.getDef("c", useNode));
  }

  @Test
  public void testAssignmentWithDependencies() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();
    AbstractCompiler compiler = new CompilerPass();
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = new Node(Token.BLOCK, null);
    Node n1 = new Node(Token.VAR, null, "a");
    Node n2 = new Node(Token.VAR, null, "b");
    Node n3 = new Node(Token.ASSIGN, null, n1, new Node(Token.NAME, null, "b"));
    Node n4 = new Node(Token.ASSIGN, null, n1, new Node(Token.NAME, null, "a"));
    root.addChild(n1);
    root.addChild(n2);
    root.addChild(n3);
    root.addChild(n4);

    cfg.buildGraphFromSingleEntry(root);
    Node useNode = cfg.getEntryNode().getFirstSuccessor();

    Definition def = analyzer.getDef("a", useNode).cast(Definition.class);
    assertSame(cfg.getNode(n4).getAnnotation().getIn().node, def.node);
    assertSame(cfg.getNode(n3).getAnnotation().getIn().node, def.node);
  }

  @Test
  public void testAssignmentWithEscapedVariable() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();
    AbstractCompiler compiler = new CompilerPass();
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = new Node(Token.BLOCK, null);
    Node n1 = new Node(Token.VAR, null, "a");
    Node n2 = new Node(Token.ASSIGN, null, n1, new Node(Token.NAME, null, "a"));
    Node n3 = new Node(Token.ASSIGN, null, new Node(Token.NAME, null, "global"), n1);
    root.addChild(n1);
    root.addChild(n2);
    root.addChild(n3);

    cfg.buildGraphFromSingleEntry(root);
    Node useNode = cfg.getEntryNode().getFirstSuccessor();

    assertNull(analyzer.getDef("a", useNode));
  }

  @Test
  public void testFunctionParameter() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();
    AbstractCompiler compiler = new CompilerPass();
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = new Node(Token.BLOCK, null);
    Node n1 = new Node(Token.FUNCTION, null, new Node(Token.NAME, null, "f"), null);
    Node n2 = new Node(Token.VAR, null, "a");
    Node n3 = new Node(Token.ASSIGN, null, n2, new Node(Token.NUM, null, 5));
    Node n4 = new Node(Token.NAME, null, "a");
    n1.addChild(n2);
    n1.addChild(n3);
    n1.addChild(n4);
    root.addChild(n1);

    cfg.buildGraphFromSingleEntry(root);
    Node useNode = cfg.getEntryNode().getFirstSuccessor();

    assertSame(cfg.getNode(n3).getAnnotation().getIn().node,
        analyzer.getDef("a", useNode));
  }

  @Test
  public void testFunctionCall() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();
    AbstractCompiler compiler = new CompilerPass();
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = new Node(Token.BLOCK, null);
    Node n1 = new Node(Token.VAR, null, "a");
    Node n2 = new Node(Token.FUNCTION, null, new Node(Token.NAME, null, "f"), null);
    Node n3 = new Node(Token.ASSIGN, null, n1, new Node(Token.NAME, null, "a"));
    Node n4 = new Node(Token.ASSIGN, null, n1, new Node(Token.NAME, null, "b"));
    Node n5 = new Node(Token.NAME, null, "f");
    Node n6 = new Node(Token.NUM, null, 5);
    n1.addChild(n3);
    n1.addChild(n4);
    n2.addChild(n1);
    n2.addChild(n5);
    n2.addChild(n6);
    root.addChild(n1);
    root.addChild(n2);

    cfg.buildGraphFromSingleEntry(root);
    Node useNode = cfg.getEntryNode().getFirstSuccessor();

    assertSame(cfg.getNode(n6).getAnnotation().getIn().node,
        analyzer.getDef("b", useNode));
  }

  @Test
  public void testComplexControlFlow() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();
    AbstractCompiler compiler = new CompilerPass();
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = new Node(Token.BLOCK, null);
    Node n1 = new Node(Token.IF, null, new Node(Token.NAME, null, "x"), null);
    Node n2 = new Node(Token.NAME, null, "a");
    Node n3 = new Node(Token.ASSIGN, null, n2, new Node(Token.NUM, null, 5));
    Node n4 = new Node(Token.BLOCK, null);
    Node n5 = new Node(Token.VAR, null, "x");
    Node n6 = new Node(Token.ASSIGN, null, n5, new Node(Token.NUM, null, 1));
    Node n7 = new Node(Token.NAME, null, "a");
    Node n8 = new Node(Token.ASSIGN, null, n7, new Node(Token.NAME, null, "b"));
    Node n9 = new Node(Token.IF, null, new Node(Token.NAME, null, "x"), null);
    Node n10 = new Node(Token.NAME, null, "b");
    Node n11 = new Node(Token.ASSIGN, null, n10, new Node(Token.NAME, null, "x"));
    Node n12 = new Node(Token.NAME, null, "a");
    Node n13 = new Node(Token.ASSIGN, null, n12, new Node(Token.NUM, null, 10));
    Node n14 = new Node(Token.NAME, null, "x");
    Node n15 = new Node(Token.ASSIGN, null, n14, new Node(Token.NAME, null, "y"));
    Node n16 = new Node(Token.NAME, null, "y");
    Node n17 = new Node(Token.RETURN, null, null, n16);
    n1.addChild(n3);
    n1.addChild(n4);
    n1.addChild(n5);
    n4.addChild(n6);
    n4.addChild(n7);
    n4.addChild(n8);
    n9.addChild(n11);
    n9.addChild(n12);
    n9.addChild(n13);
    n13.addChild(n14);
    n13.addChild(n15);
    n13.addChild(n17);
    root.addChild(n1);

    cfg.buildGraphFromSingleEntry(root);
    Node useNode = cfg.getEntryNode().getFirstSuccessor();

    Definition aDef = analyzer.getDef("a", useNode).cast(Definition.class);
    assertSame(cfg.getNode(n3).getAnnotation().getIn().node, aDef.node);
    assertSame(cfg.getNode(n8).getAnnotation().getIn().node, aDef.node);

    Definition bDef = analyzer.getDef("b", useNode).cast(Definition.class);
    assertSame(cfg.getNode(n10).getAnnotation().getIn().node, bDef.node);
    assertSame(cfg.getNode(n11).getAnnotation().getIn().node, bDef.node);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();
    AbstractCompiler compiler = new CompilerPass();
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = new Node(Token.BLOCK, null);
    Node n1 = new Node(Token.VAR, null, "a");
    Node n2 = new Node(Token.VAR, null, "b");
    Node n3 = new Node(Token.ASSIGN, null, n1, new Node(Token.NAME, null, "b"));
    Node n4 = new Node(Token.ASSIGN, null, n1, new Node(Token.NAME, null, "a"));
    Node n5 = new Node(Token.BLOCK, null);
    Node n6 = new Node(Token.VAR, null, "c");
    Node n7 = new Node(Token.ASSIGN, null, n6, new Node(Token.NUM, null, 5));
    Node n8 = new Node(Token.NAME, null, "a");
    Node n9 = new Node(Token.ASSIGN, null, n8, new Node(Token.NAME, null, "c"));
    Node n10 = new Node(Token.NAME, null, "c");
    Node n11 = new Node(Token.RETURN, null, null, n10);
    n1.addChild(n3);
    n1.addChild(n4);
    n5.addChild(n6);
    n5.addChild(n7);
    n5.addChild(n8);
    n5.addChild(n9);
    root.addChild(n1);
    root.addChild(n5);

    cfg.buildGraphFromSingleEntry(root);
    Node useNode = cfg.getEntryNode().getFirstSuccessor();

    assertSame(cfg.getNode(n7).getAnnotation().getIn().node,
        analyzer.getDef("c", useNode));
    assertTrue(analyzer.dependsOnOuterScopeVars("a", useNode));
    assertTrue(analyzer.dependsOnOuterScopeVars("b", useNode));
  }
}