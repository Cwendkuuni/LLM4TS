import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckerFactory.checkType;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    // Test case 1: Safe usage of 'this' within a constructor
    String constructorCode = "function MyConstructor() { this.myProperty = 1; }";
    Diagnostics result = checkType(new Node(constructCode), checker);
    assertFalse(result.hasErrors());

    // Test case 2: Safe usage of 'this' within a prototype method
    String prototypeCode =
        "function MyObject() {}\n"
            + "MyObject.prototype.myMethod = function() { this.myProperty = 1; }";
    result = checkType(new Node(prototypeCode), checker);
    assertFalse(result.hasErrors());

    // Test case 3: Safe usage of 'this' within a function annotated with @this
    String thisAnnotatedCode = "@this function MyFunction() { this.myProperty = 1; }";
    result = checkType(new Node(thisAnnotatedCode), checker);
    assertFalse(result.hasErrors());

    // Test case 4: Safe usage of 'this' where there's no logical place to put a @this annotation
    String noAnnotationCode = "function MyFunction() { this.myProperty = 1; }";
    result = checkType(new Node(noAnnotationCode), checker);
    assertFalse(result.hasErrors());

    // Test case 5: Unsafe usage of 'this' on the left side of an assignment
    String assignmentCode = "var a = this; a.myProperty = 1;";
    result = checkType(new Node(assignmentCode), checker);
    assertTrue(result.getDiagnostics().size() > 0);

    // Test case 6: Unsafe usage of 'this' within a function outside of a constructor, prototype method, or annotated function
    String unsafeCode = "function MyFunction() { this.myProperty = 1; }";
    result = checkType(new Node(unsafeCode), checker);
    assertTrue(result.getDiagnostics().size() > 0);

    // Test case 7: Unsafe usage of 'this' within a function as the right side of an assignment
    String assignmentRightCode = "var a = function() {}; a = this;";
    result = checkType(new Node(assignmentRightCode), checker);
    assertTrue(result.getDiagnostics().size() > 0);
  }
}