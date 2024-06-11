import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.Compiler;

public class CheckGlobalThisTest {
  
  @Test
  public void shouldReportGlobalThis_AssignLhsChildNotNull() {
    AbstractCompiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS, "this");
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.visit(nodeTraversal, n, parent);
    assertTrue(compiler.getErrorCount() > 0);
  }
  
  @Test
  public void shouldReportGlobalThis_ParentIsGet() {
    AbstractCompiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS, "this");
    Node parent = new Node(Token.GETPROP);
    checkGlobalThis.visit(nodeTraversal, n, parent);
    assertTrue(compiler.getErrorCount() > 0);
  }
  
  @Test
  public void shouldNotReportGlobalThis() {
    AbstractCompiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS, "this");
    Node parent = null;
    checkGlobalThis.visit(nodeTraversal, n, parent);
    assertEquals(0, compiler.getErrorCount());
  }
  
}