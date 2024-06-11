import static org.junit.Assert.*;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Nodeops;

public class ProcessCommonJSModulesTest {
  @org.junit.Test
  public void testProcessCommonJSModules() {
    // Given
    Compiler compiler = new Compiler();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    Node root = Nodeops.parseTemplate(
        "require('./a').print();",
        "function print() { alert('Hello World'); }",
        "exports.print = print;");

    // When
    processor.process(null, root);

    // Then
    Node expected = Nodeops.parseTemplate(
        "goog.require('a');\n" +
        "goog.provide('a');\n" +
        "function print() { alert('Hello World'); }\n" +
        "a.print = print;",
        null, null);

    // Check the processed code matches the expected code
    assertEquals(expected.toSource(), root.toSource());
  }
}