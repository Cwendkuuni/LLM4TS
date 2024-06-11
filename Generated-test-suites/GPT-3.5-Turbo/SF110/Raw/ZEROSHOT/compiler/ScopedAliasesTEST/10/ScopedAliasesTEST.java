import static org.junit.Assert.*;

import org.junit.Test;

public class ScopedAliasesTest {
  
  @Test
  public void testEnterScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable symbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, symbolTable, transformationHandler);
    
    NodeTraversal traversal = new NodeTraversal(compiler, null);
    traversal.getCurrentNode().getParent();
    
    scopedAliases.enterScope(traversal);
  }
  
  @Test
  public void testExitScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable symbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, symbolTable, transformationHandler);
    
    NodeTraversal traversal = new NodeTraversal(compiler, null);
    traversal.getCurrentNode().getParent();
    
    scopedAliases.exitScope(traversal);
  }
  
  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable symbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, symbolTable, transformationHandler);
    
    NodeTraversal traversal = new NodeTraversal(compiler, null);
    traversal.getCurrentNode().getParent();
    
    boolean result = scopedAliases.shouldTraverse(traversal, traversal.getCurrentNode(), traversal.getCurrentNode().getParent());
    
    assertTrue(result);
  }
  
  @Test
  public void testVisit() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable symbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, symbolTable, transformationHandler);
    
    NodeTraversal traversal = new NodeTraversal(compiler, null);
    traversal.getCurrentNode().getParent();
    
    scopedAliases.visit(traversal, traversal.getCurrentNode(), traversal.getCurrentNode().getParent());
  }
}