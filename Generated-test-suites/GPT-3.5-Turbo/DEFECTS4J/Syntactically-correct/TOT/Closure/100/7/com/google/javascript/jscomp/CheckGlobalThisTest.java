import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_Function_Is_Constructor() {
    AbstractCompiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis check = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, check);

    Node functionNode = Node.newString(Token.FUNCTION, "function");
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    functionNode.setJSDocInfo(jsDoc);
    
    boolean shouldTraverse = check.shouldTraverse(t, functionNode, null);
    
    assertFalse(shouldTraverse);
  }

  @Test
  public void testShouldTraverse_Function_Has_This_Annotation() {
    AbstractCompiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis check = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, check);

    Node functionNode = Node.newString(Token.FUNCTION, "function");
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    functionNode.setJSDocInfo(jsDoc);
    
    boolean shouldTraverse = check.shouldTraverse(t, functionNode, null);
    
    assertFalse(shouldTraverse);
  }

  @Test
  public void testShouldTraverse_Function_Is_Not_Constructor_Nor_Has_This_Annotation() {
    AbstractCompiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis check = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, check);

    Node functionNode = Node.newString(Token.FUNCTION, "function");
    
    boolean shouldTraverse = check.shouldTraverse(t, functionNode, null);
    
    assertTrue(shouldTraverse);
  }
  
  @Test
  public void testVisit_This_Inside_Assignment_Node() {
    AbstractCompiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis check = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, check);

    Node thisNode = Node.newString(Token.THIS, "this");
    Node assignNode = Node.newString(Token.ASSIGN, "=");
    assignNode.addChildToBack(thisNode);
    
    check.visit(t, thisNode, assignNode);
    
    assertTrue(compiler.getErrorCount() > 0);
  }
  
  @Test
  public void testVisit_This_Not_Inside_Assignment_Node() {
    AbstractCompiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis check = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, check);

    Node thisNode = Node.newString(Token.THIS, "this");
    
    check.visit(t, thisNode, null);
    
    assertFalse(compiler.getErrorCount() > 0);
  }

}