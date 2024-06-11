import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

 private Compiler compiler;

 @Before
 public void setUp() {
 compiler = new Compiler();
 }

 @Test
 public void testSafeUsesOfThis() {
 String code = "function Foo() { this.x = 1; }";
 Node node = AbstractCompiler.parseTree(code);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(node, checker);
 traversal.traverseAll();
 assertFalse(checker.hasErrors());
 }

 @Test
 public void testUnsafeUsesOfThisAsProperty() {
 String code = "var a = {}; a.x = this;";
 Node node = AbstractCompiler.parseTree(code);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(node, checker);
 traversal.traverseAll();
 assertTrue(checker.hasErrors());
 }

 @Test
 public void testUnsafeUsesOfThisAsFunction() {
 String code = "function evil() { this.useful = undefined; }";
 Node node = AbstractCompiler.parseTree(code);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(node, checker);
 traversal.traverseAll();
 assertTrue(checker.hasErrors());
 }

 @Test
 public void testUnsafeUsesOfThisAsFunctionWithParentProperty() {
 String code = "var a = {x: function() { this.useful = undefined; }};";
 Node node = AbstractCompiler.parseTree(code);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(node, checker);
 traversal.traverseAll();
 assertTrue(checker.hasErrors());
 }

 @Test
 public void testCheckGlobalThisWithNoThisAnnotation() {
 String code = "function evil() { this.x = undefined; }";
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.ERROR);
 Node node = AbstractCompiler.parseTree(code);
 NodeTraversal traversal = new NodeTraversal(node, checker);
 traversal.traverseAll();
 assertTrue(checker.hasErrors());
 }

 @Test
 public void testCheckGlobalThisWithConstructorAnnotation() {
 String code = "function Foo() { this.x = 1; }";
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(NodeUtil.parseTree(code), checker);
 traversal.traverseAll();
 assertFalse(checker.hasErrors());
 }

 @Test
 public void testCheckGlobalThisWithThisAnnotation() {
 String code = "function Foo@this() { this.x = 1; }";
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(NodeUtil.parseTree(code), checker);
 traversal.traverseAll();
 assertFalse(checker.hasErrors());
 }

 @Test
 public void testCheckGlobalThisWithPrototypeProperty() {
 String code = "function Foo() { } Foo.prototype.x = function() { this.useful = undefined; }";
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(NodeUtil.parseTree(code), checker);
 traversal.traverseAll();
 assertFalse(checker.hasErrors());
 }

 @Test
 public void testCheckGlobalThisWithPrototypeMethod() {
 String code = "function Foo() { } Foo.prototype.x = function() { this.useful = undefined; }";
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(NodeUtil.parseTree(code), checker);
 traversal.traverseAll();
 assertFalse(checker.hasErrors());
 }
}