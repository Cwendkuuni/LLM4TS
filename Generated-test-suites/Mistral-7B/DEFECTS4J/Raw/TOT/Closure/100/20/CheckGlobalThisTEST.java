import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.CheckGlobalThis;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private DiagnosticGroup diagnostics;

 @Before
 public void setUp() {
 compiler = new Compiler();
 compiler.addProcessor(new CheckGlobalThis(compiler, CheckLevel.WARNING));
 }

 @Test
 public void testSafeUsage() {
 String jsCode = "function Foo() { this.x = 1; }";
 Diagnostics diag = testJsComp(jsCode);
 assertFalse(diag.hasError());
 }

 @Test
 public void testPrototypeMethod() {
 String jsCode = "function Foo() { this.getName = function() { return 'Name'; } }";
 Diagnostics diag = testJsComp(jsCode);
 assertFalse(diag.hasError());
 }

 @Test
 public void testConstructorAnnotation() {
 String jsCode = "function Foo() { @constructor }";
 Diagnostics diag = testJsComp(jsCode);
 assertFalse(diag.hasError());
 }

 @Test
 public void testThisAnnotation() {
 String jsCode = "function Foo() { @this }";
 Diagnostics diag = testJsComp(jsCode);
 assertFalse(diag.hasError());
 }

 @Test
 public void testAssignmentToVariable() {
 String jsCode = "function Foo() { var a = this; }";
 Diagnostics diag = testJsComp(jsCode);
 assertFalse(diag.hasError());
 }

 @Test
 public void testAssignmentToFunction() {
 String jsCode = "function Foo() { var f = function() { this.x = 1; } }";
 Diagnostics diag = testJsComp(jsCode);
 assertFalse(diag.hasError());
 }

 @Test
 public void testAssignmentToPrototype() {
 String jsCode = "function Foo() { Object.prototype.x = this; }";
 Diagnostics diag = testJsComp(jsCode);
 assertFalse(diag.hasError());
 }

 @Test
 public void testAssignmentToPropertyAccess() {
 String jsCode = "function Foo() { Object.prototype.x.y = this; }";
 Diagnostics diag = testJsComp(jsCode);
 assertFalse(diag.hasError());
 }

 @Test
 public void testAssignmentOnLeftSideOfAssign() {
 String jsCode = "function Foo() { this = Object.prototype; }";
 Diagnostics diag = testJsComp(jsCode);
 assertTrue(diag.hasError());
 }

 @Test
 public void testAssignmentOnLeftSideOfAssignWithNestedAssign() {
 String jsCode = "function Foo() { (this.x = Object.prototype).y = 1; }";
 Diagnostics diag = testJsComp(jsCode);
 assertTrue(diag.hasError());
 }

 @Test
 public void testAssignmentOnRightSideOfAssign() {
 String jsCode = "function Foo() { this.x = Object.prototype; }";
 Diagnostics diag = testJsComp(jsCode);
 assertFalse(diag.hasError());
 }

 @Test
 public void testAssignmentOnRightSideOfAssignWithNestedAssign() {
 String jsCode = "function Foo() { this.x = { y: Object.prototype }; }";
 Diagnostics diag = testJsComp(jsCode);
 assertFalse(diag.hasError());
 }

 @Test
 public void testAssignmentToPrototypeWithGetter() {
 String jsCode = "function Foo() { Object.prototype.getX = function() { return this; }; Object.prototype.x = 1; }";
 Diagnostics diag = testJsComp(jsCode);
 assertFalse(diag.hasError());
 }

 @Test
 public void testAssignmentToPrototypeWithGetterAndSetter() {
 String jsCode = "function Foo() { Object.prototype.getX = function() { return this; }; Object.prototype.setX = function(x) { this.x = x; }; }";
 Diagnostics diag = testJsComp(jsCode);
 assertFalse(diag.hasError());
 }

 @Test
 public void testAssignmentToPrototypeWithGetterAndSetterAnnotated() {
 String jsCode = "function Foo() { @this Object.prototype.getX = function() { return this; }; @this Object.prototype.setX = function(x) { this.x = x; }; }";
 Diagnostics diag = testJsComp(jsCode);
 assertFalse(diag.hasError());
 }

 private Diagnostics testJsComp(String jsCode) {
 try {
 compiler.compile(Parser.parseJs(jsCode), null, null);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 return compiler.getDiagnostics();
 }

 @After
 public void tearDown() {
 compiler = null;
 }

}