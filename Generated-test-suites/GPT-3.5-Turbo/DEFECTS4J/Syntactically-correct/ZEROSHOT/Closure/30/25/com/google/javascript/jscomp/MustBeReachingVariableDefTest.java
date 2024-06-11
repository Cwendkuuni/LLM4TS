package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class MustBeReachingVariableDefTest {

  ControlFlowGraph<Node> cfg;
  Scope jsScope;
  AbstractCompiler compiler;
  MustBeReachingVariableDef variableDef;

  @Before
  public void setUp() {
    cfg = new ControlFlowGraph<>();
    jsScope = new Scope();
    compiler = new AbstractCompiler();
    variableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }

  @Test
  public void testGetDef() {
    String name = "A";
    Node useNode = new Node(Token.VAR);
    Node def = variableDef.getDef(name, useNode);
    assertNull(def);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    String name = "A";
    Node useNode = new Node(Token.VAR);
    boolean dependsOnOuterScopeVars = variableDef.dependsOnOuterScopeVars(name, useNode);
    assertFalse(dependsOnOuterScopeVars);
  }

}