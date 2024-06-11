import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.DiagnosticPosition;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

public class CheckGlobalThisTest {

 private static final String TEST_CASE_1 =
     "function Foo() { this.prop = 1; }";
 private static final String TEST_CASE_2 =
     "var x = function() { this.prop = 1; }; x.prototype.bar = function() {};";
 private static final String TEST_CASE_3 =
     "function Foo() { } function Foo.prototype.bar = function() { this.prop = 1; };";
 private static final String TEST_CASE_4 =
     "var x = function() { this.prop = 1; }; x.prop = 2;";

 @Test
 public void testCheckGlobalThis() {
   AbstractCompiler compiler = new AbstractCompiler();
   Checker checker = new Checker(compiler, new NodeTraversal.CallbackManager(), null, null, null);
   CheckGlobalThis globalThisChecker = new CheckGlobalThis(compiler, new CheckLevel(WARNING));
   checker.addPass(globalThisChecker);

   for (String testCase : new String[] { TEST_CASE_1, TEST_CASE_2, TEST_CASE_3, TEST_CASE_4 }) {
     SourceFile sourceFile = new SourceFile("test.js", testCase);
     Node rootNode = Parser.parseCompilationUnit(sourceFile, null, null);
     NodeFactory nodeFactory = sourceFile.getFactory();

     checker.check(rootNode, null, null);

     DiagnosticPosition position = null;
     for (DiagnosticType diagnosticType : checker.getDiagnostics()) {
       if (diagnosticType.getCode().equals("JSC_USED_GLOBAL_THIS")) {
         position = diagnosticType.getPosition();
         break;
       }
     }

     if (position == null) {
       System.out.println("Test case '" + testCase + "' passed.");
       continue;
     }

     Node node = findNodeAtPosition(rootNode, position);
     assertEquals("Unexpected node type", Node.THIS, node.getType());

     System.out.println("Test case '" + testCase + "' failed: expected global 'this' check to flag line "
         + (position.getLineNumber() + 1) + " column " + (position.getCharacterOffsetInLine() + 1)
         + " but didn't.");
   }
 }

 private Node findNodeAtPosition(Node rootNode, DiagnosticPosition position) {
   NodeTraversal traversal = new NodeTraversal(rootNode, new NodeTraversal.Callback() {
     public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
       if (n.getLineNumber() == position.getLineNumber()
           && n.getCharacterOffset() >= position.getCharacterOffsetInLine()
           && n.getCharacterOffset() + n.getLength() <= position.getCharacterOffsetInLine() + position.getColumnWidth()) {
         return false;
       }
       return true;
     }
   });
   return traversal.root;
 }
}