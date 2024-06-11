import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Annotations;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.NodeFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(org.robolectric.RobolectricTestRunner.class)
public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private Checker checker;
 private CompilerOptions options;

 @Before
 public void setUp() {
 Annotations.initMocks(this);
 options = new CompilerOptions(null);
 compiler = new AbstractCompiler(options);
 checker = new Checker(compiler);
 }

 @Test
 public void testCheckGlobalThisSimpleFunction() {
 String source = "function evil() { this.useful = undefined; }";
 Node node = NodeFactory.compile(source);
 NodeTraversal traverser = new NodeTraversal(checker, node);
 traverser.traverse();
 assertNotNull(checker.getDiagnostics());
 assertEquals(1, checker.getDiagnostics().size());
 assertEquals(DiagnosticType.WARNING, checker.getDiagnostics().get(0).getType());
 }

 @Test
 public void testCheckGlobalThisPrototypeProperty() {
 String source = "function evil() { Object.prototype.useful = undefined; }";
 Node node = NodeFactory.compile(source);
 NodeTraversal traverser = new NodeTraversal(checker, node);
 traverser.traverse();
 assertNull(checker.getDiagnostics());
 }

 @Test
 public void testCheckGlobalThisConstructor() {
 String source = "function Evil() { this.useful = undefined; }";
 Node node = NodeFactory.compile(source);
 NodeTraversal traverser = new NodeTraversal(checker, node);
 traverser.traverse();
 assertNull(checker.getDiagnostics());
 }

 @Test
 public void testCheckGlobalThisFunctionWithAnnotation() {
 String source = "/** @constructor */\nfunction Good() { this.useful = undefined; }";
 Node node = NodeFactory.compile(source);
 NodeTraversal traverser = new NodeTraversal(checker, node);
 traverser.traverse();
 assertNull(checker.getDiagnostics());
 }

 @Test
 public void testCheckGlobalThisFunctionInBlock() {
 String source = "function evil() { }\n{\n this.useful = undefined;\n}";
 Node node = NodeFactory.compile(source);
 NodeTraversal traverser = new NodeTraversal(checker, node);
 traverser.traverse();
 assertNotNull(checker.getDiagnostics());
 assertEquals(1, checker.getDiagnostics().size());
 assertEquals(DiagnosticType.WARNING, checker.getDiagnostics().get(0).getType());
 }

 @Test
 public void testCheckGlobalThisFunctionInScript() {
 String source = "function evil() { this.useful = undefined; }";
 Node node = NodeFactory.compile(source);
 NodeTraversal traverser = new NodeTraversal(checker, new TestSourceFile("test.js", source).getTree());
 traverser.traverse();
 assertNotNull(checker.getDiagnostics());
 assertEquals(1, checker.getDiagnostics().size());
 assertEquals(DiagnosticType.WARNING, checker.getDiagnostics().get(0).getType());
 }

 @Test
 public void testCheckGlobalThisFunctionInAnonymousFunction() {
 String source = "function good() { }\nfunction evil() { good.call(this); this.useful = undefined; }";
 Node node = NodeFactory.compile(source);
 NodeTraversal traverser = new NodeTraversal(checker, node);
 traverser.traverse();
 assertNull(checker.getDiagnostics());
 }

 @Test
 public void testCheckGlobalThisFunctionInAnonymousFunctionWithThis() {
 String source = "function good() { }\nfunction evil() { good.call(this); this.useful = this; }";
 Node node = NodeFactory.compile(source);
 NodeTraversal traverser = new NodeTraversal(checker, node);
 traverser.traverse();
 assertNull(checker.getDiagnostics());
 }

 @Test
 public void testCheckGlobalThisFunctionWithVar() {
 String source = "function evil() { var a = this; this.useful = undefined; }";
 Node node = NodeFactory.compile(source);
 NodeTraversal traverser = new NodeTraversal(checker, node);
 traverser.traverse();
 assertNull(checker.getDiagnostics());
 }

 @Test
 public void testCheckGlobalThisFunctionWithVarAndAssignment() {
 String source = "function evil() { var a = this; this.useful = a; }";
 Node node = NodeFactory.compile(source);
 NodeTraversal traverser = new NodeTraversal(checker, node);
 traverser.traverse();
 assertNull(checker.getDiagnostics());
 }

 @Test
 public void testCheckGlobalThisFunctionWithVarAndPropertyAccess() {
 String source = "function evil() { var a = this; this.useful = a.prop; }";
 Node node = NodeFactory.compile(source);
 NodeTraversal traverser = new NodeTraversal(checker, node);
 traverser.traverse();
 assertNull(checker.getDiagnostics());
 }

 @Test
 public void testCheckGlobalThisFunctionWithVarAndPropertyAccessInPrototype() {
 String source = "function evil() { var a = this; this.prop = {useful: a}; }";
 Node node = NodeFactory.compile(source);
 NodeTraversal traverser = new NodeTraversal(checker, node);
 traverser.traverse();
 assertNotNull(checker.getDiagnostics());
 assertEquals(1, checker.getDiagnostics().size());
 assertEquals(DiagnosticType.WARNING, checker.getDiagnostics().get(0).getType());
 }
}