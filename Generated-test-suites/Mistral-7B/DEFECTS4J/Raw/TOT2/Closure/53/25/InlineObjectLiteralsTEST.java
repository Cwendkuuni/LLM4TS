import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

 private AbstractCompiler compiler;
 private Supplier<String> safeNameIdSupplier;

 @Before
 public void setup() {
 compiler = new AbstractCompiler();
 safeNameIdSupplier = () -> "test_id";
 }

 @Test
 public void testInlineObjectLiterals() {
 // Given a simple object literal that can be inlined
 String javaCode = "function test() {" +
 "  var obj = {" +
 "    a: 1," +
 "    b: {'c': 2}" +
 "  };" +
 "  return obj.a + obj.b.c;" +
 "}";

 Node root = new Node(Token.PROGRAM, null);
 Node externs = new Node(Token.PROGRAM, null);
 compiler.parse(externs, javaCode, root);

 InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
 compiler, safeNameIdSupplier);
 inlineObjectLiterals.process(externs, root);

 // When the InlineObjectLiterals pass is applied
 Node result = root.getLastChild();

 // Then the object literal should be inlined and evaluated to 3
 assertEquals(3, result.evaluateToNumber());

 // Given an object literal with a cycle reference
 javaCode = "function test() {" +
 "  var obj1 = {" +
 "    a: {'b': obj1}" +
 "  };" +
 "  return obj1.a.b;" +
 "}";

 root = new Node(Token.PROGRAM, null);
 externs = new Node(Token.PROGRAM, null);
 compiler.parse(externs, javaCode, root);

 inlineObjectLiterals = new InlineObjectLiterals(
 compiler, safeNameIdSupplier);
 inlineObjectLiterals.process(externs, root);

 // When the InlineObjectLiterals pass is applied
 result = root.getLastChild();

 // Then a runtime error should be thrown due to the cycle reference
 assertNull(result);
 }

 @Test
 public void testInlineObjectLiteralsWithPropertyAccess() {
 // Given an object literal that is used as a property in an assignment
 String javaCode = "function test() {" +
 "  var obj = {" +
 "    a: {'b': 1}" +
 "  };" +
 "  obj.c = obj.a.b;" +
 "  return obj.c;" +
 "}";

 Node root = new Node(Token.PROGRAM, null);
 Node externs = new Node(Token.PROGRAM, null);
 compiler.parse(externs, javaCode, root);

 InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
 compiler, safeNameIdSupplier);
 inlineObjectLiterals.process(externs, root);

 // When the InlineObjectLiterals pass is applied
 Node result = root.getLastChild();

 // Then the object literal should be inlined and evaluated to 1
 assertEquals(1, result.evaluateToNumber());
 }

 @Test
 public void testInlineObjectLiteralsWithNestedObjects() {
 // Given an object literal with nested objects
 String javaCode = "function test() {" +
 "  var obj = {" +
 "    a: {" +
 "      b: 1," +
 "      c: {'d': 2}" +
 "    }," +
 "    e: 3" +
 "  };" +
 "  return obj.a.b + obj.a.c.d + obj.e;" +
 "}";

 Node root = new Node(Token.PROGRAM, null);
 Node externs = new Node(Token.PROGRAM, null);
 compiler.parse(externs, javaCode, root);

 InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
 compiler, safeNameIdSupplier);
 inlineObjectLiterals.process(externs, root);

 // When the InlineObjectLiterals pass is applied
 Node result = root.getLastChild();

 // Then the object literal should be inlined and evaluated to 6
 assertEquals(6, result.evaluateToNumber());
 }
}