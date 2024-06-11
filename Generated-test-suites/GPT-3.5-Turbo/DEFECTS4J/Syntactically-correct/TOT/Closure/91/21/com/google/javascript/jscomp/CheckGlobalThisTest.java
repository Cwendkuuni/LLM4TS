import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checker;
  private AbstractCompiler compiler;
  
  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }
  
  @Test
  public void testShouldTraverse_GlobalFunctionWithThisAnnotation_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    n.setJSDocInfo(new JSDocInfoBuilder().build()); // function has @this annotation
    boolean shouldTraverse = checker.shouldTraverse(t, n, null);
    
    assertFalse(shouldTraverse);
  }
  
  @Test
  public void testShouldTraverse_GlobalFunctionWithConstructorAnnotation_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    n.setJSDocInfo(new JSDocInfoBuilder().recordConstructor().build()); // function has @constructor annotation
    boolean shouldTraverse = checker.shouldTraverse(t, n, null);
    
    assertFalse(shouldTraverse);
  }
  
  @Test
  public void testShouldTraverse_GlobalFunctionWithOverrideAnnotation_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    n.setJSDocInfo(new JSDocInfoBuilder().recordOverride().build()); // function has @override annotation
    boolean shouldTraverse = checker.shouldTraverse(t, n, null);
    
    assertFalse(shouldTraverse);
  }
  
  @Test
  public void testShouldTraverse_NonGlobalFunction_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    boolean shouldTraverse = checker.shouldTraverse(t, n, parent);
    
    assertFalse(shouldTraverse);
  }
  
  @Test
  public void testShouldTraverse_FunctionInAssigningToPrototypeProperty_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(new Node(Token.GETPROP));
    parent.getFirstChild().addChildToBack(new Node(Token.STRING, "prototype"));
    boolean shouldTraverse = checker.shouldTraverse(t, n, parent);
    
    assertFalse(shouldTraverse);
  }
  
  @Test
  public void testShouldTraverse_FunctionInAssigningToNonPrototypeProperty_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(new Node(Token.GETPROP));
    parent.getFirstChild().addChildToBack(new Node(Token.STRING, "someProperty"));
    boolean shouldTraverse = checker.shouldTraverse(t, n, parent);
    
    assertTrue(shouldTraverse);
  }
  
  @Test
  public void testShouldTraverse_NonFunctionNodeInAssigningToPrototypeProperty_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.STRING, "someValue");
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(new Node(Token.GETPROP));
    parent.getFirstChild().addChildToBack(new Node(Token.STRING, "prototype"));
    boolean shouldTraverse = checker.shouldTraverse(t, n, parent);
    
    assertTrue(shouldTraverse);
  }
  
  @Test
  public void testVisit_GlobalThisInAssignment_ShouldReportError() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    boolean calledReportError = false;
    
    // Mock the compiler's report and check if it gets called
    compiler.report = (error) -> {
      calledReportError = true;
    };
    
    checker.visit(t, n, parent);
    
    assertTrue(calledReportError);
  }
  
  @Test
  public void testVisit_GlobalThisWithPropertyAccess_ShouldReportError() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    boolean calledReportError = false;
    
    // Mock the compiler's report and check if it gets called
    compiler.report = (error) -> {
      calledReportError = true;
    };
    
    checker.visit(t, n, parent);
    
    assertTrue(calledReportError);
  }
  
  @Test
  public void testVisit_NormalThis_ShouldNotReportError() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION);
    boolean calledReportError = false;
    
    // Mock the compiler's report and check if it gets called
    compiler.report = (error) -> {
      calledReportError = true;
    };
    
    checker.visit(t, n, parent);
    
    assertFalse(calledReportError);
  }
  
  @Test
  public void testVisit_ThisOnLeftSideOfAssignment_ShouldNotReportError() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    boolean calledReportError = false;
    
    // Mock the compiler's report and check if it gets called
    compiler.report = (error) -> {
      calledReportError = true;
    };
    
    checker.visit(t, n, parent);
    
    assertFalse(calledReportError);
  }
  
  @Test
  public void testVisit_ThisWithPropertyAccessButNotAssignment_ShouldNotReportError() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    boolean calledReportError = false;
    
    // Mock the compiler's report and check if it gets called
    compiler.report = (error) -> {
      calledReportError = true;
    };
    
    checker.visit(t, n, parent);
    
    assertFalse(calledReportError);
  }
}