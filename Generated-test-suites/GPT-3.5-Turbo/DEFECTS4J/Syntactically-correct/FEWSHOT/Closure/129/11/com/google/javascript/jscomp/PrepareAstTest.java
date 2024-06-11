package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    Node n = IR.empty();
    PrepareAst.prepareAst(n);
    // Add assert statements here
  }

  @Test
  public void testNormalizeBlocks() {
    Node n = IR.empty();
    PrepareAst.normalizeBlocks(n);
    // Add assert statements here
  }

  @Test
  public void testPrepareAnnotations() {
    Node n = IR.empty();
    PrepareAst.prepareAnnotations(n);
    // Add assert statements here
  }
}