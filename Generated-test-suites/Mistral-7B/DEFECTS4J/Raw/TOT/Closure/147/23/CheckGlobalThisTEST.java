import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;

import static com.google.common.truth.Truth.assertThat;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private NodeFactory nodeFactory;
 private NodeTraversal traversal;
 private CheckGlobalThis checker;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 nodeFactory = NodeFactory.getInitial();
 Parser parser = new Parser(nodeFactory, new CompilerOptions());
 traversal = new NodeTraversal(parser.parse("test", new StringReader("")), compiler);
 checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 }

 @Test
 public void testSafeThisUsage() {
 Node functionNode = nodeFactory.program(
 new Node[] {
 NodeUtil.function("function safeThisUsage() { this.x = 42; }")
 });
 traversal.traverse(checker, functionNode, null);
 assertThat(checker.getNumWarnings()).isZero();
 }

 @Test
 public void testUnsafePrototypePropertyAssignment() {
 Node functionNode = nodeFactory.program(
 new Node[] {
 NodeUtil.function("function unsafePrototypeAssignment() { Object.prototype.x = this; }")
 });
 traversal.traverse(checker, functionNode, null);
 assertThat(checker.getNumWarnings()).isEqualTo(1);
 }

 @Test
 public void testUnsafeAssignment() {
 Node functionNode = nodeFactory.program(
 new Node[] {
 NodeUtil.function("function unsafeAssignment() { a = this; }")
 });
 traversal.traverse(checker, functionNode, null);
 assertThat(checker.getNumWarnings()).isZero();
 }

 @Test
 public void testUnsafeAssignmentInsideFunction() {
 Node functionNode = nodeFactory.program(
 new Node[] {
 NodeUtil.function("function outerFunction() { function innerFunction() { a = this; } }")
 });
 traversal.traverse(checker, functionNode, null);
 assertThat(checker.getNumWarnings()).isZero();
 }

 @Test
 public void testUnsafeAssignmentInsidePrototypeFunction() {
 Node functionNode = nodeFactory.program(
 new Node[] {
 NodeUtil.function("function outerFunction() { Object.prototype.innerFunction = function() { a = this; } }")
 });
 traversal.traverse(checker, functionNode, null);
 assertThat(checker.getNumWarnings()).isZero();
 }

 @Test
 public void testUnsafeMethodCall() {
 Node functionNode = nodeFactory.program(
 new Node[] {
 NodeUtil.function("function unsafeMethodCall() { this.method(); }")
 });
 traversal.traverse(checker, functionNode, null);
 assertThat(checker.getNumWarnings()).isZero();
 }

 @Test
 public void testUnsafeMethodCallInsidePrototypeFunction() {
 Node functionNode = nodeFactory.program(
 new Node[] {
 NodeUtil.function("function outerFunction() { Object.prototype.innerFunction = function() { this.method(); } }")
 });
 traversal.traverse(checker, functionNode, null);
 assertThat(checker.getNumWarnings()).isZero();
 }

 @Test
 public void testUnsafeConstructorCall() {
 Node functionNode = nodeFactory.program(
 new Node[] {
 NodeUtil.function("function unsafeConstructorCall() { new Object(); }")
 });
 traversal.traverse(checker, functionNode, null);
 assertThat(checker.getNumWarnings()).isZero();
 }

 @Test
 public void testUnsafeConstructorCallInsidePrototypeFunction() {
 Node functionNode = nodeFactory.program(
 new Node[] {
 NodeUtil.function("function outerFunction() { Object.prototype.innerFunction = function() { new Object(); } }")
 });
 traversal.traverse(checker, functionNode, null);
 assertThat(checker.getNumWarnings()).isZero();
 }

 @Test
 public void testUnsafeFunctionAnnotatedWithThis() {
 Node functionNode = nodeFactory.program(
 new Node[] {
 NodeUtil.function("@this function unsafeFunctionAnnotatedWithThis() {}")
 });
 traversal.traverse(checker, functionNode, null);
 assertThat(checker.getNumWarnings()).isZero();
 }

 @Test
 public void testUnsafeFunctionAnnotatedWithConstructor() {
 Node functionNode = nodeFactory.program(
 new Node[] {
 NodeUtil.function("@constructor function unsafeFunctionAnnotatedWithConstructor() {}")
 });
 traversal.traverse(checker, functionNode, null);
 assertThat(checker.getNumWarnings()).isZero();
 }

 @Test
 public void testUnsafeFunctionWithNoAnnotation() {
 Node functionNode = nodeFactory.program(
 new Node[] {
 NodeUtil.function("function unsafeFunctionWithNoAnnotation() {}")
 });
 traversal.traverse(checker, functionNode, null);
 assertThat(checker.getNumWarnings()).isZero();
 }
}