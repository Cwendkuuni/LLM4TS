
package com.example.junit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Token;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.rhino.ModuleGraph;
import org.junit.Test;

public class VarCheckTest {

  @Test
  public void testProcess() {
    VarCheck varCheck = new VarCheck(mock(AbstractCompiler.class));
    Node externs = mock(Node.class);
    Node root = mock(Node.class);

    when(externs.getType()).thenReturn(Token.SCRIPT);
    when(root.getType()).thenReturn(Token.SCRIPT);

    varCheck.process(externs, root);
    assertFalse(varCheck.varsToDeclareInExterns.isEmpty());
  }

  // Test cases for visit() method here

  // Test cases for createSynthesizedExternVar() method here

  @Test
  public void testNameRefInExternsCheck() {
    VarCheck varCheck = new VarCheck(mock(AbstractCompiler.class));
    varCheck.new NameRefInExternsCheck();

    Node n = mock(Node.class);
    Node parent = mock(Node.class);

    when(n.getType()).thenReturn(Token.NAME);
    when(n.getString()).thenReturn("foo");
    when(parent.getType()).thenReturn(Token.VAR);

    varCheck.getNameRefInExternsCheck().visit(mock(NodeTraversal.class), n, parent);

    assertEquals("JSC_NAME_REFERENCE_IN_EXTERNS", varCheck.lastDiagnostic().getType().getName());
  }
}
