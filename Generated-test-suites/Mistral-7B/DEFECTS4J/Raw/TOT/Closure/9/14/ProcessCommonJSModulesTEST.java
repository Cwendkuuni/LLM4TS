import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.rhino.NodeOperations;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String input = "// Test input file\n" +
        "var a = require('module1');\n" +
        "var b = require('module2');\n" +
        "function test() {\n" +
        "  a.func1();\n" +
        "  b.func2();\n" +
        "}\n" +
        "exports.test = test;\n";

    String module1Input = "// module1\n" +
        "exports.func1 = function() {\n" +
        "  console.log('Module 1 function 1');\n" +
        "}\n";

    String module2Input = "// module2\n" +
        "exports.func2 = function() {\n" +
        "  console.log('Module 2 function 2');\n" +
        "}\n";

    CompilerInput inputCompiled = new CompilerInput();
    inputCompiled.setSourceContent("test.js", input);
    CompilerInput module1Compiled = new CompilerInput();
    module1Compiled.setSourceContent("module1.js", module1Input);
    CompilerInput module2Compiled = new CompilerInput();
    module2Compiled.setSourceContent("module2.js", module2Input);

    Processor processor = new Processor();
    ProcessCommonJSModules processorPass = new ProcessCommonJSModules(processor, ".");

    // Process the input file and the required modules
    processor.emitErrorMessagesTo(new PrintWriter(System.out));
    processor.compile(inputCompiled, Sets.newHashSet(module1Compiled, module2Compiled));

    // Check that the required modules were processed and emitted correctly
    Node testNode = NodeOperations.parseTree(processor.getJsOutput());
    Node testFunctionNode = testNode.getFirstChild();
    assertNotNull(testFunctionNode);
    assertEquals("test", testFunctionNode.getName());

    Node aRequireNode = testNode.getChildAtIndex(1);
    assertInstanceOf(Node.CallExpr.class, aRequireNode);
    Node aModuleNameNode = aRequireNode.getChildAtIndex(1);
    assertEquals("module1", aModuleNameNode.getString());

    Node bRequireNode = testNode.getChildAtIndex(2);
    assertInstanceOf(Node.CallExpr.class, bRequireNode);
    Node bModuleNameNode = bRequireNode.getChildAtIndex(1);
    assertEquals("module2", bModuleNameNode.getString());

    // Check that the functions from the required modules were imported correctly
    Node aFunc1Node = NodeOperations.getDeepChildByUniqueName(testNode, "a.func1");
    assertInstanceOf(Node.CallExpr.class, aFunc1Node);
    Node aFunc1CallNode = aFunc1Node.getChildAtIndex(0);
    assertEquals("func1", aFunc1CallNode.getName());

    Node bFunc2Node = NodeOperations.getDeepChildByUniqueName(testNode, "b.func2");
    assertInstanceOf(Node.CallExpr.class, bFunc2Node);
    Node bFunc2CallNode = bFunc2Node.getChildAtIndex(0);
    assertEquals("func2", bFunc2CallNode.getName());

    // Check that the optional 'goog.provide' and 'goog.require' statements were emitted
    Node googProvideNode = NodeOperations.getDeepChildByUniqueName(testNode, "goog.provide");
    assertInstanceOf(Node.CallExpr.class, googProvideNode);
    Node googProvideCallNode = googProvideNode.getChildAtIndex(0);
    assertEquals("provide", googProvideCallNode.getName());

    Node googRequireNode = NodeOperations.getDeepChildByUniqueName(testNode, "goog.require");
    assertInstanceOf(Node.CallExpr.class, googRequireNode);
    Node googRequireArgsNode = googRequireNode.getChildAtIndex(0);
    NodeList argsNodeList = googRequireArgsNode.getChildren();
    assertEquals(2, argsNodeList.getLength());
    Node module1ArgNode = argsNodeList.get(0);
    assertEquals("module1", module1ArgNode.getString());
    Node module2ArgNode = argsNodeList.get(1);
    assertEquals("module2", module2ArgNode.getString());
  }

  @Test
  public void testProcessCommonJSModulesWithErrors() throws Exception {
    String input = "// Test input file\n" +
        "var a = require('module1'); // Misspelled module name\n" +
        "var b = require('module2'); // Module not found\n" +
        "function test() {\n" +
        "  a.func1(); // Function not defined\n" +
        "  b.func2(); // Function not defined\n" +
        "}\n" +
        "exports.test = test;\n";

    CompilerInput inputCompiled = new CompilerInput();
    inputCompiled.setSourceContent("test.js", input);

    Processor processor = new Processor();
    ProcessCommonJSModules processorPass = new ProcessCommonJSModules(processor, ".");

    // Process the input file and the required modules
    List<JSError> errors = new ArrayList<>();
    processor.setErrorReporter(new ErrorReporter() {
      @Override
      public void reportError(int line, int lineNumber, int column, int endColumn,
          String message, String detail, String sourceName) {
        errors.add(new JSError(line, lineNumber, column, endColumn, message));
      }
    });

    processor.emitErrorMessagesTo(new PrintWriter(System.out));
    processor.compile(inputCompiled, Collections.emptySet());

    // Check that errors were reported correctly
    assertEquals(4, errors.size());
    JSError aRequireError = errors.get(0);
    assertEquals(1, aRequireError.getLineNumber());
    assertEquals("'module1' is not defined", aRequireError.getMessage());

    JSError bRequireError = errors.get(1);
    assertEquals(2, bRequireError.getLineNumber());
    assertEquals("'module2' is not defined", bRequireError.getMessage());

    JSError aFunc1Error = errors.get(2);
    assertEquals(3, aFunc1Error.getLineNumber());
    assertEquals("'a.func1' is not a function", aFunc1Error.getMessage());

    JSError bFunc2Error = errors.get(3);
    assertEquals(3, bFunc2Error.getLineNumber());
    assertEquals("'b.func2' is not a function", bFunc2Error.getMessage());
  }

  @Test
  public void testProcessCommonJSModulesRelativeRequires() throws Exception {
    String input = "// Test input file\n" +
        "var a = require('./module1');\n" +
        "var b = require('../module2');\n" +
        "function test() {\n" +
        "  a.func1();\n" +
        "  b.func2();\n" +
        "}\n" +
        "exports.test = test;\n";

    String module1Input = "// module1\n" +
        "exports.func1 = function() {\n" +
        "  console.log('Module 1 function 1');\n" +
        "}\n";

    String module2Input = "// module2\n" +
        "exports.func2 = function() {\n" +
        "  console.log('Module 2 function 2');\n" +
        "}\n";

    CompilerInput inputCompiled = new CompilerInput();
    inputCompiled.setSourceContent("test.js", input);
    CompilerInput module1Compiled = new CompilerInput();
    module1Compiled.setSourceContent("module1.js", module1Input);
    CompilerInput module2Compiled = new CompilerInput();
    module2Compiled.setSourceContent("../module2.js", module2Input);

    Processor processor = new Processor();
    ProcessCommonJSModules processorPass = new ProcessCommonJSModules(processor, ".");

    // Process the input file and the required modules
    processor.emitErrorMessagesTo(new PrintWriter(System.out));
    processor.compile(inputCompiled, Sets.newHashSet(module1Compiled, module2Compiled));

    // Check that the functions from the required modules were imported correctly
    Node testNode = NodeOperations.parseTree(processor.getJsOutput());
    Node aRequireNode = testNode.getChildAtIndex(1);
    assertInstanceOf(Node.CallExpr.class, aRequireNode);
    Node aModuleNameNode = aRequireNode.getChildAtIndex(1);
    assertEquals("module1", aModuleNameNode.getString());

    Node bRequireNode = testNode.getChildAtIndex(2);
    assertInstanceOf(Node.CallExpr.class, bRequireNode);
    Node bModuleNameNode = bRequireNode.getChildAtIndex(1);
    assertEquals("module2", bModuleNameNode.getString());

    // Check that the optional 'goog.provide' and 'goog.require' statements were emitted
    Node googProvideNode = NodeOperations.getDeepChildByUniqueName(testNode, "goog.provide");
    assertInstanceOf(Node.CallExpr.class, googProvideNode);
    Node googProvideCallNode = googProvideNode.getChildAtIndex(0);
    assertEquals("provide", googProvideCallNode.getName());

    Node googRequireNode = NodeOperations.getDeepChildByUniqueName(testNode, "goog.require");
    assertInstanceOf(Node.CallExpr.class, googRequireNode);
    Node googRequireArgsNode = googRequireNode.getChildAtIndex(0);
    NodeList argsNodeList = googRequireArgsNode.getChildren();
    assertEquals(2, argsNodeList.getLength());
    Node module1ArgNode = argsNodeList.get(0);
    assertEquals("module1", module1ArgNode.getString());
    Node module2ArgNode = argsNodeList.get(1);
    assertEquals("module2", module2ArgNode.getString());
  }
}