import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.JSChunk;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.jscomp.parsing.Config;

public class UnreachableCodeEliminationTest {
  
  private UnreachableCodeElimination uce;
  private Compiler compiler;
  
  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.initOptions(new CompilerOptions());
    uce = new UnreachableCodeElimination(compiler, true);
  }
  
  @Test
  public void testRemoveDeadCodeAfterReturn() {
    String code = "function test() { return; alert('unreachable'); }";
    Node ast = parse(code);
    
    compiler.setNormalizedInputs(JSChunk.create(new FakeSourceFile("test.js", code)));
    compiler.parseInputs();
    
    uce.process(null, ast);
    
    // Check that the alert statement has been removed
    assertFalse(ast.getFirstChild().getLastChild().isFunction());
  }
  
  @Test
  public void testRemoveNoOpStatements() {
    String code = "function test() { a.b.MyClass.prototype.propertyName; true; }";
    Node ast = parse(code);
    
    compiler.setNormalizedInputs(JSChunk.create(new FakeSourceFile("test.js", code)));
    compiler.parseInputs();
    
    uce.process(null, ast);
    
    // Check that the statements have been removed
    assertEquals(0, ast.getFirstChild().getLastChild().getChildCount());
  }
  
  @Test
  public void testKeepStatementsWithSideEffects() {
    String code = "function test() { alert('side effect'); return; }";
    Node ast = parse(code);
    
    compiler.setNormalizedInputs(JSChunk.create(new FakeSourceFile("test.js", code)));
    compiler.parseInputs();
    
    uce.process(null, ast);
    
    // Check that the alert statement is still present
    assertTrue(ast.getFirstChild().getLastChild().isFunction());
  }
  
  private Node parse(String code) {
    Config parserConfig = ParserRunner.createConfig();
    return ParserRunner.parse(parserConfig, code);
  }
}