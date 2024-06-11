import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;

public class CheckSideEffectsTest {

 private Compiler compiler;
 private CompilerOptions options;

 @Before
 public void setUp() {
 compiler = new Compiler();
 options = new CompilerOptions();
 }

 @Test
 public void testCheckSideEffects_SimpleStatement() {
 String code = "var x = 10;";
 Node ast = NodeOperations.parse(code, options);
 CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 checkSideEffects.process(null, ast);
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testCheckSideEffects_EmptyStatement() {
 String code = ";";
 Node ast = NodeOperations.parse(code, options);
 CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 checkSideEffects.process(null, ast);
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testCheckSideEffects_VoidStatement() {
 String code = "var x = function(){};";
 Node ast = NodeOperations.parse(code, options);
 CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 checkSideEffects.process(null, ast);
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testCheckSideEffects_ForLoop() {
 String code = "for (var i = 0; i < 10; i++) {}";
 Node ast = NodeOperations.parse(code, options);
 CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 checkSideEffects.process(null, ast);
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testCheckSideEffects_AssignmentStatement() {
 String code = "x = foo();";
 Node ast = NodeOperations.parse(code, options);
 CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 checkSideEffects.process(null, ast);
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testCheckSideEffects_CallStatementWithSideEffect() {
 String code = "console.log('Hello World');";
 Node ast = NodeOperations.parse(code, options);
 CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 checkSideEffects.process(null, ast);
 assertFalse(checkSideEffects.problemNodes.isEmpty());
 assertEquals(1, checkSideEffects.problemNodes.size());
 }

 @Test(expected = NullPointerException.class)
 public void testCheckSideEffects_NullStatement() {
 String code = null;
 Node ast = NodeOperations.parse(code, options);
 CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 checkSideEffects.process(null, ast);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckSideEffects_InvalidStatement() {
 String code = "invalid statement;";
 Node ast = NodeOperations.parse(code, options);
 CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 checkSideEffects.process(null, ast);
 }

 @Test
 public void testProtectSideEffectsAndStripProtection() {
 String code = "var x = 10;";
 Node ast = NodeOperations.parse(code, options);
 CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
 checkSideEffects.process(null, ast);
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 StripProtection stripProtection = new StripProtection(compiler);
 stripProtection.process(null, ast);
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }
}