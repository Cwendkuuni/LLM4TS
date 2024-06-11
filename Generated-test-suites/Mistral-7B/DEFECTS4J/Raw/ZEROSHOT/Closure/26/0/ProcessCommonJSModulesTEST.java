import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeFactory.createCall;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createLiteral;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createObjectLiteral;
import static com.google.javascript.rhino.NodeFactory.createProperty;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.NodeFactory.createVar;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws URISyntaxException {
    String inputFileName = "input.js";
    String outputFileName = "output.js";

    // Set up input file.
    CompilerInput input = new CompilerInput(new File(inputFileName));
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.init(Sets.newHashSet("--warning_level=VERBOSE"), null, null);
    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "");

    // Create test input file.
    Node root = createNode("function main() {}", createName("main"));
    Node script = createNode("module.exports = 123;", createName("script"));
    root.addChildToBack(script);

    // Process test input.
    p.process(input.getExterns(), root);

    // Check output file.
    Node expectedOutput = createNode("(function(){\n" +
        "  'use strict';\n" +
        "  var module = {\n" +
        "    module$exports: 123,\n" +
        "    module$id: '" + toModuleName(inputFileName) + "'\n" +
        "  };\n" +
        "  if (module.module$exports) {\n" +
        "    module = module.module$exports;\n" +
        "  }\n" +
        "  var main = function() {}\n" +
        "  if (typeof module.exports === 'object' && module.exports) {\n" +
        "    module.exports = main;\n" +
        "  }\n" +
        "  if (typeof define === 'function' && define.amd) {\n" +
        "    define(function() { return main; });\n" +
        "  }\n" +
        "  if (typeof exports !== 'undefined') {\n" +
        "    exports.main = main;\n" +
        "  }\n" +
        "})()\n");

    assertEquals(expectedOutput.toSource(), input.getOutputFile().getContentsAsString());
  }

  private Node createNode(String code, Node name) {
    CompilerInput ci = new CompilerInput(new File("test.js"));
    NodeFactory factory = ci.getFactory();
    Node node = factory.parse(code);
    node.setSourceFileName("test.js");
    node.setParent(createVar("root", createObjectLiteral(), null));
    node.setName(name);
    return node;
  }

  private String toModuleName(String filename) {
    return ProcessCommonJSModules.toModuleName(filename);
  }
}