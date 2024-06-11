import com.google.javascript.jscomp.*;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {
  
  private CheckSideEffects checkSideEffects;
  private AbstractCompiler compiler;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
  }
  
  @Test
  public void testProcess() {
    Node externs = IR.root();
    Node root = IR.root();
    checkSideEffects.process(externs, root);
    // TODO: Add test assertions
  }
  
  @Test
  public void testHotSwapScript() {
    Node scriptRoot = IR.root();
    Node originalRoot = IR.root();
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    // TODO: Add test assertions
  }
  
  @Test
  public void testVisit() {
    NodeTraversal t = new NodeTraversal(compiler, null, checkSideEffects);
    Node n = IR.empty();
    Node parent = IR.empty();
    checkSideEffects.visit(t, n, parent);
    // TODO: Add test assertions
  }
  
  @Test
  public void testProtectSideEffects() {
    checkSideEffects.protectSideEffects();
    // TODO: Add test assertions
  }
  
  @Test
  public void testAddExtern() {
    checkSideEffects.addExtern();
    // TODO: Add test assertions
  }
  
  @Test
  public void testStripProtection() {
    Node externs = IR.root();
    Node root = IR.root();
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    stripProtection.process(externs, root);
    // TODO: Add test assertions
  }
  
}