import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;

import java.io.File;
import java.util.Collections;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private AbstractCompiler compiler;
  private CompilerInput input;
  private CompilerOptions options;

  @org.junit.Before
  public void setUp() {
    options = new CompilerOptions();
    options.setAssumeFunctionCallsAreDeclarations(true);
    options.setEmitWarningOnly(true);
    options.setCompilerFlags(Collections.singletonList("-W:unreachable"));
    compiler = new Compiler(options);
    input = new CompilerInput("test.js", new File("test.js").getAbsolutePath(), null);
  }

  @org.junit.Test
  public void testGuessCJSModuleName() {
    String filename = "path/to/module.js";
    String expected = "module$path$to$module";
    assertEquals(expected, guessCJSModuleName(filename));
  }

  @org.junit.Test
  public void testProcessCommonJSModules() {
    input.setSrc(ProcessCommonJSModules.class.getResource("/test.js").getFile());
    compiler.parseInitialSource(input, null);
    compiler.process(input, null);
    Node root = input.getRootNode();
    Node moduleExports = root.getFirstChild().getChildAtIndex(1);
    assertEquals("module$exports", moduleExports.getString());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithRequireCall() {
    String requiredFilename = "path/to/required.js";
    String currentFilename = "path/to/main.js";

    input.setSrc(ProcessCommonJSModules.class.getResource("/test.js").getFile());
    compiler.parseInitialSource(input, null);
    compiler.process(input, null);
    Node script = input.getRootNode().getFirstChild();
    Node requireCall = script.getChildAtIndex(1);

    String expectedModuleName = "module$required";
    String actualModuleName = toModuleName(requiredFilename, currentFilename);
    assertEquals(expectedModuleName, actualModuleName);

    Node moduleRef = requireCall.getChildAtIndex(1);
    assertEquals(expectedModuleName, moduleRef.getString());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithEmptySourceName() {
    input.setSrc("");
    compiler.parseInitialSource(input, null);
    compiler.process(input, null);
    assertNull(getModule());
  }

  @org.junit.Test(expected = URISyntaxException.class)
  public void testProcessCommonJSModulesWithInvalidURI() {
    String requiredFilename = "data:text/javascript;base64,xxx";
    String currentFilename = "path/to/main.js";

    input.setSrc(ProcessCommonJSModules.class.getResource("/test.js").getFile());
    compiler.parseInitialSource(input, null);
    compiler.process(input, null);
    Node script = input.getRootNode().getFirstChild();
    Node requireCall = script.getChildAtIndex(1);

    String expectedModuleName = "module$required";
    String actualModuleName = toModuleName(requiredFilename, currentFilename);
    assertEquals(expectedModuleName, actualModuleName);

    Node moduleRef = requireCall.getChildAtIndex(1);
    assertEquals(expectedModuleName, moduleRef.getString());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithRelativeRequireCall() {
    String requiredFilename = "../path/to/required.js";
    String currentFilename = "path/to/main.js";

    input.setSrc(ProcessCommonJSModules.class.getResource("/test.js").getFile());
    compiler.parseInitialSource(input, null);
    compiler.process(input, null);
    Node script = input.getRootNode().getFirstChild();
    Node requireCall = script.getChildAtIndex(1);

    String expectedModuleName = "module$required";
    String actualModuleName = toModuleName(requiredFilename, currentFilename);
    assertEquals(expectedModuleName, actualModuleName);

    Node moduleRef = requireCall.getChildAtIndex(1);
    assertEquals(expectedModuleName, moduleRef.getString());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithModuleExports() {
    String moduleName = "module$name";

    input.setSrc(ProcessCommonJSModules.class.getResource("/test.js").getFile());
    compiler.parseInitialSource(input, null);
    compiler.process(input, null);
    Node script = input.getRootNode().getFirstChild();
    Node moduleExports = script.getChildAtIndex(1).getChildAtIndex(1);

    Node exportNameNode = moduleExports.getChildAtIndex(0);
    String exportName = exportNameNode.getString();
    assertEquals("name$name", exportName);

    Node exportValueNode = moduleExports.getChildAtIndex(1);
    Node exportValue = exportValueNode.getFirstChild();
    assertEquals("value", exportValue.getString());

    Node module = getModule();
    Set<String> exportsSet = module.getProvidedNames();
    assertTrue(exportsSet.contains("name$name"));
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithScriptNode() {
    input.setSrc(ProcessCommonJSModules.class.getResource("/test.js").getFile());
    compiler.parseInitialSource(input, null);
    compiler.process(input, null);
    Node root = input.getRootNode();
    Node script = root.getChildAtIndex(0);

    assertEquals(1, script.getChildCount());

    Node funcDecl = script.getChildAtIndex(0);
    assertEquals(Node.FUNCTION_DECL, funcDecl.getType());
    assertEquals("function$name", funcDecl.getQualifiedName());
  }

  private JSModule getModule() {
    return ((JSModule) input.getCompilerPassResults().get(ProcessCommonJSModules.class));
  }
}